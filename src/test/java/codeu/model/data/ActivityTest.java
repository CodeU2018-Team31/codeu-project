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

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

public class ActivityTest {

    @Test
    public void testCreate() {
        UUID id = UUID.randomUUID();
        String description = "User1 has just added a message to the Conversation `Hello World!`";
        Instant creation = Instant.now();
        int type = 0;

        Activity activity = new Activity(id, description, creation, type);

        Assert.assertEquals(id, activity.getId());
        Assert.assertEquals(description, activity.getDescription());
        Assert.assertEquals(creation, activity.getDatetime());
        Assert.assertEquals(type, activity.getType());
    }
}
