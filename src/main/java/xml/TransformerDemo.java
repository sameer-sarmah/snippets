package xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class TransformerDemo {

	public static void main(String[] args) {
		InputStream xmlInputStream = TransformerDemo.class.getClassLoader().getResourceAsStream("EenV2SoapResponsePayload.xml");
		Document responseDocument = convertStringToDocument(xmlInputStream);
		System.out.println(responseDocument);

	}
    public static Document convertStringToDocument(InputStream in) {
        try  
        {  
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        	factory.setFeature("http://xml.org/sax/features/external-general-entities", false); 
        	factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); 
        	factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        	factory.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
        	factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        	factory.setXIncludeAware(false); 
        	factory.setExpandEntityReferences(false);
        	factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        	DocumentBuilder builder = factory.newDocumentBuilder();  
            Document doc = builder.parse(in); 
            return doc;
        } catch (Exception e) {  
         
        } 
        return null;
    }
	
    public static String convertDocumentToString(Document document) {
        try {
        	TransformerFactory transformerFactory = TransformerFactory.newInstance();
        	transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        	Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            transformer.transform(new DOMSource(document), new StreamResult(baos));
//            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//            IOUtils.copy(bais, baos);
//            String output = IOUtils.toString(bais, Charset.defaultCharset());
             StringWriter stringWriter = new StringWriter();
             transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
 			String output = stringWriter.toString();
            return output;
        } catch (Exception e) {
              
        }
        return null;
    }
}
