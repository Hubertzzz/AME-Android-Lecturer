package edu.np.ece.ame_android_lecturer.OrmLite;

import android.content.Context;
import android.database.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.List;

/**
 * Created by MIYA on 19/09/17.
 */

public class DatabaseManager {
    private Context context;
    private Dao<Monitor,Integer> monitorDao;
    private DatabaseHelper helper;

    static private DatabaseManager instance;

    public DatabaseManager(Context context){
        this.context=context;
        helper=DatabaseHelper.getInstance(context);

        try {
            monitorDao=helper.getDao(Monitor.class);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }


    public void addMonitor(Monitor monitor){

        try {
            monitorDao.create(monitor);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateMonitor(Monitor monitor){
        try {
            monitorDao.update(monitor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMonitor(){
        try {
            /*for(int i=0;i<monitors.size();i++){
                monitorDao.deleteById(i);
            }*/
            monitorDao.deleteById(0);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteMultiMonitor(List<Monitor> monitors){
        try {
            monitorDao.delete(monitors);
            monitorDao.clearObjectCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Monitor> getMonitor(){
        List<Monitor> monitors=null;
        try {
           monitors= monitorDao.queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return monitors;
    }

}
