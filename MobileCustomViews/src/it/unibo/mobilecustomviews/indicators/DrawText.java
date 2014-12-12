/**
 * @author Nicholas Ricci
 */
package it.unibo.mobilecustomviews.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawText extends View{

	private int width = 100;
	private int height = 50;
	private int textSize = 25;
	private String textValue = "0";
	private int textColorValue = Color.BLACK;
	private String textName = "";
	private int textColorName = Color.BLACK;
	
	private Paint mPaint;
	
	public DrawText(Context context) {
		super(context);

		init();
	}
	
	public DrawText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}
	
	public DrawText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawText(textName + ": ", 0, height, mPaint);
		canvas.drawText(textValue, mPaint.measureText(textName) + 10, height, mPaint);
	}

	private void init(){
		mPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
		mPaint.setColor(textColorValue);
		mPaint.setTextSize(textSize);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		setMeasuredDimension(width, height);
	}

	public void setLayoutWidth(int width){
		this.width = width;
		invalidate();
	}
	public int getLayoutWidth(){
		return this.width;
	}
	
	public void setLayoutHeight(int height){
		this.height = height;
		invalidate();
	}
	public int getLayoutHeight(){
		return this.height;
	}
	
	public void setTextSize(int textSize){
		this.textSize = textSize;
		init();
		invalidate();
	}
	public int getTextSize(){
		return this.textSize;
	}
	
	public void setTextValue(String text){
		this.textValue = text;
		invalidate();
	}
	public String getTextValue(){
		return this.textValue;
	}
	
	public void setTextColorValue(int textColor){
		this.textColorValue = textColor;
		init();
		invalidate();
	}
	public int getTextColorValue(){
		return this.textColorValue;
	}
	
	public void setTextName(String textName){
		this.textName = textName;
		invalidate();
	}
	public String getTextName(){
		return this.textName;
	}
	
	public void setTextColorName(int textColor){
		this.textColorName = textColor;
		init();
		invalidate();
	}
	public int getTextColorName(){
		return this.textColorName;
	}
}
