package com.rdr.rodrigocorvera.parcial1;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class Contact {

    private String name;
    private String number;
    private int photo;
    private boolean favorite;
    private int originalPosition;

    public Contact(String name, String number, boolean favorite) {
        this.name = name;
        this.number = number;
        this.favorite = favorite;
    }

    public Contact(String name, String number, boolean favorite, int originalPosition) {
        this.name = name;
        this.number = number;
        this.favorite = favorite;
        this.originalPosition = originalPosition;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
