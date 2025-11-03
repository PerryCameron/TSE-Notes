package com.L2.dto.bom;

public class ComponentDTO {
    private final String item;
    private final Long itemId;
    private final int level;
    private final String description;
    private final int rev;
    private final String uom;
    private final double qty;
    private final String itemType;

    public ComponentDTO(String item, Long itemId, int level, String description,
                        int rev, String uom, double qty, String itemType) {
        this.item = item;
        this.itemId = itemId;
        this.level = level;
        this.description = description;
        this.rev = rev;
        this.uom = uom;
        this.qty = qty;
        this.itemType = itemType;
    }

    // Getters
    public String getItem() { return item; }
    public Long getItemId() { return itemId; }
    public int getLevel() { return level; }
    public String getDescription() { return description; }
    public int getRev() { return rev; }
    public String getUom() { return uom; }
    public double getQty() { return qty; }
    public String getItemType() { return itemType; }
}