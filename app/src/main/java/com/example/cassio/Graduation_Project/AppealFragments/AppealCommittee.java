package com.example.cassio.Graduation_Project.AppealFragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AppealCommittee extends Fragment implements View.OnClickListener {


    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    View mView;
    String txt;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private AddressListAdapter mAddressListAdapter;
    private DatabaseReference appealRef, insightusersRef;
    private FirebaseAuth mAuth;
    private String m_id;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView btn_profile, btn_add;
    private EditText txt_topic, txt_idea, txt_money, txt_profile;
    private ImageView btn_topic, btn_idea, btn_money1, btn_money2;
    private String themeEvent, profile;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_appeal_committee, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        btn_add = (TextView) mView.findViewById(R.id.btn_add);
        btn_topic = (ImageView) mView.findViewById(R.id.vision_id);
        btn_idea = (ImageView) mView.findViewById(R.id.content_id);
        btn_money1 = (ImageView) mView.findViewById(R.id.paid_id);
        btn_money1.setOnClickListener(this);
        btn_money2 = (ImageView) mView.findViewById(R.id.notpaid_id);
        btn_money2.setOnClickListener(this);

        txt_topic = (EditText) mView.findViewById(R.id.visiontxt_id);
        txt_idea = (EditText) mView.findViewById(R.id.contenttxt_id);
        txt_money = (EditText) mView.findViewById(R.id.salartext_id);
        txt_profile = (EditText) mView.findViewById(R.id.profiletxt_id);
        btn_profile = (TextView) mView.findViewById(R.id.committee_sug_id);


//        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage((FragmentActivity) getContext(), GOOGLE_API_CLIENT_ID, (GoogleApiClient.OnConnectionFailedListener) this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .build();
        mAutocompleteTextView = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mAddressListAdapter = new AddressListAdapter(getContext(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mAddressListAdapter);
        appealRef = FirebaseDatabase.getInstance().getReference().child("AppealsCommittee");

        mAuth = FirebaseAuth.getInstance();
        m_id = mAuth.getCurrentUser().getUid();


        btn_topic.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        themeEvent();

                    }
                });

        btn_idea.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventContent(themeEvent);


                    }
                });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                committeeProfile();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = txt_topic.getText().toString();
                String idea = txt_idea.getText().toString();
                String where = mAutocompleteTextView.getText().toString();
                String money = txt_money.getText().toString();
                final String profile = txt_profile.getText().toString();

                if (TextUtils.isEmpty(topic) || (TextUtils.isEmpty(idea)) || (TextUtils.isEmpty(where)) || (TextUtils.isEmpty(money)) || (TextUtils.isEmpty(profile))) {
                    Toast.makeText(getContext(), "please make sure all fields are filled", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference newCommitteeAppeal = appealRef.child(m_id).push();

                    newCommitteeAppeal.child("topic").setValue(topic);
                    newCommitteeAppeal.child("idea").setValue(idea);
                    newCommitteeAppeal.child("where").setValue(where);
                    newCommitteeAppeal.child("money").setValue(money);
                    newCommitteeAppeal.child("profile").setValue(profile);
                    insightusersRef = FirebaseDatabase.getInstance().getReference().child("insightUsers");


                    insightusersRef.child("committeeAppeal").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {

                                insightusersRef.child("committeeAppeal").child("developer").setValue(0);
                                insightusersRef.child("committeeAppeal").child("information manager").setValue(0);
                                insightusersRef.child("committeeAppeal").child("marketer").setValue(0);
                                insightusersRef.child("committeeAppeal").child("photographer").setValue(0);
                                insightusersRef.child("committeeAppeal").child("logistic manager").setValue(0);
                                insightusersRef.child("committeeAppeal").child("cordinator").setValue(0);
                                insightusersRef.child("committeeAppeal").child("security manager").setValue(0);
                                insightusersRef.child("committeeAppeal").child(profile).setValue(1);


                            } else {
                                String x;
                                int i;
                                switch (profile) {
                                    case "developer":
                                        x = dataSnapshot.child("developer").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("developer").setValue(i);
                                        break;
                                    case "information manager":
                                        x = dataSnapshot.child("information manager").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("information manager").setValue(i);
                                        break;
                                    case "marketer":
                                        x = dataSnapshot.child("marketer").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("marketer").setValue(i);
                                        break;

                                    case "photographer":
                                        x = dataSnapshot.child("photographer").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("photographer").setValue(i);
                                        break;

                                    case "logistic manager":
                                        x = dataSnapshot.child("logistic manager").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("logistic manager").setValue(i);
                                        break;

                                    case "cordinator":
                                        x = dataSnapshot.child("cordinator").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("cordinator").setValue(i);
                                        break;

                                    case "security manager":
                                        x = dataSnapshot.child("security manager").getValue().toString();
                                        i = cal(x);
                                        insightusersRef.child("committeeAppeal").child("security manager").setValue(i);
                                        break;

                                    default:
                                        break;
                                }


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Intent intent = new Intent(getContext(), FragmentsUnionActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "your appeal for a committe is been saved", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                }
            }
        });
        return mView;


    }

    public int cal(String x) {
        int i = Integer.valueOf(x);
        i++;
        return i;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.paid_id:


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.editTextAdd);
                final RadioGroup Rgrp = (RadioGroup) dialogView.findViewById(R.id.rbtn);


                dialogBuilder.setTitle("Ammount of money to be paid");
                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedId = Rgrp.getCheckedRadioButtonId();
                        switch (selectedId) {
                            case R.id.week_1:
                                txt = "week";
                                break;
                            case R.id.day_1:
                                txt = "day";
                                break;
                            case R.id.month_1:
                                txt = "month";
                                break;

                            default:
                                break;
                        }
                        txt_money.setText(edt.getText() + " dt per " + txt);


                    }
                })
                        .show();
                break;
            case R.id.notpaid_id:
                txt_money.setText("I need a volunteer");
        }
    }

    public String themeEvent() {
        final CharSequence[] options = getResources().getStringArray(R.array.theme_item);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Suggestions");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < options[i].length(); j++) {
                    txt_topic.setText(options[i]);
                    themeEvent = (String) options[i];


                }


            }
        });
        builder.show();
        return themeEvent;
    }

    public String committeeProfile() {
        final CharSequence[] committee_profile = getResources().getStringArray(R.array.committee_profile);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Suggestions");
        builder.setItems(committee_profile, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < committee_profile[i].length(); j++) {
                    txt_profile.setText(committee_profile[i]);
                    profile = (String) committee_profile[i];


                }


            }
        });
        builder.show();
        return profile;
    }

    public void eventContent(String themeEvent) {
        final CharSequence[] options_content_music = getResources().getStringArray(R.array.options_content_music);
        final CharSequence options_content_networking[] = getResources().getStringArray(R.array.options_content_networking);
        final CharSequence options_content_sport[] = getResources().getStringArray(R.array.options_content_sport);
        final CharSequence options_content_kids[] = getResources().getStringArray(R.array.options_content_kids);
        final CharSequence options_content_theatre[] = getResources().getStringArray(R.array.options_content_theatre);
        final CharSequence options_content_campain[] = getResources().getStringArray(R.array.options_content_campain);
        final CharSequence options_content_excursion[] = getResources().getStringArray(R.array.options_content_excursion);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("themeEvent", themeEvent);

        if (themeEvent == "Networking") {
            builder.setTitle("For Networking event");
            builder.setItems(options_content_networking, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    txt_idea.setText(options_content_networking[i]);

                }
            });
        }
        if (themeEvent == "Music") {
            builder.setTitle("For Music event");
            builder.setItems(options_content_music, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_idea.setText(options_content_music[i]);

                }
            });
        }
        if (themeEvent == "Sport") {
            builder.setTitle("For sport event");
            builder.setItems(options_content_sport, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_idea.setText(options_content_sport[i]);

                }
            });
        }
        if (themeEvent == "kids") {
            builder.setTitle("For kids event");
            builder.setItems(options_content_kids, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_idea.setText(options_content_kids[i]);

                }
            });
        }
        if (themeEvent == "theatre") {
            builder.setTitle("For theatre event");
            builder.setItems(options_content_theatre, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_idea.setText(options_content_theatre[i]);

                }
            });
        }
        if (themeEvent == "Campain") {
            builder.setTitle("For campain event");
            builder.setItems(options_content_campain, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_idea.setText(options_content_campain[i]);

                }
            });
        }
        if (themeEvent == "Excursion") {
            builder.setTitle("For excursion event");
            builder.setItems(options_content_excursion, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txt_idea.setText(options_content_excursion[i]);

                }
            });
        }


        builder.show();




    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Google Places API connection failed", Toast.LENGTH_LONG).show();

    }

    public void onConnected(@Nullable Bundle bundle) {
        mAddressListAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    public void onConnectionSuspended(int i) {
        mAddressListAdapter.setGoogleApiClient(null);

    }
}
