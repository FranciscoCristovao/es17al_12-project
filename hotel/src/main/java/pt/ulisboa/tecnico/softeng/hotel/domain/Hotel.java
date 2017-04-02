package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String roomConfirmation) {
		if( roomConfirmation==null || roomConfirmation.trim().equals("") ){
			throw new HotelException();
		}
		for(Hotel h : Hotel.hotels){
			for(Room r : h.rooms){
				for(Booking b : r.getBookings()){
					if(b.getReference().equals(roomConfirmation)){
						if(b.getCancellationReference()==null || b.getCancellationDate()==null){
							b.setCancellationReference("CANCELLED"+b.getReference());
							b.setCancellationDate(LocalDate.now());
							return b.getCancellationReference();
							}
						}
					}
				}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		if( reference==null || reference.trim().equals("") ){
			throw new HotelException();
		}
		for(Hotel hotel: Hotel.hotels){
			for(Room room: hotel.rooms){
				for (Booking booking: room.getBookings()){
					if (booking.getReference().equals(reference)){
						RoomBookingData bookInfo= new RoomBookingData();
						bookInfo.setReference(reference);
						bookInfo.setCancellation(booking.getCancellationReference());
						bookInfo.setHotelName(hotel.getName());
						bookInfo.setHotelCode(hotel.getCode());
						bookInfo.setRoomNumber(room.getNumber());
						bookInfo.setRoomType(room.getType().toString());
						bookInfo.setArrival(booking.getArrival());
						bookInfo.setDeparture(booking.getDeparture());
						bookInfo.setCancellationDate(booking.getCancellationDate());
						return bookInfo;
						
					}
				}
			}
		}
		
		throw new HotelException();
	}

	
	public int NumberRoomsAvailableByType(Type type, LocalDate arrival, LocalDate departure) {
		int numRoomsAvailable = 0;

		for (Room room : rooms) {
			if(room.isFree(type, arrival, departure)){
				numRoomsAvailable++;
			}
		}
		return numRoomsAvailable;

	}
	
	public static int NumberRoomsHotelsAvailableByType(Type type ,LocalDate arrival, LocalDate departure) {
		int numRoomsAvailable = 0;

		for (Hotel hotel : hotels) {
			numRoomsAvailable += hotel.NumberRoomsAvailableByType( type,arrival, departure);
		}
		return numRoomsAvailable;

	}
	
	
	
	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		if(number <= 0 || arrival == null || departure == null|| arrival.isAfter(departure)){
			throw new HotelException();
		}
		
		int numRoomsAvailableSingle = NumberRoomsHotelsAvailableByType(Type.SINGLE,arrival, departure);
		int numRoomsAvailableDouble = NumberRoomsHotelsAvailableByType(Type.DOUBLE,arrival, departure);

		if(number > (numRoomsAvailableSingle + numRoomsAvailableDouble)){
			throw new HotelException();
		}
		
		int i = 0;
		Set<String> roomsReserved = new HashSet<>();
		
		for(;( i < numRoomsAvailableDouble && i <number ); i++){
			String roomReserved = reserveRoom(Type.DOUBLE, arrival, departure);
			roomsReserved.add(roomReserved);
		}
		if(i < number){
			for(; i <number; i++){
				String roomReserved = reserveRoom(Type.SINGLE, arrival, departure);
				roomsReserved.add(roomReserved);
			}
		}
		
		return roomsReserved;
	}

}
