package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Activity extends Activity_Base{
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 100;

	private static int counter = 0;

	public Activity(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		checkArguments(provider, name, minAge, maxAge, capacity);

		setCode(provider.getCode() + Integer.toString(++Activity.counter));
		setName(name);
		setMinAge(minAge);
		setMaxAge(maxAge);
		setCapacity(capacity);

		setActivityProvider(provider);
	}
	
	
	private void checkArguments(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		if (provider == null || name == null || name.trim().equals("")) {
			throw new ActivityException();
		}

		if (minAge < MIN_AGE || maxAge >= MAX_AGE || minAge > maxAge) {
			throw new ActivityException();
		}

		if (capacity < 1) {
			throw new ActivityException();
		}

	}
	
	/* Can be used if we need to add verifications 
	@Override
	public void addActivityOffer(ActivityOffer offer) {
	   super.addActivityOffer(offer);
	}*/


	int getNumberOfOffers() {
		return this.getActivityOfferSet().size();
	}
	
	Set<ActivityOffer> getOffers(LocalDate begin, LocalDate end, int age) {
		Set<ActivityOffer> result = new HashSet<>();
		for (ActivityOffer offer : this.getActivityOfferSet()) {
			if (matchAge(age) && offer.available(begin, end)) {
				result.add(offer);
			}
		}
		return result;
	}

	boolean matchAge(int age) {
		return age >= this.getMinAge() && age <= this.getMaxAge();
	}

	public Booking getBooking(String reference) {
		for (ActivityOffer offer : this.getActivityOfferSet()) {
			Booking booking = offer.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	
	public void delete() {
		setActivityProvider(null);
		
		for(ActivityOffer offer : this.getActivityOfferSet()){
			offer.delete();
		}
		deleteDomainObject();
	}


}
