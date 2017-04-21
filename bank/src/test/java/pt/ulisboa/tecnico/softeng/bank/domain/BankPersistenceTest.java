package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BankPersistenceTest {
	private static final String BANK_NAME = "Money";
	private static final String BANK_CODE = "BK01";
	private static final String CLIENT_NAME = "Kelson";
	/*private String clientID*/
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank =new Bank(BANK_NAME, BANK_CODE);
		/*clientID = */new Client(bank,CLIENT_NAME)/*.getID()*/;
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Bank bank = Bank.getBankByCode(BANK_CODE);
		 
		assertEquals(BANK_NAME, bank.getName());
		
		
		Client client = null;
		for (Client c : bank.getClientSet()){
			client = c;
		}
		
		Assert.assertFalse(client==null);
		assertEquals(bank,client.getBank());
		/*assertEquals(clientID,client.getID())*/
		Assert.assertTrue(client.getID().length()>=1);
		assertEquals(CLIENT_NAME,client.getName());
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
