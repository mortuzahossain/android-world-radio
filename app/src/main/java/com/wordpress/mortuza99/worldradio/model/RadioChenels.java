package com.wordpress.mortuza99.worldradio.model;

public class RadioChenels {
    String image;
    String name;
    String url;

    public RadioChenels() {
    }

    public RadioChenels(String image, String name, String url) {
        this.image = image;
        this.name = name;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
