package com.ahchim.android.ritto.model.storesearch;

/**
 * Created by Gold on 2017. 4. 17..
 */

public class Item
{
    private String id;

    private String phone;

    private String title;

    private String category;

    private String distance;

    private String newAddress;

    private String address;

    private String imageUrl;

    private String direction;

    private String zipcode;

    private String placeUrl;

    private String longitude;

    private String latitude;

    private String addressBCode;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getNewAddress ()
    {
        return newAddress;
    }

    public void setNewAddress (String newAddress)
    {
        this.newAddress = newAddress;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getImageUrl ()
    {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getDirection ()
    {
        return direction;
    }

    public void setDirection (String direction)
    {
        this.direction = direction;
    }

    public String getZipcode ()
    {
        return zipcode;
    }

    public void setZipcode (String zipcode)
    {
        this.zipcode = zipcode;
    }

    public String getPlaceUrl ()
    {
        return placeUrl;
    }

    public void setPlaceUrl (String placeUrl)
    {
        this.placeUrl = placeUrl;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public String getAddressBCode ()
    {
        return addressBCode;
    }

    public void setAddressBCode (String addressBCode)
    {
        this.addressBCode = addressBCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", phone = "+phone+", title = "+title+", category = "+category+", distance = "+distance+", newAddress = "+newAddress+", address = "+address+", imageUrl = "+imageUrl+", direction = "+direction+", zipcode = "+zipcode+", placeUrl = "+placeUrl+", longitude = "+longitude+", latitude = "+latitude+", addressBCode = "+addressBCode+"]";
    }
}
