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
package org.apache.dubbo.rpc.cluster.directory;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.RouterChain;

import java.util.Collections;
import java.util.List;

/**
 * 多注册中心
 *
 * 可以简单的理解成在单注册中心下我们配置的一条 reference 可能对应有多个 provider，然后生成多个 invoker，我们将它们存入 RegistryDirectory 中进行管理，为了便于调用再对外只暴露出一个 invoker 来封装内部的多 invoker 情况。
 *
 * 那多个注册中心就会有多个已经封装好了的 invoker ，这又面临了选择了，于是我们用 StaticDirectory 再来存入这些 invoker 进行管理，也再封装起来对外只暴露出一个 invoker 便于调用。
 * ————————————————
 * 原文链接：https://blog.csdn.net/qq_35190492/article/details/108778077
 * StaticDirectory
 */
public class StaticDirectory<T> extends AbstractDirectory<T> {
    private static final Logger logger = LoggerFactory.getLogger(StaticDirectory.class);

    private final List<Invoker<T>> invokers;

    /**
     * 所有 Invoker 是通过构造器来传入
     * @param invokers
     */
    public StaticDirectory(List<Invoker<T>> invokers) {
        this(null, invokers, null);
    }

    public StaticDirectory(List<Invoker<T>> invokers, RouterChain<T> routerChain) {
        this(null, invokers, routerChain);
    }

    public StaticDirectory(URL url, List<Invoker<T>> invokers) {
        this(url, invokers, null);
    }

    public StaticDirectory(URL url, List<Invoker<T>> invokers, RouterChain<T> routerChain) {
        super(url == null && CollectionUtils.isNotEmpty(invokers) ? invokers.get(0).getUrl() : url, routerChain);
        if (CollectionUtils.isEmpty(invokers)) {
            throw new IllegalArgumentException("invokers == null");
        }
        this.invokers = invokers;
    }

    @Override
    public Class<T> getInterface() {
        return invokers.get(0).getInterface();
    }

    @Override
    public List<Invoker<T>> getAllInvokers() {
        return invokers;
    }

    @Override
    public boolean isAvailable() {
        if (isDestroyed()) {
            return false;
        }
        for (Invoker<T> invoker : invokers) {
            if (invoker.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        super.destroy();
        for (Invoker<T> invoker : invokers) {
            invoker.destroy();
        }
        invokers.clear();
    }

    public void buildRouterChain() {
        RouterChain<T> routerChain = RouterChain.buildChain(getUrl());
        routerChain.setInvokers(invokers);
        this.setRouterChain(routerChain);
    }

    /**
     *
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    protected List<Invoker<T>> doList(Invocation invocation) throws RpcException {
        List<Invoker<T>> finalInvokers = invokers;
        if (routerChain != null) {
            try {
                finalInvokers = routerChain.route(getConsumerUrl(), invocation);
            } catch (Throwable t) {
                logger.error("Failed to execute router: " + getUrl() + ", cause: " + t.getMessage(), t);
            }
        }
        return finalInvokers == null ? Collections.emptyList() : finalInvokers;
    }

}
