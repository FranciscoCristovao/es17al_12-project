package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomConstructorMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
	}

	@Test
	public void success() {
		Room room = new Room(this.hotel, "01", Type.DOUBLE);

		Assert.assertEquals(this.hotel, room.getHotel());
		Assert.assertEquals("01", room.getNumber());
		Assert.assertEquals(Type.DOUBLE, room.getType());
		Assert.assertEquals(1, this.hotel.getNumberOfRooms());
	}
	@Test (expected = HotelException.class)
	public void hotelNull(){
		new Room(null, "01", Type.DOUBLE);
	}
	
	@Test (expected = HotelException.class)
	public void numberNull(){
		new Room(this.hotel, null, Type.DOUBLE);
	}
	
	@Test (expected = HotelException.class)
	public void typeNull(){
		new Room(this.hotel, "01", null);
	}
	
	@Test (expected = HotelException.class)
	public void numberExists(){
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel,"01",Type.DOUBLE);
	}
	
	@Test (expected = HotelException.class)
	public void hasNoNumbers(){
		new Room(this.hotel, "0A 3", Type.DOUBLE);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
