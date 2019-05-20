package com.tool.kustiit.myshop.Model;

public class Users {

    private String Name , Number, Password;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Users(String name, String number, String password) {

        Name = name;
        Number = number;
        Password = password;
    }

    public Users()
    {

    }
}
