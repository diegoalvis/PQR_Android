package net.micrositios.pqrapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SolicitudesListActivity extends Activity {

	private SolicitudesListAdapter estadoTrackerAdapter = null;
	Bundle extras = null;
	AsyncCallWSConsulta task;
	ListView listView;
	Activity act;
	String mensaje;
	String webService;
	protected int desarrollo;
	protected int TOQUES = 4;
	private String IdGCM;
	private String PATH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.codigos_list_view);
		act = this;

		if (savedInstanceState != null) {
			GuardarInformacionApp.GuardarInformacionApp(this, this);
			GuardarInformacionApp.Cargar_info(savedInstanceState);
		}

		this.PATH = this.getFilesDir() + File.separator;
		getentidades();
		initcomponents();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		GuardarInformacionApp.Guardar_info(outState);

	}

	private void initcomponents() {
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			GCMRegistrar.register(this, "586611920514");
			IdGCM = GCMRegistrar.getRegistrationId(this);
		} catch (Exception exe) {
			Log.d(Propiedades.TAG, "GCM " + exe.toString());
		}

		listView = (ListView) findViewById(R.id.codigos_list);
		if (getSolicitudes()) {
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// Log.d(Propiedades.TAG, arg1.toString());
					if (arg3 != -1) {
						if (arg1 instanceof LinearLayout) {
							LinearLayout ll = (LinearLayout) arg1;
							for (int i = 0; i < ll.getChildCount(); i++) {
								try {
									Log.d(Propiedades.TAG, ll.getChildAt(i).getTag().toString());
								} catch (Exception e) {
									ll.getChildAt(i).setTag(ll.getChildAt(i).getBackground());
								}

								ll.getChildAt(i).setBackgroundColor(Color.argb(150, 210, 210, 210));
							}
							appStart(ll, arg2);
						}
					}
				}
			});
			// }

			TextView tv_codigos_list1 = (TextView) findViewById(R.id.tv_codigos_list1);
			tv_codigos_list1.setVisibility(View.GONE);
			TextView tv_codigos_list2 = (TextView) findViewById(R.id.tv_codigos_list2);
			tv_codigos_list2.setVisibility(View.GONE);

		} else {
			TextView tv_codigos_list1 = (TextView) findViewById(R.id.tv_codigos_list1);
			tv_codigos_list1.setVisibility(View.VISIBLE);
			TextView tv_codigos_list2 = (TextView) findViewById(R.id.tv_codigos_list2);
			tv_codigos_list2.setVisibility(View.VISIBLE);

			listView.setVisibility(View.GONE);

			SharedPreferences sp_configuracion = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
			Editor spe_configuracion = sp_configuracion.edit();
			if (!sp_configuracion.getString(Propiedades.PRIMERINICIO, "").contentEquals(Propiedades.SI)) {
				spe_configuracion.putString(Propiedades.PRIMERINICIO, Propiedades.SI);
				spe_configuracion.commit();
				showInstruccionesDialog();
			}

		}

		Button btn_codigos_list_agregar = (Button) findViewById(R.id.btn_codigos_list_agregar);
		btn_codigos_list_agregar.setVisibility(View.VISIBLE);

		btn_codigos_list_agregar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				agregarsolicitud();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change Here***
		startActivity(intent);
		finish();
		System.exit(0);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected int getsizes() {

		int Measuredwidth = 0;
		int Measuredheight = 0;
		Point size = new Point();
		WindowManager w = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);

			Measuredwidth = size.x;
			Measuredheight = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			Measuredwidth = d.getWidth();
			Measuredheight = d.getHeight();
		}

		float densidad = getResources().getDisplayMetrics().density;

		int SIZE = 0;
		if (densidad == 0.75f) {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 11;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 15;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 22;
			} else {
				SIZE = 30;
			}

		} else if (densidad == 1.0f) {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 8;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 11;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 16;
			} else {
				SIZE = 18;
			}

		} else if (densidad == 1.5f) {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 4;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 7;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 11;
			} else {
				SIZE = 17;
			}

		} else {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 2;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 4;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 8;
			} else {
				SIZE = 12;
			}
		}

		Toast.makeText(this,
				"Densidad: " + densidad + " Width " + Measuredwidth + " Height:" + Measuredheight + " SIZE: " + SIZE,
				Toast.LENGTH_LONG).show();

		return SIZE;

	}

	private void agregarsolicitud() {
		Intent formularioIntent = new Intent(this, SeleccionarEntidad.class);
		startActivity(formularioIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reanudar();
	}

	private void reanudar() {
		listView = (ListView) findViewById(R.id.codigos_list);
		if ((Propiedades.entidades.length()) < 80) {

			conseguirentidades();
		}

	}

	public void deleteSolicitudes(Cursor c) {
		String[] columns = new String[] { "_id", "webservice", "codigo", "codigoEntidad" };
		HashMap<String, String> hmCampos = new HashMap<String, String>();

		for (String string : columns) {
			hmCampos.put(string, estadoTrackerAdapter.getCursor().getString(c.getColumnIndex(string)));
		}

		Log.d("deletesolicitudes", hmCampos.toString() + " " + hmCampos.get(columns[2]));

		AsyncCallWSBorrar acwb = new AsyncCallWSBorrar(act);
		acwb.setId_solicitud(hmCampos.get(columns[0]));
		acwb.setIdgcm(IdGCM);
		acwb.setCodigoEntidad(hmCampos.get(columns[3]));
		acwb.setCodigoSeguimiento(hmCampos.get(columns[2]));
		acwb.setPuente(puente);
		acwb.execute();
	}

	@SuppressLint("HandlerLeak")
	private Handler puente = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(act, msg.obj.toString(), Toast.LENGTH_SHORT).show();
			reiniciar();
		}
	};

	private void reiniciar() {
		Intent intent = getIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
		finish();
	}

	public Boolean getSolicitudes() {
		// --get all solicitudes---
		Boolean resultado = false;
		DBAdapter db = new DBAdapter(this);
		db.open();
		// Cursor cursor;
		Cursor c = db.getAllSolicitudes("fecha DESC");
		if (c.moveToFirst()) {
			resultado = true;
		}
		// c.moveToLast();
		Cursor extendedCursor = null;
		Cursor[] cursors = { c };
		extendedCursor = new MergeCursor(cursors);

		if (extendedCursor.moveToLast()) {
			do {
				try {
					displaySolicitudes(extendedCursor);
				} catch (Exception e) {
					Log.d(Propiedades.TAG, e.toString());
				}
			} while (extendedCursor.moveToPrevious());
		}
		db.close();
		return resultado;
	}

	public void displaySolicitudes(Cursor c) {

		int layout = R.layout.solicitudes_estilo_gmail;
		estadoTrackerAdapter = new SolicitudesListAdapter(SolicitudesListActivity.this, c, false, layout, 3);
		listView.setAdapter(estadoTrackerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.listado_solicitudes_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.consulta_manual_menu_item) {
			consultarsolicitud();

			return true;
		} else if (item.getItemId() == R.id.cancelar_menu_item) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.ayuda_menu_item) {
			showInstruccionesDialog();
			return true;
		} else if (item.getItemId() == R.id.agregar_solicitud_menu_item) {
			agregarsolicitud();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	private void consultarsolicitud() {
		Intent intent = new Intent(this, ConsultarSolicitudActivity.class);
		startActivity(intent);
		finish();
	}

	@SuppressLint("NewApi")
	private void appStart(final LinearLayout ll, final int posicion) {
		DBAdapter db = new DBAdapter(this);
		db.open();

		final Cursor c = db.getAllSolicitudes("fecha DESC");

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(this.getString(R.string.solicitudes));
		builder.setItems(R.array.solicitudes_list, new DialogInterface.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					try {

						task = new AsyncCallWSConsulta(SolicitudesListActivity.this);

						Cursor cur = estadoTrackerAdapter.getCursor();
						int i = 0;

						if (cur.moveToFirst()) {
							for (int j = 0; j < posicion; j++) {

								cur.moveToNext();
							}

						}

						Propiedades.webservice = cur.getString(c.getColumnIndex("webservice"));

						Propiedades.tipows = getRest(Propiedades.webservice);
						Propiedades.codigo_entidad = cur.getString(c.getColumnIndex("codigoEntidad"));

						task.setCodigoSeguimiento(cur.getString(c.getColumnIndex("codigo")));
						task.setCodigoEntidad(cur.getString(c.getColumnIndex("codigoEntidad")));

						task.setConsultaManual(false);
						// Call execute
						task.execute();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						for (int i = 0; i < ll.getChildCount(); i++) {
							restaurarbackground(ll);
						}

					}

					break;
				case 1:
					for (int i = 0; i < ll.getChildCount(); i++) {
						if (ll.getChildAt(i).getTag() instanceof Drawable) {
							ll.getChildAt(i).setBackgroundDrawable((Drawable) ll.getChildAt(i).getTag());
						}

					}
					AlertDialog.Builder confirmar = new AlertDialog.Builder(act);
					confirmar.setTitle(R.string.pregunta_eliminar_solicitud)
							.setMessage(R.string.se_eliminara_localmente)
							.setPositiveButton(act.getString(R.string.eliminar), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							Cursor cur = estadoTrackerAdapter.getCursor();
							int i = 0;

							if (cur.moveToFirst()) {
								for (int j = 0; j < posicion; j++) {

									cur.moveToNext();
								}

							}

							deleteSolicitudes(cur);

						}
					}).setNegativeButton(act.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							restaurarbackground(ll);
							dialog.dismiss();
						}

					}).setCancelable(true).setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							restaurarbackground(ll);
						}
					}).create();

					confirmar.show();

					break;

				}
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				restaurarbackground(ll);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		int dividerId = alert.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = alert.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(this.getResources().getColor(R.color.aplicacion));
		} else {
			divider.setBackground(getDrawable(R.drawable.fondoactionbar));

		}
		int textViewId = alert.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) alert.findViewById(textViewId);
		tv.setTextColor(this.getResources().getColor(R.color.aplicacion));

		db.close();
	}

	protected String getRest(String webservice2) {
		try {
			JSONArray ja = new JSONArray(getentidades());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String webservice = jo.getString("webservice");
				if (webservice.contentEquals(webservice2)) {
					return jo.getString("tipo");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getentidades() {
		String PATH = this.getFilesDir() + File.separator;
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

	@SuppressLint({ "NewApi", "DefaultLocale" })
	@SuppressWarnings("deprecation")
	private void restaurarbackground(LinearLayout ll) {
		for (int i = 0; i < ll.getChildCount(); i++) {

			try {
				if (ll.getChildAt(i).getTag().toString().contentEquals("icono")) {
					TextView tv = (TextView) ll.getChildAt(i);
					int color = (int) (184236 * (int) tv.getText().toString().trim().toUpperCase().charAt(0) - 65);

					ColorDrawable cd = new ColorDrawable(-color);
					cd.setAlpha(150);
					int sdk = android.os.Build.VERSION.SDK_INT;
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						tv.setBackgroundDrawable(cd);
					} else {
						tv.setBackground(cd);
					}
				} else {
					ll.getChildAt(i).setBackgroundColor(Color.WHITE);
					try {

						TextView tv = (TextView) ll.getChildAt(i);
						int color = (int) (184236 * (int) tv.getText().toString().trim().toUpperCase().charAt(0) - 65);

						ColorDrawable cd = new ColorDrawable(-color);
						cd.setAlpha(150);
						int sdk = android.os.Build.VERSION.SDK_INT;
						if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
							tv.setBackgroundDrawable(cd);
						} else {
							tv.setBackground(cd);
						}

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			} catch (Exception e) {

			}

		}

	}

	@SuppressLint("NewApi")
	private void showInstruccionesDialog() {
		final Dialog instruccionesDialog = new Dialog(this);

		// TextView alertTitle=(TextView)
		// instruccionesDialog.Window.DecorView.FindViewById(Android.Resource.Id.Title);
		//
		// alertTitle.SetTextColor(this.getResources().getColor(R.color.aplicacion));
		// instruccionesDialog.getWindow().getDecorView().findViewById(Android.)

		instruccionesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		instruccionesDialog.setContentView(R.layout.instrucciones_dialog);

		// instruccionesDialog.setTitle(this
		// .getString(R.string.informacion_dialogo_titulo));

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

		btn_instrucciones.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// iniciar_gcm();
				return false;
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

		tv_creditos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (desarrollo >= TOQUES) {
					desarrollo = 0;
					mostrarDialogoPruebas();
				} else {
					desarrollo++;

				}

			}
		});

		tv_creditos.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				return false;
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
				// borrarterminosycondiciones();
				return false;
			}
		});
		instruccionesDialog.show();
		try {

			int dividerId = instruccionesDialog.getContext().getResources().getIdentifier("android:id/titleDivider",
					null, null);
			View divider = instruccionesDialog.findViewById(dividerId);
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk <= android.os.Build.VERSION_CODES.JELLY_BEAN) {

				divider.setBackgroundColor(getResources().getColor(R.color.aplicacion));
			} else {
				try {

					divider.setBackground((Drawable) act.getResources().getDrawable(R.drawable.fondoactionbar));
					int textViewId = instruccionesDialog.getContext().getResources()
							.getIdentifier("android:id/alertTitle", null, null);
					TextView tv = (TextView) instruccionesDialog.findViewById(textViewId);
					tv.setTextColor(getResources().getColor(R.color.aplicacion));
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	protected void iniciar_gcm() {
		Intent gcm_intent = new Intent(this, GCMActivity.class);
		startActivity(gcm_intent);
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
}