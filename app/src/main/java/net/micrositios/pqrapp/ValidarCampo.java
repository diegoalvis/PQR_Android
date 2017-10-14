package net.micrositios.pqrapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;

public class ValidarCampo {
	
	private String mensaje;
	private String nombre;
	private String datoContenido;
	private int tipo;
	
	private boolean requerido;
	
	private int minChar;
	private int maxChar;
	
	private static String textPattern = "";
	
	private final Context myContext;
	
	String mensajeRangoCaracteres, mensajeNumCaracteres, mensajeRequerido, mensajeEmail, mensajeTipo1, mensajeTipo2, mensajeTipo4, mensajeTipo5, mensajeTipo6;
	
	public ValidarCampo(Context context){
		myContext = context;
		mensajeRangoCaracteres = myContext.getString(R.string.mensaje_rango_caracteres);
		mensajeNumCaracteres = myContext.getString(R.string.mensaje_num_caracteres);
		mensajeRequerido = myContext.getString(R.string.campo_requerido);
		mensajeEmail = myContext.getString(R.string.mensaje_email);
		mensajeTipo1 = myContext.getString(R.string.mensaje_tipo1);
		mensajeTipo2 = myContext.getString(R.string.mensaje_tipo2);
		mensajeTipo4 = myContext.getString(R.string.mensaje_tipo4);
		mensajeTipo5 = myContext.getString(R.string.mensaje_tipo5);
		mensajeTipo6 = myContext.getString(R.string.mensaje_tipo6);
		
	}
	
	
	public void setContenidoCampo(String contenidoCampo) {
		datoContenido = contenidoCampo;
	}
	
	public void setNombre(String nombreCampo) {
		nombre = nombreCampo;
	}
    
    public void setRequerido(Boolean campoRequerido) {
		requerido = campoRequerido;
	}
    
    public void setMinChar(Integer caracterMin) {
		minChar = caracterMin;
	}
    
    public void setMaxChar(Integer caracterMax) {
		maxChar = caracterMax;
	}
    
    public void setPattern(String expresionPattern) {
    	textPattern = expresionPattern;
	}
    
    public void setTipo(int tipoCampo) {
    	tipo = tipoCampo;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	
	public boolean Validar() {
		
    	if (checkVacio()) {
    		
    		if (requerido) {
    			mensaje = mensajeRequerido;
    			return false;
			} else {
				return true;
			}
    		
    		
    	} else {
    		
    		if (tipo == 3) {
    			
    			boolean validCaracteres = validateCampo(datoContenido);
    			
    			if (!validCaracteres) {
					mensaje = mensajeEmail;
					return false;
				} else {
					mensaje = "";
					return true;
				}
				
			} else {
				
				boolean dimensionErrada = checkNumChar(datoContenido, minChar, maxChar);
	    		
	    		if (dimensionErrada) {
	    			
	    			if (minChar == maxChar) {
	    				mensaje = String.format(mensajeNumCaracteres, nombre, maxChar);
                    } else {
                    	mensaje = String.format(mensajeRangoCaracteres, nombre, minChar, maxChar);
					}
	    			return false;
					
				} else {
					
					boolean validCaracteres = validateCampo(datoContenido);
					
					if (!validCaracteres) {
						
						if (tipo == 1) {
							mensaje = mensajeTipo1;
						} else if (tipo == 2) {
							mensaje = mensajeTipo2;
						} else if (tipo == 4) {
							mensaje = mensajeTipo4;
						} else if (tipo == 5) {
							mensaje = mensajeTipo5;
						} else if (tipo == 6) {
							mensaje = mensajeTipo6;
						}
						
						return false;
					} else {
						mensaje = "";
						return true;
					}
					
				}
				
			}
    		
		}
		
	}
	
	public boolean checkVacio() {
		
		String campo = datoContenido.trim();
		
		if (campo.length() == 0) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean checkNumChar(String contenidoCampo, int caracterMin, int caracterMax) {
		
		boolean result = false;
		String campo = contenidoCampo.trim();
		
		if (campo.length() < caracterMin || campo.length() > caracterMax) {
			result = true;
			
		}
		return result;
	}
	
	/**
     * Validate given campo with regular expression.
     * @param campo for validation
     * @return true valid campo, otherwise false
     */
    public boolean validateCampo(String contenidoCampo) {
 
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(textPattern);
 
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(contenidoCampo);
        
        return matcher.matches();
 
    }
	

}
