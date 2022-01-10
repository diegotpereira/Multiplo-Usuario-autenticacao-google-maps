package com.example.multiplo_usuario_autenticacao_google_maps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    PlacesClient placesClient;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    boolean trackingFlag;
    ListView lstvw;
    AutocompleteSupportFragment supportFragment;
    LocationCallback locationCallback;
    double latitudeAtual, longitudeAtual;
    public static final int MAX = 10;
    String minhaChave = "AIzaSyDaoTFYV3cDjWcecH_e1s4mgJwQ1eV5sNM";
    String[] coloqueName, coloqueEndereco;
    LatLng[] coloqueLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        supportFragment = (AutocompleteSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);

        lstvw = findViewById(R.id.lstvw);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (trackingFlag) {

                    new LocationTask().execute(locationResult.getLastLocation());

                }
            }
        };
        checkLocationPermission();

        if (!Places.isInitialized()) {
            Places.initialize(this, minhaChave);
        }
        placesClient = Places.createClient(this);
        supportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        supportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place != null) {
                    Toast.makeText(MapsActivity.this, "" + place.getName(), Toast.LENGTH_SHORT).show();
                    mMap.clear();

                    LatLng latLng = place.getLatLng();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25f));

                    MarkerOptions colocarMarca = new MarkerOptions();
                    colocarMarca.position(latLng);
                    colocarMarca.title("Buscar Posição");
                    mMap.addMarker(colocarMarca);
                }
            }


            @Override
            public void onError(@NonNull Status status) {

            }


        });
    }

    private void checkLocationPermission() {
        if (!trackingFlag) {
            iniciarRastreamentoLocalizacao();
        } else {
            pararRastreamentoLocalizacao();
        }
    }
    private void iniciarRastreamentoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            trackingFlag = true;
            Toast.makeText(this, "A permissão de localização é concedida", Toast.LENGTH_SHORT).show();
            fusedLocationProviderClient.requestLocationUpdates(peguePedidoLocalizacao(), locationCallback, null);
        }

    }
    private LocationRequest peguePedidoLocalizacao() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(50000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }
    private void pararRastreamentoLocalizacao() {
        if (trackingFlag) {
            trackingFlag = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarRastreamentoLocalizacao();
            } else {
                Toast.makeText(this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void showPlaces(View view) {
        List<Place.Field> placeField = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeField).build();

        if (trackingFlag) {
            @SuppressLint("MissingPermission")
            Task<FindCurrentPlaceResponse> responseTask = placesClient.findCurrentPlace(request);
            responseTask.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse placeResponse = task.getResult();
                        List<PlaceLikelihood> likelihoods = placeResponse.getPlaceLikelihoods();
                        int count;
                        if (likelihoods.size() < MAX) {
                            count = likelihoods.size();
                        } else {
                            count = MAX;
                            coloqueName = new String[count];
                            coloqueEndereco = new String[count];
                            coloqueLatLng = new LatLng[count];
                            int i = 0;
                            for(PlaceLikelihood placeLikelihood : likelihoods) {
                                coloqueName[i] = placeLikelihood.getPlace().getName();
                                coloqueEndereco[i] = placeLikelihood.getPlace().getAddress();
                                coloqueLatLng[i] = placeLikelihood.getPlace().getLatLng();
                                i++;
                                if (i == count) {
                                    break;
                                }
                            }
                            preencherDetalhesLocal();
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void preencherDetalhesLocal() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MapsActivity.this, android.R.layout.simple_list_item_1, coloqueName);
        lstvw.setAdapter(adapter);
    }

    class LocationTask extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... locations) {
            Location minhaLocacao = locations[0];
            String msg = "";

            if (minhaLocacao != null) {
                latitudeAtual = minhaLocacao.getLatitude();
                longitudeAtual = minhaLocacao.getLongitude();
                msg = "done";
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
            if (s.equals("done")) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap = googleMap;
                        mMap.clear();

                        LatLng minhalatlng = new LatLng(latitudeAtual, longitudeAtual);
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mMap.setMyLocationEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhalatlng, 25f));

                        MarkerOptions marcaAtual = new MarkerOptions();
                        marcaAtual.position(minhalatlng);
                        marcaAtual.title("Posição Atual");
                        mMap.addMarker(marcaAtual);
                    }
                });
            }
        }
    }
}