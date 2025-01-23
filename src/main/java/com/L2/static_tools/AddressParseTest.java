package com.L2.static_tools;

import java.util.Arrays;
import java.util.Map;

public class AddressParseTest {
    public static void main(String[] args) {
        String[] emails = AddressParseTest.setUpTests();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        Arrays.stream(emails).forEach(email -> {
            String unformattedAddress =  AddressParser.extractAddressBlock(email);
            Map<String, String> address = AddressParser.extractAddress(email);
            System.out.println("-------------------------------------------");
            System.out.println("unformattedAddress:");
            System.out.println(unformattedAddress);
            System.out.println("-------------------------------------------");
            System.out.println("Parsed Address:");
            address.forEach((key, value) -> System.out.println(key + ": " + value));
            System.out.println("\n\n\n");
        });
    }

    public static String[] setUpTests() {
        String[] test = new String[12];

        test[0] = """
            FSR Request
            
            Team Name: 3Phase Power - Parts Request
            
            FSR Details
            -------------------------------------------------------------
            First Name: WILSON
            Last Name: ABALOS
            Phone: 4038891301
            Email: Wilson.Abalos@se.com
            WO: WO-11558232
            
            Customer Details
            ------------------------------------------------------------
            Name: Mike Pinder
            Site Name: University of Lethbridge
            Phone Number: 5873946561
            Email: mike.pinder@uleth.ca
            Address:
            4401 University Dr. Lethbridge AB
            T1K 3M4 Canada
            
            Order Details
            ------------------------------------------------------------
            Equipment Status: online
            Affected Part Serial: C11736190001
            Follow Up: Yes
            
            Part, Serial , Qty
            NMC 66074, 34003640SE - 1 piece
            
            Reason Part Needed:
            defective, no communication to serial and network monitoring
            ------------------------------------------------------------
            Request submitted Jan 15 2025 8:38pm by NAM:SESA197884
            """;

        test[1] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Allan
                Last Name: Gamarro
                Phone: 9709098205
                Email: allan.gamarro@se.com
                WO: WO-12640499
                
                Customer Details
                ------------------------------------------------------------
                Name: Allan Gamarro
                Site Name: Home
                Phone Number: 970-909-8205
                Email: allan.gamarro@se.com
                Address:
                1821 86th Ave Court
                Greeley, CO 80634
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: online
                Affected Part Serial: n/a
                Follow Up: No
                
                Part, Serial , Qty
                0J-0085 - 2
                
                Reason Part Needed:
                Shipping labels for parts returns
                ------------------------------------------------------------
                Request submitted Jan 16 2025 12:08pm by NAM:SESA665715
                """;

        test[2] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Travis
                Last Name: Manners
                Phone:
                Email: demetruis.manners@se.com
                WO: WO-12501856
                
                Customer Details
                ------------------------------------------------------------
                Name: Demetruis Manners
                Site Name: Home
                Phone Number: 6786994364
                Email: Demetruis.Manners@se.com
                Address:
                5308 W Belmont Ave Glendale Az 85301
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: None
                Affected Part Serial: None
                Follow Up: Yes
                
                Part, Serial , Qty
                KIT 0J-0449
                
                Reason Part Needed:
                Required cables and Adapter for BMS Configuration
                ------------------------------------------------------------
                Request submitted Jan 16 2025 10:09am by NAM:SESA795270
                """;

        test[3] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: John
                Last Name: Erskine
                Phone: 6232208551
                Email: lloyd.erskine@se.com
                WO: N/A
                
                Customer Details
                ------------------------------------------------------------
                Name: John
                Site Name: N/A
                Phone Number: 6232208551
                Email: lloyd.erskine@se.com
                Address:
                2369 W Betty Elyse Ln. Phoenix AZ 85023
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: N/A
                Affected Part Serial: N/A
                Follow Up: Yes
                
                Part, Serial , Qty
                0J-0449 Lithium Ion Kit
                
                Reason Part Needed:
                To perform essential job duties.
                ------------------------------------------------------------
                Request submitted Jan 16 2025 10:10am by NAM:SESA795119
                """;

        test[4] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Ralph
                Last Name: Pitts
                Phone: 4692898827
                Email: ralph.pitts@se.com
                WO: 11959422
                
                Customer Details
                ------------------------------------------------------------
                Name: Andrew Zamperin
                Site Name: Tubbesing
                Phone Number: 9725671325
                Email: andrew@tubbesing.com
                Address:
                2020 Diplomat Drive, Suite 100, Dallas, TX 75234
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: N/A
                Affected Part Serial: N/A
                Follow Up: Yes
                
                Part, Serial , Qty
                OJ-ON-96783 (1)
                
                Reason Part Needed:
                Replace defective part
                ------------------------------------------------------------
                Request submitted Jan 15 2025 11:15am by NAM:SESA760552
                """;
        test[5] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Mohamed
                Last Name: Benbarkat
                Phone: 5145781211
                Email: Mohamed.Benbarkat@se.com
                WO: WO-10569583
                
                Customer Details
                ------------------------------------------------------------
                Name: Mohamed Benbarkat
                Site Name: Schneider Electric
                Phone Number: 5145781211
                Email: mohamed.benbarkat@se.com
                Address:
                98 rue Racine, Saint Constant, QC J5A 0J8
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: Online
                Affected Part Serial: QD2351121328
                Follow Up: No
                
                Part, Serial , Qty
                Need to order Dry Contact GLiB kit
                Part# 0W99108
                Qty: 3
                
                Reason Part Needed:
                parts missing
                ------------------------------------------------------------
                Request submitted Jan 20 2025 10:42am by NAM:SESA377530
        """;
        test[6] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Kevin
                Last Name: Marheine
                Phone: 947-210-3157
                Email: kevin.marheine@se.com
                WO: WO-11708868
                
                Customer Details
                ------------------------------------------------------------
                Name: Kevin Marheine
                Site Name: Kevin Marheine
                Phone Number: 947-210-3157
                Email: kevin.marheine@se.com
                Address:
                565 Lakeshore Circle
                Apt 204
                Auburn Hills Michigan 48326
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: Cannot be ran without temp sensor to turn on charger to charge LI batteries.
                Affected Part Serial: N/A
                Follow Up: No
                
                Part, Serial , Qty
                0J-0M-1160----THIS IS A AWS SITE THERE WILL BE 1200 GALAXY VS INSTALLED SO SEND ME LIKE 20 PLEASE
                
                Reason Part Needed:
                LOST AT AWS SITE IN CARLISLE INDIANA
                ------------------------------------------------------------
                Request submitted Jan 18 2025 9:19am by NAM:SESA615307
        """;
        test[7] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Allan
                Last Name: Gamarro
                Phone: 9709098205
                Email: allan.gamarro@se.com
                WO: 12128204
                
                Customer Details
                ------------------------------------------------------------
                Name: Allan Gamarro
                Site Name: Allan Gamarro
                Phone Number: 970-909-8205
                Email: allan.gamarro@se.com
                Address:
                1821 86th Ave Court
                Greeley, CO 80634
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: Online
                Affected Part Serial: n/a
                Follow Up: No
                
                Part, Serial , Qty
                0J-0902 - UPS Ground Returns to Foxborough, MA x2
                
                Reason Part Needed:
                Shipping Labels for old parts
                ------------------------------------------------------------
                Request submitted Jan 21 2025 11:32am by NAM:SESA665715
        """;
        test[8] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Oliver
                Last Name: Elliott
                Phone: 816-289-7203
                Email: oliver.elliott@se.com
                WO: WO-11992704
                
                Customer Details
                ------------------------------------------------------------
                Name: Rachel Johnston
                Site Name: Walmart 6035
                Phone Number: 913-406-3835
                Email: rachel.dines@walmart.com
                Address:
                3220 NEVADA TERRACE
                OTTAWA, Kansas 66067-8410
                USA
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: Online
                Affected Part Serial: BD2251001114
                Follow Up: Yes
                
                Part, Serial , Qty
                0J-840-9030 QTY 1 GVLOPT001 QTY 1
                
                Reason Part Needed:
                Door Handle Broken. Galaxy VL UPS Air filter. Better to replace the air filters now then the power modules later.
                ------------------------------------------------------------
                Request submitted Jan 21 2025 4:39pm by NAM:SESA301265
                """;
        test[9] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: Taylor
                Last Name: Winiarski
                Phone: 4342331617
                Email: taylor.winiarski@se.com
                WO: WO-12123916
                
                Customer Details
                ------------------------------------------------------------
                Name: Taylor Winiarski
                Site Name: LVL08
                Phone Number: 434-233-1617
                Email: Taylor.Winiarski@se.com
                Address:
                612 Goodes Ferry Rd. South Hill, VA 23970
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: Online
                Affected Part Serial: U22049002349
                Follow Up: No
                
                Part, Serial , Qty
                0J-0P3616AH part# listed in TIPI
                
                640-3616E-Z part# listed on affected part that was removed and replaced.
                
                Reason Part Needed:
                During PM cycle PSU2 failed in PC1 during re-energization. Removed and replaced with customer spare. Replacement part needed to replenish spare used.
                ------------------------------------------------------------
                Request submitted Jan 21 2025 6:11pm by NAM:SESA696766
                """;
        test[10] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: WILSON
                Last Name: ABALOS
                Phone: 4038891301
                Email: Wilson.Abalos@se.com
                WO: WO-11558232
                
                Customer Details
                ------------------------------------------------------------
                Name: Mike Pinder
                Site Name: University of Lethbridge
                Phone Number: 5873946561
                Email: mike.pinder@uleth.ca
                Address:
                4401 University Dr. Lethbridge AB
                T1K 3M4 Canada
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: online
                Affected Part Serial: C11736190001
                Follow Up: Yes
                
                Part, Serial , Qty
                NMC 66074, 34003640SE - 1 piece
                
                Reason Part Needed:
                defective, no communication to serial and network monitoring
                ------------------------------------------------------------
                Request submitted Jan 15 2025 8:38pm by NAM:SESA197884
                """;
        test[11] = """
                FSR Request
                
                Team Name: 3Phase Power - Parts Request
                
                FSR Details
                -------------------------------------------------------------
                First Name: JONATHAN
                Last Name: HO
                Phone: 416-561-8211
                Email: Jonathan.Ho@se.com
                WO: WO-12345656
                
                Customer Details
                ------------------------------------------------------------
                Name: Ralph Ignacio
                Site Name: Employment and Social Development Canada - ESDC
                Phone Number: 416-275-1055
                Email: ralph.ignacio@servicecanada.gc.ca
                Address:
                2599 Speakman Drive. Suite 100
                Mississauga, Ontario
                L5K 1B1
                Canada
                
                Order Details
                ------------------------------------------------------------
                Equipment Status: Online
                Affected Part Serial: BD2115005008
                Follow Up: Yes
                
                Part, Serial , Qty
                Quantity 1 of Enersys Datasafe 12HX540 battery.
                
                Reason Part Needed:
                Performed BM and tested battery #30 bad with 11Vdc 44,000 mOhm. Battery date code July 2021.
                ------------------------------------------------------------
                Request submitted Nov 20 2024 10:56am by NAM:SESA45493
                """;

        return test;
    }
}
