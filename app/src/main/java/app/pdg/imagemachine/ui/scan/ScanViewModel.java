package app.pdg.imagemachine.ui.scan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import app.pdg.imagemachine.base.BaseApp;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.data.repo.DataRepo;

public class ScanViewModel extends AndroidViewModel {
    private DataRepo dataRepo;
    private LiveData<Machine> machineByQr;

    public ScanViewModel(@NonNull Application application) {
        super(application);

        dataRepo = ((BaseApp)application).getRepo();
    }

    public void loadMachineData(int qrCode){
        machineByQr = dataRepo.getMachineByQrCode(qrCode);
    }

    public LiveData<Machine> getMachineByQr() {
        return machineByQr;
    }
}
