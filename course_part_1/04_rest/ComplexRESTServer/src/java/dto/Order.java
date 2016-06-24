/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.Arrays;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author biar
 */

@XmlRootElement
public class Order {
    private int orderID;
    private int total;
    private String[] itemList;

    public Order() {
    }

    public Order(int id, int total, String[] items) {
        this.orderID = id;
        this.total = total;
        this.itemList = items;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getTotal() {
        return total;
    }

    public String[] getItemList() {
        return itemList;
    }

    public void setOrderID(int id) {
        this.orderID = id;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setItemList(String[] items) {
        this.itemList = items;
    }

    @Override
    public String toString() {
        return "Order " + this.orderID + " >> total: " + this.total + " , " + Arrays.toString(this.itemList);
    }
}
