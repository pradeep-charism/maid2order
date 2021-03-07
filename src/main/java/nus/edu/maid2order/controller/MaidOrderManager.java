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
import nus.edu.maid2order.db.MaidOrderRepository;
import nus.edu.maid2order.db.MaidRepository;
import nus.edu.maid2order.domain.UsagePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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
@RequestMapping("/maid2order/maidOrderManager/api/v1/")
public class MaidOrderManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaidOrderManager.class);

    @Autowired
    private final MaidRepository maidRepository;

    @Autowired
    private MaidOrderRepository maidUsagePlan;

    @Autowired
    private CustomerRepository customerRepository;

    public MaidOrderManager(MaidRepository maidRepository) {
        this.maidRepository = maidRepository;
    }

    /**
     * Look up all maids usage plans
     * {@link ResponseEntity} fluent API.
     */
    @GetMapping("/showAllMaidUsagePlans")
    ResponseEntity<CollectionModel<EntityModel<String>>> showAllMaidUsagePlans() {

        List<UsagePlan> usagePlans = Arrays.asList(UsagePlan.values());

        List<EntityModel<String>> maidUsagePlans = StreamSupport.stream(usagePlans.spliterator(), false)
                .map(plan -> new EntityModel<>(plan.name(), //
                        linkTo(methodOn(MaidOrderManager.class).showAllMaidUsagePlans()).withRel("maidUsagePlans"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok( //
                new CollectionModel<>(maidUsagePlans, //
                        linkTo(methodOn(MaidOrderManager.class).showAllMaidUsagePlans()).withSelfRel()));
    }
}
