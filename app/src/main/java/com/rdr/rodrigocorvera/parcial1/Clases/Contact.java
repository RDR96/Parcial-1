package com.rdr.rodrigocorvera.parcial1.Clases;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class Contact {

    private String name;
    private String number;
    private ArrayList<String> numbers;
    private int photo;
    private boolean favorite;
    private int originalPosition;
    private Bitmap bitmap;
    private int favoritePosition;
    private boolean filter = false;
    private int filterPosition;

    public Contact(String name, String number, boolean favorite) {
        this.name = name;
        this.number = number;
        this.favorite = favorite;
    }

    public Contact(String name, String number, boolean favorite, int originalPosition, Bitmap bitmap) {
        this.name = name;
        numbers = new ArrayList<>();
        numbers.add(number);
        this.favorite = favorite;
        this.originalPosition = originalPosition;
        this.bitmap = bitmap;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public void setNumberById(int position, String number){
        if (position == 0){
            numbers.add(0,number);
        } else {
            numbers.add(1,number);
        }
    }

    public static Comparator<Contact> getStuNameComparator() {
        return StuNameComparator;
    }

    public static void setStuNameComparator(Comparator<Contact> stuNameComparator) {
        StuNameComparator = stuNameComparator;
    }

    public int getFilterPosition() {
        return filterPosition;
    }

    public void setFilterPosition(int filterPosition) {
        this.filterPosition = filterPosition;
    }

    public Contact(String name, String number, boolean favorite, int originalPosition, Bitmap bitmap, int favoritePosition) {
        this.name = name;
        numbers = new ArrayList<>();
        numbers.add(number);
        this.favorite = favorite;
        this.originalPosition = originalPosition;
        this.bitmap = bitmap;
        this.favoritePosition = favoritePosition;
    }

    public int getFavoritePosition() {
        return favoritePosition;
    }

    public void setFavoritePosition(int favoritePosition) {
        this.favoritePosition = favoritePosition;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(int originalPosition) {
        this.originalPosition = originalPosition;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

    public static Comparator<Contact> StuNameComparator = new Comparator<Contact>() {

        public int compare(Contact s1, Contact s2) {
            String StudentName1 = s1.getName().toUpperCase();
            String StudentName2 = s2.getName().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

}
