package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		
		Iterator<Hotel> iterator = hotels.iterator();
		
		while(iterator.hasNext()){
			Hotel setElement = iterator.next();
			if(setElement.getCode() == code ){
				throw new HotelException();
			}
		}
		
		if (code == null || name == null || name.trim().length() == 0){
			throw new HotelException();
		}
		
		checkCode(code);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);

		
	}
	

	private void checkCode(String code) {
		if (code.trim().length() != Hotel.CODE_SIZE ) {
			throw new HotelException();
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if(type == null || arrival == null || departure == null){
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
			else{
				throw new HotelException();
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
		for(Room r : this.rooms){
			if(r.getNumber().equals(room.getNumber())){
				throw new HotelException();
			}
		}
		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public static String reserveHotel(Room.Type type, LocalDate arrival, LocalDate departure) {
		if(type == null || arrival == null || departure == null){
			throw new HotelException();
		}
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		return null;
	}

}
