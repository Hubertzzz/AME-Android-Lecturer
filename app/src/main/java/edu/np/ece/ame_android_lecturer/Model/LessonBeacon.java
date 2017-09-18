package edu.np.ece.ame_android_lecturer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MIYA on 31/08/17.
 */
public class LessonBeacon {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("lesson_id")
    @Expose
    private String lesson_id;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
