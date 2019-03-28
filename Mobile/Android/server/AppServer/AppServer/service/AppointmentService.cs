using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace AppServer.service
{
    public class AppointmentService
    {

        public AppointmentService()
        {
        }

        public IEnumerable<Appointment> GetAppointments()
        {
            using (var ctx = new appointmentdbEntities())
            {
                return ctx.Appointments.ToList();
            }
        }

        public Appointment GetAppointmentById(int id)
        {
            using (var ctx = new appointmentdbEntities())
            {
                Appointment app = ctx.Appointments.Where(x => x.id == id).FirstOrDefault();
                return app;
            }
        }


        public Appointment AddAppointment(Appointment appointment)
        {
            

            using(var ctx = new appointmentdbEntities())
            {
                var app = ctx.Appointments.Add(appointment);
                ctx.SaveChanges();
                return app;
            }
        }

        public void RemoveAppointment(int id)
        {
            using (var ctx = new appointmentdbEntities()) {
                Appointment app = ctx.Appointments.Where(x => x.id == id).First();
                ctx.Appointments.Remove(app);
                ctx.SaveChanges();
            }
        }

        public Appointment UpdateAppointment(int id,Appointment appointment)
        {

            using (var ctx = new appointmentdbEntities())
            {
                var app = ctx.Appointments.Where(x => x.id == id).First();
                app.startDate = appointment.startDate;
                app.endDate = appointment.endDate;
                app.patientName = appointment.patientName;
                app.reason = appointment.reason;
                ctx.SaveChanges();
                return app;
            }
        }

    }
}