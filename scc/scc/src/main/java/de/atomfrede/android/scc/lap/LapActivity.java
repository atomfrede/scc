package de.atomfrede.android.scc.lap;

import android.support.v4.app.FragmentActivity;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.Competition;
import de.atomfrede.android.scc.dao.CompetitionDao;

@EActivity(R.layout.activity_lap)
public class LapActivity extends FragmentActivity {

	@App
	SccApplication mApplication;

	@FragmentById(R.id.lapFragment)
	public LapFragment mLapFragment;

	public CompetitionDao competitionDao;

	public Competition selectedCompetition;

	@Extra
	public long selectedCompetionId;

	@Extra
	public String competitionName;

	@Extra
	public String competionNumber;

	@AfterInject
	public void getDao() {
		competitionDao = mApplication.competitonDao;

		// Loading the selected competition and all laps for this competition
		selectedCompetition = competitionDao.load(selectedCompetionId);

	}

	@AfterViews
	public void afterViews() {
		getActionBar().setTitle(
				getResources().getString(R.string.competition_heading_text)
						.replace("$i$", competionNumber));
		getActionBar().setSubtitle(competitionName);

		mLapFragment.setCompetitionId(this, selectedCompetionId);
	}
}
