package edu.uga.cs.hungerhero;

import java.io.Serializable;

public class DonationItem implements Serializable {
    private String id;
    private String userId;
    private String donorName;
    private String donorAddress;
    private String itemName;
    private String itemDescription;

    public DonationItem() {
        // Default constructor required for Firebase
    }

    public DonationItem(String id, String userId, String donorName, String donorAddress, String itemName, String itemDescription) {
        this.id = id;
        this.userId = userId;
        this.donorName = donorName;
        this.donorAddress = donorAddress;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(String donorAddress) {
        this.donorAddress = donorAddress;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}