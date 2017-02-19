package com.example.valentin.desu;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    final Calendar dateTime = Calendar.getInstance();
    FragmentActivity context;

    public Button btnDate;
    private DatePickerDialog datePickerDialog;
    public String date;

    public Button btnTime;
    private TimePickerDialog timePickerDialog;
    public String time;

    public  Button btnPhoto;

    public Button btnCreate;

    private FirebaseAuth mAuth;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_event, container, false);

        context = getActivity();
        btnDate = (Button)view.findViewById(R.id.button_Date);
        btnTime= (Button)view.findViewById(R.id.button_Time);
        btnPhoto = (Button)view.findViewById(R.id.button_Photo);
        btnCreate = (Button)view.findViewById(R.id.btn_createEvent);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = dateTime.get(Calendar.YEAR);
                int month = dateTime.get(Calendar.MONTH);
                int day = dateTime.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        NumberFormat f = new DecimalFormat("00");

                        btnDate.setText(f.format(dayOfMonth)+"/"+f.format(month+1)+"/"+year);
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = dateTime.get(Calendar.HOUR_OF_DAY);
                int minute = dateTime.get(Calendar.MINUTE);

                timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        btnTime.setText(hour +":"+minute);
                    }
                },hour,minute,true);

                timePickerDialog.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mNomEvent = ((EditText) context.findViewById(R.id.editText_NomEvent)).getText().toString();
                final String mLieu = ((EditText) context.findViewById(R.id.editText_Lieux)).getText().toString();
                final String mDate = ((Button) context.findViewById(R.id.button_Date)).getText().toString();
                final String mHeure = ((Button) context.findViewById(R.id.button_Time)).getText().toString();
                final String mDescription = ((EditText) context.findViewById(R.id.editText_Description)).getText().toString();
                final boolean mIsPrivate = ((CheckBox) context.findViewById(R.id.checkBox_privateEvent)).isChecked();

                if(!mNomEvent.isEmpty() && !mLieu.isEmpty() && !mDate.equals("Date") && !mDate.equals("Heure")){
                    createEvent(mAuth.getCurrentUser().getUid(),mNomEvent, mLieu, mDate, mHeure, mIsPrivate, mDescription);

                    EventFragment eventFragment = new EventFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main, eventFragment, eventFragment.getTag()).commit();
                }else{
                    Toast.makeText(getActivity(), "Un des champs n'a pas été rempli.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void createEvent(String pCreatorID, String pNomEvent, String pLieu, String pDate, String pTime, boolean pIsPrivate, String pDescription){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference().child("Events");
        DatabaseReference newEventRef = eventsRef.push();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(pDate.replace('/', '-') + " " + pTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

        Map<String, Object> eventData = new HashMap<String, Object>();

        eventData.put("CreatorUID", pCreatorID);
        eventData.put("Nom", pNomEvent);
        eventData.put("Lieu", pLieu);
        eventData.put("Date", timestamp);
        eventData.put("Description", pDescription);
        eventData.put("Prive", pIsPrivate);

        newEventRef.setValue(eventData);
    }

}
