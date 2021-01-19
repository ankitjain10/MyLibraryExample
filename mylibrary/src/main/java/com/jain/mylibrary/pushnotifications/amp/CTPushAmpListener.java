package com.jain.mylibrary.pushnotifications.amp;

import android.os.Bundle;

/**
 * Interface definition for a callback to be invoked whenever push amp payload is received.
 */
public interface CTPushAmpListener {

    /**
     * Receives a callback whenever push amp payload is received.
     */
    void onPushAmpPayloadReceived(Bundle extras);
}
