package net.micrositios.pqrapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private String codigo;
	private String entidad;

	public GCMIntentService() {
		super("586611920514");
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.d("GCMTest", "REGISTRATION: Error -> " + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String msg = intent.getExtras().getString("message");
		codigo = intent.getExtras().getString("codigo");
		entidad = intent.getExtras().getString("entidad");

		String mensajes = intent.getExtras().toString();
		Log.d("GCMTest", "Mensaje: " + msg + " " + mensajes);
		try {

			SharedPreferences prefs = context.getSharedPreferences(
					"MisPreferencias", Context.MODE_PRIVATE);

			String usuario = prefs.getString("usuario", "por_defecto");
			if (!usuario.equals("por_defecto")) {
				mostrarNotificacion(context, msg);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.d("GCMTest", "REGISTRATION: Registrado OK. REGID:" + regId);

		SharedPreferences prefs = context.getSharedPreferences(
				"MisPreferencias", Context.MODE_PRIVATE);

		String usuario = prefs.getString("usuario", "por_defecto");

		registroServidor(usuario, regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d("GCMTest", "REGISTRATION: Desregistrado OK.");
	}

	private void registroServidor(String usuario, String regId) {/*
																 * final String
																 * NAMESPACE =
																 * "http://sgoliver.net/"
																 * ; final
																 * String URL=
																 * "http://10.0.2.2:1634/ServicioRegistroGCM.asmx"
																 * ; final
																 * String
																 * METHOD_NAME =
																 * "RegistroCliente"
																 * ; final
																 * String
																 * SOAP_ACTION =
																 * "http://sgoliver.net/RegistroCliente"
																 * ;
																 * 
																 * SoapObject
																 * request = new
																 * SoapObject
																 * (NAMESPACE,
																 * METHOD_NAME);
																 * 
																 * request.
																 * addProperty
																 * ("usuario",
																 * usuario);
																 * request
																 * .addProperty
																 * ("regGCM",
																 * regId);
																 * 
																 * SoapSerializationEnvelope
																 * envelope =
																 * new
																 * SoapSerializationEnvelope
																 * (
																 * SoapEnvelope.
																 * VER11);
																 * 
																 * envelope.dotNet
																 * = true;
																 * 
																 * envelope.
																 * setOutputSoapObject
																 * (request);
																 * 
																 * HttpTransportSE
																 * transporte =
																 * new
																 * HttpTransportSE
																 * (URL);
																 * 
																 * try {
																 * transporte
																 * .call
																 * (SOAP_ACTION,
																 * envelope);
																 * 
																 * SoapPrimitive
																 * resultado_xml
																 * =
																 * (SoapPrimitive
																 * )envelope.
																 * getResponse
																 * (); String
																 * res =
																 * resultado_xml
																 * .toString();
																 * 
																 * if(res.equals(
																 * "1"))
																 * Log.d("GCMTest"
																 * ,
																 * "Registro WS: OK."
																 * ); } catch
																 * (Exception e)
																 * {
																 * Log.d("GCMTest"
																 * ,
																 * "Registro WS: NOK. "
																 * +
																 * e.getCause()
																 * + " || " +
																 * e.getMessage
																 * ()); }
																 */
	}

	private void mostrarNotificacion(Context context, String msg) {

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.notification)
				.setContentTitle(
						context.getResources().getString(R.string.app_name))
				.setContentText(
						context.getResources().getString(R.string.su_solicitud)
								+ " "
								+ codigo
								+ " "
								+ context.getResources().getString(
										R.string.ha_cambiado))
				.setSound(alarmSound).setAutoCancel(true);

		Intent localIntent = new Intent(context,
				ConsultaDesdeNotificacionActivity.class);
		localIntent.putExtra("entidad", entidad);
		localIntent.putExtra("codigo", codigo);
		TaskStackBuilder localTaskStackBuilder = TaskStackBuilder
				.create(context);
		localTaskStackBuilder
				.addParentStack(ConsultaDesdeNotificacionActivity.class);
		localTaskStackBuilder.addNextIntent(localIntent);
		localBuilder.setContentIntent(localTaskStackBuilder.getPendingIntent(0,
				134217728));
		NotificationManager nMgr = (NotificationManager) context
				.getSystemService("notification");
		nMgr.notify(1, localBuilder.build());
		/*
		 * //Obtenemos una referencia al servicio de notificaciones String ns =
		 * Context.NOTIFICATION_SERVICE; NotificationManager notManager =
		 * (NotificationManager) context.getSystemService(ns);
		 * 
		 * //Configuramos la notificaci�n int icono =
		 * android.R.drawable.stat_sys_warning; CharSequence textoEstado =
		 * "Alerta!"; long hora = System.currentTimeMillis();
		 * 
		 * Notification notif = new Notification(icono, textoEstado, hora);
		 * 
		 * //Configuramos el Intent Context contexto =
		 * context.getApplicationContext(); CharSequence titulo =
		 * "Nuevo Mensaje"; CharSequence descripcion = msg;
		 * 
		 * Intent notIntent = new Intent(contexto, GCMIntentService.class);
		 * 
		 * PendingIntent contIntent = PendingIntent.getActivity( contexto, 0,
		 * notIntent, 0);
		 * 
		 * notif.setLatestEventInfo( contexto, titulo, descripcion, contIntent);
		 * 
		 * //AutoCancel: cuando se pulsa la notificai�n �sta desaparece
		 * //notif.flags |= Notification.FLAG_AUTO_CANCEL;
		 * 
		 * //Enviar notificaci�n notManager.notify(1, notif);
		 */

	}
}
