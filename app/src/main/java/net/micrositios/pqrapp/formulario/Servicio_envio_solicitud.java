package net.micrositios.pqrapp.formulario;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.micrositios.pqrapp.Propiedades;
import net.micrositios.pqrapp.R;
import net.micrositios.pqrapp.RefreshFromFeed;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Servicio_envio_solicitud extends AsyncTask<String, Void, Void> {

	private String TAG = "micrositios";

	private String entidad = "";
	private String primernombre = "";
	private String segundonombre = "";
	private String primerapellido = "";
	private String segundoapellido = "";
	private String email = "";
	private String tipo_solicitud = "";
	private String descripcion = "";
	private String codigo = "";
	private String webservice = "";
	private String iddato = "";
	private String estado = "";
	private String vulnerable = "";
	private String tipo_documento = "";
	private String documento = "";
	private String departamento = "";
	private String municipio = "";
	private String direccion = "";
	private String telefono = "";
	private String celular = "";
	private String asunto = "";
	private String adjunto = "";
	private String medio_recepcion = "";
	private String medio_respuesta = "";
	private String anonimo = "";
	private String simcard = "";
	private String imei = "";
	private String hecho = "";
	private String nombre_tipo_identificacion = "";
	private String nombre_tipo_solicitud = "";
	private String nombre_vulnerable = "";
	private String nombre_departamento = "";
	private String nombre_municipio = "";
	private String codigo_pais = "";
	private String nombre_pais = "";
	private String codigoAsuntoSolicitud = "";
	private String codigoTipoCanalSolicitud = "";
	private String codigoTipoCanalRespuesta = "";
	private String codigoEntidad = "";
	private String siglaEntidad = "";
	private String numeroTelefonicoDispositivo = "";
	private String fecha = "";
	private String fecha_modificacion = "";
	private String llave = "";

	private static String xmlRespuesta;
	private final Activity myContext;
	private static String URL_RSS;
	private ProgressDialog dialog;
	private String idGCM = "";
	private String latitud = "";
	private String longitud = "";

	public Servicio_envio_solicitud(Activity myContext) {

		this.myContext = myContext;

	}

	@Override
	protected Void doInBackground(String... arg0) {
		Log.i(TAG, "doInBackground");
		getCodigoSeguimiento(tipo_solicitud, nombre_tipo_solicitud,
				codigoAsuntoSolicitud, asunto, descripcion, hecho,
				codigoTipoCanalSolicitud, medio_recepcion,
				codigoTipoCanalRespuesta, medio_respuesta, codigo,
				codigoEntidad, entidad, siglaEntidad, primernombre,
				segundonombre, primerapellido, segundoapellido, tipo_documento,
				nombre_tipo_identificacion, documento, vulnerable,
				nombre_vulnerable, direccion, email, telefono, celular,
				codigo_pais, nombre_pais, departamento, nombre_departamento,
				municipio, nombre_municipio, imei, simcard,
				numeroTelefonicoDispositivo, adjunto, webservice, estado,
				idGCM, latitud, longitud);

		return null;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * [{"Consecutivo":22,"Dependencia":"CONSULTORIA Y PROCESOS",
	 * "FechaSolicitud"
	 * :"2012-11-27T08:13:00","IdRadicado":32,"IdTipoSolicitud":49
	 * ,"Observacion":"","Responsable":"LUZ MERY REYES DELGADO","TipoSolicitud":
	 * "FACTURAS DE PROVVEEDORES"}
	 * ,{"Consecutivo":19,"Dependencia":"DIRECCION COMERCIAL"
	 * ,"FechaSolicitud":"2013-06-20T16:26:00"
	 * ,"IdRadicado":60,"IdTipoSolicitud"
	 * :2,"Observacion":"DAR RESPUESTA","Responsable":"MARTA RUIZ"
	 * ,"TipoSolicitud":"ENTIDADES DEL ESTADO"}]
	 * 
	 * {"respuesta":{"idTipoSolicitud":2,"idMedioRespuesta
	 * :0,"Identificacion":"79687482","Remitente":"Nombre de Prueba"
	 * ,"Direccion":"Direccion de prueba","mail":"prueba@correo.com",
	 * "Creadopor":"SA","codCiudadDANE":"11001","Observaciones":" Texto de
	 * observaciones de prueba que contiene tildes y enes. Borrelo",
	 * "fechaRemite"
	 * :"2014-12-15","mensaje":"234-133","radicado":"234","consecutivo":"133",
	 * "adjuntos"
	 * :[{"nombre":"test_pdf.pdf","error":0},{"nombre":"test_pdf - copia.pdf"
	 * ,"error":0}]}, "error":{"error_codigo":0,"error_mensaje":"OK"}}
	 * 
	 * respuesta obtenida
	 * 
	 * {"respuesta":{"idTipoSolicitud":"2","idMedioRespuesta":"","Identificacion"
	 * :"79687482",
	 * "Remitente":"juan pablo velandia pachon","Direccion":"Kra 10a # 24- 76",
	 * "mail"
	 * :"bafer1401@hotmail.com","Creadopor":"SA","codCiudadDANE":"-- Municipios --"
	 * , "Observaciones":
	 * "yxuccucucyc fucuviviiv fivicucufuf fucufificivif ficjcicffu fuxuxucuc ufucuc"
	 * ,
	 * "fechaRemite":"2014-12-15","mensaje":"235-134","radicado":"235","consecutivo"
	 * :"134", "adjuntos":[{"nombre":"IMG-20141128-WA0003.jpg","error":0}]},
	 * "error":{"error_codigo":0,"error_mensaje":"OK"}}
	 */

	/*
	 * 
	 * 
	 * 
	 * */

	private void getCodigoSeguimiento(String tipo_solicitud,
			String nombre_tipo_solicitud, String codigoAsuntoSolicitud,
			String asunto, String descripcion, String hecho,
			String codigoTipoCanalSolicitud, String medio_recepcion,
			String codigoTipoCanalRespuesta, String medio_respuesta,
			String codigo, String codigoEntidad, String entidad,
			String siglaEntidad, String primernombre, String segundonombre,
			String primerapellido, String segundoapellido,
			String tipo_documento, String nombre_tipo_identificacion,
			String documento, String vulnerable, String nombre_vulnerable,
			String direccion, String email, String telefono, String celular,
			String codigo_pais, String nombre_pais, String departamento,
			String nombre_departamento, String municipio,
			String nombre_municipio, String imei, String simcard,
			String numeroTelefonicoDispositivo, String adjunto,
			String webservice, String estado, String idGCM, String latitud,
			String longitud) {

		Log.d(Propiedades.TAG, Propiedades.webservice);

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Propiedades.webservice
				+ "/solicitudes/solicitud");

		Log.i("MICROSITIOS", "Ingreso servicio" + Propiedades.webservice
				+ "/solicitudes/solicitud");

		try {
			MultipartEntity reqEntity = new MultipartEntity();

			try {
				if (!adjunto.contentEquals("")) {
					if (adjunto.split("\\|").length == 0) {
						File f = new File(adjunto);
						Log.d("MICROS_1", f.toString());
						reqEntity.addPart("adjunto[]", new FileBody(f));
					} else {
						for (int i = 0; i < adjunto.split("\\|").length; i++) {
							File f = new File(adjunto.split("\\|")[i]);
							Log.d("MICROS_2", f.toString());
							reqEntity.addPart("adjunto[]", new FileBody(f));
						}
					}
				}
			} catch (Exception ex) {
				Log.d("MICROSITIOS ADJUNTO", adjunto.split("\\|").length
						+ "   " + ex.toString());
			}

			reqEntity.addPart("entidad", new StringBody(entidad));
			reqEntity.addPart("primernombre", new StringBody(primernombre));
			reqEntity.addPart("segundonombre", new StringBody(segundonombre));
			reqEntity.addPart("primerapellido", new StringBody(primerapellido));
			reqEntity.addPart("segundoapellido",
					new StringBody(segundoapellido));
			reqEntity.addPart("email", new StringBody(email));
			reqEntity.addPart("tipo_solicitud", new StringBody(tipo_solicitud));
			reqEntity.addPart("descripcion", new StringBody(descripcion));
			reqEntity.addPart("codigo", new StringBody(codigo));
			reqEntity.addPart("webservice", new StringBody(webservice));
			reqEntity.addPart("estado", new StringBody(estado));
			reqEntity.addPart("vulnerable", new StringBody(vulnerable));
			reqEntity.addPart("tipo_documento", new StringBody(tipo_documento));
			reqEntity.addPart("documento", new StringBody(documento));
			reqEntity.addPart("departamento", new StringBody(departamento));
			reqEntity.addPart("municipio", new StringBody(municipio));
			reqEntity.addPart("direccion", new StringBody(direccion));
			reqEntity.addPart("telefono", new StringBody(telefono));
			reqEntity.addPart("celular", new StringBody(celular));
			reqEntity.addPart("asunto", new StringBody(asunto));

			// reqEntity.addPart("Adjunto", new StringBody(""));

			reqEntity.addPart("medio_recepcion",
					new StringBody(medio_recepcion));
			reqEntity.addPart("medio_respuesta", new StringBody(""));
			try {
				reqEntity.addPart("simcard", new StringBody(simcard));
			} catch (Exception e) {
				// TODO: handle exception
				reqEntity.addPart("simcard", new StringBody(""));
			}

			reqEntity.addPart("imei", new StringBody(imei));
			reqEntity.addPart("hecho", new StringBody(hecho));
			reqEntity.addPart("nombre_tipo_identificacion", new StringBody(
					nombre_tipo_identificacion));
			reqEntity.addPart("nombre_tipo_solicitud", new StringBody(
					nombre_tipo_solicitud));
			reqEntity.addPart("nombre_vulnerable", new StringBody(
					nombre_vulnerable));
			reqEntity.addPart("nombre_municipio", new StringBody(
					nombre_municipio));
			reqEntity.addPart("nombre_departamento", new StringBody(
					nombre_departamento));
			reqEntity.addPart("codigo_pais", new StringBody(codigo_pais));
			reqEntity.addPart("nombre_pais", new StringBody(nombre_pais));
			reqEntity.addPart("codigoAsuntoSolicitud", new StringBody(
					codigoAsuntoSolicitud));
			reqEntity.addPart("codigoTipoCanalSolicitud", new StringBody(
					codigoTipoCanalSolicitud));
			reqEntity.addPart("codigoTipoCanalRespuesta", new StringBody(
					codigoTipoCanalRespuesta));
			reqEntity.addPart("codigoEntidad", new StringBody(codigoEntidad));
			reqEntity.addPart("siglaEntidad", new StringBody(siglaEntidad));
			reqEntity.addPart("numeroTelefonicoDispositivo", new StringBody(
					numeroTelefonicoDispositivo));

			Log.i("MICROSITIOS", "set info");
			try {

				reqEntity.addPart("idgcm", new StringBody(idGCM));
				Log.i("MICROSITIOS", "set idGCM" + idGCM);
			} catch (Exception e) {
				// TODO: handle exception
				reqEntity.addPart("idgcm", new StringBody(""));
			}
			reqEntity.addPart("latitud", new StringBody(latitud));
			reqEntity.addPart("longitud", new StringBody(longitud));

			reqEntity.addPart("proceso", new StringBody("proceso"));

			httppost.setEntity(reqEntity);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			// According to the JAVA API, InputStream constructor do nothing.
			// So we can't initialize InputStream although it is not an
			// interface
			InputStream inputStream = response.getEntity().getContent();

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			// InputStreamReader inputStreamReader = new
			// InputStreamReader(inputStream);

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);

			}

			xmlRespuesta = stringBuilder.toString();
			Log.i("MICROSITIOS",
					"************** Respuesta servicio solicitud ************* "
							+ xmlRespuesta);

		} catch (Exception e) {
			Log.d(TAG, e.toString());
			e.printStackTrace();

		}
	}

	@Override
	protected void onPreExecute() {
		Log.i(TAG, "onPreExecute");
		dialog = ProgressDialog.show(myContext,
				myContext.getString(R.string.cargando),
				myContext.getString(R.string.progress_dialog_enviando));
		int dividerId = dialog.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= android.os.Build.VERSION_CODES.KITKAT) {

			divider.setBackgroundColor(myContext.getResources().getColor(
					R.color.aplicacion_oscuro));
		} else {
			// divider.setBackground(micon
			// .getDrawable(R.drawable.fondoactionbar));

		}
		int textViewId = dialog.getContext().getResources()
				.getIdentifier("android:id/alertTitle", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(myContext.getResources().getColor(R.color.aplicacion));

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		Log.i(TAG, "onProgressUpdate");
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.i(TAG, "onPostExecute");

		URL_RSS = xmlRespuesta;

		dialog.dismiss();

		if (URL_RSS == null) {
			Toast.makeText(myContext,
					myContext.getString(R.string.error_conexion_servidor),
					Toast.LENGTH_LONG).show();
		} else {
			RefreshFromFeed refresh = new RefreshFromFeed(myContext);
			refresh.setUrlRss(URL_RSS);
			refresh.refreshFromFeed();
			// myContext.finish();
		}
	}

	public byte[] FileToArrayOfBytes(String archivo) {
		File file = new File(archivo);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			FileInputStream fis = new FileInputStream(file);
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
			}
			fis.close();
		} catch (Exception ex) {
			Log.d("Micrositios", ex.toString());
		}
		byte[] bytes = bos.toByteArray();
		return bytes;
	}

	public String getTAG() {
		return TAG;
	}

	public String getEntidad() {
		return entidad;
	}

	public String getPrimernombre() {
		return primernombre;
	}

	public String getSegundonombre() {
		return segundonombre;
	}

	public String getPrimerapellido() {
		return primerapellido;
	}

	public String getSegundoapellido() {
		return segundoapellido;
	}

	public String getEmail() {
		return email;
	}

	public String getTipo_solicitud() {
		return tipo_solicitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getWebservice() {
		return webservice;
	}

	public String getIddato() {
		return iddato;
	}

	public String getEstado() {
		return estado;
	}

	public String getVulnerable() {
		return vulnerable;
	}

	public String getTipo_documento() {
		return tipo_documento;
	}

	public String getDocumento() {
		return documento;
	}

	public String getDepartamento() {
		return departamento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getCelular() {
		return celular;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getAdjunto() {
		return adjunto;
	}

	public String getMedio_recepcion() {
		return medio_recepcion;
	}

	public String getMedio_respuesta() {
		return medio_respuesta;
	}

	public String getAnonimo() {
		return anonimo;
	}

	public String getSimcard() {
		return simcard;
	}

	public String getImei() {
		return imei;
	}

	public String getHecho() {
		return hecho;
	}

	public String getNombre_tipo_identificacion() {
		return nombre_tipo_identificacion;
	}

	public String getNombre_tipo_solicitud() {
		return nombre_tipo_solicitud;
	}

	public String getNombre_vulnerable() {
		return nombre_vulnerable;
	}

	public String getNombre_departamento() {
		return nombre_departamento;
	}

	public String getNombre_municipio() {
		return nombre_municipio;
	}

	public String getCodigo_pais() {
		return codigo_pais;
	}

	public String getNombre_pais() {
		return nombre_pais;
	}

	public String getCodigoAsuntoSolicitud() {
		return codigoAsuntoSolicitud;
	}

	public String getCodigoTipoCanalSolicitud() {
		return codigoTipoCanalSolicitud;
	}

	public String getCodigoTipoCanalRespuesta() {
		return codigoTipoCanalRespuesta;
	}

	public void setTAG(String tAG) {
		TAG = tAG;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public void setPrimernombre(String primernombre) {
		this.primernombre = primernombre;
	}

	public void setSegundonombre(String segundonombre) {
		this.segundonombre = segundonombre;
	}

	public void setPrimerapellido(String primerapellido) {
		this.primerapellido = primerapellido;
	}

	public void setSegundoapellido(String segundoapellido) {
		this.segundoapellido = segundoapellido;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTipo_solicitud(String tipo_solicitud) {
		this.tipo_solicitud = tipo_solicitud;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setWebservice(String webservice) {
		this.webservice = webservice;
	}

	public void setIddato(String iddato) {
		this.iddato = iddato;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setVulnerable(String vulnerable) {
		this.vulnerable = vulnerable;
	}

	public void setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public void setMedio_recepcion(String medio_recepcion) {
		this.medio_recepcion = medio_recepcion;
	}

	public void setMedio_respuesta(String medio_respuesta) {
		this.medio_respuesta = medio_respuesta;
	}

	public void setAnonimo(String anonimo) {
		this.anonimo = anonimo;
	}

	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setHecho(String hecho) {
		this.hecho = hecho;
	}

	public void setNombre_tipo_identificacion(String nombre_tipo_identificacion) {
		this.nombre_tipo_identificacion = nombre_tipo_identificacion;
	}

	public void setNombre_tipo_solicitud(String nombre_tipo_solicitud) {
		this.nombre_tipo_solicitud = nombre_tipo_solicitud;
	}

	public void setNombre_vulnerable(String nombre_vulnerable) {
		this.nombre_vulnerable = nombre_vulnerable;
	}

	public void setNombre_departamento(String nombre_departamento) {
		this.nombre_departamento = nombre_departamento;
	}

	public void setNombre_municipio(String nombre_municipio) {
		this.nombre_municipio = nombre_municipio;
	}

	public void setCodigo_pais(String codigo_pais) {
		this.codigo_pais = codigo_pais;
	}

	public void setNombre_pais(String nombre_pais) {
		this.nombre_pais = nombre_pais;
	}

	public void setCodigoAsuntoSolicitud(String codigoAsuntoSolicitud) {
		this.codigoAsuntoSolicitud = codigoAsuntoSolicitud;
	}

	public void setCodigoTipoCanalSolicitud(String codigoTipoCanalSolicitud) {
		this.codigoTipoCanalSolicitud = codigoTipoCanalSolicitud;
	}

	public void setCodigoTipoCanalRespuesta(String codigoTipoCanalRespuesta) {
		this.codigoTipoCanalRespuesta = codigoTipoCanalRespuesta;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	public void setSiglaEntidad(String siglaEntidad) {
		this.siglaEntidad = siglaEntidad;
	}

	public void setNumeroTelefonicoDispositivo(
			String numeroTelefonicoDispositivo) {
		this.numeroTelefonicoDispositivo = numeroTelefonicoDispositivo;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setFecha_modificacion(String fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}

	public void setLlave(String llave) {
		this.llave = llave;
	}

	public String getCodigoEntidad() {
		return codigoEntidad;
	}

	public String getSiglaEntidad() {
		return siglaEntidad;
	}

	public String getNumeroTelefonicoDispositivo() {
		return numeroTelefonicoDispositivo;
	}

	public String getFecha() {
		return fecha;
	}

	public String getFecha_modificacion() {
		return fecha_modificacion;
	}

	public String getLlave() {
		return llave;
	}

	public static String getXmlRespuesta() {
		return xmlRespuesta;
	}

	public static String getURL_RSS() {
		return URL_RSS;
	}

	public ProgressDialog getDialog() {
		return dialog;
	}

	public static void setXmlRespuesta(String xmlRespuesta) {
		Servicio_envio_solicitud.xmlRespuesta = xmlRespuesta;
	}

	public static void setURL_RSS(String uRL_RSS) {
		URL_RSS = uRL_RSS;
	}

	public void setDialog(ProgressDialog dialog) {
		this.dialog = dialog;
	}

	public void setIdGCM(String idGCM) {
		this.idGCM = idGCM;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

}
