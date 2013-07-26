package fr.geobert.radis.robotium;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;
import com.jayway.android.robotium.solo.Solo;
import fr.geobert.radis.R;
import fr.geobert.radis.RadisConfiguration;
import fr.geobert.radis.tools.Formater;
import fr.geobert.radis.tools.Tools;
import fr.geobert.radis.ui.OperationListActivity;
import fr.geobert.radis.ui.ScheduledOpListActivity;
import fr.geobert.radis.ui.editor.AccountEditor;
import fr.geobert.radis.ui.editor.OperationEditor;
import fr.geobert.radis.ui.editor.ScheduledOperationEditor;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RadisTest extends ActivityInstrumentationTestCase2<OperationListActivity> {
    private static final int WAIT_DIALOG_TIME = 2000;

    private static final int CUR_ACC_NAME_IDX = 0;
    private static final int CUR_ACC_SUM_IDX = 2;
    public static final int FIRST_OP_SUM_IDX = 9;
    public static final int THIRD_PARTY_FIELD_IDX = 3;
    public static final int SUM_FIELD_IDX = 4;

    static String TAG = "RadisRobotium";
    static final String ACCOUNT_NAME = "Test";
    static final String ACCOUNT_START_SUM = "1000,50";
    static final String ACCOUNT_START_SUM_FORMATED_IN_EDITOR = "1 000,50";
    static final String ACCOUNT_START_SUM_FORMATED_ON_LIST = "1 000,50 €";
    static final String ACCOUNT_DESC = "Test Description";
    static final String ACCOUNT_NAME_2 = "Test2";
    static final String ACCOUNT_START_SUM_2 = "2000,50";
    static final String ACCOUNT_START_SUM_FORMATED_ON_LIST_2 = "2 000,50 €";
    static final String ACCOUNT_DESC_2 = "Test Description 2";
    static final String ACCOUNT_NAME_3 = "Test3";
    static final String OP_TP = "Operation 1";
    static final String OP_AMOUNT = "10.50";
    static final String OP_AMOUNT_FORMATED = "-10,50";
    static final String OP_TAG = "Tag 1";
    static final String OP_MODE = "Carte bleue";
    static final String OP_DESC = "Robotium Operation 1";
    static final String OP_AMOUNT_2 = "100";

    private Solo solo;
    private RoboTools tools;

    static {
        OperationListActivity.ROBOTIUM_MODE = true;
    }

    public RadisTest() {
        super(OperationListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
        this.tools = new RoboTools(solo, this);
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            Log.d(TAG, "tearDown !");
            solo.finishOpenedActivities();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /*
     * Operations Tests
	 */

    public void addAccount() {
        assertTrue(solo.waitForActivity(AccountEditor.class));
        solo.enterText(0, ACCOUNT_NAME);
        solo.enterText(1, ACCOUNT_START_SUM);
        solo.enterText(4, ACCOUNT_DESC);
        solo.clickOnActionBarItem(R.id.confirm);
        assertTrue(solo.waitForActivity(OperationListActivity.class));
        assertEquals(1, solo.getView(Spinner.class, 0).getCount());
        assertEquals(ACCOUNT_NAME, solo.getText(CUR_ACC_NAME_IDX).getText().toString());
        assertEquals(ACCOUNT_START_SUM_FORMATED_ON_LIST, solo.getText(CUR_ACC_SUM_IDX).getText().toString());
    }

    public void addAccount2() {
        solo.pressMenuItem(0);
        solo.waitForActivity(AccountEditor.class);
        solo.enterText(0, ACCOUNT_NAME_2);
        solo.enterText(1, ACCOUNT_START_SUM_2);
        solo.enterText(4, ACCOUNT_DESC_2);
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForActivity(OperationListActivity.class);
        assertEquals(2, solo.getView(Spinner.class, 0).getCount());
    }

    public void addAccount3() {
        solo.pressMenuItem(0);
        solo.waitForActivity(AccountEditor.class);
        solo.enterText(0, ACCOUNT_NAME_3);
        solo.enterText(1, ACCOUNT_START_SUM);
        solo.enterText(4, ACCOUNT_DESC);
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForActivity(OperationListActivity.class);
        assertEquals(3, solo.getView(Spinner.class, 0).getCount());
    }

    public void editAccount() {
        solo.pressMenuItem(1);
        solo.waitForActivity(AccountEditor.class);
        assertEquals(ACCOUNT_NAME, solo.getEditText(0).getText().toString());
        assertEquals(ACCOUNT_START_SUM_FORMATED_IN_EDITOR, solo.getEditText(1).getText().toString());
        assertEquals(ACCOUNT_DESC, solo.getEditText(4).getText().toString());
        tools.scrollUp();
        solo.clearEditText(0);
        solo.enterText(0, ACCOUNT_NAME_2);
        solo.clearEditText(1);
        solo.enterText(1, ACCOUNT_START_SUM_2);
        solo.clearEditText(4);
        solo.enterText(4, ACCOUNT_DESC_2);
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForActivity(OperationListActivity.class);
        // assertEquals(1, solo.getCurrentListViews().get(0).getCount());
        assertEquals(ACCOUNT_NAME_2, solo.getText(2).getText().toString());
        assertEquals(ACCOUNT_START_SUM_FORMATED_ON_LIST_2, solo.getText(3).getText().toString());
    }

    public void deleteAccount(int originalCount) {
        solo.pressMenuItem(2);
        solo.clickOnButton(solo.getString(fr.geobert.radis.R.string.yes));
        solo.waitForDialogToClose(WAIT_DIALOG_TIME);
        if (originalCount > 1) {
            assertEquals(originalCount - 1, solo.getCurrentViews(ListView.class).get(0).getCount());
        }
    }

    private void setUpOpTest() {
        addAccount();
    }

    public void addOp() {
        setUpOpTest();
        solo.pressMenuItem(0);
        solo.waitForActivity(OperationEditor.class);
        solo.enterText(3, OP_TP);
        for (int i = 0; i < OP_AMOUNT.length(); ++i) {
            solo.enterText(4, String.valueOf(OP_AMOUNT.charAt(i)));
        }
        solo.enterText(5, OP_TAG);
        solo.enterText(6, OP_MODE);
        solo.enterText(7, OP_DESC);
        solo.clickOnActionBarItem(R.id.confirm);
        assertTrue(solo.getText(1).getText().toString().contains(Formater.getSumFormater().format(990)));
        assertTrue(solo.getText(4).getText().toString()
                .equals(OP_AMOUNT_FORMATED));
    }

    public void addManyOps() {
        setUpOpTest();
        for (int j = 0; j < 10; ++j) {
            solo.clickOnActionBarItem(R.id.create_operation);
            solo.waitForActivity(OperationEditor.class);
            solo.enterText(3, OP_TP + j);
            solo.enterText(4, OP_AMOUNT_2);
            solo.enterText(5, OP_TAG);
            solo.enterText(6, OP_MODE);
            solo.enterText(7, OP_DESC);
            solo.clickOnActionBarItem(R.id.confirm);
            solo.waitForActivity(OperationListActivity.class);
            solo.waitForView(ListView.class);
        }
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains(Formater.getSumFormater().format(0.5)));
    }

    public void testEditOp() {
        TAG = "testEditOp";
        addManyOps();
        tools.sleep(1000);
        tools.scrollUp();
        solo.clickInList(5, 0);
        tools.sleep(2000);
        solo.clickOnImageButton(0);
        solo.waitForActivity(OperationEditor.class);
        solo.clearEditText(4);
        solo.enterText(4, "103");
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForActivity(OperationListActivity.class);
        tools.sleep(500);
        tools.printCurrentTextViews();
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains(Formater.getSumFormater().format(-2.5)));
    }

    public void testQuickAddFromOpList() {
        TAG = "testQuickAddFromOpList";
        addAccount();
        tools.printCurrentTextViews();
        assertTrue(
                solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains(Formater.getSumFormater().format(1000.50)));
        tools.printCurrentEditTexts();
        solo.enterText(0, "Toto");
        solo.enterText(1, "-1");
        solo.clickOnImageButton(0);
        tools.sleep(500);
        assertEquals(1, solo.getCurrentViews(ListView.class).get(0).getCount());
        assertTrue(
                solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains(Formater.getSumFormater().format(999.50)));
    }

    public void testDisableAutoNegate() {
        TAG = "testDisableAutoNegate";
        setUpOpTest();
        solo.clickOnActionBarItem(R.id.create_operation);
        solo.waitForActivity(OperationEditor.class);
        solo.enterText(THIRD_PARTY_FIELD_IDX, OP_TP);
        solo.enterText(SUM_FIELD_IDX, "+");
        for (int i = 0; i < OP_AMOUNT.length(); ++i) {
            solo.enterText(SUM_FIELD_IDX, String.valueOf(OP_AMOUNT.charAt(i)));
        }
        tools.printCurrentEditTexts();
        assertTrue(solo.getEditText(SUM_FIELD_IDX).getText().toString().equals("+10,50"));
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForActivity(OperationListActivity.class);
        tools.printCurrentTextViews();
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains("1 011,00"));
        tools.printCurrentTextViews();
        assertTrue(solo.getText(FIRST_OP_SUM_IDX).getText().toString().equals("10,50"));
    }

    /**
     * Schedule ops
     */


    private void setUpSchOp() {
        addAccount();
        solo.clickOnActionBarItem(R.id.go_to_preferences);
        solo.waitForActivity(RadisConfiguration.class);
        solo.clickOnText(solo.getString(R.string.prefs_insertion_date_label));
        solo.clearEditText(0);
        GregorianCalendar today = Tools.createClearedCalendar();
        today.add(Calendar.DAY_OF_MONTH, 1);
        solo.enterText(0, Integer.toString(today.get(Calendar.DAY_OF_MONTH)));
        solo.clickOnButton(solo.getString(R.string.ok));
        solo.goBack();
        solo.clickOnActionBarItem(R.id.go_to_sch_op);
        solo.waitForActivity(ScheduledOpListActivity.class);
        assertTrue(solo.waitForText(solo.getString(R.string.no_operation_sch)));
    }

    public void addScheduleOp() {
        setUpSchOp();
        solo.clickOnActionBarItem(R.id.create_operation);
        solo.waitForActivity(ScheduledOperationEditor.class);
        GregorianCalendar today = Tools.createClearedCalendar();
        solo.setDatePicker(0, today.get(Calendar.YEAR),
                today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        solo.enterText(3, OP_TP);
        solo.enterText(4, "9,50");
        solo.enterText(5, OP_TAG);
        solo.enterText(6, OP_MODE);
        solo.enterText(7, OP_DESC);
        tools.scrollUp();
        solo.clickOnText(solo.getString(R.string.scheduling));
        solo.pressSpinnerItem(0, 1);
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForView(ListView.class);
        assertEquals(1, solo.getCurrentViews(ListView.class).get(0).getCount());
        solo.goBack();
        solo.waitForActivity(OperationListActivity.class);
        solo.waitForView(ListView.class);
        // -1 is for "get more ops" line
        assertEquals(1, solo.getCurrentViews(ListView.class).get(0).getCount());
        tools.printCurrentTextViews();
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains("991,00"));
    }

    public void testEditScheduledOp() {
        TAG = "testEditScheduledOp";
        addScheduleOp();
        solo.clickInList(0);
        solo.clickOnImageButton(tools.findIndexOfImageButton(R.id.edit_op));
        solo.waitForActivity(ScheduledOperationEditor.class);
        solo.clearEditText(SUM_FIELD_IDX);
        solo.enterText(SUM_FIELD_IDX, "-7,50");
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForText(solo.getString(R.string.update));
        solo.clickOnButton(solo.getString(R.string.update));
        solo.waitForActivity(OperationListActivity.class);
        solo.waitForView(ListView.class);
        tools.printCurrentTextViews();
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains("993,00"));
    }
}
