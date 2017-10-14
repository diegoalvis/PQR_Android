package net.micrositios.pqrapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RefreshFromFeedConsulta extends Activity {

	private final Context myContext;
	private ProgressDialog dialog;
	private JSONHandler jsonHandler;
	Handler handler;
	private static String URL_RSS = null;
	Intent intent;
	Bundle extras;
	ListView listView;

	private boolean esConsultaManual = false;

	public RefreshFromFeedConsulta(Context context) {
		myContext = context;
		handler = new Handler();
	}

	public void setUrlRss(String urlRss) {
		URL_RSS = urlRss;
	}

	public void setConsultaManual() {
		esConsultaManual = true;
	}

	@SuppressLint("NewApi")
	public void refreshFromFeed() {
		dialog = ProgressDialog.show(myContext, myContext.getString(R.string.cargando),
				myContext.getString(R.string.progress_dialog_procesando));
		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(myContext.getResources().getColor(R.color.aplicacion));
		} else {
			try {
				divider.setBackground(myContext.getResources().getDrawable(R.drawable.fondoactionbar));

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(myContext.getResources().getColor(R.color.aplicacion));

		Thread th = new Thread() {
			// @Override
			@Override
			public void run() {
				if (jsonHandler == null) {
					jsonHandler = new JSONHandler();
				}

				try {

					jsonHandler.processResponse(myContext, URL_RSS);

					Log.d("URL_RSS", URL_RSS);

					handler.post(new Runnable() {

						// @Override
						@Override
						public void run() {
							try {
								Log.d(Propiedades.TAG + " refreshFromFeed",
										Propiedades.codigo_entidad + " " + jsonHandler.getCodigo());

								if (jsonHandler.getValido()) {
									resetDisplay(jsonHandler.getFecha(), jsonHandler.getCodigo(),
											jsonHandler.getFecha_modificacion(), jsonHandler.getEstado(),
											jsonHandler.getNombre_tipo_solicitud(),
											jsonHandler.getCodigoTipoCanalSolicitud(), jsonHandler.getDescripcion(),
											jsonHandler.getRespuesta(), jsonHandler.getCod_error(),
											jsonHandler.getMensaje_error(), jsonHandler.getAsunto(),
											jsonHandler.getAdjunto());

								} else {
									mostrarerroractualizacion();
								}
							} catch (Exception e) {
								e.printStackTrace();
								Log.d(Propiedades.TAG + " refreshFromFeed", e.toString());
							}

							dialog.dismiss();

						}

					});

				} catch (Exception e) {
					Log.d(Propiedades.TAG + " refreshFromFeed", e.toString());
					e.printStackTrace();
					handler.post(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();

							mostrarerrorwifi();

						}

					});
				}

			}
		};
		th.start();

	}

	private void mostrarerroractualizacion() {
		Toast t = Toast.makeText(myContext, myContext.getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();

	}

	private void mostrarerrorwifi() {
		Toast t = Toast.makeText(myContext, myContext.getString(R.string.error_wifi), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();

		t = Toast.makeText(myContext, myContext.getString(R.string.error_wifi_revisar), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();

	}

	private void resetDisplay(final String fecha, final String codigo, final String fecha_modificacion,
			final String estado, final String nombre_tipo_solicitud, final String codigoTipoCanalSolicitud,
			final String descripcion, final String respuesta, final String cod_error, final String mensaje_error,
			final String asunto, final String adjunto) {
		if (!cod_error.contentEquals("0")) {
			Toast t = Toast.makeText(myContext, mensaje_error, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();

			// listarsolicitudes(codigo);

		} else {

			if (esConsultaManual) {
				Thread th = new Thread() {
					// @Override
					@Override
					public void run() {
						Log.d("MICROSITIOS_SIN RESPUESTA", respuesta.toString());

						addSolicitud(descripcion, codigo, estado, nombre_tipo_solicitud, codigoTipoCanalSolicitud,
								fecha, fecha_modificacion, asunto, respuesta, adjunto);
					}

				};
				th.start();

			} else {
				Thread th = new Thread() {
					@Override
					public void run() {

						updateSolicitud(descripcion, codigo, estado, nombre_tipo_solicitud, codigoTipoCanalSolicitud,
								fecha, fecha_modificacion, asunto, respuesta, adjunto);

					}
				};
				th.start();
			}
		}
	}

	/*
	 * private void listarsolicitudes(String codigo) { Intent consultarIntent =
	 * new Intent(myContext, SolicitudesListActivity.class);
	 * consultarIntent.putExtra("codigo", jsonHandler.getCodigo());
	 * myContext.startActivity(consultarIntent); ((Activity)
	 * myContext).finish(); }
	 */

	private void verestado(String codigo) {

		String fecha = "";
		String adjunto = "";
		String estado = "";
		String nombre_tipo_solicitud = "";
		String fecha_modificacion = "";
		String descripcion = "";
		String entidad = "";
		String hecho = "";
		String respuesta = "";
		DBAdapter db = new DBAdapter(myContext);
		try {
			db.open();
			Cursor c = db.getSolicitud(codigo, Propiedades.codigo_entidad);

			if (c.moveToFirst()) {
				fecha = c.getString(c.getColumnIndex("fecha"));
				fecha_modificacion = c.getString(c.getColumnIndex("fecha_modificacion"));
				estado = c.getString(c.getColumnIndex("estado"));
				nombre_tipo_solicitud = c.getString(c.getColumnIndex("nombre_tipo_solicitud"));
				descripcion = c.getString(c.getColumnIndex("descripcion"));
				adjunto = c.getString(c.getColumnIndex("adjunto"));
				entidad = c.getString(c.getColumnIndex("entidad"));
				hecho = c.getString(c.getColumnIndex("hecho"));
				respuesta = c.getString(c.getColumnIndex("respuesta"));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Propiedades.TAG + " verestado", e.toString());
		} finally {
			db.close();
		}

		intent = new Intent(myContext, EstadoSolicitudActivity.class);
		extras = new Bundle();
		extras.putString("codigo", codigo);

		extras.putString("fechaCreacion", fecha);

		extras.putString("fechaModificacion", fecha_modificacion);

		extras.putString("estado", estado);

		// extras.putString("nombreEntidad", entidad);
		extras.putString("tipoSolicitud", nombre_tipo_solicitud);

		extras.putString("descripcion", descripcion);

		extras.putString("hechos", hecho);

		extras.putString("adjunto", adjunto);

		extras.putString("entidad", entidad);

		extras.putString("respuesta", respuesta);

		intent.putExtras(extras);

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		myContext.startActivity(intent);
		((Activity) myContext).finish();
	}

	protected void updateSolicitud(String descripcion, String codigo, String estado, String nombre_tipo_solicitud,
			String codigoTipoCanalSolicitud, String fecha, String fecha_modificacion, String asunto, String respuesta,
			String adjunto) {

		// ---update solicitud---

		DBAdapter db = new DBAdapter(myContext);

		try {

			Log.d("DATABASE", db.toString());

			db.open();

			if (db.updateSolicitud(descripcion, codigo, estado, nombre_tipo_solicitud, codigoTipoCanalSolicitud, fecha,
					fecha_modificacion, asunto, respuesta, adjunto)) {

				handler.post(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(myContext, myContext.getString(R.string.solicitud_actualizada),
								Toast.LENGTH_LONG).show();
					}
				});
			} else {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(myContext, myContext.getString(R.string.solicitud_no_actualizada),
								Toast.LENGTH_LONG).show();
					}
				});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.d(Propiedades.TAG + " updateSolicitud", ex.toString());

			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(myContext, myContext.getString(R.string.solicitud_no_actualizada), Toast.LENGTH_LONG)
							.show();
				}
			});
		} finally {
			db.close();
		}
		verestado(codigo);

	}

	protected void updateSolicitud(String entidad, String primernombre, String segundonombre, String primerapellido,
			String segundoapellido, String email, String tipo_solicitud, String descripcion, String codigo,
			String webservice, String estado, String vulnerable, String tipo_documento, String documento,
			String departamento, String municipio, String direccion, String telefono, String celular, String asunto,
			String adjunto, String medio_recepcion, String medio_respuesta, String respuesta, String anonimo,
			String simcard, String imei, String hecho, String nombre_tipo_identificacion, String nombre_tipo_solicitud,
			String nombre_vulnerable, String nombre_departamento, String nombre_municipio, String codigo_pais,
			String nombre_pais, String codigoAsuntoSolicitud, String codigoTipoCanalSolicitud,
			String codigoTipoCanalRespuesta, String codigoEntidad, String siglaEntidad,
			String numeroTelefonicoDispositivo, String fecha, String fecha_modificacion, String llave) {

		// ---update solicitud---
		boolean act = false;
		DBAdapter db = new DBAdapter(myContext);

		try {

			db.open();
			Cursor c = db.getSolicitud(codigo, codigoEntidad);

			act = db.updateSolicitud(entidad, primernombre, segundonombre, primerapellido, segundoapellido, email,
					tipo_solicitud, descripcion, codigo, webservice, estado, vulnerable, tipo_documento, documento,
					departamento, municipio, direccion, telefono, celular, asunto, adjunto, medio_recepcion,
					medio_respuesta, respuesta, anonimo, simcard, imei, hecho, nombre_tipo_identificacion,
					nombre_tipo_solicitud, nombre_vulnerable, nombre_departamento, nombre_municipio, codigo_pais,
					nombre_pais, codigoAsuntoSolicitud, codigoTipoCanalSolicitud, codigoTipoCanalRespuesta,
					codigoEntidad, siglaEntidad, numeroTelefonicoDispositivo, fecha, fecha_modificacion, llave);
			c.close();
		} catch (Exception e) {
		} finally {
			db.close();
		}

		if (act) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(myContext, myContext.getString(R.string.solicitud_actualizada), Toast.LENGTH_LONG)
							.show();
				}
			});
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(myContext, myContext.getString(R.string.solicitud_no_actualizada), Toast.LENGTH_LONG)
							.show();
				}
			});
		}

	}

	protected void addSolicitud(final String entidad, final String primernombre, final String segundonombre,
			final String primerapellido, final String segundoapellido, final String email, final String tipo_solicitud,
			final String descripcion, final String codigo, final String webservice, final String anonimo,
			final String estado, final String vulnerable, final String tipo_documento, final String documento,
			final String departamento, final String municipio, final String direccion, final String telefono,
			final String celular, final String asunto, final String adjunto, final String medio_recepcion,
			final String medio_respuesta, final String respuesta, final String simcard, final String imei,
			final String hecho, final String nombre_tipo_identificacion, final String nombre_tipo_solicitud,
			final String nombre_vulnerable, final String nombre_departamento, final String nombre_municipio,
			final String codigo_pais, final String nombre_pais, final String codigoAsuntoSolicitud,
			final String codigoTipoCanalSolicitud, final String codigoTipoCanalRespuesta, final String codigoEntidad,
			final String siglaEntidad, final String numeroTelefonicoDispositivo, final String fecha,
			final String fecha_modificacion, final String llave) {

		// ---adicionar una solicitud---
		DBAdapter db = new DBAdapter(myContext);

		try {

			db.open();

			Cursor c = db.getSolicitud(codigo, codigoEntidad);
			if (c.moveToFirst()) {

				Thread th = new Thread() {
					@Override
					public void run() {
						updateSolicitud(entidad, primernombre, segundonombre, primerapellido, segundoapellido, email,
								tipo_solicitud, descripcion, codigo, webservice, estado, vulnerable, tipo_documento,
								documento, departamento, municipio, direccion, telefono, celular, asunto, adjunto,
								medio_recepcion, medio_respuesta, respuesta, anonimo, simcard, imei, hecho,
								nombre_tipo_identificacion, nombre_tipo_solicitud, nombre_vulnerable,
								nombre_departamento, nombre_municipio, codigo_pais, nombre_pais, codigoAsuntoSolicitud,
								codigoTipoCanalSolicitud, codigoTipoCanalRespuesta, codigoEntidad, siglaEntidad,
								numeroTelefonicoDispositivo, fecha, fecha_modificacion, llave);
					}
				};
				th.start();

				handler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(myContext, myContext.getString(R.string.solicitud_existe), Toast.LENGTH_LONG)
								.show();
					}
				});
			} else {

				/*
				 * if (entidad, primernombre, segundonombre, primerapellido,
				 * segundoapellido, email, tipo_solicitud, descripcion, codigo,
				 * webservice, iddato, estado, vulnerable, tipo_documento,
				 * documento, departamento, municipio, direccion, telefono,
				 * celular, asunto, adjunto, medio_recepcion, medio_respuesta,
				 * anonimo, simcard, imei, hecho, nombre_tipo_identificacion,
				 * nombre_tipo_solicitud, nombre_vulnerable,
				 * nombre_departamento, nombre_municipio, codigo_pais,
				 * nombre_pais, codigoAsuntoSolicitud, codigoTipoCanalSolicitud,
				 * codigoTipoCanalRespuesta, codigoEntidad, siglaEntidad,
				 * numeroTelefonicoDispositivo, fecha, fecha_modificacion,
				 * llave) >= 0){ handler.post( new Runnable() {
				 * 
				 * @Override public void run() { Toast.makeText(myContext,
				 * myContext.getString(R.string.salvar_consulta),
				 * Toast.LENGTH_LONG).show(); } } ); }
				 */

			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Propiedades.TAG + " addSolicitud", e.toString());
		} finally {
			db.close();
		}

		db.close();

	}

	private void addSolicitud(String descripcion, String codigo, String estado, String nombre_tipo_solicitud,
			String codigoTipoCanalSolicitud, String fecha, String fecha_modificacion, String asunto, String respuesta,
			String adjunto) {
		DBAdapter db = null;
		try {
			if (db == null) {
				db = new DBAdapter(myContext);
			}

			db.open();
			final long result = db.insertSolicitud(Propiedades.entidad, "", "", "", "", "", "", descripcion, codigo,
					Propiedades.webservice, "", estado, "", "", "", "", "", "", "", "", "", adjunto, "", "", respuesta,
					"", "", "", "", "", nombre_tipo_solicitud, "", "", "", "", "", "", "", "",
					Propiedades.codigo_entidad, "", "", fecha, fecha_modificacion, "");
			if (result >= 0) {

				handler.post(new Runnable() {

					// @Override
					@Override
					public void run() {
						Toast t = Toast.makeText(myContext, myContext.getString(R.string.solicitud_agregada),
								Toast.LENGTH_SHORT);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();

					}
				});

			}
		} catch (Exception e) {
		} finally {
			if (db != null) {
				db.close();
			}
		}
		verestado(codigo);

	}

}
