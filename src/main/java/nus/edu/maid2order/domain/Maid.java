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
package nus.edu.maid2order.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Domain object representing a company employee. Project Lombok keeps actual code at a minimum. {@code @Data} -
 * Generates getters, setters, toString, hash, and equals functions {@code @Entity} - JPA annotation to flag this class
 * for DB persistence {@code @NoArgsConstructor} - Create a constructor with no args to support JPA
 * {@code @AllArgsConstructor} - Create a constructor with all args to support testing
 * {@code @JsonIgnoreProperties(ignoreUnknow=true)} When converting JSON to Java, ignore any unrecognized attributes.
 * This is critical for REST because it encourages adding new fields in later versions that won't break. It also allows
 * things like _links to be ignore as well, meaning HAL documents can be fetched and later posted to the server without
 * adjustment.
 *
 * @author Greg Turnquist
 */
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Maid {

    @Id
    @GeneratedValue
    private Long maidId;

    private String maidName;

    private int age;

    /**
     * Useful constructor when id is not yet known.
     *
     * @param maidName
     * @param age
     */
    public Maid(String maidName, int age) {
        this.maidName = maidName;
        this.age = age;
    }

    public Long getMaidId() {
        return maidId;
    }

    public void setMaidId(Long maidId) {
        this.maidId = maidId;
    }
}
