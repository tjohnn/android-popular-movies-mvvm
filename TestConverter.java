package com.udacity.sandwichclub.utils;

/**
 * Created by Tjohn on 7/27/18.
 */

public class TestConverter {

    private String name;
    private String gender;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static String getTestData(){
        return "{'name':'Tosin Adeoye', 'gender':'Male', 'age':19}";
    }
}
