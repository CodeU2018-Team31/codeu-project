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
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>
<t:base>
    <jsp:attribute name="bodyContent">
    <%
        UserStore usr = UserStore.getInstance();
        ConversationStore convo = ConversationStore.getInstance();
        MessageStore msg = MessageStore.getInstance();

     %>
         <h1>Administration</h1>
         <p>-----------------------------------------------------------------------</p>
         <h2>Site Statistics</h2>
         <h3>Current Site Stats</h3>
         <p>Users: <%= usr.getUserCount() %> </p>
         <p>Conversations: <%= convo.getConvoCount() %></p>
         <p>Messages: <%= msg.getMsgCount() %> </p>
         <p>Newest User: <%= usr.getNewestUser() %> </p>

    </jsp:attribute>
</t:base>

