package storagemanager;

import javax.ejb.Local;
import javax.persistence.EntityManager;

@Local
public interface StorageManager {
    public EntityManager getEntityManager();
}
