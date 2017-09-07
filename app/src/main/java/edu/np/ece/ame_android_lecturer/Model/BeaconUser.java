package edu.np.ece.ame_android_lecturer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MIYA on 07/09/17.
 */

public class BeaconUser {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("major")
    @Expose
    private String major;

    @SerializedName("minor")
    @Expose
    private String minor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

}
