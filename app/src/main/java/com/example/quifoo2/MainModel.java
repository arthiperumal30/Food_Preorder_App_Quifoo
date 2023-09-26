package com.example.quifoo2;

public class MainModel {
    String Name, Category, Image, DishType;
    int Price, Quantity;
    Boolean Available;

    MainModel(){

    }

    public MainModel(String name, int price, String category, String image, String dishType, Boolean available, int quantity) {
        Name = name;
        Price = price;
        Category = category;
        Image = image;
        DishType = dishType;
        Available = available;
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDishType() {
        return DishType;
    }

    public void setDishType(String dishType) {
        DishType = dishType;
    }

    public Boolean getAvailable() {
        return Available;
    }

    public void setAvailable(Boolean available) {
        Available = available;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
