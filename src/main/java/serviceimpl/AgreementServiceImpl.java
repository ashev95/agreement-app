package serviceimpl;


import dao.AgreementDao;
import entity.Agreement;
import service.AgreementService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AgreementServiceImpl implements AgreementService {

    @EJB
    private AgreementDao agreementDao;

    @Override
    public void create(Agreement agreement) {
        agreementDao.persist(agreement);
    }

    @Override
    public void update(Agreement agreement) {
        agreementDao.refresh(agreement);
    }

    @Override
    public void delete(int id) {
        agreementDao.delete(id);
    }

    @Override
    public Agreement get(int id) {
        return agreementDao.get(id);
    }

    @Override
    public List<Agreement> getAll() {
        return agreementDao.getAll();
    }

    @Override
    public List<Agreement> getByAgreementNumber(int agrementNumber) {
        return agreementDao.getByAgreementNumber(agrementNumber);
    }
}
