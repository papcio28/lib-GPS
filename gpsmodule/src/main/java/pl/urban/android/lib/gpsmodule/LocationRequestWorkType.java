package pl.urban.android.lib.gpsmodule;

public enum LocationRequestWorkType {
    STOP_AFTER_TASK, //na zadanie (jakas metoda stop)
    STOP_AFTER_TIME(2 * 60 * 1000), //po okreslonym czasie
    STOP_IF_ACCURACY; //po dostaniu lokalizacja o dosc duzej dokladnosci, czyli te stopWhenFixed tzw

    Integer timeInMilis;

    LocationRequestWorkType() {
    }

    LocationRequestWorkType(Integer timeInMilis) {
        this.timeInMilis = timeInMilis;
    }
}
