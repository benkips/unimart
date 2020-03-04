package com.mabnets.www.unimart;

public class details {
    private String username;
    private String phone;
    private String adress;

    public details(String username, String phone, String adress) {
        this.username = username;
        this.phone = phone;
        this.adress = adress;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return adress;
    }
}
