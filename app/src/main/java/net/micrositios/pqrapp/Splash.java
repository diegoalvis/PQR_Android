package net.micrositios.pqrapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {

	Handler handler = new Handler();
	Runnable delay = new Runnable() {
		@Override
		public void run() {
			registerReceivers();
		}
	};
	private Splash act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		TextView tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(Propiedades.VERSION);

		act = this;

		handler.postDelayed(delay, 2000);
	}

	protected void comprobar() {
		SharedPreferences sp_perfil = getSharedPreferences(Propiedades.CONFIGURACION, Context.MODE_PRIVATE);
		if (sp_perfil.getString(Propiedades.ACEPTATERMINOS, "").contentEquals(Propiedades.SI)) {
			startactivity();
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
							startactivity();
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
	}

	protected void startactivity() {
		Intent intent = new Intent(this, SplashEntidades.class);
		startActivity(intent);
		finish();
	}

	private void registrarconfirmacion() {
		SharedPreferences sp_perfil = getSharedPreferences(Propiedades.CONFIGURACION, Context.MODE_PRIVATE);
		Editor spe_perfil = sp_perfil.edit();
		spe_perfil.putString(Propiedades.ACEPTATERMINOS, "si");
		spe_perfil.commit();
	}

	@Override
	protected void onStop() {
		unregisterreceivers();
		super.onStop();
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

}
