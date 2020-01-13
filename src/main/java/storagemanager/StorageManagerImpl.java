package storagemanager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class StorageManagerImpl implements StorageManager {

    @PersistenceContext(unitName = "PostgreDS")
    private EntityManager entityManager;

    public EntityManager getEntityManager(){
        return entityManager;
    }

}
