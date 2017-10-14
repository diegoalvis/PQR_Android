package net.micrositios.pqrapp;

import java.util.ArrayList;
import java.util.List;

import net.micrositios.pqrapp.formulario.Campo;
import net.micrositios.pqrapp.formulario.Lista_spinner;
import net.micrositios.pqrapp.formulario.Proceso;
import android.app.Application;

public class MyApplication extends Application {

	public static ArrayList<Campo> campos = new ArrayList<Campo>();
	public static ArrayList<Lista_spinner> listas = new ArrayList<Lista_spinner>();
	public static List<Proceso> procesos = new ArrayList<Proceso>();

}
