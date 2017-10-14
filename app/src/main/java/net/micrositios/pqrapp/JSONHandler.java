package net.micrositios.pqrapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONHandler {
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
	private String cod_error = "";
	private String mensaje_error = "";
	private String respuesta = "";
	private boolean is_json = true;
	private Boolean valido = true;
	private String encuesta = "";
	private String encuesta_enviada = "";

	public void processResponse(Context myContext, String str) {
		Log.d("Micrositios_variables", str);

		try {
			JSONObject jo = new JSONObject(str);
			JSONObject jo_error = jo.getJSONObject("error");

			String cod_error = jo_error.getString("error_codigo");
			String mensaje_error = jo_error.getString("error_mensaje");

			this.setCod_error(cod_error);
			this.setMensaje_error(mensaje_error);

			if (cod_error.contentEquals("0")) {
				JSONObject jo_mensaje = null;
				try {
					jo_mensaje = jo.getJSONObject("respuesta");
					Log.d("MICRO", "AQUI");
				} catch (Exception ex1) {
					JSONArray ja_mensaje = jo.getJSONArray("respuesta");
					if (ja_mensaje.length() != 0) {
						jo_mensaje = ja_mensaje.getJSONObject(0);
					}
				}

				if (jo_mensaje != null) {

					try {
						this.descripcion = jo_mensaje.getString("descripcion");
					} catch (Exception ex) {
						// this.valido=false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}

					try {
						this.encuesta_enviada = jo_mensaje
								.getString("encuesta");
						if (encuesta_enviada.equals("NO")) {

							Propiedades.encuesta_enviada = false;
						} else if (encuesta_enviada.equals("SI")) {
							Propiedades.encuesta_enviada = true;

						}

					} catch (Exception ex) {
						// this.valido=false;
						Log.d("Micrositios", ex.toString());
						Log.d("Micrositios", ex.toString());
					}

					try {
						this.estado = jo_mensaje.getString("estado");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_tipo_solicitud = jo_mensaje
								.getString("nombre_tipo_solicitud");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.codigoTipoCanalSolicitud = jo_mensaje
								.getString("codigoTipoCanalSolicitud");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.fecha = jo_mensaje.getString("fecha");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.fecha_modificacion = jo_mensaje
								.getString("fecha_modificacion");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}

					try {
						this.codigo = jo_mensaje.getString("codigo");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.entidad = jo_mensaje.getString("entidad");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.primernombre = jo_mensaje
								.getString("primernombre");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.segundonombre = jo_mensaje
								.getString("segundonombre");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.primerapellido = jo_mensaje
								.getString("primerapellido");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.segundoapellido = jo_mensaje
								.getString("segundoapellido");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.email = jo_mensaje.getString("email");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.tipo_solicitud = jo_mensaje
								.getString("tipo_solicitud");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.webservice = jo_mensaje.getString("webservice");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.iddato = jo_mensaje.getString("iddato");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.estado = jo_mensaje.getString("estado");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.adjunto = jo_mensaje.getString("adjunto");
					} catch (Exception ex) {
						// this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.vulnerable = jo_mensaje.getString("vulnerable");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.tipo_documento = jo_mensaje
								.getString("tipo_documento");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.documento = jo_mensaje.getString("documento");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.departamento = jo_mensaje
								.getString("departamento");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.municipio = jo_mensaje.getString("municipio");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.direccion = jo_mensaje.getString("direccion");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.telefono = jo_mensaje.getString("telefono");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.celular = jo_mensaje.getString("celular");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.asunto = jo_mensaje.getString("asunto");
					} catch (Exception ex) {
						// this.valido=false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.medio_recepcion = jo_mensaje
								.getString("medio_recepcion");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.medio_respuesta = jo_mensaje
								.getString("medio_respuesta");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.anonimo = jo_mensaje.getString("anonimo");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.simcard = jo_mensaje.getString("simcard");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.imei = jo_mensaje.getString("imei");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.hecho = jo_mensaje.getString("hecho");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_tipo_identificacion = jo_mensaje
								.getString("nombre_tipo_identificacion");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_tipo_solicitud = jo_mensaje
								.getString("nombre_tipo_solicitud");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_vulnerable = jo_mensaje
								.getString("nombre_vulnerable");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_departamento = jo_mensaje
								.getString("nombre_departamento");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_municipio = jo_mensaje
								.getString("nombre_municipio");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.codigo_pais = jo_mensaje.getString("codigo_pais");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.nombre_pais = jo_mensaje.getString("nombre_pais");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.codigoAsuntoSolicitud = jo_mensaje
								.getString("codigoAsuntoSolicitud");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.codigoTipoCanalSolicitud = jo_mensaje
								.getString("codigoTipoCanalSolicitud");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.codigoTipoCanalRespuesta = jo_mensaje
								.getString("codigoTipoCanalRespuesta");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.codigoEntidad = jo_mensaje
								.getString("codigoEntidad");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.siglaEntidad = jo_mensaje
								.getString("siglaEntidad");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.numeroTelefonicoDispositivo = jo_mensaje
								.getString("numeroTelefonicoDispositivo");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}

					try {
						this.fecha_modificacion = jo_mensaje
								.getString("fecha_modificacion");
					} catch (Exception ex) {
						this.valido = false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.llave = jo_mensaje.getString("llave");
					} catch (Exception ex) {
						Log.d("Micrositios_variables", ex.toString());
					}
					try {
						this.respuesta = jo_mensaje.getString("respuesta");
					} catch (Exception ex) {
						// this.valido=false;
						Log.d("Micrositios_variables_1", ex.toString());
						Log.d("Micrositios_variables", ex.toString());
					}
				} else {
					this.setCod_error("3");
					this.setMensaje_error(myContext
							.getString(R.string.no_hay_resultados));
				}

			} else {
				this.setCod_error(cod_error);
				this.setMensaje_error(mensaje_error);
			}
		} catch (JSONException e) {
			this.valido = false;
			is_json = false;
			e.printStackTrace();
		}
	}

	public boolean isIs_json() {
		return is_json;
	}

	public void setIs_json(boolean is_json) {
		this.is_json = is_json;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getPrimernombre() {
		return primernombre;
	}

	public void setPrimernombre(String primernombre) {
		this.primernombre = primernombre;
	}

	public String getSegundonombre() {
		return segundonombre;
	}

	public void setSegundonombre(String segundonombre) {
		this.segundonombre = segundonombre;
	}

	public String getPrimerapellido() {
		return primerapellido;
	}

	public void setPrimerapellido(String primerapellido) {
		this.primerapellido = primerapellido;
	}

	public String getSegundoapellido() {
		return segundoapellido;
	}

	public void setSegundoapellido(String segundoapellido) {
		this.segundoapellido = segundoapellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTipo_solicitud() {
		return tipo_solicitud;
	}

	public void setTipo_solicitud(String tipo_solicitud) {
		this.tipo_solicitud = tipo_solicitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getWebservice() {
		return webservice;
	}

	public void setWebservice(String webservice) {
		this.webservice = webservice;
	}

	public String getIddato() {
		return iddato;
	}

	public void setIddato(String iddato) {
		this.iddato = iddato;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(String vulnerable) {
		this.vulnerable = vulnerable;
	}

	public String getTipo_documento() {
		return tipo_documento;
	}

	public void setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public String getMedio_recepcion() {
		return medio_recepcion;
	}

	public void setMedio_recepcion(String medio_recepcion) {
		this.medio_recepcion = medio_recepcion;
	}

	public String getMedio_respuesta() {
		return medio_respuesta;
	}

	public void setMedio_respuesta(String medio_respuesta) {
		this.medio_respuesta = medio_respuesta;
	}

	public String getAnonimo() {
		return anonimo;
	}

	public void setAnonimo(String anonimo) {
		this.anonimo = anonimo;
	}

	public String getSimcard() {
		return simcard;
	}

	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getHecho() {
		return hecho;
	}

	public void setHecho(String hecho) {
		this.hecho = hecho;
	}

	public String getNombre_tipo_identificacion() {
		return nombre_tipo_identificacion;
	}

	public void setNombre_tipo_identificacion(String nombre_tipo_identificacion) {
		this.nombre_tipo_identificacion = nombre_tipo_identificacion;
	}

	public String getNombre_tipo_solicitud() {
		return nombre_tipo_solicitud;
	}

	public void setNombre_tipo_solicitud(String nombre_tipo_solicitud) {
		this.nombre_tipo_solicitud = nombre_tipo_solicitud;
	}

	public String getNombre_vulnerable() {
		return nombre_vulnerable;
	}

	public void setNombre_vulnerable(String nombre_vulnerable) {
		this.nombre_vulnerable = nombre_vulnerable;
	}

	public String getNombre_departamento() {
		return nombre_departamento;
	}

	public void setNombre_departamento(String nombre_departamento) {
		this.nombre_departamento = nombre_departamento;
	}

	public String getNombre_municipio() {
		return nombre_municipio;
	}

	public void setNombre_municipio(String nombre_municipio) {
		this.nombre_municipio = nombre_municipio;
	}

	public String getCodigo_pais() {
		return codigo_pais;
	}

	public void setCodigo_pais(String codigo_pais) {
		this.codigo_pais = codigo_pais;
	}

	public String getNombre_pais() {
		return nombre_pais;
	}

	public void setNombre_pais(String nombre_pais) {
		this.nombre_pais = nombre_pais;
	}

	public String getCodigoAsuntoSolicitud() {
		return codigoAsuntoSolicitud;
	}

	public void setCodigoAsuntoSolicitud(String codigoAsuntoSolicitud) {
		this.codigoAsuntoSolicitud = codigoAsuntoSolicitud;
	}

	public String getCodigoTipoCanalSolicitud() {
		return codigoTipoCanalSolicitud;
	}

	public void setCodigoTipoCanalSolicitud(String codigoTipoCanalSolicitud) {
		this.codigoTipoCanalSolicitud = codigoTipoCanalSolicitud;
	}

	public String getCodigoTipoCanalRespuesta() {
		return codigoTipoCanalRespuesta;
	}

	public void setCodigoTipoCanalRespuesta(String codigoTipoCanalRespuesta) {
		this.codigoTipoCanalRespuesta = codigoTipoCanalRespuesta;
	}

	public String getCodigoEntidad() {
		return codigoEntidad;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	public String getSiglaEntidad() {
		return siglaEntidad;
	}

	public void setSiglaEntidad(String siglaEntidad) {
		this.siglaEntidad = siglaEntidad;
	}

	public String getNumeroTelefonicoDispositivo() {
		return numeroTelefonicoDispositivo;
	}

	public void setNumeroTelefonicoDispositivo(
			String numeroTelefonicoDispositivo) {
		this.numeroTelefonicoDispositivo = numeroTelefonicoDispositivo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha_modificacion() {
		return fecha_modificacion;
	}

	public void setFecha_modificacion(String fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}

	public String getLlave() {
		return llave;
	}

	public void setLlave(String llave) {
		this.llave = llave;
	}

	public String getCod_error() {
		return cod_error;
	}

	public void setCod_error(String cod_error) {
		this.cod_error = cod_error;
	}

	public String getMensaje_error() {
		return mensaje_error;
	}

	public void setMensaje_error(String mensaje_error) {
		this.mensaje_error = mensaje_error;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Boolean getValido() {
		return valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}

}
