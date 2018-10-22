package ejercicio3;

import java.util.LinkedList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
//import servicio.tipos.Programa;

public class ProgramaEjercicio1_3 {
	public static void main(String[] args) throws Exception{
		// 1. Obtener una factoría
		SAXParserFactory factoria = SAXParserFactory.newInstance();

		// 1.1. Configurar validacion
		factoria.setValidating(true);

		// 1.2. Configurar el espacio de nombrados
		factoria.setNamespaceAware(true);

		// 2. Pedir a la factoría la construcción del analizador
		SAXParser analizador = factoria.newSAXParser();

		// 2.1 Configurar el analizador para trabajar con XML Schema
		analizador.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");

		// 3. Crear el manejador
		ManejadorEjercicio1_3 manejador = new ManejadorEjercicio1_3();

		// 4. Analizar el documento
		analizador.parse("xml/rtve.xml", manejador);
		
		System.out.println("Lista programas: ");
		LinkedList<ProgramaResultado> listaProgramas = (LinkedList<ProgramaResultado>) manejador.getProgramas();
		for (ProgramaResultado programa: listaProgramas) {
			System.out.println("Identificador: "+programa.getIdentificador()+ " || Nombre: "+programa.getNombre());
		}	
	}
}
