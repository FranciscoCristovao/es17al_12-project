package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;

@Controller
@RequestMapping(value = "/hotels/{code}/rooms/{number}/bookings")

public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@RequestMapping (method = RequestMethod.GET)
	public String showBooking(Model model, @PathVariable String code, @PathVariable String number){
		logger.info("show booking from room number:{}", number);
		
		HotelData hotelData = HotelInterface.getHotelDataByCode(code, CopyDepth.ROOMS);
	 
	 	if(hotelData == null) {
	 		
	 		model.addAttribute("error", "Error: it does not exist a hotel with the code: " + code);			
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotels());

			return "hotels";
		}
	 	
	 	RoomData roomData = HotelInterface.getRoomDataByNumber(code, number, RoomData.CopyDepth.ROOMBOOKING);
		
		if(roomData == null) {
			
			model.addAttribute("error", "Error: it does not exist a room with the number: " + number);			
			model.addAttribute("room", new RoomData());
			model.addAttribute("hotel", hotelData);

			return "rooms";
		}
		
		else{
			
			model.addAttribute("hotel", hotelData);
			model.addAttribute("room", roomData);
			model.addAttribute("booking", new RoomBookingData());
			model.addAttribute("bookings", roomData.getRoomBookingData());
			return "bookings";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String bookingSubmit(Model model, @PathVariable String code,  @PathVariable String number, @ModelAttribute RoomBookingData booking) {
		logger.info("bookingSubmit arrival={}, departure={}", booking.getArrival(), booking.getDeparture());

		try {
			System.out.println("ANTES CRIAR BOOKING");
			HotelInterface.createBooking(code, number, booking);
			System.out.println("DEPOIS CRIAR BOOKING");
		} catch (HotelException be) {
			be.printStackTrace();
			model.addAttribute("error", "Error: it was not possible to create the booking");
			
			HotelData hotelData = HotelInterface.getHotelDataByCode(code, CopyDepth.ROOMS);
			RoomData roomData = HotelInterface.getRoomDataByNumber(code, number, RoomData.CopyDepth.BOOKINGS);
			model.addAttribute("hotel", hotelData);
			model.addAttribute("room", roomData);
			model.addAttribute("booking", booking);
			model.addAttribute("bookings", roomData.getRoomBookingData());
			return "bookings";
		}

		return "redirect:/hotels/"+code+"/rooms/"+number+"/bookings";
	}
	
}
