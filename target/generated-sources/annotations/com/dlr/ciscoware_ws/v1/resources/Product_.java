package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.Inventory;
import com.dlr.ciscoware_ws.v1.resources.ProductOrder;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-27T21:39:04")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile CollectionAttribute<Product, ProductOrder> productOrderCollection;
    public static volatile SingularAttribute<Product, Double> price;
    public static volatile CollectionAttribute<Product, Inventory> inventoryCollection;
    public static volatile SingularAttribute<Product, String> name;
    public static volatile SingularAttribute<Product, String> description;
    public static volatile SingularAttribute<Product, Integer> id;

}