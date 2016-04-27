package se.ajgarn.mpeventbus;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

class ProcessHelper {
    /**
     * Returns the name of the current process.
     * @param context The application context.
     * @return The process name of null if the operation fails.
     */
    static String getCurrentProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses())
        {
            if (process.pid == pid)
                return process.processName;
        }
        return null;
    }

    /**
     * Determines whether a broadcast receiver for the specified intent is registered on a process.
     * @param process The process where to look for broadcast receivers.
     * @param intent The intent that the broadcast receiver should catch.
     * @param context The application context.
     * @return True if a matching broadcast receiver is registered on the process.
     */
    static boolean hasBroadcastReceiver(String process, Intent intent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> listeners = pm.queryBroadcastReceivers(intent, 0);
        for (ResolveInfo info : listeners) {
            if (info.activityInfo.processName.equals(process))
                return true;
        }
        return false;
    }
}
