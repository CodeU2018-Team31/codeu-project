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

    /**
     * Sets the ConversationStore used by this servlet. This function provides a common setup method
     * for use by the test framework or the service's constructor
     */
    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    /**
     * Sets the MessageStore used by this servlet. This function provides a common setup method for
     * use by the test framework or the servlet's init() function.
     */
    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    public ChatService(){
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
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
    }
}
