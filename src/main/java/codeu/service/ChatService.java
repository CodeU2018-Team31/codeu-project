package codeu.service;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;

import java.time.Instant;
import java.util.UUID;

public class ChatService {

    private ConversationStore conversationStore;
    private MessageStore messageStore;
    private NotificationService notificationService;

    /**
     * Sets the ConversationStore used by this service. This function provides a common setup method
     * for use by the test framework or the service's constructor
     */
    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    /**
     * Sets the MessageStore used by this service. This function provides a common setup method for
     * use by the test framework or the service's constructor.
     */
    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    /**
     * Sets the {@link NotificationService} used by this service. This function provides a common setup method for
     * use by the test framework or the service's constructor.
     */
    void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public ChatService(){
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setNotificationService(new NotificationService());
    }

    /**
     * Sends a message to the indicated conversation, with the provided body and author
     *
     * @param author The author of the message
     * @param messageBody The body of the message to add to the conversation
     * @param conversationName The title of the conversation to send the message to
     * @throws IllegalArgumentException If the provided conversationName could not be found in the store
     */
    public void sendMessageToConversation(User author, String messageBody, String conversationName) throws IllegalArgumentException{
        Conversation conversation = conversationStore.getConversationWithTitle(conversationName);
        if(conversation == null){
            String errorMessage = "ChatService-sendMessageToConversation:" +
                    " Conversation with name "+conversationName+" could not be found";
            System.out.println(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Message message = new Message(UUID.randomUUID(), conversation.getId(), author.getId(), messageBody, Instant.now());
        messageStore.addMessage(message);
        notificationService.generateMentionNotification(message, conversation, author);
    }
}
