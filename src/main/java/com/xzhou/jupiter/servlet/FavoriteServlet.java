package com.xzhou.jupiter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzhou.jupiter.db.MySQLConnection;
import com.xzhou.jupiter.db.MySQLException;
import com.xzhou.jupiter.entity.FavoriteRequestBody;
import com.xzhou.jupiter.entity.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite"})
public class FavoriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        // Get favorite item information from request body
        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);
        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MySQLConnection connection = null;
        try {
            // Save the favorite item to the database
            connection = new MySQLConnection();
            connection.setFavoriteItem(userId, body.getFavoriteItem());
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);

        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MySQLConnection connection = null;
        try {
            // Remove the favorite item to the database
            connection = new MySQLConnection();
            connection.unsetFavoriteItem(userId, body.getFavoriteItem().getId());
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the session is still valid, which means the user has been logged in successfully.
        HttpSession session = request.getSession(false);
        //If not login, then forbidden
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        Map<String, List<Item>> itemMap;
        MySQLConnection connection = null;
        //declare outside of try catch block because if it is inside try, “connection” in
        // finally can not recognize it
        try {
            // Read the favorite items from the database
            connection = new MySQLConnection();
            itemMap = connection.getFavoriteItems(userId);
            ServletUtil.writeItemMap(response, itemMap);

        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}