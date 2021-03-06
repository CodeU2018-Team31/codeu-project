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
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>
<t:base>
    <jsp:attribute name="title"><%= conversation.getTitle() %></jsp:attribute>
    <jsp:attribute name="headContent">
            <link rel="stylesheet" href="/css/chat.css">
          <script>
            // scroll the chat div to the bottom
            function scrollChat() {
              var chatDiv = document.getElementById('chat');
              chatDiv.scrollTop = chatDiv.scrollHeight;
            };
            window.onload = scrollChat
          </script>
    </jsp:attribute>
    <jsp:attribute name="bodyContent">
            <h1><%= request.getAttribute("customChatTitle") != null ? request.getAttribute("customChatTitle") : conversation.getTitle() %>
              <a href="" id="refresh">&#8635;</a></h1>

            <hr/>

            <div id="chat">
              <ul>
            <%
              for (Message message : messages) {
                String author = UserStore.getInstance()
                  .getUser(message.getAuthorId()).getName();
            %>
              <li><strong><%= author %>:</strong> <%= message.getMarkdownContent() %></li>
            <%
              }
            %>
              </ul>
            </div>

            <div id="input-area">
                <% if (request.getSession().getAttribute("user") != null) { %>
                <form action='<%= (String)request.getAttribute("messagePostUrl") %>' method="POST">
                    <input id="message-input" type="text" name="message">
                    <br/>
                    <button id="message-submit" type="submit">Send</button>
                </form>
                <% } else { %>
                  <p><a href="/login">Login</a> to send a message.</p>
                <% } %>
            </div>
    </jsp:attribute>
</t:base>
