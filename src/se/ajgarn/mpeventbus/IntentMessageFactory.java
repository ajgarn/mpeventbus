package se.ajgarn.mpeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Factory class creating {@link IntentMessageProducer}s for all types that can be sent
 * via an {@link Intent}.
 */
class IntentMessageFactory {
    public static IntentMessageProducer get(final CharSequence event) {
        return new IntentMessageProducer() {
            @Override
            public void putExtra(String name, Intent intent) {
                intent.putExtra(name, event);
            }
        };
    }
    public static IntentMessageProducer get(final Bundle event) {
        return new IntentMessageProducer() {
            @Override
            public void putExtra(String name, Intent intent) {
                intent.putExtra(name, event);
            }
        };
    }
    public static IntentMessageProducer get(final Parcelable event) {
        return new IntentMessageProducer() {
            @Override
            public void putExtra(String name, Intent intent) {
                intent.putExtra(name, event);
            }
        };
    }
    public static IntentMessageProducer get(final Serializable event) {
        return new IntentMessageProducer() {
            @Override
            public void putExtra(String name, Intent intent) {
                intent.putExtra(name, event);
            }
        };
    }
    public static IntentMessageProducer get(final String event) {
        return new IntentMessageProducer() {
            @Override
            public void putExtra(String name, Intent intent) {
                intent.putExtra(name, event);
            }
        };
    }
}
