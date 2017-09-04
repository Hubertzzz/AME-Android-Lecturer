package edu.np.ece.ame_android_lecturer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MIYA on 31/08/17.
 */

public class LoginResult {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("acad")
    @Expose
    private String acad;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("major")
    @Expose
    private String major;

    @SerializedName("minor")
    @Expose
    private String minor;

    @SerializedName("email")
    @Expose
    private String email;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcad() {
        return acad;
    }

    public void setAcad(String acad) {
        this.acad = acad;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
