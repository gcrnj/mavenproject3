package com.mycompany.mavenproject3.models;

public class Owner {
    private int ownerId;
    private String name;
    private String contactInfo;

    public Owner(int ownerId, String name, String contactInfo) {
        this.ownerId = ownerId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}