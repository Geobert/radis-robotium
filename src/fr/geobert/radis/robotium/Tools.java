package fr.geobert.radis.robotium;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import fr.geobert.radis.tools.Formater;

import java.util.ArrayList;
import java.util.Calendar;

class Tools {
    Solo solo;
    ActivityInstrumentationTestCase2 activity;

    Tools(Solo solo, ActivityInstrumentationTestCase2 activity) {
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
        String text = getString(textId);
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

    void waitForListView() {
        solo.waitForView(ListView.class);
    }

    void scrollUp() {
        while (solo.scrollUp()) ;
    }

    void scrollDown() {
        while (solo.scrollDown()) ;
    }

    String getString(final int id) {
        return activity.getActivity().getString(id);
    }

    String getDateStr(Calendar cal) {
        return Formater.getFullDateFormater().format(cal.getTime());
    }

    void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) activity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(solo.getEditText(0).getWindowToken(), 0);
    }


}