package com.example.cassio.Graduation_Project;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InsightsActivity extends Fragment {
    BarChart chart_age,  chart_status ,chart_profile;
    ArrayList<BarEntry> age_Entery,status_Entery,profile_Entery ;
    ArrayList<String> age_label,status_label,profile_label ;
    BarDataSet dataSet_age,dataSet_status,dataSet_profile ;
    BarData bar_age , bar_status,bar_profile;
    PieChart chart_appeal ;
    ArrayList<Entry> appeal_Entery ;
    ArrayList<String> appeal_label ;
    PieDataSet dataset_appeal ;
    PieData bar_appeal ;
    View mView;
    DatabaseReference userRef,appealRef,friendRef,userinsightRef;
    FirebaseAuth mAuth;
    String myId;
    TextView numF,numU;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.activity_insights, container, false);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        appealRef= FirebaseDatabase.getInstance().getReference().child("Appeals");
        friendRef=FirebaseDatabase.getInstance().getReference().child("Community");
        userinsightRef = FirebaseDatabase.getInstance().getReference().child("insightUsers");
        mAuth=FirebaseAuth.getInstance();
        myId=mAuth.getCurrentUser().getUid();
        numF = (TextView) mView.findViewById(R.id.numberfriends_id);
        numU = (TextView) mView.findViewById(R.id.numberuser_id);

        insightAge();
        insightfriends();
        insightStatus();
        insightDemandedAppeals();
        insightDemandedProfilesForCommittee();
        return mView;
    }
    private List<Integer> setColor(){
        String[] colorsTxt = getActivity().getResources().getStringArray(R.array.colors);
        List<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < colorsTxt.length; i++) {
            int newColor = Color.parseColor(colorsTxt[i]);
            colors.add(newColor);
        }
        return colors;
    }
    private void insightDemandedProfilesForCommittee() {
        userinsightRef.child("committeeAppeal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chart_profile = (BarChart) mView.findViewById(R.id.chart_committee);
                profile_Entery = new ArrayList<>();
                profile_label = new ArrayList<String>();
                String a=dataSnapshot.child("developer").getValue().toString();
                String b=dataSnapshot.child("information manager").getValue().toString();
                String c=dataSnapshot.child("marketer").getValue().toString();
                String d=dataSnapshot.child("photographer").getValue().toString();
                String e=dataSnapshot.child("logistic manager").getValue().toString();
                String f=dataSnapshot.child("cordinator").getValue().toString();
                String g=dataSnapshot.child("security manager").getValue().toString();
                profile_Entery.add(new BarEntry(Integer.valueOf(a), 0));
                profile_Entery.add(new BarEntry(Integer.valueOf(b), 1));
                profile_Entery.add(new BarEntry(Integer.valueOf(c), 2));
                profile_Entery.add(new BarEntry(Integer.valueOf(d), 3));
                profile_Entery.add(new BarEntry(Integer.valueOf(e), 4));
                profile_Entery.add(new BarEntry(Integer.valueOf(f), 5));
                profile_Entery.add(new BarEntry(Integer.valueOf(g), 6));

                profile_label.add("developer");
                profile_label.add("information manager");
                profile_label.add("marketer");
                profile_label.add("photographer");
                profile_label.add("logistic manager");
                profile_label.add("cordinator");
                profile_label.add("security manager");
                dataSet_profile = new BarDataSet(profile_Entery, "Appeals For Committee");
                bar_profile = new BarData(profile_label, dataSet_profile);
                List<Integer> color;
                color =setColor();
                dataSet_profile.setColors(color);
                chart_profile.setData(bar_profile);
                chart_profile.animateY(3000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void insightDemandedAppeals() {
        userinsightRef.child("eventAppeals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chart_appeal = (PieChart) mView.findViewById(R.id.chart_event_appeals);
                appeal_Entery = new ArrayList<>();
                appeal_label = new ArrayList<String>();
                String a = dataSnapshot.child("Networking").getValue().toString();
                String b = dataSnapshot.child("Campain").getValue().toString();
                String c = dataSnapshot.child("Music").getValue().toString();
                String d = dataSnapshot.child("Sport").getValue().toString();
                String f = dataSnapshot.child("kids").getValue().toString();
                String g = dataSnapshot.child("theatre").getValue().toString();
                String h = dataSnapshot.child("Excursion").getValue().toString();

                appeal_Entery.add(new BarEntry(Integer.valueOf(a), 0));
                appeal_Entery.add(new BarEntry(Integer.valueOf(b), 1));
                appeal_Entery.add(new BarEntry(Integer.valueOf(c), 2));
                appeal_Entery.add(new BarEntry(Integer.valueOf(d), 3));
                appeal_Entery.add(new BarEntry(Integer.valueOf(f), 4));
                appeal_Entery.add(new BarEntry(Integer.valueOf(g), 5));
                appeal_Entery.add(new BarEntry(Integer.valueOf(h), 6));

                appeal_label.add("Networking");
                appeal_label.add("Campain");
                appeal_label.add("Music");
                appeal_label.add("Sport");
                appeal_label.add("kids");
                appeal_label.add("theatre");
                appeal_label.add("Excursion");

                dataset_appeal = new PieDataSet(appeal_Entery, "Appeals For events");
                bar_appeal = new PieData(appeal_label, dataset_appeal);
                List<Integer> color;
                color =setColor();
                dataset_appeal.setColors(color);
                chart_appeal.setData(bar_appeal);
                chart_appeal.animateY(3000);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void insightStatus() {
        userinsightRef.child("status").child("Event Seeker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chart_status = (BarChart) mView.findViewById(R.id.chart_status);
                status_Entery = new ArrayList<>();
                status_label = new ArrayList<String>();
                String val1 = dataSnapshot.getValue().toString();

                status_Entery.add(new BarEntry(Integer.valueOf(val1), 0));
                status_label.add("Event Seeker");

                userinsightRef.child("status").child("Event planner").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String val2 = dataSnapshot.getValue().toString();

                        status_Entery.add(new BarEntry(Integer.valueOf(val2), 1));
                        status_label.add("Event planner");


                        dataSet_status = new BarDataSet(status_Entery, "user status");
                        bar_status = new BarData(status_label, dataSet_status);
                        List<Integer> color;
                        color =setColor();
                        dataSet_status.setColors(color);
                        chart_status.setData(bar_status);
                        chart_status.animateY(3000);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void insightfriends() {
        friendRef.child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {

                        int num = (int) dataSnapshot1.getChildrenCount();
                        numU.setText(Integer.toString(num));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                    int num = (int) dataSnapshot.getChildrenCount();
                    numF.setText(Integer.toString(num));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void insightAge(){
        Query mDatabaseLowestAge = userRef.orderByChild("age").limitToFirst(1);
        mDatabaseLowestAge.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chart_age = (BarChart) mView.findViewById(R.id.chart_age);
                age_Entery = new ArrayList<>();
                age_label = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    int i = Integer.parseInt(childSnapshot.child("age").getValue().toString());

                    age_Entery.add(new BarEntry(i, 0));

                    age_label.add("min");
                }
                Query q = userRef.orderByChild("age").limitToLast(1);
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            int i = Integer.parseInt(childSnapshot.child("age").getValue().toString());

                            age_Entery.add(new BarEntry(i, 1));

                            age_label.add("max");
                        }

                        dataSet_age = new BarDataSet(age_Entery, "Range age ");
                        bar_age = new BarData(age_label, dataSet_age);
                        List<Integer> color;
                        color =setColor();
                        dataSet_age.setColors(color);
                        chart_age.setData(bar_age);
                        chart_age.animateY(3000);
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }}
