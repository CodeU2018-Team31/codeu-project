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
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="java.util.List" %>
<%
List<Activity> activities = (List<Activity>) request.getAttribute("activities");
String error = (String) request.getAttribute("error");
%>
<t:base>
    <jsp:attribute name="headContent">
        <link rel="stylesheet" href="/css/activityFeed.css">
    </jsp:attribute>
    <jsp:attribute name="bodyContent">
        <h1>Activity Feed</h1>
        <% if(error != null) { %>
            <p><%= request.getAttribute("error") %></p>
        <% } else { %>
            <ul class="activities">
                <% for(Activity activity: activities){ %>
                    <li><b><%= activity.getFormattedDatetime() %> UTC :</b> <%= activity.getDescription() %></li>
                <% } %>
            </ul>
        <% } %>
    </jsp:attribute>
</t:base>
