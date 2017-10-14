package net.micrositios.pqrapp.encuesta;

import java.util.ArrayList;

public class Pregunta extends Item {

	public Pregunta(String id, String texto) {
		super(id, texto);
		// TODO Auto-generated constructor stub
	}

	ArrayList<Respuesta> respuestas = new ArrayList<Respuesta>();
	String id_respuesta = "";

	public ArrayList<Respuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(ArrayList<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	public String getId_respuesta() {
		return id_respuesta;
	}

	public void setId_respuesta(String id_respuesta) {
		this.id_respuesta = id_respuesta;
	}

}
