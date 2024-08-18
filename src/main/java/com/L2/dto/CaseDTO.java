package com.L2.dto;

import java.io.Serializable;
import java.util.List;


public class CaseDTO implements Serializable {

    int workOrder;
    int caseNumber;
    String serialNumber;
    String CallInPerson;
    String CallInPhoneNumber;
    String callInEmail;
    boolean underWarranty;
    String ActiveServiceContract;
    String ServiceLevel;
    String upsStatus;
    boolean loadSupported;
    String issue;

    String ContactName;
    String ContactPhoneNumber;
    String ContactEmail;

    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String zip;
    String country;

    List<PartDTO> parts;

    int createdWorkOrder;
    int partsOrder;


}
