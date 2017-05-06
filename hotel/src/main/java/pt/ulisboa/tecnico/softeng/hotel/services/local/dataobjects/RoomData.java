package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;


public class RoomData {
	public static enum CopyDepth{
		SHALLOW, ROOMBOOKING, BOOKINGS
	}
	
	private String code;
	private String name;
	private String number;
	private Room.Type type;
	private List<RoomBookingData> bookings = new ArrayList<>();
	
	public RoomData() {} 

	public RoomData(Room room, Hotel hotel, CopyDepth depth){
		this.code = hotel.getCode();
		this.name = hotel.getName();
		this.number = room.getNumber();
		this.type = room.getType();
		switch (depth) {
		case ROOMBOOKING:
			for(Booking booking : room.getBookingSet()){
				RoomBookingData bookingData = new RoomBookingData(room, booking);
				this.bookings.add(bookingData);
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getNumber(){
		return this.number;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public Room.Type getType(){
		return this.type;
	}
	
	public void setType(Room.Type type){
		this.type = type;
	}
	
	public void setCode(Room.Type type){
		this.type = type;
	}
<<<<<<< HEAD
<<<<<<< HEAD
	public List<RoomBookingData> getBookings(){
		return bookings;
=======
	
	public List<RoomBookingData> getRoomBookingData() {
		return this.bookings;
>>>>>>> 4592dd720aea2326d79269eed93a8fae51c94360
=======
	
	public List<RoomBookingData> getRoomBookingData() {
		return this.bookings;
>>>>>>> 4592dd720aea2326d79269eed93a8fae51c94360
	}
}
