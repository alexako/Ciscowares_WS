package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.Admin;
import com.dlr.ciscoware_ws.v1.resources.BranchAddress;
import com.dlr.ciscoware_ws.v1.resources.Orders;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-24T00:44:21")
@StaticMetamodel(Branch.class)
public class Branch_ { 

    public static volatile CollectionAttribute<Branch, Orders> order1Collection;
    public static volatile CollectionAttribute<Branch, Admin> adminCollection;
    public static volatile SingularAttribute<Branch, String> name;
    public static volatile CollectionAttribute<Branch, BranchAddress> branchAddressCollection;
    public static volatile CollectionAttribute<Branch, Orders> ordersCollection;
    public static volatile SingularAttribute<Branch, Integer> id;

}