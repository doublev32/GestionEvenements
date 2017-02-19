package com.example.valentin.desu;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    final Calendar dateTime = Calendar.getInstance();
    Context context;

    public Button btnDate;
    private DatePickerDialog datePickerDialog;
    public String date;

    public Button btnTime;
    private TimePickerDialog timePickerDialog;
    public String time;

    public  Button btnPhoto;

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
                        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        NumberFormat f = new DecimalFormat("00");

                        btnDate.setText(f.format(dayOfMonth)+"/"+f.format(month)+"/"+year);
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

        super.onActivityCreated(savedInstanceState);
    }

}