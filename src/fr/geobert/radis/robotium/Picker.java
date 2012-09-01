package fr.geobert.radis.robotium;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.jayway.android.robotium.solo.Solo;

/**
 * RobotiumTestCustomer provides user actions drive the application under test.
 * Using an instance of RobotiumTestCustomer a test suite can click, scroll,
 * enter text, retrieve, and press views in the appication being tested.
 * 
 * RobotiumTestCustomer extends Robotium's Solo class to provide its
 * functionality. RobotiumTestCustomer also provides a home for addition global
 * testing actions.
 */
public class Picker {

    private static final String AM = "AM";
    private static final String PM = "PM";
    private static final String SET = "Set";

    private static final int HOUR_PLUS_BUTTON_INDEX = 0;
    private static final int HOUR_MINUS_BUTTON_INDEX = 1;
    private static final int MINUTE_PLUS_BUTTON_INDEX = 2;
    private static final int MINUTE_MINUS_BUTTON_INDEX = 3;
    private static final int MONTH_PLUS_BUTTON_INDEX = 2;
    private static final int MONTH_MINUS_BUTTON_INDEX = 3;
    private static final int DAY_PLUS_BUTTON_INDEX = 0;
    private static final int DAY_MINUS_BUTTON_INDEX = 1;
    private static final int YEAR_PLUS_BUTTON_INDEX = 4;
    private static final int YEAR_MINUS_BUTTON_INDEX = 5;
    
    private final Solo solo;

    public Picker(Solo solo) {
        this.solo = solo;
    }

    /**
     * Calculates the minimum number of clicks needed to reach the target. A
     * positive number means click forward, while a negative number means click
     * backward.
     * 
     * @param current
     * @param target
     * @param max maximum number of values for the picker
     * @return minimum number of clicks to reach the target
     */
    private int calculateClicks(int current, int target, int max) {
    	Log.d("Robotium", String.format("%d/%d/%d", current, target, max));
		double mid = max / 2D;
        int clicks = target - current;
        if (clicks < -mid)
            clicks = max + target - current;
        else if (clicks > mid) clicks = target - current - max;
        return clicks;
    }

    /**
     * Adjusts the current date clicker dialog to the specified date by clicking
     * on indiviual controls. Months are zero-based, January = 0, February = 1,
     * etc. See java.util.Calendar.
     * 
     * @param monthOfYear
     * @param dayOfMonth
     * @param year
     */
    public void clickOnDatePicker(int monthOfYear, int dayOfMonth, int year) {
        //solo.waitForText(SET);
    	solo.waitForView(DatePicker.class);
    	Log.d("RadisRobotium", String.format("%d/%d/%d", monthOfYear, dayOfMonth, year));
        DatePicker picker = getDatePicker();
        if (picker == null) return;

        int monthClicks = calculateClicks(picker.getMonth(), monthOfYear, 12);
        Log.d("RadisRobotium", "monthClicks :" + monthClicks);
        clickOnPickerButton(monthClicks, MONTH_PLUS_BUTTON_INDEX, MONTH_MINUS_BUTTON_INDEX);

        int yearClicks = calculateClicks(picker.getYear(), year, 9999);
        clickOnPickerButton(yearClicks, YEAR_PLUS_BUTTON_INDEX, YEAR_MINUS_BUTTON_INDEX);

        int dayClicks = calculateClicks(picker.getDayOfMonth(), dayOfMonth, lastDayOfMonth(monthOfYear, year));
        clickOnPickerButton(dayClicks, DAY_PLUS_BUTTON_INDEX, DAY_MINUS_BUTTON_INDEX);

        //solo.clickOnButton(SET);
    }

    /**
     * Clicks on an indiviual picker buttons, either + or -.
     * 
     * @param plusButtonIndex
     * @param currentNumber
     * @param targetNumber
     */
    private void clickOnPickerButton(int clicks, int plusButtonIndex, int minusButtonIndex) {
        int buttonIndex = (clicks < 0) ? minusButtonIndex : plusButtonIndex;
        for (int i = 0; i < Math.abs(clicks); i++) {
            solo.clickOnImageButton(buttonIndex);
        }
    }

    /**
     * Adjusts the current time clicker dialog to the specified time by clicking
     * on indiviual controls.
     * 
     * @param hour
     * @param minute
     */
    public void clickOnTimePicker(int hour, int minute) {
        solo.waitForText(SET);
        TimePicker picker = getTimePicker();
        if (picker == null) return;

        int targetHour = convertTo12Hour(hour);
        int currentHour = convertTo12Hour(picker.getCurrentHour().intValue());
        int hourClicks = calculateClicks(currentHour, targetHour, 12);
        clickOnPickerButton(hourClicks, HOUR_PLUS_BUTTON_INDEX, HOUR_MINUS_BUTTON_INDEX);

        int minuteClicks = calculateClicks(picker.getCurrentMinute().intValue(), minute, 60);
        clickOnPickerButton(minuteClicks, MINUTE_PLUS_BUTTON_INDEX, MINUTE_MINUS_BUTTON_INDEX);

        String pickerMeridiem = solo.getButton(0).getText().toString();
        if (hour > 11) {
            if (AM.equals(pickerMeridiem)) solo.clickOnButton(AM);
        } else if (PM.equals(pickerMeridiem)) solo.clickOnButton(PM);

        //solo.clickOnButton(SET);
    }

    private int convertTo12Hour(int hour) {
        return (hour == 12) ? 12 : hour % 12;
    }

    /**
     * For the current activity, returns list of views of the specified class or
     * any of its subclasses.
     * 
     * This method mimics the behavior of Robotium's
     * ViewFetcher.getCurrentViews() method. However, Robotium's Solo class does
     * not expose this method. This method is intended find subclasses of views
     * which Robotium does not address.
     * 
     * @param filterClass - class to filter views by
     * @return list of views filter by the class specified
     */
    public <E extends View> List<E> getCurrentViews(Class<E> filterClass) {
        List<View> views = solo.getViews();
        List<E> selectedViews = new ArrayList<E>();
        for (View view : views) {
            if (filterClass.isAssignableFrom(view.getClass())) {
                selectedViews.add(filterClass.cast(view));
            }
        }
        return selectedViews;
    }

    /**
     * Returns the current date picker, if it exists; otherwise null
     * 
     * @return date picker instance
     */
    private DatePicker getDatePicker() {
        List<DatePicker> pickers = getCurrentViews(DatePicker.class);
        if (pickers.size() == 0) return null;
        return pickers.get(0);
    }

    /**
     * Returns the current time picker, if it exists; otherwise null
     * 
     * @return time picker instance
     */
    private TimePicker getTimePicker() {
        List<TimePicker> pickers = getCurrentViews(TimePicker.class);
        if (pickers.size() == 0) return null;
        return pickers.get(0);
    }

    private int lastDayOfMonth(int monthOfYear, int year) {
        Calendar targetDate = new GregorianCalendar(year, monthOfYear, 1);
        return targetDate.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}