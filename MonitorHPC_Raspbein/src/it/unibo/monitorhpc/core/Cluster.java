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
 * 
 */

public class Cluster {
	private static final String ID = "id";
	private static final String NODE_NAME = "raspberry";
	
	private ArrayList<Node> list;
	public Cluster(/*String clusterJSON*/)/* throws MalformedJsonException */{
		/*String rasp = "raspeberry";
		int nrasp = 1;*/
		list = new ArrayList<Node>(32);
		
		/*try {
			//while (true)
			while(nrasp <= 32) {
				list.add(new Node(rasp+nrasp, clusterJSON));
				++nrasp;
			}
		}catch(MalformedJsonException je) {
			String jsonFormat = "{ <nome_raspberry_1>:{ temperatura:X, clock:Y, network:{ up:S, down:G },"
					  + " ram: { tot:T, used:U }, procload:{ <cpu_1>: P1, .. ,<cpu_n>: PN }," 
					  + " sd:{ tot:T, used:U } },   	........       " 
					  + " <nome_raspberry_n>:{ temperatura:X, clock:Y, network:{ up:S, down:G }," 
					  + " ram: { tot:T, used:U }, procload:{ <cpu_1>: P1, .. ,<cpu_n>: PN },"
					  + " sd:{ tot:T, used:U } } }";
			je.printStackTrace();
			throw new MalformedJsonException("clusterJSON must be a file with a valid JSON format." + jsonFormat);
		}*/
	}
	
	public ArrayList<Node> getNodes(){
		return list;
	}
	
	public Node setClusterNode(String clusterJSON) throws MalformedJsonException{
		JSONObject root;
		Node node = null;
		try {
			root = new JSONObject(clusterJSON);
			int id = root.getInt(ID);
			node = new Node(NODE_NAME + id, clusterJSON);
			list.add(id - 1, node);
		} catch (JSONException je) {
			String jsonFormat = "{ <nome_raspberry_1>:{ temperatura:X, clock:Y, network:{ up:S, down:G },"
					  + " ram: { tot:T, used:U }, procload:{ <cpu_1>: P1, .. ,<cpu_n>: PN }," 
					  + " sd:{ tot:T, used:U } },   	........       " 
					  + " <nome_raspberry_n>:{ temperatura:X, clock:Y, network:{ up:S, down:G }," 
					  + " ram: { tot:T, used:U }, procload:{ <cpu_1>: P1, .. ,<cpu_n>: PN },"
					  + " sd:{ tot:T, used:U } } }";
			je.printStackTrace();
			throw new MalformedJsonException("clusterJSON must be a file with a valid JSON format." + jsonFormat);
		}
		return node;
	}
	
}
