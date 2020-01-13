package dao;

import entity.Client;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ClientDao {
    public void persist(Client client);
    public void refresh(Client client);
    public void delete(int id);
    public Client get(int id);
    public List<Client> getAll();
    public List<Client> searchByFIO(String lName, String fName, String pName);
}
