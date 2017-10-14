package net.micrositios.pqrapp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncCallWSBorrar extends AsyncTask<String, Void, Void> {
	private final String TAG = "micrositios";
	
	private String idgcm;
	private String codigoEntidad;
	private String codigoSeguimiento;
	private String respuestaSolicitud;
	private String id_solicitud;
	private Context myContext;
	private Handler puente;

	private Message msg; 
	
	
	public Handler getPuente() {
		return puente;
	}

	public void setPuente(Handler puente) {
		this.puente = puente;
	}

	public AsyncCallWSBorrar(Context context) {
		this.myContext = context;
	}
	
	public void setCodigoSeguimiento(String codigoSeguimientoValido) {
		codigoSeguimiento = codigoSeguimientoValido;
	}
	
	public void setCodigoEntidad(String codigoEntidadValido) {
		codigoEntidad = codigoEntidadValido;
	}

	@Override
	protected Void doInBackground(String... params) {
		Log.i(TAG, "doInBackground");
		borrarCodigoSeguimiento(codigoEntidad, codigoSeguimiento, idgcm);
		return null;
	}

	public String getIdgcm() {
		return idgcm;
	}

	public void setIdgcm(String idgcm) {
		this.idgcm = idgcm;
	}
	
	public String getId_solicitud() {
		return id_solicitud;
	}

	public void setId_solicitud(String id_solicitud) {
		this.id_solicitud = id_solicitud;
	}

	@Override
	protected void onPostExecute(Void result) {
		puente.sendMessage(msg);
	}

	@Override
	protected void onPreExecute() {
		msg=new Message();
		//this.dialog = ProgressDialog.show(myContext, myContext.getString(R.string.cargando), myContext.getString(R.string.progress_dialog_enviando));
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		Log.i(TAG, "onProgressUpdate");
	}
	
	public void borrarCodigoSeguimiento(String codigoEntidad, String codigoSeguimiento, String idgcm) {
		Log.i(TAG+" getCodigoSeguimiento", codigoEntidad+" "+codigoSeguimiento);
		try {
			URI uri = new URI(Propiedades.WSDIRECTORIO+"/solicitudes/solicitud/");
	    	HttpClient client = new DefaultHttpClient();
	    	HttpPut put= new HttpPut(uri);
	    	List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	    	pairs.add(new BasicNameValuePair("codigo_entidad", codigoEntidad));
	    	pairs.add(new BasicNameValuePair("codigo_solicitud", codigoSeguimiento));
	    	pairs.add(new BasicNameValuePair("id_gcm", idgcm));
	    	put.setEntity(new UrlEncodedFormEntity(pairs));
	    	HttpResponse response = client.execute(put);
	    	InputStream inputStream = response.getEntity().getContent();
	    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String bufferedStrChunk = null;

            while((bufferedStrChunk = bufferedReader.readLine()) != null){
               stringBuilder.append(bufferedStrChunk);
            }
            
            respuestaSolicitud=stringBuilder.toString();
            
            Log.d(TAG+" getCodigoSeguimiento", respuestaSolicitud);
            
            JSONObject jo = new JSONObject(respuestaSolicitud);
			JSONObject jo_error = jo.getJSONObject("error");

			String cod_error = jo_error.getString("codigo_error");
			String mensaje_error = jo_error.getString("mensaje");
			
			if (cod_error.contentEquals("0")) {
				DBAdapter db = new DBAdapter(myContext);
	        	db.open();
	        	if (db.deleteSolicitud(id_solicitud)) {
	        		msg.what=0;
	        		msg.obj=myContext.getString(R.string.eliminar_exito);
	        	} else{
	        		msg.what=1;
	        		msg.obj=myContext.getString(R.string.eliminar_fracaso);
	        	}
	        	db.close();
			}
			else {
				msg.what=1;
        		msg.obj=myContext.getString(R.string.eliminar_fracaso)+" "+mensaje_error;
			}
	}
	    catch (NullPointerException e){
	    	e.printStackTrace();
	    	Log.d(TAG+" getCodigoSeguimiento NPE", e.toString());
	    } catch (UnsupportedEncodingException e) {
			Log.d(TAG+" getCodigoSeguimiento UEE", e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			msg.what=1;
    		msg.obj=myContext.getString(R.string.problema_internet_o_host);
			Log.d(TAG+" getCodigoSeguimiento CPE", e.toString());
			e.printStackTrace();
		} catch (IOException e) {//PROBLEMA DE CONEXION A INTERNET
			msg.what=2;
    		msg.obj=myContext.getString(R.string.problema_internet_o_host);
			Log.d(TAG+" getCodigoSeguimiento IOE", e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			DBAdapter db = new DBAdapter(myContext);
        	db.open();
        	if (db.deleteSolicitud(id_solicitud)) {
        		msg.what=0;
        		msg.obj=myContext.getString(R.string.eliminar_exito)+myContext.getString(R.string.eliminar_en_red_fracaso);
        	} else{
        		msg.what=1;
        		msg.obj=myContext.getString(R.string.eliminar_fracaso);
        	}
        		//Toast.makeText(myContext, myContext.getString(R.string.eliminar_fracaso),
        		//		Toast.LENGTH_LONG).show();
        	db.close();
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Log.d(TAG+" getCodigoSeguimiento USE", e.toString());
			e.printStackTrace();
		}
	}
}
