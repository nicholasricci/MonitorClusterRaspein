/**
 * @author Nicholas Ricci
 */
package it.unibo.mobilecustomviews.tower;

import it.unibo.mobilecustomviews.R;
import it.unibo.mobilecustomviews.indicators.DrawText;
import it.unibo.mobilecustomviews.indicators.DrawTextUsedTotal;
import it.unibo.mobilecustomviews.indicators.Speedometer;
import it.unibo.mobilecustomviews.indicators.Temperature;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CompositeTowerPiece extends RelativeLayout{

	//Constant for max_compontent in CompositeTowerPiece
	private static final int MAX_COMPONENTS_HORIZONTAL = 4;
	private static final int MAX_COMPONENTS_VERTICAL = 4;
	private static final int MAX_RASPBERRY = 2;
	
	//Constants for ID components
	private static final int TOWER_PIECE_ID = 100;
	private static final int TEMPERATURE_RASP1_ID = 101;
	private static final int TEMPERATURE_RASP2_ID = 102;
	private static final int SPEEDOMETER_RASP1_ID = 103;
	private static final int SPEEDOMETER_RASP2_ID = 104;
	private static final int RAM_RASP1_ID = 105;
	private static final int RAM_RASP2_ID = 106;
	private static final int STORAGE_RASP1_ID = 107;
	private static final int STORAGE_RASP2_ID = 108;
	private static final int NETWORK_RASP1_ID = 109;
	private static final int NETWORK_RASP2_ID = 110;
	private static final int PROC_RASP1_ID = 111;
	private static final int PROC_RASP2_ID = 112;
	//private static final int PROC_SEGUES_TIMES = 50;
	
	//composite variable
	int compositeWidth = 140;
	int compositeHeight = 80;
	int compositeColor = Color.RED;
	int compositeTextSize = 15;
	int distanceBetweenRasp = 10;
	int distanceFromBorder = 10;
	int coordinateLeft = 0;
	int coordinateTop = 0;
	
	String rasp1Name = "";
	String rasp2Name = "";
	
	//temperature variable
	int temperatureWidth = 50;
	int temperatureHeight = 100;
	int temperatureWidthForText = 50;
	int temperatureColorMinValue = Color.GREEN;
	int temperatureColorMaxValue = Color.RED;
	float temperatureMinValue = 0;
	float temperatureMaxValue = 100;
	float temperatureTextSize = 25;
	int temperatureTextColor = Color.BLACK;
	int temperatureColorLineMinMax = Color.BLACK;
	int temperatureColorLineCurrent = Color.WHITE;
	int temperatureLineWidth = 3;
	
	//Ram Variables
	float[] ramUsed;
	float[] ramTotal;
	
	//Storage Variables
	float[] storageUsed;
	float[] storageTotal;
	
	//Network Load Variables
	float[] networkLoadUp;
	float[] networkLoadDown;
	
	//Cpus Variables
	float[] cpusWorkLoad;
	
	//Variable custom views
	TowerPiece towerPiece;
	Temperature[] temperature;
	Speedometer[] speedometer;
	DrawTextUsedTotal[] ramDrawTextUsedTotal;
	DrawTextUsedTotal[] storageDrawTextUsedTotals;
	DrawTextUsedTotal[] networkDrawTextUsedTotals;
	DrawText[] cpuDrawText;
	
	public CompositeTowerPiece(Context context) {
		super(context);
		
		initViews(context);
	}
	
	public CompositeTowerPiece(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		defAttribute(context, attrs);
		initViews(context);
	}
	
	public CompositeTowerPiece(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		defAttribute(context, attrs);
		initViews(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(compositeWidth + 2, compositeHeight + 2);
	}
	
	private void defAttribute(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompositeTowerPiece);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CompositeTowerPiece_composite_width:
				compositeWidth = a.getDimensionPixelSize(attr, 200);
				break;
			case R.styleable.CompositeTowerPiece_composite_height:
				compositeHeight = a.getDimensionPixelSize(attr, 100);
				break;
			case R.styleable.CompositeTowerPiece_composite_color:
				compositeColor = a.getColor(attr, 0);
				break;
			case R.styleable.CompositeTowerPiece_composite_text_size:
				compositeTextSize = a.getDimensionPixelSize(attr, 15);
				break;
			case R.styleable.CompositeTowerPiece_composite_distance_between_raspberry:
				distanceBetweenRasp = a.getDimensionPixelSize(attr, 10);
				break;
			case R.styleable.CompositeTowerPiece_composite_distance_from_border:
				distanceFromBorder = a.getDimensionPixelSize(attr, 10);
				break;
			case R.styleable.CompositeTowerPiece_composite_coordinate_left:
				coordinateLeft = a.getDimensionPixelSize(attr, 20);
				break;
			case R.styleable.CompositeTowerPiece_composite_coordinate_top:
				coordinateTop = a.getDimensionPixelSize(attr, 0);
				break;	
			case R.styleable.CompositeTowerPiece_composite_temperature_width:
				temperatureWidth = a.getDimensionPixelSize(attr, 50);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_height:
				temperatureHeight = a.getDimensionPixelSize(attr, 100);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_width_for_text:
				temperatureWidthForText = a.getDimensionPixelSize(attr, 50);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_color_min_value:
				temperatureColorMinValue = a.getColor(attr, Color.GREEN);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_color_max_value:
				temperatureColorMaxValue = a.getColor(attr, Color.RED);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_min_value:
				temperatureMinValue = a.getFloat(attr, 0);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_max_value:
				temperatureMaxValue = a.getFloat(attr, 100);
				break;	
			case R.styleable.CompositeTowerPiece_composite_temperature_text_size:
				temperatureTextSize = a.getDimension(attr, 25);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_text_color:
				temperatureTextColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_color_line_min_max:
				temperatureColorLineMinMax = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_color_line_current:
				temperatureColorLineCurrent = a.getColor(attr, Color.WHITE);
				break;
			case R.styleable.CompositeTowerPiece_composite_temperature_line_width:
				temperatureLineWidth = a.getInt(attr, 3);
				break;
			case R.styleable.CompositeTowerPiece_composite_raspberry1_name:
				rasp1Name = a.getString(attr);
				break;
			case R.styleable.CompositeTowerPiece_composite_raspberry2_name:
				rasp2Name = a.getString(attr);
				break;
			default:
				break;
			}
		}
		a.recycle();
	}
	
	private void initViews(Context context){
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(compositeWidth + 2, compositeHeight + 2);
		this.setLayoutParams(rllp);
		
		//create towerpiece
		towerPiece = new TowerPiece(context);
		towerPiece.setId(TOWER_PIECE_ID);
		towerPiece.setWidthTowerPiece(compositeWidth);
		towerPiece.setHeightTowerPiece(compositeHeight);
		towerPiece.setColor(compositeColor);
		towerPiece.setTextSize(compositeTextSize);
		towerPiece.setDistanceBetweenRaspberry(distanceBetweenRasp);
		towerPiece.setDistanceFromBorder(distanceFromBorder);
		towerPiece.setCoordinateLeft(coordinateLeft);
		towerPiece.setCoordinateTop(coordinateTop);
		towerPiece.setRasp1Name(rasp1Name);
		towerPiece.setRasp2Name(rasp2Name);
		//create layout params for tower piece
		RelativeLayout.LayoutParams rllp_tower = new RelativeLayout.LayoutParams(compositeWidth, compositeHeight);
		towerPiece.setLayoutParams(rllp_tower);
		//add towerPiece to RelativeLayout
		this.addView(towerPiece);
		
		//create array for temperature
		temperature = new Temperature[]{ new Temperature(context), new Temperature(context) };
		//create array for speedometer (clockspeed)
		speedometer = new Speedometer[]{ new Speedometer(context), new Speedometer(context) };
		//create array for ramUsage
		ramUsed = new float[MAX_RASPBERRY];
		//create array for ramTotal
		ramTotal = new float[MAX_RASPBERRY];
		ramDrawTextUsedTotal = new DrawTextUsedTotal[]{ new DrawTextUsedTotal(context), new DrawTextUsedTotal(context) };
		//create array for storageUsed
		storageUsed = new float[MAX_RASPBERRY];
		//create array for storageTotal
		storageTotal = new float[MAX_RASPBERRY];
		storageDrawTextUsedTotals = new DrawTextUsedTotal[]{ new DrawTextUsedTotal(context), new DrawTextUsedTotal(context) };
		//create array for networkLoadUp
		networkLoadUp = new float[MAX_RASPBERRY];
		//create array for networkLoadDown
		networkLoadDown = new float[MAX_RASPBERRY];
		networkDrawTextUsedTotals = new DrawTextUsedTotal[]{ new DrawTextUsedTotal(context), new DrawTextUsedTotal(context) };
		//create array for cpus workload
		cpusWorkLoad = new float[MAX_RASPBERRY];
		cpuDrawText = new DrawText[]{ new DrawText(context), new DrawText(context) };
		
		ramUsed[0] = 0;
		ramUsed[1] = 0;
		ramTotal[0] = 512;
		ramTotal[1] = 512;
		storageUsed[0] = 0;
		storageUsed[1] = 0;
		storageTotal[0] = 1024;
		storageTotal[1] = 1024;
		networkLoadUp[0] = 0;
		networkLoadUp[1] = 0;
		networkLoadDown[0] = 0;
		networkLoadDown[1] = 0;
		cpusWorkLoad[0] = 0;
		cpusWorkLoad[1] = 0;
		
		updateViews();
		
		//add temperature to layout
		this.addView(temperature[0]);
		this.addView(temperature[1]);
		//add speedometer to layout
		this.addView(speedometer[0]);
		this.addView(speedometer[1]);
		//add ram to layout
		this.addView(ramDrawTextUsedTotal[0]);
		this.addView(ramDrawTextUsedTotal[1]);
		//add storage to layout
		this.addView(storageDrawTextUsedTotals[0]);
		this.addView(storageDrawTextUsedTotals[1]);
		//add network to layout
		this.addView(networkDrawTextUsedTotals[0]);
		this.addView(networkDrawTextUsedTotals[1]);
		//add processor to layout
		this.addView(cpuDrawText[0]);
		this.addView(cpuDrawText[1]);
	}
	
	private void updateViews(){
		int textSize = compositeTextSize * 3 / 4;
		int widthForEachComponent = towerPiece.getRaspberryWidth() / MAX_COMPONENTS_HORIZONTAL;
		int widthForEachComponentVertical = widthForEachComponent * 2;
		int heightForEachComponent = towerPiece.getRaspberryHeight() - towerPiece.getDistanceBetweenRaspberry() * 2 - towerPiece.getTextSize();
		int distanceVertcialBetweenComponent = towerPiece.getDistanceFromBorder() + textSize;
		int distanceHorizontalBetweenComponent = towerPiece.getDistanceFromBorder();
		
		//temperature for raspberry 1
		temperature[0].setId(TEMPERATURE_RASP1_ID);
		temperature[0].setWidthPlusWidthForText(
				widthForEachComponent / 2, 
				widthForEachComponent / 2);
		temperature[0].setHeightTemperature(heightForEachComponent);
		temperature[0].setTextSize(textSize);
		RelativeLayout.LayoutParams rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponent / 2, 
				heightForEachComponent);
		rllp_rasp.addRule(RelativeLayout.ALIGN_TOP, towerPiece.getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_LEFT, towerPiece.getId());
		rllp_rasp.setMargins(towerPiece.getDistanceFromBorder() + 10, towerPiece.getDistanceFromBorder() + towerPiece.getTextSize(), 0, 0);
		temperature[0].setLayoutParams(rllp_rasp);
		
		//temperature for raspberry 2
		temperature[1].setId(TEMPERATURE_RASP2_ID);
		temperature[1].setWidthPlusWidthForText(
				widthForEachComponent / 2, 
				widthForEachComponent / 2);
		temperature[1].setHeightTemperature(heightForEachComponent);
		temperature[1].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponent / 2, 
				heightForEachComponent);
		rllp_rasp.addRule(RelativeLayout.ALIGN_BOTTOM, towerPiece.getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_LEFT, towerPiece.getId());
		rllp_rasp.setMargins(towerPiece.getDistanceFromBorder() + 10, 0, 0, towerPiece.getDistanceFromBorder() + towerPiece.getTextSize());
		temperature[1].setLayoutParams(rllp_rasp);
		
		//speedometer for raspberry 1
		speedometer[0].setId(SPEEDOMETER_RASP1_ID);
		speedometer[0].setWidthSpeedometer(widthForEachComponent);
		speedometer[0].setHeightSpeedometer(heightForEachComponent);
		speedometer[0].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponent, 
				heightForEachComponent);
		rllp_rasp.addRule(RelativeLayout.ALIGN_TOP, towerPiece.getId());
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, temperature[0].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, 
				towerPiece.getDistanceFromBorder() + towerPiece.getTextSize(), 0, 0);
		speedometer[0].setLayoutParams(rllp_rasp);
	
		//speedometer for raspberry 1
		speedometer[1].setId(SPEEDOMETER_RASP2_ID);
		speedometer[1].setWidthSpeedometer(widthForEachComponent);
		speedometer[1].setHeightSpeedometer(heightForEachComponent);
		speedometer[1].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponent, 
				heightForEachComponent);
		rllp_rasp.addRule(RelativeLayout.ALIGN_BOTTOM, towerPiece.getId());
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, temperature[1].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, 0, 0, towerPiece.getDistanceFromBorder() + towerPiece.getTextSize());
		speedometer[1].setLayoutParams(rllp_rasp);
	
		//ram
		ramDrawTextUsedTotal[0].setId(RAM_RASP1_ID);
		ramDrawTextUsedTotal[0].setTextName("RAM");
		ramDrawTextUsedTotal[0].setLayoutWidth(widthForEachComponentVertical);
		ramDrawTextUsedTotal[0].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		ramDrawTextUsedTotal[0].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);

		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[0].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_TOP, towerPiece.getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, distanceVertcialBetweenComponent, 0, 0);
		ramDrawTextUsedTotal[0].setLayoutParams(rllp_rasp);
		
		ramDrawTextUsedTotal[1].setId(RAM_RASP2_ID);
		ramDrawTextUsedTotal[1].setTextName("RAM");
		ramDrawTextUsedTotal[1].setLayoutWidth(widthForEachComponentVertical);
		ramDrawTextUsedTotal[1].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		ramDrawTextUsedTotal[1].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[1].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_BOTTOM, towerPiece.getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, 0, 0, distanceVertcialBetweenComponent);
		ramDrawTextUsedTotal[1].setLayoutParams(rllp_rasp);
		
		//storage
		storageDrawTextUsedTotals[0].setId(STORAGE_RASP1_ID);
		storageDrawTextUsedTotals[0].setTextName("SD");
		storageDrawTextUsedTotals[0].setLayoutWidth(widthForEachComponentVertical);
		storageDrawTextUsedTotals[0].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		storageDrawTextUsedTotals[0].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[0].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_TOP, ramDrawTextUsedTotal[0].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, distanceVertcialBetweenComponent, 0, 0);
		storageDrawTextUsedTotals[0].setLayoutParams(rllp_rasp);
		
		storageDrawTextUsedTotals[1].setId(STORAGE_RASP2_ID);
		storageDrawTextUsedTotals[1].setTextName("SD");
		storageDrawTextUsedTotals[1].setLayoutWidth(widthForEachComponentVertical);
		storageDrawTextUsedTotals[1].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		storageDrawTextUsedTotals[1].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[1].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_BOTTOM, ramDrawTextUsedTotal[1].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, 0, 0, distanceVertcialBetweenComponent);
		storageDrawTextUsedTotals[1].setLayoutParams(rllp_rasp);
		
		//networkLoad
		networkDrawTextUsedTotals[0].setId(NETWORK_RASP1_ID);
		networkDrawTextUsedTotals[0].setTextName("NET(UP/DOWN)");
		networkDrawTextUsedTotals[0].setLayoutWidth(widthForEachComponentVertical);
		networkDrawTextUsedTotals[0].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		networkDrawTextUsedTotals[0].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[0].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_TOP, storageDrawTextUsedTotals[0].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, distanceVertcialBetweenComponent, 0, 0);
		networkDrawTextUsedTotals[0].setLayoutParams(rllp_rasp);
		
		networkDrawTextUsedTotals[1].setId(NETWORK_RASP2_ID);
		networkDrawTextUsedTotals[1].setTextName("NET(UP/DOWN)");
		networkDrawTextUsedTotals[1].setLayoutWidth(widthForEachComponentVertical);
		networkDrawTextUsedTotals[1].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		networkDrawTextUsedTotals[1].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[1].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_BOTTOM, storageDrawTextUsedTotals[1].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, 0, 0, distanceVertcialBetweenComponent);
		networkDrawTextUsedTotals[1].setLayoutParams(rllp_rasp);
		
		//cpus workload DRAW ONLY ONE CPU
		cpuDrawText[0].setId(PROC_RASP1_ID);
		cpuDrawText[0].setTextName("CPU");
		cpuDrawText[0].setLayoutWidth(widthForEachComponentVertical);
		cpuDrawText[0].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		cpuDrawText[0].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[0].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_TOP, networkDrawTextUsedTotals[0].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, distanceVertcialBetweenComponent, 0, 0);
		cpuDrawText[0].setLayoutParams(rllp_rasp);
		
		cpuDrawText[1].setId(PROC_RASP2_ID);
		cpuDrawText[1].setTextName("CPU");
		cpuDrawText[1].setLayoutWidth(widthForEachComponentVertical);
		cpuDrawText[1].setLayoutHeight(heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		cpuDrawText[1].setTextSize(textSize);
		rllp_rasp = new RelativeLayout.LayoutParams(
				widthForEachComponentVertical, 
				heightForEachComponent / MAX_COMPONENTS_VERTICAL);
		rllp_rasp.addRule(RelativeLayout.RIGHT_OF, speedometer[1].getId());
		rllp_rasp.addRule(RelativeLayout.ALIGN_BOTTOM, networkDrawTextUsedTotals[1].getId());
		rllp_rasp.setMargins(distanceHorizontalBetweenComponent, 0, 0, distanceVertcialBetweenComponent);
		cpuDrawText[1].setLayoutParams(rllp_rasp);
	}
	
	//set the widht of towerpiece
	public void setTowerWidth(int width){
		this.compositeWidth = width;
		towerPiece.setWidthTowerPiece(width);
		compositeTextSize = (int) (width / 20);
		towerPiece.setTextSize(compositeTextSize);
		//non mi è chiaro perchè devo usare il linearLayout al posto del relative layout ???
		//con il relativelayout da errore :s
		this.setLayoutParams(new LinearLayout.LayoutParams(compositeWidth, compositeHeight));
		updateViews();
		invalidate();
		requestLayout();
	}
	public int getTowerWidth(){
		return this.compositeWidth;
	}
	
	//set the height of towerpiece
	public void setTowerHeight(int height){
		this.compositeHeight = height;
		towerPiece.setHeightTowerPiece(height);
		//non mi è chiaro perchè devo usare il linearLayout al posto del relative layout ???
		//con il relativelayout da errore :s
		this.setLayoutParams(new LinearLayout.LayoutParams(compositeWidth, compositeHeight));
		updateViews();
		invalidate();
		requestLayout();
	}
	public int getTowerHeight(){
		return this.compositeHeight;
	}
	
	//set the value of temperature for raspberry 1
	public void setTemperatureRasp1(float value){
		temperature[0].setCurrentTemperature(value);
	}
	public float getTemperatureRasp1(){
		return temperature[0].getCurrentTemperature();
	}
	
	//set the value of temperature for raspberry 2
	public void setTemperatureRasp2(float value){
		temperature[1].setCurrentTemperature(value);
	}
	public float getTemperatureRasp2(){
		return temperature[1].getCurrentTemperature();
	}
	
	public float getMinValueRasp1Temperature(){
		return temperature[0].getMinValue();
	}
	
	public float getMinValueRasp2Temperature(){
		return temperature[1].getMinValue();
	}
	
	public float getMaxValueRasp1Temperature(){
		return temperature[0].getMaxValue();
	}
	
	public float getMaxValueRasp2Temperature(){
		return temperature[1].getMaxValue();
	}
	
	//set the value of Speedometer for raspberry 1
	public void setSpeedometerRasp1(float value){
		speedometer[0].setCurrentSpeed(value);
	}
	public float getSpeedometerRasp1(){
		return speedometer[0].getCurrentSpeed();
	}
	
	//set the value of Speedometer for raspberry 2
	public void setSpeedometerRasp2(float value){
		speedometer[1].setCurrentSpeed(value);
	}
	public float getSpeedometerRasp2(){
		return speedometer[1].getCurrentSpeed();
	}
	
	//Return the min value of rasp1
	public float getMinValueRasp1Speedometer(){
		return speedometer[0].getMinValue();
	}

	//Return the min value of rasp2
	public float getMinValueRasp2Speedometer(){
		return speedometer[1].getMinValue();
	}

	//Return the max value of rasp1
	public float getMaxValueRasp1Speedometer(){
		return speedometer[0].getMaxValue();
	}

	//Return the max value of rasp2
	public float getMaxValueRasp2Speedometer(){
		return speedometer[1].getMaxValue();
	}
	
	//setter for ramusage for rasp1
	public void setRamUsedValueRasp1(float ramUsage){
		this.ramUsed[0] = ramUsage;
		ramDrawTextUsedTotal[0].setTextUsed(ramUsage + "");
	}
	public float getRamUsedValueRasp1(){
		return this.ramUsed[0];
	}
	
	//setter for ramusage for rasp2
	public void setRamUsedValueRasp2(float ramUsage){
		this.ramUsed[1] = ramUsage;
		ramDrawTextUsedTotal[1].setTextUsed(ramUsage + "");
	}
	public float getRamUsedValueRasp2(){
		return this.ramUsed[1];
	}
	
	//setter for ramTotal for rasp1
	public void setRamTotalValueRasp1(float ramTotal){
		this.ramTotal[0] = ramTotal;
		ramDrawTextUsedTotal[0].setTextTotal(ramTotal + "");
	}
	public float getRamTotalValueRasp1(){
		return this.ramTotal[0];
	}
	
	//setter for ramTotal for rasp2
	public void setRamTotalValueRasp2(float ramTotal){
		this.ramTotal[1] = ramTotal;
		ramDrawTextUsedTotal[1].setTextTotal(ramTotal + "");
	}
	public float getRamTotalValueRasp2(){
		return this.ramTotal[1];
	}
	
	//setter for storageUsed for rasp1
	public void setStorageUsedValueRasp1(float storageUsed){
		this.storageUsed[0] = storageUsed;
		storageDrawTextUsedTotals[0].setTextUsed(storageUsed + "");
	}
	public float getStorageUsedValueRasp1(){
		return this.storageUsed[0];
	}
	
	//setter for storageUsed for rasp1
	public void setStorageUsedValueRasp2(float storageUsed){
		this.storageUsed[1] = storageUsed;
		storageDrawTextUsedTotals[1].setTextUsed(storageUsed + "");
	}
	public float getStorageUsedValueRasp2(){
		return this.storageUsed[1];
	}
	
	//setter for storageTotal for rasp1
	public void setStorageTotalValueRasp1(float storageTotal){
		this.storageTotal[0] = storageTotal;
		storageDrawTextUsedTotals[0].setTextTotal(storageTotal + "");
	}
	public float getStorageTotalValueRasp1(){
		return this.storageTotal[0];
	}
	
	//setter for storageTotal for rasp2
	public void setStorageTotalValueRasp2(float storageTotal){
		this.storageTotal[1] = storageTotal;
		storageDrawTextUsedTotals[1].setTextTotal(storageTotal + "");
	}
	public float getStorageTotalValueRasp2(){
		return this.storageTotal[1];
	}
	
	//setter for networkLoadUp rasp1
	public void setNetworkLoadUpRasp1(float networkLoadUp){
		this.networkLoadUp[0] = networkLoadUp;
		networkDrawTextUsedTotals[0].setTextUsed(networkLoadUp + "");
	}
	public float getNetworkLoadUpRasp1(){
		return this.networkLoadUp[0];
	}
	
	//setter for networkLoadUp rasp2
	public void setNetworkLoadUpRasp2(float networkLoadUp){
		this.networkLoadUp[1] = networkLoadUp;
		networkDrawTextUsedTotals[1].setTextUsed(networkLoadUp + "");
	}
	public float getNetworkLoadUpRasp2(){
		return this.networkLoadUp[1];
	}
	
	//setter for networkLoadDown rasp1
	public void setNetworkLoadDownRasp1(float networkLoadDown){
		this.networkLoadDown[0] = networkLoadDown;
		networkDrawTextUsedTotals[0].setTextTotal(networkLoadDown + "");
	}
	public float getNetworkLoadDownRasp1(){
		return this.networkLoadDown[0];
	}
	
	//setter for networkLoadDown rasp1
	public void setNetworkLoadDownRasp2(float networkLoadDown){
		this.networkLoadDown[1] = networkLoadDown;
		networkDrawTextUsedTotals[1].setTextTotal(networkLoadDown + "");
	}
	public float getNetworkLoadDownRasp2(){
		return this.networkLoadDown[1];
	}
	
	//setter for cpusWorkLoad rasp1
	public void setCpusWorkLoadRasp1(float cpusWorkLoad){
		this.cpusWorkLoad[0] = cpusWorkLoad;
		cpuDrawText[0].setTextValue(cpusWorkLoad + "");
	}
	public float getCpusWorkLoadRasp1(){
		return this.cpusWorkLoad[0];
	}
	
	//setter for cpusWorkLoad rasp2
	public void setCpusWorkLoadRasp2(float cpusWorkLoad){
		this.cpusWorkLoad[1] = cpusWorkLoad;
		cpuDrawText[1].setTextValue(cpusWorkLoad + "");
	}
	public float getCpusWorkLoadRasp2(){
		return this.cpusWorkLoad[1];
	}
}
