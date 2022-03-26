package app.pdg.imagemachine.base;

import android.app.Application;

import app.pdg.imagemachine.data.SessionHandler;
import app.pdg.imagemachine.data.db.MyDatabase;
import app.pdg.imagemachine.data.repo.DataRepo;

public class BaseApp extends Application {

    SessionHandler session;

    @Override
    public void onCreate() {
        super.onCreate();

        session = new SessionHandler(getApplicationContext());
    }

    public MyDatabase getDatabase() {
        return MyDatabase.getInstance(this);
    }

    public DataRepo getRepo() {
        return DataRepo.getInstance(getDatabase(), getSession());
    }

    public SessionHandler getSession() {
        return session;
    }
}
