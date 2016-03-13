package pl.urban.android.lib.gpsmodule.impl;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import pl.urban.android.lib.gpsmodule.AbstractLocationProvider;
import pl.urban.android.lib.gpsmodule.LocationRequestListener;

public class GooglePlayLocationProvider extends AbstractLocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GPSLocationProvider";
    private static final int LOCATION_REQUEST_DEFAULT_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final long LOCATION_REQUEST_DEFAULT_INTERVAL = 200;
    private static final long LOCATION_REQUEST_DEFAULT_FASTEST_INTERVAL = 50;

    private final GoogleApiClient mApiClient;
    private final Context mContext;

    public GooglePlayLocationProvider(final Context context) {
        this.mContext = context.getApplicationContext();

        this.mApiClient = new GoogleApiClient.Builder(this.mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mApiClient.connect();
    }

    @Override
    public Location getLastLocation() {
        if (mApiClient.isConnected()) {
            final Location googleLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
            if (googleLocation != null) {
                setLastLocation(googleLocation);
            }
        }
        return super.getLastLocation();
    }

    @Override
    public void onConnected(Bundle bundle) throws SecurityException {
        Log.d(TAG, "onConnected");
        setLastLocation(LocationServices.FusedLocationApi.getLastLocation(mApiClient));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext, connectionResult.getErrorCode() + ": " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public GooglePlayLocationRequest startTracking(LocationRequestListener locationRequestListener) {
        return new GooglePlayLocationRequest(mApiClient, locationRequestListener);
    }

    @Override
    public GooglePlayLocationRequest startTracking(LocationRequestListener locationRequestListener, int timeout) {
        return new GooglePlayLocationRequest(mApiClient, locationRequestListener, timeout);
    }

    public class GooglePlayLocationRequest extends AbstractLocationRequest implements LocationListener {
        private LocationRequest mLocationRequest;
        private GoogleApiClient mApiClient;

        public GooglePlayLocationRequest(final GoogleApiClient apiClient, final LocationRequestListener listener) {
            super(listener);
            init(apiClient);
        }

        public GooglePlayLocationRequest(final GoogleApiClient apiClient, final LocationRequestListener listener, final int timeout) {
            super(listener, timeout);
            init(apiClient);
        }

        private void init(final GoogleApiClient apiClient) {
            this.mApiClient = apiClient;
            this.mLocationRequest = LocationRequest.create()
                    .setInterval(LOCATION_REQUEST_DEFAULT_INTERVAL)
                    .setFastestInterval(LOCATION_REQUEST_DEFAULT_FASTEST_INTERVAL)
                    .setPriority(LOCATION_REQUEST_DEFAULT_PRIORITY);
        }

        @Override
        protected void handleStartTracking() {
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
        }

        @Override
        protected void handleStopTracking() {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
        }

        @Override
        public void onLocationChanged(final Location location) {
            processNewLocation(location);
        }
    }
}
