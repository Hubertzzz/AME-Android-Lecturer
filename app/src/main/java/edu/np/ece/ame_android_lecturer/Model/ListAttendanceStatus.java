package edu.np.ece.ame_android_lecturer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MIYA on 05/09/17.
 */

public class ListAttendanceStatus {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("student_id")
    @Expose
    private String student_id;

    @SerializedName("lesson_date_id")
    @Expose
    private String lesson_date_id;

    @SerializedName("recorded_time")
    @Expose
    private String recorded_time;

    @SerializedName("lecturer_id")
    @Expose
    private String lecturer_id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("lesson_date")
    @Expose
    private LessonDate lessonDate;

    @SerializedName("lesson")
    @Expose
    private Lesson lesson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getLesson_date_id() {
        return lesson_date_id;
    }

    public void setLesson_date_id(String lesson_date_id) {
        this.lesson_date_id = lesson_date_id;
    }

    public String getRecorded_time() {
        return recorded_time;
    }

    public void setRecorded_time(String recorded_time) {
        this.recorded_time = recorded_time;
    }

    public String getLecturer_id() {
        return lecturer_id;
    }

    public void setLecturer_id(String lecturer_id) {
        this.lecturer_id = lecturer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LessonDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LessonDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
