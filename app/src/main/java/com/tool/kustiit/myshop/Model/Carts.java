package com.tool.kustiit.myshop.Model;

public class Carts {

    private String pName , pid , price , discount , date , quantity , time;

    public Carts() {
    }

    public Carts(String pName, String pid, String price, String discount, String date, String quantity, String time) {
        this.pName = pName;
        this.pid = pid;
        this.price = price;
        this.discount = discount;
        this.date = date;
        this.quantity = quantity;
        this.time = time;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
