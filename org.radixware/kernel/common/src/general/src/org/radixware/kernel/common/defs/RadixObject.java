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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryBranch;
import org.radixware.kernel.common.resources.icons.IIconified;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

/**
 * Base class for all Radix objects (definitions, definition collections,
 * branch, layer, segment, e.t.c).
 *
 */
public abstract class RadixObject implements IIconified {

    public static final String RADIX_OBJECT_TYPE_TITLE = "Object";
    public static final String RADIX_OBJECTS_TYPES_TITLE = "Objects";
    private final static Color DARK_GREEN = new Color(0, 128, 0);
    static final AtomicLong globalChangeVersion = new AtomicLong(0);

    public enum EEditState {

        NEW {
                    @Override
                    public Color getColor() {
                        return DARK_GREEN;
                    }
                },
        MODIFIED {
                    @Override
                    public Color getColor() {
                        return Color.BLUE;
                    }
                },
        NONE {
                    @Override
                    public Color getColor() {
                        return Color.BLACK;
                    }
                };

        public abstract Color getColor();
    }
    private String name = "";
    private volatile RenameSupport renameSupportHolder = null;
    private volatile RemoveSupport removeSupportHolder = null;
    private volatile EditStateSupport editStateSupportHolder = null;
    private final Object supportSem = new Object();
    private EEditState editState = EEditState.NEW;
    private RadixObject container = null;
    private NameProvider nameProvider = null;

    public interface NameProvider {

        public String getName();
    }

    protected RadixObject() {
        this("");
    }

    protected RadixObject(String name) {
        this.name = (name != null ? name : "");
    }

    private RenameSupport getRenameSupport() {
        if (renameSupportHolder != null) {
            return renameSupportHolder;
        }
        synchronized (supportSem) {
            if (renameSupportHolder != null) {
                return renameSupportHolder;
            }
            renameSupportHolder = new RenameSupport();
            return renameSupportHolder;
        }
    }

    /**
     * Get support to listen removing of the object. Event occured when object
     * or its owners removed from owner collection.
     */
    public RemoveSupport getRemoveSupport() {
        if (removeSupportHolder != null) {
            return removeSupportHolder;
        }
        synchronized (supportSem) {
            if (removeSupportHolder != null) {
                return removeSupportHolder;
            }
            removeSupportHolder = new RemoveSupport();
            return removeSupportHolder;
        }
    }

    private EditStateSupport getEditStateSupport() {
        if (editStateSupportHolder != null) {
            return editStateSupportHolder;
        }
        synchronized (supportSem) {
            if (editStateSupportHolder != null) {
                return editStateSupportHolder;
            }
            editStateSupportHolder = new EditStateSupport();
            return editStateSupportHolder;
        }
    }

    /**
     * Get code single-line displayable name for RadixWare object. Used for
     * visualization, for file name generation, etc. Do not use spaces,
     * international characters, punctuation marks, etc. It is recommended to
     * use java syntax for identifier naming.
     *
     * @return name for associated RadixWare object.
     */
    public String getName() {
        if (nameProvider != null) {
            return nameProvider.getName();
        }
        return name;
    }

    public NameProvider getNameProvider() {
        return nameProvider;
    }

    public void setNameProvider(NameProvider nameProvider) {
        this.nameProvider = nameProvider;
    }

    /**
     * Set name of Radix object. The function is protected, because some Radix
     * objects has only read-only name.
     *
     * @param name
     * @return true if name changed, false otherwise
     */
    public boolean setName(String name) {
        if (this.nameProvider != null) {
            return false;
        }
        if (name == null) {
            throw new IllegalStateException("Radix object name must not be null. Source: '" + this.getQualifiedName() + "'.");
        }

        switch (getNamingPolicy()) {
            case FREE:
            case UNIQUE_IDENTIFIER:
            case IDENTIFIER:
                String oldName = getName();
                this.name = name;
                String newName = getName();

                if (!Utils.equals(oldName, newName)) {
                    fireNameChange();
                    this.setEditState(EEditState.MODIFIED);
                    return true;
                } else {
                    return false;
                }
            case CONST:
                return false;
            case CALC:
                this.name = name;
                fireNameChange();
                return true;
            default:
                throw new IllegalStateException();
        }

    }

    protected void fireNameChange() {
        getRenameSupport().fireEvent(new RenameEvent(this));
    }

    public boolean isUserExtension() {
        for (RadixObject o = getContainer(); o != null; o = o.getContainer()) {
            if (o.isUserExtension()) {
                return true;
            }
            if (o instanceof Segment) {
                if (((Segment) o).getType() == ERepositorySegmentType.UDS) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Get full name for RadixWare object depends on usage context. Used for
     * debugging.
     *
     * @return qualified name of the object. For example:
     * org.radixware.kernel.designer.core.Definition
     */
    public String getQualifiedName(final RadixObject context) {
        if (context == null) {
            return getQualifiedName();
        }

        final Module thisModule = getModule();
        final Module contextModule = context.getModule();

        if (thisModule == null || contextModule == null) {
            return getQualifiedName();
        }

        if (thisModule == contextModule) {
            return getLocalName();
        }

        final Segment thisSegment = thisModule.getSegment();
        final Segment contextSegment = contextModule.getSegment();

        if (thisSegment == null || contextSegment == null) {
            return getQualifiedName();
        }

        final Layer thisLayer = thisSegment.getLayer();
        final Layer contextLayer = contextSegment.getLayer();

        if (thisLayer == null || contextLayer == null) {
            return getQualifiedName();
        }

        if (thisLayer == contextLayer) {
            if (this instanceof Module) {
                return this.getName();
            } else {
                return thisModule.getName() + "::" + getLocalName();
            }
        }

        if (this instanceof Module) {
            return thisLayer.getName() + "::" + this.getName();
        } else {
            return thisLayer.getName() + "::" + thisModule.getName() + "::" + getLocalName();
        }
    }

    /**
     * @return qualified name begins after module.
     */
    // final - to think after first overwrite.
    private String getLocalName() {
        final RadixObject ownerForQualifiedName = getOwnerForQualifedName();
        if (ownerForQualifiedName != null && !(ownerForQualifiedName instanceof Module)) {
            return ownerForQualifiedName.getLocalName() + ":" + getName();
        } else {
            return getName();
        }
    }

    public final RadixObject getOwnerForQualifedName() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner.isQualifiedNamePart()) {
                return owner;
            }
        }
        return null;
    }

    protected boolean isQualifiedNamePart() {
        return true;
    }

    /**
     * Get full name for RadixWare object. Used for debugging.
     *
     * @return qualified name of the object. For example:
     * org.radixware.kernel.designer.core.Definition
     */
    public String getQualifiedName() {
        final RadixObject qualifiedNameOwner = getOwnerForQualifedName();
        if (qualifiedNameOwner != null) {
            final String seperator = ((qualifiedNameOwner instanceof Module || qualifiedNameOwner instanceof Layer) ? "::" : ":");
            return qualifiedNameOwner.getQualifiedName() + seperator + getName();
        } else {
            return getName();
        }
    }

    /**
     * Get a human-readable multiline description for RadixWare object.
     *
     * @return description for associated RadixWare object or empty string if
     * none.
     */
    public String getDescription() {
        return "";
    }

    /**
     * Append additional tooltip. Designed for overriding.
     */
    protected void appendAdditionalToolTip(StringBuilder sb) {
    }

    /**
     * Append additional prefix to standard tooltip. Designed for overriding.
     */
    protected void insertToolTipPrefix(StringBuilder sb) {
    }

    protected final void appendLocationToolTip(StringBuilder sb) {
        final RadixObject ownerForQualifiedName = getOwnerForQualifedName();
        if (ownerForQualifiedName != null) {
            final String location = ownerForQualifiedName.getQualifiedName();
            sb.append("<br>Location: <br>&nbsp;");
            sb.append("<a href=\"");
            Definition ownerDef = getOwnerDefinition();

            if (ownerDef != null) {
                Id[] ids = ownerDef.getIdPath();
                boolean first = true;
                for (int i = 0; i < ids.length; i++) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(':');
                    }
                    sb.append(ids[i].toCharArray());
                }
            }
            sb.append("\">");
            sb.append(location);
            sb.append("</a>");
        }
    }

    /**
     * Get detailed description for RadixWare object in HTML style. By default
     * returns type name, name and location (owner qualified name) if owner
     * exist; Function final - override appendAdditionalTooltop().
     *
     * @return hint for the object.
     */
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    public String getToolTip(EIsoLanguage language, RadixObject context) {
        return getToolTip(language);
    }

    public String getToolTip(EIsoLanguage language) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        insertToolTipPrefix(sb);

        final String typeTitle = getTypeTitle();
        final String objectName = getName();
        sb.append("<b>").append(typeTitle).append(" '").append(objectName).append("'</b>");

        if (isInBranch()) { // otherwise, appendAdditionalToolTip can throw NullPointerException.
            appendAdditionalToolTip(sb);
        }

        appendLocationToolTip(sb);

        final String description = getDescriptionForToolTip(language);
        if (description != null && !description.isEmpty()) {
            List<String> descriptionLines = new LinkedList<>();
            String[] words = description.split(" ");
            StringBuilder currentLine = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                currentLine.append(words[i]);
                currentLine.append(' ');
                if (currentLine.length() > 120) {
                    descriptionLines.add(currentLine.toString());
                    currentLine.setLength(0);
                }
            }
            if (currentLine.length() > 0) {
                descriptionLines.add(currentLine.toString());
            }

            sb.append("<hr>Description: ");
            for (String dl : descriptionLines) {
                sb.append(StringEscapeUtils.escapeHtml(dl)).append("<br>");
            }
        }

        return sb.toString();
    }

    protected String getDescriptionForToolTip() {
        return getDescription();
    }

    protected String getDescriptionForToolTip(EIsoLanguage language) {
        return getDescriptionForToolTip();
    }

    /**
     * Get object RadixIcon
     *
     * @return RadixIcon (not null)
     */
    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.UNKNOWN;
    }

    /**
     * Get object container (real owner). For example, DdsColumnDef has
     * container - RadixObjects, but owner - DdsTableDef.
     */
    public final RadixObject getContainer() {
        return this.container;
    }

    /**
     * Set object container.
     */
    protected void setContainer(RadixObject container) {

        final boolean removed = setContainerNoFire(container);

        if (removed) {
            // notify all children about removing.
            visitAll(new IVisitor() {
                @Override
                public void accept(RadixObject object) {
                    object.getRemoveSupport().fireEvent(new RemovedEvent(object));
                }
            });
        }

    }

    /**
     * WARINING: Do not use this method if can not answer following questions:
     * <ul> <li> how dependences of module are really calculated when search
     * process initialized by class definition or XML scheme definition </li>
     * <li> how to locate actual superclass for class that is overwrite for
     * another class that is overwrite of another class and actual superclass is
     * also overwritten somewhere else </li> <li> what kind of modification of
     * property with nature Expression will cause incompatible mutation of layer
     * API </li> <li> what was the initial cause to write this method ("need for
     * setContainer() method with no events" - wrong answer) </li> </ul> Sets
     * radixObject's container. No events fired
     *
     * @return true if previous container was not null and new container was set
     * to null
     */
    protected boolean setContainerNoFire(RadixObject container) {
        if (this.container != null && container != null) { // throws then this.container==container, for example, table.getColumns.add(table.getColumns.get(0));
            throw new RadixObjectError("Radix object is already has an container.", this);
        }

        final boolean removed = (this.container != null && container == null);
        this.container = container;

        return removed;
    }

    /**
     * Get current edit state (in designer). The function is final because logic
     * of edit state must be standard.
     */
    public final RadixObject.EEditState getEditState() {
        if (isPersistent()) {
            return editState;
        } else {
            return EEditState.NONE;
        }

    }

    /**
     * Returns this object container if it really contains this object or null
     * Used in {@linkplain #setEditState(org.radixware.kernel.common.defs.RadixObject.EEditState)
     * }
     */
    protected boolean isSingleSideLinked() {
        final RadixObject thisContainer = getContainer();
        if (thisContainer != null && thisContainer instanceof RadixObjects) {
            return !((RadixObjects) thisContainer).contains(this);
        } else {
            return false;
        }
    }

    private void checkReadOnly() {
        if (!isReadOnly()) {
            return;
        }

        // Look RADIX-337
        final Branch branch = getBranch();
        if (branch == null) {
            return;
        }

        final IRepositoryBranch rep = branch.getRepository();
        if (rep == null) {
            return;
        }

        final RadixError error = new RadixError("An attempt to modify readonly object " + getQualifiedName());
        for (StackTraceElement stackTraceElement : error.getStackTrace()) {
            if (stackTraceElement.getClassName().startsWith("org.junit.")) {
                return;
            }
        }

        rep.processException(error);
    }

    private boolean setEditStateLocal(final EEditState newState) {
        EEditState oldState = this.editState;//NOPMD
        boolean needFireEvent = true;
        synchronized (supportSem) {
            if (this.editState == newState) {
                return false;
            }

            // создание - EditState не меняется и оФвыстается NEW, т.е. ничего не делаем
            // загрузка - после загрузки сбрасывается в NONE у главной дефиниции, это, в свою очередь, автоматически рекурсивно сбрасывает у детей до saveable (не включая).
            // попытка модификации - если было NEW, то таким и остается, иначе становится MODIFIED, в любом случае пытаемся модифицировать родилей до savable (включая)
            if (newState == EEditState.NONE) {
                this.editState = newState;
            } else { // Always MODIFIED
                if (this.editState == EEditState.NONE) {
                    checkReadOnly();
                    this.editState = newState;
                } else {
                    // safe NEW or MODIFIED
                    return false;
                }
            }
            if (editStateSupportHolder == null) {
                needFireEvent = false;//no one made call to getEditStateSupport, then no listeners. trick to save memory in server.
            }
        }

        if (needFireEvent) {
            getEditStateSupport().fireEvent(new EditStateChangedEvent(this, oldState, this.editState));
        }
        return true;
    }

    /**
     * Designed for overriding, called every time when
     * setEditState(EEditState.MODIFIED);
     */
    protected void onModified() {
    }

    /**
     * Linkage reset requirement indicator.
     *
     * @return true if linkage will be reseted after modification of the object,
     * false otherwise.
     */
    protected boolean isTemporary() {
        return false;
    }

    private boolean isTemporaryOrInTemporary() {
        for (RadixObject obj = this; obj != null; obj = obj.getContainer()) {
            if (obj.isTemporary()) {
                return true;
            }
        }
        return false;
    }
    private static final AtomicBoolean changeTrackingEnabled = new AtomicBoolean(true);
    private static final AtomicLong changeCounter = new AtomicLong(0);

    public static void enableChangeTracking() {
        changeTrackingEnabled.set(true);
    }

    public static long disableChangeTracking() {
        changeTrackingEnabled.set(false);
        return changeCounter.incrementAndGet();
    }

    public static boolean isChangeTrackingEnabled() {
        return changeTrackingEnabled.get();
    }

    public static long changeTrackingToken() {
        return changeCounter.get();
    }

    /**
     * Set edit state to the object (in designer). The function is final because
     * logic of edit state must be standard.
     *
     * @return true if edit state changed, false otherwise.
     */
    public final boolean setEditState(final EEditState newState) {
        if (newState == null) {
            throw new RadixObjectError("Edit state of Radix object must not be null.", this);
        }

        if (newState == EEditState.NEW) {
            throw new RadixObjectError("Edit state of Radix object must be NEW only at creation.", this);
        }

        if (newState == EEditState.NONE && !isSaveable()) {
            throw new RadixObjectError("Attempt to reset edit state of nonpersistent object.", this);
        }

        if (!isTemporaryOrInTemporary() && newState == EEditState.MODIFIED && changeTrackingEnabled.get()) {
            globalChangeVersion.incrementAndGet();
        }

        if (!isPersistent()) {
            return false;
        }

        final boolean result = setEditStateLocal(newState);

        if (result) {
            if (newState == EEditState.NONE) {
                // сбросить состояния у всех подобъектов до saveable (не включая), и ничего не подгружая, не придумал ничего лучше этого и следующего if.
                if (!(this instanceof IDirectoryRadixObject)) {
                    this.visitChildren(new IVisitor() {
                        @Override
                        public void accept(RadixObject object) {
                            object.setEditStateLocal(EEditState.NONE);
                        }
                    }, VisitorProviderFactory.createDefaultVisitorProvider());
                }
                if (this instanceof Module) {
                    RadixObject deps = ((Module) this).getDependences();
                    deps.setEditStateLocal(EEditState.NONE);
                }
            } else {
                if (!isSaveable()) {
                    final RadixObject thisContainer = getContainer();
                    if (thisContainer != null) {
                        // Solve RADIX-770: prevent owner modification for copy of object, that opened in modal editor.
                        if (!isSingleSideLinked()) {
                            thisContainer.setEditState(EEditState.MODIFIED);
                        }
                    }
                }
            }

            // register all saveable modified definitions
            if (this.isSaveable()) {
                getRegistry().fire(this);
            }
        }

        if (newState == EEditState.MODIFIED) {
            onModified();
        }

        return result;
    }

    /**
     * Get read only flag (in designer).
     *
     * @return true if layer of this object is base development layer or higher,
     * false otherwise
     */
    public boolean isReadOnly() {
        return container != null && container.isReadOnly();
    }

    /**
     * Visit all sub objects of the object.
     */
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
    }

    /**
     * Visit all objects in the object.
     *
     * @param visitor acceptor for objects (contains call back function).
     * @param provider defines rules for accepting of objects, see
     * {@linkplain VisitorProviderFactory}.
     */
    public final void visit(IVisitor visitor, VisitorProvider provider) {

        if (provider == null) {
            return;
        }

        if (provider.isCancelled()) {
            return;
        }

        if (provider.isTarget(this)) {
            visitor.accept(this);
        }

        if (provider.isContainer(this)) {
            visitChildren(visitor, provider);
        }
    }

    public final void visitAll(IVisitor visitor) {
        visit(visitor, VisitorProviderFactory.createDefaultVisitorProvider());
    }

    private static class BreakVisitException extends Error {

        final RadixObject object;

        public BreakVisitException(RadixObject object) {
            this.object = object;
        }
    }

    /**
     * Find first object by specified VisitorProvider.
     *
     * @return founded object or null if not found.
     */
    public final RadixObject find(VisitorProvider provider) {
        try {
            visit(new IVisitor() {
                @Override
                public void accept(RadixObject object) {
                    throw new BreakVisitException(object); // not nice, but most simple way.
                }
            }, provider);
            return null;
        } catch (BreakVisitException e) {
            return e.object;
        }
    }

    public static class RenameEvent extends RadixEvent {

        public final RadixObject radixObject;

        public RenameEvent(RadixObject radixObject) {
            this.radixObject = radixObject;
        }
    }

    public interface RenameListener extends IRadixEventListener<RenameEvent> {
    }

    public static class RenameSupport extends RadixEventSource<RenameListener, RenameEvent> {
    }

    public static class EditStateChangedEvent extends RadixEvent {

        public final RadixObject radixObject;
        public final EEditState oldEditState;
        public final EEditState newEditState;

        protected EditStateChangedEvent(RadixObject radixObject, EEditState oldEditState, EEditState newEditState) {
            this.radixObject = radixObject;
            this.newEditState = newEditState;
            this.oldEditState = oldEditState;
        }
    }

    public interface EditStateChangeListener extends IRadixEventListener<EditStateChangedEvent> {
    }

    public static class EditStateSupport extends RadixEventSource<EditStateChangeListener, EditStateChangedEvent> {
    }

    public void addEditStateListener(EditStateChangeListener l) {
        getEditStateSupport().addEventListener(l);
    }

    public void removeEditStateListener(EditStateChangeListener l) {
        getEditStateSupport().removeEventListener(l);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " '" + getQualifiedName() + "'";
    }

    /**
     * @return true if object is stored in separate file and object is a top
     * element of this file, false otherwise.
     */
    public boolean isSaveable() {
        return false;
    }

    /**
     * Indicates that modifications of this non-persistent object can't change
     * its and only its edit state.
     *
     * @return true if object is stored in file, false otherwise (for utilities
     * objects).
     */
    protected boolean isPersistent() {
        return true;
    }

    public File getFile() {
        RadixObject owner = getContainer();
        if (owner != null) {
            return owner.getFile();
        } else {
            return null;
        }
    }

    /**
     * Save radix obeject (find first saveable definition above and save it).
     *
     * @throws IOException
     * @see {@linkplain #isSaveable()}.
     */
    public void save() throws IOException {
        final RadixObject owner = getContainer();
        owner.save();
    }

    public static class RemovedEvent extends RadixEvent {

        public final RadixObject radixObject;

        protected RemovedEvent(RadixObject radixObject) {
            this.radixObject = radixObject;
        }
    }

    public interface IRemoveListener extends IRadixEventListener<RemovedEvent> {
    }

    public static class RemoveSupport extends RadixEventSource<IRemoveListener, RemovedEvent> {
    }

    /**
     * Remove object from owner collection.
     *
     * @return true if successfully, false otherwise.
     */
    public boolean delete() {
        RadixObject thisContainer = getContainer();
        if (thisContainer instanceof RadixObjects) {
            RadixObjects radixObjects = (RadixObjects) thisContainer;
            return radixObjects.remove(this);
        } else {
            if (thisContainer != null) {
                setContainer(null);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean canDelete() {
        if (isReadOnly()) {
            return false;
        }

        RadixObject thisContainer = getContainer();
        return (thisContainer instanceof RadixObjects);
    }

    /**
     * Collect all definitions to which refer current object.
     */
    public void collectDependences(List<Definition> list) {
        // Nothing by default
    }

    /**
     * Get owner module of the Radix object.
     *
     * @return owner module or null if object is in clipboard (for example), or
     * object is layer, segment or branch.
     */
    public Module getModule() {
        // overwritten in modules;
        for (RadixObject owner = this.getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof Module) {
                return (Module) owner;
            }
        }
        return null;
    }

    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return null;
    }

    /**
     * Get owner definition of the Radix object or itself if the object is
     * definition.
     *
     * @return definition or null if object is in clipboard (for example), or
     * object is layer, segment or branch.
     */
    public Definition getDefinition() {
        for (RadixObject curObject = this; curObject != null; curObject = curObject.getContainer()) {
            if (curObject instanceof Definition) {
                return (Definition) curObject;
            }

        }
        return null;
    }

    /**
     * Get owner definition of the Radix object
     *
     * @return definition or null if object is in clipboard (for example), or
     * object is layer, segment or branch.
     */
    public Definition getOwnerDefinition() {
        for (RadixObject owner = this.getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof Definition) {
                return (Definition) owner;
            }
        }
        return null;
    }

    public String getTypeTitle() {
        String className = this.getClass().getSimpleName();
        if (className.startsWith("Ads") || className.startsWith("Dds")) {
            className = className.substring(3);
        }

        if (className.endsWith("Def")) {
            className = className.substring(0, className.length() - 3);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i
                < className.length(); i++) {
            char c = className.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append(' ');
            }

            sb.append(c);
        }

        return sb.toString();
    }

    public String getTypesTitle() {
        final String typeTitle = getTypeTitle();
        if (typeTitle.endsWith("s")) {
            return typeTitle + "es";
        } else {
            return typeTitle + "s";
        }
    }

    /**
     * Registry of all modified saveable RadixObject
     */
    public static class Registry {

        private static final Map<RadixObject, Object> disableSet = new WeakHashMap<>();

        public void disableFor(RadixObject obj) {
            disableSet.put(obj, null);
        }

        public void enableFor(RadixObject obj) {
            disableSet.remove(obj);
        }

        public static class ModifiedSetChangedEvent extends RadixEvent {

            private final RadixObject radixObject;

            protected ModifiedSetChangedEvent(RadixObject radixObject) {
                this.radixObject = radixObject;
            }

            public RadixObject getRadixObject() {
                return radixObject;
            }
        }

        public interface IModifiedSetChangeListener extends IRadixEventListener<ModifiedSetChangedEvent> {
        }

        public static class ModifiedSetChangeSupport extends RadixEventSource<IModifiedSetChangeListener, ModifiedSetChangedEvent> {
        }
        private final ModifiedSetChangeSupport changeSupport = new ModifiedSetChangeSupport();

        protected void fire(RadixObject radixObject) {
            if (disableSet.containsKey(radixObject)) {
                return;
            }
            final ModifiedSetChangedEvent event = new ModifiedSetChangedEvent(radixObject);
            changeSupport.fireEvent(event);
        }

        /**
         * Get support to listen changes of modified RadixObject set.
         */
        public ModifiedSetChangeSupport getChangeSupport() {
            return changeSupport;
        }
    }
    /**
     * the only instance
     */
    private static Registry REGISTRY_INSTANCE = new Registry();

    /**
     * Get the registry containing all modified objects.
     *
     * @return the registry (singleton).
     */
    public static Registry getRegistry() {
        return REGISTRY_INSTANCE;
    }

    /**
     * @return true if cpecified child object located in this object, false
     * otherwise.
     */
    public boolean isParentOf(final RadixObject child) {
        for (RadixObject object = child.getContainer(); object != null; object = object.getContainer()) {
            if (object == this) {
                return true;
            }
        }
        return false;
    }

    public boolean isInBranch() {
        return getBranch() != null;
    }

    public Branch getBranch() {
        RadixObject radixObject = this;
        while (true) {
            RadixObject c = radixObject.getContainer();
            if (c == null) {
                if (radixObject instanceof Branch) {
                    return (Branch) radixObject;
                } else {
                    return null;
                }
            }
            radixObject = c;
        }
    }

    public Layer getLayer() {
        RadixObject radixObject = this;
        while (radixObject != null) {

            if (radixObject instanceof Layer) {
                return (Layer) radixObject;
            }
            radixObject = radixObject.getContainer();
        }
        return null;
    }

    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.UNIQUE_IDENTIFIER;
    }

    public void addRenameListener(RenameListener listener) {
        getRenameSupport().addEventListener(listener);
    }

    public void removeRenameListener(RenameListener listener) {
        getRenameSupport().removeEventListener(listener);
    }

    //look RADIX-2823:
    //should be overriden for objects that depends from
    //class catalogs and
    //add all class catalog list
    //to dependences list (ObjectPropertyPresentation, EntitySelectorItemDef in ads)
    //This method is public beacuse it is used in module editor
    public void collectDirectDependences(List<Definition> list) {
        collectDependences(list);
    }

    /**
     * Collects dependencies for module. Used for calculate dependencies of
     * module. Default implementation.
     */
    protected void collectDependencesForModule(List<Definition> list) {
        collectDirectDependences(list);
    }

    /**
     * Returns warning suppression support for the object
     */
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        return null;
    }

    /**
     * Wrapper for WarningSuppressionSupport
     */
    public boolean isWarningSuppressed(int code) {
        RadixProblem.WarningSuppressionSupport support = getWarningSuppressionSupport(false);
        if (support == null) {
            return false;
        } else {
            return support.isWarningSuppressed(code);
        }
    }

    public RadixProblem.ProblemFixSupport getProblemFixSupport() {
        return null;
    }

    public boolean needsDocumentation() {
        return true;
    }
}
