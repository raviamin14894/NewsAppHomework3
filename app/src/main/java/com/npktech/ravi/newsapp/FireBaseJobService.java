package com.npktech.ravi.newsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;



public class FireBaseJobService extends JobService {

    NotificationUtils notificationUtils;
    NewsItemRepository newsItemRepository;


    BackgroundTask backgroundTask;
    @Override
    public boolean onStartJob(final JobParameters job) {

        Log.d("FirebaseJobService", "onStartJob: "+"job started");

        backgroundTask = new BackgroundTask(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                notificationUtils = new NotificationUtils(FireBaseJobService.this);
                notificationUtils.showSmallNotification("News stories", "Stories Updating...",
                new Intent(getApplicationContext(), CancelService.class));
                newsItemRepository = new NewsItemRepository(getApplicationContext());
                newsItemRepository.addDataFromServerToLocal();
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(), "News Updating.."+s, Toast.LENGTH_SHORT).show();
                jobFinished(job,false);
            }
        };

        backgroundTask.execute();


//        newsItemRepository = new NewsItemRepository(getApplicationContext());
//        newsItemRepository.addDataFromServerToLocal(this,job);
//
//        jobFinished(job,false);
//        Log.d("data", "onStartJob: ");
//
//        notificationUtils = new NotificationUtils(FireBaseJobService.this);
//        notificationUtils.showSmallNotification("News stories", "Stories Updating...",
//                new Intent(getApplicationContext(), CancelService.class));

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public static class BackgroundTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            return "News Updating...";
        }
    }
}
