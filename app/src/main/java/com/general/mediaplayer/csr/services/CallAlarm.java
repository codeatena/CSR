package com.general.mediaplayer.csr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.general.mediaplayer.csr.R;

import java.util.Calendar;

public class CallAlarm extends BroadcastReceiver {

   private static final long MAX_ALLOW_ALARM_TIME_MILLIS = 600000L;
   private static final String RESET_24HOUR_ACTION = "com.general.mediaplayer.24HourReset";
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
         Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public void onReceive(Context context, Intent intent) {

      float diff = Math.abs(this.get24HourResetInMillis(context) - this.getCurrentTimeInMillis());
      Log.v("Diff Time" , String.valueOf(diff / 1000)) ;
      if (Math.abs(diff) >= 86400000L){

         Log.v("===", "==CallAlarm==onReceive=action=" + intent.getAction());

         String strMediaplayerSettingSp = context.getResources().getString(R.string.mediaplayer_setting_sp);
         String strOpenAlarmKey = context.getResources().getString(R.string.open_alarm_sp_key);
         SharedPreferences mSharedPreferences = context.getApplicationContext().getSharedPreferences(strMediaplayerSettingSp, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = mSharedPreferences.edit();
         editor.putBoolean(strOpenAlarmKey ,false);
         editor.apply();

         this.startReboot();
      }

   }
}
