package serviceimpl;


import dao.ClientDao;
import entity.Client;
import service.ClientService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class ClientServiceImpl implements ClientService {

    @EJB
    private ClientDao clientDao;

    @Override
    public void create(Client client) {
        clientDao.persist(client);
    }

    @Override
    public void update(Client client) {
        clientDao.refresh(client);
    }

    @Override
    public void delete(int id) {
        clientDao.delete(id);
    }

    @Override
    public Client get(int id) {
        return clientDao.get(id);
    }

    @Override
    public List<Client> getAll() {
        return clientDao.getAll();
    }

    @Override
    public List<Client> searchByFIO(String lName, String fName, String pName) {
        return clientDao.searchByFIO(lName, fName, pName);
    }
}
