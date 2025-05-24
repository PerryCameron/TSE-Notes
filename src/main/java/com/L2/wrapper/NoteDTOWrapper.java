package com.L2.wrapper;

import com.L2.dto.NoteFx;
import javafx.beans.value.ChangeListener;

// note sure I want to do this
public class NoteDTOWrapper {
    private final NoteFx note;
    private final ChangeListener<Object> listener;

    public NoteDTOWrapper(NoteFx note, ChangeListener<Object> listener) {
        this.note = note;
        this.listener = listener;
        monitorAllFields();
    }

    // Attach listeners to all observable fields
    private void monitorAllFields() {
        note.idProperty().addListener(listener);
        note.workOrderProperty().addListener(listener);
        note.caseNumberProperty().addListener(listener);
        note.serialNumberProperty().addListener(listener);
        note.modelNumberProperty().addListener(listener);
        note.callInPersonProperty().addListener(listener);
        note.callInEmailProperty().addListener(listener);
        note.callInPhoneNumberProperty().addListener(listener);
        note.titleProperty().addListener(listener);

        note.timestampProperty().addListener(listener);
        note.isEmailProperty().addListener(listener);
    }

    // You can add more functionality as needed, or use the note object directly
    public NoteFx getNote() {
        return note;
    }
}

//private IntegerProperty id = new SimpleIntegerProperty();
//private ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>();
//private StringProperty workOrder = new SimpleStringProperty();
//private StringProperty caseNumber = new SimpleStringProperty();
//private StringProperty serialNumber = new SimpleStringProperty();
//private StringProperty modelNumber = new SimpleStringProperty();
//private StringProperty callInPerson = new SimpleStringProperty();
//private StringProperty callInPhoneNumber = new SimpleStringProperty();
//private StringProperty callInEmail = new SimpleStringProperty();
//private BooleanProperty underWarranty = new SimpleBooleanProperty();
//private StringProperty activeServiceContract = new SimpleStringProperty();
//private StringProperty serviceLevel = new SimpleStringProperty();
//private StringProperty schedulingTerms = new SimpleStringProperty();
//private StringProperty upsStatus = new SimpleStringProperty();
//private BooleanProperty loadSupported = new SimpleBooleanProperty();
//private StringProperty title = new SimpleStringProperty();
//private StringProperty issue = new SimpleStringProperty();
//private StringProperty contactName = new SimpleStringProperty();
//private StringProperty contactPhoneNumber = new SimpleStringProperty();
//private StringProperty contactEmail = new SimpleStringProperty();
//private StringProperty street = new SimpleStringProperty();
//private StringProperty installedAt = new SimpleStringProperty();
//private StringProperty city = new SimpleStringProperty();
//private StringProperty state = new SimpleStringProperty();
//private StringProperty zip = new SimpleStringProperty();
//private StringProperty country = new SimpleStringProperty();
//private StringProperty createdWorkOrder = new SimpleStringProperty();
//private StringProperty tex = new SimpleStringProperty();
//private IntegerProperty partsOrder = new SimpleIntegerProperty();
//private BooleanProperty completed = new SimpleBooleanProperty();
//private BooleanProperty isEmail = new SimpleBooleanProperty();
//private StringProperty additionalCorrectiveActionText = new SimpleStringProperty();
//private StringProperty relatedCaseNumber = new SimpleStringProperty();
//private ObjectProperty<PartOrderDTO> selectedPartOrder = new SimpleObjectProperty<>();
//private ListProperty<PartOrderDTO> partOrders = new SimpleListProperty<>(FXCollections.observableArrayList());
