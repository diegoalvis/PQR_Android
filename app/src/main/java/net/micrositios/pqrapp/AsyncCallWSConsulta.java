package net.micrositios.pqrapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AsyncCallWSConsulta extends AsyncTask<String, Void, Void> {
	private final String TAG = "micrositios";

	private static String codigoEntidad;
	private static String codigoSeguimiento;
	private static String respuestaSolicitud;
	EditText codigoView;
	String webService;
	private static String URL_RSS;
	private ProgressDialog dialog;
	Handler handler;
	String codigoListActivity = null;
	String codigo;
	boolean codigoEsValido = false;
	boolean codigoListActivityAprobado = false;
	Bundle extras = null;
	private final Context myContext;
	private boolean esConsultaManual = false;
	private String idgcm;

	public AsyncCallWSConsulta(Context context) {
		myContext = context;
	}

	public void setCodigoSeguimiento(String codigoSeguimientoValido) {
		codigoSeguimiento = codigoSeguimientoValido;
	}

	public void setCodigoEntidad(String codigoEntidadValido) {
		codigoEntidad = codigoEntidadValido;
	}

	public void setConsultaManual(Boolean consultaManual) {
		esConsultaManual = consultaManual;
	}

	@Override
	protected Void doInBackground(String... params) {
		Log.i(TAG, "doInBackground");

		// Log.i(TAG, "codigoentidad"+);
		getCodigoSeguimiento(codigoEntidad, codigoSeguimiento, idgcm);
		return null;
	}

	public String getIdgcm() {
		return idgcm;
	}

	public void setIdgcm(String idgcm) {
		this.idgcm = idgcm;
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.i(TAG, "onPostExecute");

		Log.i(TAG, respuestaSolicitud);

		URL_RSS = respuestaSolicitud;

		dialog.dismiss();
		RefreshFromFeedConsulta refresh = new RefreshFromFeedConsulta(myContext);
		refresh.setUrlRss(URL_RSS);
		if (esConsultaManual) {
			refresh.setConsultaManual();
		}
		refresh.refreshFromFeed();
	}

	@Override
	protected void onPreExecute() {
		Log.i(TAG, "onPreExecute");
		dialog = ProgressDialog.show(myContext, myContext.getString(R.string.cargando),
				myContext.getString(R.string.progress_dialog_enviando));
		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(myContext.getResources().getColor(R.color.aplicacion));
		} else {
			try {
				divider.setBackground(myContext.getResources().getDrawable(R.drawable.fondoactionbar));

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(myContext.getResources().getColor(R.color.aplicacion));

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		Log.i(TAG, "onProgressUpdate");
	}

	public void getCodigoSeguimiento(String codigoEntidad, String codigoSeguimiento, String idgcm) {
		Log.i(TAG + " getCodigoSeguimiento", codigoEntidad + " " + codigoSeguimiento);

		HttpClient httpclient = new DefaultHttpClient();

		HttpUriRequest httpurirequest;

		try {
			String url = Propiedades.webservice + "/solicitudes/solicitud/" + codigoSeguimiento;
			httpurirequest = new HttpGet(url);

			Log.i(TAG + " getCodigoSeguimiento", url);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpurirequest);

			// According to the JAVA API, InputStream constructor do nothing.
			// So we can't initialize InputStream although it is not an
			// interface
			InputStream inputStream = response.getEntity().getContent();

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}

			respuestaSolicitud = stringBuilder.toString();
			Log.d(TAG + " getCodigoSeguimiento", respuestaSolicitud);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG + " getCodigoSeguimiento", e.toString());
		}
	}

}
