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

package codeu.model.data;
import java.util.UUID;

/**
 * This class represents a log of an activity, which can reflect on an activity feed.
 */
public class Notification {
    private final UUID id;
    private final UUID author;
    private final UUID conversation;
    private final String content;
    private final UUID mentioned;

    /**
     * Constructs a new Message.
     *
     * @param id the ID of this Notification
     * @param author the ID of the User who mentioned the User
     * @param conversation the ID of the Conversation this Notification belongs to
     * @param content the text to be printed in notification page
     * @param mentioned the ID of the user that was mentioned
     */
    public Notification(UUID id, UUID author, UUID conversation, String content, UUID mentioned) {
        this.id = id;
        this.author = author;
        this.conversation = conversation;
        this.content = content;
        this.mentioned = mentioned;
    }


    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return author;
    }

    public UUID getConversationId() { return conversation; }

    public String getContent() {
        return content;
    }

    public UUID getMentionedId() {
        return mentioned;
    }
}
