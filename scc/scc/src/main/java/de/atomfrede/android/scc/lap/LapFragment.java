package de.atomfrede.android.scc.lap;

import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.viewpagerindicator.TitlePageIndicator;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.CompetitionDao;
import de.atomfrede.android.scc.dao.Lap;

@EFragment(R.layout.fragment_lap)
public class LapFragment extends Fragment {

	@App
	SccApplication mApplication;
	
	@ViewById(R.id.pager)
	ViewPager mPager;
	
	@ViewById(R.id.titles)
	TitlePageIndicator mIndicator;
	
	long competitionId;
	
	LapPagerAdapter mPagerAdapter;

	public void setCompetitionId(Activity context, long competitionId){
		this.competitionId = competitionId;
		mPagerAdapter = new LapPagerAdapter(getFragmentManager(), this.competitionId, context);
		mPager.setAdapter(mPagerAdapter);
		mIndicator.setViewPager(mPager);
	}
	
	public final class LapPagerAdapter extends FragmentPagerAdapter {
		
		CompetitionDao competitionDao;
		List<Lap> laps;
		Activity context;
		
		public LapPagerAdapter(FragmentManager fm, long competitionId, Activity context){
			super(fm);
			competitionDao = mApplication.competitonDao;
			laps = competitionDao.load(competitionId).getLapList();
			this.context = context;
			
		}

		@Override
		public Fragment getItem(int position) {
			return LapEntryFragment.newInstance(laps.get(position).getId());
		}

		@Override
		public int getCount() {
			return laps.size();
		}
		
		public CharSequence getPageTitle(int position){
			return getActivity().getResources().getString(R.string.lap_header).replace("$i$", position+1+"");
		}

	}
}
