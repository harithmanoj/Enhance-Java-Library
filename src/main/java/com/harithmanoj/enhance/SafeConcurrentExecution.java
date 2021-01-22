//
//	This file is part of project Enhance Java Library
//
//	Copyright (C) 2021 Harith Manoj
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
   
package com.harithmanoj.enhance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SafeConcurrentExecution<V> {

    private static final int EXECUTOR_THREADS = 8;
    private final ExecutorService _concurrentExecutor = Executors.newFixedThreadPool(EXECUTOR_THREADS);

    private final List<Future<V>> _executingFutures = new ArrayList<>();
    private boolean _isExecutingMutable = false;
    private final Object _executorMetaSyncObject = new Object();

    SafeConcurrentExecution() {}

    public void waitForSync() throws Exception {
        for(Future<V> f : _executingFutures)
            f.get();
    }

    public Future<V> postTask(final Callable<V> fn, final boolean isMutable) {
        if(isMutable || _isExecutingMutable) {
            return _concurrentExecutor.submit(
                    new Callable<V>() {
                        @Override
                        public V call() throws Exception {

                            synchronized (_executingFutures) {
                                waitForSync();
                                _executingFutures.clear();
                            }
                            Future<V> ret = null;
                            synchronized (_executorMetaSyncObject) {
                                ret = _concurrentExecutor.submit(fn);
                                _executingFutures.add(ret);
                                _isExecutingMutable = isMutable;
                            }
                            return ret.get();
                        }
                    }
            );
        } else {
            Future<V> ret = null;
            synchronized (_executorMetaSyncObject) {
                ret = _concurrentExecutor.submit(fn);
            }
            synchronized (_executingFutures) {
                _executingFutures.add(ret);
            }
            return ret;
        }

    }

}