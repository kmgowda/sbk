/**
 * Copyright (c) KMG. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package io.sbk.perl.impl;

import io.sbk.perl.LatencyRecordWindow;
import io.sbk.perl.ReportLatency;
import io.sbk.system.Printer;
import io.sbk.perl.ReportLatencies;
import io.sbk.perl.PerlConfig;
import io.sbk.perl.PeriodicRecorder;
import io.sbk.perl.Print;

import javax.annotation.concurrent.NotThreadSafe;


/**
 *  class for Performance statistics.
 */
@NotThreadSafe
public class CompositeHashMapLatencyRecorder extends HashMapLatencyRecorder implements ReportLatencies, PeriodicRecorder {
    final public LatencyRecordWindow window;
    final public Print windowLogger;
    final public Print loggerTotal;
    final public ReportLatency reportLatency;

    public CompositeHashMapLatencyRecorder(LatencyRecordWindow window, int maxHashMapSizeMB, Print logger,
                                           Print loggerTotal, ReportLatency reportLatency) {
        super(window.lowLatency, window.highLatency, window.totalLatencyMax,
                window.totalRecordsMax, window.totalBytesMax, window.percentileFractions, window.time, maxHashMapSizeMB);
        this.window = window;
        this.windowLogger = logger;
        this.loggerTotal = loggerTotal;
        this.reportLatency = reportLatency;
    }

    /**
     * Start the window.
     *
     * @param startTime starting time.
     */
    public void start(long startTime) {
        reset(startTime);
    }

    /**
     * Reset the window.
     *
     * @param startTime starting time.
     */
    public void startWindow(long startTime) {
        window.reset(startTime);
    }


    /**
     * Get the current time duration of this window.
     *
     * @param currentTime current time.
     * @return elapsed Time in Milliseconds from the startTime.
     */
    public long elapsedMilliSecondsWindow(long currentTime) {
        return window.elapsedMilliSeconds(currentTime);
    }


    /**
     * Record the latency.
     *
     * @param startTime start time
     * @param endTime end time
     * @param bytes number of bytes
     * @param events number of events (records)
     */
    public void record(long startTime, long endTime, int bytes, int events) {
        final long latency = time.elapsed(endTime, startTime);
        reportLatency.recordLatency(startTime, bytes, events, latency);
        window.recordLatency(startTime, bytes, events, latency);
        if (window.isOverflow()) {
            window.print(startTime, windowLogger, this);
            window.reset(startTime);
            if (isOverflow()) {
                print(startTime, loggerTotal, null);
                reset(startTime);
            }
        }
    }

    /**
     * print the periodic Latency Results.
     *
     * @param currentTime current time.
     */
    public void stopWindow(long currentTime) {
        window.print(currentTime, windowLogger, this);
        if (isOverflow()) {
            if (hashMapBytesCount > maxHashMapSizeBytes) {
                Printer.log.warn("Hash Map memory size: " + maxHashMapSizeMB +
                        " exceeded! Current HashMap size in MB: " + (hashMapBytesCount / PerlConfig.BYTES_PER_MB));
            } else {
                Printer.log.warn("Total Bytes: " + totalBytes + ",  Total Records:" + totalRecords +
                        ", Total Latency: "+  totalLatency );
            }
            print(currentTime, loggerTotal, null);
            start(currentTime);
        }
    }

    /**
     * print the Final Latency Results.
     *
     * @param endTime current time.
     */
    public void stop(long endTime) {
        if (window.totalRecords > 0) {
            window.print(endTime, windowLogger, this);
        }
        print(endTime, loggerTotal, null);
    }

}
