package net.micrositios.pqrapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ConsultaDesdeNotificacionActivity extends Activity {

	private String PATH;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		Intent intent = getIntent();
		this.PATH = this.getFilesDir() + File.separator;
		getentidades();

		Log.d("ConsultaDesdeNotificacionActivity",
				intent.getStringExtra("entidad") + " "
						+ intent.getStringExtra("codigo"));

		seleccionar_entidad(intent.getStringExtra("entidad"));

		AsyncCallWSConsulta task = new AsyncCallWSConsulta(this);
		task.setCodigoSeguimiento(intent.getStringExtra("codigo"));
		task.setCodigoEntidad(intent.getStringExtra("entidad"));

		// task.setConsultaManual(false);
		// Call execute
		task.execute();
	}

	private String getentidades() {
		String json;
		StringBuilder contents = new StringBuilder();
		File aFile = new File(PATH + Propiedades.jsonentidades);
		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
			json = contents.toString();
		} catch (IOException ex) {
			ex.printStackTrace();
			json = Propiedades.entidades;
		} catch (NullPointerException e) {
			json = "";
		}

		return json;
	}

	protected void seleccionar_entidad(String codigo_entidad) {
		try {
			JSONArray ja = new JSONArray(getentidades());

			Log.d("ConsultaDesdeNotificacion", ja.toString());

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String codigoentidad = jo.getString("codigo_entidad");
				if (codigoentidad.contentEquals(codigo_entidad)) {
					Propiedades.entidad = jo.getString("entidad");
					Propiedades.webservice = jo.getString("webservice");
					Propiedades.codigo_entidad = jo.getString("codigo_entidad");
					Propiedades.sigla_entidad = jo.getString("sigla_entidad");
					Propiedades.pagina = jo.getString("pagina");
					Propiedades.correo = jo.getString("correo");
					try {
						String encuesta = jo.getString("encuesta");
						if (encuesta.equals("1")) {

							Propiedades.encuesta = true;

						} else if (encuesta.equals("0")) {

							Propiedades.encuesta = false;

						}

					} catch (Exception e) {
						// TODO: handle exception
						Propiedades.encuesta = false;
					}

					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
