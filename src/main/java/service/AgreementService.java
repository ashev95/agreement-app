package service;

import entity.Agreement;

import java.util.List;

public interface AgreementService {
    public void create(Agreement agreement);
    public void update(Agreement agreement);
    public void delete(int id);
    public Agreement get(int id);
    public List<Agreement> getAll();
    public List<Agreement> getByAgreementNumber(int agreementNumber);
}
