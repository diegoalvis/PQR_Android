package net.micrositios.pqrapp.formulario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import net.micrositios.pqrapp.BytesStreamsAndFiles;
import net.micrositios.pqrapp.FilePickerActivity;
import net.micrositios.pqrapp.GuardarInformacionApp;
import net.micrositios.pqrapp.MyApplication;
import net.micrositios.pqrapp.Propiedades;
import net.micrositios.pqrapp.R;
import net.micrositios.pqrapp.Seccion;
import net.micrositios.pqrapp.SeleccionarEntidad;
import net.micrositios.pqrapp.SolicitudesListActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 pendientes:
 */
/** definir la funcion para localizacion */

/** guardar y borrado de formulario*/

/** obtener id GCM */

@RuntimePermissions
@SuppressLint({ "NewApi", "SimpleDateFormat", "InflateParams", "DefaultLocale" })
public class FormularioActivity extends Activity {
	private static final int REQUEST_CODE_CAMERA = 101;
	Context con;
	Button envia;

	LinearLayout layout;// layout principal del formulario

	// Arreglo de objetos graficos
	ArrayList<EditText> arregloEdit;
	ArrayList<Spinner> arreglospinner;
	ArrayList<TextView> arreglo_errores;
	ArrayList<Seccion> arreglo_seccion;
	// informacion de la entidad
	String nombreEntidad = "";
	String webService = "";

	// variables para efecto de logo
	ImageView logo;

	Drawable d_cambio;
	JSONObject json_guardar;
	Boolean imagen_drawable = true;

	// Variables para la localizacion del dispositivo

	LocationManager mLocManager;
	String longitud = "";
	String latitud = "";
	// variable para guardar configuracion

	private SharedPreferences sp_perfil;
	private SharedPreferences sp_configuracion;
	private boolean actualizar = false;
	private SharedPreferences.Editor spe_perfil;
	private SharedPreferences.Editor spe_configuracion;

	//

	// layout archivos
	LinearLayout ll_file, ll_buttons;
	private LinearLayout ll_botones_adjuntar;
	private ProgressDialog dialog;
	// parametros gestor de archivos
	private static int ADJUNTOS_MAXIMO = 5;
	public static int TAMANOMAXIMO = 10000000;
	private String STRTAMANOMAXIMO = "10M";

	/** banderas para recepcion del onactivityresult **/

	private static final int TAKE_PHOTO_CODE = 0;
	private static final int REQUEST_PICK_FILE = 1;
	private static final int CAPTURE_IMAGE = 2;
	private static final int CAPTURE_VIDEO = 3;

	/** path archivos multimedia **/
	private Uri path_foto;
	private Uri path_video;
	ScrollView scroll;
	/* servicio */
	WS_rest_solicitud servicio = null;
	JSONObject json;
	Activity act;
	private String PATH;
	StateListDrawable states, states1, states2, states3;
	Button enviar = null;
	ImageButton mStartActivityButton, ib_formulario_video, ib_formulario_foto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario);
		act = this;
		con = this;
		if (savedInstanceState != null) {
			GuardarInformacionApp.GuardarInformacionApp(this, this);
			GuardarInformacionApp.Cargar_info(savedInstanceState);
		}

		dialog_progress();
		init();
		dialog.cancel();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		GuardarInformacionApp.Guardar_info(outState);

	}

	public void init() {

		// get
		this.PATH = this.getFilesDir() + File.separator;
		int numero_campos = ((MyApplication) this.getApplication()).campos.size();
		layout = (LinearLayout) findViewById(R.id.layout_formulario);
		ArrayList<Campo> campos = ((MyApplication) this.getApplication()).campos;
		con = this;
		nombreEntidad = Propiedades.entidad;
		webService = Propiedades.webservice;
		arregloEdit = new ArrayList<EditText>();
		arreglospinner = new ArrayList<Spinner>();
		arreglo_errores = new ArrayList<TextView>();
		arreglo_seccion = new ArrayList<Seccion>();

		EditText edit[] = new EditText[4];
		ll_botones_adjuntar = (LinearLayout) findViewById(R.id.ll_botones_adjuntar);
		try {
			ll_buttons = (LinearLayout) findViewById(R.id.linear_layout_buttons);
			ll_file = (LinearLayout) findViewById(R.id.filed_path_ll);
		} catch (Exception e) {
			// TODO: handle exception
		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;
		TextView text_titulo = (TextView) findViewById(R.id.textv_entidad);
		text_titulo.setText(Propiedades.entidad);
		/************************
		 * creacion de formulario con base en el arreglo Datos_formulario.campos
		 ******************************/

		sp_perfil = getSharedPreferences("perfil", Context.MODE_PRIVATE);
		sp_configuracion = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
		spe_perfil = sp_perfil.edit();
		spe_configuracion = sp_configuracion.edit();

		for (int i = 0; i < numero_campos; i++) {
			Campo campotemp = campos.get(i);

			if (campotemp.getTipo_de_campo().equals("edittext")) {
				LinearLayout edtext = crear_edittext(campotemp, getvalor(campotemp.getNombre()));

				// arreglo_errores.add(error);
				// layout.addView(error, params);
				crear_seccion(campotemp);
				Seccion sec = buscar_seccion(campotemp.getSeccion());
				if (sec != null) {
					sec.getLayoutseccion().addView(edtext, params);

				} else {
					layout.addView(edtext, params);
				}
				if (i == numero_campos - 1) {
					edtext.setBottom(30);

				}

			}
			if (campotemp.getTipo_de_campo().equals("textview")) {
				LinearLayout text = crear_textview(campotemp, getvalor(campotemp.getNombre()));

				// arreglo_errores.add(error);
				// layout.addView(error, params);
				crear_seccion(campotemp);
				Seccion sec = buscar_seccion(campotemp.getSeccion());
				if (sec != null) {
					sec.getLayoutseccion().addView(text, params);

				} else {
					layout.addView(text, params);
				}

			}

			if (campotemp.getTipo_de_campo().equals("Drop_down")) {

				LinearLayout rel = crear_spinner(campotemp);
				crear_seccion(campotemp);
				Seccion sec = buscar_seccion(campotemp.getSeccion());
				if (sec != null) {
					sec.getLayoutseccion().addView(rel);

				} else {
					layout.addView(rel);
				}

			}

			if (campotemp.getTipo_de_campo().equals("Texto_html")) {

				WebView wv = crear_webview(campotemp);
				crear_seccion(campotemp);
				Seccion sec = buscar_seccion(campotemp.getSeccion());

				if (sec != null) {
					sec.getLayoutseccion().addView(wv);

				} else {
					layout.addView(wv);
				}

			}

			if (campotemp.getTipo_de_campo().equals("Drop_down_seccion")) {
				CheckBox rel = crear_spinner_seccion(campotemp);
				layout.addView(rel);
				crear_seccion(campotemp);

			}

			if (campotemp.getTipo_de_campo().equals("check_localizacion")) {
				crear_seccion(campotemp);
				LinearLayout layout1 = crear_check_localizacion();
				Seccion sec = buscar_seccion(campotemp.getSeccion());

				if (sec != null) {
					sec.getLayoutseccion().addView(layout1, params);

				} else {
					layout.addView(layout1, params);
				}

			}

		}
		dividir_seccion();
		/** objeto de relleno para extender el formulario **/
		TextView tview = new TextView(con);
		tview.setVisibility(View.INVISIBLE);
		layout.addView(tview, params);

		ADJUNTOS_MAXIMO = conseguir_parametros_adjuntos();

		/************************
		 * boton para envio de solicitud
		 **************************/

		GradientDrawable shape_defauld = new GradientDrawable();
		shape_defauld.setShape(GradientDrawable.RECTANGLE);
		shape_defauld.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		shape_defauld.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton()));
		shape_defauld.setStroke(0, Color.WHITE);

		BitmapDrawable textura = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.textureb2));
		// TileMe.setTileModeX(TileMode.REPEAT);
		textura.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

		Drawable[] drawarray = { shape_defauld, textura };
		LayerDrawable layerdrawable = new LayerDrawable(drawarray);

		GradientDrawable shape_select = new GradientDrawable();

		shape_select.setShape(GradientDrawable.RECTANGLE);
		shape_select.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });

		shape_select.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton_select()));
		shape_select.setStroke(0, Color.WHITE);

		Drawable[] drawarrayselect = { shape_select, textura };
		LayerDrawable layerdrawableselect = new LayerDrawable(drawarrayselect);

		states = new StateListDrawable();
		states.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
		states.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
		states.addState(new int[] {}, layerdrawable);

		states1 = new StateListDrawable();
		states1.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
		states1.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
		states1.addState(new int[] {}, layerdrawable);
		states2 = new StateListDrawable();
		states2.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
		states2.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
		states2.addState(new int[] {}, layerdrawable);
		states3 = new StateListDrawable();
		states3.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
		states3.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
		states3.addState(new int[] {}, layerdrawable);

		enviar = (Button) findViewById(R.id.Button_enviar_encuesta);

		enviar.setBackground(states);

		enviar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String envio;
				try {
					envio = validar();
					if (!envio.equals("")) {
						Log.i("formulario", "Respuesta formulario" + envio);
						servicio = new WS_rest_solicitud(act, json);
						verificarguardadodatos();

					} else {
						Toast.makeText(con, con.getResources().getString(R.string.mensaje_revise_datos),
								Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		/********* oculta el teclado al iniciar el formulario ********/
		View view = this.getCurrentFocus();
		if (view != null) {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

		}

		/**** seleccion de logo segun entidad ****/
		logo = (ImageView) findViewById(R.id.image_logo_fd);

		File f = new File(Propiedades.estilo_entidad.getLogo_local());

		if (f.exists()) {
			Bitmap bitmap = null;
			try {

				bitmap = BitmapFactory.decodeFile(Propiedades.estilo_entidad.getLogo_local());
				bitmap.prepareToDraw();
				int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
				bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

				logo.setImageBitmap(bitmap);
				logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			imagen_drawable = false;
			logo.setImageBitmap(null);

		}

		// configuracion de localizacion

		mStartActivityButton = (ImageButton) findViewById(R.id.button_formulariod_file_picker);
		mStartActivityButton.setBackground(states1);

		mStartActivityButton.setImageResource(R.drawable.ic_action_attachment2);

		mStartActivityButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startfilepicker();
				if (enviar != null) {
					try {
						envia.setVisibility(View.INVISIBLE);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
		});

		ib_formulario_foto = (ImageButton) findViewById(R.id.ib_formulariod_foto);

		ib_formulario_foto.setBackground(states2);

		ib_formulario_foto.setImageResource(R.drawable.ic_action_camera2);

		ib_formulario_foto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tomar_foto();
				if (enviar != null) {
					try {
						envia.setVisibility(View.INVISIBLE);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
		});

		ImageButton ib_formulario_video = (ImageButton) findViewById(R.id.ib_formulariod_video);
		Log.i("pqr", "adjunto maximo" + ADJUNTOS_MAXIMO);
		if (ADJUNTOS_MAXIMO > 10) {
			ib_formulario_video.setBackground(states3);
			ib_formulario_video.setImageResource(R.drawable.ic_action_video2);

			ib_formulario_video.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					grabar_video();
					if (enviar != null) {
						try {
							envia.setVisibility(View.INVISIBLE);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				}
			});
		} else {

			ib_formulario_video.setVisibility(View.GONE);

		}
	}

	public void crear_seccion(Campo campotemp) {

		if (buscar_seccion(campotemp.getSeccion()) == null && !campotemp.seccion.equals("")
				&& !campotemp.seccion.equals("null")) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;

			LinearLayout layoutseccion = new LinearLayout(con);
			layoutseccion.setOrientation(LinearLayout.VERTICAL);
			TextView text_seccion = new TextView(con);

			text_seccion.setTextSize(20);
			text_seccion.setRight(5);
			// <View
			// android:layout_width="match_parent"
			// android:layout_height="1dp"
			// android:background="@android:color/black" />

			View linea = new View(con);
			LinearLayout.LayoutParams paramslin = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 3);
			paramslin.topMargin = 5;
			int sdk = android.os.Build.VERSION.SDK_INT;

			try {
				linea.setBackgroundColor(Color.parseColor(Propiedades.estilo_entidad.getColor_barra_seccion()));
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				text_seccion.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
			} catch (Exception e) {
				// TODO: handle exception
			}

			text_seccion.setTextColor(Color.parseColor(Propiedades.estilo_entidad.color_texto_seccion));
			text_seccion.setText(campotemp.getSeccion());

			LinearLayout layouthorizontal = new LinearLayout(con);
			layouthorizontal.setOrientation(LinearLayout.HORIZONTAL);
			View lineaver = new View(con);
			LinearLayout.LayoutParams paramslinver = new LinearLayout.LayoutParams(12, LayoutParams.MATCH_PARENT);

			try {
				lineaver.setBackgroundColor(Color.parseColor(Propiedades.estilo_entidad.getColor_barra_seccion()));
			} catch (Exception e) {
				// TODO: handle exception
			}

			layouthorizontal.addView(lineaver, paramslinver);

			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;

			layouthorizontal.addView(text_seccion, params2);

			layoutseccion.addView(layouthorizontal, params);
			// layoutseccion.addView(linea, paramslin);
			text_seccion.setPadding(15, 0, 0, 0);
			text_seccion.setText(campotemp.getSeccion());

			Seccion miseccion = new Seccion(campotemp.getSeccion(), layoutseccion, true);
			layout.addView(miseccion.getLayoutseccion(), params);
			arreglo_seccion.add(miseccion);
		}

	}

	public void dividir_seccion() {
		// Seccion sec = null;
		//
		// if (arreglo_seccion != null) {
		// View linea = new View(con);
		// LinearLayout.LayoutParams paramslin = new
		// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
		// paramslin.topMargin = 10;
		// int sdk = android.os.Build.VERSION.SDK_INT;
		//
		// try {
		//
		// linea.setBackgroundColor(Color.parseColor(Propiedades.estilo_entidad.color_barra_seccion));
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		//
		// try {
		// for (int i = 0; i < arreglo_seccion.size(); i++) {
		//
		// arreglo_seccion.get(i).getLayoutseccion().addView(linea, paramslin);
		//
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		//
		// }

	}

	public Seccion buscar_seccion(String seccion) {
		Seccion sec = null;

		if (arreglo_seccion != null) {
			try {
				for (int i = 0; i < arreglo_seccion.size(); i++) {

					if (arreglo_seccion.get(i).getSeccion().equals(seccion)) {

						sec = arreglo_seccion.get(i);

					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return sec;
	}

	public String getvalor(String key) {
		String valor = "";
		try {

			String datos = sp_perfil.getString("datos", "xxxxxxxxxxx");
			Log.i("pqr", "arreglo guardado" + datos);
			JSONObject json = new JSONObject(datos);

			valor = json.getString(key);
			Log.i("pqr", "arreglo guardado" + valor);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return valor;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// init();

	}

	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<Campo> campos = ((MyApplication) this.getApplication()).campos;
		if (campos.size() > 0) {
			if ((Propiedades.entidades.length()) < 80) {

				getentidades();

			}
		} else {

			Intent intent = new Intent(con, SolicitudesListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
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

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ll_file.refreshDrawableState();

		if (imagen_drawable) {
			try {

				AlphaAnimation alpha = new AlphaAnimation(1F, 0.1F); // change
				// alpha.setBackgroundColor(Color.RED);
				// as you want
				alpha.setDuration(1800); // Make animation instant
				alpha.setFillAfter(true); // Tell it to persist after the
											// animation
				// And then on your imageview
				logo.startAnimation(alpha);
				alpha.setAnimationListener(new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

						// LightingColorFilter ColorFilter = new
						// LightingColorFilter(
						// color.red, 0xFFB2B2B2);
						// d_cambio.setColorFilter(ColorFilter);
						// LightingColorFilter ColorFilter2 = new
						// LightingColorFilter(
						// color.yellow, 0xFFE0E0E0);
						// d_cambio.setColorFilter(ColorFilter2);
						// LightingColorFilter ColorFilter3 = new
						// LightingColorFilter(
						// color.blue, 0xFF6B6B6B);
						// d_cambio.setColorFilter(ColorFilter3);
						// int sdk = android.os.Build.VERSION.SDK_INT;
						// if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
						// {
						// logo.setBackgroundDrawable(d_cambio);
						// } else {
						// logo.setBackground(d_cambio);
						// }
					}
				});

				enviar.setBackground(states);
				mStartActivityButton.setBackground(states1);

				ib_formulario_foto.setBackground(states2);
				ib_formulario_video.setBackground(states3);

			} catch (Exception e) {
				// TODO: handle exception
			}

			cargarbutton();
			if (ll_file.getChildCount() > 0) {
				scroll = (ScrollView) findViewById(R.id.scrollView1);
				scroll.post(new Runnable() {
					@Override
					public void run() {

						scroll.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});

			}

			// logo.setAlpha(0.1F);
		}
	}

	public void dialog_progress() {

		dialog = ProgressDialog.show(con, con.getString(R.string.cargando),
				con.getString(R.string.progress_dialog_enviando));
		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(getResources().getColor(R.color.aplicacion));
		} else {
			try {
				divider.setBackground(getResources().getDrawable(R.drawable.fondoactionbar));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.aplicacion));

	}

	/******************************************************************/

	/************************ funciones del menu **********************/

	/***************************************************************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.formulario_creacion, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.cambiar_entidad_menu_item) {
			nombreEntidad = null;
			showChooseEntidadDialog();
			return true;
		}
		if (item.getItemId() == R.id.cancelar_menu_item) {
			listarsolicitudes();
			return true;
		}
		if (item.getItemId() == R.id.eliminar_datos) {
			// preguntarguardado();
			// borrardatos();
			// reiniciar();
			return true;
		}
		if (item.getItemId() == R.id.ayuda) {
			showInstruccionesDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		openDialogsalir();
	}

	private void openDialogsalir() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Salir");
		// set dialog message

		alertDialogBuilder.setMessage("Esta seguro de descartar el pqr?");

		alertDialogBuilder.setCancelable(false);

		alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});
		alertDialogBuilder.setPositiveButton(getResources().getString(R.string.si),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				});
		AlertDialog dialogo1 = alertDialogBuilder.create();

		dialogo1.show();
		Button cancelar = (Button) dialogo1.getButton(AlertDialog.BUTTON_NEGATIVE);

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			cancelar.setBackgroundDrawable((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
		} else {
			cancelar.setBackground((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
		}

		cancelar.setTextColor(this.getResources().getColor(R.color.white));

		Button aceptar = (Button) dialogo1.getButton(AlertDialog.BUTTON_POSITIVE);
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			aceptar.setBackgroundDrawable((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
		} else {
			aceptar.setBackground((Drawable) this.getResources().getDrawable(R.drawable.boton_dialogo));
		}
		aceptar.setTextColor(this.getResources().getColor(R.color.white));

		int dividerId = dialogo1.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialogo1.findViewById(dividerId);
		sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(this.getResources().getColor(R.color.aplicacion_oscuro));
		} else {
			try {
				divider.setBackground(getResources().getDrawable(R.color.aplicacion_oscuro));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		int textViewId = dialogo1.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialogo1.findViewById(textViewId);
		tv.setTextColor(this.getResources().getColor(R.color.aplicacion));

	}

	private void listarsolicitudes() {
		Intent consultarIntent = new Intent(this, SolicitudesListActivity.class);
		startActivity(consultarIntent);
		finish();
	}

	private void showChooseEntidadDialog() {
		Intent formularioIntent = new Intent(this, SeleccionarEntidad.class);
		startActivity(formularioIntent);
	}

	private void showInstruccionesDialog() {
		final Dialog instruccionesDialog = new Dialog(this);
		instruccionesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		instruccionesDialog.setContentView(R.layout.instrucciones_dialog);

		// instruccionesDialog.setTitle(this
		// .getString(R.string.informacion_dialogo_titulo));
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

	/************************************************************/

	/******* valida los campos y crea el arreglo de respuesta *****/
	/************************************************************/
	public String validar() throws JSONException {
		String envio = "";
		int numero_campos = ((MyApplication) this.getApplication()).campos.size();
		ArrayList<Campo> campos = ((MyApplication) this.getApplication()).campos;
		int num_edit = 0;
		int num_spinner = 0;
		boolean valido = true;
		json = new JSONObject();
		json_guardar = new JSONObject();
		// JSONArray json_array = new JSONArray();
		for (int i = 0; i < numero_campos; i++) {
			Campo campotemp = campos.get(i);
			Log.i("formulario", "nombre del campo " + campotemp.getNombre() + " " + campotemp.isObligatorio());

			if (campotemp.getTipo_de_campo().equals("edittext")) {
				String respuesta = arregloEdit.get(num_edit).getText().toString();

				if (!respuesta.equals("") && campotemp.isGuardar()) {

					json_guardar.put(campotemp.getNombre(), respuesta);
				}

				if (campotemp.isObligatorio() == true || !respuesta.equals("")) {

					Log.i("formulario", "repuesta " + respuesta);

					Seccion sec_tem = buscar_seccion(campotemp.getSeccion());

					if (validar_variable_editview(respuesta, campotemp, arregloEdit.get(num_edit),
							sec_tem.getVisible())) {
						if (sec_tem.getVisible()) {

							json.put(campotemp.getNombre_parametro(), respuesta);
						} else {

							json.put(campotemp.getNombre_parametro(), "anonimo");
							valido = true;
						}
					} else {

						valido = false;

					}

				} else {
					json.put(campotemp.getNombre_parametro(), respuesta);
					arregloEdit.get(num_edit).setError(null);
				}
				num_edit++;
			}

			if (campotemp.getTipo_de_campo().equals("Drop_down")) {
				// arreglospinner = new ArrayList<Spinner>();

				String respuesta = String.valueOf(arreglospinner.get(num_spinner).getSelectedItemPosition());
				int posicion = arreglospinner.get(num_spinner).getSelectedItemPosition() - 1;

				Log.i("pqr", "id selec" + posicion);
				Seccion sec_tem = buscar_seccion(campotemp.getSeccion());

				if (validar_variable_spinner(respuesta, campotemp, arreglo_errores.get(num_spinner),
						sec_tem.getVisible())) {

					ArrayList<Item> lista_item = new ArrayList<Item>();

					for (int j = 0; j < ((MyApplication) this.getApplication()).listas.size(); j++) {
						if (((MyApplication) this.getApplication()).listas.get(j).getNombre()
								.equals(campotemp.getConsulta())) {

							lista_item = ((MyApplication) this.getApplication()).listas.get(j).getListaArrayList();

							break;
						}

					}
					try {

						json.put(campotemp.getId_parametro(), lista_item.get(posicion).getId());
						json.put(campotemp.getNombre_parametro(), lista_item.get(posicion).gettexto());

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {

					valido = false;

				}

				num_spinner++;
			}

		}

		if (valido) {
			spe_perfil.putString("datos", json_guardar.toString());

			String adjunto = "";
			if (ll_file != null) {

				Log.d("CANTIDAD DE ADJUNTOS", ll_file.getChildCount() + "");

				if (ll_file.getChildCount() > 0) {
					for (int i = 0; i < ll_file.getChildCount(); i++) {
						if (ll_file.getChildAt(i) instanceof LinearLayout) {
							LinearLayout ll = (LinearLayout) ll_file.getChildAt(i);

							for (int j = 0; j < ll.getChildCount(); j++) {
								View v = ll.getChildAt(j);
								if (v instanceof TextView) {
									adjunto = adjunto + ((TextView) v).getTag() + "|";
								}
							}
						}
					}
					adjunto = adjunto.substring(0, adjunto.length() - 1);
				} else {
					adjunto = "";
				}
			}

			json.put("adjunto[]", adjunto);

			envio = Agregar_datos_telefono(json);

		}

		return envio;
	}

	public String Agregar_datos_telefono(JSONObject json) {

		try {
			json.put("entidad", nombreEntidad);

			json.put("codigoEntidad", Propiedades.codigo_entidad);

			json.put("siglaEntidad", Propiedades.sigla_entidad);
			json.put("webservice", webService);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			json.put("medio_recepcion", Propiedades.medio_recepcion);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// ---IMEI y SIM Card ID
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

			// ---get the SIM card ID---
			String simID = tm.getSimSerialNumber();
			// ---get the IMEI number---
			String IMEI = tm.getDeviceId();
			// ---get the phone number---
			String telNumber = "";

			if (tm.getLine1Number() != null) {
				telNumber = tm.getLine1Number();
			}

			json.put("simcard", simID);
			json.put("imei", IMEI);
			json.put("codigo_pais", "1");
			json.put("nombre_pais", "Colombia");

			json.put("hecho", "");
			json.put("numeroTelefonicoDispositivo", telNumber);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String IdGCM = "";
		try {
			GCMRegistrar.checkDevice(FormularioActivity.this);
			GCMRegistrar.checkManifest(FormularioActivity.this);
			GCMRegistrar.register(FormularioActivity.this, "586611920514");
			IdGCM = GCMRegistrar.getRegistrationId(FormularioActivity.this);

		} catch (Exception exe) {
			Log.d(Propiedades.TAG, "GCM " + exe.toString());
		}

		try {

			json.put("idgcm", IdGCM);

			json.put("latitud", latitud);

			json.put("longitud", longitud);

			String randomNum = getCadenaAlfanumAleatoria(8);
			json.put("codigo", randomNum);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

	public String getCadenaAlfanumAleatoria(int longitud) {
		String cadenaAleatoria = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		int j = 0;
		while (i < longitud / 2) {
			char c = (char) r.nextInt(255);
			if ((c >= 'A' && c != 'I' && c != 'O' && c <= 'Z')) {
				cadenaAleatoria += c;
				i++;
			}
		}
		while (j < longitud / 2) {
			char c = (char) r.nextInt(255);
			if ((c >= '2' && c <= '9')) {
				cadenaAleatoria += c;
				j++;
			}
		}
		return cadenaAleatoria;
	}

	private void verificarguardadodatos() {
		if (sp_configuracion.getString(Propiedades.PREGUNTAR, "").contentEquals(Propiedades.NO)) {
			if (sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.SI)) {
				if (actualizar) {
					guardardatos();
					servicio.execute();
				} else {
					servicio.execute();
				}
			} else {
				servicio.execute();
			}
		} else {
			mostrardialogoguardardatos();
		}
	}

	private void mostrardialogoguardardatos() {
		AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);

		View v = getLayoutInflater().inflate(R.layout.guardar_datos, null);
		final CheckBox cb_guardar_datos = (CheckBox) v.findViewById(R.id.cb_guardar_datos);
		TextView tv_guardar_datos = (TextView) v.findViewById(R.id.tv_guardar_datos);

		if (sp_configuracion.getString(Propiedades.PREGUNTAR, "").contentEquals(Propiedades.NO)
				&& sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.SI)) {
			v.findViewById(R.id.cb_guardar_datos).setVisibility(View.GONE);
			tv_guardar_datos.setText(R.string.pregunta_actualizar);
		} else {
			tv_guardar_datos.setText(R.string.pregunta_recordar_datos);
		}

		dialogo1.setView(v);
		dialogo1.setTitle(getString(R.string.tituloguadardatos));
		dialogo1.setCancelable(true);
		dialogo1.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogo1, int id) {
				if (cb_guardar_datos.isChecked()) {

					nopreguntarguardado();

				}
				guardardatos();
				servicio.execute();
				dialogo1.dismiss();

			}
		});
		dialogo1.setNeutralButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogo1, int id) {
				if (cb_guardar_datos.isChecked()) {
					nopreguntarguardado();
				}
				borrardatos();

				dialogo1.dismiss();
			}
		});

		dialogo1.show();

	}

	/******************************************************************/

	/******** transacciones para guardar en archivo plano configuracion */
	/******************************************************************/
	protected void guardardatos() {
		Log.d("GUARDAR DATOS", spe_perfil.toString());
		spe_perfil.commit();

	}

	protected void desactivarprecargado() {
		borrardatos();
		spe_configuracion.putString(Propiedades.GUARDAR, Propiedades.NO);
		spe_configuracion.commit();
	}

	private void borrardatos() {
		spe_perfil.clear();
		spe_perfil.commit();
		spe_perfil.clear();
		spe_perfil.commit();
	}

	protected void preguntarguardado() {
		spe_configuracion.putString(Propiedades.PREGUNTAR, Propiedades.SI);
		spe_configuracion.commit();
	}

	protected void nopreguntarguardado() {
		spe_configuracion.putString(Propiedades.PREGUNTAR, Propiedades.NO);
		spe_configuracion.commit();
	}

	/*********************************************************************/

	/********************** validacion de edittext *************************/
	/*********************************************************************/
	public boolean validar_variable_editview(String respuesta, Campo campo, EditText textv, boolean seccion_visible) {
		boolean valido = true;
		validacion_campos validar_campos = new validacion_campos();
		if (seccion_visible) {
			if (!respuesta.equals("")) {

				if (campo.getTipo().equals("numerico")) {
					if (!validar_campos.validar_numeros(respuesta)) {

						valido = false;
						textv.setError(campo.getMsg_error());
					}

				} else if (campo.getTipo().equals("email")) {
					if (!validar_campos.validar_email(respuesta)) {

						valido = false;
						textv.setError(campo.getMsg_error());
					}

				} else if (campo.getTipo().equals("direccion")) {
					if (!validar_campos.validar_direccion(respuesta)) {

						valido = false;
						textv.setError(campo.getMsg_error());
					}

				} else if (campo.getTipo().equals("telefono")) {
					if (!validar_campos.validar_telefono(respuesta)) {

						valido = false;
						textv.setError(campo.getMsg_error());
					}
				}

				else if (campo.getTipo().equals("multiline")) {
					if (!validar_campos.validar_descripcion(respuesta)) {

						valido = false;
						textv.setError(getResources().getString(R.string.mensaje_tipo4));

					} else {
						if (respuesta.length() < 20) {
							valido = false;
							textv.setError(campo.getMsg_error());
						} else {

							textv.setError(null);
						}
					}

				}

			} else {
				textv.setError("El campo es obligatorio");
				valido = false;
			}
		}
		if (valido) {
			textv.setError(null);
		}

		return valido;
	}

	public boolean validar_variable_spinner(String respuesta, Campo campo, TextView textv, boolean seccion_visible) {
		boolean valido = true;
		if (seccion_visible) {
			if (campo.isObligatorio()) {
				validacion_campos validar_campos = new validacion_campos();
				if (!respuesta.equals("")) {

					if (respuesta.equals("0")) {
						textv.setError("El campo es obligatorio");
						valido = false;

					}

				} else {
					textv.setError("El campo es obligatorio");
					valido = false;
				}

				if (campo.obligatorio) {
					if (!valido) {
						textv.setVisibility(View.VISIBLE);

					} else {
						textv.setVisibility(View.GONE);
					}
				} else {
					valido = true;
				}

			}

		}
		return valido;
	}

	public WebView crear_webview(Campo campo) {

		WebView webview = new WebView(con);
		String str = "<html><body><div style = \"background-color: RGBA (255,255,255,1)\";>" + campo.getMsg_error()
				+ "</div></body></html>";

		webview.setBackgroundColor(Color.TRANSPARENT);
		webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		webview.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
		// textv.setText(Html.fromHtml(campo.getMsg_error()));

		return webview;
	}

	/******************************************************************************************/

	/*****************
	 * creacion de edit text con respecto al objeto campo
	 ***********************/
	/********************************************************************************************/

	public LinearLayout crear_edittext(Campo campo, String valor) {

		LinearLayout layout = new LinearLayout(con);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView titulo = new TextView(con);
		String texto = campo.getNombre();

		if (campo.obligatorio) {
			texto += "*";

		}
		titulo.setText(texto);
		titulo.setTypeface(null, Typeface.BOLD);
		titulo.setPadding(0, 10, 0, 0);
		titulo.setTextColor(Color.parseColor(Propiedades.estilo_entidad.color_texto_campos));
		titulo.setTextSize(18);

		int margen = 15;
		int margen_izquierda = 20;
		LinearLayout layouthorizontal = new LinearLayout(con);
		layouthorizontal.setOrientation(LinearLayout.HORIZONTAL);
		TextView mText = new TextView(con);

		mText.setPadding(5, margen, 0, margen);
		mText.setTextColor(Color.BLACK);

		int tamano_letra = 12;

		mText.setTextSize(22);
		// mText.setTypeface(Typeface.SERIF, 0);
		mText.setText(campo.nombre);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		layouthorizontal.addView(mText, params);
		// add radio button

		EditText edit_text = new EditText(con);
		edit_text.setPadding(15, margen, 0, margen);
		String obligatorio = "";

		// if (campo.isObligatorio()) {
		// obligatorio = " *";
		//
		// }

		// edit_text.setHint(campo.nombre + obligatorio);

		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(campo.tamano);
		edit_text.setFilters(filterArray);

		if (!valor.equals("")) {
			edit_text.setText(valor);

		}

		if (campo.getTipo().equals("numerico")) {
			edit_text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT);
		} else if (campo.getTipo().equals("email")) {
			edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		} else if (campo.getTipo().equals("direccion")) {
			edit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
		}
		if (campo.getTipo().equals("telefono")) {
			edit_text.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
		} else if (campo.getTipo().equals("multiline")) {
			edit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE
					| InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
			edit_text.setPadding(0, 0, 0, 0);

			edit_text.setSingleLine(false);
			try {

				int lineas = (campo.tamano / 50) + 1;
				edit_text.setLines(5);
				edit_text.setHorizontallyScrolling(false);
				edit_text.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
				edit_text.setVerticalScrollBarEnabled(true);
				edit_text.setMovementMethod(ScrollingMovementMethod.getInstance());
				edit_text.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
				// edit_text.getLayoutParams().height="fill_parent";
				// edit_text.setHeight();
				edit_text.setGravity(Gravity.TOP);

			} catch (Exception e) {
				// TODO: handle exception

			}

		}
		Listener_editext listener = new Listener_editext(campo, edit_text);
		edit_text.addTextChangedListener(listener);

		arregloEdit.add(edit_text);

		layout.addView(titulo);
		layout.addView(edit_text);
		// edit_text.setmax(Integer.valueOf(campo.tamanio));
		return layout;
	}

	public LinearLayout crear_textview(Campo campo, String valor) {

		LinearLayout layout = new LinearLayout(con);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView titulo = new TextView(con);
		String texto = campo.getNombre();

		if (campo.obligatorio) {
			texto += "*";

		}
		titulo.setText(texto);

		titulo.setPadding(0, 10, 0, 0);
		titulo.setTextColor(Color.parseColor(Propiedades.estilo_entidad.getColor_texto_comentario()));
		titulo.setTextSize(18);

		int margen = 15;
		int margen_izquierda = 20;
		LinearLayout layouthorizontal = new LinearLayout(con);
		layouthorizontal.setOrientation(LinearLayout.HORIZONTAL);

		int tamano_letra = 12;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		layout.addView(titulo);
		return layout;
	}

	class Listener_editext implements TextWatcher {
		EditText edit;

		public Listener_editext(Campo campo, EditText edit) {
			this.edit = edit;

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			edit.setError(null);
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}

	/********************************************************************************/

	/********************************
	 * creacion de spinner
	 *****************************/
	/********************************************************************************/

	public LinearLayout crear_spinner(Campo campo) {

		LinearLayout layout = new LinearLayout(con);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView titulo = new TextView(con);
		String texto = campo.getNombre();

		if (campo.obligatorio) {
			texto += "*";

		}
		titulo.setText(texto);
		titulo.setTypeface(null, Typeface.BOLD);
		titulo.setPadding(0, 10, 0, 0);
		titulo.setTextColor(Color.parseColor(Propiedades.estilo_entidad.color_texto_campos));
		titulo.setTextSize(18);

		Spinner spinertext = new Spinner(con);

		RelativeLayout rela = new RelativeLayout(con);
		TextView error = new TextView(con);
		// error.setError("");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		// params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		// params.bottomMargin = 0;

		spinertext.setLayoutParams(params);
		spinertext.setPadding(0, 0, 0, 0);

		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		params.addRule(RelativeLayout.ALIGN_LEFT, spinertext.getId());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, spinertext.getId());

		error.setLayoutParams(params);
		error.setPadding(0, 0, 50, 20);

		// error.setFocusable(true);
		// error.setFocusableInTouchMode(true);

		/*
		 *
		 * <Spinner android:id="@+id/spnMySpinner" android:layout_width="400dp"
		 * android:layout_height="wrap_content"
		 * android:layout_alignParentTop="true"
		 * android:dropDownSelector="@drawable/selector_listview"
		 * android:background="@android:drawable/btn_dropdown"
		 * android:paddingBottom="0dp" android:layout_marginBottom="0dp" />
		 *
		 * <!-- Fake TextView to use to set in an error state to allow an error
		 * to be shown for the TextView --> <android.widget.TextView
		 * android:id="@+id/tvInvisibleError" android:layout_width="0dp"
		 * android:layout_height="0dp"
		 * android:layout_alignRight="@+id/spnMySpinner"
		 * android:layout_alignBottom="@+id/spnMySpinner"
		 * android:layout_marginTop="0dp" android:paddingTop="0dp"
		 * android:paddingRight="50dp" android:focusable="true"
		 * android:focusableInTouchMode="true" />
		 */
		rela.addView(spinertext);

		rela.addView(error);

		ArrayList lista = new ArrayList<String>();

		ArrayList<Item> lista_item = new ArrayList<Item>();

		for (int i = 0; i < ((MyApplication) this.getApplication()).listas.size(); i++) {
			if (((MyApplication) this.getApplication()).listas.get(i).getNombre().equals(campo.getConsulta())) {

				lista_item = ((MyApplication) this.getApplication()).listas.get(i).getListaArrayList();
				break;
			}

		}

		lista.add("Seleccione");
		for (int j = 0; j < lista_item.size(); j++) {

			lista.add(lista_item.get(j).gettexto());

		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				lista);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinertext.setAdapter(dataAdapter);
		ArrayList<Campo> campos = ((MyApplication) this.getApplication()).campos;

		ArrayList<Campo> campos_hijos = new ArrayList<Campo>();
		if (campo.getNumero_hijos() > 0) {

			for (int i = 0; i < campos.size(); i++) {
				Campo campotemp = campos.get(i);

				if (campotemp.getTipo_de_campo().equals("Drop_down")) {

					if (campotemp.getId_padre() == campo.getId()) {

						campos_hijos.add(campotemp);

					}
				}

			}

		}
		select_spinner listener = new select_spinner(campo, campos_hijos, error);

		spinertext.setOnItemSelectedListener(listener);

		spinertext.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideSoftKeyboard(act);

				return false;
			}
		});

		arreglo_errores.add(error);
		arreglospinner.add(spinertext);
		if (!texto.equals("")) {
			layout.addView(titulo);
		}
		layout.addView(rela);

		return layout;
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**********************************************************************************/
	/********************
	 * creacion de spinner_seccion
	 ***********************************/

	/********************
	 * prermite desactivar una seccion
	 *******************************/
	/**********************************************************************************/

	public CheckBox crear_spinner_seccion(Campo campo) {

		CheckBox check = new CheckBox(con);

		check.setText(campo.getNombre());
		check.setSelected(false);

		Seleccion_check_box listener = new Seleccion_check_box(campo);
		check.setOnCheckedChangeListener(listener);

		return check;

		// Spinner spinertext = new Spinner(con);
		//
		// RelativeLayout rela = new RelativeLayout(con);
		// TextView error = new TextView(con);
		// // error.setError("");
		// RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// // params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
		// RelativeLayout.TRUE);
		// // params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
		// RelativeLayout.TRUE);
		// // params.bottomMargin = 0;
		//
		// spinertext.setLayoutParams(params);
		// spinertext.setPadding(0, 0, 0, 0);
		//
		// params = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT,
		// RelativeLayout.LayoutParams.MATCH_PARENT);
		//
		// params.addRule(RelativeLayout.ALIGN_LEFT, spinertext.getId());
		// params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
		// spinertext.getId());
		//
		// error.setLayoutParams(params);
		// error.setPadding(0, 0, 50, 20);
		//
		// // error.setFocusable(true);
		// // error.setFocusableInTouchMode(true);
		//
		// /*
		// *
		// * <Spinner android:id="@+id/spnMySpinner"
		// android:layout_width="400dp"
		// * android:layout_height="wrap_content"
		// * android:layout_alignParentTop="true"
		// * android:dropDownSelector="@drawable/selector_listview"
		// * android:background="@android:drawable/btn_dropdown"
		// * android:paddingBottom="0dp" android:layout_marginBottom="0dp" />
		// *
		// * <!-- Fake TextView to use to set in an error state to allow an
		// error
		// * to be shown for the TextView --> <android.widget.TextView
		// * android:id="@+id/tvInvisibleError" android:layout_width="0dp"
		// * android:layout_height="0dp"
		// * android:layout_alignRight="@+id/spnMySpinner"
		// * android:layout_alignBottom="@+id/spnMySpinner"
		// * android:layout_marginTop="0dp" android:paddingTop="0dp"
		// * android:paddingRight="50dp" android:focusable="true"
		// * android:focusableInTouchMode="true" />
		// */
		// rela.addView(spinertext);
		//
		// rela.addView(error);
		//
		// ArrayList lista = new ArrayList<String>();
		// lista.add(campo.getNombre());
		// Item item_no = new Item("0", "No");
		// Item item_si = new Item("1", "Si");
		//
		// lista.add(item_no.gettexto());
		// lista.add(item_si.gettexto());
		//
		// ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_dropdown_item, lista);
		// dataAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinertext.setAdapter(dataAdapter);
		// select_spinner_seccion listener = new select_spinner_seccion(campo);
		// //
		// spinertext.setOnItemSelectedListener(listener);

		// return rela;
	}

	class Seleccion_check_box implements CompoundButton.OnCheckedChangeListener {
		Campo campo;
		int seleccion = 0;

		int num_actulizacion = 0;

		int posicion_seccion = 0;
		int tamanio = 0;

		public Seleccion_check_box(Campo campo) {
			this.campo = campo;

		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {

				Seccion sec = buscar_seccion(campo.getSeccion());
				if (sec.getVisible()) {
					tamanio = sec.getLayoutseccion().getLayoutParams().height;
					LayoutParams lp = (LayoutParams) sec.getLayoutseccion().getLayoutParams();
					lp.height = 0;
					sec.getLayoutseccion().setLayoutParams(lp);

					sec.setVisible(false);
				}

			} else {
				Seccion sec = buscar_seccion(campo.getSeccion());

				if (!sec.getVisible()) {
					// Seccion sec = buscar_seccion(campo.getSeccion());
					LayoutParams lp = (LayoutParams) sec.getLayoutseccion().getLayoutParams();
					lp.height = tamanio;
					sec.getLayoutseccion().setLayoutParams(lp);
					sec.setVisible(true);
				}

			}

		}

	}

	/************************************************************/

	/***************** creacion de checkbox localizacion **********/
	/************************************************************/

	public LinearLayout crear_check_localizacion() {

		LinearLayout layout = new LinearLayout(con);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.LEFT);
		layout.setPadding(0, 10, 0, 10);
		CheckBox checkbox = new CheckBox(con);
		checkbox.setText(con.getResources().getString(R.string.enviar_mis_coordenadas));

		TextView txtlongitud = new TextView(con);
		txtlongitud.setText(con.getResources().getString(R.string.lon));

		TextView txtlatitud = new TextView(con);

		txtlatitud.setText(con.getResources().getString(R.string.lat));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.leftMargin = 100;

		layout.addView(checkbox);
		layout.addView(txtlongitud, params);
		layout.addView(txtlatitud, params);

		if (!checkGPS() && !sp_configuracion.getString(Propiedades.PREGUNTARGPS, "").contentEquals(Propiedades.NO)) {
			openDialogConfirmGPS();
		}

		setLocationListener(txtlongitud, txtlatitud);

		Seleccioncheckbox listener = new Seleccioncheckbox(txtlongitud, txtlatitud);

		checkbox.setOnClickListener(listener);

		return layout;

	}

	@NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
	public void setLocationListener(TextView txtlongitud, TextView txtlatitud) {
		try {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			MyLocationListener mLocListener = new MyLocationListener(mLocManager, txtlongitud, txtlatitud);
			mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Seleccioncheckbox implements OnClickListener {
		TextView tlongitud, tlatitud;

		public Seleccioncheckbox(TextView tlongitud, TextView tlatitud) {
			this.tlongitud = tlongitud;
			this.tlatitud = tlatitud;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (((CheckBox) v).isChecked()) {

				if (!checkGPS()) {
					openDialogConfirmGPS();
				}
				tomar_coordenadas(((CheckBox) v), tlongitud, tlatitud);

			} else {

				tlongitud.setText(FormularioActivity.this.getString(R.string.lon));

				tlatitud.setText(FormularioActivity.this.getString(R.string.lat));

			}

		}

	}

	private boolean checkNetwork() {
		if (mLocManager == null) {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		boolean isNetwork = mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return isNetwork;
	}

	protected void tomar_coordenadas(CheckBox cb_formulario_coordenadas, TextView tlongitud, TextView tlatitud) {

		try {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			MyLocationListener mLocListener = new MyLocationListener(mLocManager, tlongitud, tlatitud);
			cb_formulario_coordenadas.setChecked(true);
			if (checkGPS()) {
				Toast.makeText(FormularioActivity.this, FormularioActivity.this.getString(R.string.espere),
						Toast.LENGTH_LONG).show();
				mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
				mLocListener.setMostrar(true);
			} else if (checkNetwork()) {
				mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
				mLocListener.setMostrar(true);

			} else {
				Toast.makeText(FormularioActivity.this, FormularioActivity.this.getString(R.string.error_wifi_revisar),
						Toast.LENGTH_LONG).show();
				cb_formulario_coordenadas.setChecked(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class select_spinner_seccion implements OnItemSelectedListener {

		int seleccion = 0;
		Campo campo;
		int num_actulizacion = 0;

		int posicion_seccion = 0;
		int tamanio = 0;

		public select_spinner_seccion(Campo campo) {
			this.campo = campo;

		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			if (arg2 == 2) {
				Seccion sec = buscar_seccion(campo.getSeccion());
				if (sec.getVisible()) {
					tamanio = sec.getLayoutseccion().getLayoutParams().height;
					LayoutParams lp = (LayoutParams) sec.getLayoutseccion().getLayoutParams();
					lp.height = 0;
					sec.getLayoutseccion().setLayoutParams(lp);

					sec.setVisible(false);

				}
			} else if (arg2 == 1) {
				Seccion sec = buscar_seccion(campo.getSeccion());

				if (!sec.getVisible()) {
					// Seccion sec = buscar_seccion(campo.getSeccion());
					LayoutParams lp = (LayoutParams) sec.getLayoutseccion().getLayoutParams();
					lp.height = tamanio;
					sec.getLayoutseccion().setLayoutParams(lp);
					sec.setVisible(true);
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	/****************************************************************************/
	/**
	 * clase que hereda de OnItemSelectedListener para escuchar los eventos de
	 **/

	/**
	 * los Spinner que tiene otros spiner dependientes
	 *************************/
	/****************************************************************************/

	public class select_spinner implements OnItemSelectedListener {

		ArrayList<Campo> campos_hijos;
		int seleccion = 0;
		Campo campo;
		int num_actulizacion = 0;
		TextView texterror;

		public select_spinner(Campo campo, ArrayList<Campo> campos_hijos, TextView texterror) {
			this.campo = campo;
			this.campos_hijos = campos_hijos;
			this.texterror = texterror;
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 != 0) {
				texterror.setError(null);
			}

			if (campos_hijos.size() > 0) {

				ArrayList<Item> lista_item = new ArrayList<Item>();
				for (int i = 0; i < MyApplication.listas.size(); i++) {
					if (((MyApplication) act.getApplication()).listas.get(i).getNombre().equals(campo.getConsulta())) {
						lista_item = ((MyApplication) act.getApplication()).listas.get(i).getListaArrayList();
						break;
					}
				}

				if (seleccion != arg2) {
					if (arg2 != 0) {
						dialog_progress();
						for (int i = 0; i < campos_hijos.size(); i++) {

							WS_Rest_client ws = new WS_Rest_client(con, act, WS_Rest_client.getlista);

							Handler puente_envio = new Handler() {
								@Override
								public void handleMessage(Message msg) {
									if (msg.what == 1) {
										num_actulizacion++;

										if (num_actulizacion == campos_hijos.size()) {

											// ingresa la indormacion de los
											// spiner
											// hijos
											int numero_spiner = 0;
											for (int k = 0; k < campos_hijos.size(); k++) {
												for (int j = 0; j < ((MyApplication) act.getApplication()).campos
														.size(); j++) {

													if (MyApplication.campos.get(j).getTipo_de_campo()
															.equals("Drop_down")) {

														Log.i("pqr",
																"datos lista"
																		+ ((MyApplication) act.getApplication()).campos
																				.get(j).nombre);

														if (campos_hijos.get(k).getId() == ((MyApplication) act
																.getApplication()).campos.get(j).getId()) {
															Log.i("pqr", "datos lista"
																	+ ((MyApplication) act.getApplication()).campos
																			.get(j).nombre);
															Spinner spinertext = arreglospinner.get(numero_spiner);

															ArrayList lista = new ArrayList<String>();
															lista.add("Seleccione " + campos_hijos.get(k).getNombre());

															ArrayList<Item> lista_item = new ArrayList<Item>();

															for (int i2 = 0; i2 < ((MyApplication) act
																	.getApplication()).listas.size(); i2++) {
																if (((MyApplication) act.getApplication()).listas
																		.get(i2).getNombre()
																		.equals(campos_hijos.get(k).getConsulta())) {

																	lista_item = ((MyApplication) act
																			.getApplication()).listas.get(i2)
																					.getListaArrayList();
																	break;
																}

															}

															for (int j2 = 0; j2 < lista_item.size(); j2++) {

																lista.add(lista_item.get(j2).gettexto());

															}
															spinertext.setAdapter(null);
															ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
																	con, android.R.layout.simple_spinner_dropdown_item,
																	lista);
															dataAdapter.setDropDownViewResource(
																	android.R.layout.simple_spinner_dropdown_item);
															spinertext.setAdapter(dataAdapter);

															dialog.cancel();
														}
														numero_spiner++;
													}

												}

											}
											numero_spiner = 0;
										}
										num_actulizacion = 0;
									} else if (msg.what == 0) {

									}
								}
							};

							ws.setPuente_envio(puente_envio);
							ws.getlista(campos_hijos.get(i).getConsulta(),
									String.valueOf(lista_item.get(arg2 - 1).getId()));
							ws.execute();
						}
					}
				}

				seleccion = arg2;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	/****************************************************************************************/

	/*************************
	 * metodos para la localizacion
	 ***********************************/
	/****************************************************************************************/

	public void openDialogGPS() {
		startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
	}

	public Boolean checkGPS() {
		if (mLocManager == null) {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		boolean isGPS = mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return isGPS;
	}

	public class MyLocationListener implements LocationListener {
		private LocationManager mLocManager;
		private boolean mostrar;
		TextView tlogitud, tlatitud;

		public MyLocationListener(LocationManager mLocManager, TextView tlogitud, TextView tlatitud) {
			this.mLocManager = mLocManager;
			this.tlatitud = tlatitud;
			this.tlogitud = tlogitud;
		}

		public void setMostrar(boolean mostrar) {
			this.mostrar = mostrar;
		}

		public void onLocationChanged(Location loc) {
			if (mostrar) {
				tlogitud.setVisibility(View.VISIBLE);
				tlatitud.setVisibility(View.VISIBLE);

				tlogitud.setText(con.getResources().getString(R.string.longitud) + ": " + loc.getLongitude() + "");

				longitud = String.valueOf(loc.getLongitude());

				tlatitud.setText(con.getResources().getString(R.string.latitud) + ": " + loc.getLatitude() + "");
				latitud = String.valueOf(loc.getLatitude());
				mLocManager.removeUpdates(this);
				mLocManager = null;
			}

		}

		public void onProviderDisabled(String arg0) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

	private void openDialogConfirmGPS() {
		AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
		View v = getLayoutInflater().inflate(R.layout.guardar_datos, null);
		final CheckBox cb_guardar_datos = (CheckBox) v.findViewById(R.id.cb_guardar_datos);
		cb_guardar_datos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cb_guardar_datos.isChecked()) {
					spe_configuracion.putString(Propiedades.PREGUNTARGPS, Propiedades.NO);
					spe_configuracion.commit();
				} else {
					spe_configuracion.putString(Propiedades.PREGUNTARGPS, Propiedades.SI);
					spe_configuracion.commit();
				}
			}
		});

		TextView tv_guardar_datos = (TextView) v.findViewById(R.id.tv_guardar_datos);
		tv_guardar_datos.setText(R.string.descripcion_gps);
		dialogo1.setView(v);
		dialogo1.setTitle(getString(R.string.desea_activar_gps));
		dialogo1.setCancelable(true);
		dialogo1.setPositiveButton(getString(R.string.si), new OkOnClickListener());
		dialogo1.setNeutralButton(getString(R.string.no), new CancelOnClickListener());
		dialogo1.show();

	}

	private final class CancelOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			// cb_formulario_coordenadas.setChecked(false);
		}
	}

	private final class OkOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			openDialogGPS();
		}
	}

	/****************************
	 * gestor de archivos
	 *****************************/

	private int conseguir_parametros_adjuntos() {
		Log.i("informacion", "paso del archivo" + Propiedades.parametros);
		try {
			JSONObject jo = new JSONObject(Propiedades.parametros);

			int max_upload = get_machine_readable(jo.getString("mu"));

			if (max_upload <= TAMANOMAXIMO) {
				TAMANOMAXIMO = max_upload;
				STRTAMANOMAXIMO = jo.getString("mu");
				return Integer.parseInt(jo.getString("nof"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ADJUNTOS_MAXIMO;
	}

	@SuppressWarnings("resource")
	private int get_machine_readable(String string) {
		Scanner in = new Scanner(string).useDelimiter("[^0-9]+");
		int integer = in.nextInt();
		if (string.toLowerCase().contains("m")) {
			return integer * 1000000;
		}

		return integer;
	}

	public void startfilepicker() {
		Toast.makeText(this, getString(R.string.archivo_muy_grande) + " " + STRTAMANOMAXIMO, Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(this, FilePickerActivity.class);

		// Set the initial directory to be the sdcard
		intent.putExtra(FilePickerActivity.EXTRA_FILE_PATH, Environment.getExternalStorageDirectory().toString());

		// Show hidden files
		// intent.putExtra(FilePickerActivity.EXTRA_SHOW_HIDDEN_FILES, true);

		// Only make .png files visible
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add(".pdf");
		extensions.add(".PDF");
		extensions.add(".doc");
		extensions.add(".DOC");
		extensions.add(".docx");
		extensions.add(".DOCX");
		extensions.add(".jpg");
		extensions.add(".JPG");
		extensions.add(".png");
		extensions.add(".PNG");
		extensions.add(".xls");
		extensions.add(".XLS");
		extensions.add(".xlsx");
		extensions.add(".XLSX");
		extensions.add(".ppt");
		extensions.add(".PPTX");
		extensions.add(".tif");
		extensions.add(".TIF");
		extensions.add(".tiff");
		extensions.add(".TIFF");

		intent.putExtra(FilePickerActivity.STAMMAX, STRTAMANOMAXIMO);

		intent.putExtra(FilePickerActivity.EXTRA_ACCEPTED_FILE_EXTENSIONS, extensions);

		intent.putExtra(FilePickerActivity.MAXIMUM_SIZE, TAMANOMAXIMO);

		// Start the activity
		startActivityForResult(intent, REQUEST_PICK_FILE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		final View raya = new View(this);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
		raya.setLayoutParams(llp);
		raya.setBackgroundColor(Color.BLACK);

		if (resultCode == RESULT_OK && resultCode != RESULT_CANCELED) {

			if (requestCode == REQUEST_PICK_FILE) {
				if (data.hasExtra(FilePickerActivity.EXTRA_FILE_PATH)) {
					String filePath = data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH);

					File f = new File(filePath);
					Boolean archivovalido = false;
					try {
						BytesStreamsAndFiles test = new BytesStreamsAndFiles();
						byte[] fileContents = test.read(filePath);

						StringBuilder sb = new StringBuilder();

						for (int i = 0; i < 10; i++) {
							int a = fileContents[i] & 0xff;
							sb.append((Integer.toHexString(a)));
						}

						String[] archivosValidos = { "25504446", "D0CF11E0A1B11AE1", "504B34", "504B0304", "FFD8",
								"89504E47", "4141", "4949" };

						for (int i = 0; i < archivosValidos.length; i++) {
							if (sb.toString().matches("(?i).*" + archivosValidos[i] + ".*")) {
								archivovalido = true;
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (archivovalido) {
						final View archivo = getLayoutInflater().inflate(R.layout.archivo, null);

						final TextView mFilePathTextView = (TextView) archivo.findViewById(R.id.file_path_text_view);
						// Set the file path text view
						mFilePathTextView.setTag(f.getAbsolutePath());
						mFilePathTextView.setText(f.getName() + Propiedades.getHumanReadable(f.length(), true));

						String[] archivosValidos = { "pdf", "doc", "jpg", "png", "tif", "xls", "ppt" };
						int[] imagenesArchivos = { R.drawable.pdf, R.drawable.doc, R.drawable.ima, R.drawable.ima,
								R.drawable.ima, R.drawable.xls, R.drawable.ima };

						if (ll_file.getChildCount() > 0) {
							ll_file.addView(raya);
						}

						final ImageView file_icon_image_view = (ImageView) archivo
								.findViewById(R.id.file_icon_image_view);

						for (int i = 0; i < archivosValidos.length; i++) {
							if (f.getName().toLowerCase().split("\\.")[1].contains(archivosValidos[i])) {
								file_icon_image_view.setBackgroundResource(imagenesArchivos[i]);
								break;
							}

						}

						ImageView iv_archivo = (ImageView) archivo.findViewById(R.id.file_path_image_view);
						iv_archivo.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								ll_file.removeView(archivo);
								ll_file.removeView(raya);
								revisarCantidadAdjuntos(true);
							}
						});
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						ll_file.addView(archivo, params);
					} else {
						Toast.makeText(this, getString(R.string.mensaje_archivo_valido), Toast.LENGTH_SHORT).show();
						revisarCantidadAdjuntos(true);
					}
				}

			} else if (requestCode == TAKE_PHOTO_CODE || requestCode == CAPTURE_IMAGE) {
				File f = new File(path_foto.getEncodedPath());

				Log.e("PHOTO_SIZE", f.length() + " " + TAMANOMAXIMO);

				if (f.length() > TAMANOMAXIMO) {
					Toast.makeText(this, getString(R.string.archivo_muy_grande) + " " + STRTAMANOMAXIMO,
							Toast.LENGTH_SHORT).show();
				} else {
					if (data != null) {
						Log.d("CameraDemo", "Pic saved " + data.hasExtra(MediaStore.EXTRA_OUTPUT));
					} else {
						Log.d("CameraDemo", "Pic saved " + path_foto);
					}

					final View archivo = getLayoutInflater().inflate(R.layout.archivo, null);

					TextView mFilePathTextView = (TextView) archivo.findViewById(R.id.file_path_text_view);

					final ImageView file_icon_image_view = (ImageView) archivo.findViewById(R.id.file_icon_image_view);
					file_icon_image_view.setBackgroundResource(R.drawable.ima);

					mFilePathTextView.setTag(path_foto.getEncodedPath());
					mFilePathTextView
							.setText(path_foto.getLastPathSegment() + Propiedades.getHumanReadable(f.length(), true));

					if (ll_file.getChildCount() > 0) {
						ll_file.addView(raya);
					}

					ImageView iv_archivo = (ImageView) archivo.findViewById(R.id.file_path_image_view);
					iv_archivo.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ll_file.removeView(archivo);
							ll_file.removeView(raya);
							revisarCantidadAdjuntos(true);
						}
					});
					ll_file.addView(archivo);
				}

			} else if (requestCode == CAPTURE_VIDEO) {

				File f = new File(path_video.getEncodedPath());

				if (f.length() > TAMANOMAXIMO)

				{
					Toast.makeText(this, getString(R.string.archivo_muy_grande) + " " + STRTAMANOMAXIMO,
							Toast.LENGTH_SHORT).show();
				} else

				{
					if (data != null) {
						Log.d("CameraDemo", "Pic saved " + data.hasExtra(MediaStore.EXTRA_OUTPUT));
					} else {
						Log.d("CameraDemo", "Pic saved " + path_video);
					}

					final View archivo = getLayoutInflater().inflate(R.layout.archivo, null);

					TextView mFilePathTextView = (TextView) archivo.findViewById(R.id.file_path_text_view);
					final ImageView file_icon_image_view = (ImageView) archivo.findViewById(R.id.file_icon_image_view);
					file_icon_image_view.setBackgroundResource(R.drawable.vid);
					mFilePathTextView.setTag(path_video.getEncodedPath());
					mFilePathTextView
							.setText(path_video.getLastPathSegment() + Propiedades.getHumanReadable(f.length(), true));

					if (ll_file.getChildCount() > 0) {
						ll_file.addView(raya);
					}
					ImageView iv_archivo = (ImageView) archivo.findViewById(R.id.file_path_image_view);

					iv_archivo.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ll_file.removeView(archivo);
							ll_file.removeView(raya);
							revisarCantidadAdjuntos(true);
						}
					});
					ll_file.addView(archivo);

					cargarbutton();
				}

			}
		}

		revisarCantidadAdjuntos(false);

	}

	public void cargarbutton() {
		try {

			GradientDrawable shape_defauld = new GradientDrawable();
			shape_defauld.setShape(GradientDrawable.RECTANGLE);
			shape_defauld.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			shape_defauld.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton()));
			shape_defauld.setStroke(0, Color.WHITE);

			BitmapDrawable textura = new BitmapDrawable(
					BitmapFactory.decodeResource(getResources(), R.drawable.textureb2));
			// TileMe.setTileModeX(TileMode.REPEAT);
			textura.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

			Drawable[] drawarray = { shape_defauld, textura };
			LayerDrawable layerdrawable = new LayerDrawable(drawarray);

			GradientDrawable shape_select = new GradientDrawable();

			shape_select.setShape(GradientDrawable.RECTANGLE);
			shape_select.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });

			shape_select.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton_select()));
			shape_select.setStroke(0, Color.WHITE);

			Drawable[] drawarrayselect = { shape_select, textura };
			LayerDrawable layerdrawableselect = new LayerDrawable(drawarrayselect);

			StateListDrawable states_cab = new StateListDrawable();
			states_cab.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
			states_cab.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
			states_cab.addState(new int[] {}, layerdrawable);

			try {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
					enviar.setBackground(getResources().getDrawable(R.drawable.boton_dialogo));
				} else {
				// enviar.setBackgroundDrawable(getResources().getDrawable(R.drawable.boton_dialogo));
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			enviar.refreshDrawableState();
			ll_buttons.refreshDrawableState();

			enviar.setBackground(states_cab);
			enviar.refreshDrawableState();
			ll_buttons.refreshDrawableState();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void revisarCantidadAdjuntos(boolean mostrar) {
		int adjuntos = 0;

		for (int i = 0; i < ll_file.getChildCount(); i++) {
			if (ll_file.getChildAt(i) instanceof LinearLayout) {
				adjuntos++;
			}
			Log.d("revisarCantidadAdjuntos", ll_file.getChildAt(i).toString());
		}

		if (adjuntos == (ADJUNTOS_MAXIMO)) {
			ll_botones_adjuntar.setVisibility(View.GONE);
		}
		if (mostrar) {
			ll_botones_adjuntar.setVisibility(View.VISIBLE);
		}

		if (ll_file.getChildCount() > 0) {
			ScrollView scroll = (ScrollView) findViewById(R.id.scrollView1);
			scroll.fullScroll(View.FOCUS_DOWN);
		}

	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	/* captura de imagenes y video */
	protected void tomar_foto() {
		if (checkCamera()) {
			Toast.makeText(this, getString(R.string.archivo_muy_grande) + " " + STRTAMANOMAXIMO, Toast.LENGTH_SHORT)
					.show();

			final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
			File newdir = new File(dir);
			newdir.mkdirs();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			String file = dir + dateFormat.format(date) + ".jpg";
			File newfile = new File(file);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			path_foto = Uri.fromFile(newfile);
			launchCamera();
		} else {
			Toast.makeText(this, getString(R.string.revisar_si_tiene_camara), Toast.LENGTH_SHORT).show();
		}

	}

	@NeedsPermission(Manifest.permission.CAMERA)
	public void launchCamera() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
		} else {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, path_foto);
			startActivityForResult(cameraIntent, CAPTURE_IMAGE);
		}
	}

	@SuppressLint("NeedOnRequestPermissionsResult")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE_CAMERA) {
			launchCamera();
		}
	}

	private boolean checkCamera() {
		PackageManager pm = getPackageManager();
		boolean frontCam, rearCam;

		// Must have a targetSdk >= 9 defined in the AndroidManifest
		frontCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
		rearCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		Log.d("CAMARAS", frontCam + " " + rearCam);

		int numCameras = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			numCameras = Camera.getNumberOfCameras();
		}

		if (numCameras > 0 || rearCam || frontCam) {
			return true;

		} else {
			return false;
		}
	}

	protected void grabar_video() {
		if (checkCamera()) {
			Toast.makeText(this, getString(R.string.archivo_muy_grande) + " " + STRTAMANOMAXIMO, Toast.LENGTH_SHORT)
					.show();

			final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/";
			File newdir = new File(dir);
			newdir.mkdirs();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			String file = dir + dateFormat.format(date) + ".mov";
			File newfile = new File(file);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			path_video = Uri.fromFile(newfile);
			Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, path_video);
			startActivityForResult(cameraIntent, CAPTURE_VIDEO);
		} else {
			Toast.makeText(this, getString(R.string.revisar_si_tiene_camara), Toast.LENGTH_SHORT).show();
		}
	}

}
