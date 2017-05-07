package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;

public class ActivityData {
	public static enum CopyDepth {
		SHALLOW, OFFERS
	};

	
	private String code;
	private String name;
	private int maxAge;
	private int minAge;
	
	private int capacity;
	private List<ActivityOfferData> offers = new ArrayList<>();

	public ActivityData(){}
	
	public ActivityData(Activity a, CopyDepth depth){
		this.code = a.getCode();
		this.name = a.getName();
		this.maxAge = a.getMaxAge();
		this.minAge = a.getMinAge();
		this.capacity = a.getCapacity();
		
		switch (depth) {
		case OFFERS:
			for (ActivityOffer offer : a.getActivityOfferSet()) {
				this.offers.add(new ActivityOfferData(offer,  ActivityOfferData.CopyDepth.SHALLOW));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
		
	}
	
	public String getCode(){return this.code;}
	public String getName(){return this.name;}
	public int getMaxAge(){return this.maxAge;}
	public int getMinAge(){return this.minAge;}
	public int getCapacity(){return this.capacity;}
	
	public void setCode(String code){ this.code = code;}
	public void setName(String name){this.name = name;}
	public void setMaxAge(int i){this.maxAge = i;}
	public void setMinAge(int i){this.minAge = i;}
	public void setCapacity(int i){this.capacity = i;}
	public List <ActivityOfferData> getOffers(){
		return offers;
	}
	
	public void setOffers(List <ActivityOfferData> off){
		offers = off;
	}
	
}
