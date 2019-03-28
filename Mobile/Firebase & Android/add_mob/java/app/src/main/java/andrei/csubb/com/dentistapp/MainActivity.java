package andrei.csubb.com.dentistapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import andrei.csubb.com.dentistapp.Utils.FirebaseUtil;
import andrei.csubb.com.dentistapp.model.Appointment;
import andrei.csubb.com.dentistapp.view.AppointmentAdapter;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_APPOINTMENT_REQUEST = 1;
    public static final int EDIT_APPOINTMENT_REQUEST = 2;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private WifiManager wifiManager = null;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Test crash
        //Crashlytics.getInstance().crash();

        FirebaseUtil.openFbReference("appointments");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (wifiManager.isWifiEnabled()) {
                    mDatabaseReference.child(adapter.getAppointmentAt(viewHolder.getAdapterPosition()).getId()).removeValue();
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
                intent.putExtra(AddEditAppointmentActivity.EXTRA_DATE, appointment.getDate());
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
            String date = data.getStringExtra(AddEditAppointmentActivity.EXTRA_DATE);
            //default duration if not provided is 30 min
            Integer duration = Integer.parseInt(data.getStringExtra(AddEditAppointmentActivity.EXTRA_DURATION));
            Appointment appointment = new Appointment(date, duration, patientName, reason);
            mDatabaseReference.push().setValue(appointment);

        } else if (requestCode == EDIT_APPOINTMENT_REQUEST && resultCode == RESULT_OK) {
            String id = data.getStringExtra(AddEditAppointmentActivity.EXTRA_ID);
            if (id == null) {
                Toast.makeText(MainActivity.this, "Appointment cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String patientName = data.getStringExtra(AddEditAppointmentActivity.EXTRA_PATIENT_NAME);
            String reason = data.getStringExtra(AddEditAppointmentActivity.EXTRA_REASON);
            String date = data.getStringExtra(AddEditAppointmentActivity.EXTRA_DATE);
            //default duration if not provided is 30 min
            Integer duration = Integer.parseInt(data.getStringExtra(AddEditAppointmentActivity.EXTRA_DURATION));

            Appointment appointment = new Appointment(date, duration, patientName, reason);
            mDatabaseReference.child(id).setValue(appointment);
        } else {
            Toast.makeText(MainActivity.this, "Appointment was not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
