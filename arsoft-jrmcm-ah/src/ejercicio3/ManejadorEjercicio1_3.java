package ejercicio3;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManejadorEjercicio1_3 extends DefaultHandler {

	private List<ProgramaResultado> programas;
	private String nombre;
	
	private String id;
	
	private boolean isNombre;
	
	private boolean isId;

	public List<ProgramaResultado> getProgramas() {
		return programas;
	}

	public void setProgramas(List<ProgramaResultado> programas) {
		this.programas = programas;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isNombre() {
		return isNombre;
	}

	public void setNombre(boolean isNombre) {
		this.isNombre = isNombre;
	}

	@Override
	public void startDocument() throws SAXException {
		programas = new LinkedList<ProgramaResultado>();
		nombre = "";
		isNombre = false;
		id="";
		isId = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (localName.equals("h3")) {
			isNombre = true;
		}
		if(localName.equals("a")){
			String aux = attributes.getValue(0);
			String delim = "/";
			String[] temporal;
			temporal = aux.split(delim);
			id=temporal[4];
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (isNombre) {
			nombre = String.valueOf(ch, start, length);
			isNombre = false;
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equals("li")) {
			
			ProgramaResultado p = new ProgramaResultado(id,nombre);
			
			programas.add(p);
			nombre = "";
			id="";
			isNombre = false;
		}
	}

}
