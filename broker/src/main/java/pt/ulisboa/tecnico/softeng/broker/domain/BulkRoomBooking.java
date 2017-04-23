package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking extends BulkRoomBooking_Base {
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;

	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		setNumber(number);
		setArrival(arrival);
		setDeparture(departure);
		setNumberOfRemoteErrors(0);
		setNumberOfHotelExceptions(0);
		setCancelled(false);
	}
	public void addReference(String reference){
		if (reference == null)
			throw new BrokerException();
		Reference ref = new Reference(reference);
		super.addReference(ref);
	}

	public void processBooking() {
		if (this.getCancelled()) {
			return;
		}

		try {
			Set<String> references = HotelInterface.bulkBooking(this.getNumber(), this.getArrival(), this.getDeparture());
			for(String ref : references)
				addReference(ref);
			setNumberOfHotelExceptions(0);
			setNumberOfRemoteErrors(0);
			return;
		} catch (HotelException he) {
			setNumberOfHotelExceptions(this.getNumberOfHotelExceptions() + 1);
			if (this.getNumberOfHotelExceptions() == MAX_HOTEL_EXCEPTIONS) {
				setCancelled(true);
			}
			setNumberOfRemoteErrors(0);
			return;
		} catch (RemoteAccessException rae) {
			setNumberOfRemoteErrors(this.getNumberOfRemoteErrors() + 1);
			if (this.getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				setCancelled(true);
			}
			setNumberOfHotelExceptions(0);
			return;
		}
	}

	public String getReference(String type) {
		if (this.getCancelled()) {
			return null;
		}

		for (Reference reference : this.getReferenceSet()) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference.getReference());
				setNumberOfRemoteErrors(0);
			} catch (HotelException he) {
				setNumberOfRemoteErrors(0);
			} catch (RemoteAccessException rae) {
				setNumberOfRemoteErrors(this.getNumberOfRemoteErrors() + 1);
				if (this.getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					setCancelled(true);
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				super.removeReference(reference);
				return reference.getReference();
			}
		}
		return null;
	}
	
	public void delete(){
		setBroker(null);
		for(Reference ref : getReferenceSet()){
			ref.delete();
		}
		deleteDomainObject();
	}
}

