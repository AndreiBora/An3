import 'dart:convert';
import 'package:dentistry_app/model/appointment.dart';
import 'package:intl/intl.dart';

String toJson(Appointment appointment) {
  var body = {};
  body["dateTime"] =
      DateFormat("yyyy-MM-dd HH:mm").format(appointment.dateTime);
  body["duration"] = appointment.duration;
  body["reason"] = appointment.reason;
  body["patientName"] = appointment.patientName;
  String str = json.encode(body);
  return str;
}

String toJsonList(List<Appointment> appointments) {
  List apps = List();
  appointments.forEach((appointment) {
    var body = {};
    body["dateTime"] =
        DateFormat("yyyy-MM-dd HH:mm").format(appointment.dateTime);
    body["duration"] = appointment.duration;
    body["reason"] = appointment.reason;
    body["patientName"] = appointment.patientName;
    apps.add(body);
  });
  String res = json.encode(apps);
  return res;
}
