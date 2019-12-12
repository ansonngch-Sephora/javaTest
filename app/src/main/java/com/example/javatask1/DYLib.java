package com.example.javatask1;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.dynamicyield.dyapi.DYApi;
import com.dynamicyield.listener.itf.DYListenerItf;
import com.dynamicyield.state.DYExperimentsState;

import org.json.JSONObject;

public class DYLib implements DYListenerItf {

    DyExperimentsStateListener dyExperimentsStateListener;

    private static DYLib dyLib ;
    private final Context context;
    private static boolean experimentsReady = false;

    public static DYLib getInstance(Context context) {
        if (dyLib == null) {
            dyLib = new DYLib(context);
        }
        return dyLib;
    }

    public DYLib(Context context) {

        DYApi dyApi = DYApi.getInstance();
        if (dyApi != null) {
            DYApi.getInstance().setListener(this);
        }
        this.context = context;
    }

    public void setDyExperimentsStateListener(DyExperimentsStateListener dyExperimentsStateListener) {
        this.dyExperimentsStateListener = dyExperimentsStateListener;
    }


    @Override
    public void experimentsReadyWithState(DYExperimentsState dyExperimentsState) {
        if (dyExperimentsState == DYExperimentsState.DY_READY_AND_UPDATED || dyExperimentsState == DYExperimentsState.DY_READY_NO_UPDATE_NEEDED) {
            experimentsReady = true;
        }
    }

    @Override
    public void onSmartObjectLoadResult(String s, String s1, View view) {
        Log.d("smartObject", "onSmartObjectLoadResult: ");
    }

    @Override
    public void onSmartActionReturned(String s, JSONObject jsonObject) {
        Log.d("smartAction", "onSmartActionReturned: ");
    }

    public interface DyExperimentsStateListener {
        void experimentsReady();
    }
}
