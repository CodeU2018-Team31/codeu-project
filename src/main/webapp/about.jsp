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
<t:base>
    <jsp:attribute name="bodyContent">
      <h1>About the EastCode Chat App</h1>
      <p>
        Welcome to the EastCode Chat App! Designed with developers in mind, the chat app makes you feel home
        with your every day dark theme dictating the style. Aside from that, here is a bunch of things you can explore:
      </p>
      <ul>
        <li>
            <b>EastBot: </b>
            Try chatting with our friend and smart conversational chatbot, EastBot! He is a bit shy, but try having some smalltalk with him,
            and he will hopefully give you a sensible response back! You can also ask EastBot to send messages to conversations on your behalf;
            try for example something along the lines of "Can you send, This is a Test Message, to PHPers?", and see what happens! Whenever, you
            want to chat, EastBot is always there as a private conversation, that no one else can tap into
        </li>
        <li>
            <b>Mentions and Notifications: </b>
            EastCode supports @mentions in messages as well! Feel free to @mention (i.e tag) another user in a message (or even yourself),
            and the mentioned user, if applicable, will have a notification added to their notifications page, informing them that they have
            been mentioned by you in the given conversation.
        </li>
        <li>
            <b>Styled Messages: </b>
            EastCode's Chat App has full support for markdown, so you can style your messages with formats like "_text_" for italics, or "**text**"
            for bold! This also includes support for hyperlinks, bullet points and a lot more!
        </li>
        <li>
            <b>Administration Page: </b>
            You can login with the credentials "admin" and "password" to gain access to the mock admin account. Through this account, you can have
            access to the admin panel, where you can see general statistics about the chat app, such as how many users have registered, which user
            last registered, and more!
        </li>
        <li>
            <b>Activity Feed: </b>
            Know what has been recently on the go through the Activity Feed page! Note that only activities that are classified as public will be
            logged on this page - for example, any messages sent to EastBot will not be logged on the feed.
        </li>
        <li>
            <b>Profiles: </b>
            Tell people more about yourself through a bio on your very own profile page, and check other people's profiles too!
        </li>
      </ul>

      <h3>Team Members</h3>
      <ul>
        <li>Blessing Adogame</li>
        <li>Ivanna Pena</li>
        <li>Moustafa Elsisy</li>
        <li>Xavier Ray</li>
      </ul>
    </jsp:attribute>
</t:base>

