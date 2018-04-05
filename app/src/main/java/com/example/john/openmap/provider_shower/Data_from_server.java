package com.example.john.openmap.provider_shower;

public class Data_from_server {

    private String story;
    private String image;
    private int context;
    private String owner_name;
    private String owner_info;

    //this is the contents of recycler view
    static final int TITLE = 0;
    static final int TITLE_IMAGE = 1;
    static final int STORY = 2;
    static final int IMAGE = 3;
    static final int OWNER_INFO = 4;
    static final int HEAD_INFO = 5;
    static final int LOG_OUT = 6;
    static final int AMP = 7;

    public Data_from_server(){
    }

    public Data_from_server(String story, String image, int context, String owner_name, String owner_info){
        this.story = story;
        this.context = context;
        this.image = image;
        this.owner_name = owner_name;
        this.owner_info = owner_info;
    }

    void set_Story(String story){
        this.story = story;
    }

    String get_Story(){
        return story;
    }

    void set_Context(int context){
        this.context = context;
    }

    int getContext(){
        return context;
    }

    void set_Image(String image){
        this.image = image;
    }

    String get_Image(){
        return image;
    }

    String getOwner_name(){
        return owner_name;
    }

    String getOwner_info(){
        return owner_info;
    }
}
