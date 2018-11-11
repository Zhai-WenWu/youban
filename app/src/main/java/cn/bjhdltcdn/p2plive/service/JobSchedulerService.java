package cn.bjhdltcdn.p2plive.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.orhanobut.logger.Logger;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//API需要在21及以上
public class JobSchedulerService extends JobService {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            Logger.d("handleMessage()...................");

            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, false);

            return true;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
//        Logger.d("onCreate()...................");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Logger.d("onStartCommand()...................flags ===== " + flags + "  startId ===  " +  startId);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
//        Logger.d("onStartJob()...................obParameters ===  " +  jobParameters);

        if (jobParameters != null) {
            int jobId = jobParameters.getJobId();
//            Logger.d("onStartJob()...................jobId ===  " +  jobId);

            if (handler != null) {
                handler.sendMessage( Message.obtain( handler, 1, jobParameters ) );
            }


        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

//        Logger.d("onStopJob().................jobParameters === " + jobParameters);

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Logger.d("onDestroy()..................");
    }

}
