package org.dieschnittstelle.jee.esa.jrs;

import org.dieschnittstelle.jee.esa.entities.GenericCRUDExecutor;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.util.List;

/*
UE JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	private GenericCRUDExecutor<AbstractProduct> serviceCRUD;

	//constructeurs

	public ProductCRUDServiceImpl(@Context ServletContext servletContext) {

		// receive CRUD executor from servlet context
		serviceCRUD = (GenericCRUDExecutor<AbstractProduct>)servletContext.getAttribute("productCRUD");
	}

	@Override
	public AbstractProduct createProduct(
			AbstractProduct prod) {
		return (AbstractProduct) serviceCRUD.createObject(prod);
	}

	@Override
	public List<AbstractProduct> readAllProducts() {
		return (List)serviceCRUD.readAllObjects();
	}

	@Override
	public AbstractProduct updateProduct(long id,
			AbstractProduct update) {
        return (AbstractProduct) serviceCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {
        return serviceCRUD.deleteObject(id);
	}

	@Override
	public AbstractProduct readProduct(long id) {
        return (AbstractProduct) serviceCRUD.readObject(id);
	}
	
}
