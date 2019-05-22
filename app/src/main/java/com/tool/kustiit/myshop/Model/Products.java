package com.tool.kustiit.myshop.Model;

public class Products {

    public String productName , description ,price, catergory , date, image ,time, pid;

    public Products(){

    }

    public Products(String productName, String description, String price, String catergory, String date, String image, String time, String pid) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.catergory = catergory;
        this.date = date;
        this.image = image;
        this.time = time;
        this.pid = pid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCatergory() {
        return catergory;
    }

    public void setCatergory(String catergory) {
        this.catergory = catergory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
