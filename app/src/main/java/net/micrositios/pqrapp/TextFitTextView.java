package net.micrositios.pqrapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class TextFitTextView extends TextView {

	static final String TAG = "TextFitTextView";
	boolean fit = false;
	
	public TextFitTextView(Context context) {
		super(context);
	}

	public TextFitTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextFitTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setFitTextToBox( Boolean fit ) {
		this.fit = fit;
	}
	
	protected void onDraw (Canvas canvas) {
		if (fit){
			this.setHorizontallyScrolling(false);
			this.setSingleLine(false);
			DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
			this.setMaxWidth(mDisplayMetrics.widthPixels);

			_shrinkToFit();
		}
		else {
			this.setEllipsize(TruncateAt.END);
			this.setSingleLine(true);
		}
		super.onDraw(canvas);
	}
	
	protected void _shrinkToFit() {
		
		float size = this.getTextSize();

		Rect bounds = new Rect();
		Paint textPaint = this.getPaint();
		textPaint.getTextBounds(this.getText().toString(),0,this.getText().toString().length(),bounds);
		int height = bounds.height();
		int width = bounds.width();
		
		int lineas = this.getLineCount();
		
		int theight=this.getHeight();
		int twidth=this.getWidth();
		
		Log.d(TAG, "size: "+size+" boundsheight: "+height+" thisheight"+theight+" boundswidth: "+width+" thiswidth "+twidth+" lineas: "+lineas+ " "+this.getEllipsize());

		if (lineas>=2){
			this.setTextSize(size/lineas);
			_shrinkToFit();
		}
		/*else if (lineas==2){
			this.setTextSize(9*size/10);
			_shrinkToFit();
		}*7

		/*else {
			this.fit=false;
		}*/
		
		

	}
	
	protected void _growToFit() {
/*		float size = this.getTextSize();
		
		Rect bounds = new Rect();
		Paint textPaint = this.getPaint();
		textPaint.getTextBounds(this.getText().toString(),0,this.getText().toString().length(),bounds);
		int height = bounds.height();
		int width = bounds.width();

		int height = this.getHeight();
		int width = this.getWidth();
		int lines = this.getLineCount();
		Rect r = new Rect();
		int y1 = this.getLineBounds(0, r);
		//int y2 = this.getLineBounds(lines-1, r);
		
		
		float tam = size + 1.0f;
		//Log.d(TAG, y1+" "+height+" "+size+" "+tam+" "+width);

		Log.d(TAG, size+" "+height+" "+size+" "+tam+" "+width);
		if (size <= 10.0f) {
			this.setTextSize(tam);
			_growToFit();
		}
	*/	
	}
	
}