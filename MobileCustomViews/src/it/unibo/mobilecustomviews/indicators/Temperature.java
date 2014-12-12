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
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

public class Temperature extends View{

	
	//Variable useful for temperature
	private float minValue = 0;
	private float maxValue = 100;
	private float currentValue = (maxValue + minValue) / 2;
	private int colorMinValue = Color.GREEN;;
	private int colorMaxValue = Color.RED;
	
	//Variable useful for line min max and current value
	private int colorLineMinMax = Color.BLACK;
	private int colorLineCurrent = Color.WHITE;
	private int lineWidth = 3;
	
	//Variable useful for set the view
	private int width = 50;
	private int height = 200;
	private int widthForText = 50;
	
	//Variable for text
	private float textSize = 20;
	private int textColor = Color.BLACK;
	
	//Variable useful for paint and draw
	Paint mTextPaint;
	Paint mLineMinMax;
	Paint mLineCurrent;
	Paint mCirclePaint;
	Rect mRect;
	GradientDrawable mDrawable;
	
	public Temperature(Context context) {
		super(context);
		
		init();
	}
	
	public Temperature(Context context, AttributeSet attrs) {
		super(context, attrs);

		defAttribute(context, attrs);
		init();
	}
	
	public Temperature(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		defAttribute(context, attrs);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float realHeightToDraw = height * 3 / 4;
		float marginTop = height / 4 / 2;
		float realHeightSup = marginTop + realHeightToDraw / 8;
		float realHeightInf = marginTop + realHeightToDraw / 8 * 7;
		float realHeight = realHeightInf - realHeightSup;
		
		float realWidthToDraw = width * 3 / 4;
		
		mRect.set(0, (int)realHeightSup, 
				(int)realWidthToDraw, (int)realHeightInf);
		
		mDrawable.setBounds(mRect);
		
		//draw circle top
		mCirclePaint.setColor(colorMaxValue);
		canvas.drawCircle(realWidthToDraw / 2, realHeightSup, realWidthToDraw / 2, mCirclePaint);
		//draw circle bottom
		mCirclePaint.setColor(colorMinValue);
		canvas.drawCircle(realWidthToDraw / 2, realHeightInf, realWidthToDraw / 2, mCirclePaint);
		//draw gradient
		canvas.save();
		mDrawable.draw(canvas);
		canvas.restore();
		//draw text
		canvas.drawText(maxValue + "", realWidthToDraw + 3, realHeightSup + textSize / 2, mTextPaint);
		canvas.drawText(minValue + "", realWidthToDraw + 3, realHeightInf + textSize / 2, mTextPaint);
		canvas.drawText(currentValue + "", realWidthToDraw + 3, height / 2 + textSize / 2, mTextPaint);
		//draw line
		canvas.drawLine(realWidthToDraw, realHeightSup, realWidthToDraw / 4 * 3, realHeightSup, mLineMinMax);
		canvas.drawLine(realWidthToDraw, realHeightInf, realWidthToDraw / 4 * 3, realHeightInf, mLineMinMax);
		canvas.drawLine(0, realHeightInf - (realHeight / (maxValue - minValue) * currentValue), realWidthToDraw / 4, realHeightInf - (realHeight / (maxValue - minValue) * currentValue), mLineCurrent);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//set the width like with + width for text
		setMeasuredDimension(width + widthForText, height);
	}

	private void defAttribute(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Temperature);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.Temperature_temperature_width:
				width = a.getDimensionPixelSize(attr, 50);
				break;
			case R.styleable.Temperature_temperature_height:
				height = a.getDimensionPixelSize(attr, 100);
				break;
			case R.styleable.Temperature_temperature_width_for_text:
				widthForText = a.getDimensionPixelSize(attr, 50);
				break;
			case R.styleable.Temperature_temperature_color_min_value:
				colorMinValue = a.getColor(attr, Color.GREEN);
				break;
			case R.styleable.Temperature_temperature_color_max_value:
				colorMaxValue = a.getColor(attr, Color.RED);
				break;
			case R.styleable.Temperature_temperature_min_value:
				minValue = a.getFloat(attr, 0);
				break;
			case R.styleable.Temperature_temperature_max_value:
				maxValue = a.getFloat(attr, 100);
				break;	
			case R.styleable.Temperature_temperature_text_size:
				textSize = a.getDimension(attr, 25);
				break;
			case R.styleable.Temperature_temperature_text_color:
				textColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.Temperature_temperature_color_line_min_max:
				colorLineMinMax = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.Temperature_temperature_color_line_current:
				colorLineCurrent = a.getColor(attr, Color.WHITE);
				break;
			case R.styleable.Temperature_temperature_line_width:
				lineWidth = a.getInt(attr, 3);
				break;
			default:
				break;
			}
		}
		a.recycle();
	}
	
	private void init(){
		
		mRect = new Rect(0, height / 8, width + 1, height / 8 * 7);
		
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
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                new int[] { colorMinValue, colorMaxValue });
		mDrawable.setShape(GradientDrawable.RECTANGLE);
	}
	
	//set the current value of temperature
	public void setCurrentTemperature(float currentTemperature){
		this.currentValue = currentTemperature;
		invalidate();
	}
	public float getCurrentTemperature(){
		return this.currentValue;
	}
	
	//set width for temperature
	public void setWidthTemperature(int width){
		this.width = width;
		mRect = new Rect(0, height / 8, width + 1, height / 8 * 7);
		invalidate();
	}
	public int getWidthTemperature(){
		return this.width;
	}
	
	//set the width for the text side
	public void setWidthForTextTemperature(int widthForText){
		this.widthForText = widthForText;
		invalidate();
	}
	public int getWidthForTextTemperature(){
		return this.widthForText;
	}
	
	//set the witdth and the widthForText in the same method
	public void setWidthPlusWidthForText(int width, int widthForText){
		this.width = width;
		this.widthForText = widthForText;
		//mRect = new Rect(0, height / 8, width + 1, height / 8 * 7);
		invalidate();
	}
	public int getWidthPlusWidthForText(){
		return this.width + this.widthForText;
	}
	
	//set the height for temperature
	public void setHeightTemperature(int height){
		this.height = height;
		//mRect = new Rect(0, height / 8, width + 1, height / 8 * 7);
		invalidate();
	}
	public int getHeightTemperature(){
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
