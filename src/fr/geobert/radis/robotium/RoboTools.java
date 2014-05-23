package fr.geobert.radis.robotium;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.robotium.solo.Solo;
import fr.geobert.radis.tools.Formater;

import java.util.ArrayList;
import java.util.Calendar;

class RoboTools {
    Solo solo;
    ActivityInstrumentationTestCase2 activity;

    RoboTools(Solo solo, ActivityInstrumentationTestCase2 activity) {
        this.solo = solo;
        this.activity = activity;
    }

    void printCurrentTextViews() {
        ArrayList<TextView> tvs = solo.getCurrentViews(TextView.class);
        for (int i = 0; i < tvs.size(); ++i) {
            TextView v = tvs.get(i);
            Log.i(RadisTest.TAG, "TextView " + i + ": " + v.getText());
        }
    }

    void printCurrentEditTexts() {
        ArrayList<EditText> tvs = solo.getCurrentViews(EditText.class);
        for (int i = 0; i < tvs.size(); ++i) {
            EditText v = tvs.get(i);
            Log.i(RadisTest.TAG, "EditText " + i + ": " + v.getText());
        }
    }

    void printCurrentButtons() {
        ArrayList<Button> tvs = solo.getCurrentViews(Button.class);
        for (int i = 0; i < tvs.size(); ++i) {
            TextView v = tvs.get(i);
            Log.i(RadisTest.TAG, "Buttons " + i + ": " + v.getText());
        }
    }

    void clickOnMenuItem(int textId) {
        String text = solo.getString(textId);
        solo.waitForText(text);
        solo.clickOnText(text);
    }

    void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void scrollUp() {
        while (solo.scrollUp()) ;
    }

    void scrollDown() {
        while (solo.scrollDown()) ;
    }

    String getDateStr(Calendar cal) {
        return Formater.getFullDateFormater().format(cal.getTime());
    }

    void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) activity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(solo.getEditText(0).getWindowToken(), 0);
    }

    int findIndexOfImageButton(int id) {
        Log.d("RoboTools", "findIndexOfImageButton wanted id : " + id);
        int res = -1;
        ArrayList<ImageButton> a = solo.getCurrentViews(ImageButton.class);
        for (int i = 0; i < a.size(); i++) {
            ImageButton b = a.get(i);
            Log.d("RoboTools", "findIndexOfImageButton id : " + b.getId());
            Log.d("RoboTools", "findIndexOfImageButton visibility : " + b.getVisibility());
        }

        int j = 0;
        for (int i = 0; i < a.size(); i++) {
            ImageButton b = a.get(i);
            if (b.getId() == id) {
                res = j;
                break;
            }
            if (b.getVisibility() == View.VISIBLE) {
                j++;
            }
        }
        Log.d("RoboTools", "findIndexOfImageButton : " + res);
        return res;
    }
}
