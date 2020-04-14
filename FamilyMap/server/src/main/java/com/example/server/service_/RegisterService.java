package com.example.server.service_;

import com.example.server.dao_.AuthTokenDao;
import com.example.server.dao_.Database;
import com.example.server.dao_.UserDao;
import com.example.shared.model_.AuthToken;
import com.example.server.model_.User;
import com.example.server.request_.FillRequest;
import com.example.shared.request_.RegisterRequest;
import com.example.server.response_.FillResponse;
import com.example.shared.response_.RegisterResponse;

import java.sql.Connection;
import java.util.UUID;

public class RegisterService {
    private User user;
    private AuthToken authToken;
    private UserDao userDao;
    private AuthTokenDao authTokenDao;
    private Database db;
    private RegisterResponse response;

    /**
     * Constructor
     */
    public RegisterService() {
        response = new RegisterResponse();
    }

    /**
     * Take a RegisterRequest, create a new user account, generate 4 generations of ancestor data for the new
     * user, log the user in, and return a response with an AuthToken.
     * @param request the request information from the client
     * @return RegisterResponse object as response
     */
    public RegisterResponse register(RegisterRequest request) {
        try {
            // Connect and make a new Dao
            db = new Database();
            Connection conn = db.openConnection();

            // Check request parameters
            if (request.getUserName() == null||  request.getPassword() == null || request.getEmail() == null ||
                    request.getFirstName() == null || request.getLastName() == null ||
                    (!"f".equals(request.getGender()) && !"m".equals(request.getGender()))) {
                throw new Exception("Invalid request value");
            }
            if (request.getUserName().isEmpty() ||  request.getPassword().isEmpty() || request.getEmail().isEmpty() ||
                    request.getFirstName().isEmpty() || request.getLastName().isEmpty()) {
                throw new Exception("Invalid request value");
            }

            // Build a new user
            user = new User(request.getUserName(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender(), UUID.randomUUID().toString());

            userDao = new UserDao(conn);
            authTokenDao = new AuthTokenDao(conn);

            if (userDao.getUser(user.getUserName()) != null) {
                if (authTokenDao.getAuthTokenByUserName(user.getUserName()) != null) {
                    throw new Exception("User already registered in the database");
                }
            }
            else {
                // Add the user to the database
                userDao.createUser(user);
            }

            // Create a new AuthToken for the login session
            authToken = new AuthToken(user.getUserName(), user.getPassword());
            authTokenDao.createAuthToken(authToken);

            db.closeConnection(true);

            // Fill the user with random information
            FillService fillService = new FillService();
            FillRequest fillRequest = new FillRequest();
            String generations = "4";
            FillResponse fillResponse = fillService.fill(fillRequest, user.getUserName(), generations);

            if (!fillResponse.getSuccess()) {
                throw new Exception(fillResponse.getMessage());
            }

            response.setAuthToken(authToken.getToken());
            response.setUserName(user.getUserName());
            response.setPersonID(user.getPersonID());
            response.setSuccess(true);
            return response;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (Exception error) {
                System.out.println("Error: " + error.getMessage());
            }
            if (e.getMessage() == null) {
                response.setMessage("Internal Server Error");
            }
            else {
                response.setMessage("Error: " + e.getMessage());
            }
            response.setSuccess(false);
            return response;
        }
    }
}
