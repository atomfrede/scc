package de.atomfrede.android.scc.lap;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.viewpagerindicator.TitlePageIndicator;

import de.atomfrede.android.scc.R;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

@EFragment
public class LapFragment extends Fragment {

	@ViewById(R.id.pager)
	ViewPager mPager;
	
	@ViewById(R.id.titles)
	TitlePageIndicator mIndicator;
}
