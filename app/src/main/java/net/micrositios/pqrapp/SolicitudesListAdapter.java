package net.micrositios.pqrapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class SolicitudesListAdapter extends CursorAdapter {

	private Context myContext;
	private int myLayout;
	private int etiquetas = 0;

	private int SIZE = 11;
	private int SIZE_ICON = 84;

	public SolicitudesListAdapter(Context context, Cursor cursor, boolean autoRequery) {
		super(context, cursor, autoRequery);
		myContext = context;
	}

	public SolicitudesListAdapter(Context context, Cursor cursor, boolean autoRequery, int layout) {
		super(context, cursor, autoRequery);
		myLayout = layout;
		myContext = context;
	}

	public SolicitudesListAdapter(Context context, Cursor cursor, boolean autoRequery, int layout, int etiquetas) {
		super(context, cursor, autoRequery);
		myLayout = layout;
		myContext = context;
		this.etiquetas = etiquetas;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "SimpleDateFormat", "DefaultLocale" })
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		getSize();

		TextView entidadTextView = (TextView) view.findViewById(R.id.entidad_view);
		entidadTextView.setTextSize(getSize());

		TextView codigoTextView = (TextView) view.findViewById(R.id.codigo_view);
		// codigoTextView.setFitTextToBox(true);

		TextView fechaTextView = (TextView) view.findViewById(R.id.fecha_view);
		// fechaTextView.setFitTextToBox(true);

		// TextView horaTextView = (TextView) view.findViewById(R.id.hora_view);
		// horaTextView.setFitTextToBox(true);

		TextView fechahoraTextView = (TextView) view.findViewById(R.id.fechahora_view);

		TextView estadoTextView = (TextView) view.findViewById(R.id.estado_view);
		// estadoTextView.setFitTextToBox(true);

		final TextView objetoTextView = (TextView) view.findViewById(R.id.tipo_solicitud_view);
		// objetoTextView.setFitTextToBox(true);

		if (etiquetas == 0) {
			entidadTextView.setText(myContext.getString(R.string.etiqueta_entidad) + " "
					+ cursor.getString(cursor.getColumnIndex("entidad")));
			codigoTextView.setText(myContext.getString(R.string.etiqueta_codigo) + " "
					+ cursor.getString(cursor.getColumnIndex("codigo")));
			fechaTextView.setText(myContext.getString(R.string.etiqueta_fecha) + " "
					+ cursor.getString(cursor.getColumnIndex("fecha")));
			estadoTextView.setText(myContext.getString(R.string.etiqueta_estado) + " "
					+ cursor.getString(cursor.getColumnIndex("estado")));
			objetoTextView.setText(myContext.getString(R.string.etiqueta_tipo_solicitud) + " "
					+ cursor.getString(cursor.getColumnIndex("nombre_tipo_solicitud")));
		} else if (etiquetas == 1) {
			entidadTextView.setText(myContext.getString(R.string.etiqueta_entidad));
			codigoTextView.setText(myContext.getString(R.string.etiqueta_codigo));
			fechaTextView.setText(myContext.getString(R.string.etiqueta_fecha));
			estadoTextView.setText(myContext.getString(R.string.etiqueta_estado));
			objetoTextView.setText(myContext.getString(R.string.etiqueta_tipo_solicitud));
		} else if (etiquetas == 2) {
			entidadTextView.setText(cursor.getString(cursor.getColumnIndex("entidad")));
			entidadTextView.setTypeface(null, Typeface.BOLD);
			entidadTextView.setTextSize(getSize() + 2);

			codigoTextView.setText(cursor.getString(cursor.getColumnIndex("codigo")));
			codigoTextView.setTextSize(getSize());

			estadoTextView.setText(cursor.getString(cursor.getColumnIndex("estado")));
			estadoTextView.setTextSize(getSize());

			objetoTextView.setText(cursor.getString(cursor.getColumnIndex("nombre_tipo_solicitud")));
			objetoTextView.setTextSize(getSize());

			try {

				Date fechahora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(cursor.getString(cursor.getColumnIndex("fecha")));
				Date hoy = new Date();

				int diffInDays = (int) ((hoy.getTime() - fechahora.getTime()) / (1000 * 60 * 60 * 24));

				if (diffInDays == 0) {
					String hora = new SimpleDateFormat("hh:mm a").format(fechahora);
					fechahoraTextView.setText(hora);

				} else {
					String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechahora);
					fechahoraTextView.setText(fecha);
				}

				// Log.d("FECHA-HORA",hoy+" "+fechahora+" "+diffInDays);
				fechahoraTextView.setTextSize(getSize());
				fechahoraTextView.setTextColor(Color.GRAY);

			} catch (ParseException e) {
				Log.d("ParseException", e.toString());

				e.printStackTrace();
			}

		} else if (etiquetas == 3) {
			String str = cursor.getString(cursor.getColumnIndex("nombre_tipo_solicitud"));

			TextView tv_solicitudes_eg_icono = (TextView) view.findViewById(R.id.tv_solicitudes_eg_icono);
			try {
				tv_solicitudes_eg_icono.setText(" " + str.charAt(0) + " ");
			} catch (Exception e) {
				// TODO: handle exception
			}

			tv_solicitudes_eg_icono.setWidth(SIZE_ICON);
			tv_solicitudes_eg_icono.setHeight(SIZE_ICON);
			int color = (int) (184236);
			try {
				color = (int) (184236 * (int) str.toUpperCase().charAt(0) - 65);
			} catch (Exception e) {
				// TODO: handle exception
			}

			ColorDrawable cd = new ColorDrawable(-color);
			cd.setAlpha(150);
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				tv_solicitudes_eg_icono.setBackgroundDrawable(cd);
			} else {
				tv_solicitudes_eg_icono.setBackground(cd);
			}

			entidadTextView.setText(cursor.getString(cursor.getColumnIndex("entidad")));
			entidadTextView.setTypeface(null, Typeface.BOLD);
			entidadTextView.setTextSize(getSize() + 2);

			codigoTextView.setText(cursor.getString(cursor.getColumnIndex("codigo")));
			codigoTextView.setTextSize(getSize());

			// estadoTextView.setText(cursor.getString(cursor.getColumnIndex("estado")));
			estadoTextView.setTextSize(getSize());

			objetoTextView.setText(str + " - " + cursor.getString(cursor.getColumnIndex("estado")));
			objetoTextView.setTextSize(getSize());

			try {

				Date fechahora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(cursor.getString(cursor.getColumnIndex("fecha")));
				Date hoy = new Date();

				int diffInDays = (int) ((hoy.getTime() - fechahora.getTime()) / (1000 * 60 * 60 * 24));

				if (diffInDays == 0) {
					String hora = new SimpleDateFormat("hh:mm a").format(fechahora);
					fechahoraTextView.setText(hora);

				} else {
					String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechahora);
					fechahoraTextView.setText(fecha);
				}

				// Log.d("FECHA-HORA",hoy+" "+fechahora+" "+diffInDays);
				fechahoraTextView.setTextSize(getSize());
				fechahoraTextView.setTextColor(Color.GRAY);

			} catch (ParseException e) {
				Log.d("ParseException", e.toString());

				e.printStackTrace();
			}

		} else {
			if (cursor.getString(cursor.getColumnIndex("_id")).contentEquals("-1")) {
				entidadTextView.setTypeface(null, Typeface.BOLD);
				codigoTextView.setTypeface(null, Typeface.BOLD);
				fechaTextView.setTypeface(null, Typeface.BOLD);
				estadoTextView.setTypeface(null, Typeface.BOLD);
				objetoTextView.setTypeface(null, Typeface.BOLD);
			}

			entidadTextView.setText("\n" + cursor.getString(cursor.getColumnIndex("siglaEntidad")) + "\n");
			codigoTextView.setText("\n" + cursor.getString(cursor.getColumnIndex("codigo")) + "\n");
			fechaTextView.setText("\n" + cursor.getString(cursor.getColumnIndex("fecha")) + "\n");
			estadoTextView.setText("\n" + cursor.getString(cursor.getColumnIndex("estado")) + "\n");
			objetoTextView.setText("\n" + cursor.getString(cursor.getColumnIndex("nombre_tipo_solicitud")) + "\n");
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private int getSize() {
		int Measuredwidth = 0;
		int Measuredheight = 0;
		Point size = new Point();
		WindowManager w = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);

			Measuredwidth = size.x;
			Measuredheight = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			Measuredwidth = d.getWidth();
			Measuredheight = d.getHeight();
		}

		float densidad = myContext.getResources().getDisplayMetrics().density;

		// Log.d("DENSIDAD",densidad+" Width "+Measuredwidth+"
		// Height:"+Measuredheight);

		if (densidad == 0.75f) {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE_ICON = 40;
				SIZE = 11;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 15;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 22;
			} else {
				SIZE = 30;
			}

		} else if (densidad == 1.0f) {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 8;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE_ICON = 60;
				SIZE = 11;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 16;
			} else {
				SIZE = 18;
			}

		} else if (densidad == 1.5f) {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 4;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 7;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 10;
			} else {
				SIZE = 17;
			}

		} else {
			if (Measuredwidth <= 240 || Measuredheight <= 432) {
				SIZE = 2;
			} else if (Measuredwidth <= 320 || Measuredheight <= 480) {
				SIZE = 4;
			} else if (Measuredwidth <= 480 || Measuredheight <= 860) {
				SIZE = 8;
			} else {
				SIZE = 11;
				SIZE_ICON = 110;
			}
		}
		return SIZE;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		final View view = inflater.inflate(myLayout, parent, false);

		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}
