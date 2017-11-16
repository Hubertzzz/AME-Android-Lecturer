package edu.np.ece.ame_android_lecturer.OrmLite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by MIYA on 06/11/17.
 */

@DatabaseTable(tableName = "monitor")
public class Monitor {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String module;

    @DatabaseField
    private String subjectarea;

    @DatabaseField
    private String class_section;

    @DatabaseField
    private String lesson_date_id;

<<<<<<< HEAD

    @DatabaseField
    private String start_time;

    @DatabaseField
    private String end_time;

    @DatabaseField
    private String L_Date;

    @DatabaseField
    private String Uuid;
=======

    @DatabaseField (columnName = "Ldate")
    private String Ldate;
>>>>>>> refs/remotes/origin/master


    public Monitor(){

    }

    public Monitor(int id, String module, String subjectarea, String class_section, String lesson_date_id, String start_time, String end_time, String L_Date, String Uuid) {
        this.id = id;
        this.module = module;
        this.subjectarea = subjectarea;
        this.class_section = class_section;
        this.lesson_date_id = lesson_date_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.L_Date = L_Date;
        this.Uuid = Uuid;
    }

    public String getLdate() {
        return Ldate;
    }

    public void setLdate(String ldate) {
        Ldate = ldate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setSubjectarea(String subjectarea) {
        this.subjectarea = subjectarea;
    }

    public void setClass_section(String class_section) {
        this.class_section = class_section;
    }

    public void setLesson_date_id(String lesson_date_id) {
        this.lesson_date_id = lesson_date_id;
    }

    public void setStart_time(String start_time){
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time){
        this.end_time = end_time;
    }

    public void setL_Date(String L_Date){
        this.L_Date = L_Date;
    }

    public void setUuid(String Uuid){
        this.Uuid = Uuid;
    }

    public int getId() {
        return id;
    }

    public String getModule() {
        return module;
    }

    public String getSubjectarea() {
        return subjectarea;
    }

    public String getClass_section() {
        return class_section;
    }

    public String getLesson_date_id() {
        return lesson_date_id;
    }

    public String getStart_time(){
        return start_time;
    }

    public String getEnd_time(){
        return end_time;
    }

    public String getL_Date(){
        return L_Date;
    }

    public String getUuid(){
        return Uuid;
    }
}
