package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class HotelPersistenceTest {
	private static final String HOTEL_CODE = "BK01234";
	private static final String ROOM_NUMBER = "007";
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private  String cancelledRef;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel= new Hotel(HOTEL_CODE, "Money");
		
		Room room = new Room(hotel, "007", Room.Type.SINGLE);

		Booking book = room.reserve(Room.Type.SINGLE, arrival, departure);
		cancelledRef = book.cancel();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Hotel hotel = Hotel.getHotelByCode(HOTEL_CODE);
		
		assertNotNull(hotel);
		assertEquals("Money", hotel.getName());
		
		assertEquals(1, hotel.getRoomSet().size());
		List<Room> rooms= new ArrayList<>(hotel.getRoomSet());
		Room room = rooms.get(0);
		
		assertNotNull(room);
		assertEquals(ROOM_NUMBER, room.getNumber());
		assertEquals(hotel, room.getHotel());
		assertEquals(Room.Type.SINGLE, room.getType());
		
		
		assertEquals(1, room.getBookingSet().size());
		List<Booking> bookings = new ArrayList<>(room.getBookingSet());
		Booking booking = bookings.get(0);
		
		assertNotNull(booking);
		assertEquals(arrival, booking.getArrival());
		assertEquals(departure, booking.getDeparture());
		assertEquals(HOTEL_CODE + booking.getCounter(), booking.getReference());
		assertEquals(this.cancelledRef, booking.getCancellation());
		assertNotNull(booking.getCancellationDate());
		

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}