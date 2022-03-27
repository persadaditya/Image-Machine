package app.pdg.imagemachine.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "image",
        foreignKeys = @ForeignKey(entity = Machine.class,
                parentColumns = "id", childColumns = "machine_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ))
public class Image implements Parcelable {
    @PrimaryKey
    @NonNull
    private UUID id;

    private String path;

    @ColumnInfo(name = "machine_id")
    private UUID machineId;

    public Image(@NonNull UUID id, String path, UUID machineId) {
        this.id = id;
        this.path = path;
        this.machineId = machineId;
    }


    protected Image(Parcel in) {
        path = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @NonNull
    public UUID getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public UUID getMachineId() {
        return machineId;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMachineId(UUID machineId) {
        this.machineId = machineId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
    }
}
