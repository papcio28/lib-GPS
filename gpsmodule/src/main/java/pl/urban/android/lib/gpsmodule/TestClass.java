package pl.urban.android.lib.gpsmodule;

import android.app.Activity;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class TestClass extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GooglePlayLocationProvider locationProvider = new GooglePlayLocationProvider(this, LocationRequestWorkType.STOP_AFTER_TIME);

        locationProvider.

    }
}
