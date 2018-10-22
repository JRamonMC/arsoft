package servicio.tipos;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ejercicio3.ProgramaResultado;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="favoritos")
@XmlType(name = "", propOrder = {
    "id","programas"
})
public class Favoritos {

	private String id;
	
    @XmlElement(required = true)
	private LinkedList<ProgramaResultado> programas = new LinkedList<ProgramaResultado>();

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LinkedList<ProgramaResultado> getProgramas() {
		return programas;
	}
	public void setProgramas(LinkedList<ProgramaResultado> programas) {
		this.programas = programas;
	}
	
}
