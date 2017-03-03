package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankConstructorTest {

	@Before
	public void setUp() {

	}

	
	@Test(expected = BankException.class)
	public void bankNameCantBeNull() {
		Bank bank = new Bank(null, "BK01");
	}
	
	@Test(expected = BankException.class)
	public void bankCodeCantBeNull() {
		Bank bank = new Bank("Money", null);
	}
	
	@Test(expected = BankException.class)
	public void bankNameCantBeEmpty() {
		Bank bank = new Bank("", "BK01");
	}
	
	@Test(expected = BankException.class)
	public void bankCodeCantBeEmpty() {
		Bank bank = new Bank("Money", "");
	}
	
	@Test(expected = BankException.class)
	public void bankCodeHasLength4() {
		Bank bank = new Bank("Money", "BK0");
	}
	
	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
		
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
