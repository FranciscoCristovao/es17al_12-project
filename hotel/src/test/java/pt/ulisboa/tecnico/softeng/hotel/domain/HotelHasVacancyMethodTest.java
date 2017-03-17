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
	
	@Test
	public void noVacancySameDate() {
		try{
			LocalDate arrival = new LocalDate(2016, 12, 19);
			LocalDate departure = new LocalDate(2016, 12, 21);
	
			Hotel.reserveHotel(Type.DOUBLE, arrival, departure);

			Room room1 = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

			Assert.assertEquals(null, room1);
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void noVacancySameDateBefore() {
		try{
			LocalDate arrival = new LocalDate(2016, 12, 19);
			LocalDate departure = new LocalDate(2016, 12, 21);
	
			Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
			LocalDate newarrival = new LocalDate(2016, 12, 17);
			LocalDate newdeparture = new LocalDate(2016, 12, 20);
			
			Room room1 = this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);

			Assert.assertEquals(null, room1);
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void noVacancySameDateAfter() {
		try{
			LocalDate arrival = new LocalDate(2016, 12, 19);
			LocalDate departure = new LocalDate(2016, 12, 21);
	
			Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
			LocalDate newarrival = new LocalDate(2016, 12, 20);
			LocalDate newdeparture = new LocalDate(2016, 12, 22);
			
			Room room1 = this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);

			Assert.assertEquals(null, room1);
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void noVacancySameDateOutside() {
		try{
			LocalDate arrival = new LocalDate(2016, 12, 19);
			LocalDate departure = new LocalDate(2016, 12, 21);
	
			Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
			LocalDate newarrival = new LocalDate(2016, 12, 18);
			LocalDate newdeparture = new LocalDate(2016, 12, 22);
			
			Room room1 = this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);

			Assert.assertEquals(null, room1);
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void noVacancySameDateInside() {
		try{
			LocalDate arrival = new LocalDate(2016, 12, 18);
			LocalDate departure = new LocalDate(2016, 12, 21);
	
			Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
			
			LocalDate newarrival = new LocalDate(2016, 12, 19);
			LocalDate newdeparture = new LocalDate(2016, 12, 20);
			
			Room room1 = this.hotel.hasVacancy(Type.DOUBLE, newarrival, newdeparture);

			Assert.assertEquals(null, room1);
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
