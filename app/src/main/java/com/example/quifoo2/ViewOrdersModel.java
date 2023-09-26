package com.example.quifoo2;

public class ViewOrdersModel {
    String OrderId, CounterName;

    public ViewOrdersModel(String orderId, String counterName) {
        OrderId = orderId;
        CounterName = counterName;
    }

    public ViewOrdersModel()
    {

    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getCounterName() {
        return CounterName;
    }

    public void setCounterName(String counterName) {
        CounterName = counterName;
    }
}
