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

package org.radixware.kernel.common.defs.ads;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.check.RadixProblem.ProblemFixSupport;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyWalker.Controller;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AccessRules;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.adsdef.DescribedAdsDefinition.Domains.Domain;
import org.radixware.schemas.adsdef.UsageDescription;
import org.radixware.schemas.commondef.DescribedDefinition;
import org.radixware.schemas.commondef.NamedDefinition;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;

/**
 * Base interface for ADS Provides {@linkplain Definition}
 *
 */
public abstract class AdsDefinition extends Definition implements ILocalizedDef, IEnvDependent, ICompilable, IFileBasedDef, ILocalizedDescribable {

    public static class AccessChangedEvent extends RadixEvent {

        public final AdsDefinition source;

        private AccessChangedEvent(AdsDefinition source) {
            this.source = source;
        }
    }
    private boolean loadedFromAPI;

    public boolean wasLoadedFromAPI() {
        return loadedFromAPI;
    }

    public void markAsLoadedFromAPI() {
        loadedFromAPI = true;
    }

    public interface AccessListener extends IRadixEventListener<AccessChangedEvent> {
    }

    public static class AccessChangeSupport extends RadixEventSource<AccessListener, AccessChangedEvent> {
    };

    public interface IDeprecatable {

        public void setDeprecated(boolean isDeprecated);
    }

    public AccessChangeSupport getAccessChangeSupport() {
        synchronized (definitionLock) {
            if (accChangeSupport == null) {
                accChangeSupport = new AccessChangeSupport();
            }
            return accChangeSupport;
        }
    }

    public enum ESaveMode {

        /**
         * Save in source XML file.
         */
        NORMAL,
        /**
         * Save in API
         */
        API,
        /**
         * Save usage erasure
         */
        USAGE
    }

    public class Domains extends RadixObject {

        private ArrayList<AdsPath> domains = null;

        private Domains() {
            super("Domains");
            setContainer(AdsDefinition.this);
        }

        public Id[] getDomaindIds() {
            if (domains == null || domains.isEmpty()) {
                return new Id[0];
            }
            Id[] ids = new Id[domains.size()];
            for (int i = 0, len = domains.size(); i < len; i++) {
                ids[i] = domains.get(i).getTargetId();
            }
            return ids;
        }

        private Domains(DescribedAdsDefinition xDef) {
            this();
            if (xDef.getDomains() != null) {
                List<Domain> domainsList = xDef.getDomains().getDomainList();

                if (domainsList != null && !domainsList.isEmpty()) {
                    this.domains = new ArrayList<>(domainsList.size());
                    for (Domain d : domainsList) {
                        this.domains.add(new AdsPath(d.getPath()));
                    }
                }
            }
        }

        protected void appendTo(DescribedAdsDefinition xDef, ESaveMode saveMode) {
            synchronized (this) {
                if (domains != null && !domains.isEmpty()) {
                    DescribedAdsDefinition.Domains domainsSet = xDef.addNewDomains();
                    for (AdsPath path : domains) {
                        domainsSet.addNewDomain().setPath(path.asList());
                    }
                }
            }
        }

        public void addDomain(AdsDomainDef domain) {
            synchronized (this) {
                if (domain == null) {
                    return;
                }
                if (domains == null) {
                    domains = new ArrayList<>();
                }
                AdsPath path = new AdsPath(domain.getIdPath());
                if (domains.indexOf(path) < 0) {
                    domains.add(path);

                }
                AdsDefinition.this.setEditState(EEditState.MODIFIED);

            }
        }

        public void removeDomain(AdsDomainDef domain) {
            synchronized (this) {
                if (domains == null) {
                    return;
                }
                if (domains.remove(new AdsPath(domain.getIdPath()))) {
                    AdsDefinition.this.setEditState(EEditState.MODIFIED);
                }
            }
        }

        public void removeDomain(int index) {
            synchronized (this) {
                if (domains != null && index >= 0 && index < domains.size()) {
                    domains.remove(index);
                    AdsDefinition.this.setEditState(EEditState.MODIFIED);
                }
            }
        }

        public List<AdsPath> getUsedDomainPathes() {
            synchronized (this) {
                if (domains == null) {
                    return Collections.emptyList();
                } else {
                    return new ArrayList<>(domains);
                }
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            synchronized (this) {
                if (domains != null) {
                    for (AdsPath path : domains) {
                        List<Definition> defs = path.resolve(AdsDefinition.this).all();
                        for (Definition def : defs) {
                            if (def instanceof AdsDomainDef) {
                                list.add(def);
                            }
                        }
                    }
                }
            }
        }

        public void clearDomains() {
            synchronized (this) {
                this.domains = null;
            }
        }
    }

    public static abstract class Hierarchy<T extends AdsDefinition> {

        protected final T object;

        protected Hierarchy(T object) {
            this.object = object;
        }

        /**
         * Returns definition with the same id in the same container of extended
         * definition (for class - in superclass)
         */
        public abstract SearchResult<T> findOverridden();

        //   public abstract void collectAllOverriden(List<T> collection);
        /**
         * Returns definition with the same id in the same container of
         * overwrittent definition
         */
        public abstract SearchResult<T> findOverwritten();
        //   public abstract void collectAllOverwritten(List<T> collection);

        /**
         * Returns lowest definition in overwrite sequence
         */
        @SuppressWarnings("unchecked")
        public SearchResult<T> findOverwriteBase() {
            T result = object;
            SearchResult<T> ovr = findOverwritten();
            while (ovr.get() != null) {
                result = ovr.get();
                ovr = result.<T>getHierarchy().findOverwritten();
            }
            return SearchResult.single(result);
        }

        public boolean isDefaultHierarchy() {
            return false;
        }

        @SuppressWarnings("unchecked")
        static public <TDef extends AdsDefinition> boolean isOverwrittenDef(TDef base, TDef item) {
            if (base.getId() != item.getId()) {
                return false;
            }
            java.util.List<AdsDefinition> priorList = item.getHierarchy().findOverwritten().all();
            for (AdsDefinition prior : priorList) {
                if (prior == base) {
                    return true;
                }

                if (isOverwrittenDef(base, (TDef) prior)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected class DefaultHierarchy<T extends AdsDefinition> extends Hierarchy<T> {

        @SuppressWarnings("unchecked")
        public DefaultHierarchy() {
            super((T) AdsDefinition.this);
        }

        @Override
        public SearchResult<T> findOverridden() {
            return SearchResult.empty();
        }

        @Override
        public boolean isDefaultHierarchy() {
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public SearchResult<T> findOverwritten() {
            final AdsModule module = getModule();
            if (module != null) {
                Module.HierarchyWalker<AdsModule> walker = new Module.HierarchyWalker<>();
                final List<T> collection = new LinkedList<>();
                walker.go(module, new HierarchyWalker.Acceptor<AdsModule, Object>() {
                    @Override
                    public void accept(Controller<Object> controller, AdsModule ovr) {
                        if (ovr == module) {
                            return;
                        }
                        T def = null;
                        if (AdsDefinition.this instanceof AdsLocalizingBundleDef) {
                            AdsDefinition bundleOwner = ovr.getDefinitions().findById(((AdsLocalizingBundleDef) AdsDefinition.this).getBundleOwnerId());
                            if (bundleOwner != null) {
                                def = (T) bundleOwner.findExistingLocalizingBundle();
                            }
                        } else {
                            def = (T) ovr.getDefinitions().findById(getId());
                        }

                        if (def != null) {
                            try {
                                if (!collection.contains((T) def)) {
                                    collection.add((T) def);
                                }
                            } catch (ClassCastException e) {
                                Logger.getLogger(AdsDefinition.class.getName()).log(Level.FINE, e.getMessage(), e);
                            }
                            controller.pathStop();
                        }
                    }
                });
                if (collection.isEmpty()) {
                    return SearchResult.empty();
                } else {
                    return SearchResult.list(collection);
                }
            } else {
                return SearchResult.empty();
            }
        }
    }

    public static class DefaultHierarchyIterator<T extends AdsDefinition> extends HierarchyIterator<T> {

        private List<T> init;
        private List<T> current;
        private List<T> next;
        private final EScope scope;

        public DefaultHierarchyIterator(T init, EScope scope, HierarchyIterator.Mode mode) {
            super(mode);
            this.init = Collections.singletonList(init);
            this.current = Collections.singletonList(init);
            this.next = Collections.singletonList(init);
            this.scope = scope;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean hasNext() {
            if (this.next == null && this.current != null) {
                switch (scope) {
                    case ALL:
                    case LOCAL_AND_OVERWRITE:
                        final List<T> nextList = new LinkedList<>();
                        for (T cur : this.current) {
                            cur.<T>getHierarchy().findOverwritten().iterate(new SearchResult.Acceptor<T>() {
                                @Override
                                public void accept(T object) {
                                    if (!current.contains(object) && !nextList.contains(object)) {
                                        nextList.add(object);
                                    }
                                }
                            });
                        }
                        next = nextList;
                        break;

                }
            }
            return next != null && !next.isEmpty();
        }

        @Override
        public Chain<T> next() {
            if (hasNext()) {
                current = next;
                next = null;
                return Chain.newInstance(current);
            } else {
                return Chain.empty();
            }
        }
    }
    /**
     * Describes definition envelope
     */
    protected boolean isOverwrite;
    private AccessChangeSupport accChangeSupport = null;
    private final Object definitionLock = new Object();
    //

    public static abstract class EnvlopeInfo {

        /**
         * returns envelope type
         */
        public abstract EDefType getType();

        /**
         * returns envelope type hint
         */
        public Long getSubType() {
            return null;
        }

        /**
         * returns envelope namespace uri (for xml definitions)
         */
        public String getNamespaceUri() {
            return null;
        }
    }
    protected static final byte INIT_FLAG_ISFINAL = 0x1;
    private Domains domains = null;
    private ESaveMode saveMode;
    private EDefinitionUploadMode runtimeUploadMode;
    private EAccess access;
    protected byte initFlags;
    private boolean isFinal;
    private boolean isPublished = false;
    private Id idSourceEnumId = null;
    private Id idSourceItemId = null;
    private int[] compilerWarnings;
    private Id descriptionId;

    protected AdsDefinition(Id id) {
        super(id);
        this.runtimeUploadMode = EDefinitionUploadMode.ON_DEMAND;
    }

    protected AdsDefinition(Id id, String name) {
        super(id, name);
        this.runtimeUploadMode = EDefinitionUploadMode.ON_DEMAND;
    }

    protected AdsDefinition(DescribedAdsDefinition xDef) {
        this(xDef.getId(), xDef);
    }

    protected AdsDefinition(Id id, DescribedAdsDefinition xDef) {
        super(id, xDef.getName(), xDef.getDescription());
        this.isOverwrite = xDef.isSetIsOverwrite() ? xDef.getIsOverwrite() : false;
        if (xDef.getDomains() != null) {
            this.domains = new Domains(xDef);
        }
        if (xDef.getIdSrcEnum() != null && xDef.getIdSrcEnumItem() != null) {
            this.idSourceEnumId = xDef.getIdSrcEnum();
            this.idSourceItemId = xDef.getIdSrcEnumItem();
        }
        this.runtimeUploadMode = xDef.getUploadMode() == null ? EDefinitionUploadMode.ON_DEMAND : xDef.getUploadMode();
        if (xDef.getAccessRules() != null) {

            AccessRules rules = xDef.getAccessRules();
            if (rules.isSetAccess()) {
                int value = rules.getAccess();
                if (value == 5) {
                    this.access = EAccess.PUBLIC;
                } else {
                    this.access = EAccess.getForValue(Long.valueOf(rules.getAccess()));
                }
            } else {
                this.access = null;
            }
            if (rules.isSetIsFinal()) {
                this.isFinal = rules.getIsFinal();
                this.initFlags |= INIT_FLAG_ISFINAL;
            }

            if (rules.isSetIsPublished()) {
                this.isPublished = rules.getIsPublished();
            } else {
                this.isPublished = true;
            }
        } else {
            this.isPublished = true;
        }
        loadCompilerWarnings(xDef);

        if (xDef.isSetDescriptionId()) {
            descriptionId = xDef.getDescriptionId();
        }
    }

    private void loadCompilerWarnings(org.radixware.schemas.commondef.Definition xDef) {
        if (xDef.isSetCompilerWarnings()) {
            List list = xDef.getCompilerWarnings();
            compilerWarnings = new int[list.size()];
            int index = 0;
            for (Object o : list) {
                if (o instanceof Integer) {
                    compilerWarnings[index++] = ((Integer) o).intValue();
                }
            }
        }
    }

    protected AdsDefinition(DescribedDefinition xDef) {
        super(xDef.getId(), xDef.getName(), xDef.getDescription());
        this.isOverwrite = xDef.isSetIsOverwrite() ? xDef.getIsOverwrite() : false;
        this.runtimeUploadMode = EDefinitionUploadMode.ON_DEMAND;
        loadCompilerWarnings(xDef);
    }

    protected AdsDefinition(org.radixware.schemas.commondef.Definition xDef) {
        super(xDef.getId());
        this.isOverwrite = xDef.isSetIsOverwrite() ? xDef.getIsOverwrite() : false;
        this.runtimeUploadMode = EDefinitionUploadMode.ON_DEMAND;
        loadCompilerWarnings(xDef);
    }

    @Override
    public boolean isPublished() {
//        EAccess acc = getAccessMode();
//        if (acc == EAccess.PRIVATE || acc == EAccess.DEFAULT) {
//            return false;
//        }
        return isPublished;
    }

    public boolean isIdInheritanceAllowed() {
        return false;
    }

    public void setPublished(boolean isPublished) {
        if (isPublished != this.isPublished) {
            //if (isPublished) {
//                if (getAccessMode() == EAccess.DEFAULT || getAccessMode() == EAccess.PRIVATE) {
//                    return;
//                }
            //}
            this.isPublished = isPublished;
            setEditState(EEditState.MODIFIED);
            synchronized (this) {
                if (this.accChangeSupport != null) {
                    this.accChangeSupport.fireEvent(new AccessChangedEvent(this));
                }
            }
        }
    }

    @Override
    public boolean isFinal() {
        switch (getDefinitionType()) {
            case XML_SCHEME:
            case MSDL_SCHEME:
            case XSLT:
                return true;
        }
        if ((initFlags & INIT_FLAG_ISFINAL) == 0) {
            isFinal = getDefaultIsFinal();
            initFlags |= INIT_FLAG_ISFINAL;
        }
        return isFinal;
    }

    private Id getIdFromSource(AdsEnumItemDef item) {
        if (item != null) {
            final ValAsStr val = item.getValue();
            if (val != null) {
                final Object valAsStr = val.toObject(EValType.STR);
                if (valAsStr instanceof String) {
                    final Id id = Id.Factory.loadFrom((String) valAsStr);
                    try {
                        id.getPrefix();
                        return id;
                    } catch (NoConstItemWithSuchValueError e) {
                        Logger.getLogger(AdsDefinition.class.getName()).log(Level.FINE, e.getMessage(), e);
                    }
                }
            }

        }
        return null;
    }

    public boolean isIdInherited() {
        if (isIdInheritanceAllowed()) {
            return idSourceEnumId != null && idSourceItemId != null;
        } else {
            return false;
        }
    }

    public AdsEnumItemDef findIdSourceItem() {
        if (!isIdInheritanceAllowed()) {
            return null;
        }
        if (idSourceEnumId == null || idSourceItemId == null) {
            return null;
        }
        AdsPath path = new AdsPath(new Id[]{idSourceEnumId, idSourceItemId});
        Definition def = path.resolve(this).get();
        if (def instanceof AdsEnumItemDef) {
            return (AdsEnumItemDef) def;
        } else {
            return null;
        }
    }

    public Id getIdSourceEnumId() {
        return idSourceEnumId;
    }

    public Id getIdSourceItemId() {
        return idSourceItemId;
    }

    @SuppressWarnings("unchecked")
    public void setIdSourceItem(AdsEnumItemDef eitem) {
        if (eitem == null) {
            if (idSourceEnumId != null || idSourceItemId != null) {
                this.idSourceEnumId = null;
                this.idSourceItemId = null;
                setEditState(EEditState.MODIFIED);
            }
            return;
        }
        RadixObject container = getContainer();
        if (container instanceof Definitions) {
            Id itemId = eitem.getId();
            Id enumId = eitem.getOwnerEnum().getId();
            Id newId = getIdFromSource(eitem);
            if (itemId != idSourceItemId || enumId != idSourceEnumId || getId() != newId) {
                Definition def = ((Definitions) container).findById(newId);
                if (def != null) {
                    if (def != this) {
                        return;
                    }
                }
                this.idSourceEnumId = enumId;
                this.idSourceItemId = itemId;
                ((Definitions) container).unregister(this);
                setContainerNoFire(null);
                final Id oldId = getId();
                this.setId(newId);
                setContainerNoFire(container);
                ((Definitions) container).register(this);
                afterSetIdSourceItem(oldId);
            }

            AdsModule module = getModule();
            if (module != null && eitem.getModule() != null) {
                module.getDependences().add(eitem.getModule());
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    protected void afterSetIdSourceItem(Id oldId) {
    }

    public AdsEnumDef findIdSourceEnum() {
        if (idSourceEnumId == null) {
            return null;
        }
        AdsPath path = new AdsPath(new Id[]{idSourceEnumId});
        Definition def = path.resolve(this).get();
        if (def instanceof AdsEnumDef) {
            return (AdsEnumDef) def;
        } else {
            return null;
        }
    }

    public boolean canChangeFinality() {
        switch (getDefinitionType()) {
            case XML_SCHEME:
            case MSDL_SCHEME:
            case XSLT:
                return false;
            default:
                return true;
        }
    }

    public boolean canChangePublishing() {
        // EAccess access = getAccessMode();
        return true;//access != EAccess.DEFAULT && access != EAccess.PRIVATE;
    }

    public void setFinal(boolean isFinal) {
        if (isFinal != this.isFinal) {
            this.isFinal = isFinal;
            setEditState(EEditState.MODIFIED);
            synchronized (this) {
                if (this.accChangeSupport != null) {
                    this.accChangeSupport.fireEvent(new AccessChangedEvent(this));
                }
            }
        }
    }

    protected EAccess getDefaultAccess() {
        return EAccess.PUBLIC;
    }

    public EAccess getMinimumAccess() {
        return EAccess.PRIVATE;
    }

    protected boolean getDefaultIsFinal() {
        return false;
    }

    public boolean canBeFinal() {
        return true;
    }

    public EDefinitionUploadMode getRuntimeUploadMode() {
        return runtimeUploadMode;
    }

    public void setRuntimeUploadMode(EDefinitionUploadMode mode) {
        this.runtimeUploadMode = mode;
        setEditState(EEditState.MODIFIED);
    }

    public ESaveMode getSaveMode() {
        AdsDefinition def = findTopLevelDef();
        if (def == null) {
            return saveMode;
        }
        return def.saveMode;
    }

    public void setUploadMode(ESaveMode uploadMode) {
        if (isTopLevelDefinition()) {
            this.saveMode = uploadMode;
        } else {
            throw new RadixError("Upload mode changes are allowed for top level definitions only");
        }
    }

    /**
     * Any of ads definition could be associated with one or more
     * {@linkplain AdsDomain domains} This function returns list of identifiers
     * of domains definition currently associated with
     *
     * @return list of domain identifiers
     */
    public Domains getDomains() {
        synchronized (this) {
            if (domains == null) {
                domains = new Domains();
            }
            return domains;
        }
    }

    public boolean canChangeAccessMode() {
        return isTopLevelDefinition();
    }

    public EAccess getAccessMode() {
        if (access == null) {
            if (canChangeAccessMode()) {
                return getDefaultAccess();
            } else {
                AdsDefinition owner = getOwnerDef();
                if (owner == null) {
                    return getDefaultAccess();
                } else {
                    return owner.getAccessMode();
                }
            }
        } else {
            return access;
        }

    }

    public void setAccessMode(EAccess access) {
        if (canChangeAccessMode()) {
            if (access.isLess(getMinimumAccess())) {
                access = getMinimumAccess();
            }
            EAccess the_access = getAccessMode();
            if (access != the_access) {
                this.access = access;
                if (access == EAccess.PRIVATE || access == EAccess.DEFAULT) {
                    isPublished = false;
                }
                synchronized (definitionLock) {
                    if (accChangeSupport != null) {
                        accChangeSupport.fireEvent(new AccessChangedEvent(this));
                    }
                }
                setEditState(EEditState.MODIFIED);
            }
        } else {
            throw new DefinitionError("An attempt to change definition access mode");
        }
    }

    public <T extends AdsDefinition> Hierarchy<T> getHierarchy() {
        return new DefaultHierarchy<>();
    }

    public final AdsLocalizingBundleDef findExistingLocalizingBundle() {
        return findLocalizingBundleImpl(false);
    }

    public final AdsLocalizingBundleDef findLocalizingBundle() {
        return findLocalizingBundleImpl(true);
    }

    protected AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists) {
        if (this instanceof AdsLocalizingBundleDef) {
            return (AdsLocalizingBundleDef) this;
        }
        AdsDefinition def = findTopLevelDef();
        if (def == null) {
            return null;
        }
        AdsModule module = def.getModule();

        if (module == null) {
            return null;
        }

        return module.getDefinitions().findLocalizingBundleDef(def, createIfNotExists);
    }

    @Override
    public final AdsMultilingualStringDef findLocalizedString(Id stringId) {
        AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
        if (bundle != null) {
            return bundle.getStrings().findById(stringId, EScope.ALL).get();
        }
        return null;
    }

    public AdsDefinition findTopLevelDef() {
        AdsDefinition def = this;
        while (def != null) {
            if (def.isTopLevelDefinition()) {
                return def;
            }
            def = def.getOwnerDef();
        }
        return null;
    }

    public boolean isOverwrite() {
        return isOverwrite;
    }

    public boolean setOverwrite(boolean flag) {
        if (flag != isOverwrite) {
            isOverwrite = flag;
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    public int[] getCompilerWarnings() {
        return compilerWarnings == null ? new int[0] : compilerWarnings;
    }

    public boolean isCompilerWarningSuppressed(int code) {
        if (compilerWarnings != null) {
            for (int i = 0; i < compilerWarnings.length; i++) {
                if (compilerWarnings[i] == code) {
                    return true;
                }
            }
        }
        AdsDefinition owner = getOwnerDef();
        if (owner != null) {
            return owner.isCompilerWarningSuppressed(code);
        } else {
            return false;
        }
    }

    public void addCompilerWarning(int code) {
        if (compilerWarnings == null) {
            compilerWarnings = new int[]{
                code
            };
            setEditState(EEditState.MODIFIED);
        } else {
            for (int i = 0; i < compilerWarnings.length; i++) {
                if (compilerWarnings[i] == code) {
                    return;
                }
            }
            int[] newArray = new int[compilerWarnings.length + 1];
            System.arraycopy(compilerWarnings, 0, newArray, 0, compilerWarnings.length);
            newArray[compilerWarnings.length] = code;
            compilerWarnings = newArray;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void removeCompilerWarning(int code) {
        if (compilerWarnings == null) {
            return;
        }
        for (int i = 0; i < compilerWarnings.length; i++) {
            if (compilerWarnings[i] == code) {
                int[] newArray = new int[compilerWarnings.length - 1];
                if (i == 0) {
                    System.arraycopy(compilerWarnings, 1, newArray, 0, compilerWarnings.length - 1);
                } else if (i == compilerWarnings.length - 1) {
                    System.arraycopy(compilerWarnings, 0, newArray, 0, compilerWarnings.length - 1);
                } else {
                    System.arraycopy(compilerWarnings, 0, newArray, 0, i);
                    System.arraycopy(compilerWarnings, i + 1, newArray, 0, compilerWarnings.length - i - 1);
                }
                compilerWarnings = newArray;
                setEditState(EEditState.MODIFIED);
                return;
            }
        }
    }

    private void saveCompilerWarnings(org.radixware.schemas.commondef.Definition xDef, ESaveMode saveMode) {
        if (saveMode == ESaveMode.NORMAL) {
            if (compilerWarnings != null && compilerWarnings.length > 0) {
                List<Integer> list = new ArrayList<Integer>(compilerWarnings.length);
                for (int i = 0; i < compilerWarnings.length; i++) {
                    list.add(Integer.valueOf(compilerWarnings[i]));
                }
                xDef.setCompilerWarnings(list);
            }
        }
    }

    /**
     * Saves definition to xml
     */
    public void appendTo(DescribedAdsDefinition xDef, ESaveMode saveMode) {
        synchronized (this) {
            xDef.setId(super.getId());
            xDef.setName(getPersistentName());
            if (idSourceEnumId != null && idSourceItemId != null) {
                xDef.setIdSrcEnum(idSourceEnumId);
                xDef.setIdSrcEnumItem(idSourceItemId);
            }
            saveCompilerWarnings(xDef, saveMode);
            if (saveMode == ESaveMode.API) {
                xDef.setDefinitionType(getDefinitionType());
            }
            if (saveMode != ESaveMode.USAGE) {
                final String description = getDescription();
                if (description != null && !description.isEmpty()) {
                    xDef.setDescription(description);
                }
                if (isOverwrite()) {
                    xDef.setIsOverwrite(true);
                }
                if (domains != null) {
                    domains.appendTo(xDef, saveMode);
                }
                if (runtimeUploadMode != EDefinitionUploadMode.ON_DEMAND) {
                    xDef.setUploadMode(runtimeUploadMode);
                }
            }
            if (canChangeAccessMode()) {
                EAccess acc = getAccessMode();
                boolean isf = isFinal;
                if (acc != getDefaultAccess() || isf != getDefaultIsFinal() || !isPublished()) {
                    AccessRules rules = xDef.getAccessRules();
                    if (rules == null) {
                        rules = xDef.addNewAccessRules();
                    }
                    if (acc != getDefaultAccess()) {
                        rules.setAccess(acc.getValue().intValue());
                    }
                    if (isf != getDefaultIsFinal()) {
                        rules.setIsFinal(isf);
                    }
                    if (!isPublished()) {
                        rules.setIsPublished(false);
                    }
                }
            }

            if (descriptionId != null) {
                xDef.setDescriptionId(descriptionId);
            }
        }
    }

    /**
     * Saves definition to XML
     */
    public void appendTo(NamedDefinition xDef, ESaveMode saveMode) {
        xDef.setId(super.getId());
        xDef.setName(getPersistentName());
        if (saveMode == ESaveMode.API) {
            xDef.setDefinitionType(getDefinitionType());
        }
        saveCompilerWarnings(xDef, saveMode);
        if (isOverwrite) {
            xDef.setIsOverwrite(isOverwrite);
        }
    }

    /**
     * Saves definition to XML
     */
    public void appendTo(DescribedDefinition xDef, ESaveMode saveMode) {
        xDef.setId(super.getId());
        if (getDescription() != null && !getDescription().isEmpty()) {
            xDef.setDescription(getDescription());
        }
        xDef.setName(getPersistentName());
        if (saveMode == ESaveMode.API) {
            xDef.setDefinitionType(getDefinitionType());
        }
        saveCompilerWarnings(xDef, saveMode);
        if (isOverwrite) {
            xDef.setIsOverwrite(isOverwrite);
        }
    }

    /**
     * Saves definition to xml
     */
    public void appendTo(org.radixware.schemas.commondef.Definition xDef, ESaveMode saveMode) {
        xDef.setId(super.getId());
        if (saveMode == ESaveMode.API) {
            xDef.setDefinitionType(getDefinitionType());
        }
        saveCompilerWarnings(xDef, saveMode);
        if (isOverwrite) {
            xDef.setIsOverwrite(isOverwrite);
        }
    }

    /**
     * Returns definitions owner module module
     */
    @Override
    public final AdsModule getModule() {
        return (AdsModule) super.getModule();
    }

    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {//has mean
        //do nothing
    }

    public final AdsDefinition getOwnerDef() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsDefinition) {
                return (AdsDefinition) owner;
            }
        }
        return null;
    }

    static class ComponentFound extends Error {

        final AdsDefinition def;

        private ComponentFound(AdsDefinition def) {
            this.def = def;
        }
    }

    /**
     * Performs base overwrite actions for top level definition
     */
    protected boolean overwrite(AdsModule context, AdsDefinition def) {
        if (def == null) {
            throw new NullPointerException();
        }
        if (def.getClass() != this.getClass()) {
            return false;
        }

        if (def.getModule() != context && def.getModule().getId().equals(context.getId()) && context.getSegment().getLayer().isHigherThan(def.getModule().getSegment().getLayer()) && context.getDefinitions().findById(def.getId()) == null) {
            this.setId(def.getId());
            this.setName(def.getName() + " - Override");
            this.setDescription("Overwrites " + def.getQualifiedName());
            //this.setOverwrite(true);
            return true;
        }
        return false;

    }

    public final boolean isTopLevelDefinition() {
        final Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition == null) {
            //throw new RadixObjectError("The level of definition can not be determined: " + this.getClass().getName());
            return false;
        }
        return ownerDefinition instanceof AdsModule;
    }

    public String getLocalizedStringValue(EIsoLanguage language, Id stringId) {
        if (stringId == null) {
            return null;
        }
        AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
        if (bundle != null) {
            AdsMultilingualStringDef string = bundle.getStrings().findById(stringId, EScope.LOCAL_AND_OVERWRITE).get();
            if (string != null) {
                return string.getValue(language);
            }
        }

        return null;
    }

    public Id setLocalizedStringValue(EIsoLanguage language, Id stringId, String value, ELocalizedStringKind stringKind) {
        AdsLocalizingBundleDef bundle = findLocalizingBundle();
        if (bundle != null) {
            if (stringId != null) {
                AdsMultilingualStringDef def = bundle.getStrings().findById(stringId, EScope.LOCAL_AND_OVERWRITE).get();
                if (def != null) {
                    if (def.getOwnerBundle() != bundle) {
                        def = bundle.getStrings().overwrite(def);
                    }
                    def.setValue(language, value);
                    this.setEditState(EEditState.MODIFIED);
                    return def.getId();
                }
            }
            AdsMultilingualStringDef def = AdsMultilingualStringDef.Factory.newInstance(stringKind);
            if (def != null) {
                def.setValue(language, value);
                bundle.getStrings().getLocal().add(def);
                this.setEditState(EEditState.MODIFIED);
                return def.getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public final Id setLocalizedStringValue(EIsoLanguage language, Id stringId, String value) {
        return setLocalizedStringValue(language, stringId, value, ELocalizedStringKind.SIMPLE);
    }

    public Id cloneLocalizedString(Id stringId) {
        AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
        if (bundle != null) {
            if (stringId != null) {
                AdsMultilingualStringDef def = bundle.getStrings().findById(stringId, EScope.LOCAL_AND_OVERWRITE).get();
                if (def != null) {
                    this.setEditState(EEditState.MODIFIED);
                    return def.cloneString(bundle).getId();
                }
            }
            AdsMultilingualStringDef def = AdsMultilingualStringDef.Factory.newInstance();
            if (def != null) {
                bundle.getStrings().getLocal().add(def);
                this.setEditState(EEditState.MODIFIED);
                return def.getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public SearchResult<? extends AdsDefinition> findComponentDefinition(final Id id) {
        RadixObject result = this.find(new VisitorProvider() {
            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsDefinition) {
                    return ((AdsDefinition) object).getId().equals(id);
                } else {
                    return false;
                }
            }
        });
        if (result != null) {
            return SearchResult.single((AdsDefinition) result);
        } else {
            return SearchResult.empty();
        }
    }

    public Definition findComponentDefinition(final Id[] path) {
        if (path == null || path.length == 0 || path[0] != getId()) {
            return null;
        }
        RadixObject tmpRoot = this;

        for (int i = 1; i < path.length; i++) {
            final Id id2Search = path[i];
            RadixObject result = tmpRoot.find(new VisitorProvider() {
                @Override
                public boolean isContainer(RadixObject object) {
                    return true;
                }

                @Override
                public boolean isTarget(RadixObject object) {
                    if (object instanceof Definition) {
                        return ((Definition) object).getId().equals(id2Search);
                    } else {
                        return false;
                    }
                }
            });
            if (result == null) {
                return null;
            } else {
                tmpRoot = result;
            }
        }

        return (Definition) tmpRoot;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        synchronized (this) {
            super.collectDependences(list);
            if (domains != null) {
                domains.collectDependences(list);
            }
            AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
            if (bundle != null) {
                list.add(bundle);
            }
            if (isIdInherited()) {
                AdsEnumItemDef item = findIdSourceItem();
                if (item != null) {
                    list.add(item);
                }
            }
        }
    }

    @Override
    public File getFile() {
        final Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition instanceof AdsModule) {
            return ((AdsModule) ownerDefinition).getDefinitions().getSourceFile(this, getSaveMode());
        } else {
            return super.getFile();
        }
    }

    @Override
    public void save() throws IOException {
        // synchronized (this) {
        if (getSaveMode() == ESaveMode.API) {
            throw new IOException("Definition " + getQualifiedName() + "is read only");
        }
        Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition instanceof AdsModule) {
            ((AdsModule) ownerDefinition).getDefinitions().save(this, getSaveMode());
        } else {
            super.save();
        }
        // }
    }

    @Override
    public boolean delete() {
        final Definition ownerDefinition = getOwnerDefinition();
        File file = null;

        AdsLocalizingBundleDef bundle = null;
        if (ownerDefinition instanceof AdsModule) {
            if (this.getDefinitionType() != EDefType.LOCALIZING_BUNDLE) {
                file = getFile();
                bundle = this.findExistingLocalizingBundle();
            }
        } //else {
        //   file = null;
        // }

        if (!super.delete()) {
            return false;
        }

        if (file != null) {
            try {
                FileUtils.deleteFileExt(file);
            } catch (IOException cause) {
                throw new DefinitionError("Unable to delete definition file.", this, cause);
            }
        }

        if (bundle != null) {
            bundle.delete();
        }
        return true;
    }
    private long fileLastModifiedTime = 0L;

    @Override
    public long getFileLastModifiedTime() {
        synchronized (this) {
            AdsDefinition topLevel = findTopLevelDef();
            if (topLevel != null) {
                return topLevel.fileLastModifiedTime;
            }
            return fileLastModifiedTime;
        }
    }

    // method must be private, but loading is in other packages
    public void setFileLastModifiedTime(long fileLastModifiedTime) {
        synchronized (this) {
            this.fileLastModifiedTime = fileLastModifiedTime;
        }
    }

    public static Id fileName2DefinitionId(File definitionFile) {
        final String fileName = FileUtils.getFileBaseName(definitionFile);
        return Id.Factory.loadFrom(fileName);
    }

    /**
     * Returns definition usage enviornment
     */
    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        AdsDefinition ownerDef = getOwnerDef();
        if (ownerDef != null) {
            return ownerDef.getUsageEnvironment();
        } else {
            return ERuntimeEnvironmentType.COMMON;
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        if (descriptionId != null) {
            ids.add(new MultilingualStringInfo(AdsDefinition.this) {
                @Override
                public Id getId() {
                    return descriptionId;
                }

                @Override
                public void updateId(Id newId) {
                    descriptionId = newId;
                    setEditState(EEditState.MODIFIED);
                }

                @Override
                public EAccess getAccess() {
                    return AdsDefinition.this.getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return getTypeTitle().concat(" Description");
                }

                @Override
                public boolean isPublished() {
                    return AdsDefinition.this.isPublished();
                }

                @Override
                public EMultilingualStringKind getKind() {
                    return EMultilingualStringKind.DESCRIPTION;
                }
                
                
            });
        }
    }

    public boolean isSuitableContainer(AdsDefinitions collection) {
        return false;
    }

    @Override
    public ClipboardSupport<? extends AdsDefinition> getClipboardSupport() {
        return null;
    }

    public Id getLocalizingBundleId() {
        AdsDefinition topLevel = findTopLevelDef();
        Id id;
        if (topLevel == null) {
            id = getId();
        } else {
            id = topLevel.getId();
        }
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + id.toString());
    }

    @Override
    public final Id[] getIdPath() {
        return super.getIdPath();
    }

    protected String getPersistentName() {
        return getName();
    }

    public abstract EDefType getDefinitionType();

    public long getFormatVersion() {
        return 0;
    }

    @Override
    public final String getDescription(EIsoLanguage language) {
        return getDescriptionLocation().getLocalizedStringValue(language, getDescriptionId());
    }

    @Override
    public final boolean setDescription(EIsoLanguage language, String description) {
        setDescriptionId(setLocalizedStringValue(language, descriptionId, description, ELocalizedStringKind.DESCRIPTION));
        return descriptionId != null;
    }

    @Override
    public Id getDescriptionId() {
        return descriptionId;
    }

    @Override
    public void setDescriptionId(Id id) {
        if (!Objects.equals(descriptionId, id)) {
            this.descriptionId = id;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isReadOnly() {

        if (isTopLevelDefinition()) {
            if (saveMode == ESaveMode.API || super.isReadOnly()) {
                return true;
            } else {
                AdsModule module = getModule();
                if (module == null) {
                    return true;
                } else {
                    return module.isReadOnly(this);
                }
            }
        } else {
            return saveMode == ESaveMode.API || super.isReadOnly();
        }
    }

    @Override
    public ProblemFixSupport getProblemFixSupport() {
        return new AdsDefinitionProblems(this);
    }

    public void appendToUsage(UsageDescription xDef) {
        xDef.setDefinitionType(getDefinitionType());
        xDef.setPath(Arrays.asList(getIdPath()));
        xDef.setQName(getQualifiedName());
    }

    public static String getLocalizedDescriptionForToolTip(ILocalizedDescribable describable) {
        return getLocalizedDescriptionForToolTip(describable, EIsoLanguage.ENGLISH);
    }

    public static String getLocalizedDescriptionForToolTip(ILocalizedDescribable describable, EIsoLanguage language) {
        String description;

        if (describable.getDescriptionId() != null) {
            description = describable.getDescription(language);
            if (description != null && !description.isEmpty()) {
                return description;
            }

            final Definition descriptionLocation = describable.getDescriptionLocation();
            if (descriptionLocation != null && descriptionLocation.getLayer() != null) {
                final EIsoLanguage defaultLanguage = descriptionLocation.getLayer().getDefaultLanguage();
                if (defaultLanguage != null) {
                    description = describable.getDescription(defaultLanguage);
                    if (description != null && !description.isEmpty()) {
                        return description;
                    }
                }

                final IMultilingualStringDef localizedString = descriptionLocation.findLocalizedString(describable.getDescriptionId());
                if (localizedString != null) {
                    final Iterator<EIsoLanguage> iterator = localizedString.getLanguages().iterator();
                    while (iterator.hasNext()) {
                        description = describable.getDescription(iterator.next());
                        if (description != null && !description.isEmpty()) {
                            return description;
                        }
                    }
                }
            }
        }

        if (describable instanceof IDescribable) {
            description = ((IDescribable) describable).getDescription();
            if (description != null && !description.isEmpty()) {
                return description;
            }
        }
        return null;
    }

    @Override
    protected String getDescriptionForToolTip() {
        return getDescriptionForToolTip(EIsoLanguage.ENGLISH);
    }

    @Override
    protected String getDescriptionForToolTip(EIsoLanguage language) {
        AdsDefinition def = this;
        while (def != null) {
            String description = getLocalizedDescriptionForToolTip(def, language);
            if (description != null && !description.isEmpty()) {
                return description;
            }
            def = def.getHierarchy().findOverwritten().get();
        }
        return "";
    }

    public AdsDefinition findJavaPackageNameProvider() {
        return getHierarchy().findOverwriteBase().get();
    }
}
