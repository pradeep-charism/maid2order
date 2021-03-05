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
package nus.edu.maid2order.db;

import nus.edu.maid2order.domain.Customer;
import nus.edu.maid2order.domain.Maid;
import nus.edu.maid2order.domain.MaidUsagePlan;
import nus.edu.maid2order.domain.UsagePlan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Pre-load some data using a Spring Boot {@link CommandLineRunner}.
 *
 * @author Pradeep Kumar
 */
@Component
public class DatabaseInitializer {

    /**
     * Use Spring to inject a {@link MaidRepository} that can then load data. Since this will run only after the app
     * is operational, the database will be up.
     *
     * @param repository
     */
    @Bean
    CommandLineRunner loadMaids(MaidRepository repository) {

        return args -> {
            repository.save(new Maid("Anne", 25));
            repository.save(new Maid("Borie", 35));
            repository.save(new Maid("Charlie", 40));
            repository.save(new Maid("Deon", 27));
        };
    }

    @Bean
    CommandLineRunner loadMaidUsagePlan(MaidUsagePlanRepository repository) {

        return args -> {
            repository.save(new MaidUsagePlan(UsagePlan.ONE_TIME, 10.0));
            repository.save(new MaidUsagePlan(UsagePlan.ONCE_A_FORTNIGHT, 15.0));
            repository.save(new MaidUsagePlan(UsagePlan.ONCE_A_WEEK, 10.0));
            repository.save(new MaidUsagePlan(UsagePlan.TWICE_A_WEEK, 30.0));
        };
    }

    @Bean
    CommandLineRunner loadCustomers(CustomerRepository repository) {

        return args -> {
            repository.save(new Customer("Tom"));
            repository.save(new Customer("David"));
            repository.save(new Customer("Henry"));
        };
    }
}