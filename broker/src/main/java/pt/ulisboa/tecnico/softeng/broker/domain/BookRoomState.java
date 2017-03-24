package pt.ulisboa.tecnico.softeng.broker.domain;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookRoomState extends AdventureState{

	@Override
	public State getState() {
		return State.BOOK_ROOM;
	}

	@Override
	public void process(Adventure adventure) {
		// TODO
		try {
			adventure.setRoomConfirmation(HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd())); 
		} catch (HotelException rae) {
			adventure.setState(State.UNDO);
		} catch (RemoteAccessException rae) {
			// increment number of errors
			// if (this.numOfRemoteErrors == 10) {
			// adventure.setState(State.UNDO);
			// }
			// return;
		}

		adventure.setState(State.CONFIRMED);
		
	}
	
}
