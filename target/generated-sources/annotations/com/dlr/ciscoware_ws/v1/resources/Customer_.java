package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.CustomerAddress;
import com.dlr.ciscoware_ws.v1.resources.Orders;
import com.dlr.ciscoware_ws.v1.resources.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-27T21:39:04")
@StaticMetamodel(Customer.class)
public class Customer_ { 

    public static volatile CollectionAttribute<Customer, CustomerAddress> customerAddressCollection;
    public static volatile SingularAttribute<Customer, String> phoneNumber;
    public static volatile SingularAttribute<Customer, Integer> id;
    public static volatile CollectionAttribute<Customer, Orders> ordersCollection;
    public static volatile SingularAttribute<Customer, User> userId;

}