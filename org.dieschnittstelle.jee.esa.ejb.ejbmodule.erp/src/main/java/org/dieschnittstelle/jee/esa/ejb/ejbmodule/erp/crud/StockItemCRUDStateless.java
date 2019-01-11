package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.ProductAtPosPK;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Stateless
@Local(StockItemCRUDLocal.class)
public class StockItemCRUDStateless implements StockItemCRUDLocal {
    @PersistenceContext(unitName = "erp_PU")
    private EntityManager myEntityManager;

    @Override
    public StockItem createStockItem(StockItem item) {
        return myEntityManager.merge(item);
    }

    @Override
    public StockItem readStockItem(IndividualisedProductItem prod, PointOfSale pos) {
        return myEntityManager.find(StockItem.class, new ProductAtPosPK(prod, pos));
    }

    @Override
    public StockItem updateStockItem(StockItem item) {
        return myEntityManager.merge(item);
    }

    @Override
    public List<StockItem> readAllStockItems() {
        Query query = myEntityManager.createQuery("SELECT * FROM StockItem");
        return (List<StockItem>) query.getResultList();
    }

    @Override
    public List<StockItem> readStockItemsForProduct(IndividualisedProductItem prod) {
        Query query = myEntityManager.createQuery("SELECT * FROM StockItem item WHERE PRODUCT_ID = " + prod.getId());
        return query.getResultList();
    }

    @Override
    public List<StockItem> readStockItemsForPointOfSale(PointOfSale pos) {
        Query query = myEntityManager.createQuery("SELECT t FROM StockItem t WHERE t.pos = " + pos.getId());
        List<StockItem> listSI = query.getResultList();
        return listSI;
    }
}
