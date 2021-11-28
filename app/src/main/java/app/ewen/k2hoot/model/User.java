package app.ewen.k2hoot.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String mFirstName;
    private int mScore = 0;
    public User() {
        this("");
    }

    public User(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public int getScore(){
        return mScore;
    }

    public void setScore(int score){
        mScore = score;
    }
    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    protected User(Parcel in) {
        mFirstName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
    }
}
