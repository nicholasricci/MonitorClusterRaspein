/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.dialogfragment;

import it.unibo.monitorhpc.R;
import it.unibo.monitorhpc.gui.MainActivity;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ZoomMenu extends DialogFragment implements OnItemClickListener{

	ArrayList<String> list;
	ListView listView;
	
	ZoomMenuInterface zoomMenuInterface;
	
	public ZoomMenu(ArrayList<String> list){
		this.list = list;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_zoom_menu, container, false);
        listView = (ListView) view.findViewById(R.id.zoom_menu_list);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

    }
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		listView.setAdapter(null);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			zoomMenuInterface.setZoomSelected(MainActivity.ONE_TOWER_PIECE);
			break;
		case 1:
			zoomMenuInterface.setZoomSelected(MainActivity.TWO_TOWER_PIECE);
			break;
		case 2:
			zoomMenuInterface.setZoomSelected(MainActivity.FOUR_TOWER_PIECE);
			break;
		case 3:
			zoomMenuInterface.setZoomSelected(MainActivity.EIGHT_TOWER_PIECE);
			break;
		case 4:
			zoomMenuInterface.setZoomSelected(MainActivity.SIXTEEN_TOWER_PIECE);
			break;
		default:
			break;
		}
		
		dismiss();
	}
	
	public interface ZoomMenuInterface{
		public void setZoomSelected(int zoom);
	}
	
	public void setZoomMenuInterface(ZoomMenuInterface zoomMenuInterface){
		this.zoomMenuInterface = zoomMenuInterface;
	}
}
