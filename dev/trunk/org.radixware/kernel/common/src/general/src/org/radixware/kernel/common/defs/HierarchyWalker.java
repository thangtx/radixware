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

package org.radixware.kernel.common.defs;

import java.util.*;


public class HierarchyWalker<T> {

    public interface Acceptor<T, R> {

        public void accept(Controller<R> controller, T radixObject);
    }

    public interface AbstractDefaultAcceptor<T> extends Acceptor<T, Object> {
    }

    public interface Controller<T> {

        public void stop();

        public void pathStop();

        public int dept();

        public void setResult(T result);

        public void setResultAndStop(T result);

        public void setResultAndPathStop(T result);

        public T getResult();
    }

    private static class ControllerImpl<T, R> implements Controller<T> {

        private int dept = 0;
        private T result;
        private boolean stopped;
        private boolean pathStopped;
        private Set accepted = null;

        @Override
        public void stop() {
            stopped = true;
        }

        public void accept(Object obj) {
            if (accepted == null) {
                accepted = new HashSet(11);
            }
            accepted.add(obj);
        }

        public boolean isAccepted(Object obj) {
            return accepted == null ? false : accepted.contains(obj);
        }

        @Override
        public void pathStop() {
            pathStopped = true;
        }

        @Override
        public int dept() {
            return dept;
        }

        @Override
        public void setResult(T result) {
            this.result = result;
        }

        @Override
        public T getResult() {
            return result;
        }

        @Override
        public void setResultAndStop(T result) {
            setResult(result);
            stop();
        }

        @Override
        public void setResultAndPathStop(T result) {
            setResult(result);
            pathStop();
        }
    }

    protected interface Processor<T> {

        public List<T> listBaseObjects(T radixObject);
    }
    private Processor processor;

    protected HierarchyWalker(Processor processor) {
        this.processor = processor;
    }

    private void process(ControllerImpl controller, Acceptor acceptor, T radixObject) {
        final List<T> base = processor.listBaseObjects(radixObject);

        ArrayList<T> tail = null;
        for (int i = 0, size = base.size(); i < size; i++) {
            final T obj = base.get(i);
            if (controller.isAccepted(obj)) {
                continue;
            }
            acceptor.accept(controller, obj);
            if (controller.stopped) {
                return;
            }
            controller.accept(obj);
            if (controller.pathStopped) {
                controller.pathStopped = false;
            } else {
                if (tail == null) {
                    tail = new ArrayList<>(5);
                }
                tail.add(obj);
            }

        }
        if (tail != null) {
            for (int i = 0, size = tail.size(); i < size; i++) {
                final T obj = tail.get(i);

                try {
                    controller.dept++;
                    process(controller, acceptor, obj);
                    if (controller.stopped) {
                        return;
                    }
                } finally {
                    controller.dept--;
                }
            }
        }
    }

    public <R> R go(final T root, final Acceptor<T, R> acceptor) {
        ControllerImpl<R, T> controller = new ControllerImpl<>();
        acceptor.accept(controller, root);
        controller.accept(root);
        if (controller.stopped || controller.pathStopped) {
            return controller.getResult();
        } else {
            controller.dept++;
            process(controller, acceptor, root);
            return controller.getResult();
        }
    }
}
