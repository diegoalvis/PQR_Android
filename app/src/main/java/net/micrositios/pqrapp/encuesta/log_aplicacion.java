package net.micrositios.pqrapp.encuesta;

import android.util.Log;

public class log_aplicacion {
	private boolean depuracion = true;
	private boolean informacion = true;
	private boolean error = true;
	private String TAG = "policia";
	public String modulo_prueba = "";

	public log_aplicacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getModulo_prueba() {
		return modulo_prueba;
	}

	public void setModulo_prueba(String modulo_prueba) {
		modulo_prueba = " :" + modulo_prueba;

	}

	public void log_depueracion(String msg) {
		if (depuracion) {

			msg = modulo_prueba + msg;
			Log.d(TAG, msg);
		}
	}

	public void log_error(String msg) {
		if (error) {
			msg = modulo_prueba + msg;
			Log.e(TAG, msg);
		}
	}

	public void log_informacion(String msg) {
		if (informacion) {
			msg = modulo_prueba + msg;
			Log.i(TAG, msg);
		}
	}

	public void setError(boolean error) {

		this.error = error;
	}

	public void setDepuracion(boolean depuracion) {
		this.depuracion = depuracion;
	}

	public void setInformacion(boolean info) {
		this.informacion = informacion;
	}

}
