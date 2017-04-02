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
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest{
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp(){
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
	}
	
	
	//------------------------PROCESS_PAYMENT-------------------------
	
	@Test
	public void processPaymentCancelledBankException(@Mocked final BankInterface bankInterface){
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = new BankException();
			this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void processPaymentCancelledRemoteException(@Mocked final BankInterface bankInterface){
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = new RemoteAccessException();
			this.times=3;
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	//------------------------RESERVE_ACTIVITY-------------------------
	
	@Test
	public void reserveActivityUndoCancelledActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface){
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = PAYMENT_CONFIRMATION;
			this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations(){
			{
			ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), 20);
			this.result = new ActivityException();
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityUndoCancelledRemoteException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface){
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = PAYMENT_CONFIRMATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations(){
			{
			ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), 20);
			this.result = new RemoteAccessException();
			this.times = 5;
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	//------------------------CONFIRMED-------------------------
	
	@Test
	public void reserveActivityConfirmedUndoBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface){
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = PAYMENT_CONFIRMATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations(){
			{
			ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getBegin(), 20);
			this.result = ACTIVITY_CONFIRMATION;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new BankException();
			this.times = 5;
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityConfirmedUndoActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface){
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = PAYMENT_CONFIRMATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations(){
			{
			ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getBegin(), 20);
			this.result = ACTIVITY_CONFIRMATION;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new BankOperationData();
			this.times = 1;
			}
			{
			ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			this.result = new ActivityException();
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityConfirmedUndoRemoteExceptionBank(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface){
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		new StrictExpectations(){
			{
			BankInterface.processPayment(this.anyString, this.anyInt);
			this.result = PAYMENT_CONFIRMATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations(){
			{
			ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getBegin(), 20);
			this.result = ACTIVITY_CONFIRMATION;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new RemoteAccessException();
			this.times = 20;
			}
		};
		
		for(int i=0; i<20; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	

	
	//------------------------BOOK_ROOM-------------------------
	
	@Test
	public void bookRoomUndoCancelledHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

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
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result = new HotelException();
			}
		};
		
		adventure.process();
		Assert.assertEquals(Adventure.State.UNDO,this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomUndoCancelledRemoteException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

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
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), 20);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result = new RemoteAccessException();
			this.times = 10;
			}
		};
		
		for(int i=0; i<10; i++) adventure.process();
		Assert.assertEquals(Adventure.State.UNDO,this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	
	@Test
	public void bookRoomConfirmedUndoBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result = ROOM_CONFIRMATION;
			}
		};
		
		adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED,this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new BankException();
			this.times = 5;
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
			{
			HotelInterface.cancelBooking(ROOM_CONFIRMATION);
			this.result = ROOM_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	
	
	
	@Test
	public void bookRoomConfirmedUndoActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result = ROOM_CONFIRMATION;
			}
		};
		
		adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED,this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new BankOperationData();
			this.times = 1;
			}
			{
			ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			this.result = new ActivityException();
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
			{
			HotelInterface.cancelBooking(ROOM_CONFIRMATION);
			this.result = ROOM_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomConfirmedUndoRemoteExceptionBank(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result = ROOM_CONFIRMATION;
			}
		};
		
		adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED,this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new RemoteAccessException();
			this.times = 20;
			}
		};
		
		for(int i=0; i<20; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
			{
			HotelInterface.cancelBooking(ROOM_CONFIRMATION);
			this.result = ROOM_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	
	@Test
	public void bookRoomConfirmedUndoHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;
				this.times=1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		new StrictExpectations(){
			{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			this.result = ROOM_CONFIRMATION;
			}
		};
		
		adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED,this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			this.result = new BankOperationData();
			this.times = 1;
			}
			{
			ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			this.result = new ActivityReservationData();
			this.times = 1;
			}
			{
			HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			this.result = new HotelException();
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		new StrictExpectations(){
			{
			BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			this.result = PAYMENT_CANCELLATION;
			this.times = 1;
			}
			{
			ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			this.result = ACTIVITY_CANCELLATION;
			this.times = 1;
			}
			{
			HotelInterface.cancelBooking(ROOM_CONFIRMATION);
			this.result = ROOM_CANCELLATION;
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	

	
	//-------------------------AdventureSequenceTest CONFIRMED--------------------------------
	
	@Test
	public void successWithRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface){

		new StrictExpectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyInt);
				this.result=this.anyString;
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
				this.result=this.anyString;
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