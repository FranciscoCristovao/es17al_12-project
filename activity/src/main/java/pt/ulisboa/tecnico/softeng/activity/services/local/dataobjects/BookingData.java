package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class BookingData {
	private String reference;
	
	public BookingData(Booking b){
		this.reference = b.getReference();
	}
	
	public String getReference(){
		return reference;
	}
	
	public void setReference(String ref){
		this.reference = ref;
	}
}
