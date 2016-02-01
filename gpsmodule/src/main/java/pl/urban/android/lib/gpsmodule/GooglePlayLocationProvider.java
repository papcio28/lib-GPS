package pl.urban.android.lib.gpsmodule;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class GooglePlayLocationProvider extends LocationCallback implements LocationProvider, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GPLocationProvider";
    private static final int _LOCATION_REQUEST_DEFAULT_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final long _LOCATION_REQUEST_DEFAULT_INTERVAL = 200;
    private static final long _LOCATION_REQUEST_DEFAULT_FASTEST_INTERVAL = 50;

    private Location sLocation;
    private final GoogleApiClient mApiClient;
    private final Context context;
    private final LocationRequestWorkType requestWorkType;

    public GooglePlayLocationProvider(final Context context, LocationRequestWorkType requestWorkType) {
        this.context = context.getApplicationContext();
        this.requestWorkType = requestWorkType;

        this.mApiClient = new GoogleApiClient.Builder(this.context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mApiClient.connect();
    }

    //    LocationCallback
    @Override
    public void onLocationResult(LocationResult result) {
        super.onLocationResult(result);
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
    }

    //    GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnected(Bundle bundle) throws SecurityException {
        Log.d(TAG, "onConnected");
        sLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    //    GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, connectionResult.getErrorCode() + ": " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    //    LocationProvider
    @Override
    public Location getLastLocation() {
        return sLocation;
    }

    @Override
    public void startTracking() {
        MyLocationRequest myLocationRequest = new MyLocationRequest(context, requestWorkType, mApiClient, this);
    }

    private class MyLocationRequest implements LocationListener {
        private LocationRequest mLocationRequest;
        private GoogleApiClient mApiClient;
        private LocationCallback locationCallback;

        public MyLocationRequest(Context context, LocationRequestWorkType requestWorkType, GoogleApiClient mApiClient, LocationCallback locationCallback) {
            this.mApiClient = mApiClient;
            this.locationCallback = locationCallback;
            this.mLocationRequest = LocationRequest.create()
                    .setPriority(_LOCATION_REQUEST_DEFAULT_PRIORITY)
                    .setInterval(_LOCATION_REQUEST_DEFAULT_INTERVAL)
                    .setFastestInterval(_LOCATION_REQUEST_DEFAULT_FASTEST_INTERVAL);
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        private void stopTracking() {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, locationCallback);
        }


        public void setLocationRequestPriority(final Integer priority) {
            this.mLocationRequest.setPriority(priority != null ? priority : _LOCATION_REQUEST_DEFAULT_PRIORITY);
        }

        public void setLocationRequestInterval(final Integer interval) {
            this.mLocationRequest.setInterval(interval != null ? interval : _LOCATION_REQUEST_DEFAULT_INTERVAL);
        }

        public void setLocationRequestFastestInterval(final Integer fastestInterval) {
            this.mLocationRequest.setFastestInterval(fastestInterval != null ? fastestInterval : _LOCATION_REQUEST_DEFAULT_FASTEST_INTERVAL);
        }
    }
}
