package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

@RunWith(JMockit.class)
public class AdventureSequenceTest{
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp(){
		this.adventure=new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
	}
	
	@Test
	public void successWithRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result=PAYMENT_CONFIRMATION;
				this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = this.anyString;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result= this.anyString;
			}
		};
		
		adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED,this.adventure.getState());
	}
	
	@Test
	public void successWithoutRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface){
		this.adventure = new Adventure(this.broker, new LocalDate(2016, 12, 21), this.end, 20, IBAN, 300);
		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result=PAYMENT_CONFIRMATION;
				this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = this.anyString;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
}