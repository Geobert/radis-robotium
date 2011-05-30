package fr.geobert.radis.robotium;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

import fr.geobert.radis.db.CommonDbAdapter;

@SuppressWarnings("unchecked")
public class AccountListTest extends ActivityInstrumentationTestCase2 {
	public final String TAG = "RadisRobotium";
	private static final String TARGET_PACKAGE_ID = "fr.geobert.radis";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "fr.geobert.radis.AccountList";
	private static final String ACCOUNT_NAME = "Test";
	private static final String ACCOUNT_START_SUM = "1000,50";
	private static final String ACCOUNT_START_SUM_FORMATED_IN_EDITOR = "1 000,50";
	private static final String ACCOUNT_START_SUM_FORMATED_ON_LIST= "1 000,50 €";
	private static final String ACCOUNT_DESC = "Test Description";
	private static final String ACCOUNT_NAME_2 = "Test2";
	private static final String ACCOUNT_START_SUM_2 = "2000,50";
	private static final String ACCOUNT_START_SUM_FORMATED_ON_LIST_2 = "2 000,50 €";
	private static final String ACCOUNT_DESC_2 = "Test Description 2";
	
	private static Class<?> launcherActivityClass;
	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public AccountListTest() throws ClassNotFoundException {
		super(TARGET_PACKAGE_ID, launcherActivityClass);
	}

	private Solo solo;

	@Override
	protected void setUp() throws Exception {
		Log.i(TAG, "setUp");
		trashDb();

		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testPreConditions() {
		Log.i(TAG, "testPreConditions");
		assertEquals(0, solo.getCurrentListViews().get(0).getCount());
	}

	public void testAddAccount() {
		Log.i(TAG, "testAddAccount");
		solo.pressMenuItem(0);
		solo.enterText(0, ACCOUNT_NAME);
		solo.enterText(1, ACCOUNT_START_SUM);
		solo.enterText(2, ACCOUNT_DESC);
		solo.clickOnText("Ok");
		assertEquals(1, solo.getCurrentListViews().get(0).getCount());
		assertTrue(ACCOUNT_NAME.equals(solo.getText(2).getText().toString()));
		assertTrue(ACCOUNT_START_SUM_FORMATED_ON_LIST.equals(solo.getText(3).getText().toString()));
	}

	public void testEditAccount() {
		Log.i(TAG, "testEditAccount");
		testAddAccount();
		solo.clickLongInList(0);
		solo.clickOnMenuItem("Modifier");
		assertTrue(ACCOUNT_NAME.equals(solo.getEditText(0).getText().toString()));
		assertTrue(ACCOUNT_START_SUM_FORMATED_IN_EDITOR.equals(solo.getEditText(1).getText().toString()));
		assertTrue(ACCOUNT_DESC.equals(solo.getEditText(2).getText().toString()));
		solo.clearEditText(0);
		solo.enterText(0, ACCOUNT_NAME_2);
		solo.clearEditText(1);
		solo.enterText(1, ACCOUNT_START_SUM_2);
		solo.clearEditText(2);
		solo.enterText(2, ACCOUNT_DESC_2);
		solo.clickOnText("Ok");
		assertEquals(1, solo.getCurrentListViews().get(0).getCount());
		assertTrue(ACCOUNT_NAME_2.equals(solo.getText(2).getText().toString()));
		assertTrue(ACCOUNT_START_SUM_FORMATED_ON_LIST_2.equals(solo.getText(3).getText().toString()));
	}
	
	public void testDeleteAccount() {
		
	}
	
	private void trashDb() {
		CommonDbAdapter db = CommonDbAdapter.getInstance(getActivity());
		db.trashDatabase();
	}

	@Override
	public void tearDown() throws Exception {
		Log.i(TAG, "tearDown");
		try {
			solo.finalize();
			trashDb();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}
}
