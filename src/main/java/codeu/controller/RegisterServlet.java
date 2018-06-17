package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.enumeration.ActivityTypeEnum;
import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ConversationStore;
import org.mindrot.jbcrypt.BCrypt;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

public class RegisterServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

    /**
     * Store class that gives access to Activites.
     */
    private ActivityStore activityStore;

    private ConversationStore conversationStore;

  /**
   * Set up state for handling registration-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
      setActivityStore(ActivityStore.getInstance());
      setConversationStore(ConversationStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

    /**
     * Sets the {@link ConversationStore} used by this servlet. This function provides a common setup method for use
     * by the test framework or the servlet's init() function.
     */
    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    /**
     * Sets the {@link ActivityStore} used by this servlet. This function provides a common setup method for use
     * by the test framework or the servlet's init() function.
     */
    void setActivityStore(ActivityStore activityStore) {
        this.activityStore = activityStore;
    }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = request.getParameter("username");

    if (!username.matches("[\\w*\\s*]*")) {
      request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
      return;
    }

    if (userStore.isUserRegistered(username)) {
      request.setAttribute("error", "That username is already taken.");
      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
      return;
    }

    String password = request.getParameter("password");
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

      User user;
    if (username.matches("admin") && password.matches("eastcode")) {
        user = new User(UUID.randomUUID(), username, hashedPassword, Instant.now(), true);
      userStore.addUser(user);
    }else {
        user = new User(UUID.randomUUID(), username, hashedPassword, Instant.now(), false);
      userStore.addUser(user);
    }

      //Log Activity for user creation
      String activityDescription = String.format("%s joined!", username);
      Activity activity = new Activity(UUID.randomUUID(), activityDescription, Instant.now(), ActivityTypeEnum.USER_ADDED);
      activityStore.addActivity(activity);


      //Create chatbot conversation for user
      final String conversationName = "chatbot-" + username.hashCode();
      Conversation chatbotConversation = new Conversation(UUID.randomUUID(), user.getId(), conversationName, Instant.now());
      chatbotConversation.setPrivate(true);
      conversationStore.addConversation(chatbotConversation);

    response.sendRedirect("/login");
  }
}
