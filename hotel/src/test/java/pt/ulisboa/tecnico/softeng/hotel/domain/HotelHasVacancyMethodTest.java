package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test (expected = HotelException.class)
	public void typeNull() {
	
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		this.hotel.hasVacancy(null, arrival, departure);
	}
	
	@Test (expected = HotelException.class)
	public void arrivalNull() {
	
		LocalDate departure = new LocalDate(2016, 12, 21);

		this.hotel.hasVacancy(Type.DOUBLE, null, departure);
	}
	
	@Test (expected = HotelException.class)
	public void departureNull() {
		LocalDate arrival = new LocalDate(2016, 12, 19);

		this.hotel.hasVacancy(Type.DOUBLE, arrival, null);
	}
	
	@Test
	public void hasVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals("01", room.getNumber());
	}
	
	@Test (expected = HotelException.class)
	public void noVacancySameDate() {

			LocalDate arrival = new LocalDate(2016, 12, 19);
			LocalDate departure = new LocalDate(2016, 12, 21);
	
			Hotel.reserveHotel(Type.DOUBLE, arrival, departure);

			this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);		
	}
	
	@Test (expected = HotelException.class)
	public void noVacancySameDateBefore() {
		
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);
	
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
		LocalDate newarrival = new LocalDate(2016, 12, 17);
		LocalDate newdeparture = new LocalDate(2016, 12, 20);
			
		this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);
	}
	
	@Test (expected = HotelException.class)
	public void noVacancySameDateAfter() {

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);
	
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
		LocalDate newarrival = new LocalDate(2016, 12, 20);
		LocalDate newdeparture = new LocalDate(2016, 12, 22);
			
		this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);
	}
	
	@Test (expected = HotelException.class)
	public void noVacancySameDateOutside() {

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);
	
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
		LocalDate newarrival = new LocalDate(2016, 12, 18);
		LocalDate newdeparture = new LocalDate(2016, 12, 22);
			
		this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);
	}
	
	@Test (expected = HotelException.class)
	public void noVacancySameDateInside() {

		LocalDate arrival = new LocalDate(2016, 12, 18);
		LocalDate departure = new LocalDate(2016, 12, 21);
	
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
		LocalDate newarrival = new LocalDate(2016, 12, 19);
		LocalDate newdeparture = new LocalDate(2016, 12, 20);
			
		this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
