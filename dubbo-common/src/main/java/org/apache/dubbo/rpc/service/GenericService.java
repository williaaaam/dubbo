/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.service;

import java.util.concurrent.CompletableFuture;

/**
 * 参考文档：https://dubbo.apache.org/zh/docs/v3.0/references/features/generic-reference/
 *
 * 泛化服务调用：解决客户端在没有API接口依赖情况下的解决方案，参数以及返回值中的所有POJO均用Map表示
 *
 * 泛化调用：
 * 1. 通过Spring的泛化调用
 * <dubbo:reference id="barService" interface="com.foo.BarService" generic="true" />
 *
 *
 *
 * Generic service interface
 *
 * @export
 */
public interface GenericService {

    /**
     * 1. 用Map表示POJO参数，如果返回值为POJO也将自动转成Map
     * 2. 基本类型以及Date,List,Map等不需要转换，直接调用
     *
     * // 用Map表示POJO参数，如果返回值为POJO也将自动转成Map
     * Map<String, Object> person = new HashMap<String, Object>();
     * person.put("name", "xxx");
     * person.put("password", "yyy");
     * // 如果返回POJO将自动转成Map
     * Object result = genericService.$invoke("findPerson", new String[]
     * {"com.xxx.Person"}, new Object[]{person});
     *
     *
     * Generic invocation 泛化调用
     *
     * @param method         Method name, e.g. findPerson. If there are overridden methods, parameter info is
     *                       required, e.g. findPerson(java.lang.String)
     * @param parameterTypes Parameter types 方法参数类型
     * @param args           Arguments 方法参数数组
     * @return invocation return value
     * @throws GenericException potential exception thrown from the invocation
     */
    Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException;

    default CompletableFuture<Object> $invokeAsync(String method, String[] parameterTypes, Object[] args) throws GenericException {
        Object object = $invoke(method, parameterTypes, args);
        if (object instanceof CompletableFuture) {
            return (CompletableFuture<Object>) object;
        }
        return CompletableFuture.completedFuture(object);
    }

}