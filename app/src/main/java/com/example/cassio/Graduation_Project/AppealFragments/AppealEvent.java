package com.example.cassio.Graduation_Project.AppealFragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.AddressListAdapter;
import com.example.cassio.Graduation_Project.FragmentsUnionActivity;
import com.example.cassio.Graduation_Project.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AppealEvent extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private View mView;
    private TextView btn_vision, btn_content, btn_add;
    private EditText txt_vision;
    private EditText txt_content;
    private CharSequence[] options = new CharSequence[]{"Networking", "Music", "Sport", "kids", "theatre", "Campain", "Excursion"};
    private CharSequence options_content_networking[] = new CharSequence[]{"computer science developpers meeting", "engineers meeting"};
    private CharSequence options_content_music[] = new CharSequence[]{"Concert underground", "street Concert "};
    private CharSequence options_content_sport[] = new CharSequence[]{"football", "volleyball", "beach-ball"};
    private CharSequence options_content_kids[] = new CharSequence[]{"concert", "kids meeting", "tech workshop kids", "theatre kids"};
    private CharSequence options_content_theatre[] = new CharSequence[]{"street theatre", ".."};
    private CharSequence options_content_campain[] = new CharSequence[]{"health", "politics", ".."};
    private CharSequence options_content_excursion[] = new CharSequence[]{"Sud", "Nord", "center", "camping"};
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private AddressListAdapter mAddressListAdapter;
    private DatabaseReference appealRef, insightRef;
    private FirebaseAuth mAuth;
    private String m_id;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String themeEvent;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            CharSequence attributions = places.getAttributions();

            if (attributions != null) {
            }
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AddressListAdapter.PlaceAutocomplete item = mAddressListAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_appeal_event, container, false);
        btn_vision = (TextView) mView.findViewById(R.id.vision_id);
        btn_content = (TextView) mView.findViewById(R.id.content_id);

        txt_vision = (EditText) mView.findViewById(R.id.visiontxt_id);
        txt_content = (EditText) mView.findViewById(R.id.contenttxt_id);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        btn_add = (TextView) mView.findViewById(R.id.btn_add);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage((FragmentActivity) getContext(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mAddressListAdapter = new AddressListAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mAddressListAdapter);
        appealRef = FirebaseDatabase.getInstance().getReference().child("Appeals");
        insightRef = FirebaseDatabase.getInstance().getReference().child("Insights");
        mAuth = FirebaseAuth.getInstance();
        m_id = mAuth.getCurrentUser().getUid();


        btn_vision.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        themeEvent();

                    }
                });

        btn_content.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        eventContent(themeEvent);

                    }
                });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String vision = txt_vision.getText().toString().trim();
                final String content = txt_content.getText().toString().trim();
                final String address = mAutocompleteTextView.getText().toString().trim();


                DatabaseReference newAppeal = appealRef.child(m_id).push();

                newAppeal.child("vision").setValue(vision);
                newAppeal.child("content").setValue(content);
                newAppeal.child("address").setValue(address);


                Intent goBackToProfile = new Intent(getContext(), FragmentsUnionActivity.class);
                startActivity(goBackToProfile);


            }
        });


        return mView;
    }

    public String themeEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Suggestions");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < options[i].length(); j++) {
                    txt_vision.setText(options[i]);
                    themeEvent = (String) options[i];
                    if (options[i].equals("Networking")) {


                    }

                }


            }
        });
        builder.show();
        return themeEvent;
    }

    public void eventContent(String themeEvent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("themeEvent", themeEvent);

        if (themeEvent == "Networking") {
            builder.setTitle("For Networking event");
            builder.setItems(options_content_networking, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    txt_content.setText(options_content_networking[i]);

                }
            });
        }
        if (themeEvent == "Music") {
            builder.setTitle("For Music event");
            builder.setItems(options_content_music, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_content.setText(options_content_music[i]);

                }
            });
        }
        if (themeEvent == "Sport") {
            builder.setTitle("For sport event");
            builder.setItems(options_content_sport, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_content.setText(options_content_sport[i]);

                }
            });
        }
        if (themeEvent == "kids") {
            builder.setTitle("For kids event");
            builder.setItems(options_content_kids, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_content.setText(options_content_kids[i]);

                }
            });
        }
        if (themeEvent == "theatre") {
            builder.setTitle("For theatre event");
            builder.setItems(options_content_theatre, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_content.setText(options_content_theatre[i]);

                }
            });
        }
        if (themeEvent == "Campain") {
            builder.setTitle("For campain event");
            builder.setItems(options_content_campain, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_content.setText(options_content_campain[i]);

                }
            });
        }
        if (themeEvent == "Excursion") {
            builder.setTitle("For excursion event");
            builder.setItems(options_content_excursion, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_content.setText(options_content_excursion[i]);

                }
            });
        }


        builder.show();

        mFirebaseAnalytics.setUserProperty("favorite_food", themeEvent);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Google Places API connection failed", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mAddressListAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mAddressListAdapter.setGoogleApiClient(null);

    }
}
