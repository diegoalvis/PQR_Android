package net.micrositios.pqrapp.formulario;

public class Proceso {
	String id = "";
	String nombre = "";
	String id_formulario = "";

	public Proceso(String id, String nombre, String id_formulario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.id_formulario = id_formulario;
	}

	public String getId_formulario() {
		return id_formulario;
	}

	public void setId_formulario(String id_formulario) {
		this.id_formulario = id_formulario;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
