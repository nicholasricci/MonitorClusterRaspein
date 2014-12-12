/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.bluetooth;

import it.unibo.monitorhpc.gui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class ConnectedThread extends Thread {
	public static final String TAG = "ConnectedThread";
	
	public static final int MESSAGE_READ = 99;
	
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
 
    private Handler handler;
    
    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.handler = handler;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        Log.i(TAG, "Fine costruttore ConnectedThread");
    }
 
    public void run() {
        
        byte[] buffer;
        String s;
 
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
            	//bufferFlush(buffer);
                buffer = new byte[1024];
            	mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                //Handler mHandler = new Handler();
                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                //        .sendToTarget();
                //String str = new String(buffer, "UTF-8");
                /*tringBuilder sb = new StringBuilder();
                int i = 0;
                while (buffer[i] != 0){
                	sb.append((char)buffer[i]);
                	i++;
                }*/
            	
                s = new String(buffer, "UTF-8");
            	
                handler.obtainMessage(MainActivity.MESSAGE_READ, -1, -1, s).sendToTarget();                
                
                /*Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.MESSAGE_READ, sb.toString());
                msg.setData(bundle);
                handler.sendMessage(msg);*/
                
                Log.i(TAG, s);
            } catch (IOException e) {
            	Log.i(TAG, e.getMessage());
                break;
            }
        }
    }
 
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
    
    /*
    private void bufferFlush(byte[] buffer){
    	for (int i = 0; i < buffer.length; ++i){
    		buffer[i] = 0;
    	}
    }
	*/
}