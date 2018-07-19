package codeu.service;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.UUID;

public class ChatServiceTest {

    private ConversationStore conversationStore;
    private MessageStore messageStore;
    private ChatService chatService;
    private NotificationService notificationService;

    @Before
    public void setup(){
        conversationStore = Mockito.mock(ConversationStore.class);
        messageStore = Mockito.mock(MessageStore.class);
        notificationService = Mockito.mock(NotificationService.class);
        chatService = new ChatService();
        chatService.setConversationStore(conversationStore);
        chatService.setMessageStore(messageStore);
        chatService.setNotificationService(notificationService);
    }

    @Test
    public void testSendMessageToConversation() throws IllegalArgumentException {
        String conversationName = "test_conversation";
        String messageBody = "test message";
        Conversation conversation = Mockito.mock(Conversation.class);
        UUID conversationId = UUID.randomUUID();
        User author = Mockito.mock(User.class);
        UUID authorId = UUID.randomUUID();

        Mockito.when(conversationStore.getConversationWithTitle(conversationName)).thenReturn(conversation);
        Mockito.when(author.getId()).thenReturn(authorId);
        Mockito.when(conversation.getId()).thenReturn(conversationId);

        chatService.sendMessageToConversation(author, messageBody, conversationName);
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageStore).addMessage(messageArgumentCaptor.capture());
        Assert.assertEquals(messageBody, messageArgumentCaptor.getValue().getContent());
        Assert.assertEquals(conversationId, messageArgumentCaptor.getValue().getConversationId());
        Assert.assertEquals(authorId, messageArgumentCaptor.getValue().getAuthorId());

        ArgumentCaptor<Conversation> conversationArgumentCaptor = ArgumentCaptor.forClass(Conversation.class);
        ArgumentCaptor<User> authorArgumentCaptore = ArgumentCaptor.forClass(User.class);
        Mockito.verify(notificationService).generateMentionNotification(
                messageArgumentCaptor.capture(),
                conversationArgumentCaptor.capture(),
                authorArgumentCaptore.capture()
        );
        Assert.assertEquals(messageBody, messageArgumentCaptor.getValue().getContent());
        Assert.assertEquals(conversationId, conversationArgumentCaptor.getValue().getId());
        Assert.assertEquals(authorId, authorArgumentCaptore.getValue().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageToConversation_withNonExistentConversation_andExpectIllegalArgumentException()
            throws IllegalArgumentException
    {
        String conversationName = "test_conversation";
        String messageBody = "test message";
        User author = Mockito.mock(User.class);

        Mockito.when(conversationStore.getConversationWithTitle(conversationName)).thenReturn(null);

        chatService.sendMessageToConversation(author, messageBody, conversationName);
    }
}
