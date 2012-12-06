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

		mLapFragment.competitionId = selectedCompetionId;
		mLapFragment.initPager();
		// mLapFragment.setCompetitionId(this, selectedCompetionId);
	}
}
