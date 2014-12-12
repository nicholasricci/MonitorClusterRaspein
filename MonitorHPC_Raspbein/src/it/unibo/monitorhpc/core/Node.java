/**
 * 
 */
package it.unibo.monitorhpc.core;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.MalformedJsonException;

/**
 * @author mel
 * Stores values of a Node in the Cluster.
 * Values are formatted following JSON data format
 * sent by the bluetooth server who retrieve it on
 * NFS who store data retrieved by external handler
 * module who manage Cluster's statistics.
 * 
{ <nome_raspberry_1>:{ temperatura:X, clock:Y, network:{ up:S, down:G },
					   ram: { tot:T, used:U }, procload:{ <cpu_1>: P1, .. ,<cpu_n>: PN },
					   sd:{ tot:T, used:U }
					 },
		........
	<nome_raspberry_n>:{ temperatura:X, clock:Y, network:{ up:S, down:G },
					   ram: { tot:T, used:U }, procload:{ <cpu_1>: P1, .. ,<cpu_n>: PN },
					   sd:{ tot:T, used:U }
					 }
}
 */
public class Node {
	private static final String TEMPERATURE = "temperature";
	private static final String CLOCK = "clock";
	private static final String NETWORK = "network";
	private static final String NETWORK_UP = "up";
	private static final String NETWORK_DOWN = "down";
	private static final String RAM = "ram";
	private static final String PROCLOAD = "procload";
	private static final String SD = "sd";
	private static final String MEMORY_TOT = "tot";
	private static final String MEMORY_USE = "used";
	
	private static final String ID = "id";
	
	private String name;
	private int temperatureValue;
	private int clockFrequency;
	private int[] networkLoad;
	private int[] ramLoad;
	private Integer[] coresLoad;
	private int[] sdStorageLoad;
	
	private int id;
	
	public Node(String nodeName, String nodeInfo) throws MalformedJsonException {
		JSONObject auxobj;
		JSONObject root;
		try {
			root = new JSONObject(nodeInfo);
			//root = root.getJSONObject(nodeName); // get the root of the raspberry infos
			this.name = nodeName;
			this.id = root.getInt(ID);
			this.temperatureValue = root.getInt(TEMPERATURE);
			this.clockFrequency = root.getInt(CLOCK);
			auxobj = root.getJSONObject(NETWORK);
			this.networkLoad = new int[] { auxobj.getInt(NETWORK_UP), auxobj.getInt(NETWORK_DOWN) };
			auxobj = root.getJSONObject(RAM);
			this.ramLoad = new int[] { auxobj.getInt(MEMORY_TOT), auxobj.getInt(MEMORY_USE) };
			auxobj = root.getJSONObject(PROCLOAD);
			loadProcs(auxobj);
			auxobj = root.getJSONObject(SD);
			this.sdStorageLoad = new int[] { auxobj.getInt(MEMORY_TOT), auxobj.getInt(MEMORY_USE) };
		} catch (JSONException e) {
			e.printStackTrace();
			throw new MalformedJsonException("Input cluster file was malformed. Abort.");
		}
		
	}
	
	private void loadProcs(JSONObject obj) {
		int i = 1;
		String cpu = "cpu";
		ArrayList<Integer> lst = new ArrayList<Integer>();
		try {
			while(true){
				lst.add(obj.getInt(cpu+i));
				++i;
			}
		}catch (JSONException je) {
			this.coresLoad = new Integer[lst.size()];
			this.coresLoad = lst.toArray(this.coresLoad);
		}
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the temperatureValue
	 */
	public int getTemperatureValue() {
		return temperatureValue;
	}
	/**
	 * @param temperatureValue the temperatureValue to set
	 */
	public void setTemperatureValue(int temperatureValue) {
		this.temperatureValue = temperatureValue;
	}
	/**
	 * @return the clockFrequency
	 */
	public int getClockFrequency() {
		return clockFrequency;
	}
	/**
	 * @param clockFrequency the clockFrequency to set
	 */
	public void setClockFrequency(int clockFrequency) {
		this.clockFrequency = clockFrequency;
	}
	/**
	 * @return the networkLoad
	 */
	public int[] getNetworkLoad() {
		return networkLoad;
	}
	/**
	 * @param networkLoad the networkLoad to set
	 */
	public void setNetworkLoad(int[] networkLoad) {
		this.networkLoad = networkLoad;
	}
	/**
	 * @return the ramLoad
	 */
	public int[] getRamLoad() {
		return ramLoad;
	}
	/**
	 * @param ramLoad the ramLoad to set
	 */
	public void setRamLoad(int[] ramLoad) {
		this.ramLoad = ramLoad;
	}
	/**
	 * @return the coresLoad
	 */
	public Integer[] getCoresLoad() {
		return coresLoad;
	}
	/**
	 * @param coresLoad the coresLoad to set
	 */
	public void setCoresLoad(Integer[] coresLoad) {
		this.coresLoad = coresLoad;
	}
	/**
	 * @return the sdStorageLoad
	 */
	public int[] getSdStorageLoad() {
		return sdStorageLoad;
	}
	/**
	 * @param sdStorageLoad the sdStorageLoad to set
	 */
	public void setSdStorageLoad(int[] sdStorageLoad) {
		this.sdStorageLoad = sdStorageLoad;
	}
	
	/**
	 * @return the id of raspberry
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id){
		this.id = id;
	}
	
}

