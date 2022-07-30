package org.distributed.lock.model;

import java.io.Serializable;
import java.util.Date;

public class Stock implements Serializable {

    private int id;
    private String shopid;
    private String shopname;
    private int stock;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Stock(int id, String shopid, String shopname, int stock, Date created) {
        this.id = id;
        this.shopid = shopid;
        this.shopname = shopname;
        this.stock = stock;
        this.created = created;
    }

    public Stock(){}
}
