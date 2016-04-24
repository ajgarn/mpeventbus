MPEventBus - Multi-Process EventBus
===
**Note:** This project is under development and has not been finalized yet.

MPEventBus is an extension to [greenrobot/EventBus](https://github.com/greenrobot/EventBus) that lets you send events between processes in your Android application.

[greenrobot/EventBus](https://github.com/greenrobot/EventBus) lets you send arbitrary events on an event bus and your classes subscribe to those events. MPEventBus (or MultiProcessEventBus) also can send events via Intents and listen to events via BroadcastReceivers, which delegates all received events to the EventBus instance running on the current process.

# Usage
## Initialization
To initialize MPEventBus you must call the following method from your Application class's onCreate().

    MPEventBus.init(getApplicationContext());

If you don't have an Application class yet, create one in your package root.


    public class App extends Application {
        @Override
        public void onCreate() {
            MPEventBus.init(getApplicationContext());
        }
    }

## Get the EventBus
From anywhere in your app you can get the MPEventBus instance by

    MPEventBus mpEventBus = MPEventBus.getDefault();

## Send a message
The process of sending a message is very similar to [greenrobot/EventBus](https://github.com/greenrobot/EventBus#eventbus-in-3-steps).

(1) Create an event class.

    public class CustomEvent {/* Optional body */}

(2) Register objects as listeners.

    mpEventBus.register(this);

And declare the method that should receive the event.

    @Subscribe
    public void onEvent(CustomEvent event) {/* Do something */};

(3) Send a message to all your processes, including the current one:

    mpEventBus.postToAll(new CustomEvent());
    
Or send a message only to the current process (this is the same as using EventBus.post(...)).

    mpEventBus.post(new CustomEvent());

## Wake up process by event
By default, all *running* processes listen to your events. If you want a specific process to wake up when an event is sent you must register a BroadcastReceiver in your AndroidManifest.xml for that process.

Add the following to your AndroidManifest.xml, where you replace ":process-name" with the name of the process you want to be started on between-process events.

    <receiver android:name="se.ajgarn.mpeventbus.MPEventReceiver" android:process=":process-name" android:exported="false">
        <intent-filter>
            <action android:name="multi_process_event" />
            <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>
    </receiver>
    
Note that this broadcast will wake up the process for all events sent with the method `mpEventBus.sendToAll(...)`.

## Combine with EventBus
Since MPEventBus uses EventBus internally, if you are already using EventBus you don't need to change your existing code. Calls to the EventBus.getDefault() instance will work just as normal. Instead, just replace EventBus with MPEventBus in those cases where you want to send an event to multiple processes. You still need to initialize MPEventBus in your Application class though.
