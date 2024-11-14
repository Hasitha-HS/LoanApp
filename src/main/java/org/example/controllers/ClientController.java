package org.example.controllers;

import io.swagger.annotations.ApiOperation;
import org.example.dao.ClientDAO;
import org.example.dto.ClientDTO;
import org.example.entities.Client;
import org.example.services.ClientServices;
import org.example.services.SavingsServices;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Api(value = "clients")
@Path("/api/clients")
public class ClientController {


    SavingsServices savingsServices = new SavingsServices();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createClient(Client client) {
         JSONObject object = new JSONObject();
        try {
            ClientDAO.getClientDAO().saveClient(client);
            object.put("Status", "SUCCESS");
            object.put("code", "200");
            object.put("Massage", "User registered");
        } catch (Exception e) {
            object.put("Status", "FAILED");
            object.put("code", "500");
            object.put("Massage", e.getLocalizedMessage());
        }
            return Response.status(Response.Status.OK).entity(object.toString()).build();

    }

    @PUT
    @Path("/update-client")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(Client client) {
        ClientServices clientServices = new ClientServices();
        JSONObject object = null;
        clientServices.updateClient(client);
        object.put("Status", "SUCCESS");
        object.put("code", "200");
        object.put("Massage", "Client Updated");
        return Response.status(Response.Status.OK).entity(object).build();
    }

    @POST
    @Path("/new-savings-account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSavingsAccount(Client client) {
        ClientServices clientServices = new ClientServices();
        JSONObject object = null;
        savingsServices.getNewAccNumber(client.getUserId());
        object.put("Status", "SUCCESS");
        object.put("code", "200");
        object.put("Massage", "User registered");
        return Response.status(Response.Status.OK).entity(object).build();
    }

    @GET
    @Path("/view/client")
    @Produces(MediaType.APPLICATION_JSON)
    public ClientDTO getClient(@QueryParam("id") int id) {
        ClientServices clientServices = new ClientServices();
        ClientDTO clientDTO = new ClientDTO(clientServices.getClientById(id));
        return clientDTO;
    }


    @GET
    @Path("/view/all-clients")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Client> getAllClients(@QueryParam("id") int id) {
        ClientServices clientServices = new ClientServices();
        return clientServices.getAllClients(id);
    }
}
