package org.dieschnittstelle.jee.esa.basics;


import org.dieschnittstelle.jee.esa.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.jee.esa.basics.annotations.DisplayAs;
import org.dieschnittstelle.jee.esa.basics.annotations.StockItemProxyImpl;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.dieschnittstelle.jee.esa.basics.reflection.ReflectedStockItemBuilder.getAccessorNameForField;
import static org.dieschnittstelle.jee.esa.utils.Utils.*;

public class ShowAnnotations {

	public static void main(String[] args) {
		// we initialise the collection
		StockItemCollection collection = new StockItemCollection(
				"stockitems_annotations.xml", new AnnotatedStockItemBuilder());
		// we load the contents into the collection
		collection.load();

		for (IStockItem consumable : collection.getStockItems()) {
			;
			showAttributes(((StockItemProxyImpl)consumable).getProxiedObject());
		}

		// we initialise a consumer
		Consumer consumer = new Consumer();
		// ... and let them consume
		consumer.doShopping(collection.getStockItems());
	}

	/*
	 * UE BAS2 
	 */
	private static void showAttributes(Object consumable)  {

		show("class is: " + consumable.getClass());
        try {
            Class myclass = Class.forName(consumable.getClass().getName());
            StringBuilder result = new StringBuilder();
            result.append("{").append(consumable.getClass().getSimpleName()).append(" ");
            for (Field field : myclass.getDeclaredFields()){
                field.setAccessible(true);

                String getter = getAccessorNameForField("get", field.getName());
                show("found getter name %s", getter);

                Method myMethod = myclass.getMethod(getter);
                Annotation annotation = myMethod.getAnnotation(DisplayAs.class);
                if (annotation != null){
                    String myField = ((DisplayAs) annotation).value();
                    show("My annotated Field is : %s", myField);
                    result.append(myField).append(":").append(field.get(consumable)).append(", ");
                } else {

                    result.append(field.getName()).append(":").append(field.get(consumable)).append(", ");
                    //show("My not annoted Field is : % ", field.getName());
                }

            }
            result.deleteCharAt(result.length()-1).deleteCharAt(result.length() -1);
            result.append("}");
            show(result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

}
