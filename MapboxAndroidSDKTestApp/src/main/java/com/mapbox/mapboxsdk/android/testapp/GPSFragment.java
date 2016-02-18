package com.mapbox.mapboxsdk.android.testapp;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocoahero.android.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.GeoJSONPainter;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.util.DataLoadingUtils;
import com.mapbox.mapboxsdk.views.MapView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GPSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // private static final String ARG_PARAM1 = "param1";
    // private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FeatureCollection features;
    private DataLoadingUtils roads;

    private MapView mapView;

    private NavigationFragment.OnFragmentInteractionListener mListener;

    public GPSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GPSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GPSFragment newInstance(String param1, String param2) {
        GPSFragment fragment = new GPSFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
/*
    public void createFile(Context ctx) throws JSONException, IOException {

        //mapView.loadFromGeoJSONURL("https://api.mapbox.com/v4/directions/mapbox.driving/" + p1Lat + "," + p1Long + ";" + p2Lat + "," + p2Long + ".json?access_token=&lt;your%20access%20token&gt;");
        JSONArray data = new JSONArray();
        JSONObject tour;

        GeoJSONPainter painter();
        painter.loadFromURL("https://api.mapbox.com/v4/directions/mapbox.driving/" + p1Lat + "," + p1Long + ";" + p2Lat + "," + p2Long + ".json?access_token=&lt;your%20access%20token&gt;");

        tour = new JSONObject();
        tour.put("tour", "Salton Sea");
        tour.put("price", 900);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "Death Valley");
        tour.put("price", 600);
        data.put(tour);

        String text = data.toString();

        //FileOutputStream fos = openFileOutput("tours", MODE_PRIVATE);
        FileOutputStream fos = ctx.openFileOutput("tours", ctx.MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();

    }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_localgeojson, container, false);


        // Setup Map
        mapView = (MapView) view.findViewById(R.id.localGeoJSONMapView);
        String address = getArguments().getString("address");
        String address2 = "2328 Stratford Ave Cincinnati 45219";

        Geocoder coder = new Geocoder(GPSFragment.this.getActivity());
        List<Address> addresslist;
        LatLng p1 = null;
        LatLng p2 = null;
        double p1Lat = 0;
        double p1Long = 0;
        double p2Lat = 0;
        double p2Long = 0;

        try {
            addresslist = coder.getFromLocationName(address, 5);
            if (addresslist == null) {
                return null;
            }
            Address location = addresslist.get(0);
            p1Lat = location.getLatitude();
            p1Long = location.getLongitude();

            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        try {
            addresslist = coder.getFromLocationName(address2, 5);
            if (addresslist == null) {
                return null;
            }
            Address location2 = addresslist.get(0);

            p2Lat = location2.getLatitude();
            p2Long = location2.getLongitude();

            location2.getLatitude();
            location2.getLongitude();

            p2 = new LatLng(location2.getLatitude(), location2.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        mapView.setCenter(new LatLng(p1));
        Marker Home = new Marker(mapView, address, "", new LatLng(p1));
        Marker Home2 = new Marker(mapView, address2 , "", new LatLng(p2));

        //Home.setToolTip(new addressWindow(mapView));

        mapView.addMarker(Home);
        mapView.addMarker(Home2);
        mapView.setZoom(14);

        try {

            //FeatureCollection features = DataLoadingUtils.loadGeoJSONFromAssets(getActivity(), "spatialdev_small.geojson");
            //https://api.mapbox.com/v4/directions/mapbox.driving/-122.42,37.78;-77.03,38.91.json?access_token=&lt;your%20access%20token&gt;
            String searchUrl = "https://api.mapbox.com/v4/directions/mapbox.driving/" + p1Lat + "," + p1Long + ";" + p2Lat + "," + p2Long + ".json?access_token=" + getString(R.string.testAccessToken); //url to access driving info
            System.out.println(searchUrl);
            FeatureCollection features = roads.loadGeoJSONFromUrl(searchUrl); //roads is a private DataLoadingUtil declared earlier
            //Log.d("Features", features.getFeatures());
            //FeatureCollection features = roads.loadGeoJSONFromUrl("https://api.mapbox.com/v4/directions/mapbox.driving/" + ".json?access_token=" + getString(R.string.testAccessToken));
            ArrayList<Object> uiObjects = DataLoadingUtils.createUIObjectsFromGeoJSONObjects(features, null); //Load features into objects

            //features.getType();

            //mapView.setStyleURL(Paint.Style.MAPBOX_STREETS);

            for (Object obj : uiObjects) {                          // This will need adjusting for "Points" and correctly drawing the line on roads
                if (obj instanceof Marker) {
                    mapView.addMarker((Marker) obj);
                } else if (obj instanceof PathOverlay) {
                    //((PathOverlay) obj).addPoint(p1);
                    //((PathOverlay) obj).addPoint(p2);
                    //PathOverlay line = new PathOverlay();
                    //line.addPoint(p1);
                    //line.addPoint(p2);
                    mapView.getOverlays().add((PathOverlay) obj);
                }
            }
            if (uiObjects.size() > 0) {
                mapView.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mapView.loadFromGeoJSONURL("https://api.mapbox.com/v4/directions/mapbox.driving/" + p1Lat + "," + p1Long + ";" + p2Lat + "," + p2Long + ".json?access_token=&lt;your%20access%20token&gt;");
        PathOverlay line = new PathOverlay();
        line.addPoint(p1);                              /// This section is a temporary fix, it draws a straight line between point 1 and 2
        line.addPoint(p2);
        mapView.getOverlays().add(line);

        //return inflater.inflate(R.layout.fragment_g, container, false);
        return view;
    }
}




