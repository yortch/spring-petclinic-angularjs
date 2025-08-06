/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@RestController
public class OwnerResource extends AbstractResourceController {

    private final ClinicService clinicService;


    public OwnerResource(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    private Owner retrieveOwner(int ownerId) {
        return this.clinicService.findOwnerById(ownerId);
    }

    /**
     * Create Owner
     */
    @PostMapping("/owners")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOwner(@Valid @RequestBody Owner owner) {
    	this.clinicService.saveOwner(owner);
    }
    
    /**
     * Read single Owner
     */
    @GetMapping("/owners/{ownerId}")
    public Owner findOwner(@PathVariable int ownerId) {
        return retrieveOwner(ownerId);
    }
    
    /**
     * Read List of Owners
     */
    @GetMapping("/owners/list")
    public Collection<Owner> findAll() {
        return clinicService.findAll();
    }
    
    /**
     * Update Owner
     */
    @PutMapping("/owners/{ownerId}")
    public Owner updateOwner(@PathVariable int ownerId, @Valid @RequestBody Owner ownerRequest) {
    	Owner ownerModel = retrieveOwner(ownerId);
    	// This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
    	ownerModel.setFirstName(ownerRequest.getFirstName());
    	ownerModel.setLastName(ownerRequest.getLastName());
    	ownerModel.setCity(ownerRequest.getCity());
    	ownerModel.setAddress(ownerRequest.getAddress());
    	ownerModel.setTelephone(ownerRequest.getTelephone());
        this.clinicService.saveOwner(ownerModel);
        return ownerModel;
    }



}
