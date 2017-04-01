package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelGetRoomBookingDataMethodTest {
	
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private String reference;
	
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "13", Type.SINGLE);
	}
	
	
	@Test(expected=HotelException.class)
	public void nullReference(){
		Hotel.getRoomBookingData(null);
	}
	
	@Test(expected=HotelException.class)
	public void whiteSpaceReference(){
		Hotel.getRoomBookingData("     ");
	}
	@Test(expected=HotelException.class)
	public void emptyReference(){
		Hotel.getRoomBookingData("");
	}
	@Test(expected=HotelException.class)
	public void emptySetOfBookings() {
		Hotel.getRoomBookingData("4n1mp0551bl3T0M4tchStr1ng");
	}

	@Test(expected=HotelException.class)
	public void severalBookingsDoNotMatchOneHotel() {

		Hotel.reserveRoom(Type.DOUBLE, arrival, departure);

		Hotel.reserveRoom(Type.SINGLE, arrival, departure);

		Hotel.getRoomBookingData("4n1mp0551bl3T0M4tchStr1ng");
	}
	
	@Test(expected=HotelException.class)
	public void MoreRoomsDoNotMatchMoreHotels() {

		Hotel.reserveRoom(Type.DOUBLE, arrival, departure);
		
		Hotel newHotel = new Hotel("XPTO0456", "Atlantida");
		new Room(newHotel, "01", Type.SINGLE);
		new Room(newHotel, "13", Type.SINGLE);
		Hotel.reserveRoom(Type.SINGLE, arrival, departure);

		Hotel.getRoomBookingData("anImpossibleToMatchString");

	}
	
	@Test
	public void success(){

		this.reference=Hotel.reserveRoom(Type.SINGLE, arrival, departure);
		RoomBookingData BData= Hotel.getRoomBookingData(reference);
		Assert.assertEquals(BData.getReference(), this.reference);
		Assert.assertEquals(BData.getHotelName(), this.hotel.getName());
		Assert.assertEquals(BData.getHotelCode(), this.hotel.getCode());
		Assert.assertEquals(BData.getRoomNumber(), "13");
		Assert.assertEquals(BData.getRoomType(), Type.SINGLE);
		Assert.assertEquals(BData.getArrival(), this.arrival);
		Assert.assertEquals(BData.getDeparture(), this.departure);


	}
	
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}
