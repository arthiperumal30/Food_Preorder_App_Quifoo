package com.example.quifoo2;

public class ViewCartModel {
    String Name, Image, DishType, Category;
    int Quantity, Price, QtyAvailable;

    ViewCartModel(){

    }

    public ViewCartModel(String name, String image, int quantity, int price, String dishType, String category, int qtyAvailable, String counterName) {
        Name = name;
        Image = image;
        Quantity = quantity;
        Price = price;
        Category = category;
        DishType = dishType;
        QtyAvailable = qtyAvailable;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDishType() {
        return DishType;
    }

    public void setDishType(String dishType) {
        DishType = dishType;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getQtyAvailable() {
        return QtyAvailable;
    }

    public void setQtyAvailable(int qtyAvailable) {
        QtyAvailable = qtyAvailable;
    }
}

