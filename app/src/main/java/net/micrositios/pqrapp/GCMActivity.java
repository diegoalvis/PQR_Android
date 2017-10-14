package net.micrositios.pqrapp;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.*;

public class GCMActivity extends Activity {
	
	private Button btnRegistrar;
	private Button btnDesRegistrar;
	private Button btnGuardarUsuario;
	private EditText txtUsuario;
	private TextView tvcodigo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gcm_activity);
		
		btnRegistrar = (Button)findViewById(R.id.btnRegGcm);
        btnDesRegistrar = (Button)findViewById(R.id.btnDesRegGcm);
        btnGuardarUsuario = (Button)findViewById(R.id.btnAceptar);
        txtUsuario = (EditText)findViewById(R.id.txtNombreUsuario);
        tvcodigo = (TextView)findViewById(R.id.tvcodigo);
        
        //Comprobamos si est� todo en orden para utilizar GCM
        GCMRegistrar.checkDevice(GCMActivity.this);
        GCMRegistrar.checkManifest(GCMActivity.this);
        
        btnGuardarUsuario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences prefs =
					     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
				
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("usuario", txtUsuario.getText().toString());
				editor.commit();
			}
		});
        
        btnRegistrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				//Si no estamos registrados --> Nos registramos en GCM
		        final String regId = GCMRegistrar.getRegistrationId(GCMActivity.this);
		        if (regId.equals("")) {
		        	GCMRegistrar.register(GCMActivity.this, "586611920514"); //Sender ID
		        	
		        	Log.v("GCMTest", "REG_ID: "+regId);
		        	
		        } else {
		        	Log.v("GCMTest", "Ya registrado");
		        }
		        tvcodigo.setText("Su código es: "+regId);
			}
		});
        
        btnDesRegistrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Si estamos registrados --> Nos des-registramos en GCM
		        final String regId = GCMRegistrar.getRegistrationId(GCMActivity.this);
		        if (!regId.equals("")) {
		        	GCMRegistrar.unregister(GCMActivity.this);
		        } else {
		        	Log.v("GCMTest", "Ya des-registrado");
		        }
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
