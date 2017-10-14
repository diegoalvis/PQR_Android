package net.micrositios.pqrapp.encuesta;

public class Item {
	String id = "";
	String texto = "";

	public Item(String id, String texto) {
		super();
		this.id = id;
		this.texto = texto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String gettexto() {
		return texto;
	}

	public void settexto(String texto) {
		this.texto = texto;
	}

}
