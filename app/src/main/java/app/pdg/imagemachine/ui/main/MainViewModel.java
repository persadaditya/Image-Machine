package app.pdg.imagemachine.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.pdg.imagemachine.base.BaseApp;
import app.pdg.imagemachine.data.SessionHandler;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.data.repo.DataRepo;

public class MainViewModel extends AndroidViewModel {
    private final DataRepo dataRepo;
    private SessionHandler sessionHandler;

    private LiveData<List<Machine>> machineList;


    public MainViewModel(@NonNull Application application) {
        super(application);

        sessionHandler = ((BaseApp)application).getSession();
        dataRepo = ((BaseApp)application).getRepo();

        machineList = dataRepo.getMachineList();

    }

    public LiveData<List<Machine>> getMachineList() {
        return machineList;
    }

    public void loadMachineByName(){
        dataRepo.loadMachineListByName();
    }

    public void loadMachineByType(){
        dataRepo.loadMachineListByType();
    }

}
