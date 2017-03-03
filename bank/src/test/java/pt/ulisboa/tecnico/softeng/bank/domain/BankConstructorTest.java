package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
		Assert.assertNotNull(bank.getName());
		Assert.assertNotNull(bank.getCode());
		Assert.assertNotEquals(bank.getName(), "");
		Assert.assertNotEquals(bank.getCode(), "");
		Assert.assertEquals(bank.getCode().length(), 4);
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
