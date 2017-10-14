package net.micrositios.pqrapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import net.micrositios.pqrapp.formulario.Campo;
import net.micrositios.pqrapp.formulario.FormularioActivity;
import net.micrositios.pqrapp.formulario.Lista_spinner;
import net.micrositios.pqrapp.formulario.WS_Rest_client;

@SuppressLint("NewApi")
public class SeleccionarEntidad extends Activity {

	private Builder chooseEntidadDialog;
	private int indexEntidad;
	private String codigoEntidad;
	private String PATH;
	private int num_carga = 0;
	private Context con;
	private ProgressDialog dialog;
	protected int desarrollo;
	protected int TOQUES = 4;
	private String IdGCM;
	Activity act;
	List<CharSequence> procesos = new ArrayList<CharSequence>();

	boolean cosulta_formulario = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.PATH = this.getFilesDir() + File.separator;
		setContentView(R.layout.lista_entidades);

		if (savedInstanceState != null) {
			GuardarInformacionApp.GuardarInformacionApp(this, this);
			GuardarInformacionApp.Cargar_info(savedInstanceState);
		}

		con = this;
		act = this;
		// showChooseEntidadDialog();
		ListView lista = (ListView) findViewById(R.id.listView_entidad);

		ArrayList<ItemimagenInfo> lista_en = conseguir_entidades_lista();
		ItemImagenAdapter adapter = new ItemImagenAdapter(this, lista_en);
		lista.setAdapter(adapter);

		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				seleccionar_entidad(position);
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		GuardarInformacionApp.Guardar_info(outState);

	}

	@SuppressLint("NewApi")
	private void showChooseEntidadDialog() {
		chooseEntidadDialog = new AlertDialog.Builder(this);
		chooseEntidadDialog.setCancelable(true);
		// chooseEntidadDialog.setTitle(this
		// .getString(R.string.choose_entidades_dialog));

		List<CharSequence> mHelperNames = procesos;
		if (mHelperNames.isEmpty()) {

			Toast.makeText(this, getString(R.string.ninguna_registrada), Toast.LENGTH_SHORT).show();
		} else {
			chooseEntidadDialog.setSingleChoiceItems(mHelperNames.toArray(new CharSequence[mHelperNames.size()]), 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							indexEntidad = whichButton;
						}
					});

			chooseEntidadDialog.setPositiveButton(R.string.boton_entidades_dialog,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {

							cargar_formulario_dinamico(((MyApplication) act.getApplication()).procesos.get(indexEntidad)
									.getId_formulario());

						}
					});
			chooseEntidadDialog.setNegativeButton(R.string.boton_cancelar, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			});
			chooseEntidadDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.cancel();
				}
			});

			// chooseEntidadDialog.create();
			AlertDialog alert = chooseEntidadDialog.create();
			alert.show();

			Button cancelar = (Button) alert.getButton(AlertDialog.BUTTON_NEGATIVE);

			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				cancelar.setBackgroundDrawable((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
			} else {
				cancelar.setBackground((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
			}

			cancelar.setTextColor(this.getResources().getColor(R.color.white));

			Button aceptar = (Button) alert.getButton(AlertDialog.BUTTON_POSITIVE);
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				aceptar.setBackgroundDrawable((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
			} else {
				aceptar.setBackground((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
			}
			aceptar.setTextColor(this.getResources().getColor(R.color.white));

			int dividerId = alert.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
			View divider = alert.findViewById(dividerId);
			sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

				divider.setBackgroundColor(this.getResources().getColor(R.color.Azul_oscuro));
			} else {
				// divider.setBackground(micontext
				// .getDrawable(R.drawable.fondoactionbar));

			}
			int textViewId = alert.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
			TextView tv = (TextView) alert.findViewById(textViewId);
			tv.setTextColor(this.getResources().getColor(R.color.aplicacion));

		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		reanudar();
	}

	private void reanudar() {

		if ((Propiedades.entidades.length()) < 80) {
			conseguirentidades();
			onBackPressed();

			// getentidades();
			//
			// ListView lista = (ListView) findViewById(R.id.listView_entidad);
			//
			// ArrayList<ItemimagenInfo> lista_en = conseguir_entidades_lista();
			// ItemImagenAdapter adapter = new ItemImagenAdapter(this,
			// lista_en);
			// lista.setAdapter(adapter);
			//
			// lista.setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> parent, View view,
			// int position, long id) {
			// seleccionar_entidad(position);
			// }
			// });
		}

	}

	public void dialog_progress() {

		dialog = ProgressDialog.show(con, con.getString(R.string.cargando),
				con.getString(R.string.progress_dialog_enviando));

		dialog.setOnKeyListener(new Dialog.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {

					dialog.cancel();
				}
				return false;
			}
		});

		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(con.getResources().getColor(R.color.aplicacion));
		} else {
			divider.setBackground(getDrawable(R.drawable.fondoactionbar));

		}
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(con.getResources().getColor(R.color.aplicacion));

	}

	public void cargar_estilo(String id_entidad) {

		WS_Rest_client ws = new WS_Rest_client(con, act, WS_Rest_client.Consultar_estilo);

		Handler puente_envio = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == 0) {

					Toast.makeText(con, con.getResources().getString(R.string.error_conexion_servidor),
							Toast.LENGTH_LONG);

				}
			}
		};

		ws.setPuente_envio(puente_envio);
		ws.Consultar_estilo(id_entidad);
		ws.execute();
	}

	private void cargar_formulario_dinamico(String id_formulario) {
		num_carga = 0;

		if (!cosulta_formulario) {
			((MyApplication) act.getApplication()).listas.clear();
			cosulta_formulario = true;
			((MyApplication) act.getApplication()).listas = new ArrayList<Lista_spinner>();

			WS_Rest_client ws = new WS_Rest_client(con, act, WS_Rest_client.Consultar_formulario);
			dialog_progress();
			Handler puente_envio = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {

						for (int i = 0; i < ((MyApplication) act.getApplication()).campos.size(); i++) {
							Campo campo = ((MyApplication) act.getApplication()).campos.get(i);
							if (campo.getTipo().equals("list") && campo.getId_padre() == 0) {

								num_carga++;

								WS_Rest_client ws = new WS_Rest_client(con, act, WS_Rest_client.getlista);

								Handler puente_envio = new Handler() {
									@Override
									public void handleMessage(Message msg) {
										if (msg.what == 1) {
											Log.i("numero", "numero de consultas" + num_carga);
											Log.i("numero", "tamaño lista" + MyApplication.listas.size());

											if (num_carga == MyApplication.listas.size()) {
												WS_Rest_client ws = new WS_Rest_client(con, act,
														WS_Rest_client.getlista);

												Handler puente_envio = new Handler() {
													@Override
													public void handleMessage(Message msg) {
														if (msg.what == 1) {
															cosulta_formulario = false;
															Intent intent = new Intent(SeleccionarEntidad.this,
																	FormularioActivity.class);

															startActivity(intent);
															dialog.cancel();
														} else if (msg.what == 0) {
															dialog.cancel();
															cosulta_formulario = false;
															Toast.makeText(con,
																	con.getResources().getString(
																			R.string.error_conexion_servidor),
																	Toast.LENGTH_LONG);
														}
													}
												};

												ws.setPuente_envio(puente_envio);
												ws.getlista("parametros", "2");
												ws.execute();

											}

										}

										else if (msg.what == 0) {
											dialog.cancel();
											Toast.makeText(con,
													con.getResources().getString(R.string.error_conexion_servidor),
													Toast.LENGTH_LONG);

										}
									}
								};

								ws.setPuente_envio(puente_envio);
								ws.getlista(campo.getConsulta(), "2");
								ws.execute();

							}

						}

					} else if (msg.what == 0) {
						dialog.cancel();
						Toast.makeText(con, con.getResources().getString(R.string.error_conexion_servidor),
								Toast.LENGTH_LONG);

					}
				}
			};

			ws.setPuente_envio(puente_envio);
			ws.Consultar_formulario(id_formulario);
			ws.execute();
		} else {
			Toast.makeText(this, "cargando informacion", Toast.LENGTH_LONG).show();

		}

	}

	private void obtener_proceso(String entidad) {

		cargar_estilo(entidad);
		((MyApplication) act.getApplication()).procesos.clear();
		procesos.clear();

		((MyApplication) act.getApplication()).listas.clear();
		WS_Rest_client ws = new WS_Rest_client(con, act, WS_Rest_client.Consultar_proceso);
		dialog_progress();
		Handler puente_envio = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					dialog.cancel();
					if (((MyApplication) act.getApplication()).procesos.size() > 1) {

						for (int i = 0; i < ((MyApplication) act.getApplication()).procesos.size(); i++) {

							procesos.add(((MyApplication) act.getApplication()).procesos.get(i).getNombre());

						}
						showChooseEntidadDialog();

					} else if (((MyApplication) act.getApplication()).procesos.size() == 0) {
						DatosFormulario df = new DatosFormulario(con);
						df.setId(codigoEntidad);
						df.execute();

					} else {

						cargar_formulario_dinamico(
								((MyApplication) act.getApplication()).procesos.get(0).getId_formulario());

					}

				} else if (msg.what == 0) {
					dialog.cancel();
					Toast.makeText(con, con.getResources().getString(R.string.error_conexion_servidor),
							Toast.LENGTH_LONG);

				}
			}
		};

		ws.setPuente_envio(puente_envio);
		ws.Consultar_proceso(entidad);
		ws.execute();

	}

	private List<CharSequence> conseguir_entidades() {
		List<CharSequence> mHelperNames = new ArrayList<CharSequence>();

		try {
			JSONArray ja = new JSONArray(Propiedades.entidades);

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("entidad");
				mHelperNames.add(entidad);
			}

		} catch (JSONException je) {
			je.printStackTrace();
		} catch (NullPointerException npe) {
			Toast.makeText(this, getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT).show();
		}

		return mHelperNames;
	}

	private ArrayList<ItemimagenInfo> conseguir_entidades_lista() {
		ArrayList<ItemimagenInfo> mHelperNames = new ArrayList<ItemimagenInfo>();

		try {
			JSONArray ja = new JSONArray(Propiedades.entidades);

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("entidad");
				String id = jo.getString("id");

				for (int j = 0; j < Propiedades.estilos_entidad.size(); j++) {

					if (Propiedades.estilos_entidad.get(i).getId_entidad().equals(id)) {

						ItemimagenInfo item = new ItemimagenInfo(i, entidad, entidad);
						item.setUriImagen(Propiedades.estilos_entidad.get(i).getUrl_logo());
						mHelperNames.add(item);
						break;

					}

				}

			}

		} catch (JSONException je) {
			je.printStackTrace();
		} catch (NullPointerException npe) {
			Toast.makeText(this, getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT).show();
		}

		return mHelperNames;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.listado_entidades_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.consulta_manual_menu_item) {
			consultarsolicitud();
			return true;
		} else if (item.getItemId() == R.id.ayuda_menu_item) {
			showInstruccionesDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	private void consultarsolicitud() {
		Intent intent = new Intent(this, ConsultarSolicitudActivity.class);
		startActivity(intent);
		finish();
	}

	private void showInstruccionesDialog() {
		final Dialog instruccionesDialog = new Dialog(this);

		// instruccionesDialog.setTitle(this
		// .getString(R.string.informacion_dialogo_titulo));
		// instruccionesDialog.setCancelable(false);
		instruccionesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		instruccionesDialog.setContentView(R.layout.instrucciones_dialog);

		final ScrollView sv_instrucciones = (ScrollView) instruccionesDialog.findViewById(R.id.sv_instrucciones);
		final ScrollView sv_desarrolladores = (ScrollView) instruccionesDialog.findViewById(R.id.sv_desarrolladores);
		final TextView tv_creditos = (TextView) instruccionesDialog.findViewById(R.id.tv_creditos);

		tv_creditos.setText(tv_creditos.getText().toString() + " v" + Propiedades.VERSION);

		Button btn_instrucciones = (Button) instruccionesDialog.findViewById(R.id.btn_instrucciones);
		btn_instrucciones.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sv_instrucciones.getVisibility() == View.VISIBLE) {
					sv_instrucciones.setVisibility(View.GONE);
				} else {
					sv_instrucciones.setVisibility(View.VISIBLE);
					tv_creditos.setVisibility(View.GONE);
					sv_desarrolladores.setVisibility(View.GONE);

				}
			}
		});

		Button btn_desarrolladores = (Button) instruccionesDialog.findViewById(R.id.btn_desarrolladores);
		btn_desarrolladores.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sv_desarrolladores.getVisibility() == View.VISIBLE) {
					sv_desarrolladores.setVisibility(View.GONE);
				} else {
					tv_creditos.setVisibility(View.GONE);

					sv_desarrolladores.setVisibility(View.VISIBLE);
					sv_instrucciones.setVisibility(View.GONE);
				}
			}
		});

		Button btn_creditos = (Button) instruccionesDialog.findViewById(R.id.btn_creditos);
		btn_creditos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_creditos.getVisibility() == View.VISIBLE) {
					tv_creditos.setVisibility(View.GONE);
				} else {

					tv_creditos.setVisibility(View.VISIBLE);
					sv_desarrolladores.setVisibility(View.GONE);
					sv_instrucciones.setVisibility(View.GONE);
				}
			}

		});

		Button okButton = (Button) instruccionesDialog.findViewById(R.id.btnDone);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				instruccionesDialog.dismiss();

			}
		});

		okButton.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				borrarterminosycondiciones();
				return false;
			}
		});
		instruccionesDialog.show();
	}

	protected void borrarterminosycondiciones() {
		SharedPreferences sp_perfil = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
		Editor spe_perfil = sp_perfil.edit();
		spe_perfil.putString("aceptaterminos", "no");
		spe_perfil.commit();
		Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
	}

	private void mostrarDialogoPruebas() {
		final Dialog instruccionesDialog = new Dialog(this);
		instruccionesDialog.setContentView(R.layout.elegir_pruebas);
		instruccionesDialog.setTitle(this.getString(R.string.seleccione_webservice));
		instruccionesDialog.show();

		RadioGroup rg_elegir_pruebas_webservice = (RadioGroup) instruccionesDialog
				.findViewById(R.id.rg_elegir_pruebas_webservice);
		SharedPreferences sp_perfil = this.getSharedPreferences("configuracion", Context.MODE_PRIVATE);

		if (sp_perfil.getString(Propiedades.SERVIDOR, "").contentEquals(Propiedades.WSDESARROLLO)) {
			rg_elegir_pruebas_webservice.check(R.id.r_pruebas);
		} else {
			rg_elegir_pruebas_webservice.check(R.id.r_produccion);
		}

		rg_elegir_pruebas_webservice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.r_produccion) {
					setproduccion();
				} else if (checkedId == R.id.r_pruebas) {
					setpruebas();
				}
				instruccionesDialog.cancel();
			}

		});

	}

	protected void setpruebas() {
		SharedPreferences sp_configuracion = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
		Editor spe_configuracion = sp_configuracion.edit();
		spe_configuracion.putString(Propiedades.SERVIDOR, Propiedades.WSDESARROLLO);
		spe_configuracion.commit();
		conseguirentidades();
	}

	protected void setproduccion() {
		SharedPreferences sp_configuracion = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
		Editor spe_configuracion = sp_configuracion.edit();
		spe_configuracion.putString(Propiedades.SERVIDOR, Propiedades.WSPRINCIPAL);
		spe_configuracion.commit();
		conseguirentidades();

	}

	private void conseguirentidades() {
		Entidades entidades = new Entidades(this);
		entidades.execute();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SeleccionarEntidad.this, SolicitudesListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	protected void seleccionar_entidad(int whichButton) {
		try {
			JSONArray ja = new JSONArray(getentidades());
			JSONObject jo = ja.getJSONObject(whichButton);

			Propiedades.entidad = jo.getString("entidad");

			Propiedades.webservice = jo.getString("webservice");

			Propiedades.codigo_entidad = jo.getString("id");

			Propiedades.sigla_entidad = jo.getString("sigla_entidad");

			Propiedades.pagina = jo.getString("pagina");

			Propiedades.correo = jo.getString("correo");

			Propiedades.tipows = jo.getString("tipo");
			Log.i("PQR", "pqr id_entida " + Propiedades.codigo_entidad);

			for (int j = 0; j < Propiedades.estilos_entidad.size(); j++) {
				Log.i("PQR", "pqr id_entida 11 " + Propiedades.estilos_entidad.get(j).getId_entidad());
				Log.i("PQR", "pqr id_entida 22 " + Propiedades.codigo_entidad);

				if (Propiedades.estilos_entidad.get(j).getId_entidad().equals(Propiedades.codigo_entidad)) {

					Propiedades.estilo_entidad = Propiedades.estilos_entidad.get(j);
					Log.i("PQR", "pqr id_entida seleccion " + Propiedades.estilos_entidad.get(j).getId_entidad());

					break;
				}

			}

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

			obtener_proceso(Propiedades.codigo_entidad);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			Toast.makeText(this, getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT).show();
		}

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
			} catch (Exception e) {
				e.printStackTrace();
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

}
