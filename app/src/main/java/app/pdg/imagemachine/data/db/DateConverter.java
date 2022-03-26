package app.pdg.imagemachine.data.db;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Long timestampFromDate(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date dateFromTimestamp(Long timestamp) {
        return new Date(timestamp);
    }

}
