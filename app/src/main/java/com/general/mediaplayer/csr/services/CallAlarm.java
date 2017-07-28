package com.general.mediaplayer.csr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.general.mediaplayer.csr.R;

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
      //if (Math.abs(diff) >= 86400000L){
      if (Math.abs(diff) >= 3600000L){

         Log.v("===", "==CallAlarm==onReceive=action=" + intent.getAction());

         String strMediaplayerSettingSp = context.getResources().getString(R.string.mediaplayer_setting_sp);
         String strOpenAlarmKey = context.getResources().getString(R.string.open_alarm_sp_key);
         SharedPreferences mSharedPreferences = context.getApplicationContext().getSharedPreferences(strMediaplayerSettingSp, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = mSharedPreferences.edit();
         editor.putBoolean(strOpenAlarmKey ,false);
         editor.apply();

         this.startReboot(context);
      }

   }
}
