/**
 * Copyright (c) KMG. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.sbk.api;

public class MetricsConfig {
    public int reportingSeconds;
    public int port;
    public String context;
    public TimeUnit timeUnit;
    public int minLatency;
    public int maxWindowLatency;
    public int maxLatency;
    public String percentiles;
}