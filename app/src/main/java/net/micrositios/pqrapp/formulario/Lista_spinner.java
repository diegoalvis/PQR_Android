package net.micrositios.pqrapp.formulario;

import java.util.ArrayList;

public class Lista_spinner {
	String nombre = "";
	ArrayList<Item> listaArrayList = new ArrayList<Item>();

	public Lista_spinner(String nombre, ArrayList<Item> listaArrayList) {
		super();
		this.nombre = nombre;
		this.listaArrayList = listaArrayList;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<Item> getListaArrayList() {
		return listaArrayList;
	}

	public void setListaArrayList(ArrayList<Item> listaArrayList) {
		this.listaArrayList = listaArrayList;
	}

	public void add_item(Item item) {

		this.listaArrayList.add(item);

	}

}
