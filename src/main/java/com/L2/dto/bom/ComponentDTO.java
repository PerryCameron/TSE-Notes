package com.L2.dto.bom;

import javafx.beans.property.*;

public class ComponentDTO {
    private final StringProperty item = new SimpleStringProperty();
    private final LongProperty itemId = new SimpleLongProperty();
    private final IntegerProperty level = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty revision = new SimpleStringProperty();
    private final StringProperty uom = new SimpleStringProperty();
    private final DoubleProperty quantity = new SimpleDoubleProperty();
    private final StringProperty itemType = new SimpleStringProperty();
    private final StringProperty refDes = new SimpleStringProperty();
    private final BooleanProperty inSpares = new SimpleBooleanProperty(false);

    public ComponentDTO(ComponentXML comp) {
        item.set(comp.getItem());
        itemId.set(comp.getItemId());
        level.set(comp.getLevel());
        description.set(comp.getDescription());
        revision.set(comp.getRevision());
        uom.set(comp.getUnitOfMeasurement());
        quantity.set(comp.getQuantity());
        itemType.set(comp.getItemType());
        refDes.set(comp.getRefdesCsv());
    }

    public ComponentDTO() {
    }

    // Getters for properties
    public StringProperty itemProperty() { return item; }
    public LongProperty itemIdProperty() { return itemId; }
    public IntegerProperty levelProperty() { return level; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty revisionProperty() { return revision; }
    public StringProperty uomProperty() { return uom; }
    public DoubleProperty quantityProperty() { return quantity; }
    public StringProperty itemTypeProperty() { return itemType; }
    public StringProperty refDesProperty() { return refDes; }
    public BooleanProperty inSparesProperty() { return inSpares; }


    @Override
    public String toString() {
        return "ComponentDTO{" +
                "item=" + item +
                ", itemId=" + itemId +
                ", level=" + level +
                ", description=" + description +
                ", revision=" + revision +
                ", uom=" + uom +
                ", quantity=" + quantity +
                ", itemType=" + itemType +
                ", refDes=" + refDes +
                '}';
    }
}