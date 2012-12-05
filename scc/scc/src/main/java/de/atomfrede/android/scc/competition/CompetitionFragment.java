package de.atomfrede.android.scc.competition;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		LapActivity_.intent(this.getActivity()).competionNumber(clickedCompetion.getCompetitionNumber()+"").competitionName(clickedCompetion.getName()).selectedCompetionId(clickedCompetion.getId()).start();
	}

	@UiThread
	public void onDataLoaded(boolean success) {
		mAdapater = new ListItemAdapter(this.getActivity(),
				competitionDao.loadAll());
		mListView.setAdapter(mAdapater);

		loadingTextView.setVisibility(View.GONE);
		loadingProgressBar.setVisibility(View.GONE);
		loadingLapTextView.setVisibility(View.GONE);

		mListView.setVisibility(View.VISIBLE);

	}
	
	@UiThread
	public void updateInformation(int competitionNumber, int lapNumber){
		loadingTextView.setText(getResources().getString(R.string.loading_competition).replace("$i$", competitionNumber+""));
		if(loadingLapTextView.getVisibility() == View.GONE || loadingLapTextView.getVisibility() == View.INVISIBLE){
			loadingLapTextView.setVisibility(View.VISIBLE);
		}
		loadingLapTextView.setText(getResources().getString(R.string.loading_lap).replace("$i$", lapNumber+""));
	}
	
	@UiThread
	public void updateWriteToDatabase(){
		loadingLapTextView.setVisibility(View.GONE);
		loadingTextView.setText(getResources().getString(R.string.loading_database));
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

			int currentCompetitionNumber = 1;

			List<Competition> competitions = new ArrayList<Competition>();
			Map<String, List<Lap>> competitionNumber_lap = new HashMap<String, List<Lap>>();
			List<Lap> laps = new ArrayList<Lap>();
			Map<Lap, List<LapEntry>> lap_lapEntries = new HashMap<Lap, List<LapEntry>>();
			List<LapEntry> lapEntries = new ArrayList<LapEntry>();

			Competition currentCompetition = new Competition();

			currentCompetition.setCompetitionNumber(Integer
					.valueOf(currentCompetitionNumber));

			// competitionDao.insert(currentCompetition);

			Lap currentLap = new Lap();
			currentLap.setLapNumber(1);
			currentLap.setCompetitionNumber(1);

			updateInformation(currentCompetitionNumber, 1);
			
			String line[] = reader.readNext();
			while (line != null) {

				if (!line[0].equals("WK-Nr")) {
					// Not reading the header line
					// First check if we need to create a new competition
					// element
					if (Integer.parseInt(line[0]) != currentCompetitionNumber) {
						currentCompetition.setName(line[3]);
						// Add the complete competition (=WK) to our list of
						// competitions
						competitions.add(currentCompetition);

						currentCompetitionNumber = Integer.parseInt(line[0]);
						updateInformation(currentCompetitionNumber, 1);
						currentCompetition = new Competition();
						currentCompetition
								.setCompetitionNumber(currentCompetitionNumber);

						// Also create the first lap for the new competition
						currentLap = new Lap();
						currentLap.setLapNumber(1);
						currentLap
								.setCompetitionNumber(currentCompetitionNumber);
						laps = new ArrayList<Lap>();

					}
					// Now read the next lap for the current competion
					int newLapNumber = Integer.parseInt(line[1]);
					if (newLapNumber != currentLap.getLapNumber()) {
						// New Lap must be created, otherwise use the currentlap
						// If we have a new lap we must add the old lap to the
						// list of laps of the current competion
						// currentCompetition.getLapList().add(currentLap);
						laps.add(currentLap);

						competitionNumber_lap.put(
								currentLap.getCompetitionNumber() + "", laps);
						lap_lapEntries.put(currentLap, lapEntries);
						lapEntries = new ArrayList<LapEntry>();
						// laps = new ArrayList<Lap>();
						currentLap = new Lap();
						currentLap.setLapNumber(newLapNumber);
						updateInformation(currentCompetitionNumber, newLapNumber);
						currentLap
								.setCompetitionNumber(currentCompetitionNumber);
					}

					// Now read the entry for the current competition and
					// current lap

					String firstname = line[6];
					String lastname = line[7];
					String year = line[8];
					String club = line[9];
					String time = line[10];
					int lane = Integer.parseInt(line[2]);

					LapEntry entry = new LapEntry();
					entry.setFirstname(firstname);
					entry.setLastname(lastname);
					entry.setClub(club);
					entry.setTime(time);
					entry.setYear(year);
					entry.setLane(lane);

					// Log.d(TAG, entry.toString());
					lapEntries.add(entry);
					// currentLap.getLapEntryList().add(entry);

				}
				line = reader.readNext();
			}

			laps.add(currentLap);
			competitionNumber_lap.put(currentLap.getCompetitionNumber() + "",
					laps);
			// debugData(competitions);
			updateWriteToDatabase();
			writeToDatabase(competitions, competitionNumber_lap, lap_lapEntries);
			// debugData(competitionDao.loadAll());
		} catch (Exception e) {
			Log.d(TAG, "Error During parsign data: " + e.getMessage(), e);
			success = false;
		} finally {
			onDataLoaded(success);
		}
	}

	private void writeToDatabase(List<Competition> competitions,
			Map<String, List<Lap>> competitionNumber_lap,
			Map<Lap, List<LapEntry>> lap_lapentries) {

		competitionDao.insertInTx(competitions);
		List<Competition> dbCompetitions = competitionDao.loadAll();
		List<Lap> allLaps = new ArrayList<Lap>();
		List<LapEntry> allEntries = new ArrayList<LapEntry>();
		List<LapEntry> allDbEntries = new ArrayList<LapEntry>();

		for (Competition dbCompetition : dbCompetitions) {
			long id = dbCompetition.getId();
			// Log.d(TAG,
			// "Checking laps for Competition Nr. "+dbCompetition.getCompetitionNumber()+": "+dbCompetition.getName());
			List<Lap> laps = competitionNumber_lap.get(dbCompetition
					.getCompetitionNumber() + "");

			if (laps != null) {
				// Log.d(TAG, "Laps size is "+laps.size());
				for (Lap cLap : laps) {
					// Log.d(TAG,
					// "Adding Lap "+cLap.getLapNumber()+" in Competition "+cLap.getCompetitionNumber());
					List<LapEntry> entriesForLap = lap_lapentries.get(cLap);
					// Log.d(TAG, "Entries for Lap "+entriesForLap.size());
					allEntries.addAll(entriesForLap);
					cLap.setCompetition(dbCompetition);
					cLap.setCompetitionId(id);

					allLaps.add(cLap);
				}
			} else {
				Log.d(TAG, "Laps *IS* Null");
			}
		}
		lapDao.insertInTx(allLaps);

		for (Lap dbLap : lapDao.loadAll()) {
			List<LapEntry> entriesForDbLap = lap_lapentries.get(dbLap);
			// Log.d(TAG, "LapEntries for DB Lap "+entriesForDbLap.size());
			for (LapEntry cEntry : entriesForDbLap) {
				cEntry.setLapId(dbLap.getId());
				allDbEntries.add(cEntry);
			}
		}

		lapEntryDao.insertInTx(allDbEntries);

	}

	private void debugData(List<Competition> competitionList) {
		for (Competition cComp : competitionList) {
			cComp.resetLapList();
			Log.d(TAG, "Competition: " + cComp.getCompetitionNumber() + " "
					+ cComp.getName());
			writeLaps(cComp.getLapList());
		}
	}

	private void writeLaps(List<Lap> laps) {
		for (Lap cLap : laps) {
			cLap.resetLapEntryList();
			Log.d(TAG, "Lap " + cLap.getLapNumber());
			for (LapEntry cEntry : cLap.getLapEntryList()) {
				Log.d(TAG,
						" " + cEntry.getFirstname() + " "
								+ cEntry.getLastname() + " " + cEntry.getYear()
								+ " " + cEntry.getClub() + " "
								+ cEntry.getTime());
			}
		}
	}

}
