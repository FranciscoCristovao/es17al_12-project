package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;


import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;


public class BankData {
	public static enum CopyDepth {
		SHALLOW, CLIENTS
	};
	
	private String name;
	private String code;
	private List<ClientData> clients = new ArrayList<>();
	
	public BankData() {
	}
	
	public BankData(Bank bank, CopyDepth depth) {
		this.name = bank.getName();
		this.code = bank.getCode();

		switch (depth) {
		case CLIENTS:
			for (Client cli: bank.getClientSet()) {
				this.clients.add(new ClientData(cli, ClientData.CopyDepth.SHALLOW));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}

	}
	
	public String getName(){
		return this.name;
		}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code=code;
	}
	
	public List<ClientData> getClients(){
		return this.clients;
	}
	
	public void setClients(List<ClientData> clients){
		this.clients=clients;
	}
	


	
}
