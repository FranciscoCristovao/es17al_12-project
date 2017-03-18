package pt.ulisboa.tecnico.softeng.activity.domain;

import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityMatchAgeMethodTest {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 3);
	}

	
	@Test(expected=ActivityException.class)
	public void negativeAge(){
		this.activity.matchAge(0);
	}
	
	@Test
	public void successInShortage() {
		Assert.assertTrue(this.activity.matchAge(18));
	}
	
	@Test
	public void successInExcess() {
		Assert.assertTrue(this.activity.matchAge(80));
	}
	@Test
	public void failureInExcess(){
		Assert.assertFalse(this.activity.matchAge(81));
	}
	
	@Test
	public void failureInShortage(){
		Assert.assertFalse(this.activity.matchAge(17));
	}
	

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
