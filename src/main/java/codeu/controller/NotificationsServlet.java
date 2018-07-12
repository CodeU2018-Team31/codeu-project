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

import codeu.model.data.Notification;
import codeu.model.store.basic.NotificationStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsServlet extends HttpServlet {

    private NotificationStore notificationStore;

    @Override
    public void init() throws ServletException {
        super.init();
        setNotificationStore(NotificationStore.getInstance());
    }

    /**
     * Sets the NotificationStore used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setNotificationStore(NotificationStore notificationStore) {
        this.notificationStore = notificationStore;
    }

    /**
     * This function fires when a user navigates to the Activity Feed page.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Notification> notifications = notificationStore.loadNotifications();
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
    }
}