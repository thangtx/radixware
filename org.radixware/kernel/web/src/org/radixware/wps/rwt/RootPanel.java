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

import java.awt.Dimension;
import org.radixware.kernel.common.html.Html;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.*;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.html.IHtmlContext;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.icons.images.TemporaryIcon;
import org.radixware.wps.rwt.uploading.UploadHandler;

public abstract class RootPanel extends AbstractContainer implements IHtmlContext {

    private Set<String> postedScripts = null;
    private Set<String> postedCss = null;
    private Map<String, String> cookies = null;
    public static boolean DEBUG = false;
    private WeakReference<UIObject> lastFocused = null;
    private String uuid = UUID.randomUUID().toString();
    private int activeTimerCount = 0;
    private boolean timerStateChaged = false;
    private final Object uploadHandlersSemaphore = new Object();
    private Map<String, UploadHandler> uploadHandlersByContextObjectId;
    private Map<String, UploadHandler> uploadHandlersById;

    public RootPanel() {
        super(new Html("body"));
        html.setCss("margin", "0");
        html.setCss("padding", "0");
        html.layout("$RWT._layout");
    }

    private volatile boolean timerStateProcessed = false;

    void startTimerTracking() {
        timerStateProcessed = false;
    }

    void activeTimerCount(int count) {
        if (timerStateProcessed) {
            return;
        }
        if (activeTimerCount == 0 && count != 0) {
            timerStateChaged = true;
        } else if (activeTimerCount != 0 && count == 0) {
            timerStateChaged = true;
        } else {
            timerStateChaged = false;
        }
        activeTimerCount = count;
        timerStateProcessed = true;
    }

    public String getQueryId() {
        return html.getId() + "-" + uuid;
    }
    private final List<StatusBarListener> sbListeners = new LinkedList<>();

    public interface StatusBarListener {

        public void statusBarOpened(MainStatusBar statusBar);

        public void statusBarClosed(MainStatusBar statusBar);
    }

    public void addStatusBarListener(StatusBarListener l) {
        synchronized (sbListeners) {
            if (!sbListeners.contains(l)) {
                sbListeners.add(l);
                setupStatusBarListener(l);
            }
        }
    }

    protected void setupStatusBarListener(StatusBarListener listener) {
    }

    public void removeStatusBarListener(StatusBarListener l) {
        synchronized (sbListeners) {

            sbListeners.remove(l);

        }
    }

    protected void fireStatusBarClosed(MainStatusBar c) {
        final List<StatusBarListener> ls = new ArrayList<>(sbListeners);
        for (StatusBarListener l : ls) {
            l.statusBarClosed(c);
        }
    }

    protected void fireStatusBarOpened(MainStatusBar c) {
        final List<StatusBarListener> ls = new ArrayList<>(sbListeners);
        for (StatusBarListener l : ls) {
            l.statusBarOpened(c);
        }
    }

    public void saveChanges(String rqId, OutputStream out) throws IOException {
        activeTimerCount(processTimerEvent(true));
        StringBuilder builder = new StringBuilder();
        builder.append("<updates id=\"").append(rqId).append("\" root=\"").append(html.getId()).append("\">\n");
        if (timerStateChaged) {
            if (activeTimerCount > 0) {
                builder.append("<object-timer command=\"start\"/>");
            } else {
                builder.append("<object-timer command=\"stop\"/>");
            }
        }
        final List<String> addUrls = new LinkedList<>();
        collectScripts(addUrls);
        if (!addUrls.isEmpty()) {
            builder.append("    <scripts>\n");
            for (String s : addUrls) {
                builder.append("        <script>").append(s).append("?root=").append(getQueryId()).append("</script>\n");

            }
            builder.append("    </scripts>\n");
        }
        addUrls.clear();
        if (cookies != null) {
            builder.append("    <cookies>\n");
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                builder.append("        <cookie name=\"").append(cookie.getKey()).append("\" value=\"").append(cookie.getValue()).append("\"/>\n");
            }
            builder.append("    </cookies>\n");
            cookies = null;
        }
        collectCss(addUrls);
        if (!addUrls.isEmpty()) {
            builder.append("    <styles>\n");
            for (String s : addUrls) {
                builder.append("        <style>").append(s).append("?root=").append(getQueryId()).append("</style>\n");

            }
            builder.append("    </styles>\n");
        }

        this.html.saveChanges(this, builder);

        if (!downloads.isEmpty()) {
            builder.append("    <transmissions>\n");
            for (TrInfo info : downloads) {
                if (info.src != null) {
                    builder.append("        <transmission src=\"").append(info.src).append("\" url=\"").append(info.getURL()).append("\"/>\n");
                } else {
                    builder.append("        <transmission name=\"").append(info.name).append("\" url=\"").append(info.getURL()).append("\" save=\"").append(info.open ? "false" : "true").append("\"/>\n");
                }
            }
            downloads.clear();
            builder.append("    </transmissions>\n");
        }

        if (lastFocused != null) {
            UIObject obj = lastFocused.get();
            if (obj != null && obj.getParent() != null) {
                builder.append("<focus id =\"").append(obj.getHtmlId()).append("\"/>");
            }

        }
        builder.append("\n</updates>");

        if (DEBUG) {
            System.out.println(builder);
        }
        try {
            FileUtils.writeString(out, builder.toString(), FileUtils.XML_ENCODING);
        } catch (IOException ex) {
            throw ex;
        }
    }
    private UIObject explicitChild;

    public void blockRedraw(UIObject explicit) {
        this.explicitChild = explicit;
    }

    @Override
    public UIObject getExplicitChild() {
        return explicitChild;
    }

    public void unblockRedraw() {
        this.explicitChild = null;
    }

    private boolean scriptWasPosted(String url) {
        return postedScripts != null && postedScripts.contains(url);
    }

    private void scriptPosted(String url) {
        if (postedScripts == null) {
            postedScripts = new HashSet<>();
        }
        postedScripts.add(url);
    }

    private boolean cssWasPosted(String url) {
        return postedCss != null && postedCss.contains(url);
    }

    private void cssPosted(String url) {
        if (postedCss == null) {
            postedCss = new HashSet<>();
        }
        postedCss.add(url);
    }

    protected abstract IDialogDisplayer getDialogDisplayer();

    private void collectScripts(final List<String> urls) {
        visit(new Visitor() {
            @Override
            public void accept(UIObject obj) {
                String[] scripts = obj.clientScriptsRequired();
                if (scripts != null) {
                    for (String s : scripts) {
                        if (!scriptWasPosted(s)) {
                            urls.add(s);
                            scriptPosted(s);
                        }
                    }
                }
            }
        });
    }

    private class TrInfo {

        final String name;
        final String url;
        final String src;
        final boolean open;

        public TrInfo(String name, String url, boolean open) {
            this.name = name;
            this.url = url;
            this.open = open;
            this.src = null;
        }

        public TrInfo(String src, String url) {
            this.name = null;
            this.url = url;
            this.src = src;
            this.open = false;
        }

        public String getURL() {
            return url + "?root=" + RootPanel.this.getQueryId();
        }
    }
    private final List<TrInfo> downloads = new LinkedList<>();

    public void requestDownload(String name, String url, boolean open) {
        downloads.add(new TrInfo(name, url, open));
    }

    public String requestUpload(String id, String uuid) {
        TrInfo info = new TrInfo(id, uuid);
        downloads.add(info);
        return info.getURL();
    }

    public String getUploadURL(String id, String uuid) {
        TrInfo info = new TrInfo(id, uuid);
        return info.getURL();
    }

    private void collectCss(final List<String> urls) {
        visit(new Visitor() {
            @Override
            public void accept(UIObject obj) {
                String[] scripts = obj.clientCssRequired();
                if (scripts != null) {
                    for (String s : scripts) {
                        if (!cssWasPosted(s)) {
                            urls.add(s);
                            cssPosted(s);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/client.js"};
    }

    @Override
    protected String[] clientCssRequired() {
        return new String[]{"org/radixware/wps/rwt/defaults.css"};
    }

    public abstract IMainView getExplorerView();

    public abstract void closeExplorerView();

    @Override
    public UIObject findObjectByHtmlId(String id) {
        if (oldVersionView != null && oldVersionView.getId().equals(id)) {
            return this;
        } else {
            return super.findObjectByHtmlId(id);
        }
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("update-version".equals(actionName)) {
            ((WpsEnvironment) getEnvironment()).updateToCurrentVersion();
        } else {
            super.processAction(actionName, actionParam);
        }
    }

    @Override
    public RootPanel findRoot() {
        return this;
    }
    private Html oldVersionView = null;

    public void notifyOldVersion(boolean notify) {
        if (notify) {
            if (oldVersionView == null) {
                oldVersionView = new Html("label");
                oldVersionView.setInnerText(getEnvironment().getMessageProvider().translate("StatusBar", "Old Version"));
                oldVersionView.addClass("rwt-old-version-notify");
                oldVersionView.addClass("rwt-ui-shadow");
                oldVersionView.layout("$RWT.env.oldVersion.layout");
                getHtml().add(oldVersionView);
            }
        } else {
            if (oldVersionView != null) {
                getHtml().remove(oldVersionView);
                oldVersionView = null;
            }
        }
    }

//    public void enterWaitMode() {
//        if (waitPanel == null) {
//            waitPanel = new WaitPanel();
//            add(waitPanel);
//            getDialogDisplayer().updateState(waitPanel);
//        }
//        waitPanel.inc();
//    }
//
//    public void leaveWaitMode() {
//        if (waitPanel != null) {
//            waitPanel.dec();
//            if (waitPanel.counter() == 0) {
//                remove(waitPanel);
//                waitPanel = null;
//                getDialogDisplayer().updateState(null);
//            }
//        }
//    }
    public void setTitle(String title) {
        html.setAttr("title", title);
    }

    public String getTitle() {
        return html.getAttr("title");
    }

    public void setIcon(Icon icon) {
        if (icon instanceof WpsIcon) {
            Dimension dim = null;
            WpsIcon tabIcon = TemporaryIcon.createTempIcon((WpsEnvironment) getEnvironment(), dim, TemporaryIcon.Format.PNG, (WpsIcon) icon);
            if (tabIcon != null) {
                html.setAttr("shortcutIcon", (tabIcon).getURI(this));
            }
        } else {
            html.setAttr("shortcutIcon", null);
        }
    }

    final void registerFocused(UIObject obj) {
        this.lastFocused = new WeakReference<>(obj);
    }

    final void resetFocused() {
        this.lastFocused = null;
    }

    public final void writeToCookies(final String name, final String value) {
        if (cookies == null) {
            cookies = new HashMap<>();
        }
        cookies.put(name, value);
    }

    @Override
    public void notifySent() {
        super.notifySent();
        final List<String> currentUploads = new ArrayList<>();
        synchronized (uploadHandlersSemaphore) {
            if (uploadHandlersByContextObjectId != null) {
                currentUploads.addAll(uploadHandlersByContextObjectId.keySet());
            }
        }
        visit(new Visitor() {
            @Override
            public void accept(final UIObject obj) {
                currentUploads.remove(obj.getHtmlId());
            }
        });
        synchronized (uploadHandlersSemaphore) {
            if (uploadHandlersByContextObjectId != null) {
                for (String contextObjectId : currentUploads) {
                    final UploadHandler handler = uploadHandlersByContextObjectId.get(contextObjectId);
                    if (handler != null) {
                        handler.release();
                    }
                }
            }
        }
    }

    public final void onUploadHandlerReleased(final String contextObjectId) {
        synchronized (uploadHandlersSemaphore) {
            final UploadHandler handler;
            if (uploadHandlersByContextObjectId != null) {
                handler = uploadHandlersByContextObjectId.remove(contextObjectId);
                if (uploadHandlersByContextObjectId.isEmpty()) {
                    uploadHandlersByContextObjectId = null;
                }
            } else {
                handler = null;
            }
            if (uploadHandlersById != null && handler != null) {
                uploadHandlersById.remove(handler.getId());
                if (uploadHandlersById.isEmpty()) {
                    uploadHandlersById = null;
                }
            }
        }
    }

    public final void registerUploadHandler(final UploadHandler handler, final String contextObjectId) {
        synchronized (uploadHandlersSemaphore) {
            if (uploadHandlersByContextObjectId == null) {
                uploadHandlersByContextObjectId = new HashMap<>();
            }
            if (uploadHandlersById == null) {
                uploadHandlersById = new HashMap<>();
            }
            uploadHandlersByContextObjectId.put(contextObjectId, handler);
            uploadHandlersById.put(handler.getId(), handler);
        }
    }

    public final UploadHandler findUploadHandlerById(final String uploadHandlerId) {
        synchronized (uploadHandlersSemaphore) {
            if (uploadHandlersById == null) {
                return null;
            } else {
                return uploadHandlersById.get(uploadHandlerId);
            }
        }
    }
}
