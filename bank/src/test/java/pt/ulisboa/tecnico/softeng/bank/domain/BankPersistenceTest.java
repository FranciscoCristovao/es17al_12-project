package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;

public class BankPersistenceTest {
	private static final String BANK_NAME = "Money";
	private static final String BANK_CODE = "BK01";
	private static final String CLIENT_NAME = "Kelson";
	private String clientID;
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank =new Bank(BANK_NAME, BANK_CODE);
		Client client = new Client(bank,CLIENT_NAME);
		clientID = client.getID();
		Account account = new Account(bank, client);
		new Operation(Type.DEPOSIT, account, 100);
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
		assertEquals(clientID,client.getID());
		Assert.assertTrue(client.getID().length()>=1);
		assertEquals(CLIENT_NAME,client.getName());
		
		Account account = null;
		for(Account a: bank.getAccountSet()){
			account = a;
		}
		
		Assert.assertNotNull(account);
		assertEquals(bank,account.getBank());
		assertEquals(client,account.getClient());
		Assert.assertTrue(account.getIBAN().startsWith(BANK_CODE));
		Assert.assertTrue(account.getIBAN().substring(3).length()>=1);
		Assert.assertEquals(0, account.getBalance());
		
		Operation operation = null;
		for(Operation o: bank.getOperationSet()){
			operation = o;
		}
		
		Assert.assertNotNull(operation);
		assertEquals(bank, operation.getBank());
		assertEquals(account, operation.getAccount());
		assertEquals(Type.DEPOSIT, operation.getType());
		assertEquals(100, operation.getValue());
		Assert.assertTrue(operation.getReference().startsWith(BANK_CODE));
		Assert.assertTrue(operation.getReference().length() > operation.getBank().getCode().length());
		Assert.assertNotNull(operation.getTime());

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
