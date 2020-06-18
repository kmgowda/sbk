/**
 * Copyright (c) KMG. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.sbk.FoundationDB;

import com.apple.foundationdb.Database;
import io.sbk.api.Parameters;
import io.sbk.api.Writer;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Class for File Writer.
 */
public class FoundationDBWriter implements Writer<byte[]> {
    final private Database db;
    private long key;

    public FoundationDBWriter(int id, Parameters params, Database db) throws IOException {
        this.key = id * Integer.MAX_VALUE;
        this.db = db;
    }

    @Override
    public CompletableFuture writeAsync(byte[] data) throws IOException {
        return db.runAsync(tx -> {
           tx.set(FoundationDB.longToBytes(key), data);
           key++;
           return null;
        });
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws  IOException {
    }
}