package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.Arrays;
import java.util.HashSet;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingGetReferenceMethodTest {
	
	private final int number = 5;
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private BulkRoomBooking bulkRoomBooking;
	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp(){
		this.bulkRoomBooking = new BulkRoomBooking(this.number, this.arrival, this.departure);
	}
	
	@Test
	public void success(@Mocked final HotelInterface hotelInterface, @Mocked final RoomBookingData data){

		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);	
				this.result = new HashSet<String>(Arrays.asList("ref1", "ref2", "ref3", "ref4", "ref5")); 
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = data;
				
				data.getRoomType();
				this.result = "DOUBLE";
			}
		};
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.getReference("DOUBLE");		
		Assert.assertEquals(4, this.bulkRoomBooking.getReferences().size());
		}
	
	@Test
	public void HotelException(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HashSet<String>(Arrays.asList("referencia"));
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new HotelException();
			}
		};
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.getReference("DOUBLE");
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
	}
	
	
	@Test
	public void oneRemoteError(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HashSet<String>(Arrays.asList("referencia"));
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
			}
		};
		this.bulkRoomBooking.processBooking();
		this.bulkRoomBooking.getReference("DOUBLE");
		Assert.assertEquals(1, this.bulkRoomBooking.getNumberOfRemoteErrors());
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
		
	}
	
	@Test
	public void nineRemoteError(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HashSet<String>(Arrays.asList("referencia"));
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times= 9;
			}
		};
		this.bulkRoomBooking.processBooking();
		for(int i = 0; i<9; i++) {
			this.bulkRoomBooking.getReference("DOUBLE");
		}
		Assert.assertEquals(9, this.bulkRoomBooking.getNumberOfRemoteErrors());
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
		
	}
	
	@Test
	public void tenRemoteError(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HashSet<String>(Arrays.asList("referencia"));
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times = 10;
			}
		};
		
		this.bulkRoomBooking.processBooking();
		for(int i = 0; i<10; i++) {
			this.bulkRoomBooking.getReference("DOUBLE");
		}
		Assert.assertEquals(10, this.bulkRoomBooking.getNumberOfRemoteErrors());
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), true);
		
	}
	
}