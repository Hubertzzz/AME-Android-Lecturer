package edu.np.ece.ame_android_lecturer.OrmLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MIYA on 19/09/17.
 */


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "MonitorDB.db";

    // any time you make changes to your database objects, you may have to increase the database versdion
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table

    //private Dao<Monitor,Integer> monitorsDao;
    private static DatabaseHelper instance;

    private Map<String,Dao> maps=new HashMap<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //完成对数据库的创建以及表的建立
        try {

            TableUtils.createTable(connectionSource,Monitor.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource,Monitor.class,true);
           // onCreate(db,connectionSource);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


    }



    public static synchronized DatabaseHelper getInstance(Context context){

        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class cls) throws SQLException, java.sql.SQLException {
        Dao dao=null;
        String className=cls.getSimpleName();

        if(maps.containsKey(className)){
            dao=maps.get(className);
        }else {
            dao=super.getDao(cls);
            maps.put(className,dao);
        }
        return dao;

    }

/*   public Dao<Monitor,Integer> getMonitorsDao() throws SQLException{
       try {
           if(monitorsDao==null){
               monitorsDao=getDao(Monitor.class);
           }
       } catch (java.sql.SQLException e) {
           e.printStackTrace();
       }
       return monitorsDao;
   }*/

    @Override
    public void close() {
        super.close();
        for (String key:maps.keySet()){
            Dao dao=maps.get(key);
            dao=null;
        }
    }

}
