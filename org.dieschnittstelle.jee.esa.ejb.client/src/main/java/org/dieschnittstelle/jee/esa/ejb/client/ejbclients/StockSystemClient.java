package org.dieschnittstelle.jee.esa.ejb.client.ejbclients;

import java.util.List;

import org.dieschnittstelle.jee.esa.ejb.client.Constants;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

public class StockSystemClient implements StockSystemRemote {

	private StockSystemRemote ejbProxy;
	
	public StockSystemClient() throws Exception {
		// TODO: obtain a proxy specifying the ejb interface and uri. Let all subsequent methods use the proxy.
		this.ejbProxy = EJBProxyFactory.getInstance().getProxy(null, Constants.STOCKITEM_CRUD_BEAN_URI);
	}
	
	
	@Override
	public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
		this.ejbProxy.addToStock(product, pointOfSaleId, units);
	}

	@Override
	public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId,
			int units) {
		this.ejbProxy.removeFromStock(product, pointOfSaleId, units);
	}

	@Override
	public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
		return this.ejbProxy.getProductsOnStock(pointOfSaleId);

	}

	@Override
	public List<IndividualisedProductItem> getAllProductsOnStock() {
		return this.ejbProxy.getAllProductsOnStock();
		//return null;
	}

	@Override
	public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
		return this.ejbProxy.getUnitsOnStock(product, pointOfSaleId);
		//return 0;
	}

	@Override
	public int getTotalUnitsOnStock(IndividualisedProductItem product) {
		return this.ejbProxy.getTotalUnitsOnStock(product);
		//return 0;
	}

	@Override
	public List<Long> getPointsOfSale(IndividualisedProductItem product) {
		return this.ejbProxy.getPointsOfSale(product);
		//return null;
	}


}
