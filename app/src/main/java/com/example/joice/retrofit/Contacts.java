package com.example.joice.retrofit;

import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


public class Contacts {

    String name;
    int age;

    public Contacts(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("age")
    public int getAge() {
        return age;
    }
}
