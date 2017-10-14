package net.micrositios.pqrapp.formulario;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class Descarga_Archivo extends AsyncTask<String, Void, Bitmap> {

	String url = "";
	Bitmap imagen = null;
	Handler puente_envio;
	Message msg;
	String nombre = "";
	String urllocal = "";

	public Descarga_Archivo(String url, Handler puente_envio, String nombre) {
		super();
		this.url = url;
		this.puente_envio = puente_envio;
		msg = new Message();
		this.nombre = nombre;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		try {

			urllocal = Environment.getExternalStorageDirectory() + "/pqr/" + nombre;

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		// Log.i(doInBackground , Entra en doInBackground);
		try {
			descarga();
			// imagen = descargarImagen(url);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return imagen;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		msg.what = 1;
		puente_envio.dispatchMessage(msg);
		// imgImagen.setImageBitmap(result);

	}

	public String getUrllocal() {
		return urllocal;
	}

	public void setUrllocal(String urllocal) {
		this.urllocal = urllocal;
	}

	public void descarga() {

		URL imageUrl = null;

		try {
			imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.connect();

			// Se obtiene el inputStream de la foto web y se abre el fichero
			// local.
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(urllocal);

			// Lectura de la foto de la web y escritura en fichero local
			byte[] array = new byte[1000]; // buffer temporal de lectura.
			int leido = is.read(array);
			while (leido > 0) {
				fos.write(array, 0, leido);
				leido = is.read(array);
			}

			// cierre de conexion y fichero.
			is.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}