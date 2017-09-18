package com.general.mediaplayer.csr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.general.mediaplayer.csr.MessageEvent;
import com.general.mediaplayer.csr.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class CallAlarm extends BroadcastReceiver {

   public static final String TAG = "CallAlarm";

   private long get24HourResetInMillis(Context context) {
      String strSpName = context.getResources().getString(R.string.mediaplayer_setting_sp);
      String strAlarmInterval = context.getResources().getString(R.string.alarm_set_inmillis_sp_key);
      long interval = context.getSharedPreferences(strSpName, 1).getLong(strAlarmInterval, 0L);
      Log.v(TAG, "======get24HourResetInMillis==millis=" + interval);
      return interval;
   }

   private long getCurrentTimeInMillis() {
      long var1 = Calendar.getInstance().getTimeInMillis();
      Log.v(TAG, "======getCurrentTimeInMillis==currentMillis=" + var1);
      return var1;
   }

   private void startReboot() {
      Log.v(TAG, "==CallAlarm==startReboot==");

      try {
         Process proc = Runtime.getRuntime().exec(new String[]{"/system/xbin/su", "-c", "reboot"});
         proc.waitFor();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void startReboot(Context context) {
      Log.v(TAG, "==CallAlarm==startReboot==");

      /*
      try {
         Process proc = Runtime.getRuntime().exec(new String[]{"/system/xbin/su", "-c", "reboot"});
         proc.waitFor();
      } catch (Exception ex) {
         ex.printStackTrace();
      }*/

      PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
      pm.reboot(null);

   }

   public void onReceive(Context context, Intent intent) {

      float diff = Math.abs(this.get24HourResetInMillis(context) - this.getCurrentTimeInMillis());
      Log.v("Diff Time" , String.valueOf(diff / 1000)) ;
      //if (Math.abs(diff) >= 3600000L){ // 1 hours reboot
      //if (Math.abs(diff) >= 21600000L){ // 6 hours reboot
      //if (Math.abs(diff) >= 86400000L){ // 24 hours reboot
      if (Math.abs(diff) >= 600000L){ // 10 mins reboot

         if (isReboot(context))
         {
            Log.v("===", "==CallAlarm==onReceive=action=" + intent.getAction());
            String strMediaplayerSettingSp = context.getResources().getString(R.string.mediaplayer_setting_sp);
            String strOpenAlarmKey = context.getResources().getString(R.string.open_alarm_sp_key);
            SharedPreferences mSharedPreferences = context.getApplicationContext().getSharedPreferences(strMediaplayerSettingSp, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(strOpenAlarmKey ,false);
            editor.apply();

            this.startReboot(context);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
         }
         else
         {
            // 1 min delay for reboot time
            //getCurrentTimeInMillis() + 60 * 1000;
            EventBus.getDefault().post(new MessageEvent());
         }
      }
   }

   public boolean isReboot(Context context)
   {
      String strMediaplayerSettingSp = context.getResources().getString(R.string.mediaplayer_setting_sp);
      SharedPreferences mSharedPreferences = context.getApplicationContext().getSharedPreferences(strMediaplayerSettingSp, Context.MODE_PRIVATE);
      return mSharedPreferences.getBoolean("rebootenable" ,true);
   }
}
