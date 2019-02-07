package org.dieschnittstelle.jee.esa.ejb.client.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.client.Constants;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.EJBProxyFactory;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping.ShoppingSessionFacadeRemote;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

public class ShoppingSessionFacadeClient implements ShoppingBusinessDelegate {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ShoppingSessionFacadeClient.class);

	/*
	 * DONE use a proxy for the ShoppingSessionFacadeRemote interface
	 */
	private ShoppingSessionFacadeRemote ejbProxy;

	public ShoppingSessionFacadeClient() {
		/* DONE: instantiate the proxy using the EJBProxyFactory (see the other client classes) */
		this.ejbProxy = EJBProxyFactory.getInstance().getProxy(ShoppingSessionFacadeRemote.class, Constants.SHOPPING_SESSION_FACADE_BEAN_URI);
	}

	/* DONE: implement the following methods using the proxy */

	@Override
	public void setTouchpoint(AbstractTouchpoint touchpoint) {
		ejbProxy.setTouchpoint(touchpoint);
	}

	@Override
	public void setCustomer(Customer customer) {
		ejbProxy.setCustomer(customer);
	}

	@Override
	public void addProduct(AbstractProduct product, int units) {
		ejbProxy.addProduct(product, units);
	}

	@Override
	public void purchase() throws ShoppingException {
		ejbProxy.purchase();
	}

}
