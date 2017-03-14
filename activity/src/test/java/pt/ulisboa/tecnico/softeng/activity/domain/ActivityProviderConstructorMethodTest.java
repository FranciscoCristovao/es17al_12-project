package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		Assert.assertEquals("Adventure++", provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}
	
	@Test(expected=ActivityException.class)
	public void nameNull(){
		ActivityProvider provider= new ActivityProvider("XtremX", null);
	}
	
	@Test(expected=ActivityException.class)
	public void codeNull(){
		ActivityProvider provider= new ActivityProvider(null, "Adventure++");
	}
	
	@Test(expected=ActivityException.class)
	public void nameEmpty(){
		ActivityProvider provider= new ActivityProvider("XtremX", "");
	}
	
	@Test(expected=ActivityException.class)
	public void biggerCode(){
		ActivityProvider provider= new ActivityProvider("xXtremXx", "Adventure++");
	}
	
	@Test(expected=ActivityException.class)
	public void smallerCode(){
		ActivityProvider provider= new ActivityProvider("XtrmX", "Adventure++");
	}
	
	@Test(expected=ActivityException.class)
	public void whiteSpaceCode(){
		ActivityProvider provider= new ActivityProvider("      ", "Adventure++");
	}
	
	@Test(expected=ActivityException.class)
	public void whiteSpaceCodeFail(){
		ActivityProvider provider= new ActivityProvider("   Xtrmx   ", "Adventure++");
	}
	
	@Test
	public void whiteSpaceCodeSuccess(){
		ActivityProvider provider= new ActivityProvider("   Xtremx   ", "Adventure++");
	}
	
	@Test(expected=ActivityException.class)
	public void whiteSpaceName(){
		ActivityProvider provider= new ActivityProvider("XtrmX", "  	  ");
	}
	
	@Test(expected=ActivityException.class)
	public void sameCode(){
		ActivityProvider provider1= new ActivityProvider("XtremX", "Adventure++");
		ActivityProvider provider2= new ActivityProvider("XtremX", "Adventure--");
		
	}
	
	@Test(expected=ActivityException.class)
	public void sameName(){
		ActivityProvider provider1= new ActivityProvider("XtremX", "Adventure++");
		ActivityProvider provider2= new ActivityProvider("RadAdv", "Adventure++");
	}
	
	
	

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
