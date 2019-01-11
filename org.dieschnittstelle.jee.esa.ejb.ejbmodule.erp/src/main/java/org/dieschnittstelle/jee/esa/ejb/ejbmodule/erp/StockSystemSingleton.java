package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.PointOfSaleCRUDLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.StockItemCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Remote(StockSystemRemote.class)
@WebService(targetNamespace = "http://dieschnittstelle.org/jee/esa/jws", serviceName = "StockSystemWebService", endpointInterface = "org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote")
public class StockSystemSingleton implements StockSystemRemote {

    @EJB
    PointOfSaleCRUDLocal pointOfSaleCRUDLocal;

    @EJB
    StockItemCRUDLocal stockItemCRUDLocal;

    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
        StockItem si = stockItemCRUDLocal.readStockItem(product, pos);

        if (si == null) {
            si = new StockItem(product, pos, units);
            stockItemCRUDLocal.createStockItem(si);
        } else {
            si.setUnits(si.getUnits() + units);
            stockItemCRUDLocal.updateStockItem(si);
        }
    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        if (units >= 0) {
            PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
            StockItem si = stockItemCRUDLocal.readStockItem(product, pos);
            si.setUnits(si.getUnits() - units);
            stockItemCRUDLocal.updateStockItem(si);
        }
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
        List<IndividualisedProductItem> listProducts = new ArrayList<>();
        for (StockItem si : stockItemCRUDLocal.readStockItemsForPointOfSale(pos)) {
            listProducts.add(si.getProduct());
        }
        return listProducts;
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        List<PointOfSale> listPOS = pointOfSaleCRUDLocal.readAllPointsOfSale();
        List<IndividualisedProductItem> listProducts = new ArrayList<>();
        List<StockItem> stockItems = new ArrayList<>();
        for (PointOfSale pos : listPOS) {
            stockItems.addAll(stockItemCRUDLocal.readStockItemsForPointOfSale(pos));
        }
        for (StockItem si : stockItems) {
            if (!listProducts.contains(si.getProduct())) {
                listProducts.add(si.getProduct());
            }
        }

        return listProducts;
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
        StockItem si = stockItemCRUDLocal.readStockItem(product, pos);
        if (si == null) {
            return 0;
        }
        return si.getUnits();
    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        List<PointOfSale> listPOS = pointOfSaleCRUDLocal.readAllPointsOfSale();
        List<StockItem> listSI= new ArrayList<>();
        int units = 0;
        for (PointOfSale pos : listPOS) {
            listSI.addAll(stockItemCRUDLocal.readStockItemsForPointOfSale(pos));
        }
        for (StockItem si : listSI) {
            if (si.getProduct().equals(product)) {
                units += si.getUnits();
            }
        }
        return units;
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        List<PointOfSale> listPOS = pointOfSaleCRUDLocal.readAllPointsOfSale();
        List<StockItem> listSI = new ArrayList<>();
        List<Long> listItems = new ArrayList<>();
        for (PointOfSale pos : listPOS) {
            listSI.addAll(stockItemCRUDLocal.readStockItemsForPointOfSale(pos));
        }
        for (StockItem si : listSI) {
            if (si.getProduct().equals(product)) {
                listItems.add(si.getPos().getId());
            }
        }
        return listItems;
    }
}
