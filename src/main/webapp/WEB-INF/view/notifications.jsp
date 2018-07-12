<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="codeu.model.data.Notification" %>
<%@ page import="codeu.model.store.basic.NotificationStore" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="java.util.List" %>
<%
UserStore usr = UserStore.getInstance();
NotificationStore notif = NotificationStore.getInstance();
List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
String username = (String) request.getSession().getAttribute("user");
%>
<t:base>
    <jsp:attribute name="bodyContent">
        <h1>Notifications</h1>
        <% for(Notification notification : notifications){ %>
             <% if(username != null && notification.getMentionedId().equals(usr.getUser(username).getId())) {%>
                <li><%= notification.getContent() %></li>
             <% } %>
        <% } %>
    </jsp:attribute>
</t:base>