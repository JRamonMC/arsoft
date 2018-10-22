package servicio.controlador;

import java.io.File;
import java.util.LinkedList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import ejercicio3.ProgramaResultado;
import servicio.tipos.ListadoProgramas;
import servicio.tipos.Programa;

public class ProgramaEjercicio6 {

	public static void main(String[] args) throws Exception {
		
		
		//Instancia de Servicio a la Carta
		ServicioALaCarta servicio = new ServicioALaCarta();

		servicio.getPrograma("acacias-38");
		
		//Prueba del método getListadoProgramas
		/*LinkedList<ProgramaResultado> misProgramas = servicio.getListadoProgramas();
		for (ProgramaResultado programaResultado : misProgramas) {
			//Eliminar el comentario para probar el funcionamiento
			System.out.println(programaResultado.getIdentificador());
		}*/
		 
		
		//Prueba del método getPrograma
		//Registra el programa en caso de que no esté en la base de datos
		//Detener ejecución si no se desea insertar todos los programas.
		
		//Descomentar para probar
		/*
		System.out.println("Listado programas:");
		int i = 1;
		for (ProgramaResultado programa : misProgramas) {
			Programa p = servicio.getPrograma(programa.getIdentificador());
			System.out.print("Programa " + i + " ->");
			System.out.print("	ID: " + p.getId());
			System.out.println("	NOMBRE: " + p.getNombre());
			i++;
		}
		 */
		//Prueba del metodo para obtener un programa en formato ATOM
		//Descomentar para probar
		//System.out.println(servicio.getProgramaAtom("acacias-38"));
		
		/*
		//Prueba del metodo para obtener los programas en objeto Listado programas (total 300 programas)
		//Descomentar para probar
		ListadoProgramas l = servicio.getListadoProgramasXML();
		//System.out.println(l.getProgramas().size());
		JAXBContext contexto = JAXBContext.newInstance(ListadoProgramas.class);
		Marshaller marshaller = contexto.createMarshaller();
		marshaller.marshal(l, new File("xml/listado.xml"));
		
		
		//Prueba del metodo para obtener un programa en formato ATOM
		//Descomentar para probar
		
		*/
		/*
		Programa p = servicio.getProgramaFiltrado("aguila-roja", "111");
		System.out.println(p.getEmision().get(0).getTitulo());
		*/	
		
		/*
		//Prueba del metodo para añadir Favoritos, añadirle programas y quitar uno
		//Descomentar para probar
		String idFav = servicio.crearFavoritos();
		System.out.println(idFav);
		servicio.addProgramaFavorito(idFav, "aqui-la-tierra");
		servicio.addProgramaFavorito(idFav, "acacias-38");
		
		servicio.removeProgramaFavorito(idFav, "acacias-38");
		
		
		*/
		
		
		
		
		
	}
}
