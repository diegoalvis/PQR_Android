package net.micrositios.pqrapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class RefreshFromFeed {

	private final Context myContext;
	private ProgressDialog dialog;
	// private IotdHandler iotdHandler;
	private JSONHandler jsonHandler;
	private static String URL_RSS = null;
	DBAdapter db;
	Intent intent;
	Handler handler;

	public RefreshFromFeed(Context context) {
		myContext = context;
		handler = new Handler();

	}

	public void setUrlRss(String urlRss) {
		URL_RSS = urlRss;
	}

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
			@Override
			public void run() {
				if (jsonHandler == null) {
					jsonHandler = new JSONHandler();
				}
				try {
					jsonHandler.processResponse(myContext, URL_RSS);

					handler.post(new Runnable() {
						@Override
						public void run() {
							Log.d("MICROSITIOS_0", "SEGUNDO");
							Log.d("MICROSITIOS_0", URL_RSS);

							resetDisplay(jsonHandler.getEntidad(), jsonHandler.getPrimernombre(),
									jsonHandler.getSegundonombre(), jsonHandler.getPrimerapellido(),
									jsonHandler.getSegundoapellido(), jsonHandler.getEmail(),
									jsonHandler.getTipo_solicitud(), jsonHandler.getDescripcion(),
									jsonHandler.getCodigo(), jsonHandler.getWebservice(), jsonHandler.getIddato(),
									jsonHandler.getEstado(), jsonHandler.getVulnerable(),
									jsonHandler.getTipo_documento(), jsonHandler.getDocumento(),
									jsonHandler.getDepartamento(), jsonHandler.getMunicipio(),
									jsonHandler.getDireccion(), jsonHandler.getTelefono(), jsonHandler.getCelular(),
									jsonHandler.getAsunto(), jsonHandler.getAdjunto(), jsonHandler.getMedio_recepcion(),
									jsonHandler.getMedio_respuesta(), jsonHandler.getRespuesta(),
									jsonHandler.getAnonimo(), jsonHandler.getSimcard(), jsonHandler.getImei(),
									jsonHandler.getHecho(), jsonHandler.getNombre_tipo_identificacion(),
									jsonHandler.getNombre_tipo_solicitud(), jsonHandler.getNombre_vulnerable(),
									jsonHandler.getNombre_departamento(), jsonHandler.getNombre_municipio(),
									jsonHandler.getCodigo_pais(), jsonHandler.getNombre_pais(),
									jsonHandler.getCodigoAsuntoSolicitud(), jsonHandler.getCodigoTipoCanalSolicitud(),
									jsonHandler.getCodigoTipoCanalRespuesta(), jsonHandler.getCodigoEntidad(),
									jsonHandler.getSiglaEntidad(), jsonHandler.getNumeroTelefonicoDispositivo(),
									jsonHandler.getFecha(), jsonHandler.getFecha_modificacion(), jsonHandler.getLlave(),
									jsonHandler.getCod_error(), jsonHandler.getMensaje_error());
							dialog.dismiss();

						}
					}

					);

				} catch (Exception e) {
					e.printStackTrace();
					handler.post(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();
							Toast.makeText(myContext, myContext.getString(R.string.error_wifi), Toast.LENGTH_SHORT)
									.show();
							Toast.makeText(myContext, myContext.getString(R.string.error_wifi_revisar),
									Toast.LENGTH_SHORT).show();

						}
					});

				}

			}
		};
		th.start();

	}

	private void resetDisplay(String entidad, String primernombre, String segundonombre, String primerapellido,
			String segundoapellido, String email, String tipo_solicitud, String descripcion, String codigo,
			String webservice, String iddato, String estado, String vulnerable, String tipo_documento, String documento,
			String departamento, String municipio, String direccion, String telefono, String celular, String asunto,
			String adjunto, String medio_recepcion, String medio_respuesta, String respuesta, String anonimo,
			String simcard, String imei, String hecho, String nombre_tipo_identificacion, String nombre_tipo_solicitud,
			String nombre_vulnerable, String nombre_departamento, String nombre_municipio, String codigo_pais,
			String nombre_pais, String codigoAsuntoSolicitud, String codigoTipoCanalSolicitud,
			String codigoTipoCanalRespuesta, String codigoEntidad, String siglaEntidad,
			String numeroTelefonicoDispositivo, String fecha, String fecha_modificacion, String llave, String cod_error,
			String mensaje_error) {

		if (cod_error.equals("0")) {

			addSolicitud(entidad, primernombre, segundonombre, primerapellido, segundoapellido, email, tipo_solicitud,
					descripcion, codigo, webservice, iddato, estado, vulnerable, tipo_documento, documento,
					departamento, municipio, direccion, telefono, celular, asunto, adjunto, medio_recepcion,
					medio_respuesta, respuesta, anonimo, simcard, imei, hecho, nombre_tipo_identificacion,
					nombre_tipo_solicitud, nombre_vulnerable, nombre_departamento, nombre_municipio, codigo_pais,
					nombre_pais, codigoAsuntoSolicitud, codigoTipoCanalSolicitud, codigoTipoCanalRespuesta,
					codigoEntidad, siglaEntidad, numeroTelefonicoDispositivo, fecha, fecha_modificacion, llave);
					openDialogsolicitud();

			// intent = new Intent(myContext, SolicitudesListActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intent.putExtra("EXIT", true);
			// myContext.startActivity(intent);
			// Toast.makeText(myContext, mensaje_error,
			// Toast.LENGTH_LONG).show();

		} else {
			// Validar codigos de error
			if (mensaje_error.trim().contentEquals("")) {
				if (!jsonHandler.isIs_json()) {
					Log.i(Propiedades.TAG, "Error NO llego Json");

				} else {
					Toast.makeText(myContext, myContext.getString(R.string.error_conexion_servidor), Toast.LENGTH_SHORT)
							.show();
				}

			} else {
				Toast.makeText(myContext, mensaje_error, Toast.LENGTH_LONG).show();
			}

			listarsolicitudes();

		}

	}

	private void listarsolicitudes() {
		Intent consultarIntent = new Intent(myContext, SolicitudesListActivity.class);
		myContext.startActivity(consultarIntent);
		((Activity) myContext).finish();
	}

	private void openDialogsolicitud() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
		// set title
		alertDialogBuilder.setTitle(myContext.getResources().getString(R.string.solicitud_creada));
		// set dialog message

		alertDialogBuilder.setMessage(myContext.getResources().getString(R.string.msgsolicitud_creada));

		alertDialogBuilder.setCancelable(false);

		alertDialogBuilder.setNeutralButton(myContext.getResources().getString(R.string.aceptar),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						listarsolicitudes();
					}
				});

		AlertDialog dialogo1 = alertDialogBuilder.create();

		dialogo1.show();
		int dividerId = dialogo1.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);

		View divider = dialogo1.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		// if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

		divider.setBackgroundColor(myContext.getResources().getColor(R.color.aplicacion));
		// } else {
		// // divider.setBackground(micontext
		// // .getDrawable(R.drawable.fondoactionbar));
		//
		// }
		int textViewId = dialogo1.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialogo1.findViewById(textViewId);
		tv.setTextColor(myContext.getResources().getColor(R.color.aplicacion));

	}

	private void addSolicitud(String entidad, String primernombre, String segundonombre, String primerapellido,
			String segundoapellido, String email, String tipo_solicitud, String descripcion, String codigo,
			String webservice, String iddato, String estado, String vulnerable, String tipo_documento, String documento,
			String departamento, String municipio, String direccion, String telefono, String celular, String asunto,
			String adjunto, String medio_recepcion, String medio_respuesta, String respuesta, String anonimo,
			String simcard, String imei, String hecho, String nombre_tipo_identificacion, String nombre_tipo_solicitud,
			String nombre_vulnerable, String nombre_departamento, String nombre_municipio, String codigo_pais,
			String nombre_pais, String codigoAsuntoSolicitud, String codigoTipoCanalSolicitud,
			String codigoTipoCanalRespuesta, String codigoEntidad, String siglaEntidad,
			String numeroTelefonicoDispositivo, String fecha, String fecha_modificacion, String llave) {
		try {
			if (db == null) {
				db = new DBAdapter(myContext);
			}

			db.open();
			final long result = db.insertSolicitud(entidad, primernombre, segundonombre, primerapellido,
					segundoapellido, email, tipo_solicitud, descripcion, codigo, webservice, iddato, estado, vulnerable,
					tipo_documento, documento, departamento, municipio, direccion, telefono, celular, asunto, adjunto,
					medio_recepcion, medio_respuesta, respuesta, anonimo, simcard, imei, hecho,
					nombre_tipo_identificacion, nombre_tipo_solicitud, nombre_vulnerable, nombre_departamento,
					nombre_municipio, codigo_pais, nombre_pais, codigoAsuntoSolicitud, codigoTipoCanalSolicitud,
					codigoTipoCanalRespuesta, codigoEntidad, siglaEntidad, numeroTelefonicoDispositivo, fecha,
					fecha_modificacion, llave);
			if (result >= 0) {

				handler.post(new Runnable() {

					// @Override
					@Override
					public void run() {
						Toast.makeText(myContext, myContext.getString(R.string.solicitud_agregada), Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

	}

}
