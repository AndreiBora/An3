package andrei.csubb.com.dentistapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import andrei.csubb.com.dentistapp.Utils.Util;

@Entity(tableName = "Appointments")
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Date date;
    private Integer duration;
    private String patientName;
    private String reason;
    private Boolean inSync;

    @Ignore
    public Appointment(Integer id, Date date, Integer duration, String patientName, String reason, Boolean inSync) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.patientName = patientName;
        this.reason = reason;
        this.inSync = inSync;
    }

    public Appointment(Date date, Integer duration, String patientName, String reason, Boolean inSync) {
        this.date = date;
        this.duration = duration;
        this.patientName = patientName;
        this.reason = reason;
        this.inSync = inSync;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getInSync() {
        return inSync;
    }

    public void setInSync(Boolean inSync) {
        this.inSync = inSync;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
            try {
                if(id != null) {
                    json.put("id", this.id);
                }
                json.put("patientName", patientName);
                json.put("dateTime", Util.DateToString(date));
                json.put("duration", duration);
                json.put("reason",reason);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return json;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", date=" + date +
                ", duration=" + duration +
                ", patientName='" + patientName + '\'' +
                ", reason='" + reason + '\'' +
                ", inSync=" + inSync +
                '}';
    }
}
