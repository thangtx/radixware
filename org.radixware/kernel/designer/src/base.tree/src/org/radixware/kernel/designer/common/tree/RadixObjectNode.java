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
package org.radixware.kernel.designer.common.tree;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.Action;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.actions.*;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStatusEvent;
import org.openide.filesystems.FileStatusListener;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.util.Mutex;
import org.openide.util.Mutex.ExceptionAction;
import org.openide.util.MutexException;
import org.openide.util.NbPreferences;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ClipboardEvent;
import org.openide.util.datatransfer.ClipboardListener;
import org.openide.util.datatransfer.ExClipboard;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObject.EditStateChangeListener;
import org.radixware.kernel.common.defs.RadixObject.EditStateChangedEvent;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.rights.*;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.check.CheckAction;
import org.radixware.kernel.designer.common.dialogs.check.CheckAction.CheckCookie;
import org.radixware.kernel.designer.common.dialogs.dependency.ModuleDependenciesAction;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesAction;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesAction.FindUsagesCookie;
import org.radixware.kernel.designer.common.dialogs.utils.*;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.displaying.*;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.nodes.hide.Hidable;
import org.radixware.kernel.designer.common.general.nodes.hide.HideAction;
import org.radixware.kernel.designer.common.general.nodes.hide.HideSettings;
import org.radixware.kernel.designer.common.general.nodes.hide.Restorable;
import org.radixware.kernel.designer.common.general.refactoring.RefactoringPopupAction;
import org.radixware.kernel.designer.common.general.refactoring.RefactoringProvider;
import org.radixware.kernel.designer.common.general.utils.PerformanceLogger;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction.RenameCookie;
import org.radixware.kernel.designer.common.tree.actions.NewObjectAction;
import org.radixware.kernel.designer.common.tree.actions.NewObjectCookie;
import org.radixware.kernel.designer.common.tree.actions.SpellcheckAction;

/**
 * Base class for all Radix object nodes in branches tree.
 *
 */
public class RadixObjectNode extends AbstractNode {

    private static class RadixObjectSaveCookie implements SaveCookie {

        private final RadixObject radixObject;

        public RadixObjectSaveCookie(RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        @Override
        public void save() throws IOException {
            try {
                RadixMutex.readAccess(new ExceptionAction<Void>() {
                    @Override
                    public Void run() throws IOException {
                        if (radixObject.isInBranch()) {
                            radixObject.save();
                        }
                        return null;
                    }
                });
            } catch (MutexException proxy) {
                final Exception cause = proxy.getException();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                } else {
                    throw new IllegalStateException(cause);
                }
            }
        }
    }

    private class HtmlDisplayNameUpdater {

        protected final void update() {
            final String newHtmlDisplayName = calcHtmlDisplayName();
            if (!Utils.equals(RadixObjectNode.this.htmlDisplayName, newHtmlDisplayName)) {
                RadixObjectNode.this.htmlDisplayName = newHtmlDisplayName;
                fireDisplayNameChange(null, null); // file html display name changed
            }
        }
    }
    //private static final ProjectMenuItem projectMenuItem = new ProjectMenuItem();
    private final RadixObject radixObject;
    private final InstanceContent lookupContent;
    private final CreationSupport creationSupport;
    private final RadixObjectEditCookie editCookie;
    private final SaveCookie saveCookie;
    private final RenameCookie renameCookie;
    private final RadixObjectNodeDeleteAction.Cookie deleteCookie;
    private final SpellcheckAction.SpellcheckCookie spellCheckCookie;
    private final FileStatusListener weakFileStatusListener;
    private static final PerformanceLogger ANNOTATION_PL = new PerformanceLogger("RadixObjectNode.fileStatusListener");
    private final FileStatusListener fileStatusListener = new FileStatusListener() {
        @Override
        public void annotationChanged(FileStatusEvent ev) {
            ANNOTATION_PL.enter();
            final boolean icon = ev.isIconChange();
            final boolean name = ev.isNameChange();
            if (icon || name) {
                // do not look file object in lookup, file can be create after creation of the node
                final Collection<? extends FileObject> fileObjects = RadixFileUtil.getVersioningFileObjects(radixObject);
                if (fileObjects.isEmpty()) {
                    if (!radixObject.isInBranch()) {
                        RadixFileUtil.getFileSystem().removeFileStatusListener(weakFileStatusListener);
                    }
                } else {
                    for (FileObject fileObject : fileObjects) {
                        if (ev.hasChanged(fileObject)) {
                            if (icon) {
                                updateIcon();
                            }
                            if (name) {
                                updateHtmlDisplayName();
                            }
                            updateFileObjects();
                            break;
                        }
                    }
                }
            }
            ANNOTATION_PL.leave();
        }
    };
    private String htmlDisplayName = null;
    private HtmlNameSupport htmlNameSupport;
    private final IRadixEventListener<RadixEvent> htmlNameChangeListener = new IRadixEventListener<RadixEvent>() {
        @Override
        public void onEvent(RadixEvent arg0) {
            updateHtmlDisplayName();
        }
    };
    private IconSupport iconSupport = null;
    private final IIconChangeListener iconChangeListener = new IIconChangeListener() {
        @Override
        public void onEvent(IconChangedEvent arg0) {
            updateIcon();
        }
    };
    private Image icon = null;
    //
    private final EditStateChangeListener editStateListener = new EditStateChangeListener() {
        @Override
        public void onEvent(EditStateChangedEvent e) {
            updateLookupContent();
        }
    };

    protected final void addCookie(Cookie cookie) {
        this.lookupContent.add(cookie);
    }

    protected void removeCookie(Cookie cookie) {
        this.lookupContent.remove(cookie);
    }

    protected InstanceContent getLookupContent() {
        return lookupContent;
    }
    private static final String HIDDEN_NODES = "HiddenNodes";
    private Hidable hidable = new Hidable() {
        @Override
        public void hide() {
            lookupContent.add(restorable);
            lookupContent.remove(this);
            storeHidden(true);
        }
    };
    private Restorable restorable = new Restorable() {
        @Override
        public void restore() {
            lookupContent.remove(this);
            lookupContent.add(hidable);
            storeHidden(false);
        }
    };

    private String getNodeKey() {
        StringBuilder sb = new StringBuilder(radixObject.getName());
        RadixObject ownerObject = radixObject.getContainer();
        while (ownerObject != null) {
            if (ownerObject.getName().length() > 0) {
                sb.insert(0, "/");
                sb.insert(0, ownerObject.getName());
            }
            ownerObject = ownerObject.getContainer();
        }
        return sb.toString();
    }

    private void storeHidden(boolean hidden) {
        if (hidden) {
            NbPreferences.root().node(HIDDEN_NODES).putBoolean(getNodeKey(), true);
        } else {
            NbPreferences.root().node(HIDDEN_NODES).remove(getNodeKey());
        }
    }

    private boolean readHidden() {
        return NbPreferences.root().node(HIDDEN_NODES).getBoolean(getNodeKey(), false);
    }

    public void expandLookup(Class clazz, Object object) {
        if (getLookup().lookup(clazz) != object) {
            getLookupContent().add(object);
        }
    }
    private final HtmlDisplayNameUpdater htmlDisplayNameUpdater = new HtmlDisplayNameUpdater();

    private RadixObjectNode(RadixObject radixObject, Children children, InstanceContent lookupContent) {
        super(children, new AbstractLookup(lookupContent));

        //NodesManager.assertNodeRegistered(radixObject); // check that node created thru NodesManager.findOrCreateNode
        this.radixObject = radixObject;
        this.lookupContent = lookupContent;
        this.lookupContent.add(radixObject);
        this.lookupContent.add(htmlDisplayNameUpdater);

        if (HideSettings.isHidable(this)) {
            this.lookupContent.add(hidable);
        }

        this.editCookie = createEditCookie();
        if (editCookie != null) {
            addCookie(editCookie);
        }

        this.creationSupport = createNewCreationSupport();
        if (creationSupport != null) {
            addCookie(new NewObjectCookie(radixObject, creationSupport));
        }

        this.renameCookie = createRenameCookie();
        this.deleteCookie = new RadixObjectNodeDeleteAction.Cookie(radixObject);
        this.saveCookie = new RadixObjectSaveCookie(radixObject);
        updateLookupContent();
        radixObject.addEditStateListener(editStateListener);

        if (canCheck()) {
            final CheckAction.CheckCookie checkCookie = new CheckCookie(radixObject);
            addCookie(checkCookie);
        }

        if (radixObject instanceof Module) {
            addCookie(new ModuleDependenciesAction.ModuleDependenciesCookie());
        }

        if (radixObject instanceof Definition) {
            final Definition definition = (Definition) radixObject;
            addCookie(new FindUsagesCookie(definition));
        }

        final NavigatorLookupHint navigatorLookupHint = getNavigatorLookupHint();
        if (navigatorLookupHint != null) {
            getLookupContent().add(navigatorLookupHint);
        }

        if (isSpellcheckEnabled()) {
            spellCheckCookie = new SpellcheckAction.SpellcheckCookie(radixObject);
            addCookie(spellCheckCookie);
        } else {
            spellCheckCookie = null;
        }
        if (isSvnStatusDisplayed(radixObject)) {
            weakFileStatusListener = FileUtil.weakFileStatusListener(fileStatusListener, null);
            RadixFileUtil.getFileSystem().addFileStatusListener(weakFileStatusListener);
        } else {
            weakFileStatusListener = null;
        }

        if (children instanceof Index) {
            getLookupContent().add(children);
        }

        if (radixObject instanceof IDirectoryRadixObject || radixObject.isSaveable()) {
            final RadixSearchInfo searchInfo = new RadixSearchInfo(radixObject);
            getLookupContent().add(searchInfo);
        }

//        if (isAPIEnabled()) {
//            Cookie c = getAPIObserverCookie(radixObject);
//            if (c != null) {
//                addCookie(c);
//            }
//        }
        if (readHidden()) {
            hidable.hide();
        }

        if (isAPIEnabled()) {
            final Cookie apiBrowserCookie = getApiBrowserCookie(radixObject);
            if (apiBrowserCookie != null) {
                addCookie(apiBrowserCookie);
            }
        }
    }

    private boolean isSpellcheckEnabled() {
        return radixObject instanceof Branch
                || radixObject instanceof Layer
                || radixObject instanceof AdsSegment
                || radixObject instanceof AdsModule
                || radixObject.getModule() instanceof AdsModule;

    }

    protected boolean isAPIEnabled() {
        if (radixObject instanceof IAccessible) {
            final IAccessible accessible = (IAccessible) radixObject;
            if (!accessible.getAccessFlags().isPublic() && !accessible.getAccessFlags().isProtected()) {
                return false;
            }
        }

        if (radixObject instanceof Definition) {
            if (!((Definition) radixObject).isPublished()) {
                return false;
            }
        }
        return radixObject instanceof AdsModule || (radixObject instanceof AdsDefinition && ((AdsDefinition) radixObject).isTopLevelDefinition());
    }

    protected NavigatorLookupHint getNavigatorLookupHint() {
        if (radixObject instanceof Definition) {
            return RadixNavigatorLookupHint.getInstance();
        } else {
            return null;
        }
    }

    protected RenameCookie createRenameCookie() {
        if (radixObject instanceof Definition) {
            return new RenameCookie(radixObject);
        } else {
            return null;
        }
    }

    protected RadixObjectNode(RadixObject radixObject, Children children) {
        this(radixObject, children, new InstanceContent());//NOPMD
    }

    public RadixObject getRadixObject() {
        return radixObject;
    }

    protected RadixObjectEditCookie createEditCookie() {
        return new RadixObjectEditCookie(radixObject);
    }

    protected CreationSupport createNewCreationSupport() {
        return null;
    }

    protected IconSupport createIconSupport() {
        return IconSupportsManager.newInstance(radixObject);
    }

    @Override
    public boolean canRename() {
        if (radixObject.isReadOnly()) {
            return false;
        }
        switch (radixObject.getNamingPolicy()) {
            case FREE:
            case UNIQUE_IDENTIFIER:
            case IDENTIFIER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setName(final String s) {
        final IAdvancedAcceptor<String> nameAcceptor = NameAcceptorFactory.newAcceptorForRename(radixObject);
        final IAcceptResult acceptResult = nameAcceptor.getResult(s);
        if (acceptResult.isAccepted()) {
            radixObject.setName(s);
        } else {
            DialogUtils.messageError(acceptResult.getErrorMessage());
        }
    }

    private String calcHtmlDisplayName() {
        if (htmlNameSupport == null) {
            htmlNameSupport = HtmlNameSupportsManager.newInstance(radixObject);
            htmlNameSupport.addEventListener(htmlNameChangeListener);
        }
        String result = htmlNameSupport.getTreeHtmlName();
        if (grayed) {
            if (result.startsWith("<font color=\"")) {
                int index = result.indexOf(">");
                if (index > 0) {
                    result = "<font color=\"#AAAAAA\"" + result.substring(index);
                } else {
                    result = "<font color=\"#AAAAAA\">" + result + "</font>";
                }
            } else {
                result = "<font color=\"#AAAAAA\">" + result + "</font>";
            }
        }
        if (bold) {
            result = "<b>" + result + "</b>";
        }
        return result;
    }

    // Final - register HtmlNameSupport.
    protected final void updateHtmlDisplayName() {
        htmlDisplayNameUpdater.update();
    }

    // Final - register HtmlNameSupport.
    @Override
    public final String getHtmlDisplayName() {
        if (this.htmlDisplayName == null) {
            this.htmlDisplayName = calcHtmlDisplayName();
            updateFileObjects(); // calc file object after first displaying, avoid deadlock RADIX-3619
        }
        return htmlDisplayName;
    }

    private Image calcIcon() {
        if (iconSupport == null) {
            iconSupport = createIconSupport();
            iconSupport.addEventListener(iconChangeListener);
        }

        final RadixIcon radixIcon = iconSupport.getIcon();
        Image image = radixIcon.getImage();

        if ((radixObject instanceof IDirectoryRadixObject) && !(radixObject instanceof Branch)) { // netbeans calculated versioning icon of project by itself
            final File dir = ((IDirectoryRadixObject) radixObject).getDirectory();
            if (dir != null) {
                final FileObject fileObject = RadixFileUtil.toFileObject(dir);
                if (fileObject != null) {
                    final int type = 0; // unknown
                    image = RadixFileUtil.getFileSystem().getStatus().annotateIcon(image, type, Collections.singleton(fileObject));
                }
            }
        }

        return annotateIcon(image);
    }

    protected Image annotateIcon(Image icon) {
        if (radixObject instanceof Definition && ((Definition) radixObject).isPublished() && !(radixObject instanceof Module)) {
            return RadixObjectIcon.annotatePublished(icon);
        }
        return icon;
    }

    protected void updateIcon() {
        final Image newIcon = calcIcon();
        if (!Utils.equals(this.icon, newIcon)) {
            this.icon = newIcon;
            fireIconChange();
        }
    }

    @Override
    public Image getIcon(int type) {
        if (this.icon == null) {
            this.icon = calcIcon();
        }
        return this.icon;
    }

    @Override
    public final Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public boolean canCopy() {
        return ClipboardUtils.canCopy(radixObject);
    }

    @Override
    public boolean canCut() {
        return ClipboardUtils.canCut(radixObject);
    }

    public boolean canCheck() {
        return true;
    }

    protected RadixObject getClipboardPresentation() {
        return radixObject;
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        return RadixMutex.readAccess(new Mutex.Action<Transferable>() {
            @Override
            public Transferable run() {
                if (canCopy()) {
                    final RadixObject clipboardPresentation = getClipboardPresentation();
                    if (clipboardPresentation != null) {
                        return clipboardPresentation.getClipboardSupport().createTransferable(ETransferType.DUPLICATE);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        return RadixMutex.readAccess(new Mutex.Action<Transferable>() {
            @Override
            public Transferable run() {
                if (canCut()) {
                    final RadixObject clipboardPresentation = getClipboardPresentation();
                    if (clipboardPresentation != null) {
                        final Transferable result = clipboardPresentation.getClipboardSupport().createTransferable(ETransferType.NONE);
                        initializeCutGraying();
                        return result;
                    }
                }
                return null;
            }
        });
    }

    @Override
    protected void createPasteTypes(final Transferable transferable, final List<PasteType> pasteTypes) {

        final ClipboardSupport.CanPasteResult result = ClipboardUtils.canPaste(transferable, radixObject, new ClipboardUtils.AskDuplicationResolver());
        if (result == ClipboardSupport.CanPasteResult.YES/*
                 * || result == ClipboardSupport.CanPasteResult.NO_DUPLICATE
                 */) {
            pasteTypes.add(new PasteType() {
                @Override
                public Transferable paste() throws IOException {

                    return ClipboardUtils.paste(transferable, radixObject, new ClipboardUtils.AskDuplicationResolver());

                }
            });
        }
    }

    /**
     * Override this function to add additional actions for the node.
     */
    public void addCustomActions(List<Action> actions) {
    }

    protected void addPrimaryCustomActions(List<Action> actions) {
    }

    /**
     * Fill node actions. Override addCustomActions function to add additional
     * actions for the node.
     *
     * @param forExternalPurposes - true if for editor, false otherwise.
     */
    public final void fillActions(List<Action> actions, boolean forEditorTitle) {
        updateLookupContent();

        addPrimaryCustomActions(actions);
        // group 1
        if (!forEditorTitle && this.creationSupport != null && creationSupport.isEnabled(radixObject)) {
            actions.add(SystemAction.get(NewObjectAction.class));
            actions.add(null);
        }

        // group 2
        int size = actions.size();
        if (!forEditorTitle && this.editCookie != null) {
            actions.add(SystemAction.get(EditAction.class));
        }

        if (renameCookie != null) {
            actions.add(SystemAction.get(DefinitionRenameAction.class));
        }

        if (getLookup().lookup(RefactoringProvider.class) != null) {
            actions.add(SystemAction.get(RefactoringPopupAction.class));
        }

        if (actions.size() != size) {
            actions.add(null);
        }

        // group 3
        size = actions.size();
        if (canCheck()) {
            actions.add(SystemAction.get(CheckAction.class));
        }

        if (radixObject.isSaveable() && radixObject.getDefinition() != null) {
            actions.add(SystemAction.get(SaveAction.class));
        }

        if (isSpellcheckEnabled()) {
            actions.add(SystemAction.get(SpellcheckAction.class));
        }
        addCustomActions(actions);

        if (actions.size() != size) {
            actions.add(null);
        }

        // group 4
        if (!forEditorTitle) {
            final ClipboardSupport clipboardSupport = radixObject.getClipboardSupport();
            if (clipboardSupport != null) {
                if (clipboardSupport.canCopy()) { // can copy defines global posibility to copy
                    actions.add(SystemAction.get(CutAction.class));
                    actions.add(SystemAction.get(CopyAction.class));
                }
                actions.add(SystemAction.get(PasteAction.class));
                actions.add(null);
            }
        }

        if (radixObject instanceof Module) {
            actions.add(SystemAction.get(ModuleDependenciesAction.class));
        }
        // group 5
        if (radixObject instanceof Definition) {
            actions.add(SystemAction.get(FindUsagesAction.class));
            actions.add(null);
        }

        // group 6
        final boolean isUserReportInUserDesigner = (radixObject instanceof AdsUserReportClassDef && !(radixObject.getModule() instanceof UdsModule));
        if (((radixObject instanceof Definition) && !(isUserReportInUserDesigner) && !(radixObject instanceof AdsRoleDef && ((AdsRoleDef) radixObject).isAppRole())) || (radixObject instanceof Layer)) {
            // it is impossible to use std. DeleteAction, its asynchronous, so bug with enabled state of popup menu item :-(
            actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));
            actions.add(null);
        }

        // group 7
        if (isSvnStatusDisplayed(radixObject)) {
            Class c = findVcsAction();
            if (c != null) {
                actions.add(SystemAction.get(c));
            }
        }

        if (HideSettings.isHidable(this)) {
            actions.add(SystemAction.get(HideAction.class));
        }

//        if (isAPIEnabled()) {
//
//            Class c = getAPIObserverAction();
//            if (c != null) {
//                actions.add(SystemAction.get(c));
//            }
//        }
        size = actions.size();
        if (size > 0 && actions.get(size - 1) == null) {
            actions.remove(size - 1);
        }

        if (isAPIEnabled()) {
            final Class apiBrowserActionClass = getApiBrowserAction();
            if (apiBrowserActionClass != null) {
                actions.add(SystemAction.get(apiBrowserActionClass));
            }
        }
    }

    private Class findVcsAction() {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.designer.common.tree.actions.VcsAction");
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

//    private Cookie getAPIObserverCookie(RadixObject obj) {
//        try {
//            Class c = Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.designer.common.dialogs.api.ObserveAPIAction$Cookie");
//            Constructor constr = c.getConstructor(RadixObject.class);
//            return (Cookie) constr.newInstance(obj);
//        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
//            //ignore
//            return null;
//        }
//    }
    private Cookie getApiBrowserCookie(RadixObject obj) {
        try {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                return null;
            }
            Class c = cl.loadClass("org.radixware.kernel.designer.api.editors.ApiEditAction$Cookie");
            Constructor constr = c.getConstructor(RadixObject.class);
            return (Cookie) constr.newInstance(obj);
        } catch (Throwable e) {
            //ignore
            return null;
        }
    }

    private Class getApiBrowserAction() {
        try {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                return null;
            }
            return cl.loadClass("org.radixware.kernel.designer.api.editors.ApiEditAction");
        } catch (Throwable e) {
            //ignore
            return null;
        }
    }

//    private Class getAPIObserverAction() {
//        try {
//            return Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.designer.common.dialogs.api.ObserveAPIAction");
//
//        } catch (ClassNotFoundException e) {
//            //ignore
//            return null;
//        }
//    }
    @Override
    public boolean canDestroy() {
        return radixObject.canDelete();
    }

    @Override
    public void destroy() throws IOException {
        RadixMutex.writeAccess(new Runnable() {
            @Override
            public void run() {
                if (radixObject.isInBranch() && radixObject.canDelete()) {
                    radixObject.delete();
                }
            }
        });
    }

    @Override
    public final Action[] getActions(boolean forEditorTitle) {
        List<Action> actions = new ArrayList<>();
        fillActions(actions, forEditorTitle);
        Action[] arrayOfActions = new Action[actions.size()];
        return actions.toArray(arrayOfActions);
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    private void updateLookupContent() {
        if (radixObject.getEditState() != EEditState.NONE) {
            addCookie(saveCookie);
        } else {
            removeCookie(saveCookie);
        }

        // TODO: switch to listener for ReadOnly changed (in the future, when RenameDefinition action will in global menu).
        // and remove call of this function in getActions();
        if (renameCookie != null) {
            if (radixObject.isReadOnly()) {
                removeCookie(renameCookie);
            } else {
                addCookie(renameCookie);
            }
        }

        if (canDestroy()) {
            addCookie(deleteCookie);
        } else {
            removeCookie(deleteCookie);
        }
    }

    @Override
    public String getName() {
        // displayed in toString();
        return radixObject.getName();
    }

    @Override
    public String getDisplayName() {
        // displayed in status bar after save, in delete question.
        //return radixObject.getTypeTitle() + " '" + radixObject.getQualifiedName() + "'";

        //to fix standart search in explorer tree (see #RADIX-2870)
        return getName();
    }

    @Override
    public String getShortDescription() {
        final String tooltip = radixObject.getToolTip();
        if (tooltip != null && !tooltip.isEmpty()) {
            return tooltip;
        } else {
            return null;
        }
    }

    private static boolean isSvnStatusDisplayed(RadixObject radixObject) {
        return (radixObject instanceof IDirectoryRadixObject) || radixObject.isSaveable();
    }

    private void updateFileObjects() {
        final Collection<? extends FileObject> oldFileObjects = this.getLookup().lookupAll(FileObject.class);
        final List<FileObject> newFileObjects = RadixFileUtil.getVersioningFileObjects(radixObject);

        // add new (when node initilized or radix object saved first time)
        if (newFileObjects != null) {
            for (FileObject fileObject : newFileObjects) {
                if (oldFileObjects == null || !oldFileObjects.contains(fileObject)) {
                    this.lookupContent.add(fileObject);
                }
            }
        }

        // remove old
        if (oldFileObjects != null) {
            for (FileObject fileObject : oldFileObjects) {
                if (newFileObjects == null || !newFileObjects.contains(fileObject)) {
                    this.lookupContent.remove(fileObject);
                }
            }
        }
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory {

        @Override
        public RadixObjectNode newInstance(RadixObject radixObject) {
            return new RadixObjectNode(radixObject, Children.LEAF);
        }
    }
//    private static class SubBeanNode extends BeanNode<RadixObject> {
//
//        public SubBeanNode(RadixObject bean) throws IntrospectionException {
//            super(bean);
//        }
//
//        // switch getSheet to public access
//        public Sheet getSubSheet() {
//            return super.getSheet();
//        }
//    }
//
//    @Override
//    protected Sheet createSheet() {
//        try {
//            final SubBeanNode beanNode = new SubBeanNode(radixObject);
//            return beanNode.getSubSheet();
//        } catch (IntrospectionException cause) {
//            return super.createSheet();
//        }
//    }
    private volatile boolean grayed = false;
    private volatile boolean bold = false;
    private static boolean cutGrayingInitialized = false;

    public void setGrayed(boolean grayed) {
        if (this.grayed != grayed) {
            this.grayed = grayed;
            updateHtmlDisplayName();
        }
    }

    public void setBold(boolean bold) {
        if (this.bold != bold) {
            this.bold = bold;
            updateHtmlDisplayName();
        }
    }
    //
    private static final Set<RadixObject> cutObjects = new HashSet<>();
    private static ClipboardListener clipboardListener = null;

    private static void initializeCutGraying() {
        synchronized (cutObjects) {
            if (clipboardListener != null) {
                return;
            }

            clipboardListener = new ClipboardListener() {
                @Override
                public void clipboardChanged(ClipboardEvent ev) {
                    final Set<RadixObject> newCutObjects = ClipboardUtils.getCutObjects();
                    synchronized (cutObjects) {
                        if (!Utils.equals(cutObjects, newCutObjects)) {
                            setGrayed(cutObjects, false);
                            setGrayed(newCutObjects, true);
                            cutObjects.clear();
                            cutObjects.addAll(newCutObjects);
                        }
                        if (cutObjects.isEmpty()) {
                            ev.getClipboard().removeClipboardListener(this);
                            clipboardListener = null;
                        }
                    }
                }

                private void setGrayed(Set<RadixObject> radixObjects, boolean grayed) {
                    if (radixObjects != null) {
                        for (RadixObject radixObject : radixObjects) {
                            final Node node = NodesManager.findNode(radixObject);
                            if (node instanceof RadixObjectNode) {
                                ((RadixObjectNode) node).setGrayed(grayed);
                            }
                        }
                    }
                }
            };

            final Clipboard c = ClipboardUtils.getClipboard();
            if (c instanceof ExClipboard) {
                final ExClipboard clipboard = (ExClipboard) c;
                clipboard.addClipboardListener(clipboardListener);
            }
        }
    }
}
