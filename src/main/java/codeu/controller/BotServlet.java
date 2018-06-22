package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.service.BotService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BotServlet extends HttpServlet {

    private ConversationStore conversationStore;
    private MessageStore messageStore;
    private UserStore userStore;
    private BotService botService;

    /**
     * Sets the {@link ConversationStore} used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    /**
     * Sets the {@link MessageStore} used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    /**
     * Sets the {@link UserStore} used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * Sets the {@link BotService} used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setBotService(BotService botService) {
        this.botService = botService;
    }

    /**
     * Set up state for handling chat requests.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());
        setBotService(new BotService());
    }

    /**
     * This function fires when a user navigates to the chatbot page. It gets the username from the session
     * and loads the corresponding chatbot conversation for the user, if the user is logged in, and forwards the
     * messages in the conversation to chat.jsp
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = (String) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        final String conversationTitle = "chatbot-" + user.hashCode();

        Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
        if (conversation == null) {
            System.out.println("BotServlet-doGet: Chatbot conversation was null for user `" + user + "`: " + conversationTitle);
            response.sendRedirect("/conversations");
            return;
        }

        UUID conversationId = conversation.getId();

        List<Message> messages = messageStore.getMessagesInConversation(conversationId);

        request.setAttribute("conversation", conversation);
        request.setAttribute("messages", messages);
        request.setAttribute("messagePostUrl", "/bot");
        request.setAttribute("customChatTitle", "EastBot");
        request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
    }

    /**
     * This function fires when a user submits the form on the chatbot page. It gets the logged-in
     * username from the session, the chat message from the submitted form data.
     * It creates a new Message from that data, adds it to the model, and then
     * redirects back to the chatbot page.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            // user is not logged in, don't let them add a message
            response.sendRedirect("/login");
            return;
        }

        final String conversationTitle = "chatbot-" + username.hashCode();

        Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
        if (conversation == null) {
            System.out.println("BotServlet-doPost: Chatbot conversation was null for user `" + username + "`: " + conversationTitle);
            response.sendRedirect("/conversations");
            return;
        }

        User user = userStore.getUser(username);
        if (user == null) {
            System.out.println("BotServlet-doPost: No user with username `" + username + "`");
            response.sendRedirect("/login");
            return;
        }


        String messageContent = request.getParameter("message");

        // this removes any HTML from the message content
        String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

        Message message =
                new Message(
                        UUID.randomUUID(),
                        conversation.getId(),
                        user.getId(),
                        cleanedMessageContent,
                        Instant.now());

        messageStore.addMessage(message);

        String responseMessageContent = this.botService.process(cleanedMessageContent);

        Message responseMessage =
                new Message(
                        UUID.randomUUID(),
                        conversation.getId(),
                        user.getId(),
                        responseMessageContent,
                        Instant.now());

        messageStore.addMessage(responseMessage);

        // redirect to a GET request
        response.sendRedirect("/bot");
    }
}
