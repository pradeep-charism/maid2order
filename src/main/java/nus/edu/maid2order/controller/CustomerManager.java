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
import nus.edu.maid2order.db.MaidUsagePlanRepository;
import nus.edu.maid2order.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 * @author Pradeep Kumar
 */
@RestController
@RequestMapping("/maid2order/customerManager/api/v1/")
public class CustomerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManager.class);

    @Autowired
    private final MaidRepository maidRepository;

    @Autowired
    private MaidOrderRepository maidOrderRepository;

    @Autowired
    private MaidUsagePlanRepository maidUsagePlanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerManager(MaidRepository maidRepository) {
        this.maidRepository = maidRepository;
    }

    /**
     * Customer orders a new Maid with a Usage Plan.
     *
     * @param customer
     * @return
     */
    @PostMapping("/orderNewMaidService/{maidUsagePlan}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<Maid>>> orderNewMaidService(@RequestBody Customer customer, @PathVariable UsagePlan maidUsagePlan) {
        LOGGER.info("Ordering new maid for Customer id: [{}]", customer);

        String jackie = "Jackie ";
        Maid maid = new Maid(jackie, 35);
        Maid savedMaid = maidRepository.save(maid);

        MaidOrder maidOrder = new MaidOrder(savedMaid.getMaidId(), customer.getCustomerId(), maidUsagePlan.getId());
        maidOrderRepository.save(maidOrder);

        List<EntityModel<Maid>> maids = new ArrayList<>();
        maids.add(EntityModel.of(savedMaid));

        Link link1 = linkTo(methodOn(AgencyManager.class).findMaidById(savedMaid.getMaidId())).withSelfRel();
        Link link2 = linkTo(methodOn(MaidOrderManager.class).showAllMaidUsagePlans()).withRel("updateMaidUsagePlans");
        Link link3 = linkTo(methodOn(CustomerManager.class).cancelMaidOrderService(customer.getCustomerId(), savedMaid.getMaidId())).withRel("cancelMaidOrderService");
        return ResponseEntity.ok( //
                new CollectionModel<>(maids, link1, link2, link3)
        );
    }

    /**
     * Customer orders a new Maid with a Usage Plan.
     *
     * @param customerId
     * @return
     */
    @PostMapping("/calculateMaidUsageBill/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<MaidUsageBill>>> calculateMaidUsageBill(@PathVariable Long customerId) {
        LOGGER.info("Ordering new maid for Customer id: [{}]", customerId);

        Optional<MaidOrder> maidOrderOptional = maidOrderRepository.findById(customerId);
        MaidOrder maidOrder = maidOrderOptional.orElse(null);

        Optional<MaidUsagePlan> plan = maidUsagePlanRepository.findById(maidOrder.getMaidUsagePlanId());
        maidOrder.calculateCost(plan.get());

        MaidUsageBill maidUsageBill = new MaidUsageBill(maidOrder.getMaidId(), maidOrder.getCustomerId(), plan.get().getUsagePlan(), maidOrder.calculateCost(plan.get()));
        List<EntityModel<MaidUsageBill>> maids = new ArrayList<>();
        maids.add(EntityModel.of(maidUsageBill));

        Link link2 = linkTo(methodOn(MaidOrderManager.class).showAllMaidUsagePlans()).withRel("updateMaidUsagePlans");
        return ResponseEntity.ok( //
                new CollectionModel<>(maids, link2)
        );
    }

    /**
     * Cancel maid service.
     *
     * @param customerId
     * @param maidId
     * @return
     */
    @DeleteMapping("/cancelMaidOrderService/{customerId}/{maidId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<Maid>>> cancelMaidOrderService(@PathVariable Long customerId, @PathVariable long maidId) {
        LOGGER.info("Releasing maid with id: [{}]", maidId);

        Optional<Maid> cancelledOrder = maidRepository.findById(maidId);
        maidRepository.deleteById(maidId);

        Customer customer = customerRepository.findById(customerId).get();

        List<EntityModel<Maid>> maids = new ArrayList<>();
        maids.add(EntityModel.of(cancelledOrder.get()));

        Link orderNewMaidService = linkTo(methodOn(CustomerManager.class).orderNewMaidService(customer, UsagePlan.ONCE_A_WEEK)).withRel("orderNewMaidService");
        Link maidUsagePlans = linkTo(methodOn(MaidOrderManager.class).showAllMaidUsagePlans()).withRel("maidUsagePlans");
        Link selfRel = linkTo(methodOn(AgencyManager.class).fetchAllMaids()).withSelfRel();
        return ResponseEntity.ok( //
                new CollectionModel<>(maids, selfRel, maidUsagePlans, orderNewMaidService));
    }
}
