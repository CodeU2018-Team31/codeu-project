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
<t:base>
    <jsp:attribute name="title">Profile</jsp:attribute>
    <jsp:attribute name="bodyContent">
    <% if(request.getSession().getAttribute("user") != null){ %>
          <h1>Hello <%= request.getSession().getAttribute("user") %>!</h1>
        <% } else{ %>
          <h1> Hello </h1>
        <% } %>
        <h2> Welcome to your profile </h2>

        <button> Upload </button>

    </jsp:attribute>
</t:base>
