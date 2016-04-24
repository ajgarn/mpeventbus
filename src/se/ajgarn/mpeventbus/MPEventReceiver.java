package se.ajgarn.mpeventbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

/**
 * Receives EventBus events sent via {@link MPEventBus}. This needs to be registered
 * to each process.
 */
public class MPEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object event = intent.getParcelableExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA);
        Log.d(MPEventReceiver.class.getSimpleName(), "Received event " + event);
        EventBus.getDefault().post(event);
    }

    static void register(Context context) {
        IntentFilter filter = new IntentFilter(MPEventBus.MULTI_PROCESS_INTENT_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        context.registerReceiver(new MPEventReceiver(), filter);
    }
}
