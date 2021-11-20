package com.triplet.tripper.models;

public class User {
    public String name, email, favPlace, address;
    int petType = 4, age;

    public User(String name, String email) {
        this.email = email;
        this.name = name;
    }

    public User(String name, String email, String favPlace, String address, int petType, int age) {
        this.email = email;
        this.name = name;
        this.favPlace = favPlace;
        this.address = address;
        this.petType = petType;
        this.age = age;
    }

    public User() {
        petType = 4;
        name = null;
        email = null;
        favPlace = null;
        address = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setFavPlace(String favPlace) {
        this.favPlace = favPlace;
    }

    public void setPetType(int petType) {
        this.petType = petType;
    }

    public String getFavPlace() {
        return favPlace;
    }

    public int getPetType() {
        return petType;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
