package br.com.pelisoli.realm.model;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Person extends RealmObject implements Serializable{
	@PrimaryKey
	private String identification_document;

	@Required
	private String name;

	private City city;

	public Person() {
	}

	public Person(String identification_document, String name, City city) {
		this.identification_document = identification_document;
		this.name = name;
		this.city = city;
	}

	public String getIdentification_document() {
		return identification_document;
	}

	public void setIdentification_document(String identification_document) {
		this.identification_document = identification_document;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
