package pl.urban.android.lib.gpsmodule;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;

public abstract class LocationPingIntentService extends IntentService {
    private static final int _LOCATION_PING_REQUEST_CODE = 1;

    public LocationPingIntentService(final String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final LocationProvider locationProvider = getLocationProvider();
        handleLocation(locationProvider.getLastLocation());

        PendingIntent pendingIntent = PendingIntent.getService(this, _LOCATION_PING_REQUEST_CODE,
                new Intent(this, getClass()), PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, getNextFireTimestamp(), pendingIntent);
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
