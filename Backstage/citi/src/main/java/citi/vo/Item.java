package citi.vo;

import java.sql.Timestamp;

public class Item {
    private String ItemID;
    private String name;
    private String description;
    private String merchantID;
    private String logoURL;
    private double originalPrice;
    private int points;
    private Timestamp overdueTime;

    public Item(String itemID, String name, String description, String merchantID, String logoURL, double originalPrice, int points, Timestamp overdueTime) {
        ItemID = itemID;
        this.name = name;
        this.description = description;
        this.merchantID = merchantID;
        this.logoURL = logoURL;
        this.originalPrice = originalPrice;
        this.points = points;
        this.overdueTime = overdueTime;
    }

    public String getItemID() {
        return ItemID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public int getPoints() {
        return points;
    }

    public Timestamp getOverdueTime() {
        return overdueTime;
    }

}
