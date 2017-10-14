package net.micrositios.pqrapp.encuesta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import net.micrositios.pqrapp.DBAdapter;
import net.micrositios.pqrapp.Propiedades;
import net.micrositios.pqrapp.R;

@SuppressLint("NewApi")
public class EncuestaActivity extends Activity {

	Context myContext;
	ArrayList<RadioGroup> respuestas_radiobuton;
	String respuestas = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encuesta);

		LinearLayout linear = (LinearLayout) findViewById(R.id.LinearLayout_encuesta);
		myContext = this;
		respuestas_radiobuton = new ArrayList<RadioGroup>();
		try {

			for (int i = 0; i < Encuesta.preguntas.size(); i++) {

				try {
					linear.addView(crear_textview(Encuesta.preguntas.get(i)));
					RadioGroup g_radio = crear_radio(Encuesta.preguntas.get(i));
					respuestas_radiobuton.add(g_radio);
					linear.addView(g_radio);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		Button envio = (Button) findViewById(R.id.button_envio_encuesta);
		try {
			GradientDrawable shape_defauld = new GradientDrawable();
			shape_defauld.setShape(GradientDrawable.RECTANGLE);
			shape_defauld.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			shape_defauld.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton()));
			shape_defauld.setStroke(0, Color.WHITE);

			BitmapDrawable textura = new BitmapDrawable(
					BitmapFactory.decodeResource(getResources(), R.drawable.textureb2));
			// TileMe.setTileModeX(TileMode.REPEAT);
			textura.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

			Drawable[] drawarray = { shape_defauld, textura };
			LayerDrawable layerdrawable = new LayerDrawable(drawarray);

			GradientDrawable shape_select = new GradientDrawable();

			shape_select.setShape(GradientDrawable.RECTANGLE);
			shape_select.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });

			shape_select.setColor(Color.parseColor(Propiedades.estilo_entidad.getColor_boton_select()));
			shape_select.setStroke(0, Color.WHITE);

			Drawable[] drawarrayselect = { shape_select, textura };
			LayerDrawable layerdrawableselect = new LayerDrawable(drawarrayselect);

			StateListDrawable states = new StateListDrawable();
			states.addState(new int[] { android.R.attr.state_pressed }, layerdrawableselect);
			states.addState(new int[] { android.R.attr.state_focused }, layerdrawableselect);
			states.addState(new int[] {}, layerdrawable);

			envio.setBackground(states);
		} catch (Exception e) {
			// TODO: handle exception
		}
		envio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("PQR", "envio");
				if (Validar()) {

					respuestas = cargar_respuestas();

					Log.i("prueba encuesta", "Encuesta respuesta: " + respuestas);

					WS_Rest_client ws = new WS_Rest_client(myContext, WS_Rest_client.Enviar_respuesta);

					Handler puente_envio = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 1) {

								Toast.makeText(myContext, "Envio exitoso", Toast.LENGTH_SHORT).show();
								Intent i = getIntent();

								setResult(RESULT_OK, i);
								finish();

								finish();

							} else if (msg.what == 0) {

								Intent i = getIntent();

								setResult(Activity.RESULT_CANCELED, i);
								finish();

							}
						}
					};
					Log.d("PQR", "respuesta " + respuestas);
					ws.setPuente_envio(puente_envio);
					ws.Enviar_respuesta("1", respuestas);
					ws.execute();

				} else {

					// Toast mensaje = new Toast(context);
					//
					// mensaje.setText("Conteste todas las preguntas");
					//
					// mensaje.setDuration(Toast.LENGTH_SHORT);
					// mensaje.setGravity(Gravity.CENTER_HORIZONTAL
					// | Gravity.CENTER_VERTICAL, 0, 0);
					//
					// mensaje.show();
					Toast.makeText(myContext, "Conteste todas las preguntas", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void updateSolicitud(String codigo, String respuest_encuesta) {

		// ---update solicitud---

		DBAdapter db = new DBAdapter(myContext);

		try {

			Log.d("DATABASE", db.toString());

			db.open();

			if (db.updateSolicitud(codigo, respuest_encuesta)) {
				Log.d("PQR", "actualizacion");
			}
		} catch (Exception ex) {
		} finally {
			db.close();
		}

	}

	public boolean Validar() {
		boolean valido = true;

		// respuestas_radiobuton
		for (int i = 0; i < respuestas_radiobuton.size(); i++) {

			if (respuestas_radiobuton.get(i).getCheckedRadioButtonId() != -1) {

			} else {
				Log.d("PQR", "encuesta no valida envio");
				valido = false;
				break;
			}

		}

		return valido;

	}

	public String cargar_respuestas() {
		String respuestas = "";
		JSONArray array = new JSONArray();
		JSONObject json_respuestas = new JSONObject();

		for (int i = 0; i < respuestas_radiobuton.size(); i++) {
			int id_check = respuestas_radiobuton.get(i).getCheckedRadioButtonId();
			View radioButton = respuestas_radiobuton.get(i).findViewById(id_check);
			int radioId = respuestas_radiobuton.get(i).indexOfChild(radioButton);

			Log.i("prueba encuesta", "id: " + radioId);

			if (id_check != -1) {

				JSONObject json_resp = new JSONObject();

				try {
					json_resp.put("id_solicitud", Propiedades.codigo_consulta);
					json_resp.put("respuesta", Encuesta.preguntas.get(i).getRespuestas().get(radioId).gettexto());
					json_resp.put("id_pregunta", Encuesta.preguntas.get(i).getId());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array.put(json_resp);
			}
			try {
				json_respuestas.put("respuestas", array);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return json_respuestas.toString();

	}

	public TextView crear_textview(Pregunta pre) {

		TextView textv = new TextView(myContext);
		textv.setText(pre.gettexto());

		// edit_text.setmax(Integer.valueOf(campo.tamanio));
		return textv;
	}

	public RadioGroup crear_radio(Pregunta pre) {

		RadioGroup radiogroup = new RadioGroup(myContext);
		if (pre.getRespuestas().size() < 3) {

			radiogroup.setOrientation(RadioGroup.HORIZONTAL);

		} else {
			radiogroup.setOrientation(RadioGroup.VERTICAL);
		}
		ArrayList<Respuesta> respuestas = pre.getRespuestas();
		String textos = "";
		for (int i = 0; i < respuestas.size(); i++) {
			Respuesta respuesta = respuestas.get(i);

			RadioButton radio = new RadioButton(myContext);

			radio.setText(respuesta.gettexto());
			textos += respuesta.gettexto();

			radiogroup.addView(radio);

		}

		if (textos.length() <= (2 * respuestas.size()) && respuestas.size() < 6) {

			radiogroup.setOrientation(RadioGroup.HORIZONTAL);
		}

		// edit_text.setmax(Integer.valueOf(campo.tamanio));
		return radiogroup;
	}

}
