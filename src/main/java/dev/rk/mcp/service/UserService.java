package dev.rk.mcp.service;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.rk.mcp.model.User;
import dev.rk.mcp.model.UsersResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

  private final RestTemplate restTemplate;
  private final String BASE_URL = "https://dummyjson.com";

  public UserService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Get all users with pagination
   *
   * @param limit Maximum number of users to return
   * @param skip  Number of users to skip for pagination
   * @return List of users wrapped in a response object
   */
  @Tool(name = "getAllUsers", description = "Get all users")
  public UsersResponse getAllUsers(int limit, int skip) {
    String url = BASE_URL + "/users?limit=" + limit + "&skip=" + skip;
    System.out.println("getAllUsers: " + url);
    UsersResponse usersResponse = restTemplate.getForObject(url, UsersResponse.class);
    System.out.println("getAllUsers - Call to fetch data completed: " + url);
    return usersResponse;
  }

  /**
   * Get all users (default pagination)
   *
   * @return List of users wrapped in a response object
   */
  @Tool(name = "getAllUsersDefault", description = "Get all users with default pagination")
  public UsersResponse getAllUsers() {
    String url = BASE_URL + "/users";
    System.out.println("getAllUsersDefault: " + url);
    UsersResponse usersResponse = restTemplate.getForObject(url, UsersResponse.class);
    System.out.println("getAllUsersDefault - Call to fetch data completed: " + url);
    return usersResponse;
  }

  /**
   * Get a single user by ID
   *
   * @param id The user ID
   * @return User object
   */
  @Tool(name = "getUserById", description = "Get a single user by ID")
  public User getUserById(int id) {
    String url = BASE_URL + "/users/" + id;
    System.out.println("\n=== getUserById CALLED with ID: " + id + " ===");
    System.out.println("URL: " + url);
    
    try {
      User user = restTemplate.getForObject(url, User.class);
      
      if (user == null) {
        System.out.println("ERROR: RestTemplate returned NULL");
        return null;
      }
      
      System.out.println("SUCCESS: User object received from API");
      System.out.println("  - ID: " + user.getId());
      System.out.println("  - First Name: " + user.getFirstName());
      System.out.println("  - Last Name: " + user.getLastName());
      System.out.println("  - Email: " + user.getEmail());
      System.out.println("Returning User object - should now be serialized by custom ToolCallResultConverter");
      System.out.println("=== END getUserById ===");
      
      return user;
    } catch (Exception e) {
      System.out.println("EXCEPTION in getUserById: " + e.getClass().getName() + " - " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Search for users by query
   *
   * @param query The search query
   * @return List of users that match the query
   */
  @Tool(name = "searchUsers", description = "Search for users by query")
  public UsersResponse searchUsers(String query) {
    String url = BASE_URL + "/users/search?q=" + query;
    return restTemplate.getForObject(url, UsersResponse.class);
  }

  /**
   * Add a new user
   *
   * @param user The user to add
   * @return The added user with ID
   */
  @Tool(name = "addUser", description = "Add a new user")
  public User addUser(User user) {
    String url = BASE_URL + "/users/add";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<User> request = new HttpEntity<>(user, headers);

    return restTemplate.postForObject(url, request, User.class);
  }

  /**
   * Update a user
   *
   * @param id      The ID of the user to update
   * @param updates A map of fields to update
   * @return The updated user
   */
  @Tool(name = "updateUser", description = "Update a user")
  public User updateUser(int id, Map<String, Object> updates) {
    String url = BASE_URL + "/users/" + id;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(updates, headers);

    return restTemplate.exchange(url, HttpMethod.PUT, request, User.class).getBody();
  }

  /**
   * Delete a user
   *
   * @param id The ID of the user to delete
   * @return The deleted user with isDeleted flag
   */
  @Tool(name = "deleteUser", description = "Delete a user")
  public User deleteUser(int id) {
    String url = BASE_URL + "/users/" + id;

    return restTemplate.exchange(url, HttpMethod.DELETE, null, User.class).getBody();
  }


}