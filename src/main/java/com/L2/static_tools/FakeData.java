package com.L2.static_tools;

import com.L2.dto.NoteDTO;
import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.dto.UserDTO;

import java.time.LocalDateTime;

public class FakeData {

    public static UserDTO createPerson() {
        UserDTO userDTO = new UserDTO("Parrish","Cameron","parrish.cameron@se.com", "91827");
        userDTO.setProfileLink("https://se.lightning.force.com/lightning/r/005A0000001pSZBIA2/view");
        return userDTO;
    }

    public static NoteDTO createFakeCase() {
        NoteDTO c = new NoteDTO();
        c.setId(1);
        c.setTimestamp(LocalDateTime.now());
        c.setWorkOrder("WO-10196741");
        c.setCaseNumber("123456789");
        c.setModelNumber("PMM400-ALAX");
        c.setSerialNumber("N12402180025");
        c.setCallInPerson("Justin Stain");
        c.setCallInPhoneNumber("(817)-389-2359");
        c.setCallInEmail("justin.stain@eatme.com");
        c.setIssue("""
                The network card is facing significant communication challenges, as it is unable to establish a connection through either TCP/IP or serial interfaces.
                """);
        c.setContactPhoneNumber("(540)-478-9741");
        c.setContactEmail("gteague@email.com");
        c.setContactName("Greg League");
        c.setStreet("1589 Ferris Rd");
        c.setInstalledAt("Tierpoint - Oklahoma City, OK");
        c.setCity("Garland");
        c.setState("TX");
        c.setZip("75044");
        c.setCountry("USA");
        c.setActiveServiceContract("None");
        c.setUpsStatus("Online");
        c.setServiceLevel("4-Hour");
        c.setSchedulingTerms("7x24");
        c.setCreatedWorkOrder("WO-12345678");
        c.setTex("TEX-12344567895454");
        c.setAdditionalCorrectiveActionText("Here is some extra text");
        // entitlements need filled
        c.setEntitlement("Advantage Ultra");
        // create a part order
        PartOrderDTO partOrderDTO = new PartOrderDTO("12345678");
        partOrderDTO.setOrderNumber("");
        // add it to the list
        c.getPartOrders().add(partOrderDTO);
        // make it the selected one
        c.setSelectedPartOrder(partOrderDTO);
        // add some parts to it
        c.getSelectedPartOrder().getParts().add(createFakePart1());
        c.getSelectedPartOrder().getParts().add(createFakePart2());
        c.getSelectedPartOrder().getParts().add(createFakePart3());
        c.setLoadSupported(true);
        c.setAdditionalCorrectiveActionText("This would be some additional text to explain what I did beside the standard items. This is especially useful for looking back at what I still need to do if case is not completed.");
        return c;
    }

    public static PartDTO createFakePart1() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartNumber("0J-0P8153");
        partDTO.setPartDescription("ASSY PCB PDU HMI-NMC INTERFACE BOARD");
        partDTO.setPartQuantity("1");
        partDTO.setPartEditable(Boolean.TRUE);
        return partDTO;
    }

    public static PartDTO createFakePart2() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartNumber("0G-SBS50KD");
        partDTO.setPartDescription("SBS50KW MODULE");
        partDTO.setPartQuantity("2");
        partDTO.setPartEditable(Boolean.TRUE);
        return partDTO;
    }

    public static PartDTO createFakePart3() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartNumber("LIBSMG95MODA");
        partDTO.setPartDescription("Battery Module Type A");
        partDTO.setPartQuantity("8");
        partDTO.setPartEditable(Boolean.TRUE);
        return partDTO;
    }
}
