package app.pdg.imagemachine.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;

import app.pdg.imagemachine.data.model.Machine;

@Dao
public interface MachineDao {
    @Query("Select * from machine Order By name ASC")
    LiveData<List<Machine>> getMachineListByName();

    @Query("Select * from machine Order By type ASC")
    LiveData<List<Machine>> getMachineListByType();

    @Query("Select * From machine Where id = :id")
    LiveData<Machine> getMachineById(UUID id);

    @Query("Select * from machine Where qr_number = :qrCode limit 1")
    LiveData<Machine> getMachineByQrCode(int qrCode);

    @Query("Select * from machine Order By created_at DESC limit 1")
    LiveData<Machine> getLatestMachine();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMachine(Machine machine);

    @Update
    void updateMachine(Machine machine);

    @Delete
    void deleteMachine(Machine machine);
}
