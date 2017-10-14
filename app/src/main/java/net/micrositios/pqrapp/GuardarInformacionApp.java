package net.micrositios.pqrapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import net.micrositios.pqrapp.formulario.Campo;
import net.micrositios.pqrapp.formulario.Estilos;

public class GuardarInformacionApp {
	static Context con;
	static Activity act;

	public static void GuardarInformacionApp(Context con, Activity act) {

		GuardarInformacionApp.con = con;
		GuardarInformacionApp.act = act;
	}

	public static void Guardar_info(Bundle outState) {

		outState.putString("entidad", Propiedades.entidades);

		outState.putString("json_campos", Propiedades.json_campos);

		if (Propiedades.estilos_entidad.size() > 0) {
			JSONArray arra_estilos = new JSONArray();

			for (int i = 0; i < Propiedades.estilos_entidad.size(); i++) {

				JSONObject jo = new JSONObject();
				try {
					jo.put("id_entidad", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("color_barra_seccion", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("color_boton", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("color_boton_select", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("color_texto_comentario", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("color_texto_seccion", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("color_texto_campos", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("url_logo", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("logo_local", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("fecha_modificacion", Propiedades.estilos_entidad.get(i).getId_entidad());
					jo.put("version", Propiedades.estilos_entidad.get(i).getId_entidad());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				arra_estilos.put(jo);
			}

			outState.putString("estilos", arra_estilos.toString());

		}

		// outState.putString("json_list_documentos",
		// data_servicio.json_list_documentos);

	}

	public static void Cargar_info(Bundle savedInstanceState) {

		try {
			Propiedades.entidades = savedInstanceState.getString("entidad");
			Propiedades.json_campos = savedInstanceState.getString("json_campos");

			parseo_campos(Propiedades.json_campos);

			String array_estilos = "";
			array_estilos = savedInstanceState.getString("estilos");
			if (Propiedades.estilos_entidad.size() == 0) {
				JSONArray jest = new JSONArray(array_estilos);

				for (int i = 0; i < jest.length(); i++) {

					String id_entidad = "";
					String color_barra_seccion = "";
					String color_boton = "";
					String color_boton_select = "";
					String color_texto_comentario = "";
					String color_texto_seccion = "";
					String color_texto_campos = "";
					String url_logo = "";
					String logo_local = "";
					String fecha_modificacion = "";
					String version = "";

					try {

						id_entidad = jest.getJSONObject(i).getString("id_entidad");
						color_barra_seccion = jest.getJSONObject(i).getString("color_barra_seccion");
						color_boton = jest.getJSONObject(i).getString("color_boton");
						color_boton_select = jest.getJSONObject(i).getString("color_boton_select");
						color_texto_comentario = jest.getJSONObject(i).getString("color_texto_comentario");
						color_texto_seccion = jest.getJSONObject(i).getString("color_texto_seccion");
						color_texto_campos = jest.getJSONObject(i).getString("color_texto_campos");
						url_logo = jest.getJSONObject(i).getString("url_logo");
						logo_local = jest.getJSONObject(i).getString("logo_local");
						fecha_modificacion = jest.getJSONObject(i).getString("fecha_modificacion");
						version = jest.getJSONObject(i).getString("version");

						Estilos estilo = new Estilos(id_entidad, color_barra_seccion, color_boton, color_boton_select,
								color_texto_comentario, color_texto_seccion, color_texto_campos, url_logo, logo_local,
								fecha_modificacion, version);
						Propiedades.estilos_entidad.add(estilo);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void parseo_campos(String str_json) {

		boolean parseo = false; //
		Propiedades.json_campos = str_json;
		try {
			JSONObject jo = new JSONObject(str_json);
			String status = "";

			try {
				status = jo.getString("status");
			} catch (Exception e) { // TODO: handle exception

			}
			if (status.equals("success")) {
				parseo = true;
				try {
					JSONObject j_data = jo.getJSONObject("data");

					try {
						// String st_montos = j_data.getString("montos");

						JSONArray array = j_data.getJSONArray("campos");

						((MyApplication) GuardarInformacionApp.act.getApplication()).campos.clear();

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

								((MyApplication) GuardarInformacionApp.act.getApplication()).campos.add(campo_temp);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}

					} catch (Exception e) {
						// TODO: handle exception

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

	}

}
