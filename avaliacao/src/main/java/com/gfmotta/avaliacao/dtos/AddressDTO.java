package com.gfmotta.avaliacao.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddressDTO extends SimpleAddressDTO {
	private static final long serialVersionUID = 1L;

	@Positive(message = "O valor deve ser positivo")
	@NotNull(message = "O valor não pode ser nulo")
	private Long personId;

	public AddressDTO() {
		super();
	}

	public AddressDTO(Long id, String area, String zipCode, Integer number, String city, Boolean mainAddress, Long personId) {
		super(id, area, zipCode, number, city, mainAddress);
		this.personId = personId;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}	
}
