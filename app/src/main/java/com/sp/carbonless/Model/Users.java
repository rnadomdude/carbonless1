package com.sp.carbonless.Model;

public class Users
{
    private String username, email, password, image, address;

    public Users()
    {

    }

    public Users(String username, String email, String password, String image, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.address = address;
    }


    public String getUserName() { return username;}

    public void setUserName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
