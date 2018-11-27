package com.npktech.ravi.newsapp;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.List;

public class NewsStoriesActivity extends AppCompatActivity {

    NewsAdapter adapter;
    NewsItemViewModel newsStoriesViewModel;

    int periodicTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_stories);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new NewsAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewsStoriesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        newsStoriesViewModel = ViewModelProviders.of(this).get(NewsItemViewModel.class);

        newsStoriesViewModel.refreshData();

        loadData();
        scheduleFireBaseJobService();

    }


    /**
     * Warning Documentation link : https://developer.android.com/about/versions/oreo/background#services
     *
     *  Warning : Find a way to duplicate the service's functionality with a scheduled job.
     *            If the service is not doing something immediately
     *            noticeable to the user, you should generally be able to use a scheduled job instead.
     */
    private void scheduleFireBaseJobService() {
        FirebaseJobDispatcher firebaseJobDispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Job myJob = firebaseJobDispatcher.newJobBuilder()
                .setService(FireBaseJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag("MyService1")
                .setTrigger(Trigger.executionWindow(periodicTime, periodicTime + 10))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        firebaseJobDispatcher.mustSchedule(myJob);
    }

    private void loadData() {

        final ProgressDialog progressDialog = new ProgressDialog(NewsStoriesActivity.this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Fetching news stories...");
        progressDialog.show();

         newsStoriesViewModel.refreshData();

        newsStoriesViewModel.getAlldata().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable List<NewsItem> newsItems) {
                adapter.setNewsItems(newsItems);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (isOnline(NewsStoriesActivity.this)) {
                loadData();
            } else {
                Toast.makeText(this, "There is no internet connection, Please try again...", Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) return false;
        return cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
