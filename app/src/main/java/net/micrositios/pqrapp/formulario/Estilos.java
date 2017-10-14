package net.micrositios.pqrapp.formulario;

public class Estilos {

	String id_entidad = "";
	String color_barra_seccion = "";
	String color_boton = "";
	String color_boton_select = "";
	String color_texto_comentario = "";
	String color_texto_seccion = "";
	String color_texto_campos = "";
	String url_logo = "";
	String logo_local = "";
	String fecha_modificacion = "";
	String version = "";

	public Estilos(String id_entidad, String color_barra_seccion, String color_boton, String color_boton_select,
			String color_texto_comentario, String color_texto_seccion, String color_texto_campos, String url_logo,
			String fecha_modificacion, String version) {
		super();
		this.id_entidad = id_entidad;
		this.color_barra_seccion = color_barra_seccion;
		this.color_boton = color_boton;
		this.color_boton_select = color_boton_select;
		this.color_texto_comentario = color_texto_comentario;
		this.color_texto_seccion = color_texto_seccion;
		this.color_texto_campos = color_texto_campos;
		this.url_logo = url_logo;
		this.logo_local = logo_local;
		this.fecha_modificacion = fecha_modificacion;
		this.version = version;
	}

	public Estilos(String id_entidad, String color_barra_seccion, String color_boton, String color_boton_select,
			String color_texto_comentario, String color_texto_seccion, String color_texto_campos, String url_logo,
			String logo_local, String fecha_modificacion, String version) {
		super();
		this.id_entidad = id_entidad;
		this.color_barra_seccion = color_barra_seccion;
		this.color_boton = color_boton;
		this.color_boton_select = color_boton_select;
		this.color_texto_comentario = color_texto_comentario;
		this.color_texto_seccion = color_texto_seccion;
		this.color_texto_campos = color_texto_campos;
		this.url_logo = url_logo;
		this.logo_local = logo_local;
		this.fecha_modificacion = fecha_modificacion;
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getId_entidad() {
		return id_entidad;
	}

	public void setId_entidad(String id_entidad) {
		this.id_entidad = id_entidad;
	}

	public String getColor_barra_seccion() {
		return color_barra_seccion;
	}

	public void setColor_barra_seccion(String color_barra_seccion) {
		this.color_barra_seccion = color_barra_seccion;
	}

	public String getColor_boton() {
		return color_boton;
	}

	public void setColor_boton(String color_boton) {
		this.color_boton = color_boton;
	}

	public String getColor_boton_select() {
		return color_boton_select;
	}

	public void setColor_boton_select(String color_boton_select) {
		this.color_boton_select = color_boton_select;
	}

	public String getColor_texto_comentario() {
		return color_texto_comentario;
	}

	public void setColor_texto_comentario(String color_texto_comentario) {
		this.color_texto_comentario = color_texto_comentario;
	}

	public String getColor_texto_seccion() {
		return color_texto_seccion;
	}

	public void setColor_texto_seccion(String color_texto_seccion) {
		this.color_texto_seccion = color_texto_seccion;
	}

	public String getColor_texto_campos() {
		return color_texto_campos;
	}

	public void setColor_texto_campos(String color_texto_campos) {
		this.color_texto_campos = color_texto_campos;
	}

	public String getUrl_logo() {
		return url_logo;
	}

	public void setUrl_logo(String url_logo) {
		this.url_logo = url_logo;
	}

	public String getFecha_modificacion() {
		return fecha_modificacion;
	}

	public void setFecha_modificacion(String fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}

	public String getLogo_local() {
		return logo_local;
	}

	public void setLogo_local(String logo_local) {
		this.logo_local = logo_local;
	}

}
