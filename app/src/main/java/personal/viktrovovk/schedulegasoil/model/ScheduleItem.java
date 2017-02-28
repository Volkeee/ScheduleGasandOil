package personal.viktrovovk.schedulegasoil.model;

import java.io.Serializable;

/**
 * Created by Viktor on 23/02/2017.
 */

public class ScheduleItem implements Serializable {
    private String day;
    private Integer dayNum;
    private String lesson;
    private String week;
    private Integer weekParity;
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

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
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

    public Integer getWeekParity() {
        return weekParity;
    }

    public void setWeekParity(Integer weekParity) {
        this.weekParity = weekParity;
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
                ", dayNum=" + dayNum +
                ", lesson='" + lesson + '\'' +
                ", week='" + week + '\'' +
                ", weekParity=" + weekParity +
                ", subgroup='" + subgroup + '\'' +
                ", lessonType='" + lessonType + '\'' +
                ", discipline='" + discipline + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}
