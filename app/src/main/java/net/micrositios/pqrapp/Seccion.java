package net.micrositios.pqrapp;

import android.widget.LinearLayout;

public class Seccion {
	String Seccion = "";
	LinearLayout Layoutseccion;
	Boolean visible = true;

	public Seccion(String seccion, LinearLayout layoutseccion, Boolean visible) {
		super();
		Seccion = seccion;
		Layoutseccion = layoutseccion;
		this.visible = visible;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getSeccion() {
		return Seccion;
	}

	public void setSeccion(String seccion) {
		Seccion = seccion;
	}

	public LinearLayout getLayoutseccion() {

		return Layoutseccion;

	}

	public void setLayoutseccion(LinearLayout layoutseccion) {

		Layoutseccion = layoutseccion;

	}

}
