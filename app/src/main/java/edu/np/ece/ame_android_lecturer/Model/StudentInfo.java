package edu.np.ece.ame_android_lecturer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MIYA on 07/09/17.
 */

public class StudentInfo {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("card")
    @Expose
    private String card;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("acad")
    @Expose
    private String acad;

    @SerializedName("acad_level")
    @Expose
    private String acad_level;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("beacon_user")
    @Expose
    private BeaconUser beacon_ser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAcad() {
        return acad;
    }

    public void setAcad(String acad) {
        this.acad = acad;
    }

    public String getAcad_level() {
        return acad_level;
    }

    public void setAcad_level(String acad_level) {
        this.acad_level = acad_level;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BeaconUser getBeacon_ser() {
        return beacon_ser;
    }

    public void setBeacon_ser(BeaconUser beacon_ser) {
        this.beacon_ser = beacon_ser;
    }

}
