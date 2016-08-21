package com.example.ousmane.movies.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ousmane on 8/20/16.
 */
public class Trailer implements Parcelable {

    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public Trailer(){ }

    public Trailer(Parcel in){
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.type = in.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.type);
    }

    public static final Creator CREATOR = new Creator() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
