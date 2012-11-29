package de.atomfrede.android.scc.application;

import com.googlecode.androidannotations.annotations.EApplication;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import de.atomfrede.android.scc.dao.CompetitionDao;
import de.atomfrede.android.scc.dao.DaoMaster;
import de.atomfrede.android.scc.dao.DaoMaster.OpenHelper;
import de.atomfrede.android.scc.dao.DaoSession;
import de.atomfrede.android.scc.dao.LapDao;
import de.atomfrede.android.scc.dao.LapEntryDao;

@EApplication
public class SccApplication extends Application {

	
	boolean devMode = true;
	
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
    private DaoSession daoSession;
    public CompetitionDao competitonDao;
    public LapDao lapDao;
    public LapEntryDao lapEntryDao;
    
    @Override
    public void onCreate(){
    	super.onCreate();
    	
    	OpenHelper helper;
    	if(devMode){
    		helper = new DaoMaster.DevOpenHelper(this, "scc-db", null);
    	}else{
    		helper = new DaoMaster.OpenHelper(this, "scc-db", null) {
				
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
					// TODO Auto-generated method stub
					
				}
			};
    	}
		
    	db = helper.getWritableDatabase();
    	daoMaster = new DaoMaster(db);
    	daoSession = daoMaster.newSession();
    	competitonDao = daoSession.getCompetitionDao();
    	lapDao = daoSession.getLapDao();
    	lapEntryDao = daoSession.getLapEntryDao();
    }
}
