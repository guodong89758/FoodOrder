package com.foodorder.entry;

import java.util.List;

/**
 * Created by guodong on 2016/10/18.
 */

public class OrderGood {
    private String id_order;
    private boolean isPack;
    private String persons;
    private String number;
    private List<Product> products;

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public boolean isPack() {
        return isPack;
    }

    public void setPack(boolean pack) {
        isPack = pack;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public static class Product {
        private String id_product;
        private int count;
        private List<Attribute> attributes;
        private List<Formula> formulas;

        public String getId_product() {
            return id_product;
        }

        public void setId_product(String id_product) {
            this.id_product = id_product;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }

        public List<Formula> getFormulas() {
            return formulas;
        }

        public void setFormuals(List<Formula> formulas) {
            this.formulas = formulas;
        }
    }


    public static class Attribute {
        private String id_product_attribute;
        private int count;

        public String getId_product_attribute() {
            return id_product_attribute;
        }

        public void setId_product_attribute(String id_product_attribute) {
            this.id_product_attribute = id_product_attribute;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class Formula {
        private String id_product_formula_item;
        private String id_product_item;
        private int count;

        public String getId_product_formula_item() {
            return id_product_formula_item;
        }

        public void setId_product_formula_item(String id_product_formula_item) {
            this.id_product_formula_item = id_product_formula_item;
        }

        public String getId_product_item() {
            return id_product_item;
        }

        public void setId_product_item(String id_product_item) {
            this.id_product_item = id_product_item;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
