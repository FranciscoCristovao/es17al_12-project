package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking extends Booking_Base{
	
	private static int counter = 0;


	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		setReference(provider.getCode() + Integer.toString(++Booking.counter));

		offer.addBooking(this);//modificar na relacao
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}
	}


	public String cancel() {
		setCancel("CANCEL" + this.getReference());
		setCancellationDate(new LocalDate());
		return this.getCancel();
	}

	public boolean isCancelled() {
		return this.getCancel() != null;
	}
	
	public void delete() {
		setActivityOffer(null);
		deleteDomainObject();
	}

}
