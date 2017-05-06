package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class ClientData {
	public static enum CopyDepth {
		SHALLOW, ACCOUNTS
	};

	private String name;
	private String ID;
	//private List<AccountData> accounts = new ArrayList<>();

	public ClientData() {
	}

	public ClientData(Client client, CopyDepth depth) {
		this.name = client.getName();
		this.ID=client.getID();
		switch (depth) {
		case ACCOUNTS:
			//TODO
		case SHALLOW:
			break;
		default:
			break;
		}

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getID() {
		return this.ID;
	}

	public void setID(String id) {
		this.ID = id;
	}


}
