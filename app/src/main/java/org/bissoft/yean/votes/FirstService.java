package org.bissoft.yean.votes;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by Natalia on 04.03.2016.
 */
public class FirstService extends Service {

    private static final String TAG = "MyService";
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //      Toast.makeText(this, "onCreate();", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "onDestroy();", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


    /*  @Override
      public void onStart(Intent intent, int startid)
      {
     //     Intent intents = new Intent(getBaseContext(), MainActivityFragment.class);
      //    intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     //     startActivity(intents);
          Toast.makeText(this, "Votes Service", Toast.LENGTH_LONG).show();
      //    Log.d(TAG, "onStart");
      }
  */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Votes Service", Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY;
    }


 /*   public static class StartStopReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive broadcast: " + intent.getAction());

            if (intent.getAction().equals(MainActivityFragment.ACTION_STARTED)) {
                Intent service = new Intent(context, FirstService.class);
                context.startService(service);
            } else if (intent.getAction().equals(MainActivityFragment.ACTION_STOPPED)) {
                Intent service = new Intent(context, FirstService.class);
                context.stopService(service);
            }
        }

    }*/

    static class ToastHandler extends Handler {
        WeakReference<FirstService> wrFirstService;

        public ToastHandler(FirstService service) {
            wrFirstService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FirstService service = wrFirstService.get();
            if (service != null) {
                Bundle b = msg.getData();
                Toast.makeText(service, "Start to do an action", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

