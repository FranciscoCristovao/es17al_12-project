package pt.ulisboa.tecnico.softeng.activity.services.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ProviderData.CopyDepth;



public class ActivityInterface {

	@Atomic(mode = TxMode.WRITE)
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null) {
			return booking.cancel();
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityReservationData getActivityReservationData(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					Booking booking = offer.getBooking(reference);
					if (booking != null) {
						return new ActivityReservationData(provider, offer, booking);
					}
				}
			}
		}
		throw new ActivityException();
	}
	
	//PROVIDER-------------------------------------------------------------------------------------
	@Atomic(mode = TxMode.READ)
	public static List<ProviderData> getProviders(){
		ArrayList<ProviderData> providers= new ArrayList<>();
		for (ActivityProvider provider:FenixFramework.getDomainRoot().getActivityProviderSet()){
			providers.add(new ProviderData(provider,CopyDepth.SHALLOW));
		}
		return providers;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createProvider(String code,String name) {
		new ActivityProvider(code, name);
	}
	@Atomic(mode = TxMode.WRITE)
	public static void createProvider(ProviderData provider) {
		new ActivityProvider(provider.getCode(),provider.getName());
	}
	
	@Atomic(mode = TxMode.READ)
	public static ProviderData getProviderDataByCode(String code){
		ActivityProvider p = getProviderByCode(code);
		if(p != null)
			return new ProviderData(p, CopyDepth.ACTIVITIES);
		else
			return null;
	}
	
	private static ActivityProvider getProviderByCode(String code) {
		for (ActivityProvider p : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (p.getCode().equals(code)) {
				return p;
			}
		}
		return null;
	}
	
	//ACTIVITY---------------------------------------------------------------------------------------------------------
	@Atomic(mode = TxMode.WRITE)
	public static void createActivity(String code, ActivityData activity) {
		new Activity(getProviderByCode(code),activity.getName(),activity.getMinAge(),
				activity.getMaxAge(), activity.getCapacity());
	}
	
	
	
	private static Booking getBookingByReference(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}
	
	private static Activity getActivityByCode(String codeP, String codeA) {
		ActivityProvider p = getProviderByCode(codeP);
		for (Activity activity: p.getActivitySet()) {
				if(activity.getCode().equals(codeA))
					return activity;
		}
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	public static ActivityData getActivityDataByCode(String codeP, String codeA){
		Activity a = getActivityByCode(codeP, codeA);
		if(a != null)
			return new ActivityData(a, ActivityData.CopyDepth.OFFERS);
		else
			return null;
	}
	
	//ACTIVITYOFFER---------------------------------------------------------------------------------------------------------
	@Atomic(mode = TxMode.WRITE)
	public static void createActivityOffer(String codeP, String codeA, ActivityOfferData activityOffer) {
		new ActivityOffer(getActivityByCode(codeP, codeA), activityOffer.getBegin(), activityOffer.getEnd());
	}
	
	private static Set<ActivityOffer> getOffersByCodes(String codeP, String codeA) {
		Activity a = getActivityByCode(codeP, codeA);
		return a.getActivityOfferSet();
	}
	
	@Atomic(mode = TxMode.READ)
	public static ActivityData getOffersDataByCodes(String codeP, String codeA){
		Set<ActivityOffer> offers = getOffersByCodes(codeP, codeA);
		if(offers == null)
			return null;
		List<ActivityOfferData> offersData = new ArrayList<ActivityOfferData>();
		for(ActivityOffer offer: offers){
		  	ActivityOfferData oData = new ActivityOfferData(offer, ActivityOfferData.CopyDepth.RESERVATIONS);
			oData.setBookings(offer.getBookingSet());
			offersData.add(oData);
		}
		ActivityData aData =  getActivityDataByCode(codeP, codeA);
		aData.setOffers(offersData);
		return aData;
	}
	
}	
