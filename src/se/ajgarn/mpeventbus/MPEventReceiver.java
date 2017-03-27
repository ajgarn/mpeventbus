package se.ajgarn.mpeventbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Receives EventBus events sent via {@link MPEventBus}. This needs to be registered
 * to each process.
 * <p>
 * The registration is performed automatically when you run {@link MPEventBus#init(Context)},
 * which means that running processes always listens to events from other processes. You can
 * also register this class as a {@link BroadcastReceiver} in your AndroidManifest.xml to
 * start processes when events are to be received.
 */
public class MPEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Serializable serializedType = intent.getSerializableExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA_TYPE);
            if (serializedType != null) {
                MPEventBus.EventType type = (MPEventBus.EventType) serializedType;
                Object event = getEvent(intent, type);
                Log.d(MPEventBus.TAG, "Received event " + event);
                EventBus.getDefault().post(event);
            } else {
                Log.d(MPEventBus.TAG, "Intent has no type specified.");
            }
        } catch (Exception e) {
            Log.w(MPEventBus.TAG, "Could not handle event.", e);
        }
    }

    static void register(Context context) {
        if (!isRegisteredInManifest(context)) {
            IntentFilter filter = new IntentFilter(MPEventBus.MULTI_PROCESS_INTENT_ACTION);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            context.registerReceiver(new MPEventReceiver(), filter);
        }
    }

    private static Object getEvent(Intent intent, MPEventBus.EventType type) {
        switch (type) {
            case BUNDLE: return intent.getBundleExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA);
            case CHAR_SEQUENCE: return intent.getCharSequenceExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA);
            case PARCELABLE: return intent.getParcelableExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA);
            case SERIALIZABLE: return intent.getSerializableExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA);
            case STRING: return intent.getStringExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA);
        }
        return null;
    }

    private static boolean isRegisteredInManifest(Context context) {
        Intent intent = MPEventBus.getBaseIntent();
        String process = ProcessHelper.getCurrentProcess(context);
        return ProcessHelper.hasBroadcastReceiver(process, intent, context);
    }
}
