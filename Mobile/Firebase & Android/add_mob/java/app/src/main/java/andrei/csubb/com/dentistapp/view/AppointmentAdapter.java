package andrei.csubb.com.dentistapp.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import andrei.csubb.com.dentistapp.R;
import andrei.csubb.com.dentistapp.Utils.FirebaseUtil;
import andrei.csubb.com.dentistapp.model.Appointment;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {
    private List<Appointment> appointments;
    private OnItemClickListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    public AppointmentAdapter() {
        FirebaseUtil.openFbReference("appointments");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        appointments = FirebaseUtil.appointments;

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                appointment.setId(dataSnapshot.getKey());
                appointments.add(appointment);
                notifyItemInserted(appointments.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment updatedApp = dataSnapshot.getValue(Appointment.class);
                String key = dataSnapshot.getKey();
                Appointment oldApp;
                for (int i = 0; i < appointments.size(); i++) {
                    oldApp = appointments.get(i);
                    if (oldApp.getId().equals(key)) {
                        oldApp.setDate(updatedApp.getDate());
                        oldApp.setDuration(updatedApp.getDuration());
                        oldApp.setPatientName(updatedApp.getPatientName());
                        oldApp.setReason(updatedApp.getReason());
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();
                List<Appointment> appointments1 = new ArrayList<>();
                for (Appointment app : appointments) {
                    if (!app.getId().equals(id)) {
                        appointments1.add(app);
                    }
                }
                appointments = appointments1;
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointment_item, viewGroup, false);
        return new AppointmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder appointmentHolder, int position) {
        Appointment currentAppointment = appointments.get(position);
        String date = currentAppointment.getDate();
        appointmentHolder.tvDate.setText(date);
        appointmentHolder.tvDuration.setText(currentAppointment.getDuration().toString());
        appointmentHolder.tvPatientName.setText(currentAppointment.getPatientName());
        appointmentHolder.tvReason.setText(currentAppointment.getReason());
    }

    @Override
    public int getItemCount() {
        if (appointments == null) {
            return 0;
        }
        return appointments.size();
    }

    public Appointment getAppointmentAt(int position) {
        return appointments.get(position);
    }

    class AppointmentHolder extends RecyclerView.ViewHolder {

        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvReason = itemView.findViewById(R.id.tv_reason);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(appointments.get(position));
                    }
                }
            });
        }

        private TextView tvDate;
        private TextView tvDuration;
        private TextView tvPatientName;
        private TextView tvReason;
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
