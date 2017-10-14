package net.micrositios.pqrapp.formulario;

import java.util.ArrayList;

import net.micrositios.pqrapp.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends Activity {
	Context con;
	int num_carga = 0;
	Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		con = this;
		act = this;
		// Button cargar = (Button) findViewById(R.id.buttoncargarformulario);
		//
		// cargar.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub

		num_carga = 0;

		MyApplication.listas = new ArrayList<Lista_spinner>();
		WS_Rest_client ws = new WS_Rest_client(con, act,
				WS_Rest_client.Consultar_formulario);

		Handler puente_envio = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					for (int i = 0; i < MyApplication.campos.size(); i++) {
						Campo campo = MyApplication.campos.get(i);
						if (campo.getTipo().equals("list")
								&& campo.getId_padre() == 0) {

							num_carga++;

							WS_Rest_client ws = new WS_Rest_client(con, act,
									WS_Rest_client.getlista);

							Handler puente_envio = new Handler() {
								@Override
								public void handleMessage(Message msg) {
									if (msg.what == 1) {
										Log.i("numero", "numero de consultas"
												+ num_carga);
										Log.i("numero", "tamaño lista"
												+ MyApplication.listas.size());

										if (num_carga == MyApplication.listas
												.size()) {
											Intent intent = new Intent(
													MainActivity.this,
													FormularioActivity.class);

											startActivity(intent);
										}
									} else if (msg.what == 0) {

									}
								}
							};

							ws.setPuente_envio(puente_envio);
							ws.getlista(campo.getConsulta(), "0");
							ws.execute();

						}
					}

				} else if (msg.what == 0) {

				}
			}
		};

		ws.setPuente_envio(puente_envio);
		ws.Consultar_formulario("1");
		ws.execute();
		// }
		// });

	}

}
