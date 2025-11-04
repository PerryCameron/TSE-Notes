package com.L2.dto.bom;

import java.util.List;

import com.L2.static_tools.bom.RefDesList;
import com.L2.static_tools.bom.RefDesListAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "")
public final class ComponentDTO {

    @XmlElement(name = "item")          private final String  item;
    @XmlElement(name = "itemid")        private final Long    itemId;
    @XmlElement(name = "level")         private final int     level;
    @XmlElement(name = "desc")          private final String  description;
    @XmlElement(name = "rev")           private final int     rev;
    @XmlElement(name = "uom")           private final String  uom;
    @XmlElement(name = "quantity")      private final double  qty;
    @XmlElement(name = "item_type")     private final String  itemType;

    // This is the actual field bound to XML
    @XmlElement(name = "refdeslist")
    @XmlJavaTypeAdapter(RefDesListAdapter.class)
    private final RefDesList refDesList;

    // Computed during construction
    @XmlTransient
    private final String referenceList;

    @XmlElementWrapper(name = "component")
    @XmlElement(name = "component")
    private final List<ComponentDTO> children;

    /* ------------------------------------------------------------------
       JAXB needs a no-arg constructor – fields are final, so we use a
       private constructor that JAXB can call via reflection.
       ------------------------------------------------------------------ */
    private ComponentDTO() {
        this.item = null; this.itemId = null; this.level = 0; this.description = null;
        this.rev = 0; this.uom = null; this.qty = 0; this.itemType = null;
        this.refDesList = null; this.referenceList = null; this.children = null;
    }

    /* ------------------------------------------------------------------
       Public constructor – used when you build the object yourself
       ------------------------------------------------------------------ */
    public ComponentDTO(String item, Long itemId, int level, String description,
                        int rev, String uom, double qty, String itemType,
                        RefDesList refDesList, List<ComponentDTO> children) {
        this.item = item;
        this.itemId = itemId;
        this.level = level;
        this.description = description;
        this.rev = rev;
        this.uom = uom;
        this.qty = qty;
        this.itemType = itemType;
        this.refDesList = refDesList;
        this.referenceList = refDesList != null ? refDesList.toString() : "";
        this.children = children;
    }

    /* ---- getters (immutable) ---- */
    public String getItem()           { return item; }
    public Long getItemId()           { return itemId; }
    public int getLevel()             { return level; }
    public String getDescription()    { return description; }
    public int getRev()               { return rev; }
    public String getUom()            { return uom; }
    public double getQty()            { return qty; }
    public String getItemType()       { return itemType; }
    public String getReferenceList()  { return referenceList; }
    public List<ComponentDTO> getChildren() { return children; }
}