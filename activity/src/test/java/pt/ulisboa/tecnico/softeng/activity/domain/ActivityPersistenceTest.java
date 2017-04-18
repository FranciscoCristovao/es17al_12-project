package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;


public class ActivityPersistenceTest {
	private static final String PROVIDER_CODE = "XtremX";
	private static final String PROVIDER_NAME = "Bush Walking";
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 50;
	private static final int CAPACITY = 30;
	private ActivityProvider provider;
	private Activity activity;

	
	
	@Before
	@Atomic(mode = TxMode.WRITE)
	public void setUp(){
		provider = new ActivityProvider(PROVIDER_CODE,PROVIDER_NAME);
	}
	@Test
	public void success() {
		atomicProcess();
		atomicActivityAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		activity = new Activity(this.provider, PROVIDER_NAME, MIN_AGE, MAX_AGE, CAPACITY);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicActivityAssert() {
		Set<ActivityProvider> providers = FenixFramework.getDomainRoot().getActivityProviderSet();
		for(ActivityProvider p : providers){
			provider = p;
		}
		Set<Activity> activities = provider.getActivitySet();
		for(Activity a : activities){
			activity = a;
		}
		assertEquals(PROVIDER_NAME, activity.getActivityProvider().getName());
		assertEquals(PROVIDER_CODE, activity.getActivityProvider().getCode());
		assertEquals(PROVIDER_NAME, activity.getName());
		Assert.assertTrue(activity.getCode().startsWith(PROVIDER_CODE));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		assertEquals(MIN_AGE, activity.getMinAge());
		assertEquals(MAX_AGE, activity.getMaxAge());
		assertEquals(CAPACITY, activity.getCapacity());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}
}


