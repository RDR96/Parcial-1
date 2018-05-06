package com.rdr.rodrigocorvera.parcial1;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class Contact {

    private String name;
    private String number;
    private int photo;


    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
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
