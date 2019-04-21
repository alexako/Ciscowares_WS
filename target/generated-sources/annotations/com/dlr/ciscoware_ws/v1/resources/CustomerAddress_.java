package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-21T16:37:33")
@StaticMetamodel(CustomerAddress.class)
public class CustomerAddress_ { 

    public static volatile SingularAttribute<CustomerAddress, String> zipCode;
    public static volatile SingularAttribute<CustomerAddress, String> country;
    public static volatile SingularAttribute<CustomerAddress, String> province;
    public static volatile SingularAttribute<CustomerAddress, String> city;
    public static volatile SingularAttribute<CustomerAddress, String> street;
    public static volatile SingularAttribute<CustomerAddress, Customer> customerId;
    public static volatile SingularAttribute<CustomerAddress, Integer> id;

}