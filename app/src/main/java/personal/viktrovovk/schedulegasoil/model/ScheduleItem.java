package personal.viktrovovk.schedulegasoil.model;

import java.io.Serializable;

/**
 * Created by Viktor on 23/02/2017.
 */

public class ScheduleItem implements Serializable {
    private String day;
    private Integer dayIndex;
    private String lessonOrder;
    private String week;
    private String subgroup;
    private String lessonType;
    private String discipline;
    private String lecturer;
    private String auditory;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        switch (day) {
            case "Пн":
                dayIndex = 0;
                break;
            case "Вт":
                dayIndex = 1;
                break;
            case "Ср":
                dayIndex = 2;
                break;
            case "Чт":
                dayIndex = 3;
                break;
            case "Пт":
                dayIndex = 4;
                break;
        }

        this.day = day;
    }

    public Integer getDayIndex() {
        return dayIndex;
    }

    public String getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(String lessonOrder) {
        this.lessonOrder = lessonOrder;
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

    public String getAuditory() {
        return auditory;
    }

    public void setAuditory(String auditory) {
        this.auditory = auditory;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "day='" + day + '\'' +
                ", lessonOrder='" + lessonOrder + '\'' +
                ", week='" + week + '\'' +
                ", subgroup='" + subgroup + '\'' +
                ", lessonType='" + lessonType + '\'' +
                ", discipline='" + discipline + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", auditory='" + auditory + '\'' +
                '}';
    }
}
