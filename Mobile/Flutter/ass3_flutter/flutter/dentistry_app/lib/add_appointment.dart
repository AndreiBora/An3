import 'package:dentistry_app/model/appointment.dart';
import 'package:dentistry_app/util/dbhelper.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:async';
import 'dart:convert';
import 'package:connectivity/connectivity.dart';

class AddAppointment extends StatefulWidget {
  final Appointment appointment;

  AddAppointment(this.appointment);

  @override
  State<StatefulWidget> createState() {
    return _AddAppointment(this.appointment);
  }
}

class _AddAppointment extends State<AddAppointment> {
  Appointment appointment;
  List<int> _durations = [30, 45, 60, 75, 90, 120];
  DbHelper dbHelper = DbHelper();
  _AddAppointment(this.appointment);

  Future _selectDate() async {
    DateTime picked = await showDatePicker(
        context: context,
        initialDate: new DateTime.now(),
        firstDate: new DateTime(2016),
        lastDate: new DateTime(2019));

    if (picked != null) setState(() => appointment.dateTime = picked);
  }

  Future<void> _selectTime() async {
    final TimeOfDay picked =
        await showTimePicker(context: context, initialTime: TimeOfDay.now());

    if (picked != null) {
      DateTime dt = DateTime(
          appointment.dateTime.year,
          appointment.dateTime.month,
          appointment.dateTime.day,
          picked.hour,
          picked.minute);
      setState(() => appointment.dateTime = dt);
    }
  }

  @override
  Widget build(BuildContext context) {
    double _formPadding = 5.0;
    String dateStr = appointment.dateTime != null
        ? DateFormat('yyyy-MM-dd').format(widget.appointment.dateTime)
        : '';
    String timeStr = appointment.dateTime != null
        ? DateFormat('jm').format(widget.appointment.dateTime)
        : '';
    return Scaffold(
        appBar: AppBar(title: Text('Add appointment')),
        body: Container(
          padding: EdgeInsets.all(15.0),
          child: ListView(
            children: <Widget>[
              Container(
                padding: EdgeInsets.all(_formPadding),
                alignment: Alignment.topLeft,
                child: Text('Date: ' + dateStr, textScaleFactor: 1.3),
              ),
              IconButton(icon: Icon(Icons.date_range), onPressed: _selectDate),
              Container(
                alignment: Alignment.topLeft,
                child: Text('Time: ' + timeStr, textScaleFactor: 1.3),
              ),
              IconButton(icon: Icon(Icons.access_time), onPressed: _selectTime),
              Text(
                'Duration: ' + appointment.duration.toString(),
                textScaleFactor: 1.3,
              ),
              DropdownButton<int>(
                  items: _durations.map((int value) {
                    return DropdownMenuItem<int>(
                      child: new Text(value.toString()),
                      value: value,
                    );
                  }).toList(),
                  onChanged: (value) {
                    _onDropDownChanged(value);
                  }),
              Container(
                alignment: Alignment.topLeft,
                padding: EdgeInsets.all(10.0),
                child: Text('Patient name: ' + appointment.patientName,
                    textScaleFactor: 1.3),
              ),
              TextField(
                decoration: InputDecoration(
                    labelText: 'Enter patient name',
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0))),
                onChanged: (pName) {
                  setState(() {
                    appointment.patientName = pName;
                  });
                },
              ),
              Container(
                alignment: Alignment.topLeft,
                padding: EdgeInsets.all(10.0),
                child: Text('Reason: ' + appointment.reason.toString(),
                    textScaleFactor: 1.3),
              ),
              TextField(
                decoration: InputDecoration(
                    labelText: 'Enter the appointment reason',
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0))),
                onChanged: (r) {
                  setState(() {
                    appointment.reason = r;
                  });
                },
              ),
              RaisedButton(
                child: Text('Add'),
                color: Theme.of(context).accentColor,
                textColor: Theme.of(context).buttonColor,
                onPressed: () {
                  setState(() {
                    _save();
                  });
                },
              )
            ],
          ),
        ));
  }


  void _onDropDownChanged(int value) {
    setState(() {
      appointment.duration = value;
    });
  }

  void postAppointment() async{
    //check connectivity first
    var connectivityResult = await (Connectivity().checkConnectivity());
    if(connectivityResult == ConnectivityResult.wifi){
      //send data to server and then if successfull save into db
      Map<String,String> headers={
        'Content-type' : 'application/json', 
        'Accept': 'application/json',
      };
      final url = "http://a5531a8e.ngrok.io/api/appointments";
      final response = await http.post(url,headers: headers,body: toJson(appointment));
      if(response.statusCode == 200){
        Appointment appointment = Appointment.fromJson(
          json.decode(response.body)
        );
        await dbHelper.insertAppointment(appointment);
        Navigator.pop(context, appointment);
      }else{
        print("Network Error");
        Navigator.pop(context, appointment);
      }
    }else if(connectivityResult == ConnectivityResult.none){
      //save in db
      appointment.notInSync();
      int id = await dbHelper.insertAppointment(appointment);
      appointment.id = id;
      Navigator.pop(context, appointment);
    }
  }
  void _save() async {
    postAppointment();
  }

    String toJson(Appointment appointment){
      var body={};
      body["dateTime"] = DateFormat("yyyy-MM-dd HH:mm").format(appointment.dateTime);
      body["duration"] = appointment.duration;
      body["reason"] = appointment.reason;
      body["patientName"] = appointment.patientName;
      String str = json.encode(body);
      return str;
  }
}
