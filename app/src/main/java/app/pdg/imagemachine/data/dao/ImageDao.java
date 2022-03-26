package app.pdg.imagemachine.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

import app.pdg.imagemachine.data.model.Image;

@Dao
public interface ImageDao {

    @Query("Select * From image Where machine_id = :id")
    LiveData<List<Image>> getImageListByMachineId(UUID id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(Image image);

    @Delete
    void deleteImage(Image image);
}
