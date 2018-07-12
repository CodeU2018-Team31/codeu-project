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

package codeu.model.store.basic;
import codeu.model.data.User;
import codeu.model.data.Notification;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class NotificationStore {

    /** Singleton instance of NotificationStore. */
    private static NotificationStore instance;

    /**
     * Returns the singleton instance of NotificationStore that should be shared between all servlet
     * classes. Do not call this function from a test; use getTestInstance() instead.
     */
    public static NotificationStore getInstance() {
        if (instance == null) {
            instance = new NotificationStore(PersistentStorageAgent.getInstance(), UserStore.getInstance());

        }
        return instance;
    }

    /**
     * Instance getter function used for testing. Supply a mock for PersistentStorageAgent and UserStore.
     *
     * @param persistentStorageAgent a mock used for testing
     */
    public static NotificationStore getTestInstance(PersistentStorageAgent persistentStorageAgent, UserStore userStore) {
        return new NotificationStore(persistentStorageAgent, userStore);
    }

    /**
     * The PersistentStorageAgent responsible for loading Notifications from and saving Notifications to
     * Datastore.
     */
    private PersistentStorageAgent persistentStorageAgent;


    private UserStore userStore;


    /** The in-memory list of Notifications. */
    private List<Notification> notifications;

    /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
    private NotificationStore(PersistentStorageAgent persistentStorageAgent, UserStore userStore) {
        this.persistentStorageAgent = persistentStorageAgent;
        this.userStore = userStore;
        notifications = new ArrayList<>();
    }

    public UUID getuserMentioned(String content){
        Pattern pattern = Pattern.compile("@([^\\s]+)");
        Matcher matcher = pattern.matcher(content);
        User matchedUser = null;
        if(matcher.find()) {
            String group = matcher.group(1);
            matchedUser = userStore.getUser(group);
        }
        if(matchedUser != null){
            return matchedUser.getId();
        }
        return null;
    }

    public List<Notification> loadNotifications(){
        return notifications;
    }

    /** Add a new notification to the current set of notifications known to the application. */
    public void addNotification(Notification notification) {
        notifications.add(notification);
        persistentStorageAgent.writeThrough(notification);
    }


    /** Sets the List of Notifications stored by this NotificationStore. */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
