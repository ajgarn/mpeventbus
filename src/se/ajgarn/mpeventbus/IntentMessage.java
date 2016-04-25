package se.ajgarn.mpeventbus;

import android.content.Intent;
import android.os.Parcelable;

/**
 * Factory class creating {@link IntentMessageProducer}s for all types that can be sent
 * via an {@link Intent}.
 */
class IntentMessage {
    public static IntentMessageProducer get(final Parcelable event) {
        return new IntentMessageProducer() {
            @Override
            public void putExtra(String name, Intent intent) {
                intent.putExtra(name, event);
            }
        };
    }
}
