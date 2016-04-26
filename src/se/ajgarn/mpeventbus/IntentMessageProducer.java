package se.ajgarn.mpeventbus;

import android.content.Intent;

/**
 * Puts the data of a specific type to the intent to be sent. Generalizes the
 * {@link Intent} creation so it is the the same for all types that can be sent.
 * <p>
 * Use the factory {@link IntentMessageFactory} to create {@link IntentMessageProducer}s
 * for specific types.
 */
interface IntentMessageProducer {
    void putExtra(String name, Intent intent);
}
