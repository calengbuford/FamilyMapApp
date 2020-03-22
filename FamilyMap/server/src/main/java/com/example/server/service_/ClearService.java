package com.example.server.service_;

import com.example.server.dao_.*;
import com.example.server.request_.ClearRequest;
import com.example.server.response_.ClearResponse;

import java.sql.Connection;

public class ClearService {
    private AuthTokenDao authTokenDao;
    private EventDao eventDao;
    private PersonDao personDao;
    private UserDao userDao;
    private Database db;
    private ClearResponse response;

    /**
     * Empty constructor
     */
    public ClearService() {
        response = new ClearResponse();
    }

    /**
     * Delete ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data.
     * @return ClearResponse object as response
     */
    public ClearResponse clear(ClearRequest request) {
        try {
            // Connect and make a new Dao
            db = new Database();
            Connection conn = db.openConnection();

            authTokenDao = new AuthTokenDao(conn);
            eventDao = new EventDao(conn);
            personDao = new PersonDao(conn);
            userDao = new UserDao(conn);

            // Delete all information from the database
            authTokenDao.deleteAllAuthTokens();
            eventDao.deleteAllEvents();
            personDao.deleteAllPersons();
            userDao.deleteAllUsers();

            db.closeConnection(true);
            response.setMessage("Clear succeeded.");
            response.setSuccess(true);
            return response;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (Exception error) {
                System.out.println(error.getMessage());
            }
            if (e.getMessage() == null) {
                response.setMessage("Internal Server Error");
            }
            else {
                response.setMessage(e.getMessage());
            }
            response.setSuccess(false);
            return response;
        }
    }
}
