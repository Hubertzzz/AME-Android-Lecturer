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

    public Monitor(){

    }

    public Monitor(int id, String module, String subjectarea, String class_section, String lesson_date_id) {
        this.id = id;
        this.module = module;
        this.subjectarea = subjectarea;
        this.class_section = class_section;
        this.lesson_date_id = lesson_date_id;
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
}
