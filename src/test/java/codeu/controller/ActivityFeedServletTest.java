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

package codeu.controller;

import codeu.enumeration.ActivityTypeEnum;
import codeu.model.data.Activity;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ActivityFeedServletTest {

    private ActivityFeedServlet activityFeedServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private ActivityStore mockActivityStore;

    @Before
    public void setup() {
        activityFeedServlet = new ActivityFeedServlet();

        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityFeed.jsp"))
                .thenReturn(mockRequestDispatcher);

        mockActivityStore = Mockito.mock(ActivityStore.class);
        activityFeedServlet.setActivityStore(mockActivityStore);
    }

    @Test
    public void testDoGet() throws IOException, ServletException, PersistentDataStoreException {
        Activity activity = new Activity(UUID.randomUUID(), "User1 joined!", Instant.now(), ActivityTypeEnum.USER_ADDED);
        List<Activity> activities = new ArrayList<>(Arrays.asList(activity));

        Mockito.when(mockActivityStore.getActivitiesBeforeDatetime(Mockito.any(Instant.class), Mockito.anyInt()))
                .thenReturn(activities);

        activityFeedServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("activities", activities);
        Mockito.verify(mockRequest, Mockito.never()).setAttribute("error", Mockito.eq(Mockito.notNull())); //error is never set
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGet_CatchesPersistentDataStoreException() throws IOException, ServletException, PersistentDataStoreException {
        Mockito.when(mockActivityStore.getActivitiesBeforeDatetime(Mockito.any(Instant.class), Mockito.anyInt()))
                .thenThrow(PersistentDataStoreException.class);

        activityFeedServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("error", "Could not load activities! Please try again later.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }
}
