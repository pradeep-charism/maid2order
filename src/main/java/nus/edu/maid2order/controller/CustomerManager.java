/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package nus.edu.maid2order.controller;

import nus.edu.maid2order.db.CustomerRepository;
import nus.edu.maid2order.db.MaidRepository;
import nus.edu.maid2order.db.MaidUsagePlanRepository;
import nus.edu.maid2order.domain.Maid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 * @author Pradeep Kumar
 */
@RestController
public class CustomerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManager.class);

    @Autowired
    private final MaidRepository maidRepository;

    @Autowired
    private MaidUsagePlanRepository maidUsagePlan;

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerManager(MaidRepository maidRepository) {
        this.maidRepository = maidRepository;
    }


    /**
     * Release existing maid then return a Location header.
     *
     * @param id
     * @return
     */
    @DeleteMapping("/releaseHiredMaid/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<Maid>>> releaseHiredMaid(@PathVariable long id) {
        LOGGER.info("Releasing maid with id: [{}]", id);
        maidRepository.deleteById(id);

        List<EntityModel<Maid>> maids = StreamSupport.stream(maidRepository.findAll().spliterator(), false)
                .map(maid -> new EntityModel<>(maid, //
                        linkTo(methodOn(MaidOrderManager.class).showAllMaidUsagePlans()).withRel("maidUsagePlans"), //
                        linkTo(methodOn(AgencyManager.class).fetchAllMaids()).withRel("allMaids"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok( //
                new CollectionModel<>(maids, //
                        linkTo(methodOn(AgencyManager.class).fetchAllMaids()).withSelfRel()));
    }
}
