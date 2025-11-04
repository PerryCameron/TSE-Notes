package com.L2.dto.bom;

import com.L2.static_tools.bom.ParseTester;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComponentXML {

    @XmlElement(name = "item")
    private String item;
    @XmlElement(name = "itemid")
    private long itemId;  // ← FIXED: camelCase
    @XmlElement(name = "level")
    private int    level;
    @XmlElement(name = "desc")
    private String description;
    @XmlElement(name = "rev")
    private String revision;
    @XmlElement(name = "uom")
    private String unitOfMeasurement;
    @XmlElement(name = "quantity")
    private double quantity;
    @XmlElement(name = "item_type")
    private String itemType;

    // Nested components (0…n)
    @XmlElement(name = "component")
    private List<ComponentXML> components = new ArrayList<>();

    // Reference designators – will be turned into a CSV string
    @XmlElement(name = "refdeslist")
    @XmlJavaTypeAdapter(ParseTester.RefDesAdapter.class)
    private String refdesCsv;               // <-- final result

    // -----------------------------------------------------------------
    // Helper for debugging / pretty-print
    // -----------------------------------------------------------------
    @Override
    public String toString() {
        return String.format(
                "%s (lvl=%d, qty=%.3f, type=%s) %s",
                item, level, quantity, itemType,
                refdesCsv != null ? "refs=" + refdesCsv : ""
        );
    }

    public void prettyPrint(int indent) {
        String pad = "  ".repeat(indent);
        System.out.printf("%s%s%n", pad, this);
        for (ComponentXML c : components) {
            c.prettyPrint(indent + 1);
        }
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public List<ComponentXML> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentXML> components) {
        this.components = components;
    }

    public String getRefdesCsv() {
        return refdesCsv;
    }

    public void setRefdesCsv(String refdesCsv) {
        this.refdesCsv = refdesCsv;
    }
}
