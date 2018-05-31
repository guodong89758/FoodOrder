package com.foodorder.entry;

import org.json.JSONObject;

import java.io.Serializable;

public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 6253103922899897025L;
    private String id_order_product;
    private String id_product;
    private String product_reference;
    private String product_name;
    private String product_name_zh;
    private String image;
    private int product_quantity;
    private double product_price;
    private String reduction;
    private double tax_rate;
    private double product_price_with_tax;

    public OrderInfo() {
    }

    public String getId_order_product() {
        return id_order_product;
    }

    public void setId_order_product(String id_order_product) {
        this.id_order_product = id_order_product;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getProduct_reference() {
        return product_reference;
    }

    public void setProduct_reference(String product_reference) {
        this.product_reference = product_reference;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_name_zh() {
        return product_name_zh;
    }

    public void setProduct_name_zh(String product_name_zh) {
        this.product_name_zh = product_name_zh;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getReduction() {
        return reduction;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

    public double getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(double tax_rate) {
        this.tax_rate = tax_rate;
    }

    public double getProduct_price_with_tax() {
        return product_price_with_tax;
    }

    public void setProduct_price_with_tax(double product_price_with_tax) {
        this.product_price_with_tax = product_price_with_tax;
    }

    public OrderInfo(JSONObject json) {
        if (json == null) {
            return;
        }
        this.id_order_product = json.optString("id_order_product");
        this.id_product = json.optString("id_product");
        this.product_reference = json.optString("product_reference");
        this.product_name = json.optString("product_name");
        JSONObject zhJson = json.optJSONObject("zh");
        if (zhJson != null) {
            this.product_name_zh = zhJson.optString("name");
        }
        this.image = json.optString("image");
        this.product_quantity = json.optInt("product_quantity", 0);
        this.product_price = json.optDouble("product_price");
        this.reduction = json.optString("reduction");
        this.tax_rate = json.optDouble("tax_rate");
        this.product_price_with_tax = json.optDouble("product_price_with_tax");
    }
}
