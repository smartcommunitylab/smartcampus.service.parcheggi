package eu.trentorise.smartcampus.service.parcheggi.script;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.parcheggi.data.message.Parcheggi.Parcheggio;

public class ParcheggiConnector {

	public List<Message> getParcheggi()  throws Exception {
		InputStream is = connect();
		return parse(is);
	}
	
	
	public List<Message> parse(InputStream is) throws Exception {
		List<Message> result = new ArrayList<Message>();

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(is);
		
		NodeList nodes1 = (NodeList) XPathFactory.newInstance().newXPath().compile("//GetDataItemsByFieldResult/SvcDataItem/fields").evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
//		System.out.println(nodes1.getLength());
		for (int i = 0; i < nodes1.getLength(); i++) {
			Element element1 = (Element) nodes1.item(i);
			NodeList nodes2 = (NodeList) XPathFactory.newInstance().newXPath().compile("KeyValueOfstringanyType").evaluate(element1, XPathConstants.NODESET);
			Map<String, String> map = new TreeMap<String, String>();
			for (int j = 0; j < nodes2.getLength(); j++) {
				Element element2 = (Element) nodes2.item(j);
				NodeList nodes3 = element2.getChildNodes();
				String key = null;
				String value = null;
				for (int k = 0; k < nodes3.getLength(); k++) {
					if ("b:Key".equals(nodes3.item(k).getNodeName())) {
						key = nodes3.item(k).getTextContent();
					}
					if ("b:Value".equals(nodes3.item(k).getNodeName())) {
						value = nodes3.item(k).getTextContent();
					}					
				}
				map.put(key, value);
			}
			Parcheggio.Builder pb = Parcheggio.newBuilder();
			pb.setId(map.get("Identificativo"));
			pb.setAddress(map.get("Indirizzo"));
			pb.setPlaces(map.get("Posti Liberi"));
			
			result.add(pb.build());
		}
		
		return result;
		
	}
	
	private static InputStream connect() throws Exception {
//		FileInputStream fis = new FileInputStream("src/main/resources/soapenv.xml");
		InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("service/parcheggi/soapenv.xml");
		BufferedInputStream bis = new BufferedInputStream(fis);

		StringBuffer sb = new StringBuffer();

		byte buffer[] = new byte[1000];
		int n;
		String b;
		try {
			while ((n = bis.read(buffer)) > -1) {
				b = new String(buffer, 0, n, "UTF-8");
				sb.append(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String soapXml = sb.toString();

		URL url = new URL("http://sip/LinkService/LinkService.svc/Basic");
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("SOAPAction", "urn:ILinkService/GetDataItemsByField");
//		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		conn.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(soapXml);
		wr.flush();

		return conn.getInputStream();
	}

}
