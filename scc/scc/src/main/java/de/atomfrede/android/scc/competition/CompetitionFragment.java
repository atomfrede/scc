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
package de.atomfrede.android.scc.competition;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import au.com.bytecode.opencsv.CSVReader;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.Competition;
import de.atomfrede.android.scc.dao.CompetitionDao;
import de.atomfrede.android.scc.dao.Lap;
import de.atomfrede.android.scc.dao.LapDao;
import de.atomfrede.android.scc.dao.LapEntry;
import de.atomfrede.android.scc.dao.LapEntryDao;
import de.atomfrede.android.scc.lap.LapActivity_;

@EFragment(R.layout.fragment_competition)
public class CompetitionFragment extends Fragment {

	private static final String TAG = "Scc:CompetitionFragment";

	public static final int DATABASE_COMPETIONS = 0;
	public static final int DATABASE_LAPS = 1;
	public static final int DATABASE_LAP_ENTRIES = 2;
	
	@App
	SccApplication mApplication;

	CompetitionDao competitionDao;
	LapDao lapDao;
	LapEntryDao lapEntryDao;

	@ViewById(R.id.loading_text)
	TextView loadingTextView;

	@ViewById(R.id.loading_lap_text)
	TextView loadingLapTextView;

	@ViewById(R.id.loading_progress)
	ProgressBar loadingProgressBar;

	@ViewById(R.id.competion_list)
	ListView mListView;

	ListItemAdapter mAdapater;

	@AfterViews
	public void init() {
		readData();
	}

	@ItemClick(R.id.competion_list)
	public void myListItemClicked(int position) {
		Competition clickedCompetion = mAdapater.getItem(position);
		LapActivity_.intent(this.getActivity()).competionNumber(clickedCompetion.getCompetitionNumber() + "").competitionName(clickedCompetion.getName()).selectedCompetionId(clickedCompetion.getId()).start();
	}

	@UiThread
	public void onDataLoaded(boolean success) {
		mAdapater = new ListItemAdapter(this.getActivity(), competitionDao.loadAll());
		mListView.setAdapter(mAdapater);

		loadingTextView.setVisibility(View.GONE);
		loadingProgressBar.setVisibility(View.GONE);
		loadingLapTextView.setVisibility(View.GONE);

		mListView.setVisibility(View.VISIBLE);

	}

	@UiThread
	public void updateInformation(int competitionNumber, int lapNumber) {
		loadingTextView.setText(getResources().getString(R.string.loading_competition).replace("$i$", competitionNumber + ""));
		if (loadingLapTextView.getVisibility() == View.GONE || loadingLapTextView.getVisibility() == View.INVISIBLE) {
			loadingLapTextView.setVisibility(View.VISIBLE);
		}
		loadingLapTextView.setText(getResources().getString(R.string.loading_lap).replace("$i$", lapNumber + ""));
	}

	@UiThread
	public void updateWriteToDatabase() {
		loadingLapTextView.setVisibility(View.GONE);
		loadingTextView.setText(getResources().getString(R.string.loading_database));
	}
	
	@UiThread
	public void updateWriteDatabaseStep(int databaseStep){
		switch (databaseStep) {
		case DATABASE_COMPETIONS:
			loadingLapTextView.setVisibility(View.VISIBLE);
			loadingLapTextView.setText(getResources().getString(R.string.loading_database_competitions));
			break;
		case DATABASE_LAPS:
			loadingLapTextView.setText(getResources().getString(R.string.loading_database_laps));
			break;
		case DATABASE_LAP_ENTRIES:
			loadingLapTextView.setText(getResources().getString(R.string.loading_database_participants));
			break;
		default:
			break;
		}
	}

	@Background
	public void readData() {
		boolean success = true;
		try {
			competitionDao = mApplication.competitonDao;
			lapDao = mApplication.lapDao;
			lapEntryDao = mApplication.lapEntryDao;

			if (competitionDao.count() > 0) {
				// Data is already loaded...
				return;
			}

			// TODO close all streams properly
			InputStream in = getResources().openRawResource(R.raw.me_2012);
			CSVReader reader = new CSVReader(new InputStreamReader(in, "Cp1252"), ';');

			Integer currentCompetitionNumber = null;
			Integer currentLapNumber = null;

			/**
			 * List of all Competitions
			 */
			List<Competition> competitions = new ArrayList<Competition>();
			/**
			 * Mapping from competition number to all laps for that competition
			 */
			Map<String, List<Lap>> competitionNumber_lap = new HashMap<String, List<Lap>>();

			/**
			 * List of all laps for a competitions
			 */
			List<Lap> laps = new ArrayList<Lap>();

			/**
			 * Mapping from competitionNumber_lapNumber to all LapEntries for
			 * that specific lap, which can be determined by cNumber and
			 * lapnumber
			 */
			Map<String, List<LapEntry>> lap_comp_number_lapEntries = new HashMap<String, List<LapEntry>>();

			/**
			 * List of all lap entries for one lap
			 */
			List<LapEntry> lapEntries = new ArrayList<LapEntry>();

			Competition currentCompetition = new Competition();
			Competition oldCompetition = new Competition();

			Lap currentLap = new Lap();
			Lap oldLap = new Lap();


			// updateInformation(currentCompetitionNumber, 1);

			String line[] = reader.readNext();
			while (line != null) {

				if (!line[0].equals("WK-Nr")) {
					// Not reading the header line
					// First check if we need to create a new competition
					// element
					String competitionNumberText = line[0];
					String lapNumberText = line[1];
					String competitionName = line[3];
					String firstname = line[6];
					String lastname = line[7];
					String year = line[8];
					String club = line[9];
					String time = line[10];

					int lane = Integer.parseInt(line[2]);

					int competitionNumber = Integer.parseInt(competitionNumberText);
					int lapNumber = Integer.parseInt(lapNumberText);

					updateInformation(competitionNumber, lapNumber);

					LapEntry entry = new LapEntry();
					entry.setCompetitionNumber(competitionNumber);
					entry.setLapNumber(lapNumber);
					entry.setFirstname(firstname);
					entry.setLane(lane);
					entry.setLastname(lastname);
					entry.setClub(club);
					entry.setTime(time);
					entry.setYear(year);

					String key = competitionNumber + "_" + lapNumber;

					// Inital Check
					if (currentCompetition.getCompetitionNumber() == null) {
						currentCompetition.setCompetitionNumber(competitionNumber);
						currentCompetition.setName(competitionName);
						oldCompetition = currentCompetition;
						competitions.add(currentCompetition);
						competitionNumber_lap.put(currentCompetition.getCompetitionNumber() + "", laps);
					} else {
						currentCompetition = new Competition();
						currentCompetition.setCompetitionNumber(competitionNumber);
						currentCompetition.setName(competitionName);
					}
					if (currentLap.getLapNumber() == null) {
						currentLap.setCompetitionNumber(competitionNumber);
						currentLap.setLapNumber(lapNumber);
						currentLap.setIsDone(false);
						lap_comp_number_lapEntries.put(key, lapEntries);
						laps.add(currentLap);
						oldLap = currentLap;
					} else {
						currentLap = new Lap();
						currentLap.setIsDone(false);
						currentLap.setCompetitionNumber(competitionNumber);
						currentLap.setLapNumber(lapNumber);
					}

					if (currentCompetition.getCompetitionNumber().equals(oldCompetition.getCompetitionNumber())) {

						if (currentLap.getLapNumber().equals(oldLap.getLapNumber())) {
							lapEntries.add(entry);
						} else {
							laps.add(currentLap);
							lapEntries = new ArrayList<LapEntry>();
							lapEntries.add(entry);
							lap_comp_number_lapEntries.put(key, lapEntries);
						}

					} else {
						competitions.add(currentCompetition);
						// line is a new competition
						laps = new ArrayList<Lap>();
						lapEntries = new ArrayList<LapEntry>();

						laps.add(currentLap);
						competitionNumber_lap.put(currentCompetition.getCompetitionNumber() + "", laps);

						lapEntries.add(entry);
						lap_comp_number_lapEntries.put(key, lapEntries);
						// lap_lapEntries.put(currentLap, lapEntries);

					}
				}

				oldCompetition = currentCompetition;
				oldLap = currentLap;

				line = reader.readNext();

			}

			laps.add(currentLap);
			competitionNumber_lap.put(currentLap.getCompetitionNumber() + "", laps);
			// debugData(competitions);
			updateWriteToDatabase();

			writeToDatabase(competitions, competitionNumber_lap, lap_comp_number_lapEntries);
			// debugData(competitionDao.loadAll());
		} catch (Exception e) {
			Log.d(TAG, "Error During parsign data: " + e.getMessage(), e);
			success = false;
		} finally {
			onDataLoaded(success);
		}
	}

	private void writeToDatabase(List<Competition> competitions, Map<String, List<Lap>> competitionNumber_lap, Map<String, List<LapEntry>> lap_lapentries) {
		updateWriteDatabaseStep(DATABASE_COMPETIONS);
		
		competitionDao.insertInTx(competitions);
		List<Competition> dbCompetitions = competitionDao.loadAll();
		List<Lap> allLaps = new ArrayList<Lap>();
		List<LapEntry> allEntries = new ArrayList<LapEntry>();
		List<LapEntry> allDbEntries = new ArrayList<LapEntry>();


		for (Competition dbCompetition : dbCompetitions) {
			long id = dbCompetition.getId();
			List<Lap> laps = competitionNumber_lap.get(dbCompetition.getCompetitionNumber() + "");
			if (laps != null) {
				// Log.d(TAG, "Laps size is "+laps.size());
				for (Lap cLap : laps) {

					cLap.setCompetition(dbCompetition);
					cLap.setCompetitionId(id);
					allLaps.add(cLap);

					String key = cLap.getCompetitionNumber() + "_" + cLap.getLapNumber();
					List<LapEntry> entriesForLap = lap_lapentries.get(key);

					
					if (entriesForLap != null) {
						allEntries.addAll(entriesForLap);
					} else {

					}

				}
			} else {
				Log.d(TAG, "Laps *IS* Null");
			}
		}
		
		updateWriteDatabaseStep(DATABASE_LAPS);
		lapDao.insertInTx(allLaps);

		for (Lap dbLap : lapDao.loadAll()) {

			String key = dbLap.getCompetitionNumber() + "_" + dbLap.getLapNumber();
			List<LapEntry> entriesForDbLap = lap_lapentries.get(key);
			if (entriesForDbLap != null) {
				for (LapEntry cEntry : entriesForDbLap) {
					cEntry.setLap(dbLap);
					cEntry.setCompetitionNumber(dbLap.getCompetitionNumber());
					cEntry.setLapId(dbLap.getId());
					allDbEntries.add(cEntry);
				}
			}

		}

		updateWriteDatabaseStep(DATABASE_LAP_ENTRIES);
		lapEntryDao.insertInTx(allDbEntries);

	}

	private void debugData(List<Competition> competitionList) {
		for (Competition cComp : competitionList) {
			cComp.resetLapList();
			// Log.d(TAG, "Competition: " + cComp.getCompetitionNumber() + " "
			// + cComp.getName());
			writeLaps(cComp.getLapList());
		}
	}

	private void writeLaps(List<Lap> laps) {
		for (Lap cLap : laps) {
			cLap.resetLapEntryList();
			Log.d(TAG, "Lap " + cLap.getLapNumber());
			for (LapEntry cEntry : cLap.getLapEntryList()) {
				Log.d(TAG, " " + cEntry.getFirstname() + " " + cEntry.getLastname() + " " + cEntry.getYear() + " " + cEntry.getClub() + " " + cEntry.getTime());
			}
		}
	}

}
