package com.wolfenterprisesllc.prisongourmet;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class DataHolder extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(getBaseContext())
                .applicationId("pQ3hJlWh9LiIVJ6iETzOS6Tg6ForXuiyATQCJoNZ")
               
                .server("https://parseapi.back4app.com")
                 .enableLocalDataStore()
                .build());
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
