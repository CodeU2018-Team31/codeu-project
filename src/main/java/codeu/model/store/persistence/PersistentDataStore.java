// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.store.persistence;

import codeu.enumeration.ActivityTypeEnum;
import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Notification;
import codeu.model.store.persistence.PersistentDataStoreException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String passwordHash = (String) entity.getProperty("password_hash");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        Boolean isAdmin = (Boolean) entity.getProperty("isAdmin");
        User user = new User(uuid, userName, passwordHash, creationTime, isAdmin);
        users.add(user);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
          boolean isPrivate = (boolean) entity.getProperty("is_private");
        Conversation conversation = new Conversation(uuid, ownerUuid, title, creationTime);
          conversation.setPrivate(isPrivate);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        Message message = new Message(uuid, conversationUuid, authorUuid, content, creationTime);
        messages.add(message);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }
  /**
   * Loads all Notification objects from the Datastore service and returns them in a List, sorted in
   * ascending order by userID.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Notification> loadNotifications() throws PersistentDataStoreException {

    List<Notification> notifications = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-notifications").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("convo_uuid"));
        String content = (String) entity.getProperty("content");
        UUID mentionedUuid = UUID.fromString((String) entity.getProperty("mentioned_uuid"));
        Notification notification = new Notification(uuid,authorUuid, conversationUuid, content, mentionedUuid);
        notifications.add(notification);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return notifications;
  }
  /**
   * Loads {@link Activity} objects from the Datastore service that were created before the provided datetime,
   * up to the provided limit of number of entries and sorted in descending order by creation time.
   *
   * @param startDatetime The retrieved activities would have been created exclusively before the startDatetime
   * @param limit         The max. number of entries to retrieve
   * @return A list of activities that met the criteria and sorted by creation time
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *                                      Datastore service
   */
  public List<Activity> loadActivitiesBeforeDatetime(Instant startDatetime, int limit) throws PersistentDataStoreException {
    Query query = new Query("chat-activities")
            .setFilter(new FilterPredicate("datetime", FilterOperator.LESS_THAN, startDatetime.toString()))
            .addSort("datetime", SortDirection.DESCENDING);

    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(limit));
    List<Activity> activities = new ArrayList<>();

    for (Entity entity : results) {
      try {
        UUID id = UUID.fromString((String) entity.getProperty("id"));
        String description = (String) entity.getProperty("description");
        Instant datetime = Instant.parse((String) entity.getProperty("datetime"));
        int typeIndex = Math.toIntExact((long) entity.getProperty("type")); //convert type index from long to int
        ActivityTypeEnum type = ActivityTypeEnum.values()[typeIndex]; //convert type index into the enum value

        Activity activity = new Activity(id, description, datetime, type);
        activities.add(activity);
      } catch (Exception e) {
        throw new PersistentDataStoreException(e);
      }
    }

    return activities;
  }

  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password_hash", user.getPasswordHash());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    userEntity.setProperty("isAdmin", user.getAdmin());
    datastore.put(userEntity);
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages", message.getId().toString());
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toString());
    datastore.put(messageEntity);
  }

  /** Write a Notification object to the Datastore service. */
  public void writeThrough(Notification notification) {
    Entity notificationEntity = new Entity("chat-notifications", notification.getId().toString());
    notificationEntity.setProperty("uuid", notification.getId().toString());
    notificationEntity.setProperty("author_uuid", notification.getAuthorId().toString());
    notificationEntity.setProperty("convo_uuid", notification.getConversationId().toString());
    notificationEntity.setProperty("content", notification.getContent());
    notificationEntity.setProperty("mentioned_uuid", notification.getMentionedId().toString());
    datastore.put(notificationEntity);
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations", conversation.getId().toString());
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toString());
      conversationEntity.setProperty("is_private", conversation.isPrivate());
    datastore.put(conversationEntity);
  }

  /**
   * Write an {@link Activity} object to the Datastore service.
   */
  public void writeThrough(Activity activity) {
    Entity activityEntity = new Entity("chat-activities", activity.getId().toString());
    activityEntity.setProperty("id", activity.getId().toString());
    activityEntity.setProperty("description", activity.getDescription());
    activityEntity.setProperty("datetime", activity.getDatetime().toString());
    activityEntity.setProperty("type", activity.getType().ordinal()); //store the index of the enum value
    datastore.put(activityEntity);
  }
}

