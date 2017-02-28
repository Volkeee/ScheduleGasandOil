package personal.viktrovovk.schedulegasoil.model;

import java.io.Serializable;

/**
 * Created by Viktor on 23/02/2017.
 */

public class ScheduleItem implements Serializable {
    private String day;
    private String lesson;
    private String week;
    private String subgroup;
    private String lessonType;
    private String discipline;
    private String lecturer;
    private String classroom;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "day='" + day + '\'' +
                ", lesson='" + lesson + '\'' +
                ", week='" + week + '\'' +
                ", subgroup='" + subgroup + '\'' +
                ", lessonType='" + lessonType + '\'' +
                ", discipline='" + discipline + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}
