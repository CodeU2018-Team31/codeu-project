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

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.service.BotService;
import org.apache.http.HttpException;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BotServletTest {

    private BotServlet botServlet;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private ConversationStore mockConversationStore;
    private MessageStore mockMessageStore;
    private UserStore mockUserStore;
    private ActivityStore mockActivityStore;
    private BotService mockBotService;

    @Before
    public void setup() {
        botServlet = new BotServlet();

        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/chat.jsp"))
                .thenReturn(mockRequestDispatcher);

        mockConversationStore = Mockito.mock(ConversationStore.class);
        botServlet.setConversationStore(mockConversationStore);

        mockMessageStore = Mockito.mock(MessageStore.class);
        botServlet.setMessageStore(mockMessageStore);

        mockUserStore = Mockito.mock(UserStore.class);
        botServlet.setUserStore(mockUserStore);

        mockBotService = Mockito.mock(BotService.class);
        botServlet.setBotService(mockBotService);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        String username = "mock_user";
        Mockito.when(mockSession.getAttribute("user")).thenReturn(username);

        UUID fakeConversationId = UUID.randomUUID();
        Conversation fakeConversation =
                new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());

        Mockito.when(mockConversationStore.getConversationWithTitle("chatbot-" + username.hashCode()))
                .thenReturn(fakeConversation);

        List<Message> fakeMessageList = new ArrayList<>();
        fakeMessageList.add(
                new Message(
                        UUID.randomUUID(),
                        fakeConversationId,
                        UUID.randomUUID(),
                        "test message",
                        Instant.now())
        );

        Mockito.when(mockMessageStore.getMessagesInConversation(fakeConversationId))
                .thenReturn(fakeMessageList);

        botServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest).setAttribute("conversation", fakeConversation);
        Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
        Mockito.verify(mockRequest).setAttribute("messagePostUrl", "/bot");
        Mockito.verify(mockRequest).setAttribute("customChatTitle", "EastBot");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGet_NotLoggedInUser() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

        botServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).sendRedirect("/login");
    }

    @Test
    public void testDoGet_BadConversation() throws IOException, ServletException {
        String username = "mock_user";
        Mockito.when(mockSession.getAttribute("user")).thenReturn(username);

        Mockito.when(mockConversationStore.getConversationWithTitle(Mockito.anyString())).thenReturn(null);

        botServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).sendRedirect("/conversations");
    }

    @Test
    public void testDoPost() throws IOException, ServletException, HttpException {
        String username = "mock_user";
        Mockito.when(mockSession.getAttribute("user")).thenReturn(username);

        UUID fakeConversationId = UUID.randomUUID();
        UUID fakeChatBotId = UUID.randomUUID();

        Conversation fakeConversation =
                new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());

        Mockito.when(mockConversationStore.getConversationWithTitle("chatbot-" + username.hashCode()))
                .thenReturn(fakeConversation);

        User fakeUser = new User(UUID.randomUUID(), username, "@@#!#!@@@", Instant.now(), false);
        User chatBot = new User(fakeChatBotId, "EastBot", "@@#!#!@@@", Instant.now(), false);
        Mockito.when(mockUserStore.getUser(username)).thenReturn(fakeUser);

        Mockito.when(mockRequest.getParameter("message")).thenReturn("mock_message");
        Mockito.when(mockBotService.process("mock_message")).thenReturn("mock bot response");
        Mockito.when(mockUserStore.getUser("EastBot")).thenReturn(chatBot);

        botServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(mockMessageStore, Mockito.times(2)).addMessage(messageArgumentCaptor.capture());
        List<Message> capturedMessages = messageArgumentCaptor.getAllValues();

        Assert.assertEquals("mock_message", capturedMessages.get(0).getContent());

        Assert.assertEquals("mock bot response", capturedMessages.get(1).getContent());
        Assert.assertEquals(fakeConversationId, capturedMessages.get(1).getConversationId());
        Assert.assertEquals(fakeChatBotId, capturedMessages.get(1).getAuthorId());
    }

    @Test
    public void testDoPost_NotLoggedInUser() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

        botServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).sendRedirect("/login");
    }

    @Test
    public void testDoPost_BadConversation() throws IOException, ServletException {
        String username = "mock_user";
        Mockito.when(mockSession.getAttribute("user")).thenReturn(username);
        Mockito.when(mockConversationStore.getConversationWithTitle(Mockito.anyString())).thenReturn(null);

        botServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).sendRedirect("/conversations");
    }

    @Test
    public void testDoPost_BadUsername() throws IOException, ServletException {
        String username = "mock_user";
        Mockito.when(mockSession.getAttribute("user")).thenReturn(username);

        Conversation fakeConversation = new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now());
        Mockito.when(mockConversationStore.getConversationWithTitle(Mockito.anyString())).thenReturn(fakeConversation);

        Mockito.when(mockUserStore.getUser(username)).thenReturn(null);

        botServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).sendRedirect("/login");
    }


}
