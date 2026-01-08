package dev.rk.mcp.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY
)
public class UsersResponse {
  private List<User> users;
  private int total;
  private int skip;
  private int limit;

  public UsersResponse() {}

  public List<User> getUsers() { return users; }
  public void setUsers(List<User> users) { this.users = users; }
  
  public int getTotal() { return total; }
  public void setTotal(int total) { this.total = total; }
  
  public int getSkip() { return skip; }
  public void setSkip(int skip) { this.skip = skip; }
  
  public int getLimit() { return limit; }
  public void setLimit(int limit) { this.limit = limit; }
}