package com.gfmotta.avaliacao.dtos;

import java.io.Serializable;

public class SimpleAddressDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String area;
	private String zipCode;
	private Integer number;
	private String city;
	private Boolean mainAddress;

	public SimpleAddressDTO() {

	}

	public SimpleAddressDTO(Long id, String area, String zipCode, Integer number, String city, Boolean mainAddress) {
		this.id = id;
		this.area = area;
		this.zipCode = zipCode;
		this.number = number;
		this.city = city;
		this.mainAddress = mainAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Boolean isMainAddress() {
		return mainAddress;
	}

	public void setMainAddress(Boolean mainAddress) {
		this.mainAddress = mainAddress;
	}
}