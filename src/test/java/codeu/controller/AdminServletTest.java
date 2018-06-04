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



import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
//import java.time.Instant;
//import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.mockito.Mockito;


public class AdminServletTest {
    private AdminServlet adminServlet;
    private HttpSession mockSession;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private UserStore mockUserStore;


    @Before
    public void setup() {
        adminServlet = new AdminServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
                .thenReturn(mockRequestDispatcher);
    }
    @Test
    public void testDoGet_Admin() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");
        UserStore mockUserStore = Mockito.mock(UserStore.class);
        adminServlet.setUserStore(mockUserStore);
        User admin =
                new User(
                        UUID.randomUUID(),
                        "admin",
                        "eastcode",
                        Instant.now(),
                        true);
        Mockito.when(mockUserStore.getUser("admin")).thenReturn(admin);

        adminServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
        Mockito.verify(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp")).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGet_User() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
        UserStore mockUserStore = Mockito.mock(UserStore.class);
        adminServlet.setUserStore(mockUserStore);
        User user =
                new User(
                        UUID.randomUUID(),
                        "test_username",
                        "eastcode",
                        Instant.now(),
                        false);
        Mockito.when(mockUserStore.getUser("test_username")).thenReturn(user);

        adminServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).sendRedirect("/login");
    }
    }



