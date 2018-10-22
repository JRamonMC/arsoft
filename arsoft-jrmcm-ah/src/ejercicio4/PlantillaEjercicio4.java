package ejercicio4;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class PlantillaEjercicio4 {

	public static void main(String[] args) throws Exception {

		final String urlListado = "http://www.rtve.es/m/alacarta/programsbychannel/?media=tve&channel=la1&modl=canales&filterFindPrograms=todas";

		final String id = "acacias-38";

		final String ficheroResultado = "xml/acacias-38-procesado.xml";

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

		NodeList programas = rootElement.getElementsByTagName("h3");
		if (programas.getLength() > 0) {
			nombrePrograma = programas.item(0).getTextContent();
		}

		NodeList imagenes = rootElement.getElementsByTagName("img");
		if (imagenes.getLength() > 0) {
			Element imagenAcacias = (Element) imagenes.item(0);
			urlImagen = imagenAcacias.getAttribute("src");
		}

		NodeList emisionesAcacias = rootElement.getElementsByTagName("a");
		if (emisionesAcacias.getLength() > 0) {
			Element emisionAcacia = (Element) emisionesAcacias.item(0);
			urlEmision = emisionAcacia.getAttribute("href");
		}

		// StAX: escribir en el documento la información básica
		writer.writeStartElement("p:programa");
		writer.writeNamespace("p", "http://www.example.org/ejercicio1-2");
		writer.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.writeAttribute("xsi:schemaLocation", "http://www.example.org/ejercicio1-2 Ejercicio1-2.xsd ");
		writer.writeAttribute("id", id);
		writer.writeStartElement("p:nombre");
		writer.writeCharacters(nombrePrograma);
		writer.writeEndElement();
		writer.writeStartElement("p:URLprograma");
		writer.writeCharacters("http://www.rtve.es"+ urlEmision);
		writer.writeEndElement();
		writer.writeStartElement("p:URLimagen");
		writer.writeCharacters(urlImagen);
		writer.writeEndElement();
		/*** Parte 2: analizar el documento con las emisiones del programa ***/

		// DOM: componer la URL con las emisiones (a partir del id)
		String urlEmisiones = "http://www.rtve.es/m/alacarta/videos/" + id
				+ "/multimedialist_pag.shtml/?media=tve&contentKey=&programName=" + id + "&media=tve&paginaBusqueda=1";

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
			String duracion ="";
			NodeList horas = elEm.getElementsByTagName("p");
			if (horas.getLength() > 0) {
				String hora = horas.item(0).getTextContent();
				String[] temp;
				// Separamos fecha y duracion
				temp = hora.split(delimiter);
				String fecha = temp[0];
				duracion = temp[1];
				// Necesidad de parsear la fecha
				
				if(fecha.contains("hoy")){
					fechaAux = new Date();
				}else if (fecha.contains("ayer")) {
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
			
			//Transformar la hora al formato adecuado
			String delimitador = ":";
			String[] temp2;
			// Separamos fecha y duracion
			temp2 = duracion.split(delimitador);
			String min = temp2[0];
			String sec = temp2[1];
			
			
			
			writer.writeStartElement("p:emision");
			writer.writeAttribute("titulo", tit);
			writer.writeAttribute("fecha", Utils.convertirFechaTexto(fechaAux));
			writer.writeAttribute("tiempo", "00:"+min+":"+sec);
			writer.writeAttribute("URL", "http://www.rtve.es"+urlEmi);
			writer.writeEndElement();
		}
		// StAX: escribir cada emisión en el documento XMLe

		// StAX: finalizar elemenento raíz y documento
		writer.writeEndElement();
		// StAX: cerrar el flujo de escritura

		writer.writeEndDocument();
		writer.flush();
		writer.close();

		System.out.println("fin.");
	}
}
