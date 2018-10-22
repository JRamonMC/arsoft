package ejercicio4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

	/* 
	 * El método retorna una fecha en el pasado haciendo referencia al día: pasado lunes, pasado martes, etc.
	 * El parámetro referencia es una constante de Calendar: Calendar.SUNDAY, Calendar.MONDAY, etc.
	*/
	public static Date calcularFecha(int referencia) {

		Calendar calendar = Calendar.getInstance();
		int hoy = calendar.get(Calendar.DAY_OF_WEEK);

		int diferencia = hoy - referencia;

		if (diferencia > 0) {
			calendar.add(Calendar.DATE, -diferencia);
		} else {
			calendar.add(Calendar.DATE, -7 - diferencia);
		}
		return calendar.getTime();
	}

	public static Date ayer() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	

	// Ejemplo de patrón: 30 ene 2017
	public static Date convertirTextoFecha(String fecha) throws ParseException {

		SimpleDateFormat formateador = new SimpleDateFormat("dd MMM yyyy", new Locale("es"));

		return formateador.parse(fecha);
	}

	// Salida en formato XML Schema
	public static String convertirFechaTexto(Date fecha) {
		
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
		
		return formateador.format(fecha);
	}
	
}
