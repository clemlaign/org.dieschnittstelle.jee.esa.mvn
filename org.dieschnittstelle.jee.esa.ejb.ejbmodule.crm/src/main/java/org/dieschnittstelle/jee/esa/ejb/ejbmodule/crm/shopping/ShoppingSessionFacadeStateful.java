package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.CampaignTrackingLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.CustomerTrackingLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingCartLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.crm.CustomerTransaction;
import org.dieschnittstelle.jee.esa.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.Campaign;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.ProductBundle;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import java.util.List;

@Stateful
public class ShoppingSessionFacadeStateful implements ShoppingSessionFacadeRemote{
    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShoppingSessionFacadeStateful.class);

    /*
     * the three beans that are used
     */
    @EJB
    private ShoppingCartLocal shoppingCart;

    @EJB
    private CustomerTrackingLocal customerTracking;

    @EJB
    private CampaignTrackingLocal campaignTracking;

    @EJB
    private ProductCRUDLocal productCRUDLocal;

    @EJB
    private StockSystemLocal stockSystemLocal;

    /**
     * the customer
     */
    private Customer customer;

    /**
     * the touchpoint
     */
    private AbstractTouchpoint touchpoint;

    @Override
    public void setTouchpoint(AbstractTouchpoint touchpoint) {
        this.touchpoint = touchpoint;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void addProduct(AbstractProduct product, int units) {
        this.shoppingCart.addItem(new ShoppingCartItem(product.getId(), units, product instanceof Campaign));
    }

    /*
     * verify whether campaigns are still valid
     */
    public void verifyCampaigns() throws ShoppingException {
        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException("cannot verify campaigns! No touchpoint has been set!");
        }

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {
            if (item.isCampaign()) {
                int availableCampaigns = this.campaignTracking.existsValidCampaignExecutionAtTouchpoint(
                        item.getErpProductId(), this.touchpoint);
                logger.info("got available campaigns for product " + item.getErpProductId() + ": "
                        + availableCampaigns);
                // we check whether we have sufficient campaign items available
                if (availableCampaigns < item.getUnits()) {
                    throw new ShoppingException("verifyCampaigns() failed for productBundle " + item
                            + " at touchpoint " + this.touchpoint + "! Need " + item.getUnits()
                            + " instances of campaign, but only got: " + availableCampaigns);
                }
            }
        }
    }

    @Override
    public void purchase() throws ShoppingException {
        logger.info("purchase()");

        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException(
                    "cannot commit shopping session! Either customer or touchpoint has not been set: " + this.customer
                            + "/" + this.touchpoint);
        }

        // verify the campaigns
        verifyCampaigns();

        // remove the products from stock
        checkAndRemoveProductsFromStock();

        // then we add a new customer transaction for the current purchase
        List<ShoppingCartItem> products = this.shoppingCart.getItems();
        CustomerTransaction transaction = new CustomerTransaction(this.customer, this.touchpoint, products);
        transaction.setCompleted(true);
        customerTracking.createTransaction(transaction);

        logger.info("purchase(): done.\n");
    }

    /*
     * to be implemented as server-side method for PAT2
     */
    private void checkAndRemoveProductsFromStock() {
        logger.info("checkAndRemoveProductsFromStock");

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {

            // TODO: ermitteln Sie das AbstractProduct für das gegebene ShoppingCartItem. Nutzen Sie dafür dessen erpProductId und die ProductCRUD EJB
            AbstractProduct abstractProduct = productCRUDLocal.readProduct(item.getErpProductId());

            if (item.isCampaign()) {
                this.campaignTracking.purchaseCampaignAtTouchpoint(item.getErpProductId(), this.touchpoint,
                        item.getUnits());


                Campaign campaign = (Campaign) abstractProduct;
                for (ProductBundle productBundle : campaign.getBundles()) {
                    int count = productBundle.getUnits() * item.getUnits();
                    int available = stockSystemLocal.getUnitsOnStock(productBundle.getProduct(), touchpoint.getErpPointOfSaleId());
                    if (available >= count) {
                        stockSystemLocal.removeFromStock(productBundle.getProduct(), touchpoint.getErpPointOfSaleId(), count);
                    }
                }

            } else {

                IndividualisedProductItem individualisedProductItem = (IndividualisedProductItem) abstractProduct;
                int available = stockSystemLocal.getUnitsOnStock(individualisedProductItem, touchpoint.getErpPointOfSaleId());
                if (available >= item.getUnits()) {
                    stockSystemLocal.removeFromStock(individualisedProductItem, touchpoint.getErpPointOfSaleId(), item.getUnits());
                }

            }

        }
    }
}
