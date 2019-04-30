package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.Orders;
import com.dlr.ciscoware_ws.v1.resources.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-30T17:18:52")
@StaticMetamodel(ProductOrder.class)
public class ProductOrder_ { 

    public static volatile SingularAttribute<ProductOrder, Integer> quantity;
    public static volatile SingularAttribute<ProductOrder, Product> productId;
    public static volatile SingularAttribute<ProductOrder, Orders> orderId;
    public static volatile SingularAttribute<ProductOrder, Integer> id;

}