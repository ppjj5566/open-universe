package com.example.john.openmap.helper;

public class users_location_list {

    private String id;

    private String title_image;
    private String title;

    public users_location_list(String id, String title_image, String title) {
        this.id = id;
        this.title_image = title_image;
        this.title = title;
    }

    public String get_id(){
        return id;
    }

    public String get_title_image(){
        return title_image;
    }

    public String get_title(){
        return title;
    }


}
