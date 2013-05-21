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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.ViewById;
import com.viewpagerindicator.TitlePageIndicator;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.Competition;
import de.atomfrede.android.scc.dao.CompetitionDao;
import de.atomfrede.android.scc.dao.Lap;
import de.atomfrede.android.scc.dao.LapDao;
import de.atomfrede.android.scc.dao.LapEntryDao;

@EFragment(R.layout.fragment_lap)
public class LapFragment extends Fragment {

	public static LapFragment_ newInstance(long competitionId) {
		LapFragment_ f = new LapFragment_();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putLong("competitionId", competitionId);
		f.setArguments(args);

		return f;
	}

	@App
	SccApplication mApplication;

	CompetitionDao mCompetitionDao;
	LapDao mLapDao;
	LapEntryDao mLapEntryDao;

	Competition mCompetition;

	List<Lap> mLaps;

	@ViewById(R.id.pager)
	ViewPager mPager;

	@ViewById(R.id.titles)
	TitlePageIndicator mIndicator;

	@FragmentArg("competitionId")
	public long competitionId;

	LapPagerAdapter mPagerAdapter;

    @InstanceState
	public long selectedLapId;
	
    @InstanceState
	public int lastSelectedPagerPositon;

	public long getShownCompetionId() {
		return getArguments().getLong("competitionId", -1);
	}

	@AfterViews
	public void initPager() {
		Log.d("LapFragment", "Competition ID "+competitionId);
//		Log.d("LapFragment", "GetShownCompetition ID "+getShownCompetionId());
		if (competitionId != 0) {
			mCompetitionDao = mApplication.competitonDao;
			mCompetition = mCompetitionDao.load(competitionId);
			mLapDao = mApplication.lapDao;
			mLapEntryDao = mApplication.lapEntryDao;
			mLaps = mCompetition.getLapList();

			mPagerAdapter = new LapPagerAdapter(getFragmentManager(), this.competitionId, this.getActivity());
			mPager.setAdapter(mPagerAdapter);
			mIndicator.setViewPager(mPager);

			mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					lastSelectedPagerPositon = position;
					selectedLapId = mLaps.get(position).getId();

					mCompetition.setLastSelectedLapPosition(lastSelectedPagerPositon);
					mCompetitionDao.update(mCompetition);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});

			mIndicator.setCurrentItem(mCompetition.getLastSelectedLapPosition());
		}
	}

	public void markAsDone() {
		Log.d("Laps", "mPagerAdapter.position_fragment.size() "+(mPagerAdapter.position_fragment.size()));
		if (selectedLapId == 0) {
			// 0 is the initial value...
			selectedLapId = mLaps.get(lastSelectedPagerPositon).getId();
		}

		boolean isDone = mLaps.get(lastSelectedPagerPositon).getIsDone();
		
		mLaps.get(lastSelectedPagerPositon).setIsDone(!isDone);
		Lap lap = mLapDao.load(selectedLapId);
		lap.setIsDone(!isDone);
		mLapDao.update(lap);

		
		try{
			mPagerAdapter.position_fragment.get(lastSelectedPagerPositon).mEntryAdapter.notifyDataSetChanged();
		}catch(Exception e){
			LapEntryFragment_ lapEntryFragment = (LapEntryFragment_)mPagerAdapter.instantiateItem(mPager, lastSelectedPagerPositon);
			lapEntryFragment.mEntryAdapter.notifyDataSetChanged();
		}

		if (!isDone && mPagerAdapter.getCount() > lastSelectedPagerPositon) {
			mIndicator.setCurrentItem(lastSelectedPagerPositon + 1);
		}
	}

	public final class LapPagerAdapter extends FragmentStatePagerAdapter {

		CompetitionDao competitionDao;
		Activity context;
		LruCache<Integer, LapEntryFragment> position_fragment;

		public LapPagerAdapter(FragmentManager fm, long competitionId, Activity context) {
			super(fm);
			competitionDao = mApplication.competitonDao;
			this.context = context;
			position_fragment = new LruCache<Integer, LapEntryFragment>(6);

		}

		@Override
		public Fragment getItem(int position) {
			Log.d("LapFragment", "GetItem");
			LapEntryFragment currentFragment = LapEntryFragment.newInstance(mLaps.get(position).getId());
			position_fragment.put(position, currentFragment);
			return currentFragment;
		}

		@Override
		public int getCount() {
			return mLaps.size();
		}

		public long getItemId(int position) {
			return mLaps.get(position).getId();
		}

		public CharSequence getPageTitle(int position) {
			return getActivity().getResources().getString(R.string.lap_header).replace("$i$", position + 1 + "");
		}
	}
}
