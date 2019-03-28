import 'package:dentistry_app/model/appointment.dart';
import 'package:dentistry_app/util/dbhelper.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:async';
import 'dart:convert';
import 'package:connectivity/connectivity.dart';

class EditAppointment extends StatefulWidget {
  final Appointment appointment;

  EditAppointment(this.appointment);

  @override
  State<StatefulWidget> createState() => _EditAppointment(this.appointment);
}

class _EditAppointment extends State<EditAppointment> {
  Appointment appointment;
  DbHelper dbHelper = DbHelper();
  List<int> _durations = [30, 45, 60, 75, 90, 120];

  _EditAppointment(this.appointment);

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
    return Scaffold(
        appBar: AppBar(title: Text('Edit appointment')),
        body: 
        Builder(builder: (context) => 
        Container(
          padding: EdgeInsets.all(15.0),
          child: ListView(
            children: <Widget>[
              Container(
                padding: EdgeInsets.all(_formPadding),
                alignment: Alignment.topLeft,
                child: Text(
                    'Date: ' +
                        DateFormat('yyyy-MM-dd').format(appointment.dateTime),
                    textScaleFactor: 1.3),
              ),
              IconButton(icon: Icon(Icons.date_range), onPressed: _selectDate),
              Container(
                alignment: Alignment.topLeft,
                child: Text(
                    'Time: ' + DateFormat('jm').format(appointment.dateTime),
                    textScaleFactor: 1.3),
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
                child: Text('Update'),
                color: Theme.of(context).accentColor,
                textColor: Theme.of(context).buttonColor,
                onPressed: (){
             
                                      _update(context);
                        
                },
              )
            ],
          ),
        )));
  }

  void moveToLastScreen() {
    Navigator.pop(context, true);
  }

  void _onDropDownChanged(int value) {
    setState(() {
      appointment.duration = value;
    });
  }

  void putAppointment() async{
    Map<String,String> headers={
        'Content-type' : 'application/json', 
        'Accept': 'application/json',
    };
    final url = "http://a5531a8e.ngrok.io/api/appointments/${appointment.id}";
    try{
      final response = await http.put(url,headers: headers,body: toJson(appointment));
      if(response.statusCode == 200){
        Appointment newAppointment = Appointment.fromJson(
        json.decode(response.body)
        );
        await dbHelper.updateAppointment(newAppointment);
        Navigator.pop(context, newAppointment);
      }else{
        print("Network Error");
        Navigator.pop(context, appointment);
      }
    }catch(e){
      print(e.toString());
    } 
  }

  void _update(BuildContext context) async {
    var connectivityResult = await (Connectivity().checkConnectivity());
    if(connectivityResult == ConnectivityResult.wifi){
      //Updates are allowed only when there is internet
      putAppointment();
    }else if(connectivityResult == ConnectivityResult.none){
      //show a error message
      Scaffold.of(context).showSnackBar(new SnackBar(
        content: new Text("Cannot edit when device is offline"),
      ));
    }
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
