package app.pdg.imagemachine.ui.addedit;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.UUID;

import app.pdg.imagemachine.base.BaseApp;
import app.pdg.imagemachine.data.db.AppExecutors;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.data.repo.DataRepo;
import app.pdg.imagemachine.ui.detail.DetailViewModel;

public class AddEditViewModel extends AndroidViewModel {

    private final DataRepo dataRepo;
    private final UUID machineId;
    private LiveData<List<Image>> imageLiveData;
    private final LiveData<Machine> latestMachine;

    private final MutableLiveData<Boolean> isAddData = new MutableLiveData<>(null);
    private final MutableLiveData<List<Uri>> pathImage = new MutableLiveData<>();

    public AddEditViewModel(@NonNull Application application, UUID machineId) {
        super(application);

        dataRepo = ((BaseApp)application).getRepo();
        this.machineId = machineId;
        if(machineId!=null){
            imageLiveData = dataRepo.getImageByMachineId(machineId);
        }

        latestMachine = dataRepo.getLatestMachine();
    }

//    public LiveData<Boolean> getIsAddData() { return isAddData; }
//
//    public void setIsAddData(Boolean value){ isAddData.postValue(value); }


    public LiveData<List<Uri>> getPathImage() { return pathImage; }
    public void setImagesPath(List<Uri>imagesPath){pathImage.postValue(imagesPath);}

    public LiveData<List<Image>> getImageLiveData() {
        return imageLiveData;
    }

    public LiveData<Machine> getLatestMachine() {
        return latestMachine;
    }

    public void insertUpdate(Machine machine){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (machineId==null){
                    dataRepo.insertMachine(machine);
                } else {
                    dataRepo.updateMachine(machine);
                }
            }
        });
    }

    public void insertImage(Image image){
        Log.d("aap", "insert Image: "+ image.getPath());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                dataRepo.insertImage(image);
            }
        });
    }

    public void deleteImage(Image image){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                dataRepo.deleteImage(image);
            }
        });
    }



    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final UUID mMachineId;


        public Factory(@NonNull Application application, UUID machineId) {
            mApplication = application;
            mMachineId = machineId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AddEditViewModel(mApplication, mMachineId);
        }
    }
}
