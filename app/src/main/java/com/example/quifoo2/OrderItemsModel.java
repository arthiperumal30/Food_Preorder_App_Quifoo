package com.example.quifoo2;

public class OrderItemsModel {
    String Name, Image;
    int Quantity;

    OrderItemsModel()
    {

    }

    public OrderItemsModel(String name, String image, int quantity) {
        Name = name;
        Image = image;
        Quantity = quantity;
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
}
