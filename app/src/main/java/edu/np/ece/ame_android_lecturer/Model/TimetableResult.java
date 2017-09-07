package edu.np.ece.ame_android_lecturer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by MIYA on 31/08/17.
 */

public class TimetableResult {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("lesson_id")
    @Expose
    private String lesson_id;

    @SerializedName("lecturer_id")
    @Expose
    private String lecturer_id;

    @SerializedName("lesson")
    @Expose
    private Lesson lesson;

    @SerializedName("venue")
    @Expose
    private Venue venue;

//    @SerializedName("lecturers")
//    @Expose
//    private Lecturer lecturers;

    @SerializedName("lesson_date")
    @Expose
    private LessonDate lesson_date;

    @SerializedName("beaconLesson")
    @Expose
    private LessonBeacon lessonBeacon;


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

    public  String getLecturer_id(){return lecturer_id;}

    public void  setLecturer_id(String lecturer_id){this.lecturer_id=lecturer_id;}

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonDate getLesson_date() {
        return lesson_date;
    }

    public void setLesson_date(LessonDate lesson_date) {
        this.lesson_date = lesson_date;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public LessonBeacon getLessonBeacon() {
        return lessonBeacon;
    }

    public void setLessonBeacon(LessonBeacon lessonBeacon) {
        this.lessonBeacon = lessonBeacon;
    }

//    public Lecturer getLecturers() {
//        return lecturers;
//    }
//
//    public void setLecturers(Lecturer lecturers) {
//        this.lecturers = lecturers;
//    }






}
