package pl.urban.android.lib.gpsmodule;

import android.location.Location;

public interface LocationRequestListener {
    void onLocationChanged(final AbstractLocationProvider.AbstractLocationRequest request, final Location location);

    void onRequestStopped(final AbstractLocationProvider.AbstractLocationRequest request);
}
