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

package org.radixware.kernel.server.meta.presentations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.meta.RadDefinition;

public final class RadNotificationItemDef extends RadDefinition {

    private final String titleEng;
    private final String titleNtn;
    private final List<State> states;
    private final State defaultState;

    public RadNotificationItemDef(
            final Id id,
            final String titleEng,
            final String titleNtn,
            final State[] states,
            final String defStateName) {
        super(id);
        this.titleEng = titleEng;
        this.titleNtn = titleNtn;
        if (states != null) {
            this.states = Collections.unmodifiableList(Arrays.asList(states));
        } else {
            this.states = Collections.emptyList();
        }
        if (defStateName != null) {
            State defSt = null;
            for (State st : this.states) {
                if (st.getName().equals(defStateName)) {
                    defSt = st;
                    break;
                }
            }
            this.defaultState = defSt;
        } else {
            this.defaultState = null;
        }
    }

    /**
     * @return the titleEng
     */
    public String getTitleEng() {
        return titleEng;
    }

    /**
     * @return the titleNtn
     */
    public String getTitleNtn() {
        return titleNtn;
    }

    /**
     * @return the states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @return the defaultState
     */
    public State getDefaultState() {
        return defaultState;
    }

    public static final class State {

        private final String name;
        private final String messageEng;
        private final String messageNtn;

        public State(final String name_, final String msgEng_, final String msgNtn_) {
            name = name_;
            messageEng = msgEng_;
            messageNtn = msgNtn_;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the messageEng
         */
        public String getMessageEng() {
            return messageEng;
        }

        /**
         * @return the messageNtn
         */
        public String getMessageNtn() {
            return messageNtn;
        }
    }
}
