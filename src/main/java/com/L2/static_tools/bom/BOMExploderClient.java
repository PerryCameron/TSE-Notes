package com.L2.static_tools.bom;

import jakarta.xml.soap.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class BOMExploderClient {

    // MUST include ?WSDL
    private static final String ENDPOINT_URL = "http://tarvols4web.apc.com:7003/soa-infra/services/default/APCX_BOM_EXPLODER/apcx_bom_exploder_client_ep?WSDL";

    public static String getBOMExplosionAsString(String sku, String orgName, String refDesignator) throws Exception {

        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        SOAPMessage request = factory.createMessage();

        SOAPPart soapPart = request.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();

        // === 1. Set envelope prefix and namespace ===
        envelope.removeNamespaceDeclaration("SOAP-ENV");
        envelope.setPrefix("env");
        envelope.addNamespaceDeclaration("env", "http://schemas.xmlsoap.org/soap/envelope/");

        // === 2. Body with correct prefix ===
        SOAPBody body = envelope.getBody();
        body.setPrefix("env");

        // === 3. Create bomexploder_request with ns1 on the element ===
        Name requestName = envelope.createName("bomexploder_request", "ns1", "http://xmlns.oracle.com/pcbpel/adapter/db/sp/OracleODO");
        SOAPBodyElement bomRequest = body.addBodyElement(requestName);

        // === 4. Add child elements with ns1 prefix ===
        bomRequest.addChildElement("ITEM", "ns1").addTextNode(sku);
        bomRequest.addChildElement("ORG_NAME", "ns1").addTextNode(orgName);
        // Use full closing tag
        bomRequest.addChildElement("REF_DESIGNATOR", "ns1").addTextNode(refDesignator != null ? refDesignator : "");

        // === 5. Set headers EXACTLY like VBA ===
        MimeHeaders headers = request.getMimeHeaders();
        headers.removeHeader("Content-Type"); // Remove default
        headers.setHeader("Content-Type", "text/xml"); // NO charset
        headers.setHeader("SOAPAction", "bomexploder_request");

        // === 6. Save and get raw bytes ===
        request.saveChanges();

        // === DEBUG: Print EXACT packet ===
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        request.writeTo(baos);
//        String xmlContent = baos.toString("UTF-8");

        // Remove auto-added <?xml...> and fix spacing
//        String cleanXml = xmlContent
//                .replaceFirst("<\\?xml[^>]*>\\s*", "")
//                .replaceAll("\\s+", " ")
//                .replace("> <", "><")
//                .trim();

        // Rebuild exact XML
        String expectedXml =
                "<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "  <env:Body>" +
                        "    <ns1:bomexploder_request xmlns:ns1=\"http://xmlns.oracle.com/pcbpel/adapter/db/sp/OracleODO\">" +
                        "      <ns1:ITEM>" + sku + "</ns1:ITEM>" +
                        "      <ns1:ORG_NAME>" + orgName + "</ns1:ORG_NAME>" +
                        "      <ns1:REF_DESIGNATOR>" + (refDesignator != null ? refDesignator : "") + "</ns1:REF_DESIGNATOR>" +
                        "    </ns1:bomexploder_request>" +
                        "  </env:Body>" +
                        "</env:Envelope>";

        // Overwrite with exact string
        SOAPPart newSoapPart = request.getSOAPPart();
        javax.xml.transform.stream.StreamSource source = new javax.xml.transform.stream.StreamSource(
                new java.io.StringReader(expectedXml));
        newSoapPart.setContent(source);

        request.saveChanges();

        // Re-measure
        baos = new java.io.ByteArrayOutputStream();
        request.writeTo(baos);
//        byte[] finalBytes = baos.toByteArray();
//        int contentLength = finalBytes.length;

        // === PRINT FULL PACKET ===
//        System.out.println("POST " + ENDPOINT_URL + " HTTP/1.1");
//        System.out.println("Host: tarvols4web.apc.com:7003");
//        System.out.println("Content-Type: text/xml");
//        System.out.println("SOAPAction: bomexploder_request");
//        System.out.println("Content-Length: " + contentLength);
//        System.out.println();
//        System.out.println(expectedXml);
//        System.out.println();

        // === SEND ===
        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection conn = scf.createConnection();
        //URL endpoint = new URL(ENDPOINT_URL); <- this is deprecated

        //SOAPMessage response = conn.call(request, endpoint);
        SOAPMessage response = conn.call(request, URI.create(ENDPOINT_URL).toURL());
        conn.close();

        return soapMessageToString(response);
    }

    private static String soapMessageToString(SOAPMessage msg) throws Exception {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        msg.writeTo(out);
        return out.toString(StandardCharsets.UTF_8);
    }
}