package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;

@Controller
@RequestMapping(value = "/hotels/{code}/rooms/{number}")

public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@RequestMapping (value= "/booking", method = RequestMethod.GET)
	public String showBooking(Model model, @PathVariable String code, @PathVariable String number){
		logger.info("show booking from room number:{}", number);
		
		HotelData hotelData = HotelInterface.getHotelDataByCode(code, CopyDepth.ROOMS);
		RoomData roomData = HotelInterface.getRoomDataByNumber(code, number, RoomData.CopyDepth.BOOKINGS);
	
	 
	 	if(hotelData == null) {
	 		
	 		model.addAttribute("error", "Error: it does not exist a hotel with the code: " + code);			
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotels());

			return "hotels";
		}
		
		if(roomData == null) {
			
			model.addAttribute("error", "Error: it does not exist a room with the number: " + number);			
			model.addAttribute("room", new RoomData());
			model.addAttribute("hotel", hotelData);

			return "rooms";
		}
		
		else{
			
			model.addAttribute("booking", new RoomBookingData());
			model.addAttribute("bookings", roomData.getBookings());
			return "bookings";
		}
	}
	//TIRARRRR
	@RequestMapping(value="/bookings", method = RequestMethod.POST)
	public String accountSubmit(Model model, @ModelAttribute RoomBookingData booking, @PathVariable String code,  @PathVariable String number) {
		logger.info("bookingSubmit");

		try {
			HotelInterface.createBooking(code, number);
		} catch (HotelException be) {
			be.printStackTrace();
			model.addAttribute("error", "Error: it was not possible to create the booking");
			
			HotelData hotelData = HotelInterface.getHotelDataByCode(code, CopyDepth.ROOMS);
			RoomData roomData = HotelInterface.getRoomDataByNumber(code, number, RoomData.CopyDepth.BOOKINGS);
			model.addAttribute("hotel", hotelData);
			model.addAttribute("room", roomData);
			model.addAttribute("booking", booking);
			model.addAttribute("accounts", roomData.getBookings());
			return "accounts";
		}

		return "redirect:/hotels/"+code+"/rooms/"+number+"/bookings";
	}
}
