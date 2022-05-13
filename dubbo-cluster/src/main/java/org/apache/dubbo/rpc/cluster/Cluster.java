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
package org.apache.dubbo.rpc.cluster;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.support.FailoverCluster;

/**
 * 分布式环境下的负载均衡和集群容错机制
 * Cluster目的是将多个Invoker伪装成一个Invoker，只有一个Invoker是不需要Cluster的
 * <p>
 * 作用：有服务目录，并且目录还经过了路由规则的过滤，此时我们手上还是有一堆 invokers，那对于消费者来说就需要进行抉择，那到底选哪个 invoker 进行调用呢？
 * <p>
 * 假设选择的那个 invoker 调用出错了怎么办？前面我们已经提到了，这时候就是 cluster 登场的时候了，它会把这一堆 invoker 封装成 clusterInovker，给到消费者调用的就只有一个 invoker 了，
 * <p>
 * 然后在这个 clusterInovker 内部还能做各种操作，比如选择一个 invoker ，调用出错了可以换一个等等。
 * <p>
 * Cluster. (SPI, Singleton, ThreadSafe)
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Computer_cluster">Cluster</a>
 * <a href="http://en.wikipedia.org/wiki/Fault-tolerant_system">Fault-Tolerant</a>
 */
@SPI(Cluster.DEFAULT)
public interface Cluster {
    String DEFAULT = FailoverCluster.NAME;

    /**
     * Merge the directory invokers to a virtual invoker.
     *
     * @param <T>
     * @param directory
     * @return cluster invoker
     * @throws RpcException
     */
    @Adaptive
    <T> Invoker<T> join(Directory<T> directory) throws RpcException;

    static Cluster getCluster(String name) {
        return getCluster(name, true);
    }

    static Cluster getCluster(String name, boolean wrap) {
        if (StringUtils.isEmpty(name)) {
            name = Cluster.DEFAULT;
        }
        return ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(name, wrap);
    }
}