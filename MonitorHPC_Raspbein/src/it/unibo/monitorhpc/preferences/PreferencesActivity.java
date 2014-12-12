/**
 * @author Nicholas Ricci
 */
package it.unibo.monitorhpc.preferences;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

public class PreferencesActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PreferencesFragment prefFragment = new PreferencesFragment();
	    FragmentManager fragmentManager = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    fragmentTransaction.replace(android.R.id.content, prefFragment);
	    fragmentTransaction.commit();
	    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    
	    setResult(RESULT_OK);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {   
		switch (item.getItemId()) {
        case android.R.id.home:
            //called when the up affordance/carat in actionbar is pressed
            onBackPressed();
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
