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

package org.radixware.kernel.designer.ads.editors.refactoring;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public final class OperationStatus {

    public static enum EEventType {

        WARNING, ERROR, INFO
    }

    public static final class Event {

        private EEventType type;
        private String message;

        public Event(EEventType type, String message) {
            this.type = type;
            this.message = message;
        }

        public EEventType getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + Objects.hashCode(this.type);
            hash = 53 * hash + Objects.hashCode(this.message);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Event other = (Event) obj;
            if (this.type != other.type) {
                return false;
            }
            if (!Objects.equals(this.message, other.message)) {
                return false;
            }
            return true;
        }
    }
    public static final OperationStatus OK = new OperationStatus();
    private final Set<Event> events;

    public OperationStatus() {
        events = new HashSet<>();
    }

    public OperationStatus(Event... events) {
        this();

        for (final Event event : events) {
            addEvent(event);
        }
    }

    public boolean isValid() {
        for (final Event event : events) {
            if (event.getType() == EEventType.ERROR) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public final void merge(OperationStatus status) {
        assert this != OK;

        events.addAll(status.events);
    }

    public final void addEvent(Event e) {
        assert this != OK;

        events.add(e);
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<Event> getEvents(EEventType type) {
        if (type == null) {
            return Collections.EMPTY_SET;
        }

        final Set<Event> result = new HashSet<>();
        for (final Event event : events) {
            if (event.getType() == type) {
                result.add(event);
            }
        }

        return result;
    }
}