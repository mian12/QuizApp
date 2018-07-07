package com.solution.alnahar.quizapp.pushNotification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    //ctrlO

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
       String refreshedToken= FirebaseInstanceId.getInstance().getToken();

       sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.e("token",refreshedToken);
    }
}
