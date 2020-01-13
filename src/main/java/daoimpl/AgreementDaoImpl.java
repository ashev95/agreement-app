package daoimpl;

import dao.AgreementDao;
import entity.Agreement;
import storagemanager.StorageManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AgreementDaoImpl implements AgreementDao {

    @EJB
    private StorageManager storageManager;

    @Override
    public void persist(Agreement agreement) {
        this.storageManager.getEntityManager().persist(agreement);
    }

    @Override
    public void refresh(Agreement agreement) {
        Agreement agreement1 = storageManager.getEntityManager().merge(agreement);
        storageManager.getEntityManager().flush();
        storageManager.getEntityManager().refresh(agreement1);
    }

    @Override
    public void delete(int id) {
        storageManager.getEntityManager().remove(get(id));
    }

    @Override
    public Agreement get(int id) {
        return (Agreement) storageManager.getEntityManager().find(Agreement.class, id);
    }

    @Override
    public List<Agreement> getAll() {
        List<Agreement> records = (List<Agreement>) storageManager.getEntityManager().createQuery("from Agreement").getResultList();
        return records;
    }

    @Override
    public List<Agreement> getByAgreementNumber(int agreementNumber) {
        String queryString = "from Agreement a where a.agreementNumber = :agreementNumber";
        Query query = storageManager.getEntityManager().createQuery(queryString);
        query.setParameter("agreementNumber", agreementNumber);
        List<Agreement> records = (List<Agreement>) query.getResultList();
        return records;
    }

}
