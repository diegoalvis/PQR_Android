package net.micrositios.pqrapp.encuesta;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.micrositios.pqrapp.Propiedades;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WS_Rest_client extends AsyncTask<String, Void, Void> {

	private Context myContext;
	private String servicio;
	public boolean Adjunto = false;
	int posicion = 0;
	private MultipartEntity reqEntity;

	public String url_servicio = "";

	PruebaInternet prueba_internet;

	/******************** servicios prueba ************************/

	public static String Consultar_formulario = "Consultar_formulario";

	public static String Consultar_encuesta = "consultar_encuesta";

	public static String Enviar_respuesta = "insertar_respuestas";

	/*********************************************************************/

	/*
	 * [{"id":"9","entidad":
	 * "Agencia Presidencial de Cooperacion Internacional de Colombia "
	 * ,"webservice":"http:\/\/www.pqr-apc.gov.co\/desarrollo\/mobile\/WS.php"
	 * ,"codigo_entidad":"7" ,"sigla_entidad":"APC"
	 * ,"pagina":"http:\/\/www.pqr-apc.gov.co"
	 * ,"correo":"webmaster@apccolombia.gov.co" ,"publicado":"1" ,"tipo":"rest",
	 * "produccion":"0", "encuesta":"0"},
	 * {"id":"10","entidad":"Entidad de Prueba",
	 * "webservice":"http:\/\/mmacias.micrositios.us\/icanh\/mobile\/WS.php",
	 * "codigo_entidad":"10", "sigla_entidad":"PRUEBA",
	 * "pagina":"http:\/\/www.dermatologia.gov.co\/nuevo\/",
	 * "correo":"prueba@micros.net","publicado":"1",
	 * "tipo":"rest","produccion":"0", "encuesta":"0"}]
	 */

	public String sistema_usuario = "SR_V001";
	String url_metodo = "";
	Handler puente_envio;
	Message msg;
	String Respuesta = "";
	boolean envio = false;
	log_aplicacion log_app;

	public WS_Rest_client(Context myContext, String servicio) {
		this.myContext = myContext;
		this.servicio = servicio;
		msg = new Message();

		url_servicio = Propiedades.pagina;

		log_app = new log_aplicacion();
		log_app.setModulo_prueba("web service formulario");

		prueba_internet = new PruebaInternet(myContext);
		if (servicio.equals(Consultar_formulario)) {
			url_metodo = url_servicio
					+ "/info_formulario/index.php/api/consultarformulario";
			log_app.log_informacion(url_metodo);
		} else if (servicio.equals(Consultar_encuesta)) {
			url_metodo = url_servicio
					+ "/info_formulario/index.php/api/consultar_encuesta";
			log_app.log_informacion(url_metodo);
		} else if (servicio.equals(Enviar_respuesta)) {
			url_metodo = url_servicio
					+ "/info_formulario/index.php/api/insertar_respuestas";
			log_app.log_informacion(url_metodo);
		}

	}

	public void Consultar_formulario(String id_entidad) {
		try {

			reqEntity = new MultipartEntity();
			reqEntity.addPart("id_entidad", new StringBody(id_entidad));

		} catch (Exception e) {
			// TODO: handle exception
			log_app.log_error("Error Consultar_solicitud_vivienda multipart "
					+ e.toString());

		}

	}

	public void Enviar_respuesta(String id_entidad, String respuesta) {

		try {

			reqEntity = new MultipartEntity();
			reqEntity.addPart("id_entidad", new StringBody(id_entidad));
			reqEntity.addPart("respuestas", new StringBody(respuesta));

		} catch (Exception e) {
			// TODO: handle exception
			log_app.log_error("Error Consultar_solicitud_vivienda multipart "
					+ e.toString());

		}

	}

	public void Consultarformulario(String id_entidad) {
		try {

			reqEntity = new MultipartEntity();
			reqEntity.addPart("id_entidad", new StringBody(id_entidad));

		} catch (Exception e) {
			// TODO: handle exception
			log_app.log_error("Error Consultar_solicitud_vivienda multipart "
					+ e.toString());

		}

	}

	/*
	 * 
	 * URL de consumo: {url_servicdor}/vivienda_fiscal/api/cancelar MÃ©todo de
	 * consumo: POST parÃ¡metros requeridos: numero_documento Solo admite
	 * valores numÃ©ricos numero_carne Solo admite valores numÃ©ricos
	 * fecha_nacimiento se debe enviar en formato â€œdd/mm/aaaaâ€�
	 * codigo_temporal_solicitud Solo admite valores numÃ©ricos
	 */

	@Override
	protected Void doInBackground(String... arg0) {
		log_app.log_informacion("doInBackground");

		consultapost();

		return null;
	}

	private void consultapost() {

		log_app.log_informacion(servicio + " ");
		if (prueba_internet.estadoConexion()) {
			envio = true;
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					"http.protocol.content-charset", HTTP.UTF_8);
			HttpPost httppost = new HttpPost(url_metodo);

			try {
				httppost.setEntity(reqEntity);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				// According to the JAVA API, InputStream constructor do
				// nothing.
				// So we can't initialize InputStream although it is not an
				// interface
				InputStream inputStream = response.getEntity().getContent();
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream, "UTF_8");
				// InputStreamReader inputStreamReader = new
				// InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				StringBuilder stringBuilder = new StringBuilder();
				String bufferedStrChunk = null;
				while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
					stringBuilder.append(bufferedStrChunk);
				}
				log_app.log_informacion("Entro al servicio");
				Respuesta = stringBuilder.toString();
				log_app.log_informacion("respuesta servicio " + Respuesta);
			} catch (Exception e) {
				e.printStackTrace();
				log_app.log_error("Error servicio " + servicio + "  "
						+ e.toString());
			}
		}
	}

	// public void obtener_detalles() {
	// url_metodo = url_servicio + "/recrea/api/detalleinscripcion";
	//
	// for (int i = 0; i < data_Recrea.actividades.size(); i++) {
	//
	// Consultar_detalle_inscripcion(data_Recrea.documento,
	// data_Recrea.actividades.get(i).getCodigo_actividad());
	//
	// consultapost();
	//
	// Parseo_json_recrea parseo = new Parseo_json_recrea();
	//
	// Actividad act = parseo.parseo_consulta_detalles(Respuesta,
	// data_Recrea.actividades.get(i));
	//
	// data_Recrea.actividades.get(i).setactividad(act);
	//
	// }
	// }

	public void obtener_filtros() {

	}

	@Override
	protected void onPreExecute() {

		log_app.log_informacion("onPreExecute");

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		log_app.log_informacion("onProgressUpdate");
	}

	@Override
	protected void onPostExecute(Void result) {
		if (envio) {
			log_app.log_informacion("onPostExecute");
			if (servicio.equals(Consultar_formulario)) {
				if (parseo_campos(Respuesta)) {

					msg.what = 1;
					puente_envio.sendMessage(msg);

				}
			} else if (servicio.equals(Consultar_encuesta)) {
				if (parseo_encuesta(Respuesta)) {

					msg.what = 1;
					puente_envio.sendMessage(msg);

				}
			} else if (servicio.equals(Enviar_respuesta)) {
				if (parseo_envio(Respuesta)) {

					msg.what = 1;
					puente_envio.sendMessage(msg);

				}
			}

		}

	}

	public byte[] FileToArrayOfBytes(String archivo) {
		File file = new File(archivo);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			FileInputStream fis = new FileInputStream(file);
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
			}
			fis.close();
		} catch (Exception ex) {
			Log.d("Micrositios", ex.toString());
		}
		byte[] bytes = bos.toByteArray();
		return bytes;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public boolean isAdjunto() {
		return Adjunto;
	}

	public void setAdjunto(boolean adjunto) {
		Adjunto = adjunto;
	}

	public Handler getPuente_envio() {
		return puente_envio;
	}

	public void setPuente_envio(Handler puente_envio) {
		this.puente_envio = puente_envio;
	}

	public boolean parseo_envio(String str_json) {

		boolean parseo = false; //

		try {

			JSONObject jo = new JSONObject(str_json);
			String status = "";

			try {
				status = jo.getString("status");
			} catch (Exception e) { // TODO: handle exception
				log_app.log_error("error parseo " + e.toString());
			}
			if (status.equals("success")) {
				parseo = true;

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return parseo;

	}

	public boolean parseo_campos(String str_json) {

		boolean parseo = false; //

		try {
			JSONObject jo = new JSONObject(str_json);
			String status = "";

			try {
				status = jo.getString("status");
			} catch (Exception e) { // TODO: handle exception
				log_app.log_error("error parseo " + e.toString());
			}
			if (status.equals("success")) {
				parseo = true;
				try {
					JSONObject j_data = jo.getJSONObject("data");

					try {

						JSONArray array = j_data.getJSONArray("campos");

						Datos_formulario.campos = new ArrayList<Campo>();

						for (int i = 0; i < array.length(); i++) {

							try {

								Campo campo_temp = new Campo(
										array.getJSONObject(i).getString(
												"id_campo"), array
												.getJSONObject(i).getString(
														"nombre"), array
												.getJSONObject(i).getString(
														"tamanomax"), array
												.getJSONObject(i).getString(
														"tipo"), array
												.getJSONObject(i).getString(
														"elemento"));

								Datos_formulario.campos.add(campo_temp);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						log_app.log_error("error obteniendo campos"
								+ e.toString());

					}

				} catch (Exception e) { //

				}
			}
			// else if (status.equals("error")) {
			// parseo = false;
			//
			// log_app.log_informacion(stmsg);
			//
			// }
		} catch (Exception e) {
			// TODO: handle exception
		}
		//
		return parseo;

	}

	public boolean parseo_encuesta(String str_json) {

		boolean parseo = false; //
		try {
			JSONObject jo = new JSONObject(str_json);
			String status = "";

			try {
				status = jo.getString("status");
			} catch (Exception e) { // TODO: handle exception
				log_app.log_error("error parseo " + e.toString());
			}
			if (status.equals("success")) {
				parseo = true;
				try {
					JSONObject j_data = jo.getJSONObject("data");

					try {
						// String st_montos = j_data.getString("montos");

						JSONArray array = j_data.getJSONArray("preguntas");

						Encuesta.preguntas = new ArrayList<Pregunta>();

						for (int i = 0; i < array.length(); i++) {

							try {

								Pregunta pregunta = new Pregunta(array
										.getJSONObject(i).getString(
												"id_pregunta"),
										eliminarTags(array.getJSONObject(i)
												.getString("pregunta")));

								JSONArray array_respuestas = array
										.getJSONObject(i).getJSONArray(
												"respuestas");
								log_app.log_informacion("respuesta servicio "
										+ array_respuestas.toString());
								ArrayList<Respuesta> respuestas = new ArrayList<Respuesta>();

								for (int j = 0; j < array_respuestas.length(); j++) {

									Respuesta res = new Respuesta(
											array_respuestas.getJSONObject(j)
													.getString("id_respuesta"),
											array_respuestas.getJSONObject(j)
													.getString("respuesta"));

									respuestas.add(res);

								}
								pregunta.setRespuestas(respuestas);
								Encuesta.preguntas.add(pregunta);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						log_app.log_error("error obteniendo campos"
								+ e.toString());

					}

				} catch (Exception e) { //

				}
			}
			// else if (status.equals("error")) {
			// parseo = false;
			//
			// log_app.log_informacion(stmsg);
			//
			// }
		} catch (Exception e) {
			// TODO: handle exception
		}
		//
		return parseo;

	}

	public String eliminarTags(String cadena) {
		while (true) {
			int izdaTag = cadena.indexOf('<');
			if (izdaTag < 0)
				return cadena;
			int derTag = cadena.indexOf('>', izdaTag);
			if (derTag < 0)
				return cadena;
			cadena = cadena.substring(0, izdaTag) + " "
					+ cadena.substring(derTag + 1);
		}
	}
}
