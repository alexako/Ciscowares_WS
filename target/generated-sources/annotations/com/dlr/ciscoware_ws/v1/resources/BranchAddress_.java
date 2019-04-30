package com.dlr.ciscoware_ws.v1.resources;

import com.dlr.ciscoware_ws.v1.resources.Branch;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-30T17:53:41")
@StaticMetamodel(BranchAddress.class)
public class BranchAddress_ { 

    public static volatile SingularAttribute<BranchAddress, String> zipCode;
    public static volatile SingularAttribute<BranchAddress, String> country;
    public static volatile SingularAttribute<BranchAddress, Branch> branchId;
    public static volatile SingularAttribute<BranchAddress, String> province;
    public static volatile SingularAttribute<BranchAddress, String> city;
    public static volatile SingularAttribute<BranchAddress, String> street;
    public static volatile SingularAttribute<BranchAddress, Integer> id;

}