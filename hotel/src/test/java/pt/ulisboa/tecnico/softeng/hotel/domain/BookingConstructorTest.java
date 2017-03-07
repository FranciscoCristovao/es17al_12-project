package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {
	private Hotel hotel;
	
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres");
	}

	@Test
	public void success() {
		
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(hotel, arrival, departure);
		
		Assert.assertTrue(booking.getReference().startsWith(hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
		
	}
	
	@Test
	(expected = HotelException.class)
	public void arrivalAfterDeparture(){
		LocalDate arrival = new LocalDate(2016, 12, 21);
		LocalDate departure = new LocalDate(2016, 12, 19);
		
		new Booking(hotel, arrival, departure);
		
	}
	
	@Test
	public void SameArrivalAndDeparture(){
		LocalDate arrival = new LocalDate(2016, 12, 21);
		LocalDate departure = new LocalDate(2016, 12, 21);
		
		new Booking(hotel, arrival, departure);
		
	}
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
