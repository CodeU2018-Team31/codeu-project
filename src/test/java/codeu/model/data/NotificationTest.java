package codeu.model.data;

import org.junit.Assert;
import org.junit.Test;
import java.util.UUID;

public class NotificationTest {
    @Test
    public void testCreate() {
        UUID id = UUID.randomUUID();
        UUID author = UUID.randomUUID();
        UUID conversation = UUID.randomUUID();
        String content = "User1 mentioned you in Conversation: `Hello World!`";
        UUID mentioned = UUID.randomUUID();

        Notification notification = new Notification(id, author, conversation, content, mentioned);

        Assert.assertEquals(id, notification.getId());
        Assert.assertEquals(author, notification.getAuthorId());
        Assert.assertEquals(conversation, notification.getConversationId());
        Assert.assertEquals(content, notification.getContent());
        Assert.assertEquals(mentioned, notification.getMentionedId());
    }
}
