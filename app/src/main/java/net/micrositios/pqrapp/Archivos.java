package net.micrositios.pqrapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi") public class Archivos extends AsyncTask<String, Void, String> {

	Activity act;
	String[] urls;
	//String PATH=Environment.getExternalStorageDirectory()
    //        + File.separator;
	String PATH;
	private Button mButton;
	
	long length=0;
	
	public Button getmButton() {
		return mButton;
	}

	public void setmButton(Button mButton) {
		this.mButton = mButton;
	}

	public void setAct(Activity act) {
		this.act = act;
		//this.PATH = act.getFilesDir()+File.separator;
		this.PATH =Environment.getExternalStorageDirectory()+ File.separator;
	}
	
	@Override
	protected void onPreExecute() {
		mButton.setClickable(false);
		Toast t = Toast.makeText(act, act.getString(R.string.descargando), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}

	@Override
	protected String doInBackground(String... urls) {
		this.urls=urls;
		
		String response = "";
		Log.d("ANDRO_ASYNC", "TOTAL " + urls[0]);
		for (String aurl : urls) {
			try {
				URL url = new URL(aurl.replaceAll(" ", "%20"));
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				
				File file = new File(PATH
			            + ".pqrd" //folder name
			        );
			        if (!file.exists()) {
			            file.mkdirs();
			        }
			        file = new File(PATH
				            + "pqrd" //folder name
				        );
				        if (!file.exists()) {
				            file.mkdirs();
				        }

				InputStream input = new BufferedInputStream(url.openStream());

				String fileName = aurl.substring(aurl.lastIndexOf('/') + 1);
				fileName = fileName.replaceAll(" ", "\\ ");
//				String extension = fileName.substring(fileName.lastIndexOf("."));
				String respuesta = PATH + "/pqrd/"+fileName;
				//response = PATH + "/.pqrd/"+ encriptaEnMD5(fileName)+extension;
				response = PATH + "/.pqrd/"+ fileName;
				
				OutputStream output = new FileOutputStream(response);
				OutputStream output2 = new FileOutputStream(respuesta);

				byte data[] = new byte[32];

				long total = 0;

				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					length=mButton.getWidth()*total/lenghtOfFile;
					if ((length%100)==0){
					act.runOnUiThread(new Runnable() {  
			            @Override
			            public void run() {
			            	setprogress();
			            }
			        });}
					Log.d("ANDRO_ASYNC", "TOTAL " + total);
					output.write(data, 0, count);
					output2.write(data, 0, count);
				}
				
				Log.d("ANDRO_ASYNC", "TOTAL " + response);
				Log.d("ANDRO_ASYNC", "TOTAL " + lenghtOfFile);

				output2.flush();
				output2.close();
				
				output.flush();
				output.close();
				input.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return response;

	}
	@SuppressWarnings("deprecation")
	private void setprogress() {
		final Bitmap bmResult = Bitmap.createBitmap(mButton.getWidth(), mButton.getHeight(), Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bmResult); 
	    Paint paint = new Paint();
	    LinearGradient lg1 = new LinearGradient (0, 0, 0, mButton.getHeight(), 0xFFFFFFFF, 0xFFFFFFFF, TileMode.MIRROR);
	    paint.setShader(lg1);
	    canvas.drawPaint(paint);
	    
	    Paint blackPaint = new Paint();
	    blackPaint.setColor(Color.BLACK);
	    canvas.drawRect(0, 0,  mButton.getWidth(), 1, blackPaint);
	    canvas.drawRect(0, 0,  1, mButton.getHeight(), blackPaint);

	    canvas.drawRect(0, mButton.getHeight(),  mButton.getWidth(), mButton.getHeight()-1, blackPaint);
	    canvas.drawRect(mButton.getWidth(), 0,  mButton.getWidth()-1, mButton.getHeight(), blackPaint);
	    LinearGradient lg2 = new LinearGradient (0, 0, length, 0, 0x22222222, 0x22222222, TileMode.CLAMP);
	    BlurMaskFilter bmf=new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL);
	    paint.setShader(lg2);
	    paint.setMaskFilter(bmf);
	    int sdk = android.os.Build.VERSION.SDK_INT;
	    canvas.drawRect(0, 0, length, bmResult.getHeight(), paint);
	    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	    	 mButton.setBackgroundDrawable(new BitmapDrawable(act.getResources(),bmResult));
		} else {
			mButton.setBackground(new BitmapDrawable(act.getResources(),bmResult));
		}
	    
	}

	@Override
	protected void onPostExecute(String response) {
		try {
			mButton.setClickable(true);
			mButton.setBackgroundResource(R.drawable.custom_btn_genoa);
			File file = new File(response);
			MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext=file.getName().substring(file.getName().indexOf(".")+1);
            Log.d("TIPO_ARCHIVO", ext);
            
            String type = mime.getMimeTypeFromExtension(ext);
            Log.d("TIPO_ARCHIVO", type);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),type);
            act.startActivity(Intent.createChooser(intent, act.getString(R.string.seleccione_programa)));
	} catch (Exception e) {
		e.printStackTrace();
		Toast.makeText(act, e.toString(), Toast.LENGTH_SHORT).show();
		
		for (String aurl : urls) {
			try {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(aurl));
				//act.startActivityForResult(intent, 0);
				act.startActivity(intent);
			}
			catch (Exception ex){
				ex.printStackTrace();
				Toast t = Toast.makeText(act, ex.toString(), Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();					
			}
		}
		
		}
		//Toast.makeText(act, response, Toast.LENGTH_SHORT).show();
					
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(act, requestCode+" "+resultCode+" "+data.toString(), Toast.LENGTH_SHORT).show();
		  
	}
	
	public static String encriptaEnMD5(String stringAEncriptar) {
		char[] CONSTS_HEX = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		
		try {
			MessageDigest msgd = MessageDigest.getInstance("MD5");
			byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
			StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				int bajo = (int) (bytes[i] & 0x0f);
				int alto = (int) ((bytes[i] & 0xf0) >> 4);
				strbCadenaMD5.append(CONSTS_HEX[alto]);
				strbCadenaMD5.append(CONSTS_HEX[bajo]);
			}
			return strbCadenaMD5.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	


}
