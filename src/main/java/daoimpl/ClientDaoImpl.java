package daoimpl;

import dao.ClientDao;
import entity.Client;
import storagemanager.StorageManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ClientDaoImpl implements ClientDao {

    @EJB
    private StorageManager storageManager;

    @Override
    public void persist(Client client) {
        this.storageManager.getEntityManager().persist(client);
    }

    @Override
    public void refresh(Client client) {
        Client client1 = storageManager.getEntityManager().merge(client);
        storageManager.getEntityManager().flush();
        storageManager.getEntityManager().refresh(client1);
    }

    @Override
    public void delete(int id) {
        storageManager.getEntityManager().remove(get(id));
    }

    @Override
    public Client get(int id) {
        return (Client) storageManager.getEntityManager().find(Client.class, id);
    }

    @Override
    public List<Client> getAll() {
        List<Client> records = (List<Client>) storageManager.getEntityManager().createQuery("from Client").getResultList();
        return records;
    }

    @Override
    public List<Client> searchByFIO(String lName, String fName, String pName) {
        String queryString = "from Client c" +
                " where c.id  in (select c1.id from Client c1 where c1.lName is not null and c1.lName <> '' and lower(c1.lName) like lower(:lName))" +
                " or c.id  in (select c2.id from Client c2 where c2.fName is not null and c2.fName <> '' and lower(c2.fName) like lower(:fName))" +
                " or c.id  in (select c3.id from Client c3 where c3.pName is not null and c3.pName <> '' and lower(c3.pName) like lower(:pName))";
        Query query = storageManager.getEntityManager().createQuery(queryString);
        query.setParameter("lName", (lName != null && !lName.isEmpty() ? lName + "%" : lName));
        query.setParameter("fName", (fName != null && !fName.isEmpty() ? fName + "%" : fName));
        query.setParameter("pName", (pName != null && !pName.isEmpty() ? pName + "%" : pName));
        List<Client> records = (List<Client>) query.getResultList();
        return records;
    }

}
