package net.micrositios.pqrapp.encuesta;

public class Campo {
	String id_campo = "";
	String nombre = "";
	String tamanio = "";
	String tipo = "";
	String elemento = "";

	public Campo(String id_campo, String nombre, String tamanio, String tipo,
			String elemento) {
		super();
		this.id_campo = id_campo;
		this.nombre = nombre;
		this.tamanio = tamanio;
		this.tipo = tipo;
		this.elemento = elemento;
	}

	public String getId_campo() {
		return id_campo;
	}

	public void setId_campo(String id_campo) {
		this.id_campo = id_campo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTamanio() {
		return tamanio;
	}

	public void setTamanio(String tamanio) {
		this.tamanio = tamanio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

}
