package com.example.lb1.Models;

import android.graphics.Bitmap;
import android.net.Uri;


public class ImageData {
    public int Id;
    public Bitmap Image;
    public String Name;
    public double Latitude;
    public double Longitude;

    public ImageData(int id, String name, double latitude, double longitude) {
        Id = id;
        Name = name;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public ImageData() {
    }
}
