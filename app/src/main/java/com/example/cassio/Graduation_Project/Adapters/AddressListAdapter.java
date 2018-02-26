package com.example.cassio.Graduation_Project.Adapters;

import android.content.Context;
import android.text.style.CharacterStyle;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.cassio.Graduation_Project.AddEventActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class AddressListAdapter extends ArrayAdapter<AddressListAdapter.PlaceAutocomplete> implements Filterable {
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mPlaceFilter;
    private LatLngBounds mBounds;
    private ArrayList<PlaceAutocomplete> mResultList;
    private CharacterStyle characterStyle;


    public AddressListAdapter(AddEventActivity context, int resource, LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, resource);
        mBounds = bounds;
        mPlaceFilter = filter;
    }


    public AddressListAdapter(Context context, int simple_list_item_1, LatLngBounds boundsMountainView, AutocompleteFilter o) {
        super(context, simple_list_item_1);
        mBounds = boundsMountainView;
        mPlaceFilter = o;
    }


    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
        if (mGoogleApiClient != null) {
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(), mBounds, mPlaceFilter);
            AutocompletePredictionBuffer autocompletePredictions = results.await(30, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();

            if (!status.isSuccess()) {

                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            characterStyle = null;
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), (String) prediction.getFullText(characterStyle)));
            }
            autocompletePredictions.release();
            return resultList;
        }
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mResultList = getPredictions(constraint);
                    if (mResultList != null) {
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    public class PlaceAutocomplete {


        public CharSequence placeId;
        public String description;

        public PlaceAutocomplete(CharSequence placeId, String description) {
            this.placeId = placeId;
            this.description = description;
        }

        public PlaceAutocomplete() {
        }

        public CharSequence _getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public CharSequence getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }


}