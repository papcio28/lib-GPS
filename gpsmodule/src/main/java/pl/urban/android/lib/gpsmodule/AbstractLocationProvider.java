package pl.urban.android.lib.gpsmodule;

import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;

public abstract class AbstractLocationProvider {
    private Location mLastLocation;

    public Location getLastLocation() {
        return mLastLocation;
    }

    protected void setLastLocation(final Location lastLocation) {
        mLastLocation = lastLocation;
    }

    public abstract AbstractLocationRequest startTracking(LocationRequestListener locationRequestListener);

    public abstract AbstractLocationRequest startTracking(LocationRequestListener locationRequestListener, int timeout);

    public abstract class AbstractLocationRequest {
        private final LocationRequestListener mLocationCallback;
        private final Integer mTimeout;
        private LocationRequestType mRequestType;

        private final Runnable mStopRunnable = this::stopTracking;
        private Handler mTimingHandler;

        public AbstractLocationRequest(@NonNull final LocationRequestListener listener) {
            this(listener, -1);
            this.mRequestType = LocationRequestType.STOP_AFTER_TASK;
        }

        public AbstractLocationRequest(@NonNull final LocationRequestListener listener, final int timeout) {
            this.mRequestType = LocationRequestType.STOP_AFTER_TIME;
            this.mLocationCallback = listener;
            this.mTimeout = timeout;
        }

        protected final LocationRequestType getRequestType() {
            return mRequestType;
        }

        protected abstract void handleStartTracking();

        protected abstract void handleStopTracking();

        public void startTracking() {
            handleStartTracking();
            if (getRequestType() == LocationRequestType.STOP_AFTER_TIME) {
                mTimingHandler = new Handler();
                mTimingHandler.postDelayed(mStopRunnable, mTimeout);
            }
        }

        public void stopTracking() {
            if (mTimingHandler != null) {
                mTimingHandler.removeCallbacks(mStopRunnable);
                mTimingHandler = null;
            }
            handleStopTracking();
            mLocationCallback.onRequestStopped(this);
        }

        protected final void processNewLocation(final Location location) {
            setLastLocation(location);
            mLocationCallback.onLocationChanged(this, getLastLocation());
            if (getRequestType() == LocationRequestType.STOP_AFTER_TASK) {
                stopTracking();
            }
        }
    }
}
