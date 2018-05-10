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
package org.radixware.wps.rwt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EHtmlColor;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.kernel.common.html.IHtmlUser;
import org.radixware.kernel.common.html.ToolTip;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.HttpSessionTerminatedError;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.events.AbstractEventFilter;
import org.radixware.wps.rwt.events.ContextMenuEventFilter;
import org.radixware.wps.rwt.events.ContextMenuHtmlEvent;
import org.radixware.wps.rwt.events.EHtmlEventType;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.uploading.FileUploader;
import org.radixware.wps.text.ECssPropertyName;
import org.radixware.wps.text.ECssTextOptionsStyle;
import org.radixware.wps.text.WpsFont;
import org.radixware.wps.text.WpsTextOptions;

public class UIObject implements IWidget, IHtmlUser {

    private static class PeriodicalTask implements IPeriodicalTask {

        private boolean started = true;
        private final TimerEventHandler handler;
        private Object userObject;

        private PeriodicalTask(TimerEventHandler handler) {
            this.handler = handler;
        }

        void Tick() {
            synchronized (this) {
                if (started) {
                    handler.processTimerEvent(null);
                }
            }
        }

        @Override
        public boolean isActive() {
            synchronized (this) {
                return started;
            }
        }

        @Override
        public void pause() {
            synchronized (this) {
                started = false;
            }
        }

        @Override
        public void resume() {
            synchronized (this) {
                started = true;
            }
        }

        @Override
        public void setUserObject(final Object userObj) {
            synchronized (this) {
                userObject = userObj;
            }
        }

        @Override
        public Object getUserObject() {
            synchronized (this) {
                return userObject;
            }
        }
    }

    public static class Anchors {

        public static class Anchor {

            private float part;
            private int offset;
            private UIObject target;

            public Anchor(float part, int offset) {
                this.part = part;
                this.offset = offset;
                this.target = null;
            }

            public Anchor(float part, int offset, UIObject target) {
                this.part = part;
                this.offset = offset;
                this.target = target;
            }
        }
        private Anchor left, right, top, bottom;
        private UIObject obj;

        private Anchors(UIObject obj) {
            this.obj = obj;
        }

        public Anchor getBottom() {
            return bottom;
        }

        public void setBottom(Anchor bottom) {
            this.bottom = bottom;
            updateAnchors();
        }

        public Anchor getLeft() {
            return left;
        }

        public void setLeft(Anchor left) {
            this.left = left;
            updateAnchors();
        }

        public Anchor getRight() {
            return right;
        }

        public void setRight(Anchor right) {
            this.right = right;
            updateAnchors();
        }

        public Anchor getTop() {
            return top;
        }

        public void setTop(Anchor top) {
            this.top = top;
            updateAnchors();
        }

        private void updateAnchors() {
            StringBuilder sb = new StringBuilder();
            if (top != null) {
                sb.append("t:").append(top.part).append(',').append(top.offset);
                if (top.target != null) {
                    sb.append(',').append(top.target.html.getId());
                }
            }
            if (left != null) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append("l:").append(left.part).append(',').append(left.offset);
                if (left.target != null) {
                    sb.append(',').append(left.target.html.getId());
                }
            }
            if (right != null) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append("r:").append(right.part).append(',').append(right.offset);
                if (right.target != null) {
                    sb.append(',').append(right.target.html.getId());
                }
            }
            if (bottom != null) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append("b:").append(bottom.part).append(',').append(bottom.offset);
                if (bottom.target != null) {
                    sb.append(',').append(bottom.target.html.getId());
                }
            }
            if (sb.length() > 0) {
                obj.html.setAttr("anchor", sb.toString());
            }
        }
    }

    public static class Properties {

        public static final String VISIBLE = "visible";
        public static final String FOCUSED = "focused";
    }

    public interface FocusListener {

        public void focusEvent(UIObject target, boolean focused);
    }

    public interface UIObjectPropertyListener {

        public void propertyChange(String name, Object oldValue, Object value);
    }

    @Override
    public void setToolTip(String toolTipText) {
        html.setAttr("title", toolTipText);
    }

    private ToolTip toolTip;
    
    public void setHtmlToolTip(ToolTip toolTip) {
        if (this.toolTip != null) {
            getHtml().remove(this.toolTip);
            this.toolTip = null;
        }
        if (toolTip != null) {
            toolTip.setCss("visibility", "hidden");
            toolTip.setCss("position", "absolute");
            this.toolTip = toolTip;
            getHtml().add(toolTip);
        } 
    }
    
    public String getToolTip() {
        return html.getAttr("title");
    }
    
    public void setContextMenu(RwtMenu menu) {
        this.menu = menu;
        this.menu.setMenuClosedListener(menuClosedListener);
        subscribeToEvent(new ContextMenuEventFilter(EKeyEvent.VK_CANCEL)); 
    }

    private final RwtMenu.ClosedListener menuClosedListener = new RwtMenu.ClosedListener() {
        @Override
        public void close() {
            UIObject.this.closeDropDown();
        }
    };
    
    public enum SizePolicy {

        PREFERRED,
        EXPAND,
        MINIMUM_EXPAND;
    }
    protected final Html html;
    private String objectName;
    private UIObject parent;
    private SizePolicy vSizePolicy = SizePolicy.MINIMUM_EXPAND;
    private SizePolicy hSizePolicy = SizePolicy.MINIMUM_EXPAND;
    private int preferredWidth;
    private int preferredHeight;
    private Anchors anchors;
    private List<UIObjectPropertyListener> propListeners = null;
    private List<FocusListener> focusListeners = null;
    private String[] fontStyleNames;
    private String[] fgColorStyleNames;
    private String[] bgColorStyleNames;
    private String[] textAlignStyleNames;
    private List<PeriodicalTask> timers = null;
    private volatile FileUploader fileUploader;
    private String defaultClassName;
    private List<AbstractEventFilter> eventList = null;
    private AbstractMenuContainer menuContainer;
    private RwtMenu menu;
    
    private List<PeriodicalTask> getTimers() {
        return timers;
    }

    public void addPropertyListener(UIObjectPropertyListener l) {
        synchronized (this) {
            if (propListeners == null) {
                propListeners = new LinkedList<>();
            }
            if (!propListeners.contains(l)) {
                propListeners.add(l);
            }
        }
    }

    public void removePropertyListener(UIObjectPropertyListener l) {
        synchronized (this) {
            if (propListeners != null) {
                propListeners.remove(l);
                if (propListeners.isEmpty()) {
                    propListeners = null;
                }
            }
        }
    }

    private void firePropertyChange(String name, Object oldValue, Object newValue) {
        synchronized (this) {
            if (propListeners != null) {
                for (UIObjectPropertyListener l : propListeners) {
                    l.propertyChange(name, oldValue, newValue);
                }
            }
        }
    }

    public void addFocusListener(FocusListener l) {
        synchronized (this) {
            if (focusListeners == null) {
                focusListeners = new LinkedList<>();
            }
            if (!focusListeners.contains(l)) {
                focusListeners.add(l);
            }
        }
    }

    public void removeFocusListener(FocusListener l) {
        synchronized (this) {
            if (focusListeners != null) {
                focusListeners.remove(l);
                if (focusListeners.isEmpty()) {
                    focusListeners = null;
                }
            }
        }
    }

    private void fireFocusChange(boolean focused) {
        synchronized (this) {
            if (focusListeners != null) {
                for (FocusListener l : focusListeners) {
                    l.focusEvent(this, focused);
                }
            }
        }
    }

    protected UIObject(Html html) {
        this.html = html;
        setDefaultClassName("rwt-ui-element");
        if (WebServerRunParams.getIsDevelopmentMode()){
            html.setAttr("jClassName", getClass().getName());
        }
    }

    public Anchors getAnchors() {
        synchronized (this) {
            if (anchors == null) {
                anchors = new Anchors(this);
            }
            return anchors;
        }
    }

    protected final void setDefaultClassName(String name) {
        if (getDefaultClassName() != null) {
            html.removeClass(defaultClassName);
        }
        defaultClassName = name;
        this.html.addClass(name);
    }

    protected final String getDefaultClassName() {
        return defaultClassName;
    }

    public String getHtmlId() {
        return this.html.getId();
    }

    @Override
    public void setObjectName(final String name) {
        this.objectName = name;
        final IClientEnvironment environment = IClientEnvironment.Locator.getEnvironment();
        if (environment instanceof WpsEnvironment
            && ((WpsEnvironment)environment).getRunParams().writeObjectNamesToHtml()){
            html.setAttr("objName", objectName);
        }
    }

    @Override
    public String getObjectName() {
        return this.objectName;
    }

    @Override
    public boolean isDisposed() {
        return getParent() == null;
    }

    @Override
    public Object findChild(Class<?> childClass, String childObjectName) {
        return null;
    }

    @Override
    public Html getHtml() {
        return html;
    }

    public int getTabIndex() {
        String index = html.getAttr("tabindex");
        if (index == null) {
            return -1;
        } else {
            try {
                return Integer.valueOf(index);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    public void setTabIndex(int index) {
        if (index == -1) {
            html.setAttr("tabindex", null);
        } else {
            html.setAttr("tabindex", index);
        }
    }

    @Override
    public int width() {
        return getWidth();
    }

    @Override
    public int height() {
        return getHeight();
    }

    protected String[] clientScriptsRequired() {
        return null;
    }

    protected String[] clientCssRequired() {
        return null;
    }

    public interface Visitor {

        public void accept(UIObject obj);
    }

    public void setFocused(boolean focused) {
        if (focused) {
            RootPanel root = findRoot();
            if (root != null) {
                root.registerFocused(this);
            }
        }
        fireFocusChange(focused);
    }

    public void visit(Visitor visitor) {
        visitor.accept(this);
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        if (isEnabled) {
            html.setAttr("disabled", null);
            html.removeClass("ui-state-disabled");
        } else {
            html.setAttr("disabled", true);
            html.addClass("ui-state-disabled");
        }
    }

    @Override
    public boolean isVisible() {
        return !"none".equals(html.getCss("display"));
    }

    @Override
    public void setVisible(boolean isVisible) {
        boolean oldVal = isVisible();
        if (oldVal != isVisible) {
            if (isVisible) {
                html.setCss("display", null);
            } else {
                html.setCss("display", "none");
            }
            firePropertyChange(Properties.VISIBLE, oldVal, isVisible);
        }
    }

    @Override
    public boolean isEnabled() {
        boolean selfValue = html.getAttr("disabled") == null;
        if (selfValue) {
            UIObject p = getParent();
            if (p == null) {
                return selfValue;
            } else {
                return parent.isEnabled();
            }
        } else {
            return false;
        }
    }

    public UIObject findObjectByHtmlId(String id) {
        if (html.getId().equals(id)) {
            return this;
        } else {
            if (menuContainer != null) {
                return menuContainer.findObjectByHtmlId(id);
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void subscribeToEvent(AbstractEventFilter newEventFilter) {
        boolean isExists = false;
        if (eventList == null) {
            eventList = new LinkedList<>();
        }
        if (eventList.isEmpty()) {
            eventList.add(newEventFilter);
            addEventFilterToHmtl(newEventFilter);
        } else {
            for (AbstractEventFilter eventFilter : eventList) {
                if (eventFilter.getType().equals(newEventFilter.getType())) {
                    if (eventFilter.merge(newEventFilter)) {
                        addEventFilterToHmtl(eventFilter);
                    }
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                eventList.add(newEventFilter);
                addEventFilterToHmtl(newEventFilter);
            }
        }
    }
    
    private void addEventFilterToHmtl(AbstractEventFilter eventFilter) {
        if (eventFilter instanceof ContextMenuEventFilter) {
            this.getHtml().setAttr("contextMenu", "true");
        }
        this.getHtml().setAttr(eventFilter.getType().getValue() + "Params", eventFilter.toJSONString());
        this.getHtml().setAttr(eventFilter.getType().getValue(), "$RWT.onFilteredEvent");
    }
    
    public void unsubscribeFromEvent(EHtmlEventType eventType) {
        if (eventList!=null && !eventList.isEmpty()){
            if (eventType.equals(EHtmlEventType.CONTEXTMENU)) {
                this.getHtml().setAttr("contextMenu", null);
            }
            final List<AbstractEventFilter> copyList = new LinkedList<>(eventList);
            for (AbstractEventFilter eventFilter : copyList) {
                if (eventFilter.getType()==eventType) {
                    eventList.remove(eventFilter);
                    this.getHtml().setAttr(eventType.getValue() + "Params", null);
                    this.getHtml().setAttr(eventType.getValue(), null);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void unmerge(AbstractEventFilter newEventFilter) {
        if (eventList != null || !eventList.isEmpty()) {
            for (AbstractEventFilter eventFilter : eventList) {
                if (eventFilter.getType().equals(newEventFilter.getType())) {
                    if (eventFilter.unmerge(newEventFilter)) {
                        this.getHtml().setAttr(eventFilter.getType().getValue() + "Params", eventFilter.toJSONString());
                        this.getHtml().setAttr(eventFilter.getType().getValue(), "$RWT.onFilteredEvent");
                    }
                }
            }
        }
    }

    protected void processHtmlEvent(HtmlEvent event) {
        if (event instanceof ContextMenuHtmlEvent) {
            if (menuContainer != null) {
                UIObject.this.closeDropDown();
            } else if (beforeShowContextMenu()) {
                    exposeMenu(((ContextMenuHtmlEvent)event).getParams());
                }
            }
    }
    
    private void exposeMenu(String coordinates) {
        this.getHtml().setAttr("menuId", menu.getHtmlId());
        menu.getHtml().setAttr("coordinates", coordinates);
        menu.getHtml().setAttr("handler_id", this.getHtmlId()); //to getNextZIndex in js dropdown layout
        menuContainer = new AbstractMenuContainer(menu, this.findRoot());
    }
    
    private void closeDropDown() {
        if (menuContainer != null) {
            menuContainer.destroy();
            menuContainer = null;
        }
    }
    
    protected boolean beforeShowContextMenu() {
        return true;
    }

    public void processEvent(String eventString, String paramString) {
        if (Events.isFocusedEvent(eventString)) {
            setFocused(true);
        } else if (Events.isDefocusedEvent(eventString)) {
            setFocused(false);
        }
    }

    public void processAction(final String actionName, final String actionParam) {
        if (fileUploader != null) {
            if ("file-selected".equals(actionName)) {
                fileUploader.processFileSelectedAjaxAction(actionParam);
            } else if ("upload-started".equals(actionName)) {
                fileUploader.processUploadStartedAjaxAction();
            } else if ("upload-rejected".equals(actionName)) {
                fileUploader.processUploadRejectedAjaxAction();
            } 
        }
        if ("close-drop-down".equals(actionName)) {
            closeDropDown();
        } else if ("filteredevent".equals(actionName)) {
            final HtmlEvent event = HtmlEvent.Factory.parseHtmlEventFromJsonString(actionParam);
            if (event!=null){
                processHtmlEvent(event);
            }
        }
    }

    public void notifySent() {
        RootPanel root = findRoot();
        if (root != null) {
            root.getHtml().rendered(root);
        } else {
            this.getHtml().rendered(null);
        }
    }

    public void accept(HttpQuery query) {
        try {
            RootPanel root = findRoot();
            if (root != null) {
                root.resetFocused();
            }
            List<HttpQuery.EventSet> events = query.getEvents();
            for (HttpQuery.EventSet event : events) {
                if (event.isEventRequest()) {
                    final String eventName = event.getEventName();
                    if (Events.isRenderedEvent(eventName)) {
                        if (root == null) {
                            root = findRoot();
                        }
                        List<Runnable> actions = null;
                        if (root != null) {

                            //root.html.rendered();
                            if (!(this instanceof ProgressHandleManager)) {
                                //rendered callback should be executed only on consistent state of
                                //object
                                actions = ((UIObject) root).renderedCallback(query);
                            }

                        } else {
                            //this.html.rendered();
                            actions = this.renderedCallback(query);
                        }
                        if (actions != null) {
                            for (Runnable r : actions) {
                                r.run();
                            }
                        }
                    } else if (Events.isTimerEvent(eventName)) {
                        if (root == null) {
                            root = findRoot();
                        }
                        if (root != null) {
                            root.startTimerTracking();
                            root.activeTimerCount(((UIObject) root).processTimerEvent(false));
                        }
                    } else {
                        if (root == null) {
                            root = findRoot();
                        }
                        if (root != null) {
                            root.startTimerTracking();
                        }
                        String widgetId = event.getEventWidgetId();
                        if ("root".equals(widgetId)) {
                            if (Events.isActionEvent(eventName)) {
                                String actionName = Events.getActionName(event);
                                if ("init".equals(actionName)) {
                                    ((WpsEnvironment) getEnvironment()).getConfigStore().acceptInitParams(event.getEventParam());
                                }
                            }
                        }
                        if (RootPanel.DEBUG) {
                            System.out.println("Widget id = " + widgetId);
                        }
                        if (widgetId != null) {
                            UIObject target = this.findObjectByHtmlId(widgetId);
                            if (target != null) {
                                if (Events.isActionEvent(eventName)) {
                                    target.processAction(Events.getActionName(event), event.getEventParam());
                                } else {
                                    target.processEvent(eventName, event.getEventParam());
                                }
                            }
                        }
                        if (root == null) {
                            root = findRoot();
                        }
                        if (root != null) {
                            root.activeTimerCount(((UIObject) root).processTimerEvent(true));
                        }
                    }
                }
            }
        } catch (HttpSessionTerminatedError ex) {
            ex.trace();
        } catch (Throwable e) {
            try {
                getEnvironment().processException(e);
            } catch (Throwable otherException) {
                getEnvironment().getTracer().error(e);
                getEnvironment().getTracer().error(otherException);
            }
        }
    }

    private List<Runnable> renderedCallback(final HttpQuery query) {
        final List<Runnable> postCallbackActions = new LinkedList<>();
        visit(new Visitor() {
            @Override
            public void accept(UIObject obj) {
                Runnable runnable = obj.componentRendered(query);
                if (runnable != null) {
                    postCallbackActions.add(runnable);
                }
            }
        });
        return postCallbackActions;
    }
    private final Object timersLock = new Object();

    protected final int processTimerEvent(final boolean noTick) {
        final int[] count = new int[]{0};
        final List<PeriodicalTask> tasks = new LinkedList<>();
        visit(new Visitor() {
            @Override
            public void accept(UIObject obj) {
                List<PeriodicalTask> timers = null;
                synchronized (obj.timersLock) {
                    if (obj.timers != null) {
                        timers = new ArrayList<>(obj.timers);
                    }
                }

                if (timers != null) {
                    for (PeriodicalTask t : timers) {
                        tasks.add(t);
                    }
                }

            }
        });
        for (PeriodicalTask t : tasks) {
            if (!noTick) {
                t.Tick();
            }
            if (t.isActive()) {
                count[0]++;
            }
        }
        return count[0];
    }

    protected Runnable componentRendered(HttpQuery query) {
        return null;
    }

    public UIObject getParent() {
        return parent;
    }

    public void setParent(UIObject parent) {
        this.parent = parent;
        if (parent == null) {
            if (fileUploader != null) {
                fileUploader.dispose();
            }
            html.renew();
        }
    }

    public RootPanel findRoot() {
        if (parent == null) {
            return null;
        } else {
            return parent.findRoot();
        }
    }

    private int getDimensionInPx(String name) {
        String w = html.getCss(name);
        if (w != null && w.endsWith("px")) {
            int index = w.indexOf("px");
            try {
                return Integer.parseInt(w.substring(0, index));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private float getDimensionInPercent(String name) {
        String w = html.getCss(name);
        if (w != null && w.endsWith("%")) {
            int index = w.indexOf("%");
            try {
                return Float.parseFloat(w.substring(0, index));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public int getWidth() {
        return getDimensionInPx(Html.CSSRule.WIDTH);
    }

    public void setWidth(int w) {
        html.setCss(Html.CSSRule.WIDTH, String.valueOf(w) + "px");
    }

    public int getHeight() {
        return getDimensionInPx(Html.CSSRule.HEIGHT);
    }

    public void setHeight(int h) {
        html.setCss(Html.CSSRule.HEIGHT, String.valueOf(h) + "px");
    }

    public float getHCoverage() {
        return getDimensionInPercent(Html.CSSRule.WIDTH);
    }

    public float getVCoverage() {
        return getDimensionInPercent(Html.CSSRule.HEIGHT);
    }

    public void setHCoverage(float w) {
        html.setCss(Html.CSSRule.WIDTH, String.valueOf(w) + "%");
    }

    public void setVCoverage(float w) {
        html.setCss(Html.CSSRule.HEIGHT, String.valueOf(w) + "%");
    }

    public SizePolicy getHSizePolicy() {
        return hSizePolicy;
    }

    public SizePolicy getVSizePolicy() {
        return vSizePolicy;
    }

    public void setHSizePolicy(SizePolicy hSizePolicy) {
        this.hSizePolicy = hSizePolicy;
        switch (this.hSizePolicy) {
            case PREFERRED:
                setWidth(preferredWidth);
                break;
            case EXPAND:
                setHCoverage(100);
                break;
            case MINIMUM_EXPAND:
                html.setCss(Html.CSSRule.WIDTH, null);
                break;
        }
    }

    public void setVSizePolicy(SizePolicy vSizePolicy) {
        this.vSizePolicy = vSizePolicy;
        switch (this.vSizePolicy) {
            case PREFERRED:
                setHeight(preferredHeight);
                break;
            case EXPAND:
                setVCoverage(100);
                break;
            case MINIMUM_EXPAND:
                html.setCss(Html.CSSRule.HEIGHT, null);
                break;
        }
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
        switch (this.vSizePolicy) {
            case PREFERRED:
                setHeight(preferredHeight);
                break;
        }
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
        switch (this.hSizePolicy) {
            case PREFERRED:
                setWidth(preferredWidth);
                break;
        }
    }

    public void setSizePolicy(SizePolicy hPolicy, SizePolicy vPolicy) {
        setVSizePolicy(vPolicy);
        setHSizePolicy(hPolicy);
    }

    protected ICssStyledItem getBackgroundHolder() {
        return html;
    }

    public void setBackground(Color c) {
        getBackgroundHolder().setCss("background-color", c == null ? null : color2Str(c));
    }

    protected ICssStyledItem getForegroundHolder() {
        return html;
    }

    public Color getBackground() {
        String css = getBackgroundHolder().getCss("background-color");
        if (css == null) {
            return null;
        } else {
            return colorFromStr(css);
        }
    }

    public void setBorderColor(Color c) {
        html.setCss("border-color", c == null ? null : color2Str(c));
    }

    public Color getBorderColor() {
        String css = html.getCss("border-color");
        if (css == null) {
            return null;
        } else {
            return colorFromStr(css);
        }
    }

    public void setForeground(Color c) {
        getForegroundHolder().setCss("color", c == null ? null : color2Str(c));
    }

    public Color getForeground() {
        String css = getForegroundHolder().getCss("color");
        if (css == null) {
            return null;
        } else {
            return colorFromStr(css);
        }
    }

    protected static Color colorFromStr(String str) {
        try {
            return Color.decode(str);
        } catch (NumberFormatException ex) {
            EHtmlColor color;
            try{
                color = EHtmlColor.forValue(str);
            } catch (IllegalArgumentException exeption) {
                return null;
            }
            return Color.decode(color.getHexCode());
        }
    }

    protected static String color2Str(Color c) {
        String r = Integer.toHexString(c.getRed());
        String g = Integer.toHexString(c.getGreen());
        String b = Integer.toHexString(c.getBlue());
        if (r.length() == 1) {
            r = "0" + r;
        }
        if (g.length() == 1) {
            g = "0" + g;
        }
        if (b.length() == 1) {
            b = "0" + b;
        }

        return "#" + r + g + b;
    }

    protected boolean fontSizePersistent() {
        return true;
    }

    protected ICssStyledItem getFontOptionsHolder() {
        return html;
    }

    public void setFont(final WpsFont font) {
        if (getFontOptionsHolder() != null) {
            removeStyles(fontStyleNames, getFontOptionsHolder());
            if (font == null) {
                for (ECssPropertyName property : ECssPropertyName.FONT_PROPERTIES) {
                    getFontOptionsHolder().setCss(property.getPropertyName(), null);
                }
                fontStyleNames = null;
            } else {
                final List<String> newStyleNames = new LinkedList<>();
                if (!font.getPredefinedCssStyles().isEmpty()) {
                    for (ECssTextOptionsStyle style : font.getPredefinedCssStyles()) {
                        if (!getFontOptionsHolder().containsClass(style.getStyleName())) {
                            newStyleNames.add(style.getStyleName());
                        }
                    }
                    if (!newStyleNames.isEmpty()) {
                        fontStyleNames = new String[newStyleNames.size()];
                        for (int i = newStyleNames.size() - 1; i >= 0; i--) {
                            fontStyleNames[i] = newStyleNames.get(i);
                        }
                    } else {
                        fontStyleNames = null;
                    }
                }
                font.applyTo(getFontOptionsHolder(), !fontSizePersistent());
            }
        }
    }

    public WpsFont getFont() {
        return getFontOptionsHolder() == null ? null : WpsFont.Factory.fromHtml(getFontOptionsHolder());
    }

    public void setTextOptions(final WpsTextOptions options) {
        if (options.getFont() != null) {
            setFont(options.getFont());
        }
        removeStyles(fgColorStyleNames, getForegroundHolder());
        removeStyles(bgColorStyleNames, getBackgroundHolder());
        removeStyles(textAlignStyleNames, getFontOptionsHolder());
        if (options.getPredefinedCssStyles().isEmpty()) {
            if (isEnabled()) {
                setBackground(options.getBackgroundColor());
            }
            if (isEnabled()) {
                setForeground(options.getForegroundColor());
            }
            if (getFontOptionsHolder() != null && options.getAlignment() != null) {
                getFontOptionsHolder().setCss(ECssPropertyName.TEXT_ALIGNMENT.getPropertyName(), options.getAlignment().getCssPropertyValue());
            }
            bgColorStyleNames = null;
            fgColorStyleNames = null;
            textAlignStyleNames = null;
        } else {
            final EnumSet<ECssTextOptionsStyle> styles = options.getPredefinedCssStyles();
            setBackground(null);
            setForeground(null);
            if (getFontOptionsHolder() != null) {
                getFontOptionsHolder().setCss(ECssPropertyName.TEXT_ALIGNMENT.getPropertyName(), null);
            }
            if (isEnabled()) {
                if (options.getBackgroundColor() != null) {
                    bgColorStyleNames = addNewStyles(getBackgroundHolder(), styles);
                } else {
                    bgColorStyleNames = null;
                }
                if (options.getForegroundColor() != null) {
                    fgColorStyleNames = addNewStyles(getForegroundHolder(), styles);
                } else {
                    fgColorStyleNames = null;
                }
            } else {
                bgColorStyleNames = null;
                fgColorStyleNames = null;
            }
            if (options.getAlignment() != null) {
                textAlignStyleNames = addNewStyles(getFontOptionsHolder(), styles);
            } else {
                textAlignStyleNames = null;
            }
        }
    }

    public void setBorderBoxSizingEnabled(final boolean isEnabled) {
        if (isEnabled) {
            html.addClass("rwt-ui-element-with-border");
        } else {
            html.removeClass("rwt-ui-element-with-border");
        }
    }

    private static void removeStyles(final String[] styleNames, final ICssStyledItem html) {
        if (styleNames != null && html != null) {
            for (String styleName : styleNames) {
                html.removeClass(styleName);
            }
        }
    }

    private static String[] addNewStyles(final ICssStyledItem html, final EnumSet<ECssTextOptionsStyle> styles) {
        if (html != null && !styles.isEmpty()) {
            final List<String> newStyleNames = new LinkedList<>();
            for (ECssTextOptionsStyle style : styles) {
                if (!html.containsClass(style.getStyleName())) {
                    newStyleNames.add(style.getStyleName());
                    html.addClass(style.getStyleName());
                }
            }
            if (!newStyleNames.isEmpty()) {
                final String[] result = new String[newStyleNames.size()];
                for (int i = newStyleNames.size() - 1; i >= 0; i--) {
                    result[i] = newStyleNames.get(i);
                }
                return result;
            }
        }
        return null;
    }

    protected IClientApplication getApplication() {
        RootPanel root = findRoot();
        IClientApplication app;
        if (root != null && root != this) {
            app = root.getApplication();
        } else {
            final Thread t = Thread.currentThread();
            if (t instanceof IContextEnvironment) {
                app = ((IContextEnvironment) t).getClientEnvironment().getApplication();
            } else {
                app = null;
            }
        }
        return app;
    }

    public void setClientHandler(String customEventName, String code) {
        setClientHandler(html, customEventName, code);
    }

    protected void setClientHandler(Html target, String customEventName, String code) {
        target.setAttr("rwt_f_" + customEventName, code);
    }

    public IClientEnvironment getEnvironment() {
        return getEnvironmentStatic();
    }

    protected static IClientEnvironment getEnvironmentStatic() {
        final Thread t = Thread.currentThread();
        if (t instanceof IContextEnvironment) {
            return ((IContextEnvironment) t).getClientEnvironment();
        } else {
            return null;
        }
    }

    public void setLeft(int left) {
        html.setCss("left", left + "px");
        html.setCss("position", "absolute");
    }

    public int getLeft() {
        String val = html.getCss("left");
        if (val != null && "absolute".equals(html.getCss("position"))) {
            return getDimensionInPx("left");
        } else {
            return 0;
        }
    }

    public void setTop(int top) {
        html.setCss("top", top + "px");
        html.setCss("position", "absolute");
    }

    public int getTop() {
        String val = html.getCss("top");
        if (val != null && "absolute".equals(html.getCss("position"))) {
            return getDimensionInPx("top");
        } else {
            return 0;
        }
    }

    public void unsetLocation() {
        html.setCss("top", null);
        html.setCss("left", null);
        unsetPosition();
    }

    public void unsetSize() {
        html.setCss("width", null);
        html.setCss("height", null);
    }

    public void unsetWidth() {
        html.setCss("width", null);
    }

    public void unsetHeight() {
        html.setCss("height", null);
    }

    public void unsetPosition() {
        html.setCss("position", null);
    }

    public void setMinimumWidth(int w) {
        html.setCss("min-width", String.valueOf(w) + "px");
    }

    public int getMinimumWidth() {
        return getDimensionInPx("min-width");
    }

    public void setMinimumHeight(int h) {
        html.setCss("min-height", String.valueOf(h) + "px");
    }

    public int getMinimumHeight() {
        return getDimensionInPx("min-height");
    }

    public void setPersistenceKey(String key) {
        html.setAttr("ccid", key);
    }

    public String getPersistenceKey() {
        return html.getAttr("ccid");
    }

    @Override
    public IPeriodicalTask startTimer(TimerEventHandler handler) {
        synchronized (this) {
            if (timers == null) {
                timers = new LinkedList<>();
            }
            PeriodicalTask result = new PeriodicalTask(handler);
            timers.add(result);
            return result;
        }
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public void killTimer(final IPeriodicalTask timer) {
        synchronized (this) {
            if (timers != null && timers.contains(timer)) {
                timer.pause();
                timers.remove(timer);
                if (timers.isEmpty()) {
                    timers = null;
                }
            }
        }
    }

    public void setFileUploader(final FileUploader uploader) {
        fileUploader = uploader;
    }

    public FileUploader getFileUploader() {
        return fileUploader;
    }     
}