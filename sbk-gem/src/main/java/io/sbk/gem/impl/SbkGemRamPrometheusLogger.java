/**
 * Copyright (c) KMG. All Rights Reserved..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package io.sbk.gem.impl;

import io.sbk.gem.GemLogger;
import io.sbk.ram.impl.SbkRamPrometheusLogger;

public class SbkGemRamPrometheusLogger extends SbkRamPrometheusLogger implements GemLogger {

    @Override
    public String[] getOptionsArgs() {
        return new String[]{"-time", "-context"};
    }

    @Override
    public String[] getParsedArgs() {
        return new String[]{"-time", getTimeUnit().name(), "-context",
                metricsConfig.port +metricsConfig.context};
    }

}
