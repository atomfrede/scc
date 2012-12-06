package de.atomfrede.android.scc.lap;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.LapEntry;

@EFragment(R.layout.fragment_lap_entry)
public class LapEntryFragment extends Fragment {

	public static LapEntryFragment newInstance(long lapId) {
		LapEntryFragment_ newFragment = new LapEntryFragment_();
		Bundle args = new Bundle();
		args.putLong("lapId", lapId);
		newFragment.setArguments(args);
		return newFragment;
	}

	public List<LapEntry> lapEntries;

	@App
	public SccApplication mApplication;

	@FragmentArg("lapId")
	public long lapId;

	@ViewById(R.id.entryList)
	public ListView entryList;

	public LapEntryListAdapter mEntryAdapter;

	@AfterInject
	public void loadData() {
		lapEntries = mApplication.lapDao.load(lapId).getLapEntryList();
	}

	@AfterViews
	public void fillList() {
		mEntryAdapter = new LapEntryListAdapter(this.getActivity(), lapEntries);
		entryList.setAdapter(mEntryAdapter);
	}

}
