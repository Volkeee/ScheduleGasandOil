package personal.viktrovovk.schedulegasoil.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import personal.viktrovovk.schedulegasoil.model.ScheduleItem;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;

/**
 * Created by Viktor on 23/02/2017.
 */

public class Parser {
//    <select name ="faculty_id" onchange="this\.form\.submit\(\);">([\s\S]*?)<\/select>
//    value="([0-9]+)"\s+>(.*?)<\/option>

//    <option value="0">Група<\/option>([\s\S]*?)<\/select>
//    value="([0-9]+)">(.*?)<\/option>

    //    (<td>(..*?)<\/td>)<\/tr>
//    <tr  id=".*">\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>\s+<td>(..*?)<\/td>
//    <tr  id="day2">\s+<td>(..*?)<\/td>
    public static ArrayList<SelectorItem> parseSelector(String pageString, String tagStartEndRegex) {
        String itemRegex = "value=\"([0-9]+)\"\\s+>(.*?)<\\/option>";
        ArrayList<SelectorItem> items = new ArrayList<>();

        Pattern facultySelectorPattern = Pattern.compile(tagStartEndRegex);
        Pattern facultySelectorItemPattern = Pattern.compile(itemRegex);

        Matcher facultySelectorMatcher = facultySelectorPattern.matcher(pageString);
        if (facultySelectorMatcher.find()) {
            String facultySelectorHTML = facultySelectorMatcher.group(1);

            Matcher selectorItemMatcher = facultySelectorItemPattern.matcher(facultySelectorHTML);

            while (!selectorItemMatcher.hitEnd()) {
                if (selectorItemMatcher.find()) {
                    String upperString = selectorItemMatcher.group(2).trim().substring(0, 1).toUpperCase() + selectorItemMatcher.group(2).trim().substring(1);
                    items.add(new SelectorItem(Integer.parseInt(selectorItemMatcher.group(1).trim()), upperString));
                }
            }
            Log.d("Parser", Integer.toString(items.size()).concat(" entries parsed"));
        }
        return items;
    }

    public static ArrayList<ScheduleItem> parseSchedule(String pageString) {
        String itemRegex = "<tr  id=\".*\">\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>\\s+<td>(..*?)<\\/td>";
        ArrayList<ScheduleItem> items = new ArrayList<>();

        Pattern schedulePattern = Pattern.compile(itemRegex);

        Matcher scheduleMatcher = schedulePattern.matcher(pageString);
        while (!scheduleMatcher.hitEnd()) {
            if (scheduleMatcher.find()) {
                ScheduleItem scheduleItem = new ScheduleItem();
                scheduleItem.setDay(scheduleMatcher.group(1).trim());
                scheduleItem.setLessonOrder(scheduleMatcher.group(2).trim());
                scheduleItem.setWeek(scheduleMatcher.group(3).trim());
                scheduleItem.setSubgroup(scheduleMatcher.group(4).trim());
                scheduleItem.setLessonType(scheduleMatcher.group(5).trim());
                scheduleItem.setDiscipline(scheduleMatcher.group(6).trim());
                scheduleItem.setLecturer(scheduleMatcher.group(7).trim());
                scheduleItem.setAuditory(scheduleMatcher.group(8).trim());
                items.add(scheduleItem);
            }
        }
        Log.d("Parser", Integer.toString(items.size()) + " schedule items parsed");

        return items;
    }
}
