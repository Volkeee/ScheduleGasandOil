package personal.viktrovovk.schedulegasoil.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import personal.viktrovovk.schedulegasoil.model.SelectorItem;

/**
 * Created by Viktor on 23/02/2017.
 */

public class Parser {
//    <select name ="faculty_id" onchange="this\.form\.submit\(\);">([\s\S]*?)<\/select>
//    value="([0-9]+)"\s+>(.*?)<\/option>

//    <option value="0">Група<\/option>([\s\S]*?)<\/select>
//    value="([0-9]+)">(.*?)<\/option>

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
                    String upperString = selectorItemMatcher.group(2).trim().substring(0,1).toUpperCase() + selectorItemMatcher.group(2).trim().substring(1);
                    items.add(new SelectorItem(Integer.parseInt(selectorItemMatcher.group(1).trim()), upperString));
                }
            }
            Log.d("Parser", Integer.toString(items.size()).concat(" entries parsed"));
        }
        return items;
    }

   /* public static ArrayList<SelectorItem> parseGroupsSelector(String pageString) {
        ArrayList<SelectorItem> items = new ArrayList<>();
        Pattern groupsSelectorPattern = Pattern.compile("<option value=\"0\">Група<\\/option>([\\s\\S]*?)<\\/select>");
        Pattern groupsSelectorItemPattern = Pattern.compile("value=\"([0-9]+)\">(.*?)<\\/option>");

        Matcher groupsSelectorMatcher = groupsSelectorPattern.matcher(pageString);
        if (groupsSelectorMatcher.find()) {
            String groupSelectorHTML = groupsSelectorMatcher.group(1);

            Matcher groupsItemMatcher = groupsSelectorItemPattern.matcher(groupSelectorHTML);

            while(!groupsItemMatcher.hitEnd()) {
                if(groupsItemMatcher.hitEnd()) {
                    items.add(new SelectorItem(Integer.parseInt()))
                }
            }
        }

        return items;
    }*/
}
