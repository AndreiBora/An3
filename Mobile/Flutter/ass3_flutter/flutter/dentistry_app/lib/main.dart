import 'package:dentistry_app/add_appointment.dart';
import 'package:dentistry_app/edit_appointmnet.dart';
import 'package:dentistry_app/model/appointment.dart';
import 'package:dentistry_app/util/dbhelper.dart';
import 'package:dentistry_app/util/utils.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:async';
import 'dart:convert';
import 'package:connectivity/connectivity.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Dentistry',
        theme: ThemeData(primarySwatch: Colors.blue),
        home: Home());
  }
}

class Home extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _Home();
}

class _Home extends State<Home> {
  List<Appointment> appointments;
  DbHelper dbHelper = DbHelper();

  void postAppointments(List<Appointment> syncAppointments) async {
    Map<String, String> headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };
    final url = "http://a5531a8e.ngrok.io/api/sync/appointments";
    final response =
        await http.post(url, headers: headers, body: toJsonList(syncAppointments));
    if (response.statusCode == 200) {
      for(int i=0;i<syncAppointments.length;i++){
        syncAppointments[i].inSync = 1;
        dbHelper.updateAppointment(syncAppointments[i]);
      }
    } else {
      print("Network Error");
    }
  }

  @override
  void initState() {
    super.initState();
    if (appointments == null) {
      appointments = List<Appointment>();
      getData();
    }
    Connectivity().onConnectivityChanged.listen((ConnectivityResult result) {
      if (result == ConnectivityResult.wifi) {
        List<Appointment> notSyncAppointments = List();
        for (int i = 0; i < appointments.length; i++) {
          if (appointments[i].inSync == 0) {
            notSyncAppointments.add(appointments[i]);
          }
        }
        if (notSyncAppointments.isEmpty == false) {
          postAppointments(notSyncAppointments);
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    appointments.forEach((app) => print(app));    
    return Scaffold(
      appBar: AppBar(
          title: Center(
        child: Text('Appointments'),
      )),
      body: Builder(builder: (context) => getListAppointments()),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: () {
          navigateToDetail(Appointment.empty());
        },
      ),
    );

    
  }

  void getData() {
    final dbFuture = dbHelper.initializeDb();
    dbFuture.then((result) {
      final appointmentFuture = dbHelper.getAppointments();
      appointmentFuture.then((result) {
        List<Appointment> app = List<Appointment>();
        for (int i = 0; i < result.length; i++) {
          app.add(Appointment.fromMap(result[i]));
        }
        setState(() {
          appointments = app;
        });
      });
    });
  }

  ListView getListAppointments() {
    return ListView.builder(
      itemCount: appointments.length,
      itemBuilder: (BuildContext context, int position) {
        return Container(
          padding: EdgeInsets.all(5.0),
          child: Card(
              child: Container(
            padding: EdgeInsets.only(top: 20.0, bottom: 20.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Text('Date: ' +
                    DateFormat('yyyy-MM-dd')
                        .format(appointments[position].dateTime)),
                Text('Time: ' +
                    DateFormat('jm').format(appointments[position].dateTime)),
                IconButton(
                  icon: Icon(Icons.delete),
                  onPressed: () async {
                    // checkConnectivity();
                    var connectivityResult =
                        await (Connectivity().checkConnectivity());
                    if (connectivityResult == ConnectivityResult.wifi) {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return AlertDialog(
                            title: new Text("Delete appointment"),
                            content: new Text(
                                "Are you sure you want to delete the appointment?"),
                            actions: <Widget>[
                              new FlatButton(
                                child: new Text("Yes"),
                                onPressed: () {
                                  deleteAppointment(appointments[position].id);
                                  Navigator.of(context).pop();
                                },
                              ),
                              new FlatButton(
                                child: new Text("Cancel"),
                                onPressed: () {
                                  Navigator.of(context).pop();
                                },
                              ),
                            ],
                          );
                        },
                      );
                    } else if (connectivityResult == ConnectivityResult.none) {
                      //show a error message
                      Scaffold.of(context).showSnackBar(new SnackBar(
                        content:
                            new Text("Cannot delete when device is offline"),
                      ));
                    }
                  },
                ),
                IconButton(
                  icon: Icon(Icons.edit),
                  onPressed: () {
                    var app = appointments[position];
                    navigateToEdit(app);
                  },
                )
              ],
            ),
          )),
        );
      },
    );
  }

  void deleteAppointment(int id) async {
    Map<String, String> headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };
    final url = "http://a5531a8e.ngrok.io/api/appointments/${id.toString()}";
    try {
      var response = await http.delete(url, headers: headers);

      if (response.statusCode == 204) {
        await dbHelper.deleteAppointment(id);
        int removePos = 0;
        for (int i = 0; i < appointments.length; i++) {
          if (appointments[i].id == id) {
            removePos = i;
          }
        }
        setState(() {
          appointments.removeAt(removePos);
        });
        //getData();
      } else {
        print("Network Error");
      }
    } catch (e) {
      print(e);
    }
  }

  void navigateToDetail(Appointment app) async {
    Appointment result =
        await Navigator.push(context, MaterialPageRoute(builder: (context) {
      return AddAppointment(app);
    }));
    if (result != null) {
      setState(() {
        appointments.add(result);
      });

      // getData();
    }
  }

  void navigateToEdit(Appointment app) async {
    Appointment appointment =
        await Navigator.push(context, MaterialPageRoute(builder: (context) {
      return EditAppointment(app);
    }));
    if (appointment != null) {
      //getData();
      int pos = -1;
      for (int i = 0; i < appointments.length; i++) {
        if (appointments[i].id == appointment.id) {
          pos = i;
        }
      }
      setState(() {
        if (pos != -1) {
          appointments[pos] = appointment;
        }
      });
    }
  }
}
