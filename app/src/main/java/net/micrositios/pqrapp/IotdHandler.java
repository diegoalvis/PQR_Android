package net.micrositios.pqrapp;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

public class IotdHandler extends DefaultHandler {
	//private static final String TAG = IotdHandler.class.getSimpleName();
	
	private boolean inItem = false;
	
	private boolean inStatusCode = false;
	private boolean inMensaje = false;
	private boolean inCodigo = false;
	private boolean inFechaHoraCreacion = false;
	private boolean inNombreEstadoSolicitud = false;
	private boolean inCodigoEntidad = false;
	private boolean inTipoSolicitud = false;
	private boolean inObjetoSolicitud = false;
	private boolean inHechosSolicitud = false;
	
	private boolean inMensajeErrorConsulta = false;
	private boolean inCodigoSolicitudConsulta = false;
	private boolean inFechaHoraModificaionConsulta = false;
	private boolean inRespuesta = false;
	
	private String url = null;
	
	private final StringBuffer statusCode = new StringBuffer();
	private final StringBuffer statusMessage = new StringBuffer();
	private final StringBuffer codigo = new StringBuffer();
	private final StringBuffer fechaHoraCreacion = new StringBuffer();
	private final StringBuffer nombreEstadoSolicitud = new StringBuffer();
	private final StringBuffer codigoEntidad = new StringBuffer();
	private final StringBuffer tipoSolicitud = new StringBuffer();
	private final StringBuffer objetoSolicitud = new StringBuffer();
	private final StringBuffer hechosSolicitud = new StringBuffer();
	
	private final StringBuffer mensajeError = new StringBuffer();
	private final StringBuffer codigoSolicitudConsulta = new StringBuffer();
	private final StringBuffer fechaHoraModificacionConsulta = new StringBuffer();
	private final StringBuffer respuestaConsulta = new StringBuffer();
	
	@Override
    public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		  
		if (localName.equals("enclosure")) {
			url = attributes.getValue("url");
		}
		
		if (localName.startsWith("response")) {
			inItem = true;
		} else {
			if (inItem) {
				
				/* Respuesta a crescion solicitud valida */
				if (localName.equals("statusCode")) { 
					inStatusCode = true;
				} else { 
					inStatusCode = false;
				}
				
				if (localName.equals("statusMessage")) { 
					inMensaje = true;
				} else { 
					inMensaje = false;
				}
				
				if (localName.equals("trackingHash")) { 
					inCodigo = true;
				} else { 
					inCodigo = false;
				}
				
				if (localName.equals("fechahoraCreacion")) { 
					inFechaHoraCreacion = true;
				} else { 
					inFechaHoraCreacion = false;
				}
				
				if (localName.equals("nombreEstadoSolicitud")) { 
					inNombreEstadoSolicitud = true;
				} else { 
					inNombreEstadoSolicitud = false;
				}
				
				if (localName.equals("codigoEntidad")) {
					inCodigoEntidad = true;
				} else { 
					inCodigoEntidad = false;
				}
				
				if (localName.equals("tipoSolicitud")) {
					inTipoSolicitud = true;
				} else { 
					inTipoSolicitud = false;
				}
				
				if (localName.equals("objetoSolicitud")) {
					inObjetoSolicitud = true;
				} else { 
					inObjetoSolicitud = false;
				}
				
				if (localName.equals("hechosSolicitud")) {
					inHechosSolicitud = true;
				} else { 
					inHechosSolicitud = false;
				}
				
				/* Respuesta a consulta solicitud */
				if (localName.equals("mensaje")) {
					inMensajeErrorConsulta = true;
				} else {
					inMensajeErrorConsulta = false;
				}
				
				if (localName.equals("codigoSolicitud")) {
					inCodigoSolicitudConsulta = true;
				} else {
					inCodigoSolicitudConsulta = false;
				}
				
				if (localName.equals("fechahoraModificacion")) {
					inFechaHoraModificaionConsulta = true;
				} else {
					inFechaHoraModificaionConsulta = false;
				}
				if (localName.equals("respuesta")) {
					inRespuesta = true;
				} else {
					inRespuesta = false;
				}
				
			}
		}
		
	}
	
	@Override
	public void characters(char ch[], int start, int length) {
		String chars = (new String(ch).substring(start, start + length));
		/* Respuesta a creacion solicitud valida */
		if (inStatusCode) {
			statusCode.append(chars);
		}
		if (inMensaje) {
			statusMessage.append(chars);
		}
		if (inCodigo) {
			codigo.append(chars);
		}
		if (inFechaHoraCreacion) { 
			fechaHoraCreacion.append(chars);
		}
		if (inNombreEstadoSolicitud) { 
			nombreEstadoSolicitud.append(chars);
		}
		if (inCodigoEntidad) { 
			codigoEntidad.append(chars);
		}
		if (inTipoSolicitud) { 
			tipoSolicitud.append(chars);
		}
		
		if (inObjetoSolicitud) { 
			objetoSolicitud.append(chars);
		}
		
		if (inHechosSolicitud) { 
			hechosSolicitud.append(chars);
		}
		 
		/* Respuesta a consulta solicitud */
		if (inMensajeErrorConsulta) {
			mensajeError.append(chars);
		}
		 
		if (inCodigoSolicitudConsulta) {
			codigoSolicitudConsulta.append(chars);
        }
		
		if (inFechaHoraModificaionConsulta) {
	        fechaHoraModificacionConsulta.append(chars);
        }
		
		if (inRespuesta) {
			respuestaConsulta.append(chars);
		}
		 
	 }
	
	public void processFeed(Context context, String url) {
	//public void processFeed(Context context, URL url) {
		try {
        	
        	Log.e("", url.toString());
            
               
        } catch (Exception e) {
            Log.e("", e.toString());
        } 
	}


	public String getUrl() {
		return url;
	}
	
	/* Respuesta a creacion solicitud valida */
	public String getStatusCode() {
		return statusCode.toString();
	}
	public String getMensaje() {
		return statusMessage.toString();
	}
	public String getCodigo() {
		return codigo.toString();
	}
	public String getFechaHoraCreacion() {
		return fechaHoraCreacion.toString();
	}
	public String getNombreEstadoSolicitud() {
		return nombreEstadoSolicitud.toString();
	}
	public String getCodigoEntidad() {
		return codigoEntidad.toString();
	}
	public String getTipoSolicitud() {
		return tipoSolicitud.toString();
	}
	public String getObjetoSolicitud() {
		return objetoSolicitud.toString();
	}
	public String getHechoSolicitud() {
		return hechosSolicitud.toString();
	}
	
	public void setStatusCode() {
		statusCode.delete(0, statusCode.length());
	}
	public void setMensaje() {
		statusMessage.delete(0, statusMessage.length());
	}
	public void setCodigo() {
		codigo.delete(0, codigo.length());
	}
	public void setFechaHoraCreacion() {
		fechaHoraCreacion.delete(0, fechaHoraCreacion.length());
	}
	public void setNombreEstadoSolicitud() {
		nombreEstadoSolicitud.delete(0, nombreEstadoSolicitud.length());
	}
	public void setCodigoEntidad() {
		codigoEntidad.delete(0, codigoEntidad.length());
	}
	public void setTipoSolicitud() {
		tipoSolicitud.delete(0, tipoSolicitud.length());
	}
	public void setObjetoSolicitud() {
		objetoSolicitud.delete(0, objetoSolicitud.length());
	}
	public void setHechoSolicitud() {
		hechosSolicitud.delete(0, hechosSolicitud.length());
	}
	
	/* Respuesta a consulta solicitud */
	public String getMensajeErrorConsulta() {
		return mensajeError.toString();
	}
	public String getCodigoSolicitudConsulta() {
		return codigoSolicitudConsulta.toString();
	}
	public String getFechaModificacionConsulta() {
		return fechaHoraModificacionConsulta.toString();
	}
	public String getRespuestaConsulta() {
		return respuestaConsulta.toString();
	}


	public void setMensajeErrorConsulta() {
		mensajeError.delete(0, mensajeError.length());
	}
	public void setCodigoSolicitudConsulta() {
		codigoSolicitudConsulta.delete(0, codigoSolicitudConsulta.length());
	}
	public void setFechaModificacionConsulta() {
		fechaHoraModificacionConsulta.delete(0, fechaHoraModificacionConsulta.length());
	}
	public void setRespuestaConsulta() {
		respuestaConsulta.delete(0, respuestaConsulta.length());
	}
	
}

