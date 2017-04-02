package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelBulkBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.DOUBLE);
		new Room(this.hotel, "03", Type.SINGLE);
		new Room(this.hotel, "04", Type.SINGLE);
		new Room(this.hotel, "05", Type.SINGLE);

	}

	@Test
	public void bulkBooking() {
		Set<String> result = hotel.bulkBooking(3, arrival, departure);
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
	}
	
	@Test(expected = HotelException.class)
	public void numberZero(){
		hotel.bulkBooking(0, arrival, departure);
	}
	
	
	@Test(expected = HotelException.class)
	public void arrivalNull(){
		hotel.bulkBooking(3, null, departure);
	}
	
	
	@Test(expected = HotelException.class)
	public void departureNull(){
		hotel.bulkBooking(3, arrival, null);
	 }
	
	@Test(expected = HotelException.class)
	public void noBulkBooking() {
		Set<String> result = hotel.bulkBooking(7, arrival, departure);
		Assert.assertNull(result);
	}
	
	@Test(expected = HotelException.class)
	public void arrivalAfterDeparture() {
		Set<String> result = hotel.bulkBooking(7,departure, arrival );
		Assert.assertNull(result);
	}
	
	@Test(expected = HotelException.class)
	public void noVacancyAfterBulkBooking(){
		hotel.bulkBooking(5, arrival, departure);
		Hotel.reserveRoom(Type.SINGLE, arrival, departure);
	}
	
	@Test
	public void vacancyAfterBulkBooking(){
		hotel.bulkBooking(2, arrival, departure);
		Hotel.reserveRoom(Type.SINGLE, arrival, departure);
	}
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
