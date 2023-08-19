package com.example.uktourism.Model.DataModel;

public class User {
    String name,email,userPfp;

    public User(){

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.userPfp = "https://firebasestorage.googleapis.com/v0/b/uktourism-db.appspot.com/o/ukTourism%2FusersPfp%2FdefaultPfp.jpg?alt=media&token=41bfeeee-afd4-46e2-a764-71b344e86b19";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPfp() {
        return userPfp;
    }

    public void setUserPfp(String userPfp) {
        this.userPfp = userPfp;
    }
}
