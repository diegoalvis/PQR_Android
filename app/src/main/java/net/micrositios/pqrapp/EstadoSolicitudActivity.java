package net.micrositios.pqrapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.micrositios.pqrapp.encuesta.EncuestaActivity;
import net.micrositios.pqrapp.encuesta.PruebaInternet;
import net.micrositios.pqrapp.encuesta.WS_Rest_client;

@SuppressLint({ "InlinedApi", "NewApi" })
public class EstadoSolicitudActivity extends Activity {

	// private CodigoListDatabaseHelper databaseHelper;
	SolicitudesListActivity solicitud;

	String codigo = "", fechaCreacion = "", fechaModificacion = "", estado = "", codigoEntidad = "", nombreEntidad = "",
			tipoSolicitud = "", descripcion = "", hechos = "", respuesta = "";
	Bundle bundle;
	TextView entidadView, fechaView, codigoNombreView, codigoView, tipoSolicitudView, descripcionView, hechosView,
			estadoView, respuestaView;

	private LinearLayout ll_estadosolicitud_adjuntos_usuario;
	private LinearLayout ll_estadosolicitud_adjuntos_entidad;

	private TextView fechamodificacionView;

	private int SIZE;

	Activity act;
	private ImageView logo;
	Drawable d_cambio;
	boolean imagen_drawable = true;
	Button encuesta;
	Context con;
	ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estado_solicitud);
		this.act = this;
		con = this;
		getSize();

		entidadView = (TextView) findViewById(R.id.entidad_view);

		fechaView = (TextView) findViewById(R.id.fecha_view);
		fechamodificacionView = (TextView) findViewById(R.id.fecha_hora_modificacion_view);
		// codigoNombreView = (TextView) findViewById(R.id.codigo_nombre_view);

		tipoSolicitudView = (TextView) findViewById(R.id.tipo_solicitud_valor_view);
		descripcionView = (TextView) findViewById(R.id.descripcion_valor_view);
		hechosView = (TextView) findViewById(R.id.hechos_valor_view);
		estadoView = (TextView) findViewById(R.id.estado_valor_view);
		respuestaView = (TextView) findViewById(R.id.respuesta_valor_view);
		ll_estadosolicitud_adjuntos_usuario = (LinearLayout) findViewById(R.id.ll_estadosolicitud_adjuntos_usuario);

		encuesta = (Button) findViewById(R.id.Button_encuesta);
		encuesta.setVisibility(View.INVISIBLE);

		for (int j = 0; j < Propiedades.estilos_entidad.size(); j++) {

			if (Propiedades.estilos_entidad.get(j).getId_entidad().equals(Propiedades.codigo_entidad)) {

				Propiedades.estilo_entidad = Propiedades.estilos_entidad.get(j);

				break;

			}

		}

		try {

			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {

				try {

					codigo = bundle.getString("codigo");
					Propiedades.codigo_consulta = codigo;
					codigoView = (TextView) findViewById(R.id.codigo_valor_view);

					if (codigo.length() != 0) {
						codigoView.setVisibility(View.VISIBLE);
						codigoView.setText(codigo);
						Propiedades.codigo_consulta = codigo;
					} else {
						codigoView.setText(null);
						codigoView.setVisibility(View.GONE);
					}

				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud codigo" + e.toString());
				}

				try {

					nombreEntidad = bundle.getString("entidad");

					if (nombreEntidad.length() != 0) {
						seleccionar_entidad(nombreEntidad);
						entidadView.setVisibility(View.VISIBLE);
						entidadView.setText(nombreEntidad);
					} else {
						entidadView.setText(null);
						entidadView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					nombreEntidad = "";

					Log.e("pqr", "Error estdo solicitud nombreEntidad" + e.toString());
				}

				try {

					fechaCreacion = bundle.getString("fechaCreacion");
					if (fechaCreacion.length() != 0) {
						fechaView.setText(fechaCreacion);
						fechaView.setVisibility(View.VISIBLE);
					} else {
						fechaView.setText(null);
						fechaView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud fechaCreacion" + e.toString());
				}

				try {

					fechaModificacion = bundle.getString("fechaModificacion");
					if (fechaCreacion.length() != 0) {
						fechamodificacionView
								.setText(getString(R.string.etiqueta_fecha_actualizacion) + " : " + fechaModificacion);
						fechamodificacionView.setVisibility(View.VISIBLE);
					} else {
						fechamodificacionView.setText(null);
						fechamodificacionView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud fechaModificacion" + e.toString());
				}

				try {

					tipoSolicitud = bundle.getString("tipoSolicitud");
					if (tipoSolicitud.length() != 0) {
						tipoSolicitudView.setVisibility(View.VISIBLE);
						tipoSolicitudView.setText(Html.fromHtml(tipoSolicitud));
					} else {
						tipoSolicitudView.setText(null);
						tipoSolicitudView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud tipoSolicitud" + e.toString());
				}

				try {

					estado = bundle.getString("estado");
					if (estado.length() != 0) {

						Log.i("PQR", "respuesta encuesta" + respuesta);

						if (!Propiedades.encuesta_enviada) {
							estadoView.setVisibility(View.VISIBLE);
							estadoView.setText(getString(R.string.etiqueta_estado) + " : " + estado);

							Log.i("pqr", "estado: " + estado);

							if ((estado.equalsIgnoreCase("cerrado") || estado.equalsIgnoreCase("Respuesta consultada"))
									&& Propiedades.encuesta) {

								encuesta.setVisibility(View.VISIBLE);

								try {
									GradientDrawable shape_defauld = new GradientDrawable();
									shape_defauld.setShape(GradientDrawable.RECTANGLE);
									shape_defauld.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
									shape_defauld
											.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton()));
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

									shape_select.setColor(
											Color.parseColor(Propiedades.estilo_entidad.getColor_boton_select()));
									shape_select.setStroke(0, Color.WHITE);

									Drawable[] drawarrayselect = { shape_select, textura };
									LayerDrawable layerdrawableselect = new LayerDrawable(drawarrayselect);

									StateListDrawable states = new StateListDrawable();
									states.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
									states.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
									states.addState(new int[] {}, layerdrawable);

									encuesta.setBackground(states);
								} catch (Exception e) {
									// TODO: handle exception
								}

								encuesta.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub

										dialogo_progress();

										PruebaInternet p_int = new PruebaInternet(con);

										if (p_int.estadoConexion()) {
											WS_Rest_client ws = new WS_Rest_client(con,
													WS_Rest_client.Consultar_encuesta);

											Handler puente_envio = new Handler() {
												@Override
												public void handleMessage(Message msg) {
													if (msg.what == 1) {
														dialog.cancel();
														Intent intent = new Intent(EstadoSolicitudActivity.this,
																EncuestaActivity.class);
														startActivityForResult(intent, 101);

													} else if (msg.what == 0) {
														dialog.cancel();
													}
												}
											};
											ws.setPuente_envio(puente_envio);
											ws.Consultar_formulario("1");
											ws.execute();
										}

									}
								});

							}
						}
					} else {

						estadoView.setText(null);

						estadoView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud estado" + e.toString());
				}

				try {

					descripcion = bundle.getString("descripcion");
					if (descripcion.length() != 0) {

						descripcionView.setVisibility(View.VISIBLE);
						descripcionView
								.setText(getString(R.string.etiqueta_descripcion) + ": " + Html.fromHtml(descripcion));

					} else {

						descripcionView.setText(null);

						descripcionView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud descripcion" + e.toString());
				}

				try {

					hechos = bundle.getString("hechos");
					if (hechos.length() != 0) {

						hechosView.setVisibility(View.VISIBLE);
						hechosView.setText(getString(R.string.etiqueta_hechos) + " : " + hechos);

					} else {

						hechosView.setText(null);

						hechosView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error estdo solicitud hechos" + e.toString());
				}

				try {
					JSONObject jo_adjuntos = new JSONObject(bundle.getString("adjunto").trim());

					JSONArray ja_usuario = jo_adjuntos.getJSONArray("Usuario");

					if (ja_usuario.length() > 0) {

						for (int i = 0; i < ja_usuario.length(); i++) {

							JSONObject jo_adjunto = ja_usuario.getJSONObject(i);
							final String nombre = jo_adjunto.getString("documento_nombre");
							final String ruta = jo_adjunto.getString("ruta").contains("http")
									? jo_adjunto.getString("ruta") : "http://" + jo_adjunto.getString("ruta");

							Button btn = new Button(this);
							btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
							btn.setText(nombre);
							btn.setTextSize(14);
							btn.setBackgroundResource(R.drawable.custom_btn_genoa);
							LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT, 0);
							llp.setMargins(0, 5, 0, 5);

							btn.setLayoutParams(llp);

							btn.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									openpdf(ruta, v);
								}
							});
							Log.d(Propiedades.TAG, nombre);
							// ll_estadosolicitud_adjuntos.addView(tv);
							ll_estadosolicitud_adjuntos_usuario.addView(btn);
						}
					} else {
						TextView tv_documentos_usuario = (TextView) findViewById(R.id.tv_documentos_usuario);
						tv_documentos_usuario.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					Log.d("MICROSITIOS ", e.toString());

				}

				try {

					respuesta = bundle.getString("respuesta");

					LinearLayout ll_estado_solicitud_respuesta = (LinearLayout) findViewById(
							R.id.ll_estado_solicitud_respuesta);

					if (respuesta.length() != 0 && !respuesta.trim().contentEquals("null")) {
						ll_estado_solicitud_respuesta.setVisibility(View.VISIBLE);

						Log.d("RESPEUSTA ", respuesta.toString());

						respuestaView.setVisibility(View.VISIBLE);
						respuestaView.setText(getString(R.string.etiqueta_respuesta) + ": " + respuesta);

						try {
							JSONObject jo_adjuntos = new JSONObject(bundle.getString("adjunto").trim());

							JSONArray ja_usuario = jo_adjuntos.getJSONArray("Respuesta");

							Log.d("AAAdjuntos", ja_usuario.toString());

							if (ja_usuario.length() > 0) {
								ll_estadosolicitud_adjuntos_entidad = (LinearLayout) findViewById(
										R.id.ll_estadosolicitud_adjuntos_entidad);
								TextView tv_documentos_entidad = (TextView) findViewById(R.id.tv_documentos_entidad);
								for (int i = 0; i < ja_usuario.length(); i++) {
									JSONObject jo_adjunto = ja_usuario.getJSONObject(i);
									final String nombre = jo_adjunto.getString("documento_nombre");
									final String ruta = jo_adjunto.getString("ruta");

									Button btn = new Button(this);

									btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
									btn.setText(nombre);
									btn.setTextSize(14);
									btn.setBackgroundResource(R.drawable.custom_btn_genoa);
									LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0);
									llp.setMargins(0, 5, 0, 5);

									btn.setLayoutParams(llp);

									btn.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											openpdf(ruta, v);
										}
									});
									Log.d(Propiedades.TAG, nombre);
									tv_documentos_entidad.setVisibility(View.VISIBLE);
									ll_estadosolicitud_adjuntos_entidad.addView(btn);
									ll_estadosolicitud_adjuntos_entidad.setVisibility(View.VISIBLE);
								}

							} else {
								TextView tv_documentos_entidad = (TextView) findViewById(R.id.tv_documentos_entidad);
								tv_documentos_entidad.setVisibility(View.GONE);

							}
						} catch (Exception e) {
							Log.d("MICROSITIOS_adjunto_entidad", e.toString());

						}

					} else {
						respuestaView.setText(null);
						ll_estado_solicitud_respuesta.setVisibility(View.GONE);
						respuestaView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "Error respuesta" + e.toString());
				}

				try {

					logo = (ImageView) findViewById(R.id.imageView_logo_estado);

					Log.i("pqr", "nombre de la entidad" + nombreEntidad);

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

					entidadView.setTextColor(Color.parseColor(Propiedades.estilo_entidad.getColor_texto_seccion()));
					tipoSolicitudView
							.setTextColor(Color.parseColor(Propiedades.estilo_entidad.getColor_texto_seccion()));

				} catch (Exception e) {
					// TODO: handle exception
					Log.e("pqr", "error logo" + e.toString());
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("pqr", "Error estdo consulta " + e.toString());
		}

	}

	public void dialogo_progress() {

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 101) {
			if (resultCode == RESULT_OK) {
				encuesta.setVisibility(View.GONE);

			}

		}

	}

	private String getentidades() {
		String json;
		StringBuilder contents = new StringBuilder();
		String PATH = this.getFilesDir() + File.separator;
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

	protected void seleccionar_entidad(String nombreEntidad) {
		try {
			JSONArray ja = new JSONArray(getentidades());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String entidad = jo.getString("entidad");
				if (entidad.contentEquals(nombreEntidad)) {
					// Propiedades.entidad = entidad;
					// Propiedades.webservice = jo.getString("webservice");
					// Propiedades.codigo_entidad =
					// jo.getString("codigo_entidad");
					// Propiedades.sigla_entidad =
					// jo.getString("sigla_entidad");
					Propiedades.pagina = jo.getString("pagina");
					// Propiedades.correo = jo.getString("correo");
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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (imagen_drawable) {
			try {

				AlphaAnimation alpha = new AlphaAnimation(1F, 0.2F); // change
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

						// Bitmap bitmap = ((BitmapDrawable)
						// d_cambio).getBitmap();
						// Bitmap bitmap_gray = toGrayscale(bitmap);

						// LightingColorFilter ColorFilter = new
						// LightingColorFilter(
						// color.red, 0xFFB2B2B2);
						// // BitmapDrawable b = new
						// BitmapDrawable(bitmap_gray);
						// //
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
		}
		// logo.setAlpha(0.1F);

	}

	// public Bitmap toGrayscale(Bitmap bmpOriginal) {
	// int width, height;
	// height = bmpOriginal.getHeight();
	// width = bmpOriginal.getWidth();
	//
	// Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
	// Bitmap.Config.RGB_565);
	// Canvas c = new Canvas(bmpGrayscale);
	// Paint paint = new Paint();
	// ColorMatrix cm = new ColorMatrix();
	// cm.setSaturation(0);
	// ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	// paint.setColorFilter(f);
	// c.drawBitmap(bmpOriginal, 0, 0, paint);
	// return bmpGrayscale;
	// }

	protected void openpdf(String adjunto, View v) {
		Button progressButton = (Button) v;
		Log.d(Propiedades.TAG, adjunto);
		Archivos arc = new Archivos();
		String[] adj = { adjunto };
		arc.setmButton(progressButton);
		arc.setAct(this);
		arc.execute(adj);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		listarsolicitudes();
	}

	private void listarsolicitudes() {
		Intent consultarIntent = new Intent(this, SolicitudesListActivity.class);
		startActivity(consultarIntent);
		finish();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private int getSize() {
		int Measuredwidth = 0;
		int Measuredheight = 0;
		Point size = new Point();
		WindowManager w = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);

			Measuredwidth = size.x;
			Measuredheight = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			Measuredwidth = d.getWidth();
			Measuredheight = d.getHeight();
		}

		float densidad = this.getResources().getDisplayMetrics().density;

		Log.d("DENSIDAD", densidad + " Width " + Measuredwidth + " Height:" + Measuredheight);

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
		return SIZE;
	}

}
