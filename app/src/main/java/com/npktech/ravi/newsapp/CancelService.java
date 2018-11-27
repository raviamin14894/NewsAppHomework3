package com.npktech.ravi.newsapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;



public class CancelService extends IntentService {

    public CancelService() {
        super("nothing");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e("CancelIntent", "onHandleIntent: "+"dcjcnjdcn" );

        FirebaseJobDispatcher firebaseJobDispatcher=
                new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        firebaseJobDispatcher.cancel("MyService1");

        new NotificationUtils(CancelService.this).dismissNotification();

    }
}
