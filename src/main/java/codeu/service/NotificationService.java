package codeu.service;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.basic.NotificationStore;

import java.util.UUID;

public class NotificationService {

    private NotificationStore notificationStore;

    public NotificationService(){
        this.notificationStore = NotificationStore.getInstance();
    }

    /**
     * Allows for mocking dependencies of the service
     *
     * @param notificationStore A notificationStore to be used by the NotificationService instance
     */
    public NotificationService(NotificationStore notificationStore){
        this.notificationStore = notificationStore;
    }

    /**
     * Parses a given message for @mention tags, and finds the corresponding user for them, if
     * applicable. If found, generates a mention notification for the mentioned user.
     *
     * @param message The message that possibly contains an @mention
     * @param conversation The conversation the message belongs to
     * @param author The author of the message
     */
    public void generateMentionNotification(Message message, Conversation conversation, User author){
        UUID mentionedUser = notificationStore.getuserMentioned(message.getContent());
        //Add notification if a user is mentioned
        if(mentionedUser != null) {

            String notificationinfo = String.format(
                    "%s mentioned you in <a href=\"/chat/%s\">%s</a>: %s",
                    author.getName(),
                    conversation.getTitle(),
                    conversation.getTitle(),
                    message.getContent()
            );

            Notification notification = new Notification(
                    UUID.randomUUID(),
                    author.getId(),
                    conversation.getId(),
                    notificationinfo,
                    mentionedUser
            );

            notificationStore.addNotification(notification);
        }
    }
}
