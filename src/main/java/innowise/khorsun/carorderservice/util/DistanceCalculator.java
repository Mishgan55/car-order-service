package innowise.khorsun.carorderservice.util;

public class DistanceCalculator {

    private DistanceCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Разница широты и долготы
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Формула Haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Расстояние между точками
        return PropertyUtil.EARTH_RADIUS * c;
    }
}
