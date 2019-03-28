import 'package:sqflite/sqflite.dart';
import 'dart:async';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:dentistry_app/model/appointment.dart';

class DbHelper {
  static final DbHelper _dbHelper = DbHelper._internal();

  String tblAppointment = "Appointments";
  String appId = "id";
  String dateTime = "startDate";
  String duration = "duration";
  String patientName = "patientName";
  String reason = "reason";
  String inSync = "inSync";

  DbHelper._internal();
  factory DbHelper() {
    return _dbHelper;
  }

  static Database _db;

  Future<Database> get db async{
    if(_db == null){
      _db = await initializeDb();
    }
    return _db;
  }

  Future<Database> initializeDb() async{
    Directory dir = await getApplicationDocumentsDirectory();
    String path = dir.path + "dentistry.db";
    var path2 = await getDatabasesPath();
    var dbDentistry = await openDatabase(path,version: 1,onCreate:_createDb);
    return dbDentistry;
  }
  void _createDb(Database db,int newVersion) async{
    await db.execute(
      "CREATE TABLE $tblAppointment($appId INTEGER PRIMARY KEY, $dateTime TEXT, " +
        "$duration INTEGER, $patientName TEXT, $reason TEXT,$inSync TEXT)"
    );
  }

  Future<int> insertAppointment(Appointment app) async{
    Database db = await this.db;
    var result = await db.insert(tblAppointment, app.toMap());
    return result;
  }

  Future<List> getAppointments() async{
    Database db = await this.db;
    return await db.rawQuery("SELECT $appId, $dateTime, $duration, $patientName, $reason, $inSync  FROM $tblAppointment");
  }

  Future<int> getNrAppointments() async{
    Database db = await this.db;
    return Sqflite.firstIntValue(
     await db.rawQuery("SELECT count(*) from $tblAppointment")      
    );
  }

  Future<int> updateAppointment(Appointment app) async{
    var db = await this.db;
    return await db.update(tblAppointment, app.toMap(),
    where: "$appId = ?",whereArgs: [app.id]
    );
  }

  Future<int> deleteAppointment(int id) async{
    var db = await this.db;
    return await db.rawDelete("DELETE FROM $tblAppointment WHERE $appId = $id");
  }

  clearDb() async{
    var db = await this.db;
    var result = await db.rawDelete("DELETE FROM $tblAppointment");
    return result;
  }
}
