package de.atomfrede.android.scc.competition;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import au.com.bytecode.opencsv.CSVReader;

import com.googlecode.androidannotations.annotations.EFragment;

import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.dao.Competition;

@EFragment
public class CompetitionFragment extends Fragment {

	public void readData() throws IOException{
		InputStream in = getResources().openRawResource(R.raw.me_2012);
		CSVReader reader = new CSVReader(new InputStreamReader(in));
		
		int currentCompetitionNumber = 1;
		int currentLapNumber = 1;
		
		List<Competition> competitions = new ArrayList<Competition>();
		Competition currentCompetition = new Competition();
		currentCompetition.setCompetitionNumber(Integer.valueOf(currentCompetitionNumber));
//		competitions.add(currentCompetition);
		
		while(reader.readNext() != null){
			String[] line = reader.readNext();
			if(line[0] != "WK-Nr"){
				//Not reading the header line
				//First check if we need to create a new competition element
				if(Integer.parseInt(line[0]) != currentCompetitionNumber){
					competitions.add(currentCompetition);
					currentCompetitionNumber = Integer.parseInt(line[0]);
					currentCompetition = new Competition();
					currentCompetition.setCompetitionNumber(currentCompetitionNumber);
				}
			}
		}
	}
	
	public void readCompetition(){
		
	}

}
