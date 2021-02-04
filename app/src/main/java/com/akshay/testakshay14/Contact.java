package com.akshay.testakshay14;

public class Contact {
    int _id;
    String fname;
    String lname;
    String email;
    String password;
    String gender;
    String locationcity;
    String dateOfb;
    String userImage;

    public Contact(int _id, String fname, String lname, String email, String gender, String dateOfb, String locationcity, String userImage, String password) {
        this._id = _id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.locationcity = locationcity;
        this.dateOfb = dateOfb;
        this.userImage = userImage;

    }

    public Contact(String fname, String lname, String email, String gender, String dateOfb, String locationcity, String userImage, String password) {

        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.locationcity = locationcity;
        this.dateOfb = dateOfb;
        this.userImage = userImage;

    }

    public String getDateOfb() {
        return dateOfb;
    }

    public void setDateOfb(String dateOfb) {
        this.dateOfb = dateOfb;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Contact() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    String bdate;
}
