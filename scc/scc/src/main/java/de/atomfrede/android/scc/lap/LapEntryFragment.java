/*
*	 SCC - The Sprintercup Companion App provides you with the Meldeergbnis right on your smartphone
*    
*    Copyright (C) 2012  Frederik Hahne <atomfrede@gmail.com>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
		mEntryAdapter = new LapEntryListAdapter(this.getActivity(), lapId, lapEntries);
		entryList.setAdapter(mEntryAdapter);
	}

}
