package ejercicio3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="programaResultado",propOrder={"identificador","nombre"})
public class ProgramaResultado {
	
	private String identificador;
	private String nombre;
	
	
	public ProgramaResultado (String identificador, String nombre) {
		this.identificador = identificador;
		this.nombre = nombre;
	}
	
	public ProgramaResultado(){};
	
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
