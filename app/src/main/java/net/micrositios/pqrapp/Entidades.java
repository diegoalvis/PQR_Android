package net.micrositios.pqrapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Entidades extends AsyncTask<Void, Void, Void> {

	private static final String TAG = null;
	private Context context;
	private String PATH;
	private Message respuesta;
	private Handler puente;

	public Entidades(Context context) {
		this.PATH = context.getFilesDir() + File.separator;
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		getentidades();
		return null;
	}

	private void getentidades() {

		InputStream inputStream = null;

		try {

			SharedPreferences sp_perfil = context.getSharedPreferences(
					"configuracion", Context.MODE_PRIVATE);

			URL url = new URL(sp_perfil.getString(Propiedades.SERVIDOR,
					Propiedades.WSPRINCIPAL));// SE INICICALIZA EL
												// SHAREDPREFERENCES CON EL
												// SERVICDOR
			URLConnection conexion = url.openConnection(); // DE
															// PRODUCION(.WSPRINCIPAL)
															// O EN PRUEBAS
															// (WSDESARROLLO)
			conexion.connect();
			InputStream input = new BufferedInputStream(url.openStream());
			String json = PATH + Propiedades.jsonentidades;
			OutputStream output = new FileOutputStream(json);
			byte data[] = new byte[32];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();

			final StringBuilder sb = new StringBuilder();

			int intentos = 0;
			do {
				DefaultHttpClient httpclient = new DefaultHttpClient(
						new BasicHttpParams());
				HttpGet httpget = new HttpGet(sp_perfil.getString(
						Propiedades.SERVIDOR, Propiedades.WSPRINCIPAL));// SE
																		// INICICALIZA
																		// EL
																		// SHAREDPREFERENCES
																		// CON
																		// EL
																		// SERVICDOR
				HttpResponse response = httpclient.execute(httpget); // DE
																		// PRODUCION(.WSPRINCIPAL)
																		// O EN
																		// PRUEBAS
																		// (WSDESARROLLO)
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				Propiedades.entidades = sb.toString();
				intentos++;
				Log.i(TAG, "entidades" + Propiedades.entidades);
				if ((sb.toString().length() < 80 && intentos < 4)) {

					try {
						Thread.sleep(300);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			} while (sb.toString().length() < 80 && intentos < 4);

			if (sb.toString().length() < 80) {
				Log.i(TAG, Propiedades.entidades);
				respuesta.what = 1;

			} else {
				Log.i(TAG, Propiedades.entidades);
				respuesta.what = 0;
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			e.printStackTrace();
			respuesta.what = 1;

		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {

			}
		}
	}

	@Override
	protected void onPreExecute() {
		Log.i(TAG, "onPreExecute");
		if (puente == null) {
			puente = new Handler();
		}
		if (respuesta == null) {
			respuesta = new Message();
		}

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		Log.i(TAG, "onProgressUpdate");
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.d(Propiedades.TAG, "onPostExecute " + respuesta.obj);
		puente.sendMessage(respuesta);
	}

	public void setPuente(Handler puente) {
		this.puente = puente;
	}

}
