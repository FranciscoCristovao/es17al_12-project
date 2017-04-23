package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room extends Room_Base{
	public static enum Type {
		SINGLE, DOUBLE
	}

	public Room(Hotel hotel, String number, Type type) {
		checkArguments(hotel, number, type);
		
		setNumber(number);
		setType(type);
		setHotel(hotel);

	}

	private void checkArguments(Hotel hotel, String number, Type type) {
		if (hotel == null || number == null || number.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (!number.matches("\\d*")) {
			throw new HotelException();
		}
	}
	
	@Override
	public void setHotel(Hotel h){
		if (h==null){
			super.setHotel(null);
		}
		else{
			h.addRoom(this);
		}
	}

	int getNumberOfBookings() {
		return this.getBookingSet().size();
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(getType())) {
			return false;
		}

		for (Booking booking : this.getBookingSet()) {
			if (booking.conflict(arrival, departure)) {
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

		Booking booking = new Booking(getHotel(), arrival, departure);
		super.addBooking(booking);

		return booking;
	}

	public Booking getBooking(String reference) {
		for (Booking booking : this.getBookingSet()) {
			if (booking.getReference().equals(reference)
					|| (booking.isCancelled() && booking.getCancellation().equals(reference))) {
				return booking;
			}
		}
		return null;
	}
	
	public void delete(){
		for (Booking b: this.getBookingSet()){
			b.delete();
		}
		setHotel(null);
		deleteDomainObject();
	}

}
