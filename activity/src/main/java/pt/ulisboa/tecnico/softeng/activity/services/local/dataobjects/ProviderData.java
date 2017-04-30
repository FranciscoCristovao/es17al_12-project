package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

public class ProviderData {

	public static enum CopyDepth {
		SHALLOW, ACTIVITIES
	};

	private String name;
	private String code;
	private List<ActivityData> activities = new ArrayList<>();
	public ProviderData() {
	}

	public ProviderData(ActivityProvider provider, CopyDepth depth) {
		this.name = provider.getName();
		this.code = provider.getCode();

		switch (depth) {
		case ACTIVITIES:
			//TODO
			break;
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

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ActivityData> getAdventures() {
		return this.activities;
	}

	public void setActivities(List<ActivityData> activities) {
		this.activities = activities;
	}




}
