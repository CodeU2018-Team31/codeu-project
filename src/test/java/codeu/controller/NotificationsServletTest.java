package codeu.controller;

import codeu.model.data.Notification;
import codeu.model.store.basic.NotificationStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NotificationsServletTest {
    private NotificationsServlet notificationsServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private NotificationStore mockNotificationStore;

    @Before
    public void setup() {
        notificationsServlet = new NotificationsServlet();

        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);

        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/notifications.jsp"))
                .thenReturn(mockRequestDispatcher);

        mockNotificationStore = Mockito.mock(NotificationStore.class);
        notificationsServlet.setNotificationStore(mockNotificationStore);
    }

    @Test
    public void testDoGet() throws IOException, ServletException{
        Notification notification = new Notification(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),"test @user1", UUID.randomUUID());
        List<Notification> notifications = new ArrayList<>(Arrays.asList(notification));

        notificationsServlet.doGet(mockRequest, mockResponse);
        mockRequest.setAttribute("notifications", notifications);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
        Mockito.verify(mockRequest.getRequestDispatcher("/WEB-INF/view/notifications.jsp")).forward(mockRequest, mockResponse);
    }
}
