package net.micrositios.pqrapp.formulario;

import java.util.StringTokenizer;

import android.util.Log;

public class validacion_campos {

	private static String TEXTOTIPO1 = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]*$";// Texto sin
	// espacios
	private static String TEXTOTIPO2 = "[A-Za-záéíóúÁÉÍÓÚñÑ\\s]*";// Texto con
	// espacios
	private static String TEXTOTIPO3 = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";// email

	private static String TEXTOTIPO4 = "[-_.,:;¡!¿?'\"#A-Za-záéíóúÁÉÍÓÚñÑ0-9\\s]*";// Texto
	private static String TEXTOTIPO_descripcion = "[-_.,:;¡!¿?@'\"#A-Za-záéíóúÁÉÍÓÚñÑ0-9\\s]*";// Texto

	// y
	// caracteres
	// especiales
	private static String TEXTOTIPO5 = "^[A-Z0-9]*$";// Texto y numeros
	private static String TEXTOTIPO6 = "^\\d*$";// Telefono
	private static String TEXTOTIPO7 = "\\d+";

	public validacion_campos() {

	}

	public static boolean validar_numeros(String numero) {

		return numero.matches(TEXTOTIPO7);

	}

	public static boolean validar_email(String numero) {
		boolean email = false;
		email = numero.matches(TEXTOTIPO3);

		return email;

	}

	public static boolean validar_text(String numero) {
		boolean text_espacio = false;
		text_espacio = numero.matches(TEXTOTIPO2);

		return text_espacio;

	}

	public static boolean validar_telefono(String numero) {
		boolean telefono = false;
		telefono = numero.matches(TEXTOTIPO6);

		return telefono;

	}

	public static boolean validar_direccion(String numero) {
		boolean direccion = false;
		direccion = numero.matches(TEXTOTIPO4);

		return direccion;
	}

	public static boolean validar_descripcion(String numero) {
		boolean direccion = false;
		direccion = numero.matches(TEXTOTIPO_descripcion);

		return direccion;
	}

	public static boolean validar_fecha(String fecha) {
		boolean esfecha = false;
		if (9 <= fecha.length() && fecha.length() <= 10) {

			Log.i("Policia", fecha);
			fecha = fecha.replace(" ", "");
			StringTokenizer st = new StringTokenizer(fecha, "/");

			if (st.countTokens() == 3) {

				String dia = st.nextToken();
				String mes = st.nextToken();
				String aniof = st.nextToken();

				if (Integer.valueOf(dia) != 0 && Integer.valueOf(dia) <= 31 && Integer.valueOf(mes) <= 12
						&& aniof.length() == 4) {
					Log.i("Policia", "valido");

					esfecha = true;
				}

			}
		}

		return esfecha;

	}

}
