package se.ajgarn.mpeventbus;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;

/**
 * Event bus that sends events within a single process or over multiple processes.
 * Use {@link #post(Object)} to send an event to all subscribers within the current process.
 * Use {@link #postToAll(Parcelable)} to send an event to all subscribers on all processes
 * within the application. Since the event object must be serialized to be sent between processes,
 * there are more restrictions on its class type. Primitive types or using {@link Parcelable} is
 * recommended since those types are the fastest to transform, but all implemented
 * {@link #postToAll(Parcelable)} methods work fine.
 * <p>
 * This is a wrapper of {@link org.greenrobot.eventbus.EventBus} with extended functionality.
 */
public class MPEventBus {

    public static final String MULTI_PROCESS_INTENT_ACTION = "multi_process_event";
    public static final String MULTI_PROCESS_INTENT_EXTRA = "event";

    private static final String TAG = MPEventBus.class.getSimpleName();

    private static MPEventBus instance;

    private final Context context;

    private MPEventBus(Context context) {
        this.context = context;
        MPEventReceiver.register(context);
    }

    public static synchronized void init(Context context) {
        instance = new MPEventBus(context);
    }

    public static MPEventBus getDefault() {
        if (instance == null)
            throw new IllegalStateException(MPEventBus.class.getSimpleName() + " must be initialized via init().");
        return instance;
    }

    public void post(Object event) {
        postInternal(event);
    }

    /**
     * Posts and event to the event bus and also send an intent to add this event on other
     * processes' EventBuses in the application.
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void postToAll(Parcelable event) {
        postInternal(event);
        sendIntent(IntentMessage.get(event));
    }

    private void postInternal(Object event) {
        Log.d(TAG, "Sending event " + event);
        EventBus.getDefault().post(event);
    }

    private void sendIntent(IntentMessageProducer eventContainer) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MULTI_PROCESS_INTENT_ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        eventContainer.putExtra(MULTI_PROCESS_INTENT_EXTRA, broadcastIntent);
        context.sendBroadcast(broadcastIntent);
    }
}