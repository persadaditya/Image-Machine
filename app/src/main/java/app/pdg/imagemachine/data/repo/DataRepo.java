package app.pdg.imagemachine.data.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;
import java.util.UUID;

import app.pdg.imagemachine.data.SessionHandler;
import app.pdg.imagemachine.data.db.AppExecutors;
import app.pdg.imagemachine.data.db.MyDatabase;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;

public class DataRepo {

    private static DataRepo sInstance;
    private final MyDatabase myDatabase;
    private MediatorLiveData<List<Machine>> mObservableMachine;

    public DataRepo(final MyDatabase database, SessionHandler session){
        myDatabase = database;
        mObservableMachine = new MediatorLiveData<>();

        if(session.isSortByNameMode()){
            loadMachineListByName();
        } else {
            loadMachineListByType();
        }

    }

    public static DataRepo getInstance(final MyDatabase database, SessionHandler session){
        if (sInstance == null) {
            synchronized (DataRepo.class) {
                if (sInstance == null) {
                    sInstance = new DataRepo(database, session);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Machine>> getMachineList(){
        return mObservableMachine;
    }

    public void loadMachineListByName(){
        mObservableMachine.addSource(myDatabase.machineDao().getMachineListByName(), data-> {
            if(myDatabase.getDatabaseCreated().getValue()!=null){
                mObservableMachine.postValue(data);
            }
        });
    }

    public void loadMachineListByType(){
        mObservableMachine.addSource(myDatabase.machineDao().getMachineListByType(), data-> {
            if(myDatabase.getDatabaseCreated().getValue()!=null){
                mObservableMachine.postValue(data);
            }
        });
    }

    public LiveData<List<Machine>> getMachineListByName(){
        return myDatabase.machineDao().getMachineListByName();
    }

    public LiveData<List<Machine>> getMachineListByType(){
        return  myDatabase.machineDao().getMachineListByType();
    }

    public LiveData<Machine> getMachineById(UUID uuid){
        return myDatabase.machineDao().getMachineById(uuid);
    }

    public LiveData<Machine> getMachineByQrCode(int number){
        return myDatabase.machineDao().getMachineByQrCode(number);
    }

    public LiveData<List<Image>> getImageByMachineId(UUID uuid){
        return myDatabase.imageDao().getImageListByMachineId(uuid);
    }

    public LiveData<Machine> getLatestMachine(){
        return myDatabase.machineDao().getLatestMachine();
    }

    public void insertMachine(Machine machine){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.machineDao().insertMachine(machine);
            }
        });
    }


    public void updateMachine(Machine machine){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.machineDao().updateMachine(machine);
            }
        });
    }

    public void deleteMachine(Machine machine){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.machineDao().deleteMachine(machine);
            }
        });
    }

    public void insertImage(Image image){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.imageDao().insertImage(image);
            }
        });
    }

    public LiveData<Image> getImageById(UUID uuid){
        return myDatabase.imageDao().getImageById(uuid);
    }

    public void deleteImage(Image image){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.imageDao().deleteImage(image);
            }
        });
    }


}
