package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderCancelReservationTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 1;
	private static final int AGE = 40;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	
	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);

		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
	}


	@Test(expected = ActivityException.class)
	public void nullActivityConfirmationReference() {
		ActivityProvider.cancelReservation(null);

	}

	@Test(expected = ActivityException.class)
	public void emptyActivityConfirmationReference() {
		ActivityProvider.cancelReservation("");

	}
	
	@Test(expected = ActivityException.class)
	public void blankActivityConfirmationReference() {
		ActivityProvider.cancelReservation("  ");

	}
	
	@Test
	public void NonExistantReservation() {
		try{
			ActivityProvider.reserveActivity(begin, end, AGE);
			ActivityProvider.cancelReservation("ABC2");
			Assert.fail();
		} catch(ActivityException e){
			Assert.assertEquals((provider.findOffer(begin, end, AGE)).size(), 0);
			Assert.assertEquals(this.offer.getNumberOfBookings(), 1);
		}
	}
	
	@Test
	public void DuplicateCancelReservation() {
		try{
			String resReference = ActivityProvider.reserveActivity(begin, end, AGE);
			Assert.assertEquals(this.offer.hasVacancy(), false);
			ActivityProvider.cancelReservation(resReference);
			Assert.assertEquals(this.offer.hasVacancy(), true);
			ActivityProvider.cancelReservation(resReference);
			Assert.fail();
		} catch(ActivityException e){
			Assert.assertEquals((provider.findOffer(begin, end, AGE)).size(), 1);
			Assert.assertEquals(this.offer.getNumberOfBookings(), 0);
		}
	}
	
	
	@Test
	public void success() {
		String resReference = ActivityProvider.reserveActivity(begin, end, AGE);
		Assert.assertEquals(this.offer.hasVacancy(), false);
		Assert.assertEquals((provider.findOffer(begin, end, AGE)).size(), 0);
		String cancelReference = ActivityProvider.cancelReservation(resReference);
		Assert.assertEquals(this.offer.getNumberOfBookings(), 0);
		Assert.assertEquals((provider.findOffer(begin, end, AGE)).size(), 1);
		Assert.assertEquals(this.offer.hasVacancy(), true);
		Assert.assertEquals("cancelled" + resReference, cancelReference);
	}

	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
