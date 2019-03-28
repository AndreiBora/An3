package andrei.csubb.com.dentistapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import andrei.csubb.com.dentistapp.model.Appointment;

public class Util {

    public static String DateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date);
    }

    public static Date StringToDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateStr);
    }

    public static Appointment appFromJSON(JSONObject json) {
        Appointment appointment = null;
        try {
            int id = json.getInt("id");
            String patientName = json.getString("patientName");
            String reason = json.getString("reason");
            Date date = StringToDate(json.getString("dateTime"));
            int duration = json.getInt("duration");
            appointment = new Appointment(id,date,duration,patientName,reason,false);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return appointment;
    }
}
