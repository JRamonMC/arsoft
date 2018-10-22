package servicio.tipos;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ejercicio3.ProgramaResultado;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="listadoprogramas")
@XmlType(name = "", propOrder = {
    "programas"
})
public class ListadoProgramas {

    @XmlElement(required = true)
	private LinkedList<ProgramaResultado> programas;

	public LinkedList<ProgramaResultado> getProgramas() {
		return programas;
	}

	public void setProgramas(LinkedList<ProgramaResultado> programas) {
		this.programas = programas;
	}
}
