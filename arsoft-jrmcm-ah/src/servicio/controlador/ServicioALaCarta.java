package servicio.controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ejercicio21.SignedRequestsHelper;
import ejercicio3.ManejadorEjercicio1_3;
import ejercicio3.ProgramaResultado;
import ejercicio4.Utils;
import servicio.tipos.Favoritos;
import servicio.tipos.ListadoProgramas;
import servicio.tipos.Programa;
import servicio.tipos.TipoEmision;

public class ServicioALaCarta {

	public LinkedList<ProgramaResultado> getListadoProgramas()
			throws ParserConfigurationException, SAXException, IOException {

		// 1. Obtener una factoría
		SAXParserFactory factoria = SAXParserFactory.newInstance();
		// 1.1. Configurar validacion
		factoria.setValidating(true);
		// 1.2. Configurar el espacio de nombrados
		factoria.setNamespaceAware(true);
		// 2. Pedir a la factoría la construcción del analizador
		SAXParser analizador = factoria.newSAXParser();
		// 2.1 Configurar el analizador para trabajar con XML Schema
		analizador.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		// 3. Crear el manejador
		ManejadorEjercicio1_3 manejador = new ManejadorEjercicio1_3();
		// 4. Analizar el documento

		String urlRTVE = "http://www.rtve.es/m/alacarta/programsbychannel/?media=tve&channel=la1&modl=canales& filterFindPrograms=todas";
		URL ur2l = new URL(urlRTVE);
		InputStream is2 = ur2l.openConnection().getInputStream();
		InputStreamReader isr2 = new InputStreamReader(is2, "UTF-8");

		analizador.parse(new InputSource(isr2), manejador);

		return (LinkedList<ProgramaResultado>) manejador.getProgramas();
	}

	public Programa getPrograma(String id) throws Exception {
		recuperarPrograma(id);
		String documento = "xml-bd/" + id + ".xml";
		JAXBContext contexto = JAXBContext.newInstance("servicio.tipos");
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		Programa programa = (Programa) unmarshaller.unmarshal(new File(documento));
		return programa;
	}

	public String getProgramaAtom(String id) throws TransformerException, FileNotFoundException {

		String documentoEntrada = "xml-bd/" + id + ".xml";
		String transformacion = "xml/plantillaEjercicio1-5.xsl";

		StringWriter salida = new StringWriter();

		TransformerFactory factoria = TransformerFactory.newInstance();
		Transformer transformador = factoria.newTransformer(new StreamSource(transformacion));
		Source origen = new StreamSource(documentoEntrada);
		Result destino = new StreamResult(salida);
		transformador.transform(origen, destino);
		System.out.println("FIN.");
		return salida.toString();
	}

	public void recuperarPrograma(String id) throws Exception {
		boolean noActualizado = false;

		File fichero = new File("xml-bd/" + id + ".xml");
		if (fichero.exists()) {
			System.out.println("Fichero encontrado");
			long ultimaModificacion = fichero.lastModified();
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			long ayerMillis = c.getTimeInMillis();
			if (ayerMillis - ultimaModificacion < 0) {
				System.out.println("Actualizado recientemente");
			} else {
				System.out.println("No actualizado, se procederá a su registro en la BD");
				noActualizado = true;
			}

		} else {
			System.out.println("Fichero no encontrado, se procederá a su registro en la BD");
			noActualizado = true;
		}

		if (noActualizado) {
			// COMPROBAR QUE PUEDE SER UN ID VALIDO
			registrarPrograma(id);
		}
		noActualizado = false;

	}

	// Función auxiliar de recuperar programa que se encarga de registrarlo
	// segun el ejercicio 4
	public void registrarPrograma(String id) throws Exception {
		final String urlListado = "http://www.rtve.es/m/alacarta/programsbychannel/?media=tve&channel=la1&modl=canales&filterFindPrograms=todas";

		String idProgramaSAC = id;

		String tituloPrograma = nombrePrograma(idProgramaSAC);

		String ficheroResultado = "xml-bd/" + idProgramaSAC + ".xml";

		String nombrePrograma = "";
		String urlImagen = "";
		String urlEmision = "";

		// DOM: Obtener la factoría y el analizador (builder)
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoria.newDocumentBuilder();

		// Document documento = analizador.parse();
		// StAX: Obtener un writer para generar el fichero resultado
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = xof.createXMLStreamWriter(new FileOutputStream(ficheroResultado));

		// StAX: escribir la cabecera del documento: elemento raíz, espacios de
		// nombres, enlace con esquema, etc.
		writer.writeStartDocument();

		/*** Parte 1: analizar el documento de listado de programas ***/

		// DOM: construir el árbol DOM del listado
		URL url = new URL(urlListado);
		InputStream is = url.openConnection().getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");

		Document doc = builder.parse(new InputSource(isr));
		// DOM: analizar el árbol y extraer la información básica: id (nos lo
		// dan), nombre, url e imagen
		Element rootElement = doc.getDocumentElement();

		nombrePrograma = tituloPrograma;

		NodeList imagenes = rootElement.getElementsByTagName("img");
		if (imagenes.getLength() > 0) {
			for (int i = 0; i < imagenes.getLength(); i++) {
				Element imagenAcacias = (Element) imagenes.item(i);
				if (imagenAcacias.getAttribute("alt").equals(tituloPrograma)) {
					urlImagen = imagenAcacias.getAttribute("src");
				}
			}
		}

		NodeList emisionesAcacias = rootElement.getElementsByTagName("a");
		if (emisionesAcacias.getLength() > 0) {
			for (int i = 0; i < imagenes.getLength(); i++) {
				Element emisionAcacia = (Element) emisionesAcacias.item(i);
				if (emisionAcacia.getAttribute("href").contains(idProgramaSAC)) {

					urlEmision = emisionAcacia.getAttribute("href");
				}
			}
		}

		// StAX: escribir en el documento la información básica
		writer.writeStartElement("p:programa");
		writer.writeNamespace("p", "http://www.example.org/ejercicio1-2");
		writer.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.writeAttribute("xsi:schemaLocation", "http://www.example.org/ejercicio1-2 Ejercicio1-2.xsd ");
		writer.writeAttribute("id", idProgramaSAC);
		writer.writeStartElement("p:nombre");
		writer.writeCharacters(nombrePrograma);
		writer.writeEndElement();
		writer.writeStartElement("p:URLprograma");
		writer.writeCharacters("http://www.rtve.es" + urlEmision);
		writer.writeEndElement();
		writer.writeStartElement("p:URLimagen");
		writer.writeCharacters(urlImagen);
		writer.writeEndElement();

		/*** Parte 2: analizar el documento con las emisiones del programa ***/

		// DOM: componer la URL con las emisiones (a partir del id)
		String urlEmisiones = "http://www.rtve.es/m/alacarta/videos/" + idProgramaSAC
				+ "/multimedialist_pag.shtml/?media=tve&contentKey=&programName=" + idProgramaSAC
				+ "&media=tve&paginaBusqueda=1";

		// Parche: añadir un elemento raíz al documento (se adjunta ayuda en el
		// enunciado)
		URL url2 = new URL(urlEmisiones);
		InputStream is2 = url2.openConnection().getInputStream();
		InputStreamReader isr2 = new InputStreamReader(is2, "UTF-8");

		Scanner scanner = new Scanner(isr2);

		// Utilizamos StringWriter para componer en memoria el documento XML
		// bien formato
		StringWriter stringWriter = new StringWriter();

		stringWriter.append("<emisiones>");
		while (scanner.hasNextLine()) {

			stringWriter.append(scanner.nextLine());
		}
		stringWriter.append("</emisiones>");
		stringWriter.close();
		scanner.close();

		// Abrimos un flujo de lectura al documento en memoria
		StringReader reader2 = new StringReader(stringWriter.toString());

		// DOM: obtener el árbol DOM (del documento modificado en memoria)

		DocumentBuilderFactory factoria2 = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder2 = factoria2.newDocumentBuilder();

		Document doc2 = builder2.parse(new InputSource(reader2));
		// DOM: para cada emisión:
		Element rootEmisiones = doc2.getDocumentElement();

		// DOM: extraer la información: título, fecha, tiempo y url
		NodeList emisionesLista = rootEmisiones.getElementsByTagName("li");

		// Variable utilizada para trocear la fecha y la duración
		String delimiter = " - ";

		for (int i = 0; i < emisionesLista.getLength(); i++) {
			// Seleccionamos una emision de la lista
			Element elEm = (Element) emisionesLista.item(i);
			// titulo
			String tit = "";
			NodeList titulo = elEm.getElementsByTagName("h4");
			if (titulo.getLength() > 0) {
				tit = titulo.item(0).getTextContent();

			}
			// fecha y tiempo
			Date fechaAux = null;
			String duracion = "";
			NodeList horas = elEm.getElementsByTagName("p");
			if (horas.getLength() > 0) {
				String hora = horas.item(0).getTextContent();
				String[] temp;
				// Separamos fecha y duracion
				temp = hora.split(delimiter);
				String fecha = temp[0];
				duracion = temp[1];
				// Necesidad de parsear la fecha

				if (fecha.contains("hoy")) {
					fechaAux = new Date();
				} else if (fecha.contains("ayer")) {
					fechaAux = Utils.ayer();
				} else if (fecha.contains("pasado")) {
					if (fecha.contains("lunes")) {
						fechaAux = Utils.calcularFecha(Calendar.MONDAY);
					} else if (fecha.contains("martes")) {
						fechaAux = Utils.calcularFecha(Calendar.TUESDAY);
					} else if (fecha.contains("miércoles")) {
						fechaAux = Utils.calcularFecha(Calendar.WEDNESDAY);
					} else if (fecha.contains("jueves")) {
						fechaAux = Utils.calcularFecha(Calendar.THURSDAY);
					} else if (fecha.contains("viernes")) {
						fechaAux = Utils.calcularFecha(Calendar.FRIDAY);
					} else if (fecha.contains("sábado")) {
						fechaAux = Utils.calcularFecha(Calendar.SATURDAY);
					} else if (fecha.contains("domingo")) {
						fechaAux = Utils.calcularFecha(Calendar.SUNDAY);
					}
				} else {
					fechaAux = Utils.convertirTextoFecha(fecha);
				}

			}
			NodeList urlsEmisiones = elEm.getElementsByTagName("a");
			String urlEmi = "";
			if (urlsEmisiones.getLength() > 0) {
				Element urlEm = (Element) urlsEmisiones.item(0);
				urlEmi = urlEm.getAttribute("href");
			}

			// Transformar la hora al formato adecuado
			String delimitador = ":";
			String[] temp2;
			// Separamos fecha y duracion
			temp2 = duracion.split(delimitador);
			String min = "00";
			String sec = "00";
			String ho = "00";
			if(temp2.length==3){
				ho = temp2[0];
				if(ho.length()==1){
					ho = "0" + ho;
				}
				min=temp2[1];
				sec=temp2[2];
				
			}else{
				min=temp2[0];
				sec=temp2[1];
			}
			
			
			
			
			
			
			writer.writeStartElement("p:emision");
			writer.writeAttribute("titulo", tit);
			writer.writeAttribute("fecha", Utils.convertirFechaTexto(fechaAux));
			writer.writeAttribute("tiempo", ho+":" + min + ":" + sec);
			writer.writeAttribute("URL", "http://www.rtve.es" + urlEmi);
			writer.writeEndElement();
		}
		// StAX: escribir cada emisión en el documento XMLe

		/*** Parte 3 - Añadir productos de Amazon ***/
		
		String codigoAsin = "";
		String tituloItem = "";
		String urlImagenPequena= "";
		String urlImagenGrande= "";
		String precioBajo= "";
		String urlDetalle= "";

		SignedRequestsHelper signed = new SignedRequestsHelper(nombrePrograma);
		HashMap<String,String> params = new HashMap<String,String>();
		String  urlStringAWS = signed.sign(params);
		
		System.out.println(urlStringAWS);	
		
		URL url3 = new URL(urlStringAWS);
		InputStream is3 = url3.openConnection().getInputStream();
		InputStreamReader isr3 = new InputStreamReader(is3, "UTF-8");
		//HASTA AQUí OK
		
		
		
		// DOM: Obtener la factoría y el analizador (builder)
		DocumentBuilderFactory factoria3 = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder3 = factoria3.newDocumentBuilder();
		
		Document doc3 = builder3.parse(new InputSource(isr3));
		
		Element rootElement3 = doc3.getDocumentElement();

		NodeList itemsRaiz = rootElement3.getElementsByTagName("Items");
		//HASTA aQuí ok
		
		Element itemRaiz = (Element) itemsRaiz.item(0);
		
		NodeList items = itemRaiz.getElementsByTagName("Item");

		String delimitador = "/";
		String[] temp;
		
		
		// Configuramos la factoria para consultas XPath
		XPathFactory factoriaxpath = XPathFactory.newInstance();
		XPath xpath = factoriaxpath.newXPath();
		System.out.println("ServicioALaCarta.registrarPrograma()1");
		//HASTA AQUI OK
		
		if(items.getLength()==0){
			System.out.println("No se dispone de información de productos para este programa");
			
		}else
			
		for (int i = 0; i < 3; i++) {
			System.out.println("ServicioALaCarta.registrarPrograma()2");
			//System.out.println("\nItem "+i+":");
			Element item = (Element) items.item(i);
			
			XPathExpression xASIN = xpath.compile("ASIN");
			Node nodeASIN = (Node) xASIN.evaluate(item, XPathConstants.NODE);
			if (nodeASIN != null) {
				String asin = nodeASIN.getTextContent();
				System.out.println("ASIN: " + asin);
				codigoAsin = asin;
			}


			XPathExpression xURL = xpath.compile("DetailPageURL");
			Node nodeurl = (Node) xURL.evaluate(item, XPathConstants.NODE);
			if (nodeurl != null) {
				String urlA = nodeurl.getTextContent();
				System.out.println("URL con más detalles: " + urlA);
				urlDetalle = urlA;
				
				temp = urlA.split(delimitador);
				String titulo = temp[3];

				System.out.println("Titulo: " + formatearTitulo(titulo));
				tituloItem = formatearTitulo(titulo);

			}
			XPathExpression xurlp = xpath.compile("SmallImage/URL");
			Node nodeurlp = (Node) xurlp.evaluate(item, XPathConstants.NODE);
			if (nodeurlp != null) {
				String urlPeq = nodeurlp.getTextContent();
				System.out.println("SmallImage: " + urlPeq);
				urlImagenPequena = urlPeq;
			}

			XPathExpression xurlg = xpath.compile("LargeImage/URL");
			Node nodeurlg = (Node) xurlg.evaluate(item, XPathConstants.NODE);
			if (nodeurlg != null) {
				String urlGran = nodeurlg.getTextContent();
				System.out.println("LargeImage: " + urlGran);
				urlImagenGrande = urlGran;
			}

			XPathExpression xp = xpath.compile("OfferSummary/LowestNewPrice/FormattedPrice");
			Node np = (Node) xp.evaluate(item, XPathConstants.NODE);
			if (np != null) {
				String fp = np.getTextContent();
				System.out.println("Precio +bajo: " + fp);
				precioBajo = fp;
			}
			System.out.println("ServicioALaCarta.registrarPrograma()previo a escribir");
			
			writer.writeStartElement("p:producto");
			writer.writeAttribute("ASIN", codigoAsin);
			writer.writeAttribute("Titulo", tituloItem);
			writer.writeAttribute("urlImgSmall", urlImagenPequena);
			writer.writeAttribute("urlImgLarge", urlImagenGrande);
			writer.writeAttribute("Precio", precioBajo);
			writer.writeAttribute("URL", urlDetalle);
			writer.writeEndElement();
		}
		
		

		// StAX: finalizar elemenento raíz y documento
		writer.writeEndElement();
		// StAX: cerrar el flujo de escritura

		writer.writeEndDocument();
		writer.flush();
		writer.close();

		System.out.println("fin.");
	}

	// Funcion auxiliar que dado un programa nos devuelve su id
	public String nombrePrograma(String id) throws ParserConfigurationException, SAXException, IOException {
		String nombrePrograma = "";
		LinkedList<ProgramaResultado> programas = getListadoProgramas();
		for (ProgramaResultado programa : programas) {
			if (id.equals(programa.getIdentificador())) {
				nombrePrograma = programa.getNombre();
			}
		}
		return nombrePrograma;
	}

	//////////////////////////////////////////////////////////////////////
	///////////////////// FIN EJERCICIO 6//////////////////////////////////
	/////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////
	////////////////// COMIENZO EJERCICIO 7////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	public ListadoProgramas getListadoProgramasXML() throws SAXException, IOException, ParserConfigurationException {

		// 1. Obtener una factoría
		SAXParserFactory factoria = SAXParserFactory.newInstance();
		// 1.1. Configurar validacion
		factoria.setValidating(true);
		// 1.2. Configurar el espacio de nombrados
		factoria.setNamespaceAware(true);
		// 2. Pedir a la factoría la construcción del analizador
		SAXParser analizador = factoria.newSAXParser();
		// 2.1 Configurar el analizador para trabajar con XML Schema
		analizador.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		// 3. Crear el manejador
		ManejadorEjercicio1_3 manejador = new ManejadorEjercicio1_3();
		// 4. Analizar el documento
		analizador.parse("xml/rtve.xml", manejador);

		ListadoProgramas listado = new ListadoProgramas();

		LinkedList<ProgramaResultado> programas = new LinkedList<ProgramaResultado>();

		programas = (LinkedList<ProgramaResultado>) manejador.getProgramas();
		listado.setProgramas(programas);

		return listado;
	}

	public Programa getProgramaFiltrado(String id, String titulo) throws Exception {
		recuperarPrograma(id);
		String documento = "xml-bd/" + id + ".xml";
		JAXBContext contexto = JAXBContext.newInstance("servicio.tipos");
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		Programa programaOrigen = (Programa) unmarshaller.unmarshal(new File(documento));

		Programa programaFiltrado = new Programa();
		programaFiltrado.setId(programaOrigen.getId());
		programaFiltrado.setNombre(programaOrigen.getNombre());
		programaFiltrado.setURLimagen(programaOrigen.getURLimagen());
		programaFiltrado.setURLprograma(programaOrigen.getURLimagen());

		for (TipoEmision emision : programaOrigen.getEmision()) {

			if (emision.getTitulo().contains(titulo)) {
				programaFiltrado.getEmision().add(emision);
			}

		}

		return programaFiltrado;
	}

	public String crearFavoritos() throws Exception {

		String id = UUID.randomUUID().toString();

		Favoritos favs = new Favoritos();
		favs.setId(id);
		String nombreDocFav = "favoritos-" + id + ".xml";

		JAXBContext contexto = JAXBContext.newInstance(Favoritos.class);

		Marshaller marshaller = contexto.createMarshaller();
		
		marshaller.setProperty("jaxb.formatted.output", true);

		marshaller.marshal(favs, new File("xml-bd/" + nombreDocFav));

		return id;

	}

	public boolean addProgramaFavorito(String idFavoritos, String idPrograma) throws Exception {

		LinkedList<ProgramaResultado> programas = getListadoProgramas();
		System.out.println(programas.size());
		ProgramaResultado programa = null;
		boolean programaEncontrado = false;

		for (ProgramaResultado programaResultado : programas) {
			if (programaResultado.getIdentificador().equals(idPrograma)) {
				System.out.println(programaResultado.getIdentificador());

				programaEncontrado = true;
				System.out.println(programaEncontrado);
				programa = programaResultado;
				System.out.println(programa.getNombre());
			}
		}
		if (!programaEncontrado) {
			return false;
		}

		String ficheroFavoritos = "xml-bd/favoritos-" + idFavoritos + ".xml";

		JAXBContext contexto = JAXBContext.newInstance(Favoritos.class);

		Unmarshaller unmarshaller = contexto.createUnmarshaller();

		Favoritos favs = (Favoritos) unmarshaller.unmarshal(new File(ficheroFavoritos));

		System.out.println(favs.getId());

		favs.getProgramas().add(programa);

		Marshaller marshaller = contexto.createMarshaller();

		marshaller.marshal(favs, new File(ficheroFavoritos));

		return true;

	}

	public boolean removeProgramaFavorito(String idFavoritos, String idPrograma) throws Exception {

		String ficheroFavoritos = "xml-bd/favoritos-" + idFavoritos + ".xml";

		JAXBContext contexto = JAXBContext.newInstance(Favoritos.class);

		Unmarshaller unmarshaller = contexto.createUnmarshaller();

		Favoritos favs = (Favoritos) unmarshaller.unmarshal(new File(ficheroFavoritos));

		boolean encontrado = false;

		for (ProgramaResultado programa : favs.getProgramas()) {
			if (programa.getIdentificador().equals(idPrograma)) {
				encontrado = true;
				favs.getProgramas().remove(programa);
				break;
			}
		}
		if (encontrado) {
			Marshaller marshaller = contexto.createMarshaller();
			marshaller.marshal(favs, new File(ficheroFavoritos));

			return true;
		} else {
			return false;
		}

	}

	public Favoritos getFavoritos(String idFavoritos) throws Exception {
		String ficheroFavoritos = "xml-bd/favoritos-" + idFavoritos + ".xml";
		JAXBContext contexto = JAXBContext.newInstance(Favoritos.class);

		Unmarshaller unmarshaller = contexto.createUnmarshaller();

		Favoritos favs = (Favoritos) unmarshaller.unmarshal(new File(ficheroFavoritos));

		return favs;
	}

	private static String formatearTitulo(String fuente) {

		return fuente.replaceAll("-", " ").replace("%C3%9a", "Ú").replace("%C3%89", "É").replace("%C3%8d", "Í")
				.replace("%C3%93", "Ó").replace("%C3%81", "Á");
	}

}
