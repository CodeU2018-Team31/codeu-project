package codeu.model.store.persistence;

import codeu.enumeration.ActivityTypeEnum;
import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Notification;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String nameOne = "admin";
    String passwordHashOne = "eastcode";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Boolean isAdminOne = true;
    User inputUserOne = new User(idOne, nameOne, passwordHashOne, creationOne, isAdminOne);

    UUID idTwo = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String nameTwo = "test_username_two";
    String passwordHashTwo = "$2a$10$ttaMOMMGLKxBBuTN06VPvu.jVKif.IczxZcXfLcqEcFi1lq.sLb6i";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Boolean isAdminTwo = false;
    User inputUserTwo = new User(idTwo, nameTwo, passwordHashTwo, creationTwo, isAdminTwo);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordHashOne, resultUserOne.getPasswordHash());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());
    Assert.assertEquals(isAdminOne, resultUserOne.getAdmin());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordHashTwo, resultUserTwo.getPasswordHash());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
    Assert.assertEquals(isAdminTwo, resultUserTwo.getAdmin());
  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, titleOne, creationOne);

    UUID idTwo = UUID.fromString("10000002-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, titleTwo, creationTwo);
      inputConversationTwo.setPrivate(true);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());
      Assert.assertFalse(resultConversationOne.isPrivate());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());
      Assert.assertTrue(resultConversationTwo.isPrivate());
  }

  @Test
  public void testSaveAndLoadMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID conversationOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, contentOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID conversationTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content one";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, contentTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();

    // confirm that what we saved matches what we loaded
    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());
  }

    @Test
    public void testSaveAndLoadActivities() throws PersistentDataStoreException {
        UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
        String descriptionOne = "User1 has joined!";
        Instant datetimeOne = Instant.ofEpochMilli(1);
        ActivityTypeEnum typeOne = ActivityTypeEnum.USER_ADDED;
        Activity activityOne =
                new Activity(idOne, descriptionOne, datetimeOne, typeOne);

        UUID idTwo = UUID.fromString("10000000-2222-3333-4444-555555555556");
        String descriptionTwo = "User2 has joined!";
        Instant datetimeTwo = Instant.ofEpochMilli(2);
        ActivityTypeEnum typeTwo = ActivityTypeEnum.USER_ADDED;
        Activity activityTwo =
                new Activity(idTwo, descriptionTwo, datetimeTwo, typeTwo);

        // save
        persistentDataStore.writeThrough(activityOne);
        persistentDataStore.writeThrough(activityTwo);

        // load since ofEpochMilli(3) excluded
        List<Activity> expectTwoResults = persistentDataStore.loadActivitiesBeforeDatetime(Instant.ofEpochMilli(3), 10);
        // load since ofEpochMilli(2) excluded
        List<Activity> expectOneResult = persistentDataStore.loadActivitiesBeforeDatetime(Instant.ofEpochMilli(2), 10);
        // load since ofEpochMilli(3) excluded, and limit to 0 results
        List<Activity> expectNoResultsByLimit = persistentDataStore.loadActivitiesBeforeDatetime(Instant.ofEpochMilli(3), 0);
        // load since ofEpochMilli(1) excluded
        List<Activity> expectNoResultsByInstant = persistentDataStore.loadActivitiesBeforeDatetime(Instant.ofEpochMilli(1), 10);

        Assert.assertEquals(2, expectTwoResults.size());
        Assert.assertEquals(1, expectOneResult.size());
        Assert.assertEquals(0, expectNoResultsByLimit.size());
        Assert.assertEquals(0, expectNoResultsByInstant.size());

        //Ensure correct sorting, with newer activity first in the list
        Assert.assertEquals(activityTwo.getId().toString(), expectTwoResults.get(0).getId().toString());
        Assert.assertEquals(activityOne.getId().toString(), expectTwoResults.get(1).getId().toString());

        //Ensure correct setting of properties
        Activity retrievedActivityOne = expectTwoResults.get(1);
        Assert.assertEquals(activityOne.getDescription(), retrievedActivityOne.getDescription());
        Assert.assertEquals(activityOne.getDatetime(), retrievedActivityOne.getDatetime());
        Assert.assertEquals(activityOne.getType(), retrievedActivityOne.getType());
    }

    @Test
    public void testSaveAndLoadNotifications() throws PersistentDataStoreException {
        UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
        UUID authorOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
        UUID conversationOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
        String contentOne = "testuser mentioned you in testconversation: test @user1";
        UUID mentionedOne = UUID.fromString("10000003-2222-3333-4444-555555555555");
        Notification notificationOne =
                new Notification(idOne, authorOne, conversationOne, contentOne, mentionedOne);

        UUID idTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
        UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
        UUID conversationTwo = UUID.fromString("10000006-2222-3333-4444-555555555555");
        String contentTwo = "testuser mentioned you in testconversation: test @user2";
        UUID mentionedTwo = UUID.fromString("10000007-2222-3333-4444-555555555555");
        Notification notificationTwo =
                new Notification(idTwo, authorTwo, conversationTwo, contentTwo, mentionedTwo);

        // save
        persistentDataStore.writeThrough(notificationOne);
        persistentDataStore.writeThrough(notificationTwo);

        // load
        List<Notification> resultNotifications = persistentDataStore.loadNotifications();

        // confirm that what we saved matches what we loaded
        Notification resultNotificationsOne = resultNotifications.get(0);
        Assert.assertEquals(idOne, resultNotificationsOne.getId());
        Assert.assertEquals(authorOne, resultNotificationsOne.getAuthorId());
        Assert.assertEquals(conversationOne, resultNotificationsOne.getConversationId());
        Assert.assertEquals(contentOne, resultNotificationsOne.getContent());
        Assert.assertEquals(mentionedOne, resultNotificationsOne.getMentionedId());

        Notification resultNotificationsTwo = resultNotifications.get(1);
        Assert.assertEquals(idTwo, resultNotificationsTwo.getId());
        Assert.assertEquals(authorTwo, resultNotificationsTwo.getAuthorId());
        Assert.assertEquals(conversationTwo, resultNotificationsTwo.getConversationId());
        Assert.assertEquals(contentTwo, resultNotificationsTwo.getContent());
        Assert.assertEquals(mentionedTwo, resultNotificationsTwo.getMentionedId());
    }
}
