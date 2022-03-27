package app.pdg.imagemachine.ui.image;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import app.pdg.imagemachine.base.BaseApp;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.repo.DataRepo;
import app.pdg.imagemachine.ui.detail.DetailViewModel;

public class ImageViewModel extends AndroidViewModel {

    private final LiveData<Image> imageLiveData;
    private final DataRepo repo;

    public ImageViewModel(@NonNull Application application, DataRepo dataRepo, UUID uuid) {
        super(application);

        imageLiveData = dataRepo.getImageById(uuid);
        repo = dataRepo;

    }

    public LiveData<Image> getImageLiveData() {
        return imageLiveData;
    }

    public void deleteImage(Image image){
        repo.deleteImage(image);
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
            return (T) new ImageViewModel(mApplication,mRepository, mMachineId);
        }
    }
}
