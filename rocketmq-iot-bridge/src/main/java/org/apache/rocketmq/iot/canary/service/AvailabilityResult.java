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

package org.apache.rocketmq.iot.canary.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvailabilityResult {
    private Logger logger = LoggerFactory.getLogger(AvailabilityResult.class);

    private AtomicInteger successNum;
    private AtomicInteger failNum;
    private double ratio;

    public AvailabilityResult() {
        successNum = new AtomicInteger(0);
        failNum = new AtomicInteger(0);
        ratio = 0.0;
    }

    public AvailabilityResult(AtomicInteger successNum, AtomicInteger failNum) {
        this.successNum = successNum;
        this.failNum = failNum;
        ratio = 0.0;
    }

    public double getRatio() {
        if (successNum.get() == 0 && failNum.get() == 0) {
            try {
                Thread.sleep(200);
                if (successNum.get() == 0 && failNum.get() == 0) {
                    return 0;
                }
            } catch (InterruptedException e) {
                logger.error("thread sleep interrupted when get ratio", e);
            }
        }
        ratio = successNum.get() * 1.0 / (successNum.get() + failNum.get());
        successNum.set(0);
        failNum.set(0);
        return ratio;
    }

    public void addSuccess() {
        successNum.getAndIncrement();
    }

    public void addFail() {
        failNum.getAndIncrement();
    }

    @Override public String toString() {
        return "AvailabilityResult{" +
            "successNum=" + successNum +
            ", failNum=" + failNum +
            ", ratio=" + ratio +
            '}';
    }
}
