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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AdminServlet extends HttpServlet {

    /**
     * Store class that gives access to Users.
     */
    private UserStore userStore;

    @Override
    public void init() throws ServletException {
        super.init();
        setUserStore(UserStore.getInstance());
    }

    /**
     * Sets the UserStore used by this servlet. This function provides a common setup method for use
     * by the test framework or the servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
//
//    @Override
//    public void doPost(HttpServletRequest request, HttpServletResponse response)
//        throws IOException, ServletException {
//
//        String username = (String) request.getSession().getAttribute("user");
//        if (username != "admin") {
//            // user is not logged in, don't let them go to admin page
//            response.sendRedirect("/login");
//            return;
//        }
//
//        User user = userStore.getUser(username);
//        if (user == null) {
//            // user was not found, don't let them go to admin page
//            System.out.println("User not found: " + username);
//            response.sendRedirect("/login");
//            return;
//        }
//    }

    /**
     * This function fires when a user navigates to the Admin page.
     * It simply forwards the request to the corresponding jsp view
     */
//
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = (String) request.getSession().getAttribute("user");
//        User user = userStore.getUser(username);
        if (!username.matches("admin")) {
            response.sendRedirect("/login");
        }else {
            request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        }
    }
}
//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response)
//        throws IOException, ServletException {
//        request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
//        }
//    }


