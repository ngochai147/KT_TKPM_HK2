package com.fooddelivery.user.dto;

public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String userType;
    private Boolean active;

    // Constructors
    public UserDTO() {}

    public UserDTO(String email, String password, String fullName, String userType) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userType = userType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
