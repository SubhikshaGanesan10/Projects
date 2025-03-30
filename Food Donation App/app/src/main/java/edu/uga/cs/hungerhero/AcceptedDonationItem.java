package edu.uga.cs.hungerhero;

public class AcceptedDonationItem {
    private String receiverUsername;
    private String donorName;
    private String donorAddress;
    private String itemName;
    private String itemDescription;
    private String donorUserId;
    private String receiverUserId;

    public AcceptedDonationItem() {
        // Default constructor required for Firebase
    }

    public AcceptedDonationItem(String receiverUsername, String donorName, String donorAddress, String itemName, String itemDescription, String donorUserId, String receiverUserId) {
        this.receiverUsername = receiverUsername;
        this.donorName = donorName;
        this.donorAddress = donorAddress;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.donorUserId = donorUserId;
        this.receiverUserId = receiverUserId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
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

    public String getDonorUserId() {
        return donorUserId;
    }

    public void setDonorUserId(String donorUserId) {
        this.donorUserId = donorUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }
}