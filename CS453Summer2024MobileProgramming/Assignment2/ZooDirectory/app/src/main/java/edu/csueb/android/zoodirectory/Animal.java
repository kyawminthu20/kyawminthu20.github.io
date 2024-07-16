package edu.csueb.android.zoodirectory;


import android.os.Parcel;
import android.os.Parcelable;

public class Animal implements Parcelable {
    private String name;
    private int imageResource;
    private String description;

    public Animal(String name, int imageResource, String description) {
        this.name = name;
        this.imageResource = imageResource;
        this.description = description;
    }

    protected Animal(Parcel in) {
        name = in.readString();
        imageResource = in.readInt();
        description = in.readString();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageResource);
        dest.writeString(description);
    }
}
