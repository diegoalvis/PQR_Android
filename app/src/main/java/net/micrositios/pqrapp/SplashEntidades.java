package net.micrositios.pqrapp;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import net.micrositios.pqrapp.formulario.Descarga_Archivo;
import net.micrositios.pqrapp.formulario.WS_Rest_client;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SplashEntidades extends Activity {

	Handler handler = new Handler();
	Context con;
	Runnable delay = new Runnable() {
		@Override
		public void run() {
			registerReceivers();

		}
	};
	SplashEntidades act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashentidades);
		act = this;
		con = this;

		File dir = new File(Environment.getExternalStorageDirectory() + "/pqr/");

		// si el direcctorio no existe, lo creo
		if (!dir.exists()) {

			dir.mkdir();

		}

		handler.postDelayed(delay, 2000);

	}

	@SuppressLint("HandlerLeak")
	protected void init() {
		Handler puente = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					traer_imagenes();

				} else {
					Toast.makeText(con, "Problemas de conexion a internet, verifique y vuelva a ingresar.",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		Entidades entidades = new Entidades(this);
		entidades.setPuente(puente);
		entidades.execute();
	}

	int consultas = 0;
	int i = 0;
	JSONArray ja;

	public void traer_imagenes() {

		try {
			ja = new JSONArray(Propiedades.entidades);

			for (i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String id = jo.getString("id");
				String sigla = jo.getString("sigla_entidad");
				WS_Rest_client ws = new WS_Rest_client(con, act, WS_Rest_client.Consultar_estilo);

				Handler puente_envio = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == 1) {
							consultas++;
							if (ja.length() == consultas) {
								try {
									descargar_imagenes();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else if (msg.what == 0) {
							consultas++;
							if (ja.length() == consultas) {
								try {
									descargar_imagenes();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					}
				};

				ws.setPuente_envio(puente_envio);
				ws.Consultar_estilo(id);
				ws.execute();

			}

		} catch (JSONException je) {
			je.printStackTrace();
		} catch (NullPointerException npe) {
			Toast.makeText(this, getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT).show();
		}
	}

	int descargas = 0;

	public void descargar_imagenes() throws JSONException {
		descargas = 0;
		Handler puentedescarga = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					descargas++;
					if (descargas == Propiedades.estilos_entidad.size()) {

						startactivity();
					}
				} else if (msg.what == 0) {
					descargas++;
					if (descargas == Propiedades.estilos_entidad.size()) {

						startactivity();

					}

				}

			}
		};
		ja = new JSONArray(Propiedades.entidades);
		for (i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String id = jo.getString("id");
			String sigla = jo.getString("sigla_entidad");

			for (int j = 0; j < Propiedades.estilos_entidad.size(); j++) {

				if (Propiedades.estilos_entidad.get(j).getId_entidad().equals(id)) {

					File f = new File(Environment.getExternalStorageDirectory() + "/pqr/" + sigla
							+ Propiedades.estilos_entidad.get(j).getVersion() + ".png");
					Propiedades.estilos_entidad.get(j).setLogo_local(Environment.getExternalStorageDirectory() + "/pqr/"
							+ sigla + Propiedades.estilos_entidad.get(j).getVersion() + ".png");

					if (f.exists()) {
						descargas++;

					} else {

						Descarga_Archivo descarga = new Descarga_Archivo(
								Propiedades.estilos_entidad.get(i).getUrl_logo(), puentedescarga,
								sigla + Propiedades.estilos_entidad.get(j).getVersion() + ".png");

						descarga.execute();

					}

					if (descargas == Propiedades.estilos_entidad.size()) {

						startactivity();

					}
					break;
				}

			}

		}
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

	private void startactivity() {
		Intent consultarIntent = new Intent(this, SolicitudesListActivity.class);
		startActivity(consultarIntent);
		finish();
	}

	@Override
	protected void onStop() {

		unregisterreceivers();
		super.onStop();

	}

	@Override
	protected void onPause() {
		unregisterreceivers();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterreceivers();
		super.onDestroy();
	}

	protected void comprobar() {
		SharedPreferences sp_perfil = getSharedPreferences(Propiedades.CONFIGURACION, Context.MODE_PRIVATE);
		if (sp_perfil.getString(Propiedades.ACEPTATERMINOS, "").contentEquals(Propiedades.SI)) {
			init();
		} else {
			mostrar_dialogo();
		}

	}

	private void registerReceivers() {
		registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			if (noConnectivity) {
				alert();
			} else {
				comprobar();
			}
		}
	};

	@SuppressWarnings("deprecation")
	private void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(this.getString(R.string.no_conexion_internet));
		alertDialog.setMessage(this.getString(R.string.no_conexion_internet_mensaje));
		alertDialog.setCancelable(false);
		alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				finish();
			}
		});
		alertDialog.show();
	}

	@SuppressLint("InflateParams")
	protected void mostrar_dialogo() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(this.getString(R.string.titulo_terminos_condiciones));

		View v = getLayoutInflater().inflate(R.layout.terminos_y_condiciones, null);
		final CheckBox cb_terminos_y_condiciones = (CheckBox) v.findViewById(R.id.cb_terminos_y_condiciones);

		alertDialog.setView(v);
		alertDialog.setCancelable(false);
		alertDialog.setNegativeButton(getString(R.string.no), null);
		alertDialog.setPositiveButton(getString(R.string.aceptar), null);
		final AlertDialog alert = alertDialog.create();

		alert.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button aceptar = alert.getButton(AlertDialog.BUTTON_POSITIVE);
				aceptar.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						if (cb_terminos_y_condiciones.isChecked()) {
							registrarconfirmacion();
							init();

							alert.cancel();
						} else {
							Toast.makeText(act, getString(R.string.mensaje_terminos_condiciones), Toast.LENGTH_SHORT)
									.show();
						}
					}

				});

				Button cancelar = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
				cancelar.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						alert.cancel();
						finish();
					}
				});
			}
		});

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

		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(this.getResources().getColor(R.color.aplicacion_oscuro));
		} else {
			// divider.setBackground(micontext
			// .getDrawable(R.drawable.fondoactionbar));

		}
		int textViewId = alert.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) alert.findViewById(textViewId);
		tv.setTextColor(this.getResources().getColor(R.color.white));

	}

	private void registrarconfirmacion() {
		SharedPreferences sp_perfil = getSharedPreferences(Propiedades.CONFIGURACION, Context.MODE_PRIVATE);
		Editor spe_perfil = sp_perfil.edit();
		spe_perfil.putString(Propiedades.ACEPTATERMINOS, "si");
		spe_perfil.commit();
	}

	private void unregisterreceivers() {
		if (mConnReceiver != null) {
			try {
				unregisterReceiver(mConnReceiver);
			} catch (Exception e) {
				Log.d(Propiedades.TAG, "No se pudo desregistrar");
			}
		}
	}

}
