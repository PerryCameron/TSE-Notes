package com.L2.static_tools.bom;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "")
public class RefDesListWrapper {
    @XmlElement(name = "refdes")
    List<String> refdes;
}
