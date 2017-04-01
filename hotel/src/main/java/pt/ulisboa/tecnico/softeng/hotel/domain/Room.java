package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room {
	public static enum Type {
		SINGLE, DOUBLE
	}

	private final Hotel hotel;
	private final String number;
	private final Type type;
	private final Set<Booking> bookings = new HashSet<>();

	public Room(Hotel hotel, String number, Type type) {
		checkArguments(hotel, number, type);

		this.hotel = hotel;
		this.number = number;
		this.type = type;

		this.hotel.addRoom(this);
	}

	private void checkArguments(Hotel hotel, String number, Type type) {
		if (hotel == null || number == null || number.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (!number.matches("\\d*")) {
			throw new HotelException();
		}
	}

	Hotel getHotel() {
		return this.hotel;
	}

	String getNumber() {
		return this.number;
	}

	Type getType() {
		return this.type;
	}

	int getNumberOfBookings() {
		int cancellations=0;
		for(Booking b : this.getBookings()){
			if(b.getCancellationReference()!=null)
				cancellations++;
		}
		return this.getBookings().size()-cancellations;
	}

	

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(this.type)) {
			return false;
		}

		for (Booking booking : this.getBookings()) {
			if (booking.getCancellationReference()==null && booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (!isFree(type, arrival, departure)) {
			throw new HotelException();
		}
		
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}
		
		if(departure.compareTo(arrival) < 1){
			throw new HotelException();
		}
		
		Booking booking = new Booking(this.hotel, arrival, departure);
		this.getBookings().add(booking);

		return booking;
	}

	public Set<Booking> getBookings() {
		return bookings;
	}

}
