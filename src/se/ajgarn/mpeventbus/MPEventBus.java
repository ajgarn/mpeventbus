package se.ajgarn.mpeventbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

/**
 * Event bus that sends events within a single process or over multiple processes.
 * Use {@link #post(Object)} to send an event to all subscribers within the current process.
 * Use {@link #postToAll(Parcelable)} to send an event to all subscribers on all processes
 * within the application. Since the event object must be serialized to be sent between processes,
 * there are more restrictions on its class type. Using {@link Parcelable} for complex objects is
 * recommended since those types are the fastest to transform, but all implemented
 * {@link #postToAll(Parcelable)} methods work fine.
 * <p>
 * This is a wrapper of {@link org.greenrobot.eventbus.EventBus} with extended functionality.
 */
public class MPEventBus {

    public static final String MULTI_PROCESS_INTENT_ACTION = "multi_process_event";
    public static final String MULTI_PROCESS_INTENT_EXTRA = "event";

    protected static final String TAG = MPEventBus.class.getSimpleName();

    private static MPEventBus instance;

    private final Context context;

    private MPEventBus(Context context) {
        this.context = context;
        MPEventReceiver.register(context);
    }

    /**
     * Initializes the event bus and sets up listeners for events sent by other processes.
     * This method should be called in onCreate() from your Application class.
     * @param context The application context.
     */
    public static synchronized void init(Context context) {
        if (instance == null)
            instance = new MPEventBus(context);
    }

    public static MPEventBus getDefault() {
        if (instance == null)
            throw new IllegalStateException(MPEventBus.class.getSimpleName() + " must be initialized via init().");
        return instance;
    }

    /**
     * Posts an event to the event bus on the current process. Other processes will not get
     * this event.
     * <p>
     * This is the same as calling {@link EventBus#post(Object)}
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void post(Object event) {
        postInternal(event);
    }

    /**
     * Posts an event to the event bus and also send an intent to add this event on other
     * processes' EventBuses in the application.
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void postToAll(CharSequence event) {
        postInternal(event);
        sendIntent(IntentMessageFactory.get(event));
    }

    /**
     * Posts an event to the event bus and also send an intent to add this event on other
     * processes' EventBuses in the application.
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void postToAll(Bundle event) {
        postInternal(event);
        sendIntent(IntentMessageFactory.get(event));
    }

    /**
     * Posts an event to the event bus and also send an intent to add this event on other
     * processes' EventBuses in the application.
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void postToAll(Parcelable event) {
        postInternal(event);
        sendIntent(IntentMessageFactory.get(event));
    }

    /**
     * Posts an event to the event bus and also send an intent to add this event on other
     * processes' EventBuses in the application.
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void postToAll(Serializable event) {
        postInternal(event);
        sendIntent(IntentMessageFactory.get(event));
    }

    /**
     * Posts an event to the event bus and also send an intent to add this event on other
     * processes' EventBuses in the application.
     * @param event The event to post.
     * @see EventBus#post(Object)
     */
    public void postToAll(String event) {
        postInternal(event);
        sendIntent(IntentMessageFactory.get(event));
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call
     * {@link #unregister(Object)} once they are no longer interested in receiving events.
     * <p/>
     * Subscribers have event handling methods that must be annotated by {@link Subscribe}.
     * The Subscribe annotation also allows configuration like ThreadMode and priority.
     * @see EventBus#register(Object)
     */
    public void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * Unregisters the given subscriber from all event classes.
     * @see EventBus#unregister(Object)
     */
    public void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * Returns the intent to be sent to other processes without any extra data.
     * Add the event to send with putExtra(...).
     */
    protected static Intent getBaseIntent() {
        Intent intent = new Intent();
        intent.setAction(MULTI_PROCESS_INTENT_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    private void postInternal(Object event) {
        Log.d(TAG, "Sending event " + event);
        EventBus.getDefault().post(event);
    }

    private void sendIntent(IntentMessageProducer eventContainer) {
        Intent broadcastIntent = getBaseIntent();
        eventContainer.putExtra(MULTI_PROCESS_INTENT_EXTRA, broadcastIntent);
        context.sendBroadcast(broadcastIntent);
    }
}
