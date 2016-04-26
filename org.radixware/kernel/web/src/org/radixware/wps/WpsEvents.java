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

package org.radixware.wps;


public class WpsEvents {

    public static final String HTTP_EVENT_MARK = "_w_e";
    public static final String HTTP_MARK_WIDGET_ID = "_wid";

//    public static abstract class Event {
//
//        protected final WpsObject object;
//
//        public Event(WpsObject object) {
//            this.object = object;
//        }
//
//        public abstract String getName();
//
//        public abstract String getDisplayName();
//
//        public abstract void execute();
//    }
    private static final String ACTION_EVENT_NAME = "_ae";
    private static final String ACTION_EVENT_ACTION_NAME = "_a";
    public static final String ACTION_EVENT_ACTION_PARAM = "_ap";

//    public static class ActionEvent extends Event {
//
//        private final String actionName;
//        private final String actionParam;
//
//        public ActionEvent(WpsObject object, String actionName, String actionParam) {
//            super(object);
//            this.actionName = actionName;
//            this.actionParam = actionParam;
//        }
//
//        @Override
//        public String getName() {
//            return ACTION_EVENT_NAME;
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "Action";
//        }
//
//        @Override
//        public void execute() {
//            System.out.println("Action is ready to execute");
//            WpsAction action = object.getActionSupport().findAction(actionName);
//            if (action != null) {
//                action.execute(object, actionParam);
//            }
//        }
//    }
    private static final String PROPERTY_CHANGE_EVENT_NAME = "_pc";
    private static final String PROPERTY_CHANGE_EVENT_PROPERTY_NAME = "_pn";
    private static final String PROPERTY_CHANGE_EVENT_PROPERTY_VALUE = "_pv";

//    public static class PropertyChange extends Event {
//
//        public final String value;
//        public final String propertyName;
//
//        public PropertyChange(WpsObject object, String propertyName, String value) {
//            super(object);
//            this.value = value;
//            this.propertyName = propertyName;
//        }
//
//        @Override
//        public String getName() {
//            return PROPERTY_CHANGE_EVENT_NAME;
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "Property Change";
//        }
//
//        @Override
//        public void execute() {
//            WpsProperty prop = object.getPropertySupport().findProperty(propertyName);
//            if (prop != null) {
//                prop.valueChanged(value);
//            }
//        }
//    }

//    public static Event getObjectEvent(WpsObject w, HttpQuery query) {
//        String eventName = query.get(HTTP_EVENT_MARK);
//        if (eventName != null) {
//            if (PROPERTY_CHANGE_EVENT_NAME.equals(eventName)) {
//                String propertyName = query.get(PROPERTY_CHANGE_EVENT_PROPERTY_NAME);
//                String propertyValue = query.get(PROPERTY_CHANGE_EVENT_PROPERTY_VALUE);
//                if (propertyName != null && propertyValue != null) {
//                    return new PropertyChange(w, propertyName, propertyValue);
//                }
//            } else if (ACTION_EVENT_NAME.equals(eventName)) {
//                String actionName = query.get(ACTION_EVENT_ACTION_NAME);
//                if (actionName != null) {
//                    return new ActionEvent(w, actionName, query.get(ACTION_EVENT_ACTION_PARAM));
//                }
//            }
//        }
//        return null;
//    }
}
