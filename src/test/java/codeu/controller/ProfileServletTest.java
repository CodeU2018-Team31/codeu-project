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

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProfileServletTest {

    private ProfileServlet ProfileServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private HttpSession mockSession;
    private UserStore mockUserStore;

    @Before
    public void setup() {
        ProfileServlet = new ProfileServlet();

        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockSession = Mockito.mock(HttpSession.class);
        mockUserStore = Mockito.mock(UserStore.class);

        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
                .thenReturn(mockRequestDispatcher);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        ProfileServlet.setUserStore(mockUserStore);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        User user = Mockito.mock(User.class);

        Mockito.when(mockSession.getAttribute("user")).thenReturn("test_user");
        Mockito.when(mockUserStore.getUser("test_user")).thenReturn(user);
        Mockito.when(user.getBio()).thenReturn("test bio");

        ProfileServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("bio", "test bio");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGet_withNoLoggedInUser_AndExpectRedirect() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn(null);
        Mockito.when(mockUserStore.getUser("test_user")).thenReturn(null);

        ProfileServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).sendRedirect("/login");
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        User user = Mockito.mock(User.class);

        Mockito.when(mockSession.getAttribute("user")).thenReturn("test_user");
        Mockito.when(mockUserStore.getUser("test_user")).thenReturn(user);
        Mockito.when(mockRequest.getParameter("bio")).thenReturn("test bio");
        ProfileServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(user).setBio("test bio");
        Mockito.verify(mockUserStore).updateUser(user);
        Mockito.verify(mockResponse).sendRedirect("/profile");
    }
}
