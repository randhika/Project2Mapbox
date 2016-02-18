package com.mapbox.mapboxsdk.android.testapp;
    import android.location.Address;
    import android.location.Geocoder;
    import android.os.Bundle;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import com.mapbox.mapboxsdk.geometry.LatLng;
    import com.mapbox.mapboxsdk.overlay.Marker;
    import com.mapbox.mapboxsdk.views.InfoWindow;
    import com.mapbox.mapboxsdk.views.MapView;

    import java.util.List;


    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link userAddressFragment.OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link userAddressFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class userAddressFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String addressKey;
        private String addressValue;


        public userAddressFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment userAddressFragment.
         */
        // TODO: Rename and change types and number of parameters
    /*public static userAddressFragment newInstance(String addressKey, String addressValue) {
        userAddressFragment fragment = new userAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/


        private MapView mapView;

// This is the custom info window that launches the address when clicking the marker as well as launches the new fragment on click
    public class addressWindow extends InfoWindow {
        String address = getArguments().getString("address");
        public addressWindow(MapView mv) {
            super(R.layout.infowindow_custom, mv);

            setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Demonstrate custom onTouch()
                        setOnTouchListener(this);

                        Fragment fragment;
                        fragment = new GPSFragment();
                        Bundle args = new Bundle();
                        args.putString("address",address);
                        fragment.setArguments(args);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        // Still close the InfoWindow though
                        close();
                    }

                    // Return true as we're done processing this event
                    return true;
                }
            });
        }

        /**
         * Dynamically set the content in the CustomInfoWindow
         * @param overlayItem The tapped Marker
         */
        @Override
        public void onOpen(Marker overlayItem) {
            String title = overlayItem.getTitle();
            ((TextView) mView.findViewById(R.id.customTooltip_title)).setText(title);
        }
    }
        //This sets up the map with the user address after it is converted to lat and long coordinates
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localgeojson, container, false);


        // Setup Map
        mapView = (MapView) view.findViewById(R.id.localGeoJSONMapView);
        // mapView.goToUserLocation(true);
        mapView.setCenter(new LatLng(47.668780, -122.387883));
        mapView.setZoom(14);
        // mapView.getUserLocationEnabled();

        String address = getArguments().getString("address");

        Geocoder coder = new Geocoder(userAddressFragment.this.getActivity());
        List<Address> addresslist;
        LatLng p1 = null;

        try {
            addresslist = coder.getFromLocationName(address, 5);
            if (addresslist == null) {
                return null;
            }
            Address location = addresslist.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        //This adds the markers and centers the map on its location

        mapView.setCenter(new LatLng(p1));
        Marker Home = new Marker(mapView, address + "  Navigate Here", "", new LatLng(p1));

        Home.setToolTip(new addressWindow(mapView));



        mapView.addMarker(Home);
        return view;


    }



    @Override
    public void onResume() {
        super.onResume();







    }


}
