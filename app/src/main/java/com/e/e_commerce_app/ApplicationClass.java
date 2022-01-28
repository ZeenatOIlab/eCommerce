package com.e.e_commerce_app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.e.e_commerce_app.Activitys.VerificationActivity;
import com.e.e_commerce_app.Model.CartModel;
import com.e.e_commerce_app.Model.MyOrderModel;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;

public class  ApplicationClass extends Application {
    public static ArrayList<CartModel> cartModelslist = new ArrayList<>();
    public static ArrayList<MyOrderModel> orderList = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        // Logging set to help debug issues, remove before releasing your app.

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }
}
