package com.L2.enums;

public enum BomItemType {
    MFG("Manufactured Part", "Components produced in-house from raw materials"),
    PKG("Packaging Material", "Materials used for packing and shipping the product"),
    SP("Spare Part", "Replacement parts for maintenance or repair"),
    CON("Consumable", "Items consumed during production but not part of the final product"),
    TOOL("Tooling", "Tools or fixtures required for manufacturing"),
    FG("Finished Goods", "The final product delivered to the customer"),
    SA("Sub-Assembly", "A partially assembled unit that is part of the final product"),
    PUR("Purchased", "Items bought from external suppliers"),
    UK("Unknown", "Unknown reference");

    private final String meaning;
    private final String description;

    BomItemType(String meaning, String description) {
        this.meaning = meaning;
        this.description = description;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getDescription() {
        return description;
    }

    // Lookup by code
    public static BomItemType fromCode(String code) {
        for (BomItemType type : BomItemType.values()) {
            if (type.name().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return UK;
    }

    // Convenience methods
    public static String getMeaningByCode(String code) {
        return fromCode(code).getMeaning();
    }

    public static String getDescriptionByCode(String code) {
        return fromCode(code).getDescription();
    }
}


