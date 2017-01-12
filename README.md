MPEventBus - Multi-Process EventBus
===
**Note:** This project is under development and has not been finalized yet.

MPEventBus is an extension to [greenrobot/EventBus](https://github.com/greenrobot/EventBus) that lets you send events between processes in your Android application.

In [greenrobot/EventBus](https://github.com/greenrobot/EventBus), you can send arbitrary events that your classes subscribed to the event bus receive. MPEventBus can, in addition, send events via Intents and listen to events from other processes via BroadcastReceivers, which delegate all received events to the EventBus instance running on the current process.

In order to send an event object between processes, the object must be either Serializable, Parcelable, a Bundle or a string. 

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

    public class CustomEvent implements Serializable {/* Optional body */}

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
You can use EventBus and MPEventBus at the same time, since MPEventBus delegates to the EventBus instance. In other words, calls to the EventBus.getDefault() instance will work as they did before. In those cases you want to send events between processes, you need to make sure that the event object is either Serializable, Parcelable, a Bundle or a string. Then, initialize MPEventBus as described in the steps above.

## Other
Feel free to send pull requests if you want to improve anything.

## License
Copyright &copy; 2016-2017 Anton Jansson

The project is released under the [Apache License, Version 2.0](LICENSE)
