package dao;

import entity.Agreement;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AgreementDao {
    public void persist(Agreement agreement);
    public void refresh(Agreement agreement);
    public void delete(int id);
    public Agreement get(int id);
    public List<Agreement> getAll();
    public List<Agreement> getByAgreementNumber(int agreementNumber);
}