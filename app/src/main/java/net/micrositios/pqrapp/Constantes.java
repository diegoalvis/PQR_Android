package net.micrositios.pqrapp;

import android.content.Context;
import android.widget.ArrayAdapter;

public class Constantes {

	public static String[] getMunicipios(Context context, int index) {

		String[] codigosMunicipios;

		// ---Spinner View Municipios---
		switch (index) {
		case 1:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_amazonas);
			break;
		case 2:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_antioquia);
			break;
		case 3:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_arauca);
			break;
		case 4:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_atlantico);
			break;
		case 5:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_bogota);
			break;
		case 6:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_bolivar);
			break;
		case 7:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_boyaca);
			break;
		case 8:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_caldas);
			break;
		case 9:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_caqueta);
			break;
		case 10:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_casanare);
			break;
		case 11:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_cauca);
			break;
		case 12:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_cesar);
			break;
		case 13:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_choco);
			break;
		case 14:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_cordoba);
			break;
		case 15:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_cundinamarca);
			break;
		case 16:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_guainia);
			break;
		case 17:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_guajira);
			break;
		case 18:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_guaviare);
			break;
		case 19:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_huila);
			break;
		case 20:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_magdalena);
			break;
		case 21:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_meta);
			break;
		case 22:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_norte_santander);
			break;
		case 23:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_narino);
			break;
		case 24:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_putumayo);
			break;
		case 25:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_quindio);
			break;
		case 26:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_risaralda);
			break;
		case 27:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_san_andres);
			break;
		case 28:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_santander);
			break;
		case 29:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_sucre);
			break;
		case 30:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_tolima);
			break;
		case 31:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_valle_cauca);
			break;
		case 32:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_vaupes);
			break;
		case 33:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios_vichada);
			break;
		default:
			codigosMunicipios = context.getResources().getStringArray(
					R.array.codigos_municipios);
			break;
		}
		
		return codigosMunicipios;
	}

	public static ArrayAdapter<CharSequence> getMunicipio(Context context, int index) {
		ArrayAdapter<CharSequence> aMunicipio;
		switch (index) {
		case 1:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_amazonas,
					android.R.layout.simple_spinner_item);
			break;
		case 2:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_antioquia,
					android.R.layout.simple_spinner_item);
			break;
		case 3:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_arauca,
					android.R.layout.simple_spinner_item);
			break;
		case 4:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_atlantico,
					android.R.layout.simple_spinner_item);
			break;
		case 5:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_bogota,
					android.R.layout.simple_spinner_item);
			break;
		case 6:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_bolivar,
					android.R.layout.simple_spinner_item);
			break;
		case 7:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_boyaca,
					android.R.layout.simple_spinner_item);
			break;
		case 8:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_caldas,
					android.R.layout.simple_spinner_item);
			break;
		case 9:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_caqueta,
					android.R.layout.simple_spinner_item);
			break;
		case 10:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_casanare,
					android.R.layout.simple_spinner_item);
			break;
		case 11:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_cauca,
					android.R.layout.simple_spinner_item);
			break;
		case 12:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_cesar,
					android.R.layout.simple_spinner_item);
			break;
		case 13:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_choco,
					android.R.layout.simple_spinner_item);
			break;
		case 14:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_cordoba,
					android.R.layout.simple_spinner_item);
			break;
		case 15:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_cundinamarca,
					android.R.layout.simple_spinner_item);
			break;
		case 16:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_guainia,
					android.R.layout.simple_spinner_item);
			break;
		case 17:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_guajira,
					android.R.layout.simple_spinner_item);
			break;
		case 18:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_guaviare,
					android.R.layout.simple_spinner_item);
			break;
		case 19:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_huila,
					android.R.layout.simple_spinner_item);
			break;
		case 20:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_magdalena,
					android.R.layout.simple_spinner_item);
			break;
		case 21:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_meta,
					android.R.layout.simple_spinner_item);
			break;
		case 22:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_norte_santander,
					android.R.layout.simple_spinner_item);
			break;
		case 23:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_narino,
					android.R.layout.simple_spinner_item);
			break;
		case 24:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_putumayo,
					android.R.layout.simple_spinner_item);
			break;
		case 25:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_quindio,
					android.R.layout.simple_spinner_item);
			break;
		case 26:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_risaralda,
					android.R.layout.simple_spinner_item);
			break;
		case 27:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_san_andres,
					android.R.layout.simple_spinner_item);
			break;
		case 28:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_santander,
					android.R.layout.simple_spinner_item);
			break;
		case 29:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_sucre,
					android.R.layout.simple_spinner_item);
			break;
		case 30:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_tolima,
					android.R.layout.simple_spinner_item);
			break;
		case 31:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_valle_cauca,
					android.R.layout.simple_spinner_item);
			break;
		case 32:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_vaupes,
					android.R.layout.simple_spinner_item);
			break;
		case 33:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios_vichada,
					android.R.layout.simple_spinner_item);
			break;
		default:
			aMunicipio = ArrayAdapter.createFromResource(
					context,
					R.array.municipios,
					android.R.layout.simple_spinner_item);
			break;
		}
		
		return aMunicipio;
	}

}
