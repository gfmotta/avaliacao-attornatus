package com.gfmotta.avaliacao.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gfmotta.avaliacao.dtos.AddressDTO;
import com.gfmotta.avaliacao.dtos.PersonDTO;
import com.gfmotta.avaliacao.dtos.SimpleAddressDTO;
import com.gfmotta.avaliacao.entities.Address;
import com.gfmotta.avaliacao.entities.Person;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper mapper() {
    	var mapper = new ModelMapper();
    	mapper.typeMap(PersonDTO.class, Person.class).addMappings(x -> x.skip(Person::setId));
    	mapper.typeMap(AddressDTO.class, Address.class).addMappings(x -> x.skip(Address::setId));
    	mapper.typeMap(SimpleAddressDTO.class, Address.class).addMappings(x -> x.skip(Address::setId));
        return mapper; 
    }
}
