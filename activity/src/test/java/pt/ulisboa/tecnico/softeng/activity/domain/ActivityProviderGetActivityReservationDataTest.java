package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderGetActivityReservationDataTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
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
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
	}
	
	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.getActivityReservationData("");
	}
	
	@Test(expected = ActivityException.class)
	public void blankReference() {
		ActivityProvider.getActivityReservationData("");
	}
	
	@Test
	public void success() {
		
		Booking booking = new Booking(this.provider, this.offer);
		
		ActivityReservationData activityReservationData = ActivityProvider.getActivityReservationData(booking.getReference());
		
		Assert.assertEquals(activityReservationData.getBegin(), this.offer.getBegin());
		Assert.assertEquals(activityReservationData.getEnd(), this.offer.getEnd());
		Assert.assertEquals(activityReservationData.getName(), this.activity.getName());
		Assert.assertEquals(activityReservationData.getCode(), this.activity.getCode());
		Assert.assertEquals(activityReservationData.getReference(), booking.getReference());

	}
	
}