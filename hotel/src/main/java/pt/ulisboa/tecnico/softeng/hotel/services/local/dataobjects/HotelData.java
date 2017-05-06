package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class HotelData {
	public static enum CopyDepth{
		SHALLOW, ROOMS
	}
	
	private String code;
	private String name;
	private List<RoomData> rooms = new ArrayList<>();
	
	public HotelData(){
		
	}
	
	public HotelData(Hotel hotel, CopyDepth depth){
		this.code = hotel.getCode();
		this.name = hotel.getName();
		
		switch (depth) {
		case ROOMS:
			for (Room room : hotel.getRoomSet()) {
				this.rooms.add(new RoomData(room, hotel, RoomData.CopyDepth.SHALLOW));
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
	
	public List<RoomData> getRooms(){
		return this.rooms;
	}
	
	public void setRooms(List<RoomData> rooms){
		this.rooms=rooms;
	}
	
}