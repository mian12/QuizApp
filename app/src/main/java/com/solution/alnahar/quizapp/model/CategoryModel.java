package com.solution.alnahar.quizapp.model;

public class CategoryModel {

    private String Name;
    private String Image;

    public CategoryModel() {
    }

    public CategoryModel(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
