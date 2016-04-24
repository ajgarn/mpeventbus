package se.ajgarn.mpeventbus;

import android.content.Intent;

/**
 * @author Anton Jansson.
 */
interface IntentMessageProducer {
    void putExtra(String name, Intent intent);
}
