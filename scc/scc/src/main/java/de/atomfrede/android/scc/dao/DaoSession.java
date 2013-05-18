package de.atomfrede.android.scc.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import de.atomfrede.android.scc.dao.Competition;
import de.atomfrede.android.scc.dao.Lap;
import de.atomfrede.android.scc.dao.LapEntry;

import de.atomfrede.android.scc.dao.CompetitionDao;
import de.atomfrede.android.scc.dao.LapDao;
import de.atomfrede.android.scc.dao.LapEntryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig competitionDaoConfig;
    private final DaoConfig lapDaoConfig;
    private final DaoConfig lapEntryDaoConfig;

    private final CompetitionDao competitionDao;
    private final LapDao lapDao;
    private final LapEntryDao lapEntryDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        competitionDaoConfig = daoConfigMap.get(CompetitionDao.class).clone();
        competitionDaoConfig.initIdentityScope(type);

        lapDaoConfig = daoConfigMap.get(LapDao.class).clone();
        lapDaoConfig.initIdentityScope(type);

        lapEntryDaoConfig = daoConfigMap.get(LapEntryDao.class).clone();
        lapEntryDaoConfig.initIdentityScope(type);

        competitionDao = new CompetitionDao(competitionDaoConfig, this);
        lapDao = new LapDao(lapDaoConfig, this);
        lapEntryDao = new LapEntryDao(lapEntryDaoConfig, this);

        registerDao(Competition.class, competitionDao);
        registerDao(Lap.class, lapDao);
        registerDao(LapEntry.class, lapEntryDao);
    }
    
    public void clear() {
        competitionDaoConfig.getIdentityScope().clear();
        lapDaoConfig.getIdentityScope().clear();
        lapEntryDaoConfig.getIdentityScope().clear();
    }

    public CompetitionDao getCompetitionDao() {
        return competitionDao;
    }

    public LapDao getLapDao() {
        return lapDao;
    }

    public LapEntryDao getLapEntryDao() {
        return lapEntryDao;
    }

}
