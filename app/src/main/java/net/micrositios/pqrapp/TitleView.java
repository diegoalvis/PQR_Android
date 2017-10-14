package net.micrositios.pqrapp;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TitleView extends View {
	
	private final Bitmap titleGraphic;
	private final Bitmap tagLine;
	private int screenW;
	private int screenH;
	private final Bitmap informacionButtonUp;
	private final Bitmap informacionButtonDown;
	private final Bitmap crearButtonUp;
	private final Bitmap crearButtonDown;
	private final Bitmap consultarButtonUp;
	private final Bitmap consultarButtonDown;
	private boolean infoButtonPressed;
	private boolean crearButtonPressed;
	private boolean consultarButtonPressed;
	private final Context myContext;
	
	private int scaledButtonW;
	private int scaledButtonH;
	private int scaledTagLineW;
	private int scaledTagLineH;
	private Bitmap infoButtonUpScaled;
	private Bitmap infoButtonDownScaled;
	private Bitmap crearButtonUpScaled;
	private Bitmap crearButtonDownScaled;
	private Bitmap consultarButtonUpScaled;
	private Bitmap consultarButtonDownScaled;
	private Bitmap tagLineScaled;
	
	private int distX, distX2, distX3;

	public TitleView(Context context) {
		super(context);
		myContext = context;
		titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
		tagLine = BitmapFactory.decodeResource(getResources(), R.drawable.tag_line);
		
		informacionButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.informacion_button_up);
		informacionButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.informacion_button_down);
		crearButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.crear_button_up);
		crearButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.crear_button_down);
		consultarButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.consultar_button_up);
		consultarButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.consultar_button_down);		
		context.setTheme(android.R.style.Theme_Black);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		
		scaledButtonW = screenW/5;
		scaledButtonH = scaledButtonW*1;
		scaledTagLineW = (int) (screenW*0.8);
		scaledTagLineH = (int) (scaledTagLineW*0.2075);
		
		distX = screenW / 10;
		distX2 = 4 * distX;
		distX3 = 7 * distX;
		
		tagLineScaled = Bitmap.createScaledBitmap(tagLine, scaledTagLineW, scaledTagLineH, false);
		
		infoButtonUpScaled = Bitmap.createScaledBitmap(informacionButtonUp, scaledButtonW, scaledButtonH, false);
		infoButtonDownScaled = Bitmap.createScaledBitmap(informacionButtonDown, scaledButtonW, scaledButtonH, false);
		crearButtonUpScaled = Bitmap.createScaledBitmap(crearButtonUp, scaledButtonW, scaledButtonH, false);
		crearButtonDownScaled = Bitmap.createScaledBitmap(crearButtonDown, scaledButtonW, scaledButtonH, false);
		consultarButtonUpScaled = Bitmap.createScaledBitmap(consultarButtonUp, scaledButtonW, scaledButtonH, false);
		consultarButtonDownScaled = Bitmap.createScaledBitmap(consultarButtonDown, scaledButtonW, scaledButtonH, false);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawBitmap(titleGraphic, (screenW - titleGraphic.getWidth()) / 2, (int) (screenH * 0.05), null);
		canvas.drawBitmap(tagLineScaled, (screenW - tagLineScaled.getWidth()) / 2, (int) (screenH * 0.1) + titleGraphic.getHeight(), null);
		
		
		if (infoButtonPressed){
			canvas.drawBitmap(infoButtonDownScaled, distX, (int)(screenH*0.95) - infoButtonDownScaled.getHeight(), null);
		}
		else{
			canvas.drawBitmap(infoButtonUpScaled, distX, (int)(screenH*0.95) - infoButtonUpScaled.getHeight(), null);
		}
		
		if (crearButtonPressed){
			canvas.drawBitmap(crearButtonDownScaled, distX2, (int)(screenH*0.95) - crearButtonDownScaled.getHeight(), null);
		}
		else{
			canvas.drawBitmap(crearButtonUpScaled, distX2  , (int)(screenH*0.95) - crearButtonUpScaled.getHeight(), null);
		}
		if (consultarButtonPressed){
			canvas.drawBitmap(consultarButtonDownScaled, distX3, (int)(screenH*0.95) - consultarButtonDownScaled.getHeight(), null);
		}
		else{
			canvas.drawBitmap(consultarButtonUpScaled, distX3, (int)(screenH*0.95) - consultarButtonUpScaled.getHeight(), null);
		}
		
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event){
		int eventaction = event.getAction();
		int X = (int)event.getX();
		int Y = (int)event.getY();
		
		switch (eventaction){
		
		case MotionEvent.ACTION_DOWN:
			if (X > distX && X < (3 * distX) && Y > (int)(screenH*0.95) - scaledButtonH && Y < (int)(screenH*0.95))
			{
				infoButtonPressed = true;
			}
			if (X > (distX2) && X < (6 * distX) && Y > (int)(screenH*0.95) - scaledButtonH && Y < (int)(screenH*0.95))
			{
				crearButtonPressed = true;
			}
			if (X > distX3 && X < (9 * distX) && Y > (int)(screenH*0.95) - scaledButtonH && Y < (int)(screenH*0.95))
			{
				consultarButtonPressed = true;
			}
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			break;
			
		case MotionEvent.ACTION_UP:
			if (infoButtonPressed){
				
				showInstruccionesDialog();
				
			}
			infoButtonPressed = false;
			if (crearButtonPressed){
				Intent formularioIntent = new Intent(myContext, SeleccionarEntidad.class);
				myContext.startActivity(formularioIntent);
			}
			crearButtonPressed = false;
			if (consultarButtonPressed){
				Intent consultarIntent = new Intent(myContext, SolicitudesListActivity.class);
				myContext.startActivity(consultarIntent);
			}
			consultarButtonPressed = false;
			break;		
		}
		invalidate();
		return true;
		
	}
	
	private void showInstruccionesDialog() {
		final Dialog instruccionesDialog = new Dialog(myContext);
		instruccionesDialog.setContentView(R.layout.instrucciones_dialog);
		instruccionesDialog.setTitle(myContext.getString(R.string.informacion_dialogo_titulo));
		//instruccionesDialog.setCancelable(false);

		final ScrollView sv_instrucciones = (ScrollView) instruccionesDialog.findViewById(R.id.sv_instrucciones);
		final ScrollView sv_desarrolladores = (ScrollView) instruccionesDialog.findViewById(R.id.sv_desarrolladores);
		final TextView tv_creditos = (TextView) instruccionesDialog.findViewById(R.id.tv_creditos);
		
		tv_creditos.setText(tv_creditos.getText().toString()+" v"+Propiedades.VERSION);
		
		Button btn_instrucciones = (Button) instruccionesDialog.findViewById(R.id.btn_instrucciones);
		btn_instrucciones.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sv_instrucciones.getVisibility()==View.VISIBLE){
					sv_instrucciones.setVisibility(View.GONE);
				}
				else {
					sv_instrucciones.setVisibility(View.VISIBLE);
					tv_creditos.setVisibility(View.GONE);
					sv_desarrolladores.setVisibility(View.GONE);

				}
			}
		});
		
		
		Button btn_desarrolladores = (Button) instruccionesDialog.findViewById(R.id.btn_desarrolladores);
		btn_desarrolladores.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sv_desarrolladores.getVisibility()==View.VISIBLE){
					sv_desarrolladores.setVisibility(View.GONE);
				}
				else {
					tv_creditos.setVisibility(View.GONE);

					sv_desarrolladores.setVisibility(View.VISIBLE);
					sv_instrucciones.setVisibility(View.GONE);
				}
			}
		});
		
		Button btn_creditos = (Button) instruccionesDialog.findViewById(R.id.btn_creditos);
		btn_creditos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (tv_creditos.getVisibility()==View.VISIBLE){
					tv_creditos.setVisibility(View.GONE);
				}
				else {

					tv_creditos.setVisibility(View.VISIBLE);
					sv_desarrolladores.setVisibility(View.GONE);
					sv_instrucciones.setVisibility(View.GONE);
				}
			}

		});
		
		Button okButton = (Button) instruccionesDialog.findViewById(R.id.btnDone);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				instruccionesDialog.dismiss();
				
			}
		});
		
		okButton.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				borrarterminosycondiciones();
				return false;
			}
		});
		instruccionesDialog.show();
	}
	protected void borrarterminosycondiciones() {
    	SharedPreferences sp_perfil = myContext.getSharedPreferences("configuracion", Context.MODE_PRIVATE);
		Editor spe_perfil = sp_perfil.edit();
		spe_perfil.putString("aceptaterminos", "no");
		spe_perfil.commit();
		Toast.makeText(myContext, "OK", Toast.LENGTH_SHORT).show();
	}
	

}
