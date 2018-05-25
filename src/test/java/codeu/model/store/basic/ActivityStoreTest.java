package codeu.model.store.basic;

import codeu.enumeration.ActivityTypeEnum;
import codeu.model.data.Activity;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

public class ActivityStoreTest {

    private ActivityStore activityStore;
    private PersistentStorageAgent mockPersistentStorageAgent;
    private Activity activity;

    @Before
    public void setup() {
        mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
        activityStore = ActivityStore.getTestInstance(mockPersistentStorageAgent);
        activity = new Activity(UUID.randomUUID(), "User1 joined!", Instant.EPOCH, ActivityTypeEnum.USER_ADDED);
    }

    @Test
    public void testAddActivity() {
        activityStore.addActivity(activity);
        Mockito.verify(mockPersistentStorageAgent).writeThrough(activity);
    }

    @Test
    public void testGetActivitiesBeforeDatetime() throws PersistentDataStoreException {
        Instant instant = Instant.EPOCH;
        int limit = 10;
        activityStore.getActivitiesBeforeDatetime(instant, limit);
        Mockito.verify(mockPersistentStorageAgent).loadActivitiesBeforeDatetime(instant, limit);
    }

}
