package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData.CopyDepth;

@Controller
@RequestMapping(value = "/hotels/{code}/rooms/{number}")

public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@RequestMapping (value= "/booking", method = RequestMethod.GET)
	public String showBooking(Model model, @PathVariable String code, @PathVariable String number){
		logger.info("show booking from room number:{}", number);
		
		HotelData hotelData = HotelInterface.getHotelDataByCode(code, CopyDepth.ROOMS);
		RoomData roomData = HotelInterface.getRoomDataByNumber(code, number, CopyDepth.BOOKINGS);
	
	 
	 	if(hotelData == null) {
	 		
	 		model.addAttribute("error", "Error: it does not exist a hotel with the code: " + code);			
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotel());

			return "hotels";
		}
		
		if(roomData == null) {
			
			model.addAttribute("error", "Error: it does not exist a room with the number: " + number);			
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", HotelInterface.getRooms());

			return "rooms";
		}
		
		else{
			
			model.addAttribute("booking", new roomBookingData());
			model.addAttribute("bookings", roomData.getBookings());
			return "bookings";
		}
		
		
		
		
		
		
		return null;
	}
	
}
