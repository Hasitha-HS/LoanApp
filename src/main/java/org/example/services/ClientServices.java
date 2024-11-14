package org.example.services;

import org.example.dao.ClientDAO;
import org.example.dao.LogDAO;
import org.example.entities.Client;
import org.example.exception.DataNotFoundException;
import org.example.exception.RequestNotAuthroizedException;

import java.util.List;

public class ClientServices {


    public void updateClient(Client client1) {

        int id = client1.getUserId();
        System.out.println(id);
        Client client0 = ClientDAO.getClientDAO().getClient(id);
        try {
            if (null == ClientDAO.getClientDAO().getClient(id)) {
                throw new DataNotFoundException("User not found!");
            } else {

                if (client1.getFirstname() != null && !client1.getFirstname().isEmpty()) {
                    client0.setFirstname(client1.getFirstname());
                }
                if (client1.getLastname() != null && !client1.getLastname().isEmpty()) {
                    client0.setLastname(client1.getLastname());
                }
                if (client1.getOccupation() != null && !client1.getOccupation().isEmpty()) {
                    client0.setOccupation(client1.getOccupation());
                }
                if (client1.getAddress() != null && !client1.getAddress().isEmpty()) {
                    client0.setAddress(client1.getAddress());
                }
                if (client1.getEmail() != null && !client1.getEmail().isEmpty()) {
                    client0.setEmail(client1.getEmail());
                }
                if (client1.getPassword() != null && !client1.getPassword().isEmpty()) {
                    client0.setPassword(client1.getPassword());
                }
                if (client1.getNic() != null && !client1.getNic().isEmpty()) {
                    client0.setNic(client1.getNic());
                }

                ClientDAO.getClientDAO().updateClient(client0);
                ClientDAO.getClientDAO().clearClientDAO();
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveClient(Client client){
        ClientDAO.getClientDAO().saveClient(client);
    }

    public Client getClientById(int clientId) {
        return ClientDAO.getClientDAO().getClient(clientId);
    }

    public void deleteClientById(int clientId) {
        ClientDAO.getClientDAO().deleteClient(clientId);
    }

    public List<Client> getAllClients(int userId) {
        return ClientDAO.getClientDAO().getClients(userId);
    }
}


