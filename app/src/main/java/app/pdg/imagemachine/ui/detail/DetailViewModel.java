package app.pdg.imagemachine.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.UUID;

import app.pdg.imagemachine.base.BaseApp;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.data.repo.DataRepo;

public class DetailViewModel extends AndroidViewModel {

    private final LiveData<List<Image>> imageList;
    private final LiveData<Machine> machineLiveData;

    public DetailViewModel(@NonNull Application application, DataRepo dataRepo, UUID id) {
        super(application);

        imageList = dataRepo.getImageByMachineId(id);
        machineLiveData = dataRepo.getMachineById(id);
    }

    public LiveData<List<Image>> getImageList() {
        return imageList;
    }

    public LiveData<Machine> getMachineLiveData() {
        return machineLiveData;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final UUID mMachineId;

        private final DataRepo mRepository;

        public Factory(@NonNull Application application, UUID machineId) {
            mApplication = application;
            mMachineId = machineId;
            mRepository = ((BaseApp) application).getRepo();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DetailViewModel(mApplication,mRepository, mMachineId);
        }
    }
}



