package service;


import entity.Client;

import java.util.List;

public interface ClientService {
    public void create(Client client);
    public void update(Client client);
    public void delete(int id);
    public Client get(int id);
    public List<Client> getAll();
    public List<Client> searchByFIO(String lName, String fName, String pName);
}
