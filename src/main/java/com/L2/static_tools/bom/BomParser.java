package com.L2.static_tools.bom;

import com.L2.dto.bom.ComponentDTO;
import jakarta.xml.bind.*;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.List;

public class BomParser {

    private static final JAXBContext CTX;

    static {
        try {
            CTX = JAXBContext.newInstance(
                    BomExploderResponse.class,
                    ComponentDTO.class,           // ← ADD THIS
                    RefDesListWrapper.class,      // ← ADD THIS
                    RefDesList.class              // ← ADD THIS (optional, but safe)
            );
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /***
     * @param soapXml the complete SOAP envelope (as you posted)
     * @return the list of top-level components (the tree root(s))
     */
    public static List<ComponentDTO> parse(String soapXml) throws JAXBException {
        System.out.println("Parsing XML of size: " + soapXml.length());

        // 1. Find <bomexploder_response
        int start = soapXml.indexOf("<bomexploder_response") +31;
//        int start = soapXml.indexOf("<bomexploder_response");
//        int end = soapXml.lastIndexOf("</bomexploder_response>") + "</bomexploder_response>".length();
        int end = soapXml.lastIndexOf("</bomexploder_response>");

        String payloadWithAttr = soapXml.substring(start, end);
        System.out.println("Raw payload:\n" + payloadWithAttr);

//        // 2. Remove xmlns="" attribute
//        String payload = payloadWithAttr.replaceAll("\\s*xmlns=\"\"", "");
//
//        System.out.println("Clean payload:\n" + payload);

        // 3. Unmarshal
        Unmarshaller u = CTX.createUnmarshaller();
        StreamSource src = new StreamSource(new StringReader(payloadWithAttr));
        JAXBElement<BomExploderResponse> root = u.unmarshal(src, BomExploderResponse.class);
        System.out.println("There are " + root.getValue().getRootComponents().size() + " components");
        return root.getValue().getRootComponents();
    }
}