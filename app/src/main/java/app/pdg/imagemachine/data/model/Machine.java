package app.pdg.imagemachine.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "machine")
public class Machine implements Parcelable {

    @PrimaryKey
    @NonNull
    private UUID id;

    private String name;

    @ColumnInfo(name= "type")
    private String machineType;

    @ColumnInfo(name = "qr_number")
    private int qrNumber;

    @ColumnInfo(name = "last_maintenance_date")
    private Date lastMaintenanceDate;

    @ColumnInfo(name = "created_at")
    private Date createdAt;


    public Machine(@NonNull UUID id, String name, String machineType, int qrNumber, Date lastMaintenanceDate, Date createdAt) {
        this.id = id;
        this.name = name;
        this.machineType = machineType;
        this.qrNumber = qrNumber;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.createdAt = createdAt;
    }

    protected Machine(Parcel in) {
        name = in.readString();
        machineType = in.readString();
        qrNumber = in.readInt();
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };

    @NonNull
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMachineType() {
        return machineType;
    }

    public int getQrNumber() {
        return qrNumber;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public void setQrNumber(int qrNumber) {
        this.qrNumber = qrNumber;
    }

    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(machineType);
        parcel.writeInt(qrNumber);
    }
}
