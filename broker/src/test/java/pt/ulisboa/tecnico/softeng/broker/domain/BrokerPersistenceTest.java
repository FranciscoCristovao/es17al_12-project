package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BrokerPersistenceTest {
	private static final String BROKER_NAME = "Happy Going";
	private static final String BROKER_CODE = "BK1017";
	private static final int ROOM_NUMBER = 420;	
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private  final String REF = "RITZ";

	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME);

		new Adventure(broker, this.begin, this.end, AGE, IBAN, AMOUNT);
		
		BulkRoomBooking BRB = new BulkRoomBooking(ROOM_NUMBER, begin, end);
		BRB.addReference(REF);
		broker.addBulkRoomBooking(BRB);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getBrokerSet().size());

		List<Broker> brokers = new ArrayList<>(FenixFramework.getDomainRoot().getBrokerSet());
		Broker broker = brokers.get(0);

		assertEquals(BROKER_CODE, broker.getCode());
		assertEquals(BROKER_NAME, broker.getName());
		assertEquals(1, broker.getAdventureSet().size());

		List<Adventure> adventures = new ArrayList<>(broker.getAdventureSet());
		Adventure adventure = adventures.get(0);

		assertNotNull(adventure.getID());
		assertEquals(broker, adventure.getBroker());
		assertEquals(this.begin, adventure.getBegin());
		assertEquals(this.end, adventure.getEnd());
		assertEquals(AGE, adventure.getAge());
		assertEquals(IBAN, adventure.getIBAN());
		assertEquals(AMOUNT, adventure.getAmount());

		assertEquals(Adventure.State.PROCESS_PAYMENT, adventure.getState().getValue());
		assertEquals(0, adventure.getState().getNumOfRemoteErrors());
		
		assertEquals(1, broker.getBulkRoomBookingSet().size());
		List<BulkRoomBooking> BRBookingList= new ArrayList<>(broker.getBulkRoomBookingSet());
		BulkRoomBooking BRBooking = BRBookingList.get(0);
		
		assertEquals(ROOM_NUMBER, BRBooking.getNumber());
		assertEquals(this.begin, BRBooking.getArrival());
		assertEquals(this.end, BRBooking.getDeparture());
		assertEquals(0, BRBooking.getNumberOfRemoteErrors());
		assertEquals(0, BRBooking.getNumberOfHotelExceptions());
		assertEquals(false, BRBooking.getCancelled());
		
		assertEquals(1,BRBooking.getReferenceSet().size());
		List<Reference> References= new ArrayList<>(BRBooking.getReferenceSet());
		Reference r = References.get(0);
		assertEquals(this.REF, r.getReference());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
	}

}
