/*
 *  Copyright 2012 Frederik Hahne
 *  
 *  This file is part of Scc - Swimming Cup Companion App.
 *
 *  Scc is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Scc is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Scc.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.atomfrede.scc.generator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class SccDaoGenerator {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "de.atomfrede.android.scc.dao");
		schema.enableKeepSectionsByDefault();
		
		createCompetition(schema);

		new DaoGenerator().generateAll(schema, "../scc/scc/src/main/java");
	}

	private static void createCompetition(Schema schema) {
		Entity competition = schema.addEntity("Competition");
		competition.addIdProperty();
		competition.addStringProperty("name");
		competition.addIntProperty("competitionNumber");
		

		Entity lap = schema.addEntity("Lap");
		lap.implementsInterface("Comparable<Lap>");
		Property lapNumberProp = lap.addIntProperty("lapNumber").getProperty();
		lap.addIdProperty();
		lap.addBooleanProperty("isDone");
		Property competitionNumber = lap.addIntProperty("competitionNumber").getProperty();
		Property competitionId = lap.addLongProperty("competitionId").notNull()
				.getProperty();
		lap.addToOne(competition, competitionId);
		ToMany competitionToLaps = competition.addToMany(lap, competitionId);
		competitionToLaps.orderAsc(lapNumberProp);
		
		Entity lapEntry = schema.addEntity("LapEntry");
		lapEntry.implementsInterface("Comparable<LapEntry>");
		Property lapId = lapEntry.addLongProperty("lapId").notNull()
				.getProperty();
		lapEntry.addToOne(lap, lapId);
		ToMany lapToLapEntry = lap.addToMany(lapEntry, lapId);
		
		lapEntry.addBooleanProperty("isDone");
		lapEntry.addStringProperty("firstname");
		lapEntry.addStringProperty("lastname");
		lapEntry.addStringProperty("year");
		lapEntry.addStringProperty("club");
		lapEntry.addStringProperty("time");
		lapEntry.addIntProperty("competitionNumber");
		lapEntry.addIntProperty("lapNumber");
		Property lane = lapEntry.addIntProperty("lane").getProperty();
		
		lapToLapEntry.orderAsc(lane);
	}

}
