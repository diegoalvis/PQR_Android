package net.micrositios.pqrapp;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 public String Json_correo(String palabra, String ruta, String contenido) {
 String mensaje = "";

 mensaje = "{palabra:" + "\"" + palabra + "\"" + ",ruta:" + "\"" + ruta
 + "\"" + "contenido:" + "\""
 + lista_item.get(seleccion).getTitle() + "\n " + "\"" + "}";
 Log.i("Cremil", mensaje);
 return mensaje;
 }

 public void enviar_correo(int seleccion, String correo) {
 consultasws_rest con = new consultasws_rest(
 Propiedades.servicio_envio_email, mContext);
 String url_archivo = lista_item.get(seleccion).getUrl_archivo();

 String mensaje = Json_correo("Ver PDF",
 url_archivo.replace(" ", "%20"), lista_item.get(seleccion)
 .getTitle());

 * 
 * */

@SuppressLint({ "NewApi", "SimpleDateFormat", "InflateParams", "DefaultLocale" })
public class FormularioCreacionActivity extends Activity {

	private boolean prueba = false;

	private static final int TAKE_PHOTO_CODE = 0;
	private static final int REQUEST_PICK_FILE = 1;
	private static final int CAPTURE_IMAGE = 2;
	private static final int CAPTURE_VIDEO = 3;
	private static int ADJUNTOS_MAXIMO = 5;
	public static int TAMANOMAXIMO = 10000000;
	private String STRTAMANOMAXIMO = "10M";

	private static String codigoSolicitud;
	private static String nombreSolicitud;
	private static String codigoAsunto = "";
	private static String nombreAsunto;
	private static String codigoCanalRespuesta;
	private static String nombreCanalRespuesta;
	private static String desSolValido;
	private static String hechosSolicitudValido;
	private static String randomNum;
	private static String nombreEntidad;
	private static String primerNombreValido;
	private static String segundoNombreValido;
	private static String primerApellidoValido;
	private static String segundoApellidoValido;
	private static String codigoIdentificacion;
	private static String nombreIdentificacion;
	private static String numeroIdentificacionValido;
	private static String nombreGrupoVulnerable;
	private static String codigoGrupoVulnerable;
	private static String direccionNotificacionValido;
	private static String eMailValido;
	private static String telefonoValido;
	private static String celularNotificacionValido;
	private static String codigoPais;
	private static String nombrePais;
	private static String codigoDepartamento;
	private static String nombreDepartamento;
	private static String codigoMunicipio;
	private static String nombreMunicipio;

	private String IMEI;
	private String simID;
	private String telNumber = "";
	private String[] codigosIdentificaciones;
	private String[] codigosPaises;
	private String[] codigosDepartamentos;
	private String[] codigosMunicipios;

	Map<String, String> map_grupos_vulnerables = new HashMap<String, String>();
	Map<String, String> map_medios_respuesta = new HashMap<String, String>();
	Map<String, String> map_tipos_solicitud = new HashMap<String, String>();
	Map<String, String> map_tipos_asunto = new HashMap<String, String>();
	Map<String, String> map_subtipos_solicitud = new HashMap<String, String>();
	Map<String, String> map_subtipos_asunto = new HashMap<String, String>();

	TextView tv, errorTipoSolicitudView, errorTipoDocumentoView, errorTipoCanalView, errorGrupoVulnerableView,
			nombreEntidadView, errorAsuntoView;
	EditText pNombreView, sNombreView, pApellidoView, sApellidoView, direccionView, identificacionView, eMailView,
			telefonoView, celularView;
	EditText desSolicitudView;
	EditText hechosSolicitudView;
	String webService;

	private Spinner sSolicitud;
	private Spinner sCanalRespuesta;
	private Spinner sIdentificaion;
	private Spinner sGrupoVulnerable;
	private Spinner sPais;
	private Spinner sDepartamento;
	private Spinner sMunicipio;

	String municipios_default, municipios_amazonas, municipios_antioquia;
	String codigos_municipios_default, codigos_municipios_amazonas, codigos_municipios_antioquia;

	boolean hechoRequerido = false;
	boolean documentoRequerido = true;
	boolean direccionRequerido = false;
	boolean eMailRequerido = false;
	boolean telefonoFijoRequerido = false;

	private final ArrayList<ValidarCampo> camposFormulario = new ArrayList<ValidarCampo>();

	private static String TEXTOTIPO1 = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]*$";// Texto sin
																	// espacios
	private static String TEXTOTIPO2 = "[A-Za-záéíóúÁÉÍÓÚñÑ\\s]*";// Texto con
																	// espacios
	private static String TEXTOTIPO3 = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";// email
	private static String TEXTOTIPO4 = "[-_.,:;¡!¿?'\"#A-Za-záéíóúÁÉÍÓÚñÑ0-9\\s]*";// Texto
																					// y
																					// caracteres
																					// especiales
	private static String TEXTOTIPO5 = "^[A-Z0-9]*$";// Texto y numeros
	private static String TEXTOTIPO6 = "^\\d*$";// Telefono

	private boolean tipoSolicitudEsValido, tipoDocumentoEsValido, tipoCanalEsValido, grupoVulnerableEsValido,
			asuntoEsValido;

	private ImageButton mStartActivityButton;

	private LinearLayout ll_file;

	private String adjunto = "";

	private String anonimo;
	private Spinner sAsuntos;
	private EditText pretensionesSolicitudView;
	private String pretencionesSolicitudValido;
	private SharedPreferences.Editor spe_perfil;
	private SharedPreferences.Editor spe_configuracion;
	private AsyncCallWS task;
	private SharedPreferences sp_perfil;
	private SharedPreferences sp_configuracion;
	private boolean actualizar = false;

	int finalizar = 0;
	private String IdGCM;
	private Spinner tipo_subsolicitud_view;
	private Spinner tipo_subasunto_view;
	private ImageButton ib_formulario_foto;
	private ImageButton ib_formulario_video;
	private CheckBox cb_formulario_coordenadas;
	private Uri path_foto;
	private Uri path_video;
	private TextView tv_formulario_longitud;
	private TextView tv_formulario_latitud;

	private LinearLayout ll_botones_adjuntar;
	private LocationManager mLocManager;
	private ImageView logo;
	Drawable d_cambio;
	Boolean imagen_drawable = true;
	private int tamano_descripcion = 250;
	private TextView text_limite_caracteres;
	private Context context;
	boolean mostrar_mensaje_caracteres = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario_creacion);
		nombreEntidad = Propiedades.entidad;
		webService = Propiedades.webservice;
		context = this;
		initcomponents();
	}

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
			preguntarguardado();
			borrardatos();
			reiniciar();
			return true;
		}
		if (item.getItemId() == R.id.ayuda) {
			showInstruccionesDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showChooseEntidadDialog() {
		Intent formularioIntent = new Intent(this, SeleccionarEntidad.class);
		startActivity(formularioIntent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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

			} catch (Exception e) {
				// TODO: handle exception
			}
			// logo.setAlpha(0.1F);
		}
	}

	protected void initcomponents() {
		sp_perfil = getSharedPreferences("perfil", Context.MODE_PRIVATE);
		sp_configuracion = getSharedPreferences("configuracion", Context.MODE_PRIVATE);

		spe_perfil = sp_perfil.edit();
		spe_configuracion = sp_configuracion.edit();

		if (!checkGPS() && !sp_configuracion.getString(Propiedades.PREGUNTARGPS, "").contentEquals(Propiedades.NO)) {
			openDialogConfirmGPS();
		}

		try {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			MyLocationListener mLocListener = new MyLocationListener(mLocManager);
			mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		text_limite_caracteres = (TextView) findViewById(R.id.textView_limite_caracteres);
		text_limite_caracteres.setText("Caracteres: " + tamano_descripcion);
		pNombreView = (EditText) findViewById(R.id.primer_nombre_view);
		pNombreView.setHintTextColor(Color.BLACK);
		setcampo(pNombreView);

		sNombreView = (EditText) findViewById(R.id.segundo_nombre_view);
		sNombreView.setHintTextColor(Color.BLACK);
		setcampo(sNombreView);

		pApellidoView = (EditText) findViewById(R.id.primer_apellido_view);
		pApellidoView.setHintTextColor(Color.BLACK);
		setcampo(pApellidoView);

		sApellidoView = (EditText) findViewById(R.id.segundo_apellido_view);
		sApellidoView.setHintTextColor(Color.BLACK);
		setcampo(sApellidoView);

		eMailView = (EditText) findViewById(R.id.e_mail_view);
		eMailView.setHintTextColor(Color.BLACK);
		setcampo(eMailView);

		telefonoView = (EditText) findViewById(R.id.telefono_view);
		telefonoView.setHintTextColor(Color.BLACK);
		setcampo(telefonoView);

		celularView = (EditText) findViewById(R.id.celular_view);
		celularView.setHintTextColor(Color.BLACK);
		setcampo(celularView);

		desSolicitudView = (EditText) findViewById(R.id.descripcion_solicitud_view);
		desSolicitudView.setHintTextColor(Color.BLACK);
		hechosSolicitudView = (EditText) findViewById(R.id.hechos_solicitud_view);
		hechosSolicitudView.setHintTextColor(Color.BLACK);
		pretensionesSolicitudView = (EditText) findViewById(R.id.pretensiones_solicitud_view);
		pretensionesSolicitudView.setHintTextColor(Color.BLACK);
		direccionView = (EditText) findViewById(R.id.direccion_view);
		direccionView.setHintTextColor(Color.BLACK);
		setcampo(direccionView);

		identificacionView = (EditText) findViewById(R.id.identificaion_view);
		identificacionView.setHintTextColor(Color.BLACK);
		setcampo(identificacionView);
		logo = (ImageView) findViewById(R.id.imageView_logo);
		try {

			Log.i("PQR", "pqr imagen " + Propiedades.estilo_entidad.getLogo_local());
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

		} catch (Exception e) {
			imagen_drawable = false;
			logo.setImageBitmap(null);
			// TODO: handle exception
		}
		// logo.setAlpha(0.1F);
		errorTipoSolicitudView = (TextView) findViewById(R.id.error_tipo_solicitud_view);
		errorTipoDocumentoView = (TextView) findViewById(R.id.error_tipo_documento_view);
		errorTipoCanalView = (TextView) findViewById(R.id.error_tipo_canal_view);
		errorGrupoVulnerableView = (TextView) findViewById(R.id.error_grupo_vulnerable_view);
		nombreEntidadView = (TextView) findViewById(R.id.entidad_view);
		mStartActivityButton = (ImageButton) findViewById(R.id.start_file_picker_button);
		errorAsuntoView = (TextView) findViewById(R.id.error_asunto_view);

		// ---Spinner View Grupo Vulnerable---
		sGrupoVulnerable = (Spinner) findViewById(R.id.grupo_vulnerable_view);
		ArrayList<String> grupos_vulnerables = conseguir_grupos_vulnerables();
		ArrayAdapter<String> aGrupoVulnerable = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				grupos_vulnerables);
		aGrupoVulnerable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sGrupoVulnerable.setAdapter(aGrupoVulnerable);

		sGrupoVulnerable.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				nombreGrupoVulnerable = sGrupoVulnerable.getSelectedItem().toString();
				codigoGrupoVulnerable = map_grupos_vulnerables.get(nombreGrupoVulnerable);

				if (codigoGrupoVulnerable.equals("0")) {
					grupoVulnerableEsValido = false;
				} else {
					errorGrupoVulnerableView.setVisibility(View.GONE);
					grupoVulnerableEsValido = true;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		mStartActivityButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startfilepicker();
			}
		});

		ib_formulario_foto = (ImageButton) findViewById(R.id.ib_formulario_foto);
		ib_formulario_foto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tomar_foto();
			}
		});

		ib_formulario_video = (ImageButton) findViewById(R.id.ib_formulario_video);
		ib_formulario_video.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				grabar_video();
			}
		});

		cb_formulario_coordenadas = (CheckBox) findViewById(R.id.cb_formulario_coordenadas);
		cb_formulario_coordenadas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cb_formulario_coordenadas.isChecked()) {
					if (!checkGPS()) {
						openDialogConfirmGPS();
					}
					tomar_coordenadas();
				} else {
					tv_formulario_longitud.setText(FormularioCreacionActivity.this.getString(R.string.lon));
					tv_formulario_latitud.setText(FormularioCreacionActivity.this.getString(R.string.lat));
				}
			}
		});

		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(tamano_descripcion);
		desSolicitudView.setFilters(filterArray);

		desSolicitudView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (desSolicitudView.getText().toString().length() == 250) {
					if (mostrar_mensaje_caracteres) {
						text_limite_caracteres.setText("Caracteres: "
								+ (tamano_descripcion - (desSolicitudView.getText().toString().length())));
						Toast.makeText(FormularioCreacionActivity.this,
								"Maximo caracteres 250. Si desea puede realizar su solicitud en un archivo adjunto.",
								Toast.LENGTH_LONG).show();
						Log.i("PQR",
								"Maximo caracteres 250. Si desea puede realizar su solicitud en un archivo adjunto.");
						mostrar_mensaje_caracteres = false;

					}
				} else {
					text_limite_caracteres.setText(
							"Caracteres: " + (tamano_descripcion - (desSolicitudView.getText().toString().length())));
				}

				if (desSolicitudView.getText().toString().length() <= 200) {
					mostrar_mensaje_caracteres = true;

				}

				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				// TODO Auto-generated method stub
			}
		});

		ll_botones_adjuntar = (LinearLayout) findViewById(R.id.ll_botones_adjuntar);

		tv_formulario_longitud = (TextView) findViewById(R.id.tv_formulario_longitud);
		tv_formulario_latitud = (TextView) findViewById(R.id.tv_formulario_latitud);

		// ---Spinner View Canal Respuesta---
		sCanalRespuesta = (Spinner) findViewById(R.id.tipo_canal_respuesta_view);
		ArrayList<String> medios_de_respuesta = conseguir_medios_de_respuesta();
		ArrayAdapter<String> aCanalRespuesta = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				medios_de_respuesta);
		aCanalRespuesta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sCanalRespuesta.setAdapter(aCanalRespuesta);

		sCanalRespuesta.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				nombreCanalRespuesta = sCanalRespuesta.getSelectedItem().toString();
				codigoCanalRespuesta = map_medios_respuesta.get(nombreCanalRespuesta);

				if (codigoCanalRespuesta.equals("0")) {
					tipoCanalEsValido = false;
				} else {
					errorTipoCanalView.setVisibility(View.GONE);
					tipoCanalEsValido = true;
				}

				if (codigoCanalRespuesta.equals("1")) {
					direccionRequerido = true;
				} else {
					direccionRequerido = false;
				}
				if (codigoCanalRespuesta.equals("2")) {
					eMailRequerido = true;
				} else {
					eMailRequerido = false;
				}
				if (codigoCanalRespuesta.equals("3")) {
					telefonoFijoRequerido = true;
				} else {
					telefonoFijoRequerido = false;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// ---Spinner View Solicitud---
		sSolicitud = (Spinner) findViewById(R.id.tipo_solicitud_view);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayList<String> tipos_de_solicitud = conseguir_tipos_de_solicitud();
		ArrayAdapter<String> aSolicitud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				tipos_de_solicitud);
		// Specify the layout to use when the list of choices appears
		aSolicitud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sSolicitud.setAdapter(aSolicitud);
		sSolicitud.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				nombreSolicitud = sSolicitud.getSelectedItem().toString();
				codigoSolicitud = map_tipos_solicitud.get(nombreSolicitud);

				if (codigoSolicitud.equals("0")) {
					tipoSolicitudEsValido = false;
				} else {

					errorTipoSolicitudView.setVisibility(View.GONE);
					tipoSolicitudEsValido = true;
					mostrar_subsolicitudes(arg2);
				}

				if (nombreSolicitud.trim().equalsIgnoreCase("PETICION")) {
					hechosSolicitudView.setVisibility(View.VISIBLE);
					desSolicitudView.setVisibility(View.GONE);
					text_limite_caracteres.setVisibility(View.GONE);

					hechoRequerido = true;
					pretensionesSolicitudView.setVisibility(View.VISIBLE);
				} else {
					hechosSolicitudView.setVisibility(View.GONE);
					desSolicitudView.setVisibility(View.VISIBLE);
					pretensionesSolicitudView.setVisibility(View.GONE);
					pretensionesSolicitudView.setText(null);
					hechosSolicitudView.setText(null);
					hechoRequerido = false;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		tipo_subsolicitud_view = (Spinner) findViewById(R.id.tipo_subsolicitud_view);
		if (tiene_subtipos_solicitud()) {
			tipo_subsolicitud_view.setVisibility(View.VISIBLE);
		} else {
			tipo_subsolicitud_view.setVisibility(View.GONE);
		}

		// ---Spinner View Asunto---
		sAsuntos = (Spinner) findViewById(R.id.asunto_view);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayList<String> tipos_de_asunto = conseguir_tipos_de_asunto();
		ArrayAdapter<String> aAsunto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				tipos_de_asunto);
		// Specify the layout to use when the list of choices appears
		aAsunto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sAsuntos.setAdapter(aAsunto);
		sAsuntos.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				nombreAsunto = sAsuntos.getSelectedItem().toString();
				codigoAsunto = map_tipos_asunto.get(nombreAsunto);

				if (arg2 == 0) {
					asuntoEsValido = false;
				} else {
					mostrar_subasuntos(arg2);
					errorAsuntoView.setVisibility(View.GONE);
					asuntoEsValido = true;
					nombreAsunto = sAsuntos.getSelectedItem().toString();

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		tipo_subasunto_view = (Spinner) findViewById(R.id.tipo_subasunto_view);
		if (tiene_tipos_asunto()) {
			tipo_subasunto_view.setVisibility(View.VISIBLE);
		} else {
			tipo_subasunto_view.setVisibility(View.GONE);
		}

		// ---Spinner View Identificacion---
		codigosIdentificaciones = getResources().getStringArray(R.array.codigos_identificacion);
		sIdentificaion = (Spinner) findViewById(R.id.tipo_identificacion_view);
		ArrayAdapter<CharSequence> aIdentificacion = ArrayAdapter.createFromResource(this, R.array.identificacion,
				android.R.layout.simple_spinner_item);
		aIdentificacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sIdentificaion.setAdapter(aIdentificacion);

		sIdentificaion.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int index = sIdentificaion.getSelectedItemPosition();
				nombreIdentificacion = sIdentificaion.getSelectedItem().toString();
				codigoIdentificacion = codigosIdentificaciones[index];

				setperfil(sIdentificaion, index + "");

				if (codigoIdentificacion.equals("0")) {
					tipoDocumentoEsValido = false;
				} else {
					errorTipoDocumentoView.setVisibility(View.GONE);
					tipoDocumentoEsValido = true;
				}

				if (codigoIdentificacion.equals("5")) {

					pNombreView.setText("Nombre Entidad");
				}

				if (codigoIdentificacion.equals("7")) {
					// documentoRequerido = false;
					documentoRequerido = true;// Se deja requerido hasta que el
												// webservice valido esto
				} else {
					documentoRequerido = true;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		setcampo(sIdentificaion);

		// ---Spinner View Paises---
		codigosPaises = getResources().getStringArray(R.array.codigos_paises);
		sPais = (Spinner) findViewById(R.id.paises_view);
		ArrayAdapter<CharSequence> aPais = ArrayAdapter.createFromResource(this, R.array.paises,
				android.R.layout.simple_spinner_item);
		aPais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sPais.setAdapter(aPais);

		sPais.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int index = sPais.getSelectedItemPosition();
				nombrePais = sPais.getSelectedItem().toString();
				codigoPais = codigosPaises[index];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// ---Spinner View Departamentos---
		codigosDepartamentos = getResources().getStringArray(R.array.codigos_departamentos);
		sDepartamento = (Spinner) findViewById(R.id.departamentos_view);
		ArrayAdapter<CharSequence> aDepartamento = ArrayAdapter.createFromResource(this, R.array.departamentos,
				android.R.layout.simple_spinner_item);
		aDepartamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDepartamento.setAdapter(aDepartamento);

		sDepartamento.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int index = sDepartamento.getSelectedItemPosition();
				nombreDepartamento = sDepartamento.getSelectedItem().toString();
				codigoDepartamento = codigosDepartamentos[index];
				setmunicipios(index);
				setmunicipio(index);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// ---IMEI y SIM Card ID
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		// ---get the SIM card ID---
		simID = tm.getSimSerialNumber();
		// ---get the IMEI number---
		IMEI = tm.getDeviceId();
		// ---get the phone number---
		if (tm.getLine1Number() != null) {
			telNumber = tm.getLine1Number();
		}
		nombreEntidadView.setText(nombreEntidad);

		Log.d(Propiedades.TAG, simID + " " + telNumber + " " + " " + IMEI);

		try {
			GCMRegistrar.checkDevice(FormularioCreacionActivity.this);
			GCMRegistrar.checkManifest(FormularioCreacionActivity.this);
			GCMRegistrar.register(FormularioCreacionActivity.this, "586611920514");
			IdGCM = GCMRegistrar.getRegistrationId(FormularioCreacionActivity.this);
		} catch (Exception exe) {
			Log.d(Propiedades.TAG, "GCM " + exe.toString());
		}

		ADJUNTOS_MAXIMO = conseguir_parametros_adjuntos();
		Log.d("PARAMETROS", TAMANOMAXIMO + " " + ADJUNTOS_MAXIMO);

		if (prueba) {
			ViewTreeObserver vto = pNombreView.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					set_prueba();
					pNombreView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
		}

	}

	protected void set_prueba() {
		desSolicitudView.setText(getString(R.string.prueba));
		sGrupoVulnerable.setSelection(1);
	}

	private int conseguir_parametros_adjuntos() {
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

	protected void tomar_coordenadas() {

		try {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			MyLocationListener mLocListener = new MyLocationListener(mLocManager);
			cb_formulario_coordenadas.setChecked(true);
			if (checkGPS()) {
				Toast.makeText(FormularioCreacionActivity.this,
						FormularioCreacionActivity.this.getString(R.string.espere), Toast.LENGTH_LONG).show();
				mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
				mLocListener.setMostrar(true);
			} else if (checkNetwork()) {
				mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
				mLocListener.setMostrar(true);

			} else {
				Toast.makeText(FormularioCreacionActivity.this,
						FormularioCreacionActivity.this.getString(R.string.error_wifi_revisar), Toast.LENGTH_LONG)
						.show();
				cb_formulario_coordenadas.setChecked(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean checkNetwork() {
		if (mLocManager == null) {
			mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		boolean isNetwork = mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return isNetwork;
	}

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

		public MyLocationListener(LocationManager mLocManager) {
			this.mLocManager = mLocManager;
		}

		public void setMostrar(boolean mostrar) {
			this.mostrar = mostrar;
		}

		public void onLocationChanged(Location loc) {
			if (mostrar) {
				tv_formulario_latitud.setVisibility(View.VISIBLE);
				tv_formulario_longitud.setVisibility(View.VISIBLE);

				tv_formulario_longitud.setText(
						FormularioCreacionActivity.this.getString(R.string.longitud) + ": " + loc.getLongitude() + "");
				tv_formulario_latitud.setText(
						FormularioCreacionActivity.this.getString(R.string.latitud) + ": " + loc.getLatitude() + "");

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
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, path_foto);
			startActivityForResult(cameraIntent, CAPTURE_IMAGE);
		} else {
			Toast.makeText(this, getString(R.string.revisar_si_tiene_camara), Toast.LENGTH_SHORT).show();
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

	protected void mostrar_subsolicitudes(int arg2) {
		ArrayList<String> subtipos_de_solicitud = conseguir_subtipos_de_solicitud(arg2);
		if (subtipos_de_solicitud.size() > 2) {
			tipo_subsolicitud_view.setAlpha(1);
			Toast.makeText(this, getString(R.string.seleccione_tipo_solicitud), Toast.LENGTH_SHORT).show();
			tipo_subsolicitud_view.setVisibility(View.VISIBLE);
			tipo_subsolicitud_view.setClickable(true);
			ArrayAdapter<String> aSolicitud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
					subtipos_de_solicitud);
			aSolicitud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			tipo_subsolicitud_view.setAdapter(aSolicitud);
			tipo_subsolicitud_view.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					if (arg2 != 0) {
						nombreSolicitud = arg0.getSelectedItem().toString();
						codigoSolicitud = map_subtipos_solicitud.get(arg2 + "");
					} else {
						nombreSolicitud = sSolicitud.getSelectedItem().toString();
						codigoSolicitud = map_tipos_solicitud.get(nombreSolicitud);
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		} else {
			if (tiene_subtipos_solicitud()) {
				tipo_subsolicitud_view.setAlpha((float) 0.5);
				tipo_subsolicitud_view.setVisibility(View.VISIBLE);
				tipo_subsolicitud_view.setClickable(false);
			} else {
				tipo_subsolicitud_view.setVisibility(View.GONE);
			}

		}
	}

	protected void mostrar_subasuntos(int arg2) {
		ArrayList<String> subtipos_de_asunto = conseguir_subtipos_de_asunto(arg2);
		if (subtipos_de_asunto.size() > 1) {
			Toast.makeText(this, getString(R.string.seleccione_tipo_de_asunto), Toast.LENGTH_SHORT).show();
			tipo_subasunto_view.setVisibility(View.VISIBLE);
			tipo_subasunto_view.setClickable(true);
			tipo_subasunto_view.setAlpha(1);
			ArrayAdapter<String> aSolicitud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
					subtipos_de_asunto);
			aSolicitud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			tipo_subasunto_view.setAdapter(aSolicitud);
			tipo_subasunto_view.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					if (arg2 != 0) {
						nombreAsunto = arg0.getSelectedItem().toString();
						codigoAsunto = map_subtipos_asunto.get(arg2 + "");
					} else {
						nombreAsunto = sAsuntos.getSelectedItem().toString();
						codigoAsunto = map_tipos_asunto.get(nombreAsunto);
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		} else {

			tipo_subasunto_view.setAlpha((float) 0.5);
			tipo_subasunto_view.setClickable(false);
		}
	}

	private ArrayList<String> conseguir_subtipos_de_asunto(int id_padre) {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		map_subtipos_asunto.put("0", "0");
		mHelperNames.add(getString(R.string.seleccione));
		try {
			JSONArray ja = new JSONArray(Propiedades.asuntos);
			JSONObject jo = ja.getJSONObject(id_padre - 1);
			JSONArray ja_subtipos = jo.getJSONArray("subtipos");
			for (int i = 0; i < ja_subtipos.length(); i++) {
				JSONObject jo_subtipo = ja_subtipos.getJSONObject(i);
				String subtipo = jo_subtipo.getString("nombre");
				mHelperNames.add(subtipo);
				String id = jo_subtipo.getString("id");
				map_subtipos_asunto.put((i + 1) + "", id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mHelperNames;
	}

	private void setcampo(View v) {
		if (v instanceof EditText) {
			if (v.getTag() != null
					&& sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.SI)) {
				String tag = v.getTag().toString();
				if (!sp_perfil.getString(tag, "").contentEquals("")) {
					((EditText) v).setText(sp_perfil.getString(tag, ""));
				}
			}

		}
		if (v instanceof Spinner) {
			if (v.getTag() != null
					&& sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.SI)) {
				String tag = v.getTag().toString();

				if (!sp_perfil.getString(tag, "").contentEquals("")) {
					((Spinner) v).setSelection(Integer.parseInt(sp_perfil.getString(tag, "")));
				}
			}

		}
	}

	private void setperfil(View v, String texto) {
		Log.d(texto, sp_configuracion.getString(Propiedades.GUARDAR, ""));
		if (v instanceof EditText) {
			String tag = v.getTag().toString();

			if (sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.SI)) {
				if (!texto.contentEquals(sp_perfil.getString(tag, "-1"))) {
					spe_perfil.putString(tag, texto);
					actualizar = true;
				}
			} else if (!sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.NO)) {
				if (!texto.contentEquals(sp_perfil.getString(tag, "-1"))) {
					spe_perfil.putString(tag, texto);
				}
			}
		}
		if (v instanceof Spinner) {
			String tag = v.getTag().toString();
			Log.d(Propiedades.TAG, tag + " " + texto);
			if (sp_perfil.getString(tag, "").contentEquals("")) {
				spe_perfil.putString(tag, texto);
			} else if (!texto.contentEquals(sp_perfil.getString(tag, ""))) {
				Log.d("ACTUALIZAR", "TRUE");
				actualizar = true;
				spe_perfil.putString(tag, texto);
			}
		}
	}

	private ArrayList<String> conseguir_tipos_de_asunto() {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		mHelperNames.add(getString(R.string.seleccione_tipo_de_asunto));
		map_tipos_asunto.put(getString(R.string.seleccione_tipo_de_asunto), "0");

		try {
			JSONArray ja = new JSONArray(Propiedades.asuntos);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("nombre");
				if (entidad.equalsIgnoreCase("Personal")) {
					sAsuntos.setVisibility(View.GONE);
					asuntoEsValido = true;
					nombreAsunto = "";
				}
				mHelperNames.add(entidad);
				String id = jo.getString("id");
				map_tipos_asunto.put(entidad, id);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			if (finalizar == 0) {
				Toast.makeText(this, getString(R.string.problema_internet_o_host), Toast.LENGTH_SHORT).show();
				listarsolicitudes();
				finalizar++;
			}
		}
		return mHelperNames;
	}

	private ArrayList<String> conseguir_tipos_de_solicitud() {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		mHelperNames.add(getString(R.string.seleccione_tipo_solicitud));

		map_tipos_solicitud.put(getString(R.string.seleccione_tipo_solicitud), "0");

		try {
			JSONArray ja = new JSONArray(Propiedades.tiposdesolicitudes);

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("solicitud");

				if (entidad.length() != 0) {
					mHelperNames.add(entidad);

					String id = jo.getString("id");
					map_tipos_solicitud.put(entidad, id);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();

			if (finalizar == 0) {
				Toast.makeText(this, getString(R.string.problema_internet_o_host), Toast.LENGTH_SHORT).show();
				listarsolicitudes();
				finalizar++;
			}
		}

		return mHelperNames;
	}

	private Boolean tiene_subtipos_solicitud() {
		Boolean tiene = false;
		try {
			if (Propiedades.tiposdesolicitudes != null) {
				JSONArray ja = new JSONArray(Propiedades.tiposdesolicitudes);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					JSONArray ja_subtipos = jo.getJSONArray("subtipos");
					if (ja_subtipos.length() > 0) {
						return true;
					}
				}
			} else {
				return false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return tiene;
	}

	private Boolean tiene_tipos_asunto() {
		Boolean tiene = false;
		try {
			if (Propiedades.asuntos != null) {
				JSONArray ja = new JSONArray(Propiedades.asuntos);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					String entidad = jo.getString("nombre");
					if (entidad.equalsIgnoreCase("Personal")) {
						return false;
					}
				}
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					JSONArray ja_subtipos = jo.getJSONArray("subtipos");
					if (ja_subtipos.length() == 0) {
						return true;
					}
				}
			} else {
				return false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tiene;
	}

	private ArrayList<String> conseguir_subtipos_de_solicitud(int id_padre) {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		map_subtipos_solicitud.put("0", "0");
		mHelperNames.add(getString(R.string.seleccione));
		try {
			JSONArray ja = new JSONArray(Propiedades.tiposdesolicitudes);
			JSONObject jo = ja.getJSONObject(id_padre - 1);
			JSONArray ja_subtipos = jo.getJSONArray("subtipos");
			for (int i = 0; i < ja_subtipos.length(); i++) {
				JSONObject jo_subtipo = ja_subtipos.getJSONObject(i);
				String subtipo = jo_subtipo.getString("solicitud");
				if (!subtipo.isEmpty()) {
					mHelperNames.add(subtipo);
					String id = jo_subtipo.getString("id");
					map_subtipos_solicitud.put((i + 1) + "", id);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			if (finalizar == 0) {
				Toast.makeText(this, getString(R.string.problema_internet_o_host), Toast.LENGTH_SHORT).show();
				listarsolicitudes();
				finalizar++;
			}
		}
		return mHelperNames;
	}

	private ArrayList<String> conseguir_medios_de_respuesta() {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		mHelperNames.add(getString(R.string.seleccione_medio_respuesta));
		map_medios_respuesta.put(getString(R.string.seleccione_medio_respuesta), "0");

		try {
			JSONArray ja = new JSONArray(Propiedades.mediosderespuesta);

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("medio");
				if (entidad.length() != 0) {
					mHelperNames.add(entidad);
					String id = jo.getString("id");
					map_medios_respuesta.put(entidad, id);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			if (finalizar == 0) {
				Toast.makeText(this, getString(R.string.problema_internet_o_host), Toast.LENGTH_SHORT).show();
				listarsolicitudes();
				finalizar++;
			}
		}

		return mHelperNames;
	}

	private ArrayList<String> conseguir_grupos_vulnerables() {
		ArrayList<String> mHelperNames = new ArrayList<String>();
		mHelperNames.add(getString(R.string.seleccione_grupo_vulnerable));
		map_grupos_vulnerables.put(getString(R.string.seleccione_grupo_vulnerable), "0");

		try {
			JSONArray ja = new JSONArray(Propiedades.grupos_vulnerables);

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("grupo_vulnerable");
				if (entidad.length() != 0) {
					mHelperNames.add(entidad);
					String id = jo.getString("id");

					map_grupos_vulnerables.put(entidad, id);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			if (finalizar == 0) {
				Toast.makeText(this, getString(R.string.problema_internet_o_host), Toast.LENGTH_SHORT).show();
				listarsolicitudes();
				finalizar++;
			}
		}

		return mHelperNames;
	}

	private void appStart() {
		primerNombreValido = null;
		segundoNombreValido = null;
		primerApellidoValido = null;
		segundoApellidoValido = null;
		eMailValido = null;
		telefonoValido = null;
		celularNotificacionValido = null;
		desSolValido = null;
		hechosSolicitudValido = null;

		direccionNotificacionValido = null;
		numeroIdentificacionValido = null;

		boolean desSolEsValido;
		boolean pretensionesEsValido;
		boolean hechosEsValido;

		if (desSolicitudView.getVisibility() == View.VISIBLE) {
			/*
			 * Test Descripcion
			 */
			ValidarCampo campoDesSolicitud = new ValidarCampo(this);
			camposFormulario.add(campoDesSolicitud);
			String mensajeDesSolicitud = null;
			String desSolicitud = desSolicitudView.getText().toString().trim();
			/* ! */
			// desSolicitud = "Descripción Descripción Descripción";
			/**/
			setUpValidador(desSolicitud, 20, 250, true, getString(R.string.descripcion_solicitud), TEXTOTIPO4, 4);

			desSolEsValido = campoDesSolicitud.Validar();
			pretensionesEsValido = true;
			hechosEsValido = true;

			if (desSolEsValido) {
				mensajeDesSolicitud = campoDesSolicitud.getMensaje();
				desSolicitudView.setError(null);
				desSolValido = desSolicitud;

			} else {
				mensajeDesSolicitud = campoDesSolicitud.getMensaje();
				desSolicitudView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeDesSolicitud + "</font>"));
			}
		} else {/*
				 * Test Hechos
				 */
			ValidarCampo campoHechos = new ValidarCampo(this);
			camposFormulario.add(campoHechos);
			String mensajeHechos = null;
			String hechosSolicitud = hechosSolicitudView.getText().toString().trim();
			/* ! */
			// hechosSolicitud =
			// "Esto es una prueba de todos los hechos. Enviado desde el app";
			/**/

			setUpValidador(hechosSolicitud, 0, tamano_descripcion, hechoRequerido, getString(R.string.hechos_solicitud),
					TEXTOTIPO4, 4);

			hechosEsValido = campoHechos.Validar();

			if (hechosEsValido) {
				mensajeHechos = campoHechos.getMensaje();
				hechosSolicitudView.setError(null);
				hechosSolicitudValido = hechosSolicitud;

			} else {
				mensajeHechos = campoHechos.getMensaje();
				hechosSolicitudView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeHechos + "</font>"));
			}
			/*
			 * Test Pretensiones
			 */
			ValidarCampo campoPretensiones = new ValidarCampo(this);
			camposFormulario.add(campoPretensiones);
			String mensajePretensiones = null;
			String pretensionesSolicitud = pretensionesSolicitudView.getText().toString().trim();
			/* ! */
			// hechosSolicitud =
			// "Esto es una prueba de todos los hechos. Enviado desde el app";
			/**/
			setUpValidador(pretensionesSolicitud, 0, 250, hechoRequerido, getString(R.string.pretensiones_solicitud),
					TEXTOTIPO4, 4);

			pretensionesEsValido = campoPretensiones.Validar();

			if (pretensionesEsValido) {
				mensajePretensiones = campoPretensiones.getMensaje();
				pretensionesSolicitudView.setError(null);
				pretencionesSolicitudValido = pretensionesSolicitud;

			} else {
				mensajePretensiones = campoPretensiones.getMensaje();
				pretensionesSolicitudView
						.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajePretensiones + "</font>"));
			}

			desSolEsValido = pretensionesEsValido && hechosEsValido;
			desSolValido = "HECHO:" + hechosSolicitudValido + "PRETENSION:" + pretencionesSolicitudValido;
		}

		/*
		 * Test Primer Nombre
		 */
		ValidarCampo campoPrimerNombre = new ValidarCampo(this);
		camposFormulario.add(campoPrimerNombre);
		String mensajePrimerNombre = null;
		String primerNombre = pNombreView.getText().toString().trim();
		/* ! */
		// primerNombre = "Nombre";
		/**/

		setUpValidador(primerNombre, 3, 20, true, getString(R.string.primer_nombre), TEXTOTIPO1, 1);

		boolean pNEsValido = campoPrimerNombre.Validar();

		if (pNEsValido) {
			mensajePrimerNombre = campoPrimerNombre.getMensaje();
			pNombreView.setError(null);
			primerNombreValido = primerNombre;
			setperfil(pNombreView, primerNombreValido);

		} else {
			mensajePrimerNombre = campoPrimerNombre.getMensaje();
			pNombreView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajePrimerNombre + "</font>"));
		}

		// primerNombreValido = "";

		/*
		 * Test Segundo Nombre
		 */
		ValidarCampo campoSegundoNombre = new ValidarCampo(this);
		camposFormulario.add(campoSegundoNombre);
		String mensajeSegundoNombre = null;
		String segundoNombre = sNombreView.getText().toString().trim();
		/* ! */
		// segundoNombre = "Segundo Nombre";
		/**/

		setUpValidador(segundoNombre, 3, 30, false, getString(R.string.segundo_nombre), TEXTOTIPO2, 2);

		boolean sNEsValido = campoSegundoNombre.Validar();

		if (sNEsValido) {
			mensajeSegundoNombre = campoSegundoNombre.getMensaje();
			sNombreView.setError(null);
			segundoNombreValido = segundoNombre;
			setperfil(sNombreView, segundoNombreValido);

		} else {
			mensajeSegundoNombre = campoSegundoNombre.getMensaje();
			sNombreView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeSegundoNombre + "</font>"));
		}

		/*
		 * Test Primer Apellido
		 */
		ValidarCampo campoPrimerApellido = new ValidarCampo(this);
		camposFormulario.add(campoPrimerApellido);
		String mensajePrimerApellido = null;
		String primerApellido = pApellidoView.getText().toString().trim();
		/* ! */
		// primerApellido = "Apellido";
		/**/
		setUpValidador(primerApellido, 3, 30, true, getString(R.string.primer_apellido), TEXTOTIPO2, 2);

		boolean pAEsValido = campoPrimerApellido.Validar();

		if (pAEsValido) {
			mensajePrimerApellido = campoPrimerApellido.getMensaje();
			pApellidoView.setError(null);
			primerApellidoValido = primerApellido;
			setperfil(pApellidoView, primerApellidoValido);

		} else {
			mensajePrimerApellido = campoPrimerApellido.getMensaje();
			pApellidoView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajePrimerApellido + "</font>"));
		}

		/*
		 * Test Segundo Apellido
		 */
		ValidarCampo campoSegundoApellido = new ValidarCampo(this);
		camposFormulario.add(campoSegundoApellido);
		String mensajeSegundoApellido = null;
		String segundoApellido = sApellidoView.getText().toString().trim();
		/* ! */
		// segundoApellido = "Segundo Apellido";
		/**/
		setUpValidador(segundoApellido, 3, 30, false, getString(R.string.segundo_apellido), TEXTOTIPO2, 2);

		boolean sAEsValido = campoSegundoApellido.Validar();

		if (sAEsValido) {
			mensajeSegundoApellido = campoSegundoApellido.getMensaje();
			sApellidoView.setError(null);
			segundoApellidoValido = segundoApellido;
			setperfil(sApellidoView, segundoApellidoValido);

		} else {
			mensajeSegundoApellido = campoSegundoApellido.getMensaje();
			sApellidoView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeSegundoApellido + "</font>"));
		}

		/*
		 * Test Direccion
		 */
		ValidarCampo campoDireccion = new ValidarCampo(this);
		camposFormulario.add(campoDireccion);
		String mensajeDireccion = null;
		String direccion = direccionView.getText().toString().trim();

		/* ! */
		// direccion = "Calle 123 A # 45 - 67";
		/**/

		setUpValidador(direccion, 3, 250, direccionRequerido, getString(R.string.direccion), TEXTOTIPO4, 4);
		boolean dirEsValido = campoDireccion.Validar();
		if (dirEsValido) {
			mensajeDireccion = campoDireccion.getMensaje();
			direccionView.setError(null);
			direccionNotificacionValido = direccion;
			setperfil(direccionView, direccionNotificacionValido);
		} else {
			mensajeDireccion = campoDireccion.getMensaje();
			direccionView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeDireccion + "</font>"));
		}

		/*
		 * Test e-mail
		 */
		ValidarCampo campoEMail = new ValidarCampo(this);
		camposFormulario.add(campoEMail);
		String mensajeEMail = null;
		String eMail = eMailView.getText().toString().trim();
		/* ! */
		// eMail = "email@email.com";
		/**/
		eMailRequerido = true;
		setUpValidador(eMail, 0, 0, eMailRequerido, getString(R.string.e_mail), TEXTOTIPO3, 3);

		boolean eMEsValido = campoEMail.Validar();

		if (eMEsValido) {
			mensajeEMail = campoEMail.getMensaje();
			eMailView.setError(null);
			eMailValido = eMail;
			setperfil(eMailView, eMailValido);

		} else {
			mensajeEMail = campoEMail.getMensaje();
			eMailView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeEMail + "</font>"));
		}

		/*
		 * Test Telefono
		 */
		ValidarCampo campoTelefono = new ValidarCampo(this);
		camposFormulario.add(campoTelefono);
		String mensajeTelefono = null;
		String telefono = telefonoView.getText().toString().trim();
		/* ! */
		// telefono = "2222222";
		/**/
		setUpValidador(telefono, 0, 20, telefonoFijoRequerido, getString(R.string.telefono), TEXTOTIPO6, 6);

		boolean telEsValido = campoTelefono.Validar();

		if (telEsValido) {
			mensajeTelefono = campoTelefono.getMensaje();
			telefonoView.setError(null);
			telefonoValido = telefono;
			setperfil(telefonoView, telefonoValido);

		} else {
			mensajeTelefono = campoTelefono.getMensaje();
			telefonoView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeTelefono + "</font>"));
		}

		/*
		 * Test Celular
		 */
		ValidarCampo campoCelular = new ValidarCampo(this);
		camposFormulario.add(campoCelular);
		String mensajeCelular = null;
		String celular = celularView.getText().toString().trim();
		/* ! */
		// celular = "3012554545";
		/**/
		setUpValidador(celular, 0, 20, true, getString(R.string.celular), TEXTOTIPO6, 6);

		boolean celEsValido = campoCelular.Validar();

		if (celEsValido) {
			mensajeCelular = campoCelular.getMensaje();
			celularView.setError(null);
			celularNotificacionValido = celular;
			setperfil(celularView, celularNotificacionValido);

		} else {
			mensajeCelular = campoCelular.getMensaje();
			celularView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeCelular + "</font>"));
		}

		/*
		 * Test Identificacion
		 */
		ValidarCampo campoIdentificaion = new ValidarCampo(this);
		camposFormulario.add(campoIdentificaion);
		String mensajeIdentificacion = null;
		String identificacion = identificacionView.getText().toString().trim();

		/* ! */
		// identificacion = "123456789";
		/**/

		setUpValidador(identificacion, 3, 30, documentoRequerido, getString(R.string.documento), TEXTOTIPO5, 5);
		boolean idEsValido = campoIdentificaion.Validar();
		if (idEsValido) {
			mensajeIdentificacion = campoIdentificaion.getMensaje();
			identificacionView.setError(null);
			numeroIdentificacionValido = identificacion;
			setperfil(identificacionView, numeroIdentificacionValido);

		} else {
			mensajeIdentificacion = campoIdentificaion.getMensaje();
			Log.d(Propiedades.TAG, mensajeIdentificacion);
			identificacionView.setError(Html.fromHtml("<font color='#FFFFFF'>" + mensajeIdentificacion + "</font>"));
		}

		/*
		 * Test Tipo Solicitud
		 */
		if (tipoSolicitudEsValido) {
			errorTipoSolicitudView.setVisibility(View.GONE);
			errorTipoSolicitudView.setError(null);
		} else {
			errorTipoSolicitudView.setVisibility(View.VISIBLE);
			errorTipoSolicitudView.setError("");
		}

		/*
		 * Test Tipo Identificacion
		 */
		if (tipoDocumentoEsValido) {
			errorTipoDocumentoView.setVisibility(View.GONE);
			errorTipoDocumentoView.setError(null);
		} else {
			errorTipoDocumentoView.setVisibility(View.VISIBLE);
			errorTipoDocumentoView.setError("");
		}
		/*
		 * Test Tipo Canal Respuesta
		 */
		if (tipoCanalEsValido) {
			errorTipoCanalView.setVisibility(View.GONE);
			errorTipoCanalView.setError(null);
		} else {
			errorTipoCanalView.setVisibility(View.VISIBLE);
			errorTipoCanalView.setError("");
		}
		/*
		 * Test Grupo Vulnerable
		 */
		if (grupoVulnerableEsValido) {
			errorGrupoVulnerableView.setVisibility(View.GONE);
			errorGrupoVulnerableView.setError(null);
		} else {
			errorGrupoVulnerableView.setVisibility(View.VISIBLE);
			errorGrupoVulnerableView.setError("");
		}

		adjunto = "";
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

		/*
		 * Test Asunto
		 */
		if (asuntoEsValido) {
			errorAsuntoView.setVisibility(View.GONE);
			errorAsuntoView.setError(null);
		} else {
			errorAsuntoView.setVisibility(View.VISIBLE);
			errorAsuntoView.setError("");
		}

		try {
			IdGCM = GCMRegistrar.getRegistrationId(FormularioCreacionActivity.this);
		} catch (Exception exe) {
			IdGCM = "";
			Toast.makeText(this, getString(R.string.no_se_puede_notificar), Toast.LENGTH_SHORT).show();
		}

		Log.d("PQRappv2",
				pNEsValido + "," + sNEsValido + "," + pAEsValido + "," + sAEsValido + "," + eMEsValido + ","
						+ telEsValido + "," + celEsValido + "," + desSolEsValido + "," + hechosEsValido + ","
						+ dirEsValido + "," + idEsValido + "," + tipoSolicitudEsValido + "," + tipoDocumentoEsValido
						+ "," + tipoCanalEsValido + "," + grupoVulnerableEsValido + "," + asuntoEsValido);

		if (pNEsValido && sNEsValido && pAEsValido && sAEsValido && eMEsValido && telEsValido && celEsValido
				&& desSolEsValido && hechosEsValido && dirEsValido && idEsValido && tipoSolicitudEsValido
				&& tipoDocumentoEsValido && tipoCanalEsValido && grupoVulnerableEsValido && pretensionesEsValido
				&& asuntoEsValido) {
			/* Codigo de seguimiento */
			randomNum = getCadenaAlfanumAleatoria(8);

			// Create instance for AsyncCallWS
			task = new AsyncCallWS(this);

			task.setEntidad(Propiedades.entidad);
			task.setPrimernombre(primerNombreValido);
			task.setSegundonombre(segundoNombreValido);
			task.setPrimerapellido(primerApellidoValido);
			task.setSegundoapellido(segundoApellidoValido);
			task.setEmail(eMailValido);
			task.setTipo_solicitud(codigoSolicitud);
			task.setDescripcion(desSolValido);
			task.setCodigo(randomNum);
			task.setWebservice(Propiedades.webservice);
			task.setIddato("");
			task.setEstado(Propiedades.estado);
			task.setVulnerable(codigoGrupoVulnerable);
			task.setTipo_documento(codigoIdentificacion);
			task.setDocumento(numeroIdentificacionValido);
			task.setDepartamento(codigoDepartamento);
			task.setMunicipio(codigoMunicipio);
			task.setDireccion(direccionNotificacionValido);
			task.setTelefono(telefonoValido);
			task.setCelular(celularNotificacionValido);
			task.setAsunto(nombreAsunto);
			task.setAdjunto(adjunto);
			task.setMedio_recepcion(Propiedades.medio_recepcion);
			task.setMedio_respuesta(codigoCanalRespuesta);

			task.setAnonimo(anonimo);

			task.setSimcard(simID);
			task.setImei(IMEI);
			task.setHecho("");
			task.setNombre_tipo_identificacion(nombreIdentificacion);
			task.setNombre_tipo_solicitud(nombreSolicitud);
			task.setNombre_vulnerable(nombreGrupoVulnerable);
			task.setNombre_departamento(nombreDepartamento);
			task.setNombre_municipio(nombreMunicipio);
			task.setCodigo_pais(codigoPais);
			task.setNombre_pais(nombrePais);

			task.setCodigoAsuntoSolicitud(codigoAsunto);

			task.setCodigoTipoCanalSolicitud(Propiedades.tipocanalsolicitud);
			task.setCodigoTipoCanalRespuesta(codigoCanalRespuesta);
			task.setCodigoEntidad(Propiedades.codigo_entidad);
			task.setSiglaEntidad(Propiedades.sigla_entidad);
			task.setNumeroTelefonicoDispositivo(telNumber);

			if (!(tv_formulario_latitud.getText().toString().contentEquals(getString(R.string.lat)))
					&& !(tv_formulario_longitud.getText().toString().contentEquals(getString(R.string.lon)))) {
				task.setLatitud(tv_formulario_latitud.getText().toString().split(":")[1]);
				task.setLongitud(tv_formulario_longitud.getText().toString().split(":")[1]);
			}

			task.setIdGCM(IdGCM);
			// Call execute
			verificarguardadodatos();

		} else {
			Toast.makeText(this, getString(R.string.mensaje_revise_datos), Toast.LENGTH_SHORT).show();
		}

	}

	private void verificarguardadodatos() {
		if (sp_configuracion.getString(Propiedades.PREGUNTAR, "").contentEquals(Propiedades.NO)) {
			if (sp_configuracion.getString(Propiedades.GUARDAR, "").contentEquals(Propiedades.SI)) {
				if (actualizar) {
					guardardatos();
					task.execute();
				} else {
					task.execute();
				}
			} else {
				task.execute();
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
				task.execute();
				dialogo1.dismiss();

			}
		});
		dialogo1.setNeutralButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogo1, int id) {
				if (cb_guardar_datos.isChecked()) {
					nopreguntarguardado();
				}
				borrardatos();
				task.execute();
				dialogo1.dismiss();
			}
		});

		dialogo1.show();
	}

	protected void preguntarguardado() {
		spe_configuracion.putString(Propiedades.PREGUNTAR, Propiedades.SI);
		spe_configuracion.commit();
	}

	protected void nopreguntarguardado() {
		spe_configuracion.putString(Propiedades.PREGUNTAR, Propiedades.NO);
		spe_configuracion.commit();
	}

	protected void guardardatos() {
		Log.d("GUARDAR DATOS", spe_perfil.toString());

		spe_perfil.commit();
		activarprecargado();
	}

	protected void desactivarprecargado() {
		borrardatos();
		spe_configuracion.putString(Propiedades.GUARDAR, Propiedades.NO);
		spe_configuracion.commit();
	}

	protected void activarprecargado() {
		Log.d("PRECARGAR", spe_configuracion.toString());
		spe_configuracion.putString(Propiedades.GUARDAR, Propiedades.SI);
		spe_configuracion.commit();
	}

	private void borrardatos() {
		spe_perfil.clear();
		spe_perfil.commit();
		spe_perfil.clear();
		spe_perfil.commit();
	}

	private void reiniciar() {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	// setUpValidador(Contenido del Campo, min char, max char, Es Requerido,
	// Nombre del Campo, Pattern, tipo#);
	// Tipo 1: Texto sin espacios
	// Tipo 2: Texto con espacios
	// Tipo 3: email
	// Tipo 4: texto con espacios con caracteres especiales
	// Tipo 5: texto en mayúsculas o números
	private void setUpValidador(String contenidoCampo, Integer minChar, Integer maxChar, Boolean requerido,
			String nombreCampo, String pattern, Integer tipo) {
		for (ValidarCampo campoToSet : camposFormulario) {
			campoToSet.setContenidoCampo(contenidoCampo);
			campoToSet.setMinChar(minChar);
			campoToSet.setMaxChar(maxChar);
			campoToSet.setRequerido(requerido);
			campoToSet.setNombre(nombreCampo);
			campoToSet.setPattern(pattern);
			campoToSet.setTipo(tipo);
		}
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

	public void onRefresh(View view) {
		appStart();
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
		ll_file = (LinearLayout) findViewById(R.id.file_path_ll);
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
						ll_file.addView(archivo);
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

				if (f.length() > TAMANOMAXIMO) {
					Toast.makeText(this, getString(R.string.archivo_muy_grande) + " " + STRTAMANOMAXIMO,
							Toast.LENGTH_SHORT).show();
				} else {
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
				}
			}
		}

		revisarCantidadAdjuntos(false);
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
	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
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
						Intent intent = new Intent(FormularioCreacionActivity.this, SeleccionarEntidad.class);
						startActivity(intent);

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
				divider.setBackground(getDrawable(R.color.aplicacion_oscuro));
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

	protected void setmunicipios(int index) {
		codigosMunicipios = Constantes.getMunicipios(this, index);

	}

	protected void setmunicipio(int index) {
		sMunicipio = (Spinner) findViewById(R.id.municipios_view);

		ArrayAdapter<CharSequence> aMunicipio;
		aMunicipio = Constantes.getMunicipio(this, index);

		aMunicipio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sMunicipio.setAdapter(aMunicipio);

		sMunicipio.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int index = sMunicipio.getSelectedItemPosition();
				nombreMunicipio = sMunicipio.getSelectedItem().toString();
				codigoMunicipio = codigosMunicipios[index];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

}
