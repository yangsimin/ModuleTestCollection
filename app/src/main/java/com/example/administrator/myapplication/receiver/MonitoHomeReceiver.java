package com.example.administrator.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class MonitoHomeReceiver extends BroadcastReceiver
{
    final String HOME_DIALOG_REASON = "homereason";

    final String HOME_DIALOG_REASON_HOME = "homekey";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        {
            String reason = intent.getStringExtra(HOME_DIALOG_REASON);
            if (reason != null && reason.equals(HOME_DIALOG_REASON_HOME))
            {
                Log.d("homelistener", "home执行了" );
            }
        }
    }
}
