package codeu.service;

import codeu.enumeration.ActivityTypeEnum;
import codeu.model.data.*;
import codeu.model.store.basic.ActivityStore;
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
    private ActivityStore activityStore;

    @Before
    public void setup(){
        conversationStore = Mockito.mock(ConversationStore.class);
        messageStore = Mockito.mock(MessageStore.class);
        notificationService = Mockito.mock(NotificationService.class);
        activityStore = Mockito.mock(ActivityStore.class);
        chatService = new ChatService();
        chatService.setConversationStore(conversationStore);
        chatService.setMessageStore(messageStore);
        chatService.setNotificationService(notificationService);
        chatService.setActivityStore(activityStore);
    }

    @Test
    public void testSendMessageToConversation() throws IllegalArgumentException {
        String conversationName = "test_conversation";
        String messageBody = "test @message";
        Conversation conversation = Mockito.mock(Conversation.class);
        UUID conversationId = UUID.randomUUID();
        User author = Mockito.mock(User.class);
        UUID authorId = UUID.randomUUID();

        Mockito.when(conversationStore.getConversationWithTitle(conversationName)).thenReturn(conversation);
        Mockito.when(author.getId()).thenReturn(authorId);
        Mockito.when(author.getName()).thenReturn("test_user");
        Mockito.when(conversation.getId()).thenReturn(conversationId);

        chatService.sendMessageToConversation(author, messageBody, conversationName);
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageStore).addMessage(messageArgumentCaptor.capture());
        Assert.assertEquals("test <font class='mention'>@message</font>", messageArgumentCaptor.getValue().getContent());
        Assert.assertEquals(conversationId, messageArgumentCaptor.getValue().getConversationId());
        Assert.assertEquals(authorId, messageArgumentCaptor.getValue().getAuthorId());

        ArgumentCaptor<Conversation> conversationArgumentCaptor = ArgumentCaptor.forClass(Conversation.class);
        ArgumentCaptor<User> authorArgumentCaptore = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);

        Mockito.verify(notificationService).generateMentionNotification(
                Mockito.eq(messageBody),
                conversationArgumentCaptor.capture(),
                authorArgumentCaptore.capture()
        );
        Assert.assertEquals(conversationId, conversationArgumentCaptor.getValue().getId());
        Assert.assertEquals(authorId, authorArgumentCaptore.getValue().getId());

        Mockito.verify(activityStore).addActivity(activityArgumentCaptor.capture());
        String expectedActivityBody = "test_user sent a message to test_conversation: test @message";
        Assert.assertEquals(ActivityTypeEnum.MESSAGE_ADDED, activityArgumentCaptor.getValue().getType());
        Assert.assertEquals(expectedActivityBody, activityArgumentCaptor.getValue().getDescription());
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
