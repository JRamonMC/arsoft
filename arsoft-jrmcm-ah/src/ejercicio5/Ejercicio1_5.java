package ejercicio5;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Ejercicio1_5 {

	public static void main(String[] args) throws TransformerException {
		
		final String documentoEntrada = "xml/Ejercicio1-2.xml";
		final String documentoSalida = "xml/salidaEjercicio1-5.atom.xml";
		final String transformacion = "xml/plantillaEjercicio1-5.xsl";
			
		TransformerFactory factoria = TransformerFactory.newInstance();
		
		Transformer transformador =	factoria.newTransformer(new StreamSource(transformacion));
		Source origen = new StreamSource(documentoEntrada);
		Result destino = new StreamResult(documentoSalida);
		transformador.transform(origen, destino);
		System.out.println("FIN.");
	}

}
