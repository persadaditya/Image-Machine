package app.pdg.imagemachine.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;


public class SessionHandler {
    private static final String PREF_NAME = "AppSession";
    private static final String KEY_SORT = "sortKey";
    private Context mContext;
    private final SharedPreferences.Editor mEditor;
    private final SharedPreferences mPreferences;

    public SessionHandler(@NonNull Context context) {
        this.mContext = context;
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }


    /**
     * setting sort for machine
     *
     * @param isName pass true for sorting to name
     *               pass false for sorting to type
     * */
    public void setSortMode(Boolean isName){
        mEditor.putBoolean(KEY_SORT, isName);
        mEditor.commit();
    }


    /**
     * get sorting for machine
     *
     * return true if sorting by name
     * return false if sorting by type
     * */
    public boolean isSortByNameMode(){
        return mPreferences.getBoolean(KEY_SORT, true);
    }


}
