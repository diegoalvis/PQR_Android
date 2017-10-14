package net.micrositios.pqrapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class DatosFormulario extends AsyncTask<Void, Void, Void> {

	private static final String TAG = null;
	private Context context;
	private String Id;
	private ProgressDialog dialog;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public DatosFormulario(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		Log.d("REST", Propiedades.tipows);

		getgruposvulnerables();
		getmediosderespuesta();
		gettiposdesolicitud();
		getasuntos();
		getparametros();
		return null;
	}

	private void getparametros() {
		Log.i(TAG, "onPostExecute");

		InputStream inputStream = null;

		try {
			final StringBuilder sb = new StringBuilder();

			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

			HttpUriRequest httpurirequest = new HttpGet(Propiedades.webservice + "/entidades/parametros");
			Log.i(TAG, "consulata" + Propiedades.webservice + "/entidades/parametros");
			HttpResponse response = httpclient.execute(httpurirequest);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			Propiedades.parametros = sb.toString();
			Log.i("PARAM", Propiedades.parametros);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
				squish.printStackTrace();
			}
		}
	}

	private void getasuntos() {
		Log.i(TAG, "onPostExecute");

		InputStream inputStream = null;

		try {
			final StringBuilder sb = new StringBuilder();

			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

			HttpUriRequest httpurirequest = new HttpGet(Propiedades.webservice + "/entidades/asuntos");
			Log.i(TAG, "consulata" + Propiedades.webservice + "/entidades/asuntos");
			HttpResponse response = httpclient.execute(httpurirequest);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			Propiedades.asuntos = sb.toString();
			Log.i(TAG, "medios de respuesta " + Propiedades.asuntos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
				squish.printStackTrace();
			}
		}

	}

	private void gettiposdesolicitud() {
		Log.i(TAG, "onPostExecute");
		InputStream inputStream = null;

		try {
			final StringBuilder sb = new StringBuilder();
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

			HttpUriRequest httpurirequest = new HttpGet(Propiedades.webservice + "/entidades/tiposdesolicitud");

			HttpResponse response = httpclient.execute(httpurirequest);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			Propiedades.tiposdesolicitudes = sb.toString();
			Log.i(TAG, Propiedades.tiposdesolicitudes);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
				squish.printStackTrace();
			}
		}
	}

	private void getmediosderespuesta() {
		Log.i(TAG, "onPostExecute");

		InputStream inputStream = null;

		try {
			final StringBuilder sb = new StringBuilder();
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

			HttpUriRequest httpurirequest = new HttpGet(Propiedades.webservice + "/entidades/mediosderespuesta");
			Log.i(TAG, "consulata" + Propiedades.webservice + "/entidades/mediosderespuesta	");

			HttpResponse response = httpclient.execute(httpurirequest);

			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			Propiedades.mediosderespuesta = sb.toString();
			Log.i(TAG, Propiedades.mediosderespuesta);

		} catch (Exception e) {
			Log.e("pqr", "error medios de respuesta");
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
				squish.printStackTrace();
			}
		}
	}

	private void getgruposvulnerables() {
		Log.i(TAG, "onPostExecute");

		InputStream inputStream = null;

		try {
			final StringBuilder sb = new StringBuilder();
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

			HttpUriRequest httpurirequest = new HttpGet(Propiedades.webservice + "/entidades/gruposvulnerables");

			HttpResponse response = httpclient.execute(httpurirequest);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			Propiedades.grupos_vulnerables = sb.toString();
			Log.i(TAG, Propiedades.grupos_vulnerables);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
				squish.printStackTrace();
			}
		}

	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(context, context.getString(R.string.cargando),
				context.getString(R.string.progress_dialog_enviando));

		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(context.getResources().getColor(R.color.aplicacion));
		} else {
			// divider.setBackground(micontext
			// .getDrawable(R.drawable.fondoactionbar));

		}
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(context.getResources().getColor(R.color.aplicacion));

		Log.i(TAG, "onPreExecute");
	}

	@Override
	protected void onProgressUpdate(Void... values) {

		Log.i(TAG, "onProgressUpdate");
	}

	@Override
	protected void onPostExecute(Void result) {
		dialog.dismiss();
		startactivity();

	}

	private void startactivity() {
		Intent formularioIntent = new Intent(context, FormularioCreacionActivity.class);

		formularioIntent.setFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		// formularioIntent.putExtra("EXIT", true);

		context.startActivity(formularioIntent);
		// ((Activity) context).finish();
	}

}
