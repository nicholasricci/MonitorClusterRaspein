/**
 * @author Nicholas Ricci
 */
package it.unibo.mobilecustomviews.tower;

import it.unibo.mobilecustomviews.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class TowerPiece extends View{

	//Paint utile per modificare lo stile del disegno
	//utilizzato uno per il rettangolo interno e uno per il rettangolo esterno
	Paint mPaintRectExternalBorder;
	Paint mPaintRectExternal;
	Paint mPaintRectInternal;
	Paint mPaintText;
	
	//raspberry's names
	String rasp1Name = "";
	String rasp2Name = "";
	
	int width = 140;
	int height = 80;
	int color = Color.RED;
	int textSize = 15;
	int distanceBetweenRasp = 10;
	int distanceFromBorder = 10;
	int coordinateLeft = 0;
	int coordinateTop = 0;
	
	public TowerPiece(Context context) {
		super(context);
		
		initViews(context);
	}
	
	public TowerPiece(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		defAttribute(context, attrs);
		initViews(context);
	}
	
	public TowerPiece(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		defAttribute(context, attrs);
		initViews(context);
	}

	private void initViews(Context context){
		
		mPaintRectExternalBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintRectExternalBorder.setColor(Color.BLACK);
		mPaintRectExternalBorder.setStrokeWidth(1);
		mPaintRectExternalBorder.setStyle(Style.STROKE);
		
		mPaintRectExternal = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintRectExternal.setColor(color);
		mPaintRectExternal.setStrokeWidth(3);
		
		mPaintRectInternal = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintRectInternal.setColor(Color.WHITE);
		mPaintRectInternal.setStrokeWidth(3);
		
		mPaintText = new Paint(Paint.LINEAR_TEXT_FLAG);
		mPaintText.setTextSize(textSize);
		mPaintText.setColor(Color.BLACK);
	}
	
	private void defAttribute(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TowerPiece);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.TowerPiece_piece_width:
				width = a.getDimensionPixelSize(attr, 140);
				break;
			case R.styleable.TowerPiece_piece_color:
				color = a.getColor(attr, Color.RED);
				break;
			case R.styleable.TowerPiece_piece_height:
				height = a.getDimensionPixelSize(attr, 80);
				break;
			case R.styleable.TowerPiece_piece_text_size:
				textSize = a.getDimensionPixelSize(attr, 15);
				break;
			case R.styleable.TowerPiece_piece_distance_between_raspberry:
				distanceBetweenRasp = a.getDimensionPixelSize(attr, 10);
				break;
			case R.styleable.TowerPiece_piece_distance_from_border:
				distanceFromBorder = a.getDimensionPixelSize(attr, 10);
				break;
			case R.styleable.TowerPiece_piece_coordinate_left:
				coordinateLeft = a.getDimensionPixelSize(attr, 0);
				break;
			case R.styleable.TowerPiece_piece_coordinate_top:
				coordinateTop = a.getDimensionPixelSize(attr, 0);
				break;	
			case R.styleable.TowerPiece_piece_rasp1_name:
				rasp1Name = a.getString(attr);
				break;
			case R.styleable.TowerPiece_piece_rasp2_name:
				rasp2Name = a.getString(attr);
				break;
			default:
				break;
			}
		}
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawRect(coordinateLeft, coordinateTop, 
				coordinateLeft + width, coordinateTop + height, 
				mPaintRectExternal);
		canvas.drawRect(coordinateLeft + distanceFromBorder, coordinateTop, 
				coordinateLeft + width - distanceFromBorder, 
				height / 2 - distanceBetweenRasp / 2, 
				mPaintRectInternal);
		canvas.drawRect(coordinateLeft + distanceFromBorder, 
				coordinateTop + height / 2 + distanceBetweenRasp / 2, 
				coordinateLeft + width - distanceFromBorder, 
				height, 
				mPaintRectInternal);
		
		//bordo
		canvas.drawRect(coordinateLeft, coordinateTop, 
				coordinateLeft + width, coordinateTop + height, 
				mPaintRectExternalBorder);
		
		if (!rasp1Name.equals("")){
			canvas.drawText(rasp1Name, 
					coordinateLeft + distanceFromBorder + 5, 
					coordinateTop + textSize, 
					mPaintText);
		}
		if (!rasp2Name.equals("")){
			canvas.drawText(rasp2Name, 
					coordinateLeft + distanceFromBorder + 5, 
					coordinateTop + height - textSize / 4, 
					mPaintText);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(width + 2, height + 2);
	}
	
	//get the raspberry (whitespace) height for one raspberry
	public int getRaspberryHeight(){
		return (height - distanceBetweenRasp) / 2;
	}
	
	//get the raspberry (whitespace) width for one raspberry
	public int getRaspberryWidth(){
		return (width - distanceFromBorder * 2);
	}
	
	//set the width of the tower piece
	public void setWidthTowerPiece(int width){
		this.width = width;
		invalidate();
		requestLayout();
	}
	public int getWidthTowerPiece(){
		return this.width;
	}
	
	//set the height of the tower piece
	public void setHeightTowerPiece(int height){
		this.height = height;
		invalidate();
		requestLayout();
	}
	public int getHeightTowerPiece(){
		return this.height;
	}
	
	//set the color of the tower piece
	public void setColor(int color){
		this.color = color;
		initViews(getContext());
		invalidate();
	}
	public int getColor(){
		return this.color;
	}
	
	//set the size of text
	public void setTextSize(int textSize){
		this.textSize = textSize;
		initViews(getContext());
		invalidate();
	}
	public int getTextSize(){
		return this.textSize;
	}
	
	//set the distance between two raspberry
	public void setDistanceBetweenRaspberry(int distanceBetweenRaspberry){
		this.distanceBetweenRasp = distanceBetweenRaspberry;
		invalidate();
	}
	public int getDistanceBetweenRaspberry(){
		return this.distanceBetweenRasp;
	}
	
	//set the distance from border
	public void setDistanceFromBorder(int distanceFromBorder){
		this.distanceFromBorder = distanceFromBorder;
		invalidate();
	}
	public int getDistanceFromBorder(){
		return this.distanceFromBorder;
	}
	
	//set the coordinate to start on the left
	public void setCoordinateLeft(int coordinateLeft){
		this.coordinateLeft = coordinateLeft;
		invalidate();
	}
	public int getCoordinateLeft(){
		return this.coordinateLeft;
	}
	
	//set the coordinate to start on the top
	public void setCoordinateTop(int coordinateTop){
		this.coordinateTop = coordinateTop;
		invalidate();
	}
	public int getCoordinateTop(){
		return this.coordinateTop;
	}
	
	//set the raspberry1 name
	public void setRasp1Name(String rasp1Name){
		this.rasp1Name = rasp1Name;
		invalidate();
	}
	public String getRasp1Name(){
		return this.rasp1Name;
	}
	
	//set the raspberry2 name
	public void setRasp2Name(String rasp2Name){
		this.rasp2Name = rasp2Name;
		invalidate();
	}
	public String getRasp2Name(){
		return this.rasp2Name;
	}
}
