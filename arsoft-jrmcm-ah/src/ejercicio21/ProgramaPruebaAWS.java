package ejercicio21;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ProgramaPruebaAWS {

	public static void main(String[] args) throws Exception {

		// final String fichero = "xml/aguila-roja-release.xml";

		SignedRequestsHelper signed = new SignedRequestsHelper("Acacias 38");
		HashMap<String, String> params = new HashMap<String, String>();
		String urlString = signed.sign(params);

		System.out.println(urlString);

		URL ur2l = new URL(urlString);
		InputStream is2 = ur2l.openConnection().getInputStream();
		InputStreamReader isr2 = new InputStreamReader(is2, "UTF-8");

		// DOM: Obtener la factoría y el analizador (builder)
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoria.newDocumentBuilder();

		Document doc = builder.parse(new InputSource(isr2));

		Element rootElement = doc.getDocumentElement();

		NodeList itemsRaiz = rootElement.getElementsByTagName("Items");

		Element itemRaiz = (Element) itemsRaiz.item(0);

		NodeList items = itemRaiz.getElementsByTagName("Item");

		String delimitador = "/";
		String[] temp;

		// Configuramos la factoria para consultas XPath
		XPathFactory factoriaxpath = XPathFactory.newInstance();
		XPath xpath = factoriaxpath.newXPath();
		
		if(items.getLength()==0){
			System.out.println("No se dispone de información de productos para este programa");
			
		}else
		
		for (int i = 0; i < 4; i++) {
			System.out.println("\nItem " + i + ":");
			Element item = (Element) items.item(i);
			
			XPathExpression xASIN = xpath.compile("ASIN");
			Node nodeASIN = (Node) xASIN.evaluate(item, XPathConstants.NODE);
			if (nodeASIN != null) {
				String urlPeq = nodeASIN.getTextContent();
				System.out.println("ASIN: " + urlPeq);
			}


			XPathExpression xURL = xpath.compile("DetailPageURL");
			Node nodeurl = (Node) xURL.evaluate(item, XPathConstants.NODE);
			if (nodeurl != null) {
				String url = nodeurl.getTextContent();
				System.out.println("URL con más detalles: " + url);

				temp = url.split(delimitador);
				String titulo = temp[3];

				System.out.println("Titulo: " + formatearTitulo(titulo));

			}
			XPathExpression xurlp = xpath.compile("SmallImage/URL");
			Node nodeurlp = (Node) xurlp.evaluate(item, XPathConstants.NODE);
			if (nodeurlp != null) {
				String urlPeq = nodeurlp.getTextContent();
				System.out.println("SmallImage: " + urlPeq);
			}

			XPathExpression xurlg = xpath.compile("LargeImage/URL");
			Node nodeurlg = (Node) xurlg.evaluate(item, XPathConstants.NODE);
			if (nodeurlg != null) {
				String urlGran = nodeurlg.getTextContent();
				System.out.println("LargeImage: " + urlGran);
			}

			XPathExpression xp = xpath.compile("OfferSummary/LowestNewPrice/FormattedPrice");
			Node np = (Node) xp.evaluate(item, XPathConstants.NODE);
			if (np != null) {
				String fp = np.getTextContent();
				System.out.println("Precio +bajo: " + fp);
			}
		}

	}

	private static String formatearTitulo(String fuente) {
		return fuente.replaceAll("-", " ").replace("%C3%9a", "Ú").replace("%C3%89", "É").replace("%C3%8d", "Í")
				.replace("%C3%93", "Ó").replace("%C3%81", "Á");
	}

}
