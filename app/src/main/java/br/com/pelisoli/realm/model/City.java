package br.com.pelisoli.realm.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class City extends RealmObject implements Serializable {
	@PrimaryKey
	private String id;

	@Required
	private String name;

	public City() {
	}

	public City(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public City(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
