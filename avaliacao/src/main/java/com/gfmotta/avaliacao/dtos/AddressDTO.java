package com.gfmotta.avaliacao.dtos;

import jakarta.validation.constraints.Positive;

public class AddressDTO extends SimpleAddressDTO {
	private static final long serialVersionUID = 1L;

	@Positive(message = "O valor deve ser positivo")
	private Long personId;
	
	public AddressDTO(Long personId) {
		super();
		this.personId = personId;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}	
}
