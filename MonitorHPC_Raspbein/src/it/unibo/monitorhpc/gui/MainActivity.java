/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.gui;

import it.unibo.mobilecustomviews.tower.CompositeTowerPiece;
import it.unibo.monitorhpc.R;
import it.unibo.monitorhpc.bluetooth.AcceptThread;
import it.unibo.monitorhpc.core.Cluster;
import it.unibo.monitorhpc.core.Node;
import it.unibo.monitorhpc.dialogfragment.ZoomMenu;
import it.unibo.monitorhpc.dialogfragment.ZoomMenu.ZoomMenuInterface;
import it.unibo.monitorhpc.network.DownloadRaspberriesDetails;
import it.unibo.monitorhpc.network.DownloadRaspberriesDetails.DownloadRaspberriesDetailsInterface;
import it.unibo.monitorhpc.preferences.PreferencesActivity;
import it.unibo.monitorhpc.support.TowerPieceSupport;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//Constants for debug
	public static final String TAG = "Activity_main";
	
	public static final int REQUEST_PREFERENCES = 2;
	
	//Constants for bluetooth
	public static final int REQUEST_ENABLE = 1;
	private static final int TIME_DISCOVERABLE = 300;
	
	//Constants for Handler messages
	public static final int MESSAGE_READ = 1;
	
	//Constants for Mode Interactive
	public static final String ACTIVITY_MODE = "MainActivity";
	
	//Constants to identify device's inches
	private static final int TEN_INCHES_TABLET = 1;
	private static final int SEVEN_INCHES_TABLET = 2;
	private static final int LOWER_SEVEN_INCHES = 3;
	
	//Constants to identify how many tower pieces view at one time 
	public static final int ONE_TOWER_PIECE = 1; 
	public static final int TWO_TOWER_PIECE = 2;
	public static final int FOUR_TOWER_PIECE = 4; 
	public static final int EIGHT_TOWER_PIECE = 8; 
	public static final int TWELVE_TOWER_PIECE = 12; 
	public static final int SIXTEEN_TOWER_PIECE = 16;
	
	//Constant for max nodes of HPC
	public static final int NUMBER_NODES = 32;
	
	//constant useful for init values
	private static final int STATUS_INIT_VALUES = 0;
	
	//constant useful for download json string for each raspberry, is the base string
	//you MUST add to this string a id of raspberry.
	//TODO - e.g. server_raspberries_infos/
	private static final String URL_JSON_RASPBERRY = "sever_raspberry_infos.php?id=";
	
	//Variable for new thread in listening bluetooth connection
	private AcceptThread ac;
	
	//Variable for list all bluetooth device
	BroadcastReceiver mReceiver;
	
	//Variable for bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter;
	
	//Variable contain the tower piece and max tower piece can be visualized
	private static CompositeTowerPiece[] towerPiecesArray = new CompositeTowerPiece[NUMBER_NODES / 2];
	private int maxVisualized;
	
	//Variable for zoom select
	private int zoomSelected;
	
	//Variable for real width and height in dpi
	private int widthInDpi;
	private int heightInDpi;
	
	//margin linear layout
	private int linearLayoutMargin;
	
	//Timer for autoscroll
	CountDownTimer cdt;
	
	//Status to know in what tower piece we are
	int status;
	
	//Seconds for the timer to tick
	int seconds;
	
	//Variable for network refresh in server mode
	int networkRefresh;
	
	//HashMap for zoomSelected to retrieve width and height of towerpiece
	@SuppressLint("UseSparseArrays")
	HashMap<Integer, TowerPieceSupport> hashMap = new HashMap<Integer, TowerPieceSupport>();
	
	//ScrollView
	ScrollView verticalSV;
	HorizontalScrollView horizontalSV;
	
	//Cluster Variable
	static Cluster cluster = new Cluster();
	
	static TextView txtView;
	
	private static final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			String msgStr = (String) msg.obj;
            if (txtView != null && msgStr != null)
            	txtView.setText(msgStr);

            Log.i("prova", msgStr);
            //try to create cluster
            createClusterFromJson(msgStr);
            
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
        //take wake lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        txtView = (TextView)findViewById(R.id.main_text);
        verticalSV = (ScrollView) findViewById(R.id.vertical_scroll);
		horizontalSV = (HorizontalScrollView) findViewById(R.id.horizontal_scroll);
        
        //Check if the button pressed is server or bluetooth
        Intent intent = getIntent();
        int mode = intent.getIntExtra(ACTIVITY_MODE, ChooseMode.SERVER_MODE);
        if (mode == ChooseMode.SERVER_MODE){
        	//randomValue();
        	//This function is disabled because we haven't server that setted information
        	//if there a server that have information MUST uncomment and set the correct url
        	//TODO
        	//retrieveAndSetRaspDetailsFromWebServer();
        }else if (mode == ChooseMode.BLUETOOTH_MODE) {
        	/*if bluetooth button is pressed then check if bluetooth adapter exists
        	 * if exits then appear a dialog to activate it
        	 * else exit from activity
        	 */
            bluetoothExists();
            bluetoothIsEnabled();
		}

		status = STATUS_INIT_VALUES;
		widthInDpi = (int)(getWidthInPixel() * 6 / 7);
		heightInDpi = (int)(getHeightInPixel() * 6 / 7);
        getTowerPieceVisualized();
       
        initZoom();
        
        //to return back
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (cdt != null){
			cdt.cancel();
			cdt = null;
		}
	}

	//Create the menu with overflow icon in action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	//onitem selected in action bar menu inside overflow icon
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            actionSettings();
	            return true;
	        case R.id.action_zoom_select:
	            //zoomSelect();
	        	ArrayList<String> arrayList = new ArrayList<String>();
	        	arrayList.add(getResources().getString(R.string.zoom_one_tower_piece));
	        	arrayList.add(getResources().getString(R.string.zoom_two_tower_piece));
	        	arrayList.add(getResources().getString(R.string.zoom_four_tower_piece));
	        	arrayList.add(getResources().getString(R.string.zoom_eight_tower_piece));
	        	arrayList.add(getResources().getString(R.string.zoom_sixteen_tower_piece));
	            ZoomMenu zoomMenu = new ZoomMenu(arrayList);
	            zoomMenu.setZoomMenuInterface(new ZoomMenuInterface() {
					
					@Override
					public void setZoomSelected(int zoom) {
						
						zoomSelected = zoom;
						zoomSelect();
					}
				});
	            zoomMenu.show(getFragmentManager(), TAG);
	            return true;
	        case android.R.id.home:
	        	onBackPressed();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void bluetoothExists(){
		// Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
	}
	
	private void bluetoothIsEnabled(){
		//if bluetooth is enable appear a dialog with the enabling
		if (!mBluetoothAdapter.isEnabled()) {
    		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, TIME_DISCOVERABLE);
			startActivityForResult(discoverableIntent, REQUEST_ENABLE);
			//Log.i(TAG, "Attivazione bluetooth");
        }else{
        	startServerBluetooth();
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_ENABLE){
			if (resultCode == TIME_DISCOVERABLE){
				//Log.i(TAG, "onActivityResult(): abilitato");
				startServerBluetooth();
			}
		}
		if (requestCode == REQUEST_PREFERENCES){
			if (resultCode == RESULT_OK){
				initializePreferences();
			}
		}
	}
	
	private void initializePreferences(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		if (!sharedPreferences.contains(getResources().getString(R.string.preference_key_speed_movement))){
			editor.putString(getResources().getString(R.string.preference_key_speed_movement), "5");
		}
		if (!sharedPreferences.contains(getResources().getString(R.string.preference_key_zoom))){
			editor.putString(getResources().getString(R.string.preference_key_zoom), ONE_TOWER_PIECE + "");
		}
		if (!sharedPreferences.contains(getResources().getString(R.string.preference_key_network_refresh))){
			editor.putString(getResources().getString(R.string.preference_key_network_refresh), "1");
		}

		editor.commit();
		
		seconds = Integer.parseInt(sharedPreferences.getString(getResources().getString(R.string.preference_key_speed_movement), "5"));
		zoomSelected = Integer.parseInt(sharedPreferences.getString(getResources().getString(R.string.preference_key_zoom), ONE_TOWER_PIECE + ""));
		networkRefresh = Integer.parseInt(sharedPreferences.getString(getResources().getString(R.string.preference_key_network_refresh), "1"));
		
	}
	
	private void startServerBluetooth(){
		if (ac == null){
			ac = new AcceptThread(mBluetoothAdapter, handler);
			ac.start();
			//Log.i(TAG, "Server in funzione");
		}
	}
	
	//Detect device's inches
	private int getInchesOfDevice(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		/*This will return the absolute value of the 
		 * width and the height in pixels, e.g. 1280x720 
		 * for the Galaxy SIII
		 */
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		
		//THE FIRST METHOD ISN'T PRECISE LIKE SECOND
		
		//get the density of the screen in form of scale factor (ldpi, mdpi, hdpi, xhdpi, xxhdpi)
		//float scaleFactor = metrics.density;
		/*
		 * calculate the amount of density independent pixels (dpi) there are for 
		 * width and height we earlier calculated.
		 */
		/*
		float widthDp = widthPixels / scaleFactor;
		float heightDp = heightPixels / scaleFactor;
		float smallestWidth = Math.min(widthDp, heightDp);
		if (smallestWidth > 720) {
		    return TEN_INCHES_TABLET;
		} 
		else if (smallestWidth > 600) {
		    return SEVEN_INCHES_TABLET;
		}else{
			return LOWER_SEVEN_INCHES;
		}
		*/
		
		//how many pixels there are per inch of the screen.
		float widthDpi = metrics.xdpi;
		float heightDpi = metrics.ydpi;
		
		//to know how many inches the device is.
		float widthInches = widthPixels / widthDpi;
		float heightInches = heightPixels / heightDpi;
		
		/*The size of the diagonal in inches 
		 * is equal to the square root of 
		 * the height in inches squared plus 
		 * the width in inches squared.
		 */
		double diagonalInches = Math.sqrt(
		    (widthInches * widthInches) 
		    + (heightInches * heightInches));
		
		if (diagonalInches >= 10) {
		    return TEN_INCHES_TABLET;
		} 
		else if (diagonalInches >= 7) {
		    return SEVEN_INCHES_TABLET;
		}else{
			return LOWER_SEVEN_INCHES;
		}
	}
	
	public int getWidthInPixel(){

		/*DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//get the density of the screen in form of scale factor (ldpi, mdpi, hdpi, xhdpi, xxhdpi)
		float scaleFactor = metrics.density;
		/*This will return the absolute value of the 
		 * width and the height in pixels, e.g. 1280x720 
		 * for the Galaxy SIII
		 */
		//int widthPixels = metrics.widthPixels;
		/*
		 * calculate the amount of density independent pixels (dpi) there are for 
		 * width and height we earlier calculated.
		 */
		/*float widthDp = widthPixels / scaleFactor;
		return widthDp;*/
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		
		return width;
	}
	
	public int getHeightInPixel(){

		/*
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		float scaleFactor = metrics.density;
		
		int heightPixels = metrics.heightPixels;
		float heightDp = heightPixels / scaleFactor;
		
		return heightDp;
		*/
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int height = displayMetrics.heightPixels;
		
		return height;
	}
	
	/**get all tower pieces inside the view
	 * Landscape:
	 * if 10 inches there are 16 tower pieces
	 * if 7 inches there are 8 tower pieces
	 * if lower then 7 inches there is only 1 tower piece
	 * 
	 * Portrait:
	 * if 10 inches there are 8 tower pieces
	 * if 7 inches there are 4 tower pieces
	 * if lower then 7 inches there is only 1 tower piece
	 * 
	 * the count of tower pieces is in maxVisualized
	 * the tower pieces memorized in towerPiecesArray
	 */
	private void getTowerPieceVisualized(){
		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		
		/*linearLayoutMargin = ((LinearLayout.LayoutParams)
				((LinearLayout)root.getChildAt(0)).getLayoutParams())
				.leftMargin;*/
		
		linearLayoutMargin = getWidthInPixel() / 7 / 8;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(linearLayoutMargin, linearLayoutMargin, linearLayoutMargin, linearLayoutMargin);
		//((LinearLayout)root.getChildAt(0)).setLayoutParams(lp);
		
		//Log.i(TAG, linearLayoutMargin + "");
		Toast.makeText(this, linearLayoutMargin + "", Toast.LENGTH_SHORT).show();
		
		int k = 0;
		maxVisualized = 0;
		for (int i = 0; i < root.getChildCount(); i++){
			View towerView = root.getChildAt(i);
			if (towerView instanceof LinearLayout){
				LinearLayout towerLayout = (LinearLayout) towerView;
				towerLayout.setLayoutParams(lp);
				for (int j = 0; j < towerLayout.getChildCount(); j++){
					if (towerLayout.getChildAt(j) instanceof CompositeTowerPiece){
						towerPiecesArray[k] = (CompositeTowerPiece)towerLayout.getChildAt(j);
						k++;
						maxVisualized++;
					}
				}
			}else if (towerView instanceof CompositeTowerPiece){
				towerPiecesArray[k] = (CompositeTowerPiece)towerView;
				k++;
				maxVisualized++;
			}
		}
	}
	
	/**
	 * This method allow the user to set some settings
	 */
	private void actionSettings(){
		Intent intent = new Intent(this, PreferencesActivity.class);
		startActivityForResult(intent, REQUEST_PREFERENCES);
	}
	
	/**
	 * This method allow the user to select the width and height of tower piece 
	 * so he can view the tower piece in some different way
	 */
	private void zoomSelect(){
		
		for (int i = 0; i < maxVisualized; ++i){
			TowerPieceSupport towerPieceSupport = hashMap.get(zoomSelected);
			towerPiecesArray[i].setTowerWidth(towerPieceSupport.getWidth());
			towerPiecesArray[i].setTowerHeight(towerPieceSupport.getHeight());
		}

		startAnimation();
	}
	
	private void initZoom(){
		TowerPieceSupport towerPieceSupport = new TowerPieceSupport();
		towerPieceSupport.setWidth(widthInDpi);
		towerPieceSupport.setHeight(heightInDpi);
		hashMap.put(ONE_TOWER_PIECE, towerPieceSupport);
		towerPieceSupport = new TowerPieceSupport();
		towerPieceSupport.setWidth(widthInDpi);
		towerPieceSupport.setHeight(heightInDpi / TWO_TOWER_PIECE);
		hashMap.put(TWO_TOWER_PIECE, towerPieceSupport);
		towerPieceSupport = new TowerPieceSupport();
		towerPieceSupport.setWidth(widthInDpi);
		towerPieceSupport.setHeight(heightInDpi / FOUR_TOWER_PIECE);
		hashMap.put(FOUR_TOWER_PIECE, towerPieceSupport);
		towerPieceSupport = new TowerPieceSupport();
		towerPieceSupport.setWidth(widthInDpi / TWO_TOWER_PIECE);
		towerPieceSupport.setHeight(heightInDpi / FOUR_TOWER_PIECE);
		hashMap.put(EIGHT_TOWER_PIECE, towerPieceSupport);
		towerPieceSupport = new TowerPieceSupport();
		towerPieceSupport.setWidth(widthInDpi / FOUR_TOWER_PIECE);
		towerPieceSupport.setHeight(heightInDpi / FOUR_TOWER_PIECE);
		hashMap.put(SIXTEEN_TOWER_PIECE, towerPieceSupport);
		int devicesInches = getInchesOfDevice();
		switch (devicesInches){
			case TEN_INCHES_TABLET:
				zoomSelected = SIXTEEN_TOWER_PIECE;
				break;
			case SEVEN_INCHES_TABLET:
				zoomSelected = EIGHT_TOWER_PIECE;
				break;
			case LOWER_SEVEN_INCHES:
				zoomSelected = ONE_TOWER_PIECE;
				break;
			default:
		}

        //initializeSharedPreferences();
		initializePreferences();
		zoomSelect();
	}
	
	/**
	 * Start Animation
	 */
	private void startAnimation(){

		//reset
		if (cdt != null){
			cdt.cancel();
			cdt = null;
		}
		status = STATUS_INIT_VALUES + ONE_TOWER_PIECE;
		horizontalSV.scrollTo(0, horizontalSV.getScrollY());
		verticalSV.scrollTo(verticalSV.getScrollX(), 0);
		
		//for width to move
		final int widthMove = linearLayoutMargin + widthInDpi;
		
		switch (zoomSelected) {
		case ONE_TOWER_PIECE:
			cdt = new CountDownTimer(seconds * 1000 * (NUMBER_NODES / 2 / ONE_TOWER_PIECE), seconds * 1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {

					//Log.i(TAG, "" + status);
					if (status == FOUR_TOWER_PIECE || status == EIGHT_TOWER_PIECE || status == TWELVE_TOWER_PIECE){
						horizontalSV.smoothScrollTo(horizontalSV.getScrollX() + widthMove, horizontalSV.getScrollY());
						verticalSV.smoothScrollTo(verticalSV.getScrollX(), 0);
					}else{
						verticalSV.smoothScrollTo(verticalSV.getScrollX(), verticalSV.getScrollY() + heightInDpi);
					}
					if (status < SIXTEEN_TOWER_PIECE)
						status++;
					else if (status == SIXTEEN_TOWER_PIECE){
						status = STATUS_INIT_VALUES + ONE_TOWER_PIECE;
						horizontalSV.smoothScrollTo(0, horizontalSV.getScrollY());
						verticalSV.smoothScrollTo(verticalSV.getScrollX(), 0);
					}
				}
				
				@Override
				public void onFinish() {
					this.start();
				}
			}.start();
			break;
		case TWO_TOWER_PIECE:
			cdt = new CountDownTimer(seconds * 1000 * (NUMBER_NODES / 2 / TWO_TOWER_PIECE), seconds * 1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					if (status == (FOUR_TOWER_PIECE / TWO_TOWER_PIECE) || status == (EIGHT_TOWER_PIECE / TWO_TOWER_PIECE) || status == (TWELVE_TOWER_PIECE / TWO_TOWER_PIECE)){
						horizontalSV.smoothScrollTo(horizontalSV.getScrollX() + widthMove, horizontalSV.getScrollY());
						verticalSV.smoothScrollTo(verticalSV.getScrollX(), 0);
					}else{
						verticalSV.smoothScrollTo(verticalSV.getScrollX(), verticalSV.getScrollY() + heightInDpi);
					}
					if (status < (SIXTEEN_TOWER_PIECE / TWO_TOWER_PIECE))
						status++;
					else if (status == (SIXTEEN_TOWER_PIECE / TWO_TOWER_PIECE)){
						status = STATUS_INIT_VALUES + ONE_TOWER_PIECE;
						horizontalSV.smoothScrollTo(0, horizontalSV.getScrollY());
						verticalSV.smoothScrollTo(verticalSV.getScrollX(), 0);
					}
				}
				
				@Override
				public void onFinish() {
					this.start();
				}
			}.start();
			break;
		case FOUR_TOWER_PIECE:
			cdt = new CountDownTimer(seconds * 1000 * (NUMBER_NODES / 2 / FOUR_TOWER_PIECE), seconds * 1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					if (status < FOUR_TOWER_PIECE){
						status++;
						horizontalSV.smoothScrollTo(horizontalSV.getScrollX() + widthMove, horizontalSV.getScrollY());
					}else{
						status = STATUS_INIT_VALUES + ONE_TOWER_PIECE;
						horizontalSV.smoothScrollTo(0, horizontalSV.getScrollY());
					}
				}
				
				@Override
				public void onFinish() {
					this.start();
				}
			}.start();
			break;
		case EIGHT_TOWER_PIECE:
				cdt = new CountDownTimer(seconds * 1000 * (NUMBER_NODES / 2 / EIGHT_TOWER_PIECE), seconds * 1000) {
				
					@Override
					public void onTick(long millisUntilFinished) {
						if (status < TWO_TOWER_PIECE){
							status++;
							horizontalSV.smoothScrollTo(horizontalSV.getScrollX() + widthMove, horizontalSV.getScrollY());
						}else{
							status = STATUS_INIT_VALUES + ONE_TOWER_PIECE;
							horizontalSV.smoothScrollTo(0, horizontalSV.getScrollY());
						}
					}
					
					@Override
					public void onFinish() {
						this.start();
					}
				}.start();
				break;
		case SIXTEEN_TOWER_PIECE:
			//nothing all pieces are visible
			break;
		default:
			break;
		}
	}
	
	//TODO - remove SuppressWarnings when you put the correct url 
	//correct url must be something like this
	//e.g. server_raspberries_info.php?id=1, server_raspberries_info.php?id=2, etc. 
	@SuppressWarnings("unused")
	private void retrieveAndSetRaspDetailsFromWebServer(){
		new CountDownTimer(networkRefresh * 1000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				for (int i = 1; i <= NUMBER_NODES; i++){
					new DownloadRaspberriesDetails(URL_JSON_RASPBERRY + i, 
							new DownloadRaspberriesDetailsInterface() {
						
						@Override
						public void getResult(String result) {
							if (!result.equals(""))
								createClusterFromJson(result);
						}
					}).execute();
				}
			}
			
			@Override
			public void onFinish() {
				this.start();
			}
		}.start();
	}
	
	private static void createClusterFromJson(String json){
		try {
			Node node = cluster.setClusterNode(json);
			int indexNode = node.getId();
			if (indexNode % 2 == 0){
				indexNode = indexNode / 2 - 1;
				towerPiecesArray[indexNode].setTemperatureRasp2(node.getTemperatureValue());
				towerPiecesArray[indexNode].setSpeedometerRasp2(node.getClockFrequency());
				towerPiecesArray[indexNode].setNetworkLoadUpRasp2(node.getNetworkLoad()[0]);
				towerPiecesArray[indexNode].setNetworkLoadDownRasp2(node.getNetworkLoad()[1]);
				towerPiecesArray[indexNode].setRamTotalValueRasp2(node.getRamLoad()[0]);
				towerPiecesArray[indexNode].setRamUsedValueRasp2(node.getRamLoad()[1]);
				towerPiecesArray[indexNode].setCpusWorkLoadRasp2(node.getCoresLoad()[0]);
				towerPiecesArray[indexNode].setStorageTotalValueRasp2(node.getSdStorageLoad()[0]);
				towerPiecesArray[indexNode].setStorageUsedValueRasp2(node.getSdStorageLoad()[1]);
			}else{
				indexNode /= 2;
				towerPiecesArray[indexNode].setTemperatureRasp1(node.getTemperatureValue());
				towerPiecesArray[indexNode].setSpeedometerRasp1(node.getClockFrequency());
				towerPiecesArray[indexNode].setNetworkLoadUpRasp1(node.getNetworkLoad()[0]);
				towerPiecesArray[indexNode].setNetworkLoadDownRasp1(node.getNetworkLoad()[1]);
				towerPiecesArray[indexNode].setRamTotalValueRasp1(node.getRamLoad()[0]);
				towerPiecesArray[indexNode].setRamUsedValueRasp1(node.getRamLoad()[1]);
				towerPiecesArray[indexNode].setCpusWorkLoadRasp1(node.getCoresLoad()[0]);
				towerPiecesArray[indexNode].setStorageTotalValueRasp1(node.getSdStorageLoad()[0]);
				towerPiecesArray[indexNode].setStorageUsedValueRasp1(node.getSdStorageLoad()[1]);
			}
		} catch (MalformedJsonException e) {
			e.printStackTrace();
			Log.e("prova", e.getMessage());
		}
	}
	
	/**
	 * Only for test
	 */
	/*boolean su1 = true;
	boolean su2 = false;
	private void randomValue(){
		new CountDownTimer(100000, 100) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				for (int i = 0; i < maxVisualized; i++){
					float currentValue1 = towerPiecesArray[i].getTemperatureRasp1();
					if (currentValue1 <= towerPiecesArray[i].getMinValueRasp1Temperature())
						su1 = true;
					else if (currentValue1 >= towerPiecesArray[i].getMaxValueRasp1Temperature())
						su1 = false;
					if (su1){
						currentValue1 = currentValue1 + 1;
					}else{
						currentValue1 = currentValue1 - 1;
					}
					towerPiecesArray[i].setTemperatureRasp1(currentValue1);
					towerPiecesArray[i].setSpeedometerRasp1(currentValue1);
					
					float currentValue2 = towerPiecesArray[i].getTemperatureRasp2();
					if (currentValue2 <= towerPiecesArray[i].getMinValueRasp2Temperature())
						su2 = true;
					else if (currentValue2 >= towerPiecesArray[i].getMaxValueRasp2Temperature())
						su2 = false;
					if (su2){
						currentValue2 = currentValue2 + 1;
					}else{
						currentValue2 = currentValue2 - 1;
					}
					towerPiecesArray[i].setTemperatureRasp2(currentValue2);
					towerPiecesArray[i].setSpeedometerRasp2(currentValue2);
				}
			}
			
			@Override
			public void onFinish() {
				Toast.makeText(getApplicationContext(), "fine countdown", Toast.LENGTH_SHORT).show();
			}
		}.start();
	}*/
}