package com.echo.echofarm.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.work.Configuration;

public class PushUpdateService extends JobService {

    private static final String TAG = "PushUpdateService";
    private boolean jobCancelled = false;

    public PushUpdateService() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0, 1000);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob");

        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob");
        jobCancelled = true;
        return true;
    }

    private void doBackgroundWork(final JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "startBackgroudWork");
                jobFinished(params, false);
            }
        }).start();
    }
}
