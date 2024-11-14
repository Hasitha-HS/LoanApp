package org.example.controllers;

import org.example.dao.LogDAO;
import org.example.entities.Client;
import org.example.exception.DataNotFoundException;
import org.example.services.ClientAuthenticate;
import org.json.JSONObject;

import javax.security.auth.login.CredentialException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/auth")
public class AuthenticationController {

//    @POST
//    @Path("/login")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map<String, String> login(Client client) {
//        Map<String, String> response = null;
//        response = new HashMap<>();
//        try {
//            ClientAuthenticate.getClientAuthenticate().validateUser(client.getUserId(), client.getPassword());
//            response.put("Status", "SUCCESS");
//            response.put("code", "200");
//            response.put("Massage", "User Logged In");
//        } catch (DataNotFoundException | CredentialException | ConstraintViolationException e) {
//            response.put("Status", "FAILED");
//            response.put("Massage", e.getMessage());
//        }
//        return response;
//    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Client client) {
        JSONObject object = new JSONObject();
        try {
            ClientAuthenticate.getClientAuthenticate().validateUser(client.getUserId(), client.getPassword());
            object.put("Status", "SUCCESS");
            object.put("code", "200");
            object.put("Massage", "User Logged In");
        } catch (DataNotFoundException | CredentialException | ConstraintViolationException e) {
            object.put("Status", "FAILED");
            object.put("Massage", e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(object.toString()).build();
    }

    @DELETE
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@QueryParam("id") int id) {
         LogDAO.getLogDAO().deleteLog(id);
        JSONObject object = null;
        object.put("Status", "FAILED");
        object.put("Massage","Logged out");
        return Response.status(Response.Status.OK).build();
    }
}
