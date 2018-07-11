package codeu.model.store.basic;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationStoreTest {


    private PersistentStorageAgent mockPersistentStorageAgent;
    private UserStore userStore;
    private User user;
    private NotificationStore notificationStore;
    private Notification notification;

    @Before
    public void setup() {
        mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
        userStore = UserStore.getTestInstance(mockPersistentStorageAgent);
        notificationStore = NotificationStore.getTestInstance(mockPersistentStorageAgent);
        UUID UserId = UUID.randomUUID();
        user = new User(UserId,
                        "user1",
                        "$2a$10$/zf4WlT2Z6tB5sULB9Wec.QQdawmF0f1SbqBw5EeJg5uoVpKFFXAa",
                        Instant.ofEpochMilli(1000),
                        false);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userStore.setUsers(userList);
        notification = new Notification(UUID.randomUUID(),
                                        UUID.randomUUID(),
                                        UUID.randomUUID(),
                                        "testuser mentioned you in testconversation : test @user1 test",
                                        user.getId());
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        notificationStore.setNotifications(notificationList);

    }


    @Test
    public void testgetuserMentioned_found() {
//        UUID resultID = notificationStore.getuserMentioned("test @user1 test");
//        Assert.assertEquals(user.getId(), resultID);

    }

    @Test
    public void testgetuserMentioned_notfound() {
        UUID resultNotification = notificationStore.getuserMentioned("unfound user");

        Assert.assertNull(resultNotification);
    }

    @Test
    public void testgetUserofNotifications_True() {
        boolean getUserofNotifications = notificationStore.getUserofNotifications(notification.getMentionedId());
        Assert.assertTrue(getUserofNotifications);
    }

    @Test
    public void testgetUserofNotifications_False() {
        boolean getUserofNotifications = notificationStore.getUserofNotifications(null);
        Assert.assertFalse(getUserofNotifications);
    }

    @Test
    public void testgetNotificationsofUser_found() {
        String resultNotification = notificationStore.getNotificationsofUser(user.getId());
        Assert.assertEquals(notification.getContent(), resultNotification);
    }

    @Test
    public void testgetNotificationsofUser_notfound() {
       String resultNotification = notificationStore.getNotificationsofUser(null);

       Assert.assertNull(resultNotification);
    }

    @Test
    public void testAddNotification() {
        notificationStore.addNotification(notification);
        Mockito.verify(mockPersistentStorageAgent).writeThrough(notification);
    }


}
