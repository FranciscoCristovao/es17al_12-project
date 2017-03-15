package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderFindOfferMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer));
	}
	
	@Test(expected=ActivityException.class)
	public void beginNull(){
		LocalDate begin = null;
		LocalDate end = new LocalDate(2016, 12, 21);
		
		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);
	}
	
	@Test(expected=ActivityException.class)
	public void endNull(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = null;
		
		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);
	}
	
	@Test(expected=ActivityException.class)
	public void impossibleAge(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		
		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 0);
	}
	
	@Test
	public void oneDateOff() {
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(0, offers.size());
	}
	
	@Test
	public void inappropriateAgeUp() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 81);

		Assert.assertEquals(0, offers.size());
	}
	
	
	@Test
	public void inappropriateAgeDown() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 17);

		Assert.assertEquals(0, offers.size());
	}
	
	@Test
	public void allGoodNoVacancy() {
		while(this.offer.hasVacancy())
			new Booking(this.provider, this.offer);
		
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(0, offers.size());
	}
	
	
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
	
	
}
