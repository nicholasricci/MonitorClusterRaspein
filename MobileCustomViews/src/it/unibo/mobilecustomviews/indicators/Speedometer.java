/**
 * @author Nicholas Ricci
 */
package it.unibo.mobilecustomviews.indicators;

import it.unibo.mobilecustomviews.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

public class Speedometer extends View{

	//Variable useful for temperature
	private float minValue = 0;
	private float maxValue = 100;
	private float middleValue = (maxValue + minValue) / 2;
	private float currentValue = middleValue;
	private int colorMinValue = Color.WHITE;
	private int colorMaxValue = Color.GRAY;
	
	//Variable useful for line min max and current value
	private int colorLineMinMax = Color.BLACK;
	private int colorLineCurrent = Color.WHITE;
	private int lineWidth = 3;
	
	//Variable useful for set the view
	private int width = 50;
	private int height = 200;
	
	//Variable for text
	private float textSize = 20;
	private int textColor = Color.BLACK;
	
	//Variable useful for paint and draw
	Paint mTextPaint;
	Paint mLineMinMax;
	Paint mLineCurrent;
	Paint mCirclePaint;
	//Rect mRect;
	RectF mOvalExternal;
	RectF mOvalInternal;
	GradientDrawable mDrawable;
	
	public Speedometer(Context context) {
		super(context);

		init();
	}
	
	public Speedometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		defAttribute(context, attrs);
		init();
	}
	
	public Speedometer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		defAttribute(context, attrs);
		init();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float realHeightToDraw = height * 3 / 4;
		float marginTop = height / 4 / 2;
		float realHalfHeightToDraw = realHeightToDraw / 2 + marginTop;
		float realHeightSup = marginTop + realHeightToDraw / 8;
		float realHeightInf = marginTop + realHeightToDraw / 8 * 7;
		float realHeight = realHeightInf - realHeightSup;
		
		float realWidthToDraw = width * 3 / 4;
		float marginLeft = width / 4 / 2;
		float realHalfWidthToDraw = realWidthToDraw / 2 + marginLeft;
		float realWidthLeft = marginLeft + realWidthToDraw / 8;
		float realWidthRight = marginLeft + realWidthToDraw / 8 * 7;
		float realWidth = realWidthRight - realWidthLeft;
		
		//mRect.set(0, (int)realHeightSup, 
		//		(int)realWidthToDraw, (int)realHeightInf);
		
		mOvalExternal.set(realHalfWidthToDraw - realWidth / 2, 
				realHalfHeightToDraw - realHeight / 2,
				realHalfWidthToDraw + realWidth / 2, 
				realHalfHeightToDraw + realHeight / 2);
		
		mOvalInternal.set(realHalfWidthToDraw - realWidth / 2 + marginLeft / 4, 
				realHalfHeightToDraw - realHeight / 2 + marginTop / 4,
				realHalfWidthToDraw + realWidth / 2 - marginLeft / 4, 
				realHalfHeightToDraw + realHeight / 2 - marginTop / 4);
		
		//mDrawable.setBounds(mRect);
		
		//draw circle
		mCirclePaint.setColor(colorMaxValue);
		//canvas.drawCircle(realHalfWidthToDraw, realHalfHeightToDraw, realWidth / 2, mCirclePaint);
		canvas.drawOval(mOvalExternal, mCirclePaint);
		//draw gradient
		//canvas.save();
		mCirclePaint.setColor(colorMinValue);
		canvas.drawArc(mOvalInternal, -180, 180, true, mCirclePaint);
		//mDrawable.draw(canvas);
		//canvas.restore();
		
		//draw text
		canvas.drawText(maxValue + "", width - mTextPaint.measureText(maxValue + ""), 
				realHalfHeightToDraw + mTextPaint.descent() - mTextPaint.ascent(), mTextPaint);
		canvas.drawText(minValue + "", 0,
				realHalfHeightToDraw + mTextPaint.descent() - mTextPaint.ascent(), mTextPaint);
		canvas.drawText(middleValue + "", realHalfWidthToDraw - mTextPaint.measureText(middleValue + "") / 2, 
				marginTop / 2 + textSize / 2, mTextPaint);
		
		//draw line
		float cos = (float)Math.cos(Math.toRadians(currentValue * (180 / maxValue)));
		float sin = (float)Math.sin(Math.toRadians(currentValue * (180 / maxValue)));
		//if (cos < 0){
			canvas.drawLine(realHalfWidthToDraw, realHalfHeightToDraw, 
					realHalfWidthToDraw - cos * (realWidth - marginLeft / 2) / 2, 
					realHalfHeightToDraw - sin * (realHeight - marginTop / 2) / 2, mLineMinMax);
		/*}else{
			canvas.drawLine(realHalfWidthToDraw, realHalfHeightToDraw, 
					realHalfWidthToDraw - cos * (realWidth - marginLeft / 2) / 2, 
					realHalfHeightToDraw - sin * (realHeight - marginTop / 2) / 2, mLineMinMax);
		}*/
			
		//draw current value text
		canvas.drawText(currentValue + "", realHalfWidthToDraw - mTextPaint.measureText(currentValue + "") / 2, 
				realHalfHeightToDraw - marginTop + textSize / 2, mTextPaint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		setMeasuredDimension(width, height);
	}

	private void defAttribute(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Speedometer);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.Speedometer_speedometer_width:
				width = a.getDimensionPixelSize(attr, 50);
				break;
			case R.styleable.Speedometer_speedometer_height:
				height = a.getDimensionPixelSize(attr, 100);
				break;
			case R.styleable.Speedometer_speedometer_color_min_value:
				colorMinValue = a.getColor(attr, Color.GREEN);
				break;
			case R.styleable.Speedometer_speedometer_color_max_value:
				colorMaxValue = a.getColor(attr, Color.RED);
				break;
			case R.styleable.Speedometer_speedometer_min_value:
				minValue = a.getFloat(attr, 0);
				break;
			case R.styleable.Speedometer_speedometer_max_value:
				maxValue = a.getFloat(attr, 100);
				break;	
			case R.styleable.Speedometer_speedometer_text_size:
				textSize = a.getDimension(attr, 25);
				break;
			case R.styleable.Speedometer_speedometer_text_color:
				textColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.Speedometer_speedometer_color_line_min_max:
				colorLineMinMax = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.Speedometer_speedometer_color_line_current:
				colorLineCurrent = a.getColor(attr, Color.WHITE);
				break;
			case R.styleable.Speedometer_speedometer_line_width:
				lineWidth = a.getInt(attr, 3);
				break;
			default:
				break;
			}
		}
		a.recycle();
	}
	
	private void init(){
		
		mOvalExternal = new RectF(0, 0, width, height);
		mOvalInternal = new RectF(0, 0, width, height);
		//mRect = new Rect(0, height / 8, width + 1, height / 8 * 7);
		
		//init the value of text paint
		mTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
		mTextPaint.setColor(textColor);
		mTextPaint.setTextSize(textSize);
		//init the value of Line Min Max
		mLineMinMax = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLineMinMax.setStyle(Style.FILL_AND_STROKE);
		mLineMinMax.setColor(colorLineMinMax);
		mLineMinMax.setStrokeWidth(lineWidth);
		//init the value of Line Current
		mLineCurrent = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLineCurrent.setStyle(Style.FILL_AND_STROKE);
		mLineCurrent.setColor(colorLineCurrent);
		mLineCurrent.setStrokeWidth(lineWidth);
		//init the value of Circle Paint
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Style.FILL);
		mCirclePaint.setColor(Color.BLACK);
		mCirclePaint.setStrokeWidth(lineWidth);
		//init the value of Drawable
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { colorMinValue, colorMaxValue });
		mDrawable.setShape(GradientDrawable.RADIAL_GRADIENT);
	}
	
	//set the current value of temperature
	public void setCurrentSpeed(float currentTemperature){
		this.currentValue = currentTemperature;
		invalidate();
	}
	public float getCurrentSpeed(){
		return this.currentValue;
	}
	
	//set width for temperature
	public void setWidthSpeedometer(int width){
		this.width = width;
		invalidate();
	}
	public int getWidthSpeedometer(){
		return this.width;
	}
	
	//set the height for temperature
	public void setHeightSpeedometer(int height){
		this.height = height;
		invalidate();
	}
	public int getHeightSpeedometer(){
		return this.height;
	}
	
	//set the color of min value
	public void setColorMinValue(int colorMinValue){
		this.colorMinValue = colorMinValue;
		init();
		invalidate();
	}
	public int getColorMinValue(){
		return this.colorMinValue;
	}
	
	//set the color of max value
	public void setColorMaxValue(int colorMaxValue){
		this.colorMaxValue = colorMaxValue;
		init();
		invalidate();
	}
	public int getColorMaxValue(){
		return this.colorMaxValue;
	}
	
	//set the min value of temperature
	public void setMinValue(float minValue){
		this.minValue = minValue;
		invalidate();
	}
	public float getMinValue(){
		return this.minValue;
	}

	//set the max value of temperature
	public void setMaxValue(float maxValue){
		this.maxValue = maxValue;
		invalidate();
	}
	public float getMaxValue(){
		return this.maxValue;
	}
	
	public void setTextSize(float textSize){
		this.textSize = textSize;
		init();
		invalidate();
	}
	public float getTextSize(){
		return this.textSize;
	}
	
	//set textColor
	public void setTextColor(int textColor){
		this.textColor = textColor;
		init();
		invalidate();
	}
	public int getTextColor(){
		return this.textColor;
	}
	
	//set the color line indicator for min and max
	public void setColorLineMinMax(int colorLineMinMax){
		this.colorLineMinMax = colorLineMinMax;
		init();
		invalidate();
	}
	public int getColorLineMinMax(){
		return this.colorLineMinMax;
	}
	
	//set the color line indicator for current temperature
	public void setColorLineCurrent(int colorLineCurrent){
		this.colorLineCurrent = colorLineCurrent;
		init();
		invalidate();
	}
	public int getColorLineCurrent(){
		return this.colorLineCurrent;
	}
	
	//set the line widht for temperature indicators
	public void setLineWidth(int lineWidht){
		this.lineWidth = lineWidht;
		init();
		invalidate();
	}
	public int getLineWidth(){
		return this.lineWidth;
	}
	
}
