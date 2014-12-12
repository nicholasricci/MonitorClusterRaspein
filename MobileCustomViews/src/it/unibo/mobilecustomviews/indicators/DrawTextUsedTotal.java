/**
 * @author Nicholas Ricci
 */
package it.unibo.mobilecustomviews.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class DrawTextUsedTotal extends View{

	private int width = 100;
	private int height = 50;
	private int textSize = 25;
	private String textUsed = "0";
	private String textTotal = "0";
	private String textSlash = " / ";
	private int textColorUsed = Color.GREEN;
	private int textColorTotal = Color.RED;
	private int textColorSlash = Color.BLACK;
	private String textName = "";
	
	private Paint mPaintUsed;
	private Paint mPaintTotal;
	private Paint mPaintSlash;
	
	Rect bounds; 
	
	public DrawTextUsedTotal(Context context) {
		super(context);

		init();
	}
	
	public DrawTextUsedTotal(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}
	
	public DrawTextUsedTotal(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//canvas.drawText(textUsed + " / " + textTotal, 0, height, mPaintUsed);
		canvas.drawText(textName + ": ", 0, height, mPaintSlash);
		canvas.drawText(textUsed, mPaintSlash.measureText(textName) + 10, height, mPaintUsed);
		canvas.drawText(textSlash, mPaintSlash.measureText(textName) + mPaintUsed.measureText(textUsed) + 10, height, mPaintSlash);
		canvas.drawText(textTotal, mPaintSlash.measureText(textName) + mPaintUsed.measureText(textUsed) + mPaintSlash.measureText(textSlash) + 10, height, mPaintTotal);
	}

	private void init(){
		bounds = new Rect();
		
		mPaintUsed = new Paint(Paint.LINEAR_TEXT_FLAG);
		mPaintUsed.setColor(textColorUsed);
		mPaintUsed.setTextSize(textSize);
		
		mPaintTotal = new Paint(Paint.LINEAR_TEXT_FLAG);
		mPaintTotal.setColor(textColorTotal);
		mPaintTotal.setTextSize(textSize);
		
		mPaintSlash = new Paint(Paint.LINEAR_TEXT_FLAG);
		mPaintSlash.setColor(textColorSlash);
		mPaintSlash.setTextSize(textSize);
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
	
	public void setTextUsed(String text){
		this.textUsed = text;
		invalidate();
	}
	public String getTextUsed(){
		return this.textUsed;
	}
	
	public void setTextColorUsed(int textColor){
		this.textColorUsed = textColor;
		init();
		invalidate();
	}
	public int getTextColorUsed(){
		return this.textColorUsed;
	}
	
	public void setTextTotal(String text){
		this.textTotal = text;
		invalidate();
	}
	public String getTextTotal(){
		return this.textTotal;
	}
	
	public void setTextColorTotal(int textColor){
		this.textColorTotal = textColor;
		init();
		invalidate();
	}
	public int getTextColorTotal(){
		return this.textColorTotal;
	}
	
	public void setTextColorSlash(int textColor){
		this.textColorSlash = textColor;
		init();
		invalidate();
	}
	public int getTextColorSlash(){
		return this.textColorSlash;
	}
	
	public void setTextName(String textName){
		this.textName = textName;
		invalidate();
	}
	public String getTextName(){
		return this.textName;
	}
}
