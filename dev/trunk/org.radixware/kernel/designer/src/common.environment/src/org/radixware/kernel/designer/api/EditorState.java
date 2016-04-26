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

package org.radixware.kernel.designer.api;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.defs.RadixObject;


public class EditorState {

    public enum OpenState {

        OPENED, CLOSED
    }

    public static class Factory {

        public static EditorState createSimple(OpenState state) {
            return new EditorState(state);
        }

        public static EditorState createComplex(OpenState state) {
            return new ComplexEditorState(state);
        }
    }

    private static class ComplexEditorState extends EditorState {

        private final Map<RadixObject, EditorState> editorsState;
        private final Map<String, EditorState> controllsStates;
        private int scrollPosition = 0;

        private ComplexEditorState(OpenState state) {
            super(state);
            this.editorsState = new WeakHashMap<>();
            this.controllsStates = new HashMap<>();
        }

        @Override
        public EditorState getEditorState(RadixObject key) {
            return editorsState.get(key);
        }

        @Override
        public void putEditorState(RadixObject key, EditorState state) {
            if (state != null) {
                editorsState.put(key, state);
            }
        }

        @Override
        public EditorState getControllState(String key) {
            return controllsStates.get(key);
        }

        @Override
        public void putControllState(String key, EditorState state) {
            if (state != null) {
                controllsStates.put(key, state);
            }
        }

        @Override
        public boolean isComplex() {
            return true;
        }

        @Override
        public int getScrollPosition() {
            return scrollPosition;
        }

        @Override
        public void setScrollPosition(int scrollPosition) {
            this.scrollPosition = scrollPosition;
        }
    }
    private final OpenState state;

    private EditorState(OpenState state) {
        this.state = state;
    }

    public OpenState getState() {
        return state;
    }

    public EditorState getEditorState(RadixObject key) {
        return null;
    }

    public void putEditorState(RadixObject key, EditorState state) {
    }

    public EditorState getControllState(String key) {
        return null;
    }

    public void putControllState(String key, EditorState state) {
    }

    public boolean isComplex() {
        return false;
    }

    public int getScrollPosition() {
        return 0;
    }

    public void setScrollPosition(int scrollPosition) {
    }
}
