<%@tag description="Main base template for chat app"%>
<%@ attribute name="bodyContent" required="true" rtexprvalue="true" %>
<%@ attribute name="headContent" rtexprvalue="true" %>
<%@ attribute name="title" rtexprvalue="true" %>
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
<!DOCTYPE html>
<html>
<head>
  <title><%= title != null ? title : "EastCode Chat" %></title>
  <link rel="stylesheet" href="/css/main.css">
  <link href="https://fonts.googleapis.com/css?family=Encode+Sans+Condensed" rel="stylesheet">
  ${headContent}
</head>
<body>

  <nav>
    <a id="navTitle" href="/">&lt;EastCode/&gt;</a>
    <a href="/profile">Profile</a>
    <a href="/conversations">Conversations</a>
    <a href="/activity">Activity Feed</a>
    <a href="/admin">Administration</a>
    <% if(request.getSession().getAttribute("user") == null){ %>
          <a href="/login">Login</a>
    <% } else { %>
           <a href="/notifications">Notifications</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a id="greeting">Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } %>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
        ${bodyContent}
    </div>
  </div>
</body>
</html>
