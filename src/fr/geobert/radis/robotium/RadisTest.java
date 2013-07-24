package fr.geobert.radis.robotium;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.Spinner;
import com.jayway.android.robotium.solo.Solo;
import fr.geobert.radis.R;
import fr.geobert.radis.tools.Formater;
import fr.geobert.radis.ui.OperationListActivity;
import fr.geobert.radis.ui.editor.AccountEditor;

public class RadisTest extends ActivityInstrumentationTestCase2<OperationListActivity> {
    private static final int WAIT_DIALOG_TIME = 2000;

    private static final int CUR_ACC_NAME_IDX = 0;
    private static final int CUR_ACC_SUM_IDX = 2;

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

    static String OP_EDITOR = "OperationEditor";
    static String SCH_LIST = "ScheduledOpListActivity";
    static String SCH_EDITOR = "ScheduledOperationEditor";
    static String CFG_EDITOR = "RadisConfiguration";


    private Solo solo;
    private Tools tools;

    static {
        OperationListActivity.ROBOTIUM_MODE = true;
    }

    public RadisTest() {
        super(OperationListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        this.tools = new Tools(solo, this);
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
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
        solo.clickOnButton(tools.getString(fr.geobert.radis.R.string.yes));
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
        solo.waitForActivity(OP_EDITOR);
        solo.enterText(3, OP_TP);
        for (int i = 0; i < OP_AMOUNT.length(); ++i) {
            solo.enterText(4, String.valueOf(OP_AMOUNT.charAt(i)));
        }
        solo.enterText(5, OP_TAG);
        solo.enterText(6, OP_MODE);
        solo.enterText(7, OP_DESC);
        solo.clickOnActionBarItem(R.id.confirm);
        assertTrue(solo.getText(1).getText().toString().contains("990,00"));
        assertTrue(solo.getText(4).getText().toString()
                .equals(OP_AMOUNT_FORMATED));
    }

    public void addManyOps() {
        setUpOpTest();
        for (int j = 0; j < 10; ++j) {
            solo.clickOnActionBarItem(R.id.create_operation);
            solo.waitForActivity(OP_EDITOR);
            solo.enterText(3, OP_TP + j);
            solo.enterText(4, OP_AMOUNT_2);
            solo.enterText(5, OP_TAG);
            solo.enterText(6, OP_MODE);
            solo.enterText(7, OP_DESC);
            solo.clickOnActionBarItem(R.id.confirm);
            solo.waitForActivity(OperationListActivity.class);
            tools.waitForListView();
        }
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains("0,50"));
    }

    public void testEditOp() {
        TAG = "testEditOp";
        addManyOps();
        tools.sleep(1000);
        tools.scrollUp();
        solo.clickInList(5, 0);
        tools.sleep(2000);
        solo.clickOnImageButton(0);
        solo.waitForActivity(OP_EDITOR);
        solo.clearEditText(4);
        solo.enterText(4, "103");
        solo.clickOnActionBarItem(R.id.confirm);
        solo.waitForActivity(OperationListActivity.class);
        tools.sleep(500);
        tools.printCurrentTextViews();
        assertTrue(solo.getText(CUR_ACC_SUM_IDX).getText().toString().contains("-2,50"));
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
}
