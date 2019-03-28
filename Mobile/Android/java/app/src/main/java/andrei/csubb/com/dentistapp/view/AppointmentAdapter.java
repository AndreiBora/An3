package andrei.csubb.com.dentistapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import andrei.csubb.com.dentistapp.R;
import andrei.csubb.com.dentistapp.Utils.Util;
import andrei.csubb.com.dentistapp.model.Appointment;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {
    private List<Appointment> appointments = new ArrayList<>();
    private OnItemClickListener listener;

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
        String date = Util.DateToString(currentAppointment.getDate());
        appointmentHolder.tvDate.setText(date);
        appointmentHolder.tvDuration.setText(currentAppointment.getDuration().toString());
        appointmentHolder.tvPatientName.setText(currentAppointment.getPatientName());
        appointmentHolder.tvReason.setText(currentAppointment.getReason());
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
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
