package app.pdg.imagemachine.data.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import app.pdg.imagemachine.data.dao.ImageDao;
import app.pdg.imagemachine.data.dao.MachineDao;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;

@Database(entities = {Machine.class, Image.class}, version = 2)
@TypeConverters(value = {DateConverter.class, UUIDConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    private static final String LOG_TAG = MyDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "MyDatabase";
    private static MyDatabase sInstance;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static MyDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MyDatabase.class, MyDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigrationFrom(1)
                        .build();

                MyDatabase database = MyDatabase.getInstance(context);
                database.setDatabaseCreated();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }


    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    public abstract MachineDao machineDao();
    public abstract ImageDao imageDao();
}
