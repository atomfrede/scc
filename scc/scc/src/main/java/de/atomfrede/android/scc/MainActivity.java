package de.atomfrede.android.scc;

import com.googlecode.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

@EActivity
public class MainActivity extends Activity {

    private static String TAG = "scc";

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
    }

}

