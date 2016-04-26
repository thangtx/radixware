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

package org.radixware.kernel.common.check;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.ThreadSafe;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


@ThreadSafe // thread safe - for remove listener
public class RadixProblemRegistry implements IProblemHandler {

    public static enum EChangeType {

        ADDED,
        REMOVED,
        CLEARED
    }

    public static class ChangedEvent extends RadixEvent {

        private final Set<RadixProblem> problems;
        private final EChangeType type;

        public ChangedEvent(EChangeType type, Set<RadixProblem> problems) {
            this.type = type;
            this.problems = problems;
        }

        public EChangeType getChangeType() {
            return type;
        }

        public Set<RadixProblem> getChangedProblems() {
            return problems;
        }
    }

    public static interface IChangeListener extends IRadixEventListener<ChangedEvent> {
    }
    private final RadixEventSource<IChangeListener, ChangedEvent> changesSupport = new RadixEventSource<IChangeListener, ChangedEvent>();
    private final Set<RadixProblem> problems = new HashSet<RadixProblem>();
    private final RadixObject.IRemoveListener radixObjectRemovedListener = new RadixObject.IRemoveListener() {

        @Override
        public void onEvent(RemovedEvent e) {
            clear(Collections.singleton(e.radixObject));
        }
    };
    private static final RadixProblemRegistry INSTANCE = new RadixProblemRegistry();

    private RadixProblemRegistry() {
    }

    public static final RadixProblemRegistry getDefault() {
        return INSTANCE;
    }

    public void addChangeListener(IChangeListener l) {
        changesSupport.addEventListener(l);
    }

    public void removeChangeListener(IChangeListener l) {
        changesSupport.removeEventListener(l);
    }

    /**
     * Clear all problems of specified Radix objects and its children.
     */
    public void clear(final Collection<? extends RadixObject> contexts) {
        final EChangeType changeType;
        final Set<RadixProblem> old = new HashSet<RadixProblem>();

        synchronized (this) {
            for (RadixObject context : contexts) {
                for (RadixProblem problem : problems) {
                    final RadixObject source = problem.getSource();
                    if (context == source || context.isParentOf(source)) {
                        old.add(problem);
                        source.getRemoveSupport().removeEventListener(radixObjectRemovedListener);
                    }
                }
            }
            if (old.isEmpty()) {
                return;
            }

            problems.removeAll(old);

            if (problems.isEmpty()) {
                changeType = EChangeType.CLEARED;
            } else {
                changeType = EChangeType.REMOVED;
            }
        }

        changesSupport.fireEvent(new ChangedEvent(changeType, old));
    }

    @Override
    public void accept(RadixProblem problem) {
        synchronized (this) {
            final RadixObject source = problem.getSource();
            Layer srcLayer = source.getLayer();
            if (srcLayer != null && srcLayer.isReadOnly()) {
                return;
            }
            problems.add(problem);
            source.getRemoveSupport().addEventListener(radixObjectRemovedListener);
        }
        changesSupport.fireEvent(new ChangedEvent(EChangeType.ADDED, Collections.singleton(problem)));
    }

    public void clearAll() {
        synchronized (this) {
            if (problems.isEmpty()) {
                return;
            }

            for (RadixProblem problem : problems) {
                final RadixObject source = problem.getSource();
                source.getRemoveSupport().removeEventListener(radixObjectRemovedListener);
            }
            problems.clear();
        }

        changesSupport.fireEvent(new ChangedEvent(EChangeType.CLEARED, null));
    }

    public Set<RadixProblem> getAllProblemSet() {
        synchronized (this) {
            return new HashSet<RadixProblem>(problems);
        }
    }

    public Set<RadixProblem> getProblemSet(RadixObject radixObject) {
        final Set<RadixProblem> result = new HashSet<RadixProblem>();

        synchronized (this) {
            for (RadixProblem problem : problems) {
                if (problem.getSource() == radixObject) {
                    result.add(problem);
                }
            }
        }

        return result;
    }
}
