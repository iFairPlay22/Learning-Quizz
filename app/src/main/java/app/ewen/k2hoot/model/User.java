package app.ewen.k2hoot.model;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    // Constructors
    public User() {
        this("");
    }

    public User(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    // Score
    private int mScore = 0;

    public int getScore(){
        return mScore;
    }

    public void setScore(int score){
        mScore = score;
    }

    // Name

    private String mFirstName;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }


    // Parcelable
    protected User(Parcel in) {
        mFirstName = in.readString();
        mScore = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeInt(mScore);
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
        return this.hashCode();
    }

    // Preferences
    private static final String SHARED_PREF_USER_FIRST_NAME = "SHARED_PREF_USER_FIRST_NAME";
    private static final String SHARED_PREF_USER_LAST_SCORE = "SHARED_PREF_USER_LAST_SCORE";

    public void storeInPreferences(SharedPreferences sharedPreferences) {
        sharedPreferences.edit()
                .putString(SHARED_PREF_USER_FIRST_NAME, mFirstName)
                .putInt(SHARED_PREF_USER_LAST_SCORE, mScore)
                .apply();
    }

    public void loadFromPreferences(SharedPreferences sharedPreferences) {
        mFirstName = sharedPreferences.getString(SHARED_PREF_USER_FIRST_NAME, mFirstName);
        mScore = sharedPreferences.getInt(SHARED_PREF_USER_LAST_SCORE, mScore);
    }

    // Bundle
    public static final String BUNDLE_STATE_USER = "BUNDLE_STATE_USER";

    public static User getUserFromBundle(Bundle inBundle) {
        if (inBundle == null) {
            return new User();
        } else {
            return inBundle.getParcelable(BUNDLE_STATE_USER);
        }
    }

    public void saveUserInBundle(Bundle outBundle) {
        outBundle.putParcelable(BUNDLE_STATE_USER, this);
    }
}
