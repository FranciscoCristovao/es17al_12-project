package pt.ulisboa.tecnico.softeng.hotel.services.local;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;

public class HotelInterface {
	
	@Atomic(mode = TxMode.WRITE)
	public static void createHotel(HotelData hotelData) {
		new Hotel(hotelData.getCode(), hotelData.getName() );
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createRoom(String hotelCode, RoomData roomData) {
		new Room(getHotelByCode(hotelCode), roomData.getNumber(), roomData.getType());
	}
	@Atomic(mode = TxMode.WRITE)
	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelBooking(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}
	
	@Atomic(mode = TxMode.READ)
	public static List<RoomData> getRooms(Hotel hotel, RoomData.CopyDepth depth) {	
		List<RoomData> listRooms = new ArrayList<>();
		for (Room room : hotel.getRoomSet()) {
			listRooms.add(new RoomData(room, hotel, depth));
		}
		return listRooms;		
	}

	@Atomic(mode = TxMode.READ)
	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			for (Room room : hotel.getRoomSet()) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		if (number < 1) {
			throw new HotelException();
		}

		List<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (int i = 0; i < number; i++) {
			references.add(rooms.get(i).reserve(rooms.get(i).getType(), arrival, departure).getReference());
		}

		return references;
	}

	static List<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		List<Room> availableRooms = new ArrayList<>();
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			availableRooms.addAll(hotel.getAvailableRooms(arrival, departure));
			if (availableRooms.size() >= number) {
				return availableRooms;
			}
		}
		return availableRooms;
	}
	
	
	
	private static Room getRoomByNumber(String code, String number) {
		
		for (Hotel hotel: FenixFramework.getDomainRoot().getHotelSet()) {
  			if (hotel.getCode().equals(code)) {
  				for(Room room: hotel.getRoomSet()){
  					if(room.getNumber().equals(number)){
  						return room;
  					}
  				}
  			}
  		}
		return null;
	}
	
	
	@Atomic(mode = TxMode.READ)
	public static RoomData getRoomDataByNumber(String code, String number, RoomData.CopyDepth depth) {
		
		Hotel hotel = getHotelByCode(code);
		Room room = getRoomByNumber(code, number);

		if (room != null) {
			return new RoomData(room, hotel, depth);
		} else {
			return null;
		}
	}

	@Atomic(mode = TxMode.READ)
	public static List<HotelData> getHotels() {
		List<HotelData> hotels = new ArrayList<>();
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotels.add(new HotelData(hotel, CopyDepth.SHALLOW));
		}
		return hotels;
	}
	
	private static Hotel getHotelByCode(String code) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getCode().equals(code)) {
				return hotel;
			}
		}
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	public static HotelData getHotelDataByCode(String code, HotelData.CopyDepth copyDepth) {
		Hotel hotel = getHotelByCode(code);
		if (hotel == null) {
			return null;
		}
		return new HotelData(hotel, copyDepth);
	}
	//APAGAR PARA O COMMIT
	@Atomic(mode = TxMode.WRITE)
	public static void createBooking(String code, String number) {
		Room room = getRoomByNumber(code, number);
		LocalDate arrival = room.getBooking(number).getArrival();
		LocalDate departure = room.getBooking(number).getDeparture();
		new Booking(room, arrival, departure);
		
	}

}
