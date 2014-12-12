/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class AcceptThread extends Thread{
	private static final String TAG = "AcceptThread";
	private final BluetoothServerSocket mmServerSocket;
	private final BluetoothAdapter mBluetoothAdapter;
	
	public static final String MY_UUID = "6209b6a0-e676-11e3-ac10-0800200c9a66";
	public static final String NAME = "bluetoothConnection";
	
	private Handler handler;
	 
    public AcceptThread(BluetoothAdapter mBluetoothAdapter, Handler handler) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.handler = handler;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            //tmp = this.mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString(MY_UUID));
            tmp = this.mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, UUID.fromString(MY_UUID));
            Log.i(TAG, "creazione del servizio sdp riuscita");
        } catch (IOException e) {
        	Log.i(TAG, "creazione del servizio sdp non riuscita");
        }
        mmServerSocket = tmp;
    }
 
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
                Log.i(TAG, "server in ascolto");
            } catch (IOException e) {
            	Log.i(TAG, "Distruzione del server");
                break;
            }
            // If a connection was accepted
            if (socket != null) {
            	Log.i(TAG, "server in ascolto");
                // Do work to manage the connection (in a separate thread)
                ConnectedThread ct = new ConnectedThread(socket, handler);
                ct.start();
                try {
					mmServerSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
                break;
            }
        }
    }
 
    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}
