package net.micrositios.pqrapp;

public class ItemimagenInfo {
	protected long id;
	protected String titulo;
	protected String tipo;
	protected String fecha_solicitud;
	protected String fecha_actual;
	protected String uriImagen;


	public ItemimagenInfo(long id, String tipo, String titulo) {

		this.id = id;
		this.tipo = tipo;
		this.titulo = titulo;
	}

	public String getUriImagen() {
		return uriImagen;
	}

	public void setUriImagen(String uriImagen) {
		this.uriImagen = uriImagen;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getFecha_solicitud() {
		return fecha_solicitud;
	}

	public void setFecha_solicitud(String fecha_solicitud) {
		this.fecha_solicitud = fecha_solicitud;
	}

	public String getFecha_actual() {
		return fecha_actual;
	}

	public void setFecha_actual(String fecha_actual) {
		this.fecha_actual = fecha_actual;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {

	}
}
