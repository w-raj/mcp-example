package dev.rk.mcp.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY
)
public class User {
  private int id;
  private String firstName;
  private String lastName;
  private String maidenName;
  private int age;
  private String gender;
  private String email;
  private String phone;
  private String username;
  private String password;
  private String birthDate;
  private String image;
  private String bloodGroup;
  private double height;
  private double weight;
  private String eyeColor;
  private Map<String, String> hair;
  private String ip;
  private Map<String, Object> address;
  private String macAddress;
  private String university;
  private Map<String, Object> bank;
  private Map<String, Object> company;
  private String ein;
  private String ssn;
  private String userAgent;
  private Map<String, String> crypto;
  private String role;

  public User() {}

  // Getters and Setters
  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
  
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  
  public String getMaidenName() { return maidenName; }
  public void setMaidenName(String maidenName) { this.maidenName = maidenName; }
  
  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }
  
  public String getGender() { return gender; }
  public void setGender(String gender) { this.gender = gender; }
  
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  
  public String getBirthDate() { return birthDate; }
  public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
  
  public String getImage() { return image; }
  public void setImage(String image) { this.image = image; }
  
  public String getBloodGroup() { return bloodGroup; }
  public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
  
  public double getHeight() { return height; }
  public void setHeight(double height) { this.height = height; }
  
  public double getWeight() { return weight; }
  public void setWeight(double weight) { this.weight = weight; }
  
  public String getEyeColor() { return eyeColor; }
  public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }
  
  public Map<String, String> getHair() { return hair; }
  public void setHair(Map<String, String> hair) { this.hair = hair; }
  
  public String getIp() { return ip; }
  public void setIp(String ip) { this.ip = ip; }
  
  public Map<String, Object> getAddress() { return address; }
  public void setAddress(Map<String, Object> address) { this.address = address; }
  
  public String getMacAddress() { return macAddress; }
  public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
  
  public String getUniversity() { return university; }
  public void setUniversity(String university) { this.university = university; }
  
  public Map<String, Object> getBank() { return bank; }
  public void setBank(Map<String, Object> bank) { this.bank = bank; }
  
  public Map<String, Object> getCompany() { return company; }
  public void setCompany(Map<String, Object> company) { this.company = company; }
  
  public String getEin() { return ein; }
  public void setEin(String ein) { this.ein = ein; }
  
  public String getSsn() { return ssn; }
  public void setSsn(String ssn) { this.ssn = ssn; }
  
  public String getUserAgent() { return userAgent; }
  public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
  
  public Map<String, String> getCrypto() { return crypto; }
  public void setCrypto(Map<String, String> crypto) { this.crypto = crypto; }
  
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
  
  @Override
  public String toString() {
    return "User{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName + "'}";
  }
}