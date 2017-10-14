package net.micrositios.pqrapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.micrositios.pqrapp.encuesta.log_aplicacion;

import java.util.ArrayList;

public class ItemImagenAdapter extends BaseAdapter {
	protected Activity activity;
	protected ArrayList<ItemimagenInfo> items;

	static class ViewHolder {
		public ImageView icon;
		public TextView titulo;
		public TextView fecha_actual;
		public TextView fecha_solicitud;
	}

	log_aplicacion log;

	public ItemImagenAdapter(Activity activity, ArrayList<ItemimagenInfo> items) {
		this.activity = activity;
		this.items = items;
		log = new log_aplicacion();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder viewholder;
		// if (convertView == null) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi = inflater.inflate(R.layout.lista_imagen, null);
		// viewholder = new ViewHolder();
		// // viewholder.icon = (ImageView) vi
		// // .findViewById(R.id.imagen_solicitud);
		//
		// viewholder.titulo = (TextView) vi.findViewById(R.id.titulo);
		//
		// // viewholder.fecha_actual = (TextView) vi
		// // .findViewById(R.id.textfecha_actual);
		// //
		// // viewholder.fecha_solicitud = (TextView) vi
		// // .findViewById(R.id.textfecha_solicitud);
		//
		// vi.setTag(viewholder);
		// }

		ItemimagenInfo item = items.get(position);

		// viewholder = (ViewHolder) vi.getTag();


//		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		ImageView icon = (ImageView) vi.findViewById(R.id.imagen_solicitud);
		Picasso.with(activity)
				.load(item.getUriImagen())
				.resize(50, 50)
				.centerCrop()
				.into(icon);

//		File f = new File(item.getUriImagen());
//		log.log_informacion("uri local in" + item.getUriImagen());
//		if (f.exists()) {
//			Bitmap bitmap = null;
//			try {
//				log.log_informacion("uri local in" + item.getUriImagen());
//				bitmap = BitmapFactory.decodeFile(item.getUriImagen());
//				bitmap.prepareToDraw();
//				int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
//				bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
//
//				icon.setImageBitmap(bitmap);
//				icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//
//		} else {
//			icon.setImageBitmap(null);
//
//		}

		TextView titulo = (TextView) vi.findViewById(R.id.titulo);
		titulo.setText(item.getTitulo());

		// TextView fecha_actual = (TextView)
		// vi.findViewById(R.id.textfecha_actual);
		// viewholder.fecha_actual.setText(item.getFecha_actual());

		// TextView fecha_solicitud = (TextView)
		// vi.findViewById(R.id.textfecha_solicitud);
		// viewholder.fecha_solicitud.setText(item.getFecha_solicitud());

		return vi;
	}
}
