package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkOffer(offer);
		checkProvider(provider);
		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	private void checkOffer(ActivityOffer offer) {
		if (offer == null)
			throw new ActivityException();
		if (!offer.hasVacancy())
			throw new ActivityException();
	}
	
	private void checkProvider(ActivityProvider provider) {
		if (provider == null)
			throw new ActivityException();		
	}	

	public String getReference() {
		return this.reference;
	}
}
