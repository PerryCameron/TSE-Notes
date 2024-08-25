package com.L2.static_tools;

import com.L2.dto.CaseDTO;
import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;

import java.time.LocalDateTime;

public class FakeData {

    public static CaseDTO createFakeCase() {
        CaseDTO c = new CaseDTO();
        c.setId(234);
        c.setTimestamp(LocalDateTime.now());
        c.setWorkOrder("WO-10196741");
        c.setCaseNumber("123456789");
        c.setModelNumber("PMM400-ALAX");
        c.setSerialNumber("N12402180025");
        c.setCallInPerson("Justin Stain");
        c.setCallInPhoneNumber("(817)-389-2359");
        c.setCallInEmail("justin.stain@eatme.com");
        c.setIssue("""
                The network card is facing significant communication challenges, as it is unable to establish a connection through either TCP/IP or serial interfaces. This issue could be due to a variety of factors. There may be a problem with the card's firmware, leading to a failure in initializing the communication protocols. Alternatively, there could be a hardware fault within the network card itself, such as a damaged port or an internal circuitry issue that is preventing proper signal transmission. It's also possible that the network card's drivers are outdated or corrupted, causing it to malfunction when attempting to communicate with other devices on the network. Environmental factors like electromagnetic interference or physical damage to the card or its connections could also be contributing to this failure. Further investigation is required to pinpoint the exact cause and determine the appropriate course of action to restore communication.
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
        // entitlements need filled
        c.setEntitlement("Advantage Ultra");
        // create a part order
        PartOrderDTO partOrderDTO = new PartOrderDTO("12345678");
        // add it to the list
        c.getPartOrders().add(partOrderDTO);
        // make it the selected one
        c.setSelectedPartOrder(partOrderDTO);
        // add some parts to it
        c.getSelectedPartOrder().getParts().add(createFakePart1());
        c.getSelectedPartOrder().getParts().add(createFakePart2());
        c.getSelectedPartOrder().getParts().add(createFakePart3());
        c.getSelectedPartOrder().getParts().add(createFakePart3());
        c.getSelectedPartOrder().getParts().add(createFakePart3());
        c.getSelectedPartOrder().getParts().add(createFakePart3());
        c.setLoadSupported(true);
        return c;
    }

    public static PartDTO createFakePart1() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartNumber("0J-0P8153");
        partDTO.setPartDescription("ASSY PCB PDU HMI-NMC INTERFACE BOARD");
        partDTO.setPartQuantity(1);
        partDTO.setPartEditable(Boolean.TRUE);
        return partDTO;
    }

    public static PartDTO createFakePart2() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartNumber("0G-SBS50KD");
        partDTO.setPartDescription("SBS50KW MODULE");
        partDTO.setPartQuantity(2);
        partDTO.setPartEditable(Boolean.TRUE);
        return partDTO;
    }

    public static PartDTO createFakePart3() {
        PartDTO partDTO = new PartDTO();
        partDTO.setPartNumber("LIBSMG95MODA");
        partDTO.setPartDescription("Battery Module Type A");
        partDTO.setPartQuantity(8);
        partDTO.setPartEditable(Boolean.TRUE);
        return partDTO;
    }
}
