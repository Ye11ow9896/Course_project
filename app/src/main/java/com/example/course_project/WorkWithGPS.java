//package com.example.course_project;
//
//import java.util.Date;
//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import androidx.core.app.ActivityCompat;
//
//public class WorkWithGPS extends Activity {
//
//    public LocationManager locationManager;
//    double latitude=lat;
//    double longitude=lng;
//    float distance=0;
//    Location crntLocation=new Location("crntlocation");
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        crntLocation = new Location("crntlocation");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000, 10, locationListener);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationManager.removeUpdates(locationListener);
//    }
//
//
//    private LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            showLocation(location);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            if (ActivityCompat.checkSelfPermission(null, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(null, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                       int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            showLocation(locationManager.getLastKnownLocation(provider));
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            //if (provider.equals(LocationManager.GPS_PROVIDER)) {
//            //    tvStatusGPS.setText("Status: " + String.valueOf(status));
//            //}
//        }
//
//
//        private void showLocation(Location location) {
//                        if (location == null)
//                return;
//            else if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//                tvLocationGPS.setText(formatLocation(location));
//            }
//        }
//        private String formatLocation(Location location) {
//            if (location == null)
//                return "";
//            return String.format(
//                    "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
//                    location.getLatitude(), location.getLongitude(), new Date(
//                            location.getTime()));
//        }
//    };
//}