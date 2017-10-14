package net.micrositios.pqrapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConsultarSolicitudActivity extends Activity {
	Map<String, String> map_entidades = new HashMap<String, String>();
	private String PATH;

	EditText codigoView;
	String webService;
	Handler handler;
	String codigoListActivity = null;
	String codigo;
	boolean codigoEsValido = false;

	private Spinner sEntidades;

	private final ArrayList<ValidarCampo> campoConsultarSolicitud = new ArrayList<ValidarCampo>();
	private String IdGCM;

	private static String TEXTOTIPO5 = "^[A-Z0-9]*$";// Texto y numeros
	String nombreEntidad = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consultar_solicitud);
		this.PATH = this.getFilesDir() + File.separator;
		// ---Spinner View Entidades---
		sEntidades = (Spinner) findViewById(R.id.entidad_view);

		ArrayList<String> tipos_de_solicitud = conseguir_entidades();

		ArrayAdapter<String> aEntidades = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				tipos_de_solicitud);
		aEntidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sEntidades.setAdapter(aEntidades);

		codigoView = (EditText) findViewById(R.id.codigo_view);
		sEntidades.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				nombreEntidad = sEntidades.getSelectedItem().toString();
				seleccionar_entidad(nombreEntidad);

				if (nombreEntidad.contains("Agencia Presidencial")) {
					codigoView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(14) });

				} else if (nombreEntidad.contains("Coldeportes")) {

					codigoView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(40) });

					codigoView.setInputType(InputType.TYPE_CLASS_TEXT);

				} else {

					codigoView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String value = extras.getString("codigo");
			codigoView.setText(value);

		}
		// android:maxLength="16"

		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			GCMRegistrar.register(this, "586611920514");
			IdGCM = GCMRegistrar.getRegistrationId(this);
		} catch (Exception exe) {
			Log.d(Propiedades.TAG, "GCM " + exe.toString());
		}
	}

	private void appStart() {

		// Edit Control
		codigoView = (EditText) findViewById(R.id.codigo_view);

		// int entidadId = sEntidades.getSelectedItemPosition();

		/* Test Campo Codigo */
		ValidarCampo campoCodigo = new ValidarCampo(this);
		campoConsultarSolicitud.add(campoCodigo);
		String mensajeCodigo = null;

		codigo = codigoView.getText().toString().trim();

		setUpValidador(codigo, 0, 255, true, getString(R.string.codigo_seguimiento), TEXTOTIPO5, 5);

		if (campoCodigo.Validar() || nombreEntidad.contains("Coldeportes")) {
			if (!codigoExistente(codigo)) {
				// Create instance for AsyncCallWS
				AsyncCallWSConsulta task = new AsyncCallWSConsulta(this);
				task.setConsultaManual(true);
				task.setCodigoSeguimiento(codigo);
				task.setCodigoEntidad(Propiedades.codigo_entidad);
				task.setIdgcm(IdGCM);

				task.setConsultaManual(true);
				// Call execute
				task.execute();
			} else {
				Toast t = Toast.makeText(this, getString(R.string.solicitud_existe), Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();

			}
		} else {
			Toast t = Toast.makeText(this, getString(R.string.mensaje_revise_datos), Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			mensajeCodigo = campoCodigo.getMensaje();
			codigoView.setError(mensajeCodigo);
		}

	}

	private boolean codigoExistente(String codigo) {
		DBAdapter db = new DBAdapter(this);
		Cursor c = null;
		try {
			db.open();
			c = db.getSolicitud(codigo, Propiedades.codigo_entidad);
			return c.moveToFirst();

		} catch (Exception e) {
			Log.d("MICROSITIOS_ERROR", e.toString());
			return false;
		} finally {
			if (c != null) {
				c.close();
			}

			db.close();
		}

	}

	private void setUpValidador(String contenidoCampo, Integer minChar, Integer maxChar, Boolean requerido,
			String nombreCampo, String pattern, Integer tipo) {

		for (ValidarCampo campoToSet : campoConsultarSolicitud) {

			campoToSet.setContenidoCampo(contenidoCampo);
			campoToSet.setMinChar(minChar);
			campoToSet.setMaxChar(maxChar);
			campoToSet.setRequerido(requerido);
			campoToSet.setNombre(nombreCampo);
			campoToSet.setPattern(pattern);
			campoToSet.setTipo(tipo);

		}

	}

	private ArrayList<String> conseguir_entidades() {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		map_entidades.clear();

		try {
			JSONArray ja = new JSONArray(getentidades());

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("entidad");
				String codigo_entidad = jo.getString("codigo_entidad");
				mHelperNames.add(entidad);

				map_entidades.put(entidad, codigo_entidad);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			Toast.makeText(this, getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT).show();
		}

		return mHelperNames;
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

	public void onCancel(View view) {
		Intent consultarIntent = new Intent(this, SolicitudesListActivity.class);
		startActivity(consultarIntent);
		finish();
	}

	public void onSave(View view) {
		appStart();
	}

	protected void seleccionar_entidad(String nombreEntidad) {
		try {
			JSONArray ja = new JSONArray(getentidades());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("entidad");
				if (entidad.contentEquals(nombreEntidad)) {
					Propiedades.entidad = entidad;
					Propiedades.webservice = jo.getString("webservice");
					Propiedades.codigo_entidad = jo.getString("codigo_entidad");
					Propiedades.sigla_entidad = jo.getString("sigla_entidad");
					Propiedades.pagina = jo.getString("pagina");
					Propiedades.correo = jo.getString("correo");
					try {
						String encuesta = jo.getString("encuesta");
						Log.i("pqr", "encusta:" + encuesta);

						if (encuesta.equals("1")) {

							Propiedades.encuesta = true;

						} else if (encuesta.equals("0")) {

							Propiedades.encuesta = false;

						}

					} catch (Exception e) {
						// TODO: handle exception
						Propiedades.encuesta = false;
					}
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.listado_solicitudes_menu_consulta, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.consulta_manual_menu_item) {
			Intent intent = new Intent(this, ConsultarSolicitudActivity.class);
			startActivity(intent);
			return true;
		}
		if (item.getItemId() == R.id.cancelar_menu_item) {
			finish();
			return true;
		}
		if (item.getItemId() == R.id.ayuda_menu_item) {
			showInstruccionesDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	private void showInstruccionesDialog() {
		final Dialog instruccionesDialog = new Dialog(this);
		instruccionesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		instruccionesDialog.setContentView(R.layout.instrucciones_dialog);

		// instruccionesDialog.setCancelable(false);

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent consultarIntent = new Intent(this, SolicitudesListActivity.class);
		startActivity(consultarIntent);
		finish();

	}

}
