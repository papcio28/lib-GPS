package pl.urban.android.lib.gpsmodule;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

public abstract class LocationPingIntentService extends IntentService {

    public LocationPingIntentService(final String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final LocationProvider locationProvider = getLocationProvider();
        // TODO: Implement
    }

    /**
     * Provides LocationProvider implementation
     */
    protected abstract LocationProvider getLocationProvider();

    /**
     * Provides next service fire timestamp for AlarmManager
     */
    protected abstract long getNextFireTimestamp();

    /**
     * Subclasses should override this method to handle obtained Location object
     */
    protected abstract long handleLocation(Location location);
}
