package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;


public class ClientPersistenceTest {
	private static final String CLIENT_NAME = "Kelson";
	private  Bank clientBank;
	private String clientID;

	
	
	@Before
	@Atomic(mode = TxMode.WRITE)
	public void setUp(){
		clientBank = new Bank("ClientPersistenceTestBank","CPTB");
	}
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		clientID = new Client(clientBank,CLIENT_NAME).getID();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Bank bank = Bank.getBankByCode("CPTB");
		Client client = bank.getClientByID(clientID);
		assertEquals(1,bank.getClientSet().size());
		assertEquals(clientID,client.getID());
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}
}


