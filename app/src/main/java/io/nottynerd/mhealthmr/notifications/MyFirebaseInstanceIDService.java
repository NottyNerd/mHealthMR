package io.nottynerd.mhealthmr.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import io.nottynerd.mhealthmr.utility.RegisterPushTask;
import io.nottynerd.mhealthmr.utility.Utilities;

/**
 * Created by NottyNerd on 06/02/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String FRIENDLY_ENGAGE_TOPIC = "crizizMessage";
    private static final String DEAL_ENGAGE_TOPIC = "mHealthReminder";

    @Override
    public void onTokenRefresh() {
       // super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("LOGGER", "Refreshed token: " + refreshedToken);
        try {
            FirebaseMessaging.getInstance()
                    .subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
            FirebaseMessaging.getInstance()
                    .subscribeToTopic(DEAL_ENGAGE_TOPIC);

            Utilities.savePersistedData(Utilities.GCM_REG_ID_KEY, refreshedToken, getApplicationContext());
            // TODO: Implement this method to send any registration to your app's servers.

            sendRegistrationToServer(refreshedToken);

        }catch (Exception ex)
        {}



    }

    private void sendRegistrationToServer(String refreshedToken) {
        // Once a token is generated, we subscribe to topic.

    }
}
