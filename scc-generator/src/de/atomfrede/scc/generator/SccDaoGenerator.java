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

	public static void main(String[] args) throws Exception{
		Schema schema = new Schema(1, "de.atomfrede.android.scc.dao");
		
		createCompetition(schema);
		
		new DaoGenerator().generateAll(schema, "../scc/scc/src/main/java/de/atomfrede/android/scc/dao");
	}
	
	private static void createCompetition(Schema schema){
		Entity competition = schema.addEntity("Competition");
		competition.addIdProperty();
		competition.addStringProperty("Nnme");
		
		
		Entity lap = schema.addEntity("Lap");
		lap.addIdProperty();
		Property competitionId = lap.addLongProperty("competitionId").notNull().getProperty();
		lap.addToOne(competition, competitionId);
		ToMany competitionToLaps = competition.addToMany(lap, competitionId);
	
		
		Entity lapEntry = schema.addEntity("LapEntry");
		Property lapId = lapEntry.addLongProperty("lapId").notNull().getProperty();
	}
	
	private static void addLap(Schema schema){
		
	}
	/*
	 * private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
	 */
}
