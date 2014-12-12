/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.gui;

import it.unibo.monitorhpc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseMode extends Activity{

	//Constants for start MainActivity server or bluetooth
	public static final int SERVER_MODE = 1;
	public static final int BLUETOOTH_MODE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mode);
		
		Button btnServer = (Button) findViewById(R.id.choosemode_server);
		btnServer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChooseMode.this, MainActivity.class);
				intent.putExtra(MainActivity.ACTIVITY_MODE, SERVER_MODE);
				startActivity(intent);
			}
		});
		
		Button btnBluetooth = (Button) findViewById(R.id.choosemode_bluetooth);
		btnBluetooth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChooseMode.this, MainActivity.class);
				intent.putExtra(MainActivity.ACTIVITY_MODE, BLUETOOTH_MODE);
				startActivity(intent);
			}
		});
	}
	
}
