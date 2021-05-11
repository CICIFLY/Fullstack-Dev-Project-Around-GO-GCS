package com.xzhou.jupiter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzhou.jupiter.db.MySQLConnection;
import com.xzhou.jupiter.db.MySQLException;
import com.xzhou.jupiter.entity.LoginRequestBody;
import com.xzhou.jupiter.entity.LoginResponseBody;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read user data from the request body
        LoginRequestBody body = ServletUtil.readRequestBody(LoginRequestBody.class, request);

        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username;
        MySQLConnection connection = null;
        try {
            // Verify if the user ID and password are correct
            connection = new MySQLConnection();
            String userId = body.getUserId();
            String password = ServletUtil.encryptPassword(body.getUserId(), body.getPassword());
            username = connection.verifyLogin(userId, password);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            connection.close();
        }

        // Create a new session for the user if user ID and password are correct,
        // otherwise return Unauthorized error.
        if (!username.isEmpty()) {
            // Create a new session, put user ID as an attribute into the session object, and set the expiration time to 600 seconds.
            HttpSession session = request.getSession();// create session and return UUID
            session.setAttribute("user_id", body.getUserId());
            session.setMaxInactiveInterval(600);
            //sessionId will be stored in the header
            LoginResponseBody loginResponseBody = new LoginResponseBody(body.getUserId(), username);
            response.setContentType("application/json;charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().print(new ObjectMapper().writeValueAsString(loginResponseBody));
            //这里不写response.add(session_id) , 因为Tomcat帮你实现了
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
