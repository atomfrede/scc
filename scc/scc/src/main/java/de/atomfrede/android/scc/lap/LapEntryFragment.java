package de.atomfrede.android.scc.lap;

import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.LapEntry;

@EFragment(R.layout.fragment_lap_entry)
public class LapEntryFragment extends Fragment {

	public List<LapEntry> lapEntries;

	@App
	public SccApplication mApplication;
	
	@ViewById(R.id.entryList)
	public ListView entryList;

	public LapEntryListAdapter mEntryAdapter;

	@AfterViews
	public void fillList(){
		entryList.setAdapter(mEntryAdapter);
	}
	
	public void setLapEntries(Activity context, List<LapEntry> lapEntries) {
		this.lapEntries = lapEntries;
		Log.d("Entries", lapEntries.size()+"");
		Log.d("Activity", context+"");
		mEntryAdapter = new LapEntryListAdapter(context, lapEntries);
//		entryList.setAdapter(mEntryAdapter);

	}

}
