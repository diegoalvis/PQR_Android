package net.micrositios.pqrapp.formulario;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import net.micrositios.pqrapp.MyApplication;
import net.micrositios.pqrapp.Propiedades;
import net.micrositios.pqrapp.encuesta.PruebaInternet;

public class WS_Rest_client extends AsyncTask<String, Void, Void> {

	private Context myContext;
	private String servicio;
	public boolean Adjunto = false;
	int posicion = 0;
	private MultipartEntity reqEntity;

	public String url_servicio = "http://www.micrositios.net";
	// public String url_servicio =
	// "http://jgomez.micrositios.us/micros-portal-2015-portalmicros";

	PruebaInternet prueba_internet;
	String consulta_list = "";// tipo de consulta getlista
	/******************** servicios prueba ************************/

	public static String Consultar_formulario = "Consultar_formulario";
	public static String Enviar_respuesta = "insertar_respuestas";
	public static String getlista = "getlista";
	public static String Consultar_proceso = "Consultar_proceso";
	public static String Consultar_estilo = "Consultar_estilo";

	/*********************************************************************/

	public String sistema_usuario = "SR_V001";
	String url_metodo = "";
	Handler puente_envio;
	Message msg;
	String Respuesta = "";
	boolean envio = false;
	log_aplicacion log_app;
	Activity act;

	public WS_Rest_client(Context myContext, Activity act, String servicio) {
		this.myContext = myContext;
		this.servicio = servicio;
		this.act = act;
		msg = new Message();
		log_app = new log_aplicacion();
		log_app.setModulo_prueba("web service formulario");
		prueba_internet = new PruebaInternet(myContext);
		if (servicio.equals(Consultar_formulario)) {
			url_metodo = url_servicio + "/pqr_movil/index.php/api/consultarformulario";
			log_app.log_informacion(url_metodo);
		} else if (servicio.equals(getlista)) {
			url_metodo = Propiedades.webservice.replace("/WS.php", "") + "/Getlist.php";
			log_app.log_informacion(url_metodo);
		} else if (servicio.equals(Consultar_proceso)) {

			url_metodo = url_servicio + "/pqr_movil/index.php/api/consultarproceso";
		} else if (servicio.equals(Consultar_estilo)) {

			url_metodo = url_servicio + "/pqr_movil/index.php/api/consultarestilo";
		}

	}

	public void Consultar_proceso(String id) {
		try {

			reqEntity = new MultipartEntity();
			reqEntity.addPart("id_entidad", new StringBody(id));

		} catch (Exception e) {
			// TODO: handle exception
			log_app.log_error("Error Consultar_solicitud_vivienda multipart " + e.toString());

		}

	}

	public void Consultar_estilo(String id) {
		try {

			reqEntity = new MultipartEntity();
			reqEntity.addPart("id_entidad", new StringBody(id));

		} catch (Exception e) {
			// TODO: handle exception
			log_app.log_error("Error Consultar_solicitud_vivienda multipart " + e.toString());

		}

	}

	public void Consultar_formulario(String id) {
		try {

			reqEntity = new MultipartEntity();
			reqEntity.addPart("id_formulario", new StringBody(id));

		} catch (Exception e) {
			// TODO: handle exception
			log_app.log_error("Error Consultar_solicitud_vivienda multipart " + e.toString());

		}

	}

	public void getlista(String consulta, String id) {

		try {
			consulta_list = consulta;
			reqEntity = new MultipartEntity();

			reqEntity.addPart("consulta", new StringBody(consulta));
			Log.i("Pqr", "lista consulta" + consulta);

			reqEntity.addPart("id", new StringBody(id));

		} catch (Exception e) {
			// TODO: handle exception
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

	private void getparametros() {

		InputStream inputStream = null;

		try {
			final StringBuilder sb = new StringBuilder();

			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

			HttpUriRequest httpurirequest = new HttpGet(Propiedades.webservice + "/entidades/parametros");

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

	private void consultapost() {

		log_app.log_informacion(servicio + " ");
		if (prueba_internet.estadoConexion()) {
			envio = true;
			int intentos = 0;
			do {
				HttpClient httpclient = new DefaultHttpClient();
				httpclient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
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
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF_8");
					// InputStreamReader inputStreamReader = new
					// InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
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
					log_app.log_error("Error servicio " + servicio + "  " + e.toString());
				}
				intentos++;
			} while (Respuesta.length() < 80 && intentos < 3);
			Log.i("pqr", "respuesta cliente post " + Respuesta);
		}
	}

	public void obtener_filtros() {

	}

	@Override
	protected void onPreExecute() {

		long timeout = 15;

		// Timer timer = new Timer();
		// timer.schedule(new TimerTask() {
		// public void run() {
		// if (Respuesta.equals("")) {
		// try {
		// // cancelado = true;
		// // ws.cancel(true);
		// // data_sesion.mensaje_error = new Mensaje(false,
		// // "Tiempo de espera de conexión agotado");
		// // data_vivienda.mensaje_error = new Mensaje(false,
		// // "Tiempo de espera de conexión agotado");
		// // msg.what = 0;
		// // puente_envio.dispatchMessage(msg);
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		//
		// }
		// }
		// }, timeout * 1000);

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

				} else {
					msg.what = 0;
					puente_envio.sendMessage(msg);

				}
			} else if (servicio.equals(getlista)) {
				if (consulta_list.equals("parametros")) {
					if (Respuesta.length() > 0) {
						msg.what = 1;
						puente_envio.sendMessage(msg);
						Propiedades.parametros = Respuesta;
					} else {
						msg.what = 0;
						puente_envio.sendMessage(msg);

					}

				} else {
					if (parseo_lista(Respuesta)) {

						msg.what = 1;
						puente_envio.sendMessage(msg);

					} else {
						msg.what = 0;
						puente_envio.sendMessage(msg);

					}

				}
			} else if (servicio.equals(Consultar_proceso)) {
				if (parseo_Consultar_proceso(Respuesta)) {

					msg.what = 1;
					puente_envio.sendMessage(msg);

				} else {
					msg.what = 0;
					puente_envio.sendMessage(msg);

				}
			} else if (servicio.equals(Consultar_estilo)) {
				if (parseo_Consultar_estilo(Respuesta)) {

					msg.what = 1;
					puente_envio.sendMessage(msg);

				} else {
					msg.what = 0;
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
		Propiedades.json_campos = str_json;
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

						JSONArray array = j_data.getJSONArray("campos");

						((MyApplication) act.getApplication()).campos.clear();

						for (int i = 0; i < array.length(); i++) {

							try {

								int id = 0;
								int posicion = 0;
								String tipo_de_campo = "";
								String nombre = "";
								int tamano = 0;
								String tipo = "";
								boolean obligatorio = false;
								String nombre_parametro = "";
								String id_solicitud = "";
								String consulta = "";
								int id_padre = 0;
								String msg_error = "";
								int tamano_minimo = 0;
								int num_hijos = 0;
								String id_parametro = "";
								boolean guardar = false;
								String seccion = "";
								try {
									id = array.getJSONObject(i).getInt("idcampos");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									tipo_de_campo = array.getJSONObject(i).getString("tipo_de_campo");
								} catch (Exception e) {
									// TODO: handle exception
								}
								try {
									nombre = array.getJSONObject(i).getString("nombre");
								} catch (Exception e) {
									// TODO: handle exception
								}
								try {
									tamano = array.getJSONObject(i).getInt("tamano_max");
								} catch (Exception e) {
									// TODO: handle exception
								}
								try {
									tipo = array.getJSONObject(i).getString("tipo");

								} catch (Exception e) {
									// TODO: handle exception
								}
								try {
									String obli = array.getJSONObject(i).getString("obligatorio");
									if (obli.equals("0")) {
										obligatorio = false;
									} else if (obli.equals("1")) {
										obligatorio = true;
									}

								} catch (Exception e) {
									// TODO: handle exception

								}

								try {
									nombre_parametro = array.getJSONObject(i).getString("nombre_parametro");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {

									id_parametro = array.getJSONObject(i).getString("id_parametro");

								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									consulta = array.getJSONObject(i).getString("consulta");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									id_padre = array.getJSONObject(i).getInt("id_padre");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									num_hijos = array.getJSONObject(i).getInt("num_hijos");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									tamano_minimo = array.getJSONObject(i).getInt("tamano_minimo");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									msg_error = array.getJSONObject(i).getString("msg_error");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									String sguardar = array.getJSONObject(i).getString("guardar");
									if (sguardar.equals("0")) {
										guardar = false;
									} else {
										guardar = true;
									}

								} catch (Exception ex) {
									// TODO: handle exception
								}

								try {
									seccion = array.getJSONObject(i).getString("seccion");

								} catch (Exception ex) {
									// TODO: handle exception
								}

								Campo campo_temp = new Campo(id, tipo_de_campo, nombre, tamano, tipo, obligatorio,
										nombre_parametro, id_parametro, id_solicitud, consulta, id_padre, msg_error,
										tamano_minimo, num_hijos, guardar, seccion);

								((MyApplication) act.getApplication()).campos.add(campo_temp);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}

					} catch (Exception e) {
						// TODO: handle exception
						log_app.log_error("error obteniendo campos" + e.toString());

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

	public boolean parseo_Consultar_proceso(String str_json) {

		boolean parseo = false; //

		try {
			((MyApplication) act.getApplication()).procesos.clear();

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
						((MyApplication) act.getApplication()).procesos.clear();
						JSONArray array = j_data.getJSONArray("procesos");

						for (int i = 0; i < array.length(); i++) {

							try {

								String id = "";
								String nombre = "";
								String id_formulario = "";
								try {
									id = array.getJSONObject(i).getString("idprocesos");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									nombre = array.getJSONObject(i).getString("nombre_proceso");
								} catch (Exception e) {
									// TODO: handle exception
								}

								try {
									id_formulario = array.getJSONObject(i).getString("id_formulario");
									Proceso nuevo_p = new Proceso(id, nombre, id_formulario);

									((MyApplication) act.getApplication()).procesos.add(nuevo_p);
								} catch (Exception e) {
									// TODO: handle exception
								}

							} catch (Exception e) {
								// TODO: handle exception
							}

						}

					} catch (Exception e) {
						// TODO: handle exception
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return parseo;
	}

	public boolean parseo_Consultar_estilo(String str_json) {

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
					String id_entidad = "";
					String color_barra_seccion = "";
					String color_boton = "";
					String color_boton_select = "";
					String color_texto_comentario = "";
					String color_texto_seccion = "";
					String color_texto_campos = "";
					String url_logo = "";
					String fecha_modificacion = "";
					String version = "";
					try {

						id_entidad = j_data.getString("id_entidad");
						color_barra_seccion = j_data.getString("color_barra_seccion");
						color_boton = j_data.getString("color_boton");
						color_boton_select = j_data.getString("color_boton_select");
						color_texto_comentario = j_data.getString("color_texto_comentario");
						color_texto_seccion = j_data.getString("color_texto_seccion");
						color_texto_campos = j_data.getString("color_texto_campos");
						url_logo = j_data.getString("url_logo");
						fecha_modificacion = j_data.getString("fecha_modificacion");
						version = j_data.getString("version");

						Estilos estilo_entidad = new Estilos(id_entidad, color_barra_seccion, color_boton,
								color_boton_select, color_texto_comentario, color_texto_seccion, color_texto_campos,
								url_logo, fecha_modificacion, version);

						Propiedades.estilos_entidad.add(estilo_entidad);

					} catch (Exception e) {
						// TODO: handle exception
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return parseo;
	}

	public boolean parseo_lista(String str_json) {

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
						String nombre_lista = "";
						try {
							nombre_lista = j_data.getString("nombre_lista");
						} catch (Exception e) {
							// TODO: handle exception
						}

						JSONArray array = j_data.getJSONArray("list");

						ArrayList<Item> lista_item = new ArrayList<Item>();
						Lista_spinner lista = new Lista_spinner(nombre_lista, lista_item);
						for (int i = 0; i < array.length(); i++) {
							// list.add(array.getJSONObject(i).getString("interestKey"));

							try {

								Item item_temp = new Item(array.getJSONObject(i).getString("id"),
										array.getJSONObject(i).getString("nombre"));
								lista.add_item(item_temp);

							} catch (Exception e) {
								// TODO: handle exception
								log_app.log_error("error obteniendo listado" + e.toString());
							}

							/*
							 * tipo_persona": "Acudiente",
							 * 
							 * "tipo_documento": "CC",
							 * 
							 * "numero_documento": "3333333333",
							 * 
							 * "nombre": "Nombre Acudiente"}
							 */

						}
						for (int i = 0; i < ((MyApplication) act.getApplication()).listas.size(); i++) {
							if (((MyApplication) act.getApplication()).listas.get(i).getNombre().equals(nombre_lista)) {
								((MyApplication) act.getApplication()).listas.remove(i);
							}

						}

						((MyApplication) act.getApplication()).listas.add(lista);
					} catch (Exception e) {
						// TODO: handle exception
						log_app.log_error("error obteniendo campos" + e.toString());

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

}
