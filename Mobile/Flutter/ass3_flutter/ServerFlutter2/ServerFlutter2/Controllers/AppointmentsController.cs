using ServerFlutter2.service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ServerFlutter2.Controllers
{
    public class AppointmentsController : ApiController
    {
        private AppointmentService appointmentService;

        public AppointmentsController()
        {
            appointmentService = new AppointmentService();
        }

        // GET api/values
        public IEnumerable<Appointment> Get()
        {
            return appointmentService.GetAppointments();
        }

        [Route("api/sync/appointments")]
        [HttpPost]
        public HttpResponseMessage PostAppointments([FromBody]IEnumerable<Appointment> appointments)
        {
            try
            {
                appointmentService.addAppointments(appointments);
              
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);

            }
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        // GET api/values/5
        public HttpResponseMessage Get(int id)
        {
            Appointment app;
            try
            {
                app = appointmentService.GetAppointmentById(id);
                if (app == null)
                {
                    return Request.CreateResponse(HttpStatusCode.NotFound);
                }
                return Request.CreateResponse(HttpStatusCode.OK, app);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
        }

        // POST api/values
        public HttpResponseMessage Post([FromBody]Appointment appointment)
        {
            try
            {
                var app = appointmentService.AddAppointment(appointment);
                return Request.CreateResponse(HttpStatusCode.OK, app);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
        }

        // PUT api/values
        public HttpResponseMessage Put(int id, [FromBody]Appointment appointment)
        {
            try
            {
                var newApp = appointmentService.UpdateAppointment(id, appointment);
                return Request.CreateResponse(HttpStatusCode.OK, newApp);
            }
            catch (InvalidOperationException e)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }

        }

        // DELETE api/values/5
        public HttpResponseMessage Delete(int id)
        {
            try
            {
                appointmentService.RemoveAppointment(id);
                return Request.CreateResponse(HttpStatusCode.NoContent);

            }
            catch (InvalidOperationException e)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
        }
    }

}
