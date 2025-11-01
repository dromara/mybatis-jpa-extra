/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.dromara.mybatis.jpa.id.impl;

import java.util.UUID;

import org.dromara.mybatis.jpa.id.IdentifierGenerator;
/**
 * UUID生成器，UUID生成后转小写
 */
public class UUIDGenerator implements IdentifierGenerator{

    @Override
    public String generate(Object object) {
        return UUID.randomUUID().toString().toLowerCase();
    }

}
