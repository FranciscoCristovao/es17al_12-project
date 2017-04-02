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
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingProcessMethodTest {
	
	private final int number = 3;
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
	public void success(@Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);	
				this.result = new HashSet<String>(Arrays.asList("ref1", "ref2", "ref3", "ref4", "ref5")); 
			}
		};
		this.bulkRoomBooking.processBooking();		
		Assert.assertEquals(this.bulkRoomBooking.getReferences().size(), 5);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
	}
	
	@Test
	public void cancelReturned(){
		this.bulkRoomBooking.setCancelled(true);
		this.bulkRoomBooking.processBooking();
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getReferences().size(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), true);
	}
	
	@Test
	public void oneHotelException(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations(){
		{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HotelException();
			}
		};
		this.bulkRoomBooking.processBooking();
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 1);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
		
	}
	@Test
	public void twoHotelException(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations(){
		{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HotelException();
				this.times = 2;
			}
		};
		for(int i = 0; i < 2; i++) this.bulkRoomBooking.processBooking();
		
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 2);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
		
	}
	
	@Test
	public void threeHotelException(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations(){
		{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HotelException();
				this.times = 3;
			}
		};
		for(int i = 0; i < 3; i++) this.bulkRoomBooking.processBooking();
		
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 3);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), true);
		
	}
	
	@Test
	public void oneRemoteError(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new RemoteAccessException();
			}
		};
		this.bulkRoomBooking.processBooking();

		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 1);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
		
	}
	
	@Test
	public void nineRemoteError(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = 9;
			}
		};
		for(int i = 0; i<9; i++) this.bulkRoomBooking.processBooking();
		
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 9);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), false);
		
	}
	
	@Test
	public void tenRemoteError(@Mocked final HotelInterface hotelInterface){
		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new RemoteAccessException();
				this.times = 10;
			}
		};
		for(int i = 0; i<10; i++) this.bulkRoomBooking.processBooking();
		
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfHotelExceptions(), 0);
		Assert.assertEquals(this.bulkRoomBooking.getNumberOfRemoteErrors(), 10);
		Assert.assertEquals(this.bulkRoomBooking.getCancelled(), true);
		
	}
	
	/**/
}