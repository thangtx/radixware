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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.wps.rwt.Events;


public class HttpQuery {

    public static final HttpQuery NO_QUERY = new HttpQuery(null, null);
    public static final HttpQuery DISPOSE_QUERY = new HttpQuery(null, null){

        @Override
        public boolean isDisposeRequest() {
            return true;
        }        
    };
    
    private static final String CLOSE_DIALOG_CALLBACK = "_dlg_close";
    private static final String PARAM_NAME_RQ_ID = "_rq_id_";    
    private final Map<String, String> params = new HashMap<>(21);
    private final List<EventSet> events = new LinkedList<>();
    private final boolean isDisposeRequest;
    private final ClientAuthData authData;

    public static class EventSet {

        private final Map<String, String> params = new HashMap<>(21);

        private EventSet(String paramString) {
            String[] elements = paramString.split("\\^");
            for (String e : elements) {
                int idx = e.indexOf("=");
                if (idx > 0) {
                    String name = e.substring(0, idx);
                    String value = e.substring(idx + 1);
                    try {
                        if (Events.ACTION_EVENT_ACTION_PARAM.equals(name)) {
                            byte[] bytes = Base64.decode(value);
                            if (bytes == null) {
                                params.put(name, value);
                            } else {
                                params.put(name, new String(bytes, FileUtils.XML_ENCODING));
                            }
                        } else {
                            params.put(name, value);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(HttpQuery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        public String getEventName() {
            return params.get(WpsEvents.HTTP_EVENT_MARK);
        }

        public String getEventParam() {
            return params.get(Events.ACTION_EVENT_ACTION_PARAM);
        }

        public String getEventWidgetId() {
            return params.get(WpsEvents.HTTP_MARK_WIDGET_ID);
        }

        public boolean isCloseDialogCallback() {
            return params.get(CLOSE_DIALOG_CALLBACK) != null;
        }

        public String getClosedDialogId() {
            return params.get(CLOSE_DIALOG_CALLBACK);
        }

        public String get(String name) {
            return params.get(name);
        }

        public boolean isEventRequest() {
            return params.get(WpsEvents.HTTP_EVENT_MARK) != null;
        }
    }

    public HttpQuery(final String queryString, final String authzHeader) {
        authData = authzHeader==null || authzHeader.isEmpty() ? null : new ClientAuthData(authzHeader);
        boolean disposeRequest = false;
        if (queryString != null && !queryString.isEmpty()) {
            String[] elements = queryString.split("&");
            for (String e : elements) {
                if (e.startsWith("_w_e")) {
                    EventSet set = new EventSet(e);
                    events.add(set);                    
                } else {
                    int idx = e.indexOf("=");
                    if (idx > 0) {
                        String name = e.substring(0, idx);
                        String value = e.substring(idx + 1);
                        params.put(name, value);
                    } else if (e.equals("dispose")) {
                        disposeRequest = true;
                    }
                }
            }
        }
        isDisposeRequest = disposeRequest;
    }
    
    public HttpQuery(final String queryString) {
        this(queryString, null);
    }

    ClientAuthData getAuthData(){
        return authData;
    }

    public boolean isDisposeRequest() {
        return isDisposeRequest;
    }
    
    public boolean isInitRequest(){
        for (HttpQuery.EventSet event : getEvents()) {
            if (Events.isActionEvent(event.getEventName()) 
                && "init".equals(Events.getActionName(event))) {
                return true;
            }
        }
        return false;
    }

    public List<EventSet> getEvents() {
        return events;
    }

    public String getId() {
        Object obj = get(PARAM_NAME_RQ_ID);
        return (String) obj;
    }

    public String get(String name) {
        return params.get(name);
    }

    private String rootId = null;

    public String getRootId() {
        if (rootId == null) {
            rootId = get("root");
            if (rootId != null) {
                int index = rootId.indexOf("-");
                if (index > 0) {
                    rootId = new String(rootId.substring(0, index));
                }
            }
        }
        return rootId;
    }
    
    public String getToken(){
        return get("token");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(e.getKey()).append('=').append(e.getValue());
        }
        if (!getEvents().isEmpty() && org.radixware.wps.rwt.Events.isActionEvent(getEvents().get(0).getEventName())){
            String actionName = org.radixware.wps.rwt.Events.getActionName(getEvents().get(0));
            final String widgetId = getEvents().get(0).getEventWidgetId();
            sb.append(String.format(" action \'%s\' for widget %s", actionName, widgetId));
        }
        return sb.toString();
    }
    
    public boolean isEmpty() {
        return params.isEmpty();
    }
}
