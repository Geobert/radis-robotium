package fr.geobert.radis.robotium;
import com.jayway.android.robotium.solo.Solo;	
import android.test.ActivityInstrumentationTestCase2;	

@SuppressWarnings("unchecked")
public class RadisTest extends ActivityInstrumentationTestCase2 {
	private	static final String TARGET_PACKAGE_ID = "fr.geobert.radis";	
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "fr.geobert.radis.AccountList";	
	private static Class<?> launcherActivityClass;	
	static {	
		try {	
			launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);	
		} catch (ClassNotFoundException e) {	
			throw new RuntimeException(e);	
		}	
	}	

	public RadisTest() throws ClassNotFoundException { 
		super(TARGET_PACKAGE_ID, launcherActivityClass);	
	}	

	private Solo solo;	

	@Override	
	protected void setUp() throws Exception {	
		solo = new Solo(getInstrumentation(), getActivity());	
	}	

	public void testCanOpenSettings() {
		solo.pressMenuItem(0);
	}	

	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();	
		} catch (Throwable e) {	
			e.printStackTrace();	
		}	
		getActivity().finish();	
		super.tearDown();	
	}	
}
