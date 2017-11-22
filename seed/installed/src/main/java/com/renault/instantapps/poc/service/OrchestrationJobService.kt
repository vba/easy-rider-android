package com.renault.instantapps.poc.service

import android.app.job.JobParameters
import android.app.job.JobService

class OrchestrationJobService: JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return true;
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        //EventOrchestrationService.startEventOrchestrator(applicationContext);
        return true;
    }
}