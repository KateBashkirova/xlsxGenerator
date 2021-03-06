package POJO;

import java.util.ArrayList;

public class Order {
    public String productName;
    public Integer productAmount;
    public Float pricePerUnit;
    public String deliveryDate;
    public String courierDelivery;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCourierDelivery() {
        if (courierDelivery == null) {
            return "not required";
        } else {
            return courierDelivery;
        }
    }

    public void setCourierDelivery(String courierDelivery) {
        this.courierDelivery = courierDelivery;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public ArrayList<String> getTotalOrderInfo() {
        ArrayList<String> totalOrderInfo = null;
        totalOrderInfo.add(getProductName());
        totalOrderInfo.add(String.valueOf(getProductAmount()));
        totalOrderInfo.add(String.valueOf(getPricePerUnit()));
        totalOrderInfo.add(getDeliveryDate());
        totalOrderInfo.add(getCourierDelivery());
        return totalOrderInfo;
    }
}
