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
import nus.edu.maid2order.domain.UsagePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
public class AgencyManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgencyManager.class);

    @Autowired
    private final MaidRepository maidRepository;

    @Autowired
    private MaidUsagePlanRepository maidUsagePlan;

    @Autowired
    private CustomerRepository customerRepository;

    public AgencyManager(MaidRepository maidRepository) {
        this.maidRepository = maidRepository;
    }

    /**
     * Look up all maids, and transform them into a REST collection resource. Then return them through Spring Web's
     * {@link ResponseEntity} fluent API.
     */
    @GetMapping("/fetchAllMaids")
    ResponseEntity<CollectionModel<EntityModel<Maid>>> fetchAllMaids() {

        List<EntityModel<Maid>> maids = StreamSupport.stream(maidRepository.findAll().spliterator(), false)
                .map(maid -> new EntityModel<>(maid, //
                        linkTo(methodOn(AgencyManager.class).findMaidById(maid.getId())).withSelfRel(), //
                        linkTo(methodOn(AgencyManager.class).fetchAllMaids()).withRel("allMaids"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok( //
                new CollectionModel<>(maids, //
                        linkTo(methodOn(AgencyManager.class).fetchAllMaids()).withSelfRel()));
    }

    @PostMapping("/hireNewMaid/{plan}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<Maid>> hireNewMaid(@RequestBody Maid maid, @PathVariable UsagePlan plan) {

        try {
            LOGGER.info("Hiring Maid: [{}], Usage plan: [{}]", maid, plan);
            Maid savedMaid = maidRepository.save(maid);

            EntityModel<Maid> maidResource = new EntityModel<>(savedMaid, //
                    linkTo(methodOn(AgencyManager.class).findMaidById(savedMaid.getId())).withSelfRel());

            return ResponseEntity //
                    .created(new URI(maidResource.getRequiredLink(IanaLinkRelations.SELF).getHref())) //
                    .body(maidResource);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Look up a single {@link Maid} and transform it into a REST resource. Then return it through Spring Web's
     * {@link ResponseEntity} fluent API.
     *
     * @param id
     */
    @GetMapping("/findMaidBy/{id}")
    ResponseEntity<EntityModel<Maid>> findMaidById(@PathVariable long id) {

        return maidRepository.findById(id) //
                .map(maid -> new EntityModel<>(maid, //
                        linkTo(methodOn(AgencyManager.class).findMaidById(maid.getId())).withSelfRel(), //
                        linkTo(methodOn(AgencyManager.class).fetchAllMaids()).withRel("maids"))) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update existing maid then return a Location header.
     *
     * @param maid
     * @param id
     * @return
     */
    @PutMapping("/updateMaid/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> updateMaid(@RequestBody Maid maid, @PathVariable long id) throws URISyntaxException {

        Maid maidToUpdate = maid;
        maidToUpdate.setId(id);
        maidRepository.save(maidToUpdate);

        Link newlyCreatedLink = linkTo(methodOn(AgencyManager.class).findMaidById(id)).withSelfRel();

        return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();

    }

    /**
     * Release existing maid then return a Location header.
     *
     * @param id
     * @return
     */
    @DeleteMapping("/terminateMaid/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<Maid>>> terminateMaid(@PathVariable long id) {
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
