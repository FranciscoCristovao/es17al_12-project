package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";

	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";

	private static final String ROOM_CONFIRMATION = "RoomConfirmation";

	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.CONFIRMED);
	}
	
	@Test
	public void didNotPayed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

		new Verifications() {
			{
				BankInterface.getOperationData(this.anyString);
				this.times = 1;

				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 1;

				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};
	}


	@Test
	public void confirmedPaymentBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}


	@Test
	public void confirmedPaymentRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void confirmedActivityAndPayment(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	

	@Test
	public void confirmedRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	
	@Test
	public void confirmedActivityReservationException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	
	@Test
	public void confirmedActivityReservationRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	
	
	
	

	@Test
	public void confirmedHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	
	@Test
	public void confirmedHotelRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final HotelInterface hotelInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	
	
	@Test //Test if the numOfRemoteErrors increments getOperationData BankException
	public void confirmedAdventureStateBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				int i = 0;
				while(i < 5){
					BankInterface.getOperationData(PAYMENT_CONFIRMATION);
					this.result = new BankException();
					i ++;
				}
			}
		};

		int i = 0;
		while(i < 5){
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	
	
	
	@Test //Test if the numOfRemoteErrors increments of getOperationData RemoteAccessException
	public void confirmedAdventureStateRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				int i = 0;
				while(i < 20){
					BankInterface.getOperationData(PAYMENT_CONFIRMATION);
					this.result = new RemoteAccessException();
					i++;
				}
			}
		};

		int i = 0;
		while(i < 20){
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	
	
	
	@Test //Test the resetNumOfRemoteErrors
	public void confirmedActReservationDataRemoteException(@Mocked final BankInterface bankInterface, 
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				int i = 0;
				while(i < 20){
					ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
					
					this.result = new RemoteAccessException();
					
					i++;
				}
			}
		};

		int i = 0;
		while(i < 20){
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	
	
	@Test //Test the resetNumOfRemoteErrors
	public void confirmedRoomBookingDataRemoteException(@Mocked final BankInterface bankInterface, 
			@Mocked final ActivityInterface activityInterface,  @Mocked final HotelInterface hotelInterface) {
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				int i = 0;
				while(i < 20){
					HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
					
					this.result = new RemoteAccessException();
					
					i++;
				}
			}
		};

		int i = 0;
		while(i < 20){
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	
}
