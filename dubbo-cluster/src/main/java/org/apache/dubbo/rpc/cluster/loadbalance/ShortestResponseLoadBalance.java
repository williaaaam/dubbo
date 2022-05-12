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
package org.apache.dubbo.rpc.cluster.loadbalance;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 2020 年 5 月 15 日，Dubbo 发布 2.7.7 release 版本，新增ShortestResponseLoadBalance
 *
 * 从多个服务提供者中选择出调用成功的且响应时间最短的服务提供者，由于满足这样条件的服务提供者有可能有多个。所以当选择出多个服务提供者后要根据他们的权重做分析。
 *
 * 但是如果只选择出来了一个，直接用选出来这个。
 *
 * 如果真的有多个，看它们的权重是否一样，如果不一样，则走加权随机算法的逻辑。
 *
 * 如果它们的权重是一样的，则随机调用一个。
 *
 * ShortestResponseLoadBalance
 * </p>
 * Filter the number of invokers with the shortest response time of success calls and count the weights and quantities of these invokers.
 * If there is only one invoker, use the invoker directly;
 * if there are multiple invokers and the weights are not the same, then random according to the total weight;
 * if there are multiple invokers and the same weight, then randomly called.
 */
public class ShortestResponseLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "shortestresponse";

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // Number of invokers
        int length = invokers.size();
        // 临时变量，循环中当前最短响应时间
        // Estimated shortest response time of all invokers
        long shortestResponse = Long.MAX_VALUE;
        // The number of invokers having the same estimated shortest response time
        // 相同最短响应时间的提供者个数
        int shortestCount = 0;
        // The index of invokers having the same estimated shortest response time
        // 相同最短时间响应时间的提供者下标
        int[] shortestIndexes = new int[length];
        // the weight of every invokers
        // Provider的权重
        int[] weights = new int[length];
        // The sum of the warmup weights of all the shortest response  invokers
        // 多个具有相同最短响应时间的服务提供者对应的预热权重之和
        int totalWeight = 0;
        // The weight of the first shortest response invokers
        // 第一个具有最短响应时间的服务提供者的权重
        int firstWeight = 0;
        // Every shortest response invoker has the same weight value?
        // 多个满足条件的提供者的权重是否一致
        boolean sameWeight = true;

        // 过滤所有的最短响应时间提供者
        // Filter out all the shortest response invokers
        for (int i = 0; i < length; i++) {
            Invoker<T> invoker = invokers.get(i);
            RpcStatus rpcStatus = RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName());
            // Calculate the estimated response time from the product of active connections and succeeded average elapsed time.
            // 计算调用成功的平均时间=调用成功的请求数总数对应的总耗时 / 调用成功的请求数总数 = 成功调用的平均时间。


            long succeededAverageElapsed = rpcStatus.getSucceededAverageElapsed();
            // 该服务提供者的活跃数，也就是堆积的请求数
            int active = rpcStatus.getActive();
            // 如果当前这个请求发给这个服务提供者预计需要等待的时间。乘以 active 的原因是因为它需要排在堆积的请求的后面
            long estimateResponse = succeededAverageElapsed * active;
            // 获取预热权重 >= 1
            int afterWarmup = getWeight(invoker, invocation);
            weights[i] = afterWarmup;
            // Same as LeastActiveLoadBalance
            if (estimateResponse < shortestResponse) { // 如果出现更短的响应时间的服务提供者，首先记录更短的响应时间，然后记录当前服务提供者的下标
                shortestResponse = estimateResponse;
                shortestCount = 1;
                shortestIndexes[0] = i;
                totalWeight = afterWarmup;
                firstWeight = afterWarmup;
                sameWeight = true;
            } else if (estimateResponse == shortestResponse) { // 有多个响应时间相同的提供者， 1. 相同权重计数+1 2. 记录下标
                shortestIndexes[shortestCount++] = i;
                totalWeight += afterWarmup; // 总权重
                if (sameWeight && i > 0
                        && afterWarmup != firstWeight) { // 判断权重是否相等
                    sameWeight = false;
                }
            }
        }
        // 1. 直接调用
        if (shortestCount == 1) {
            // 经过选择后只有一个服务提供者满足条件。所以，直接使用这个服务提供者。
            return invokers.get(shortestIndexes[0]);
        }

        // 2. 加权随机算法选择一个
        // 说明存在多个响应时间相同的Provider
        if (!sameWeight && totalWeight > 0) { // 多个Prover响应时间相同，但权重不同 加权随机负载均衡
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < shortestCount; i++) {
                int shortestIndex = shortestIndexes[i];
                offsetWeight -= weights[shortestIndex];
                if (offsetWeight < 0) {
                    return invokers.get(shortestIndex);
                }
            }
        }

        // 3. 随机调用
        // 存在多个响应时间相同的Provider，权重相同 并且总响应时间大于0，则随机调用 和加权随机RandomLoadBalance算法一样
        return invokers.get(shortestIndexes[ThreadLocalRandom.current().nextInt(shortestCount)]);
    }
}
