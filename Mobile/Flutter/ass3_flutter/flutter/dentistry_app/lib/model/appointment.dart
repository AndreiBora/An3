import 'package:intl/intl.dart';
import 'dart:convert';

class Appointment {
  int id;
  DateTime dateTime;
  int duration;
  String patientName;
  String reason;
  int inSync = 1;

  Appointment(this.dateTime, this.duration, this.patientName, this.reason);
  
  Appointment.empty() {
    dateTime = DateTime.now();
    duration = 0;
    patientName = '';
    reason = '';
  }
  Appointment.withId({this.id,this.dateTime,this.duration, this.patientName, this.reason});

  factory Appointment.fromJson(Map<String,dynamic> json){
    return Appointment.withId(
      id: json['id'],
      dateTime: DateFormat("yyyy-MM-dd HH:mm").parse(json['dateTime']),
      duration: json['duration'],
      reason: json['reason'],
      patientName: json['patientName']
    );
  }

  

  Map<String,dynamic> toMap(){
    var map = Map<String,dynamic>();
    String dateFormat =DateFormat("yyyy-MM-dd HH:mm").format(dateTime); 
    map["startDate"] = dateFormat;
    map["duration"] = duration;
    map["patientName"] = patientName;
    map["reason"] = reason;
    if(id != null){
      map["id"] = id;
    }
    map["inSync"] = inSync;
    return map;
  }

  Appointment.fromMap(Map<String,dynamic> map){
    print(map);
    id = map["id"];
    dateTime= DateFormat("yyyy-MM-dd HH:mm").parse(map["startDate"]);
    duration = map["duration"];
    print(duration);
    patientName = map["patientName"];
    reason = map["reason"];
    inSync = int.parse(map["inSync"]);
  }

  

  void notInSync(){
    this.inSync = 0;
  }


  @override
    String toString() {
      return "$id "+ dateTime.toIso8601String() + "$duration $patientName $reason $inSync";
    }
}
