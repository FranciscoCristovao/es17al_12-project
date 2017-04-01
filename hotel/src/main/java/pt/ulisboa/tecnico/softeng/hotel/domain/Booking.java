package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;
	private final String reference;
	private String cancellation_reference;
	private final LocalDate arrival;
	private final LocalDate departure;
	private LocalDate cancellation_date;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		checkArguments(hotel, arrival, departure);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure) {
		if (hotel == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	LocalDate getArrival() {
		return this.arrival;
	}

	LocalDate getDeparture() {
		return this.departure;
	}
	
	
	public void setCancellationReference(String reference){
		cancellation_reference=reference;
	}
	

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(this.arrival) || arrival.isAfter(this.arrival)) && arrival.isBefore(this.departure)) {
			return true;
		}

		if ((departure.equals(this.departure) || departure.isBefore(this.departure))
				&& departure.isAfter(this.arrival)) {
			return true;
		}

		if ((arrival.isBefore(this.arrival) && departure.isAfter(this.departure))) {
			return true;
		}

		return false;
	}

	public void setCancellationDate(LocalDate now) {
		cancellation_date=now;
		
	}

	public String getCancellationReference() {
		return cancellation_reference;
	}

	public LocalDate getCancellationDate(){
		return cancellation_date;
	}
}
