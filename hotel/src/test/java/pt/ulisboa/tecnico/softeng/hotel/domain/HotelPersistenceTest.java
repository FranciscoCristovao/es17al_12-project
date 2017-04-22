package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel= new Hotel(HOTEL_CODE, "Money");
		
		new Room(hotel, "007", Room.Type.SINGLE);
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
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}