package andrei.csubb.com.dentistapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import andrei.csubb.com.dentistapp.Utils.Util;
import andrei.csubb.com.dentistapp.model.Appointment;
import andrei.csubb.com.dentistapp.view.AppointmentAdapter;
import andrei.csubb.com.dentistapp.view.AppointmentViewModel;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_APPOINTMENT_REQUEST = 1;
    public static final int EDIT_APPOINTMENT_REQUEST = 2;

    private AppointmentViewModel appointmentViewModel;
    private WifiManager wifiManager = null;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_add:
                        Intent intent = new Intent(MainActivity.this, AddEditAppointmentActivity.class);
                        startActivityForResult(intent, ADD_APPOINTMENT_REQUEST);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);


        FloatingActionButton buttonAddAppointment = findViewById(R.id.btn_add_appointment);
        buttonAddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditAppointmentActivity.class);
            startActivityForResult(intent, ADD_APPOINTMENT_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.appointments_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AppointmentAdapter adapter = new AppointmentAdapter();
        recyclerView.setAdapter(adapter);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        appointmentViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        appointmentViewModel.getAppointments().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                adapter.setAppointments(appointments);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (wifiManager.isWifiEnabled()) {
                    appointmentViewModel.delete(adapter.getAppointmentAt(viewHolder.getAdapterPosition()), MainActivity.this);
                }
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (!wifiManager.isWifiEnabled()) {
                    Toast.makeText(MainActivity.this, "Cannot perform delete because device is offline", Toast.LENGTH_SHORT).show();
                    return makeMovementFlags(0, 0);
                }
                return super.getMovementFlags(recyclerView, viewHolder);
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(appointment -> {
            if (wifiManager.isWifiEnabled()) {
                Intent intent = new Intent(MainActivity.this, AddEditAppointmentActivity.class);
                intent.putExtra(AddEditAppointmentActivity.EXTRA_ID, appointment.getId());
                intent.putExtra(AddEditAppointmentActivity.EXTRA_PATIENT_NAME, appointment.getPatientName());
                intent.putExtra(AddEditAppointmentActivity.EXTRA_REASON, appointment.getReason());
                intent.putExtra(AddEditAppointmentActivity.EXTRA_DATE, Util.DateToString(appointment.getDate()));
                intent.putExtra(AddEditAppointmentActivity.EXTRA_DURATION, appointment.getDuration());
                startActivityForResult(intent, EDIT_APPOINTMENT_REQUEST);
            } else {
                Toast.makeText(MainActivity.this, "Cannot perform update because device is offline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_APPOINTMENT_REQUEST && resultCode == RESULT_OK) {
            String patientName = data.getStringExtra(AddEditAppointmentActivity.EXTRA_PATIENT_NAME);
            String reason = data.getStringExtra(AddEditAppointmentActivity.EXTRA_REASON);
            String dateStr = data.getStringExtra(AddEditAppointmentActivity.EXTRA_DATE);
            //default duration if not provided is 30 min
            Integer duration = Integer.parseInt(data.getStringExtra(AddEditAppointmentActivity.EXTRA_DURATION));
            Date date = null;
            try {
                date = Util.StringToDate(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Appointment appointment = new Appointment(date, duration, patientName, reason, false);
            appointmentViewModel.insert(appointment, this, wifiManager);

        } else if (requestCode == EDIT_APPOINTMENT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditAppointmentActivity.EXTRA_ID, 0);
            if (id == 0) {
                Toast.makeText(MainActivity.this, "Appointment cannot be updated", Toast.LENGTH_SHORT).show();
            }
            String patientName = data.getStringExtra(AddEditAppointmentActivity.EXTRA_PATIENT_NAME);
            String reason = data.getStringExtra(AddEditAppointmentActivity.EXTRA_REASON);
            String dateStr = data.getStringExtra(AddEditAppointmentActivity.EXTRA_DATE);
            //default duration if not provided is 30 min
            Integer duration = Integer.parseInt(data.getStringExtra(AddEditAppointmentActivity.EXTRA_DURATION));
            Date date = null;
            try {
                date = Util.StringToDate(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Appointment appointment = new Appointment(date, duration, patientName, reason, false);
            appointment.setId(id);
            appointmentViewModel.update(appointment, MainActivity.this);
        } else {
            Toast.makeText(MainActivity.this, "Appointment was not saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                appointmentViewModel.pushNotSync();
            }
        }
    };
}
