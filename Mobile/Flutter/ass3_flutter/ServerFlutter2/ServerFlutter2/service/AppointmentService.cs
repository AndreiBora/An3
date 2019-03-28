using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ServerFlutter2.service
{
    public class AppointmentService
    {
        public IEnumerable<Appointment> GetAppointments()
        {
            using (var ctx = new appointmentdbfEntities())
            {
                return ctx.Appointments.ToList();
            }
        }

        public Appointment GetAppointmentById(int id)
        {
            using (var ctx = new appointmentdbfEntities())
            {
                Appointment app = ctx.Appointments.Where(x => x.id == id).FirstOrDefault();
                return app;
            }
        }


        public Appointment AddAppointment(Appointment appointment)
        {
            int id = appointment.id;
            Appointment app = null;

            using (var ctx = new appointmentdbfEntities())
            {
                app = ctx.Appointments.Add(appointment);
                ctx.SaveChanges();
            }
        
            return app;
        }

        internal void addAppointments(IEnumerable<Appointment> appointments)
        {
            using(var ctx = new appointmentdbfEntities())
            {
                ctx.Appointments.AddRange(appointments);
                ctx.SaveChanges();
            }
        }

        public void RemoveAppointment(int id)
        {
            using (var ctx = new appointmentdbfEntities())
            {
                Appointment app = ctx.Appointments.Where(x => x.id == id).First();
                ctx.Appointments.Remove(app);
                ctx.SaveChanges();
            }
        }

        public Appointment UpdateAppointment(int id, Appointment appointment)
        {

            using (var ctx = new appointmentdbfEntities())
            {
                var app = ctx.Appointments.Where(x => x.id == id).First();
                app.dateTime = appointment.dateTime;
                app.patientName = appointment.patientName;
                app.reason = appointment.reason;
                app.duration = appointment.duration;
                ctx.SaveChanges();
                return app;
            }
        }
    }
}