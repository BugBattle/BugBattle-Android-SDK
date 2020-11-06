package bugbattle.io.bugbattle.service;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bugbattle.io.bugbattle.BuildConfig;


/**
 * Read the log of the application.
 */
public class LogReader {

    private String formatDate(String time, String date) {
        String result = "";
        String[] splittedDate = date.split("-");

        result += Calendar.getInstance().get(Calendar.YEAR) + "-" + splittedDate[1] + "-" + splittedDate[0] + " " + time;
        return result;
    }

    /**
     * Reads the stacktrace, formats the string
     * @param context needed to read the packagename to filter logs
     * @return {@link JSONArray} formatted log
     */
    public JSONArray readLog(Context context) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            JSONArray log = new JSONArray();
            String line = "";
            Pattern pattern = Pattern.compile("^\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3}");
            while ((line = bufferedReader.readLine()) != null) {
                Matcher mt = pattern.matcher(line);
                if (mt.lookingAt()) {
                    String[] splittedLine = line.split(" ");
                    JSONObject object = new JSONObject();
                    object.put("date", formatDate(splittedLine[1], splittedLine[0]));
                    StringBuilder text = new StringBuilder();
                    for (int i = 7; i < splittedLine.length; i++) {
                        text.append(splittedLine[i]).append(" ");
                    }
                    object.put("log", text.toString());
                    log.put(object);
                }
            }
            Runtime.getRuntime().exec("logcat - c");

            return log;
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
