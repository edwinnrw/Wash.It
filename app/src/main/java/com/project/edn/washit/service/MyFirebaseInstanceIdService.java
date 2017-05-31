package com.project.edn.washit.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by EDN on 5/6/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("TOKEN FIREBASE", FirebaseInstanceId.getInstance().getToken());

    }

    private void saveToken(String tokenId) {

    }
}
