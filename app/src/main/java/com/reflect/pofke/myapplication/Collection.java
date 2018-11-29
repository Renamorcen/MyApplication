package com.reflect.pofke.myapplication;

public class Collection {
    public Collection(String[] categories, String key) {
        this.categories = categories;
        this.key = key;
    }

    public String[] getCategories() {
        return categories;
    }

    public String getCategorie(int i) {

            return categories[i];
        }


    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String[] categories;
    private String key;
}
