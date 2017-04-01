package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelCancelBookingMethodTest {
	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;
	private String reference;
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.reference=Hotel.reserveRoom(Type.DOUBLE, arrival, departure);
	}
	
	@Test(expected=HotelException.class)
	public void nullConfirmationReference(){
		Hotel.cancelBooking(null);
	}
	
	@Test(expected=HotelException.class)
	public void emptyConfirmationReference(){
		Hotel.cancelBooking("");
	}
	
	@Test(expected=HotelException.class)
	public void spacesConfirmationReference(){
		Hotel.cancelBooking("    ");
	}
	
	@Test
	public void nonExistentConfirmationReference(){
		try{
			Hotel.cancelBooking("doesntexist");
			Assert.fail();
			}
		catch(HotelException e){
			Assert.assertFalse(this.room.isFree(Type.DOUBLE,arrival,departure));
			Assert.assertEquals(this.room.getNumberOfBookings(),1);
		}
	}
	
	@Test
	public void duplicateCancelation(){
		try{
			Hotel.cancelBooking(reference);
			Hotel.cancelBooking(reference);
			Assert.fail();
			}
		catch(HotelException e){
			Assert.assertTrue(this.room.isFree(Type.DOUBLE,arrival,departure));
			Assert.assertEquals(this.room.getNumberOfBookings(),0);
			}
	}
	
	
	
	@Test
	public void SuccessfulCancellation(){
		String cancel = Hotel.cancelBooking(reference);
		Assert.assertTrue(this.room.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertEquals(this.room.getNumberOfBookings(),0);
		Assert.assertEquals(cancel,"CANCELLED"+reference);
	}
	
	
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}

