package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.Branch;
import com.dlr.ciscoware_ws.v1.resources.Customer;
import com.dlr.ciscoware_ws.v1.resources.ProductOrder;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-21T17:08:12")
@StaticMetamodel(Order1.class)
public class Order1_ { 

    public static volatile CollectionAttribute<Order1, ProductOrder> productOrderCollection;
    public static volatile SingularAttribute<Order1, Branch> branchId;
    public static volatile SingularAttribute<Order1, Date> modifiedDate;
    public static volatile SingularAttribute<Order1, Customer> customerId;
    public static volatile SingularAttribute<Order1, Integer> id;
    public static volatile SingularAttribute<Order1, Date> deliveryDate;
    public static volatile SingularAttribute<Order1, Date> orderDate;
    public static volatile SingularAttribute<Order1, Double> totalCost;
    public static volatile SingularAttribute<Order1, String> status;

}