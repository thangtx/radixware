/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.utils;


public abstract class IterableWalker<ItemType> implements IWalker<ItemType>, Iterable<ItemType> {

    public static abstract class Acceptor<ItemType, ResultType> implements IWalker.IAcceptor<ItemType, ResultType> {

        private boolean stop = false;
        private ResultType result;

        public Acceptor() {
        }

        public Acceptor(ResultType result) {
            this.result = result;
        }

        @Override
        public boolean isStopped() {
            return stop;
        }

        @Override
        public ResultType getResult() {
            return result;
        }

        protected void cancel() {
            stop = true;
        }

        protected void setResult(ResultType result) {
            this.result = result;
        }
    }

    @Override
    public <ResultType> ResultType walk(IWalker.IAcceptor<ItemType, ResultType> acceptor) {
        if (acceptor == null) {
            return null;
        }
        for (final ItemType item : this) {
            acceptor.accept(item);
            if (acceptor.isStopped()) {
                break;
            }
        }
        return acceptor.getResult();
    }
}