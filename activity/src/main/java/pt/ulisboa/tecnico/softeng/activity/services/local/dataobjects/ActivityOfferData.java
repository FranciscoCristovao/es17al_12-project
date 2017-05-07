package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityOfferData {
	public static enum CopyDepth {
		SHALLOW, RESERVATIONS
	};
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end;
	private int capacity;
	private List<BookingData> bookings = new ArrayList<>();

	public ActivityOfferData(){}
	
	public ActivityOfferData(ActivityOffer activityOffer, CopyDepth depth){
		this.begin = activityOffer.getBegin();
		this.end = activityOffer.getEnd();
		this.capacity = activityOffer.getCapacity();
		switch (depth) {
		case RESERVATIONS:
			for (Booking booking : activityOffer.getBookingSet()) {
				this.bookings.add(new BookingData(booking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
		
		
	}
	
	public LocalDate getBegin(){
		return this.begin;
	}
	
	public LocalDate getEnd(){
		return this.end;
	}
	
	public int getCapacity(){
		return this.capacity;
	}

	
	public void setBegin(LocalDate date){
		this.begin = date;
	}
	
	public void setEnd(LocalDate date){
		this.end = date;
	}
	
	public void setCapacity(int i){
		this.capacity = i;
	}
	
	public List <BookingData> getBookings(){
		return bookings;
	} 
	
	public void setBookings(Set<Booking> bookings){
		List <BookingData> bookingsData = new ArrayList<BookingData>();
		for(Booking booking: bookings){
			bookingsData.add(new BookingData(booking));
		}
	} 
}
