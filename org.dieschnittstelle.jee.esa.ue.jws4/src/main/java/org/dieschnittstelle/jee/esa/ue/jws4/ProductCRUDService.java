package org.dieschnittstelle.jee.esa.ue.jws4;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.GenericCRUDExecutor;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.Campaign;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.ProductType;

/*
 * UE JWS4: machen Sie die Funktionalitaet dieser Klasse als Web Service verfuegbar und verwenden Sie fuer 
 * die Umetzung der Methoden die Instanz von GenericCRUDExecutor<AbstractProduct>,
 * die Sie aus dem ServletContext auslesen koennen
 */
@WebService(targetNamespace = "http://dieschnittstelle.org/jee/esa/jws", name = "IProductCRUDService", serviceName = "ProductCRUDWebService", portName = "ProductCRUDPort")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public class ProductCRUDService {


	@Resource
	private WebServiceContext webServiceContext;

	@PostConstruct
	@WebMethod(exclude = true)
	public void initialiseContext() {
	}

	public List<AbstractProduct> readAllProducts() {
		return getGenericCRUDExecutor().readAllObjects();
	}

	public AbstractProduct createProduct(AbstractProduct product) {
		return getGenericCRUDExecutor().createObject(product);
	}

	public AbstractProduct updateProduct(AbstractProduct update) {
		return getGenericCRUDExecutor().updateObject(update);
	}

	public boolean deleteProduct(long id) {
		return getGenericCRUDExecutor().deleteObject(id);
	}

	public IndividualisedProductItem readProduct(long id) {
		return (IndividualisedProductItem) getGenericCRUDExecutor().readObject(id);
	}


	private GenericCRUDExecutor<AbstractProduct> getGenericCRUDExecutor() {
		// obtain the CRUD executor from the servlet context
		return (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) webServiceContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT)).getAttribute("productCRUD");
	}

}
