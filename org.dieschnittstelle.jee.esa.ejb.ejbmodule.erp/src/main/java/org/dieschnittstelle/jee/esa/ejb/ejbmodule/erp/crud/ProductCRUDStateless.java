package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Stateless
@Remote(ProductCRUDRemote.class)
public class ProductCRUDStateless implements ProductCRUDRemote, ProductCRUDLocal {

    @PersistenceContext(unitName = "erp_PU")
    private EntityManager myEntityManager;

    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {
        myEntityManager.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        return myEntityManager.createQuery("SELECT p FROM AbstractProduct AS p").getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        myEntityManager.merge(update);
        return update;
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return myEntityManager.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        AbstractProduct result = myEntityManager.find(AbstractProduct.class, productID);
        if(result == null)
            return false;
        myEntityManager.remove(result);
        return true;
    }
}
