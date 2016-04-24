package se.ajgarn.mpeventbus;

import android.content.Intent;
import android.os.Parcelable;

/**
 * @author Anton Jansson.
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
