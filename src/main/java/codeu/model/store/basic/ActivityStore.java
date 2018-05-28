package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.time.Instant;
import java.util.List;

public class ActivityStore {

    private static ActivityStore instance; //Singleton
    private PersistentStorageAgent persistentStorageAgent;

    /**
     * Retrieves the ActivityStore singleton; for testing, use getTestInstance instead
     */
    public static ActivityStore getInstance() {
        if (instance == null) {
            instance = new ActivityStore(PersistentStorageAgent.getInstance());
        }
        return instance;
    }

    public static ActivityStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
        return new ActivityStore(persistentStorageAgent);
    }

    /**
     * Private constructor for singleton instance
     */
    private ActivityStore(PersistentStorageAgent persistentStorageAgent) {
        this.persistentStorageAgent = persistentStorageAgent;
    }

    public void addActivity(Activity activity) {
        this.persistentStorageAgent.writeThrough(activity);
    }

    /**
     * Loads {@link Activity} objects from the Datastore service that were created before the provided datetime
     * and up to the provided limit of number of entries.
     *
     * @param startDatetime The retrieved activities would have been created exclusively before the startDatetime
     * @param limit         The max. number of entries to retrieve
     * @return A list of activities that met the criteria
     * @throws PersistentDataStoreException Loading activities failed
     */
    public List<Activity> getActivitiesBeforeDatetime(Instant startDatetime, int limit) throws PersistentDataStoreException {
        return this.persistentStorageAgent.loadActivitiesBeforeDatetime(startDatetime, limit);
    }
}
