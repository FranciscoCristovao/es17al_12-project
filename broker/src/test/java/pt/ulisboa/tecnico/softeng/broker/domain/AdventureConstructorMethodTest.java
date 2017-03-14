package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private Broker broker;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}
	
	@Test(expected = BrokerException.class)
	public void adventureBrokerCantBeNull(){
		LocalDate begin = new LocalDate(2016, 12, 16);
		LocalDate end = new LocalDate(2017, 12, 16);
		Adventure adventure = new Adventure(null, begin, end, 20, "PT5023", 100 );
	}
	@Test(expected = BrokerException.class)
	public void adventureIBANCantBeNull(){
		LocalDate begin = new LocalDate(2016, 12, 16);
		LocalDate end = new LocalDate(2017, 12, 16);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, null, 100 );
	}
	@Test(expected = BrokerException.class)
	public void adventureValueCantBeZero(){
		LocalDate begin = new LocalDate(2016, 12, 16);
		LocalDate end = new LocalDate(2017, 12, 16);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "PT5023", 0);
	}
	@Test(expected = BrokerException.class)
	public void adventureValueCantBeNegative(){
		LocalDate begin = new LocalDate(2016, 12, 16);
		LocalDate end = new LocalDate(2017, 12, 16);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "PT5023", -1);
	}
	@Test(expected = BrokerException.class)
	public void adventureAgeUpper99(){
		LocalDate begin = new LocalDate(2016, 12, 16);
		LocalDate end = new LocalDate(2017, 12, 16);
		Adventure adventure = new Adventure(this.broker, begin, end, 100, "PT5023", 100);
	}
	@Test(expected = BrokerException.class)
	public void adventureAgeLower18(){
		LocalDate begin = new LocalDate(2016, 12, 16);
		LocalDate end = new LocalDate(2017, 12, 16);
		Adventure adventure = new Adventure(this.broker, begin, end, 17, "PT5023", 100);
	}
	@Test(expected = BrokerException.class)
	public void adventureConflictDate(){
		LocalDate begin = new LocalDate(2017, 12, 16);
		LocalDate end = new LocalDate(2016, 12, 16);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "PT5023", 100);
	}
	@Test(expected = BrokerException.class)
	public void adventureDateNull(){
		LocalDate end = new LocalDate(2016, 12, 16);
		Adventure adventure = new Adventure(this.broker, null, end, 20, "PT5023", 100);
	}
	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK011234567", 300);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(begin, adventure.getBegin());
		Assert.assertEquals(end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals("BK011234567", adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getBankPayment());
		Assert.assertNull(adventure.getActivityBooking());
		Assert.assertNull(adventure.getRoomBooking());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
