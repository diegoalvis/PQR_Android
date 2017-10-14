package net.micrositios.pqrapp;

import java.util.ArrayList;

import net.micrositios.pqrapp.formulario.Estilos;

public class Propiedades {
	public static final String TAG = "MICROSITIOS";
	public static final String tipocanalsolicitud = "4";
	public static final String PREGUNTAR = "preguntar";
	public static final String ACEPTATERMINOS = "aceptaterminos";
	public static final String PRIMERINICIO = "primerinicio";
	public static final String CONFIGURACION = "configuracion";
	public static final String WSDIRECTORIO = "http://micrositios.net/micros_app/WS.php";
	// public static final String WSPRINCIPAL =
	// "http://micrositios.net/micros_app/WS.php/entidades";

	public static final String WSPRINCIPAL = "http://micrositios.net/micros_app/WS.php/entidades";

	public static final String WSDESARROLLO = "http://micrositios.net/micros_app/WS.php/entidades/pruebas";
	public static String medio_recepcion = "ANDROID";
	public static String entidad;
	public static String webservice;
	public static String estado = "Abierto";
	public static String codigo_vulnerable;
	public static String codigo_entidad;
	public static String sigla_entidad;
	public static String entidades = "";
	public static String grupos_vulnerables;
	public static String pagina;
	public static String correo;
	public static String mediosderespuesta;
	public static String tiposdesolicitudes;
	public static String asuntos;
	public static String parametros;
	public static String NO = "no";
	public static String SI = "si";
	public static String GUARDAR = "guardar";
	public static String VERSION = "3.0-20140912";// public static String
	public static String codigo_consulta = ""; // VERSION="1.4100-rev400402";
	public static String tipows = "";
	public static boolean encuesta = false;
	public static boolean encuesta_enviada = true;
	public static final String jsonentidades = "entidades.json";
	public static final String SERVIDOR = "SERVIDOR";
	public static final String PREGUNTARGPS = "preguntargps";
	public static String json_campos = "";

	public static Estilos estilo_entidad = null;
	public static ArrayList<Estilos> estilos_entidad = new ArrayList<Estilos>();

	public static String getHumanReadable(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return " - " + String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
