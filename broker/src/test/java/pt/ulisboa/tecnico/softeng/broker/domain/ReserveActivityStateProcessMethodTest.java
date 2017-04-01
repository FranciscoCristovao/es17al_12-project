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
import pt.ulisboa.tecnico.softeng.activity.exception.*;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.*;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.RESERVE_ACTIVITY);
	}
	
	@Test
	public void reserveActivityActivityException(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityRemoteAccessException(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityFifthRemoteAccessException(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = new RemoteAccessException();
				this.times = 5;
			}
		};

		for(int i =0; i<5; i++) this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
				
	}
	
	@Test
	public void reserveActivityFourthRemoteAccessException(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = new RemoteAccessException();	
				this.times = 4;
			}
		};

		for(int i =0; i<4; i++) this.adventure.process();

		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
	}
	
	@Test
	public void reserveActivitySuccessDifDates(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(),20);
				this.result = this.anyString;
				
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
	}
	
	@Test
	public void reserveActivitySuccessEqualDates(@Mocked final ActivityInterface activityInterface) {
		this.adventure = new Adventure(this.broker, new LocalDate(2016, 12, 21), this.end, 20, IBAN, 300);
		this.adventure.setState(State.RESERVE_ACTIVITY);
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
