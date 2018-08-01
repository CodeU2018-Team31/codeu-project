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

public class ProfileServlet extends HttpServlet {

    private UserStore userStore;

    /**
     * Sets the {@link UserStore} used by this servlet. This function provides a common setup method for use
     * by the test framework or the servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        setUserStore(UserStore.getInstance());
    }
    /**
     * This function starts when a user navigates to a profile page.
     * It simply forwards the request  to the corresponding jsp view
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String username = (String) request.getSession().getAttribute("user");
        User user = userStore.getUser(username);

        if(user == null){
            response.sendRedirect("/login");
            return;
        }

        String bio = user.getBio();
        request.setAttribute("bio", bio);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = (String) request.getSession().getAttribute("user");
        User user = userStore.getUser(username);

        if(user == null){
            response.sendRedirect("/login");
            return;
        }

        String bio = request.getParameter("bio");
        user.setBio(bio);
        userStore.updateUser(user);

        response.sendRedirect("/profile");
    }
}