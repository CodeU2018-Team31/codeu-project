package codeu.service;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.basic.NotificationStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

public class NotificationServiceTest {

    private NotificationStore mockNotificationStore;
    private NotificationService notificationService;

    @Before
    public void setup(){
        mockNotificationStore = Mockito.mock(NotificationStore.class);
        notificationService = new NotificationService(mockNotificationStore);
    }

    @Test
    public void test_generateMentionNotification(){

        User fakeUser =
                new User(
                        UUID.randomUUID(),
                        "test_username",
                        "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
                        Instant.now(),
                        false);
        User tester =
                new User(
                        UUID.randomUUID(),
                        "test",
                        "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
                        Instant.now(),
                        false);

        Conversation fakeConversation =
                new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now());

        Message fakeMessage =
                new Message(UUID.randomUUID(), fakeConversation.getId(), fakeUser.getId(), "hey @test.", Instant.now());

        Mockito.when(mockNotificationStore.getuserMentioned(fakeMessage.getContent())).thenReturn(tester.getId());
        notificationService.generateMentionNotification(fakeMessage, fakeConversation, fakeUser);

        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);
        Mockito.verify(mockNotificationStore).addNotification(notificationArgumentCaptor.capture());
        Assert.assertEquals(
                "test_username mentioned you in <a href=\"/chat/test_conversation\">test_conversation</a>: hey @test.",
                notificationArgumentCaptor.getValue().getContent());
        Assert.assertEquals(tester.getId(),notificationArgumentCaptor.getValue().getMentionedId());
    }
}
