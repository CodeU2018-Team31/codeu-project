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

import codeu.enumeration.ActivityTypeEnum;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;

/**
 * This class represents a log of an activity, which can reflect on an activity feed.
 */
public class Activity {
    private final UUID id;
    private final String description;
    private final Instant datetime;
    private final ActivityTypeEnum type;

    /**
     * @param id          The unique id identifying the activity entry
     * @param description The body text of the activity entry, describing what happened
     * @param datetime    The instant at which the activity was created
     * @param type        The type of the activity, which identifies the kind of action that occured
     */
    public Activity(UUID id, String description, Instant datetime, ActivityTypeEnum type) {
        this.id = id;
        this.description = description;
        this.datetime = datetime;
        this.type = type;
    }


    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public ActivityTypeEnum getType() {
        return type;
    }

    public String getFormattedDatetime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneId.of("UTC"));
        return formatter.format(datetime);
    }
}
