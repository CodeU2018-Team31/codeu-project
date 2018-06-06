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
<%@ page import= "codeu.model.data.Conversation"%>
<%@ page import= "codeu.model.data.Message"%>
<%@ page import= "codeu.model.data.User"%>
<%@ page import= "codeu.model.store.persistence.PersistentDataStoreException"%>
<%@ page import= "codeu.model.store.persistence.PersistentDataStore"%>
<%@ page import= "com.google.appengine.api.datastore.DatastoreService"%>
<%@ page import= "com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import= "com.google.appengine.api.datastore.Entity"%>
<%@ page import= "com.google.appengine.api.datastore.PreparedQuery"%>
<%@ page import= "com.google.appengine.api.datastore.Query"%>
<%@ page import= "com.google.appengine.api.datastore.Query.SortDirection"%>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import= "java.time.Instant"%>
<%@ page import= "java.util.ArrayList"%>
<%@ page import= "java.util.List"%>
<%@ page import= "java.util.UUID"%>
<t:base>
    <jsp:attribute name="bodyContent">
    <%
       PersistentDataStore store = new PersistentDataStore();

       int numusers = store.loadUsers().size();

       int numconvo = store.loadConversations().size();

       int nummessage = store.loadMessages().size();

     %>
         <h1>Administration</h1>
         <p>-----------------------------------------------------------------------</p>
         <h2>Site Statistics</h2>
         <h3>Current Site Stats</h3>
         <p>Users: <%= numusers %> </p>
         <p>Conversations: <%= numconvo %> </p>
         <p>Messages: <%= nummessage %> </p>

    </jsp:attribute>
</t:base>

