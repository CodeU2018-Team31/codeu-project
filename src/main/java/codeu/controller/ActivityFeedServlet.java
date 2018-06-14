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

import codeu.model.data.Activity;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.persistence.PersistentDataStoreException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ActivityFeedServlet extends HttpServlet {

    private ActivityStore activityStore;

  @Override
  public void init() throws ServletException {
      super.init();
      setActivityStore(ActivityStore.getInstance());
  }

    /**
     * Sets the {@link ActivityStore} used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setActivityStore(ActivityStore activityStore) {
        this.activityStore = activityStore;
    }

  /**
   * This function fires when a user navigates to the Activity Feed page.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
      List<Activity> activities = new ArrayList<>();

      //Try to load the list of activities. If we fail to do so, we will return a meaningful error
      //message in the page to the user
      try {
          activities = activityStore.getActivitiesBeforeDatetime(Instant.now(), Integer.MAX_VALUE);
      } catch (PersistentDataStoreException exception) {
          System.out.println("ActivityFeedServlet:doGet - Could not load activities: " + exception.getMessage());
          request.setAttribute("error", "Could not load activities! Please try again later.");
      }

      request.setAttribute("activities", activities);
    request.getRequestDispatcher("/WEB-INF/view/activityFeed.jsp").forward(request, response);
  }
}
