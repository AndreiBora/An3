package andrei.csubb.com.dentistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import andrei.csubb.com.dentistapp.Utils.Util;

public class AddEditAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_PATIENT_NAME =
            "andrei.csubb.com.dentistapp.EXTRA_PATIENT_NAME";
    public static final String EXTRA_REASON =
            "andrei.csubb.com.dentistapp.EXTRA_REASON";
    public static final String EXTRA_DURATION =
            "andrei.csubb.com.dentistapp.EXTRA_DURATION";
    public static final String EXTRA_DATE =
            "andrei.csubb.com.dentistapp.EXTRA_DATE";
    public static final String EXTRA_ID =
            "andrei.csubb.com.dentistapp.EXTRA_ID";


    private EditText etxtPatientName;
    private EditText etxtReason;
    private ImageView ivDate;
    private Spinner spDuration;
    private Button btnAdd;
    private TextView tvDate;
    private Calendar calendar = Calendar.getInstance();
    private String duration;
    private ImageView ivPickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);


        //initialize
        etxtPatientName = findViewById(R.id.etxt_patient_name);
        etxtReason = findViewById(R.id.etxt_reason);
        ivDate = findViewById(R.id.iv_date);
        spDuration = findViewById(R.id.sp_duration);
        btnAdd = findViewById(R.id.btn_appointment);
        tvDate = findViewById(R.id.tv_date_picked);
        ivPickTime = findViewById(R.id.iv_pick_time);
        //set spinner
        spDuration = findViewById(R.id.sp_duration);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this,
                R.array.duration_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDuration.setAdapter(spAdapter);
        spDuration.setOnItemSelectedListener(this);

        //initialize date text view
        tvDate.setText(Util.DateToString(calendar.getTime()));

        ivDate.setOnClickListener(v -> {
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH);
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(
                    this,
                    (view, year1, month1, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year1);
                        calendar.set(Calendar.MONTH, month1);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        tvDate.setText(Util.DateToString(calendar.getTime()));
                    },
                    year,
                    month,
                    day
            ).show();
        });
        //initialize time image view
        ivPickTime.setOnClickListener(v->{
            new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        tvDate.setText(Util.DateToString(calendar.getTime()));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });
        btnAdd.setOnClickListener(v -> {
            saveAppointment();
        });

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Appointment");
            etxtPatientName.setText(intent.getStringExtra(EXTRA_PATIENT_NAME));
            etxtReason.setText(intent.getStringExtra(EXTRA_REASON));
            int posDuration = spAdapter.getPosition(intent.getCharSequenceExtra(EXTRA_DURATION));
            spDuration.setSelection(posDuration);
            try {
                String dateStr = intent.getStringExtra(EXTRA_DATE);
                Date date = Util.StringToDate(dateStr);
                tvDate.setText(dateStr);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            btnAdd.setText("Update");
        }else{
            setTitle("Add Appointment");
        }
    }

    private void saveAppointment() {
        String patientName = etxtPatientName.getText().toString();
        String reason = etxtReason.getText().toString();
        Intent data = new Intent();
        data.putExtra(EXTRA_PATIENT_NAME, patientName);
        data.putExtra(EXTRA_REASON, reason);
        data.putExtra(EXTRA_DATE, Util.DateToString(calendar.getTime()));
        duration = spDuration.getSelectedItem().toString();
        data.putExtra(EXTRA_DURATION, duration);
        int id = getIntent().getIntExtra(EXTRA_ID,-100);
        if(id != -100){
            data.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK,data);
        finish();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        duration = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
