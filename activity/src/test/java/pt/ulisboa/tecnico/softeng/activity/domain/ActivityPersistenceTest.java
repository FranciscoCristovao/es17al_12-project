package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;


public class ActivityPersistenceTest {
	private static final String PROVIDER_CODE = "XtremX";
	private static final String PROVIDER_NAME = "Bush Walking";
	private ActivityProvider provider;

	
	
	@Before
	@Atomic(mode = TxMode.WRITE)
	public void setUp(){
	}
	@Test
	public void success() {
		atomicProcess();
		atomicActivityAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		provider = new ActivityProvider(PROVIDER_CODE,PROVIDER_NAME);
		
	}

	@Atomic(mode = TxMode.READ)
	public void atomicActivityAssert() {

		Set<ActivityProvider> providers = FenixFramework.getDomainRoot().getActivityProviderSet();
		provider = providers.iterator().next();
		
		/*ActivityProvider */
		assertEquals(1,providers.size());
		assertEquals(PROVIDER_CODE,provider.getCode());
		assertEquals(PROVIDER_NAME,provider.getName());
		
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}
}


