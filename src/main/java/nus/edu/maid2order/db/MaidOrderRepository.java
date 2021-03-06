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

import nus.edu.maid2order.domain.Maid;
import nus.edu.maid2order.domain.MaidOrder;
import nus.edu.maid2order.domain.OneTimeMaidUsagePlan;
import org.springframework.data.repository.CrudRepository;

/**
 * A simple Spring Data {@link CrudRepository} for storing {@link Maid}s.
 *
 * @author Greg Turnquist
 */
public interface MaidOrderRepository extends CrudRepository<MaidOrder, Long> {
}