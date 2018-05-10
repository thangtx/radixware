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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.common.meta.RadTitledDef;
import org.radixware.kernel.common.meta.RadUtils;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadJoinPropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Restrictions;

//TODO REFACTORING do and use here the generic class DbpObjList implements Iterable {DbpObjList(DbpObjList ancestorList), getByName(), getById()}
public class RadClassPresentationDef extends RadTitledDef {

    private static ThreadLocal<Boolean> LINKING = new ThreadLocal<>();

    private static enum Bits {

        MULTIFILE_REPORT(0x1),
        DOUBLE_PASS_REPORT(0x2);
        private int value;

        private Bits(int value) {
            this.value = value;
        }
    }
    private RadClassDef classDef = null;
    private final boolean isEntityClassPres;
    final private Map<Id, RadCommandDef> commandsById;
    private Map<String, RadCommandDef> commandsByName = null;
    final private Map<Id, RadSelectorPresentationDef> selectorPresentationsById;
    private Map<String, RadSelectorPresentationDef> selectorPresentationsByName = null;
    private final List<RadEditorPresentationDef> editorPresentations;
    final private Map<Id, RadEditorPresentationDef> editorPresentationsById;
    private Map<String, RadEditorPresentationDef> editorPresentationsByName = null;
    private Map<Id, RadPropertyPresentationDef> propPresentationsById;
    private final Map<Id, Id> replacePresIdByOrigPresId;
    private final RadEntityTitleFormatDef defaultObjectTitleFormat;    //Private fields
    private final Map<Id, RadSortingDef> sortingsById;
    private final Map<Id, RadFilterDef> filtersById;
    private final Restrictions restrictions;
    private final Id defaultSelectorPresentationId;
    private final Map<Id, ClassCatalog> classCatalogsById;
    private Collection<RadCommandDef> allCommands = null;
    private Collection<RadSortingDef> allSortings = null;
    private Collection<RadFilterDef> allFilters = null;
    private List<RadEditorPresentationDef> allEditorPresentations = null;
    private Set<EReportExportFormat> supportedReportFormats = null;
    private int bits;

    @Deprecated
    public RadClassPresentationDef(
            final Id classId,
            final String name,
            final Id titleId,
            final RadPropertyPresentationDef[] propPresentations,
            final RadCommandDef[] commands,
            final RadSortingDef[] sortings,
            final RadFilterDef[] filters,
            final RadColorSchemeDef[] colotSchemes,
            final RadEditorPresentationDef[] editorPresentations,
            final RadSelectorPresentationDef[] selectorPresentations,
            final Id defaultSelectorPresentationId,
            final Map<Id, Id> replacePresIdByOrigPresId,
            final RadEntityTitleFormatDef defaultObjectTitleFormat,
            final RadClassPresentationDef.ClassCatalog[] classCatalogs,
            final Restrictions restrictions) {
        this(classId, name, titleId, propPresentations, commands, sortings, filters, editorPresentations, selectorPresentations, defaultSelectorPresentationId, replacePresIdByOrigPresId, defaultObjectTitleFormat, classCatalogs, restrictions);
    }

    public RadClassPresentationDef(
            final Id classId,
            final String name,
            final Id titleId,
            final RadPropertyPresentationDef[] propPresentations,
            final RadCommandDef[] commands,
            final RadSortingDef[] sortings,
            final RadFilterDef[] filters,
            final RadEditorPresentationDef[] editorPresentations,
            final RadSelectorPresentationDef[] selectorPresentations,
            final Id defaultSelectorPresentationId,
            final Map<Id, Id> replacePresIdByOrigPresId,
            final RadEntityTitleFormatDef defaultObjectTitleFormat,
            final RadClassPresentationDef.ClassCatalog[] classCatalogs,
            final Restrictions restrictions,
            final Set<EReportExportFormat> supportedReportFormats,
            final int bits) {
        this(classId,
                name,
                titleId,
                propPresentations,
                commands,
                sortings,
                filters,
                editorPresentations,
                selectorPresentations,
                defaultSelectorPresentationId,
                replacePresIdByOrigPresId,
                defaultObjectTitleFormat,
                classCatalogs,
                restrictions,
                supportedReportFormats);
        this.bits = bits;
    }

    public RadClassPresentationDef(
            final Id classId,
            final String name,
            final Id titleId,
            final RadPropertyPresentationDef[] propPresentations,
            final RadCommandDef[] commands,
            final RadSortingDef[] sortings,
            final RadFilterDef[] filters,
            final RadEditorPresentationDef[] editorPresentations,
            final RadSelectorPresentationDef[] selectorPresentations,
            final Id defaultSelectorPresentationId,
            final Map<Id, Id> replacePresIdByOrigPresId,
            final RadEntityTitleFormatDef defaultObjectTitleFormat,
            final RadClassPresentationDef.ClassCatalog[] classCatalogs,
            final Restrictions restrictions,
            final Set<EReportExportFormat> supportedReportFormats) {
        this(classId,
                name,
                titleId,
                propPresentations,
                commands,
                sortings,
                filters,
                editorPresentations,
                selectorPresentations,
                defaultSelectorPresentationId,
                replacePresIdByOrigPresId,
                defaultObjectTitleFormat,
                classCatalogs,
                restrictions);
        if (supportedReportFormats == null || supportedReportFormats.isEmpty()) {
            this.supportedReportFormats = null;
        } else {
            this.supportedReportFormats = EnumSet.copyOf(supportedReportFormats);
        }
    }

    public RadClassPresentationDef(
            final Id classId,
            final String name,
            final Id titleId,
            final RadPropertyPresentationDef[] propPresentations,
            final RadCommandDef[] commands,
            final RadSortingDef[] sortings,
            final RadFilterDef[] filters,
            final RadEditorPresentationDef[] editorPresentations,
            final RadSelectorPresentationDef[] selectorPresentations,
            final Id defaultSelectorPresentationId,
            final Map<Id, Id> replacePresIdByOrigPresId,
            final RadEntityTitleFormatDef defaultObjectTitleFormat,
            final RadClassPresentationDef.ClassCatalog[] classCatalogs,
            final Restrictions restrictions) {
        super(classId, name, classId, titleId);
        if (propPresentations == null) {
            this.propPresentationsById = Collections.emptyMap();
        } else {
            this.propPresentationsById = new HashMap<Id, RadPropertyPresentationDef>(propPresentations.length * 2 + 1);
            for (RadPropertyPresentationDef propPres : propPresentations) {
                this.propPresentationsById.put(propPres.getPropId(), propPres);
                propPres.link(this);
            }
            this.propPresentationsById = Collections.unmodifiableMap(this.propPresentationsById);
        }
        this.selectorPresentationsById = selectorPresentations == null ? Collections.<Id, RadSelectorPresentationDef>emptyMap() : Collections.unmodifiableMap(RadUtils.<RadSelectorPresentationDef>arr2MapById(selectorPresentations));
        this.editorPresentationsById = editorPresentations == null ? Collections.<Id, RadEditorPresentationDef>emptyMap() : Collections.unmodifiableMap(RadUtils.<RadEditorPresentationDef>arr2MapById(editorPresentations));
        this.editorPresentations = editorPresentations == null ? Collections.<RadEditorPresentationDef>emptyList() : Arrays.asList(editorPresentations);

        this.commandsById = commands == null ? Collections.<Id, RadCommandDef>emptyMap() : Collections.unmodifiableMap(RadUtils.<RadCommandDef>arr2MapById(commands));

        this.replacePresIdByOrigPresId = replacePresIdByOrigPresId;
        this.defaultObjectTitleFormat = defaultObjectTitleFormat;

        if (sortings != null) {
            this.sortingsById = Collections.unmodifiableMap(RadUtils.arr2MapById(sortings));
        } else {
            this.sortingsById = Collections.emptyMap();
        }
        if (filters != null) {
            this.filtersById = Collections.unmodifiableMap(RadUtils.arr2MapById(filters));
        } else {
            this.filtersById = Collections.emptyMap();
        }
        if (classCatalogs != null && classCatalogs.length != 0) {
            final Map<Id, ClassCatalog> catalogs = new HashMap<Id, ClassCatalog>();
            for (ClassCatalog c : classCatalogs) {
                catalogs.put(c.getId(), c);
            }
            this.classCatalogsById = Collections.unmodifiableMap(catalogs);
        } else {
            this.classCatalogsById = Collections.emptyMap();
        }
        this.restrictions = restrictions;

        this.defaultSelectorPresentationId = defaultSelectorPresentationId;

        this.isEntityClassPres = (getClassId().getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS);
    }

    @Override
    public void link() {
        LINKING.set(true);
        super.link();
        getCommands();
        getCommandByName(null);
        getEditorPresentations();
        getSelectorPresentations();
        getEditorPresentationByName(null);
        getSelectorPresentationByName(null);
        getSortings();
        getFilters();
        for (ClassCatalog catalog : classCatalogsById.values()) {
            catalog.getPresentation();
        }
        LINKING.set(false);
    }

    public void link(final RadClassDef classDef) {
        this.classDef = classDef;
        final Release release = classDef.getRelease();
        for (RadSelectorPresentationDef selPres : this.selectorPresentationsById.values()) {
            selPres.link(this);
        }
        for (RadEditorPresentationDef edPres : this.editorPresentationsById.values()) {
            edPres.link(this);
        }
        for (RadCommandDef c : commandsById.values()) {
            c.link(release);
        }
        for (ClassCatalog c : classCatalogsById.values()) {
            c.link(this);
        }
    }

    public Id getClassId() {
        return getTitleOwnerDefId();
    }

    public RadClassDef getClassDef() {
        return classDef;
    }

    public String getTitle() {
        if (getTitleId() == null) {
            if (isEntityClassPres || (getClassId().getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS)) {
                RadClassPresentationDef classPresentationDef = getAncestorPres();
                if (classPresentationDef != null) {
                    return classPresentationDef.getTitle();
                }
            }
        }
        return super.getTitle(getClassDef().getRelease().getReleaseVirtualEnviroment());
    }

    /**
     * @return the propPresentationsById
     */
    Map<Id, RadPropertyPresentationDef> getPropPresentationsById() {
        return propPresentationsById;
    }

    public final Collection<RadCommandDef> getCommands() {
        if (allCommands != null) {
            return allCommands;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres == null) {
            if (commandsById != null) {
                allCommands = commandsById.values();
            } else {
                allCommands = Collections.<RadCommandDef>emptyList();
            }
        } else {
            allCommands = ancsPres.getCommands();
            if (commandsById != null) {
                allCommands = new ArrayList<RadCommandDef>(allCommands);
                allCommands.addAll(commandsById.values());
                allCommands = Collections.unmodifiableCollection(allCommands);
            }
        }
        return allCommands;
    }

    public final RadCommandDef getCommandById(final Id cmdId) {
        final RadCommandDef cmd = commandsById.get(cmdId);
        if (cmd != null) {
            return cmd;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getCommandById(cmdId);
        }
        throw new DefinitionNotFoundError(cmdId);
    }

    public final RadCommandDef getCommandByName(final String cmdName) {
        if (commandsByName == null)//index "by name" creates on first query "by name"
        {
            commandsByName = Collections.unmodifiableMap(RadUtils.<RadCommandDef>map2MapByName(commandsById));
        }
        final RadCommandDef cmd = commandsByName.get(cmdName);
        if (cmd != null) {
            return cmd;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getCommandByName(cmdName);
        }
        if (Objects.equals(LINKING.get(), Boolean.TRUE)) {
            return null;
        }
        throw new IllegalUsageError("Command with Name=\"" + cmdName + "\" not defined");
    }

    public boolean isDefaultSelectorPresentationDefined() {
        return getEntityClassPresentation().defaultSelectorPresentationId != null;
    }

    public RadSelectorPresentationDef getDefaultSelectorPresentation() {
        return getEntityClassPresentation().getSelectorPresentationById(getEntityClassPresentation().defaultSelectorPresentationId);
    }

    public Restrictions getDefRestrictions() {
        return getEntityClassPresentation().restrictions;
    }

    public final RadEditorPresentationDef getEditorPresentationById(final Id presId) {
        final RadEditorPresentationDef pres = editorPresentationsById.get(presId);
        if (pres != null) {
            return pres;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getEditorPresentationById(presId);
        }
        throw new DefinitionNotFoundError(presId);
    }

    public final RadEditorPresentationDef getEditorPresentationByName(final String presName) {
        if (editorPresentationsByName == null)//index "by name" creates on first query "by name"
        {
            editorPresentationsByName = Collections.unmodifiableMap(RadUtils.<RadEditorPresentationDef>map2MapByName(editorPresentationsById));
        }
        final RadEditorPresentationDef pres = editorPresentationsByName.get(presName);
        if (pres != null) {
            return pres;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getEditorPresentationByName(presName);
        }
        if (Objects.equals(LINKING.get(), Boolean.TRUE)) {
            return null;
        }
        throw new IllegalUsageError("Editor presentation \"" + presName + "\" is not defined");
    }

    public final RadSelectorPresentationDef getSelectorPresentationById(final Id presId) {
        final RadSelectorPresentationDef pres = selectorPresentationsById.get(presId);
        if (pres != null) {
            return pres;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getSelectorPresentationById(presId);
        }
        throw new DefinitionNotFoundError(presId);
    }

    public final RadSelectorPresentationDef getSelectorPresentationByName(final String presName) {
        if (selectorPresentationsByName == null)//index "by name" creates on first query "by name"
        {
            selectorPresentationsByName = Collections.synchronizedMap(RadUtils.<RadSelectorPresentationDef>map2MapByName(selectorPresentationsById));
        }
        final RadSelectorPresentationDef pres = selectorPresentationsByName.get(presName);
        if (pres != null) {
            return pres;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getSelectorPresentationByName(presName);
        }
        if (Objects.equals(LINKING.get(), Boolean.TRUE)) {
            return null;
        }
        throw new IllegalUsageError("Selector presentation \"" + presName + "\" is not defined");
    }

    public final Id getReplacedPresentationId(final Id replacePresId) {
        if (replacePresIdByOrigPresId != null) {
            for (Map.Entry<Id, Id> e : replacePresIdByOrigPresId.entrySet()) {
                if (e.getValue().equals(replacePresId)) {
                    return e.getKey();
                }
            }
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getReplacedPresentationId(replacePresId);
        }
        return null;
    }

    public final Id getActualPresentationId(final Id origPresId) {
        if (replacePresIdByOrigPresId != null && replacePresIdByOrigPresId.containsKey(origPresId)) {
            return replacePresIdByOrigPresId.get(origPresId);
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getActualPresentationId(origPresId);
        }
        return origPresId;
    }

    public final RadEntityTitleFormatDef getDefaultObjectTitleFormat() {
        return getEntityClassPresentation().defaultObjectTitleFormat;
    }

    public final RadFilterDef getFilterById(final Id id) {
        final RadFilterDef res = filtersById == null ? null : filtersById.get(id);
        if (res != null) {
            return res;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getFilterById(id);
        }
        throw new DefinitionNotFoundError(id);
    }

    public final boolean isFilterExistsById(final Id id) {
        final RadFilterDef res = filtersById == null ? null : filtersById.get(id);
        if (res != null) {
            return true;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null && ancsPres.isFilterExistsById(id)) {
            return true;
        }
        return false;
    }

    public final RadFilterDef getFilterByName(final String name) {
        RadFilterDef flt;
        if (filtersById != null) {
            final Iterator iter = filtersById.values().iterator();
            while (iter.hasNext()) {
                flt = (RadFilterDef) iter.next();
                if (flt.getName().equals(name)) {
                    return flt;
                }
            }
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getFilterByName(name);
        }
        throw new IllegalUsageError("Predefined filter with Name=\"" + name + "\" not defined");
    }
    private Collection<RadSelectorPresentationDef> allSelectorPresentations = null;

    public final Collection<RadSelectorPresentationDef> getSelectorPresentations() {
        if (allSelectorPresentations != null) {
            return allSelectorPresentations;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres == null) {
            if (selectorPresentationsById != null) {
                allSelectorPresentations = selectorPresentationsById.values();
            } else {
                allSelectorPresentations = Collections.<RadSelectorPresentationDef>emptyList();
            }
        } else {
            allSelectorPresentations = ancsPres.getSelectorPresentations();
            if (selectorPresentationsById != null) {
                allSelectorPresentations = new ArrayList<RadSelectorPresentationDef>(allSelectorPresentations);
                allSelectorPresentations.addAll(selectorPresentationsById.values());
                allSelectorPresentations = Collections.unmodifiableCollection(allSelectorPresentations);
            }
        }
        return allSelectorPresentations;
    }

    public final RadPropertyPresentationDef getPropPresById(final Id propId) {
        final RadPropertyPresentationDef propPres = getPropPresentationsById().get(propId);
        if (propPres != null) {
            return propPres;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getPropPresById(propId);
        }
        return null;
    }

    private final RadClassPresentationDef getAncestorPres() {
        final RadClassDef clDef = getClassDef();
        if (clDef.getAncestorId() == null) {
            return null;
        }
        final RadClassDef ancestor = getClassDef().getRelease().getClassDef(clDef.getAncestorId());
        return ancestor.getPresentation();
    }

    public final RadSortingDef getSortingById(final Id id) {
        final RadSortingDef res = sortingsById == null ? null : sortingsById.get(id);
        if (res != null) {
            return res;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getSortingById(id);
        }
        throw new DefinitionNotFoundError(id);
    }

    public final RadSortingDef getSortingByName(final String name) {
        RadSortingDef srt;
        if (sortingsById != null) {
            final Iterator iter = sortingsById.values().iterator();
            while (iter.hasNext()) {
                srt = (RadSortingDef) iter.next();
                if (srt.getName().equals(name)) {
                    return srt;
                }
            }
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getSortingByName(name);
        }
        throw new IllegalUsageError("Predefined sorting with Name=\"" + name + "\" not defined");
    }

    public final Collection<RadSortingDef> getSortings() {
        if (allSortings != null) {
            return allSortings;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres == null) {
            if (sortingsById != null) {
                allSortings = sortingsById.values();
            } else {
                allSortings = Collections.<RadSortingDef>emptyList();
            }
        } else {
            allSortings = ancsPres.getSortings();
            if (sortingsById != null) {
                allSortings = new ArrayList<RadSortingDef>(allSortings);
                allSortings.addAll(sortingsById.values());
                allSortings = Collections.unmodifiableCollection(allSortings);
            }
        }
        return allSortings;
    }

    public final Collection<RadFilterDef> getFilters() {
        if (allFilters != null) {
            return allFilters;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres == null) {
            if (filtersById != null) {
                allFilters = filtersById.values();
            } else {
                allFilters = Collections.<RadFilterDef>emptyList();
            }
        } else {
            allFilters = ancsPres.getFilters();
            if (filtersById != null) {
                allFilters = new ArrayList<RadFilterDef>(allFilters);
                allFilters.addAll(filtersById.values());
                allFilters = Collections.unmodifiableCollection(allFilters);
            }
        }
        return allFilters;
    }

    public ClassCatalog getClassCatalogById(final Id id) {
        final ClassCatalog res = classCatalogsById.get(id);
        if (res != null) {
            return res;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            return ancsPres.getClassCatalogById(id);
        }
        throw new DefinitionNotFoundError(id);
    }

    public ClassCatalog findClassCatalogById(final Id id) {
        final ClassCatalog res = classCatalogsById.get(id);
        if (res != null) {
            return res;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        return ancsPres == null ? null : ancsPres.getClassCatalogById(id);
    }

    final RadPropertyPresentationDef getPropPres(final Id propId, final EPropAttrInheritance inherBit) {
        RadPropertyPresentationDef propPres = getPropPresentationsById().get(propId);
        if (propPres != null && !propPres.getInheritanceMask().contains(inherBit)) {
            return propPres;
        }
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            try {
                ancsPres.getClassDef().getPropById(propId);
                return ancsPres.getPropPres(propId, inherBit);
            } catch (DefinitionNotFoundError e) {
                //This presentation can't inherit property attributes from the ancestor
                //because this property defined in this class.
            }
        }
        final RadPropDef prop = getClassDef().getPropById(propId);
        if (prop instanceof RadParentPropDef) {
            try {
                final RadClassPresentationDef refEntityDef = getClassDef().getRelease().getClassDef(((RadJoinPropDef) prop).getJoinedClassId()).getPresentation();
                return refEntityDef.getPropPres(((RadJoinPropDef) prop).getJoinedPropId(), inherBit);
            } catch (DefinitionNotFoundError e) {
                return null;
            }
        }
        return null;
    }

    public Set<EReportExportFormat> getSupportedReportFormats() {
        return supportedReportFormats;
    }

    public final String getRequestedRoleIdsStr() {
        return RadClassPresentationDef.getRequestedRoleIdsStr(getEditorPresentations());
    }

    static final String getRequestedRoleIdsStr(final Collection<RadEditorPresentationDef> edPresList) {
        final List<Id> roleIds = new ArrayList<Id>();
        for (RadEditorPresentationDef edPres : edPresList) {
            for (java.util.Map.Entry<Id, Restrictions> roleRestr : edPres.getRoleRestrById().entrySet()) {
                if (!roleRestr.getValue().getIsAccessRestricted() && !roleIds.contains(roleRestr.getKey())) {
                    roleIds.add(roleRestr.getKey());
                }
            }
        }
        final StringBuilder roleIdsStr = new StringBuilder(roleIds.size() * 30);
        for (Id roleId : roleIds) {
            if (roleIdsStr.length() != 0) {
                roleIdsStr.append(',');
            }
            roleIdsStr.append(roleId);
        }
        return roleIdsStr.toString();
    }

    public final List<RadEditorPresentationDef> getEditorPresentations() {
        if (allEditorPresentations != null) {
            return allEditorPresentations;
        }
        allEditorPresentations = new ArrayList<RadEditorPresentationDef>(editorPresentations);
        final RadClassPresentationDef ancsPres = getAncestorPres();
        if (ancsPres != null) {
            allEditorPresentations.addAll(ancsPres.getEditorPresentations());
        }
        allEditorPresentations = Collections.unmodifiableList(allEditorPresentations);
        return allEditorPresentations;
    }

    private final RadClassPresentationDef getEntityClassPresentation() {
        if (isEntityClassPres) {
            return this;
        }
        final RadClassPresentationDef ancestorPres = getAncestorPres();
        if (ancestorPres == null) {
            throw new IllegalUsageError("Class #" + getClassId() + " is not (based on) Entity Class");
        }
        return ancestorPres.getEntityClassPresentation();

    }

    public static final class ClassCatalog extends RadDefinition {

        private final ClassCatalog.ELinkMode linkMode;
        private final Collection<ClassCatalog.Item> ownItems;
        private List<ClassCatalog.ItemPresentation> presentation = null;
        private RadClassPresentationDef classPres;

        public static enum ELinkMode {

            VIRTUAL, FINAL
        }

        public ClassCatalog(
                final Id id,
                final ClassCatalog.ELinkMode linkMode,
                final ClassCatalog.Item[] items) {
            super(id);
            this.linkMode = linkMode;
            if (items != null) {
                this.ownItems = Collections.unmodifiableCollection(Arrays.asList(items));
            } else {
                this.ownItems = Collections.emptyList();
            }
        }

        public List<ClassCatalog.ItemPresentation> getPresentation() {
            if (presentation == null) {
                final Collection<ClassCatalog.Item> allItems;
                if (linkMode == ClassCatalog.ELinkMode.FINAL) {
                    allItems = ownItems;
                } else {
                    allItems = new LinkedList<>();
                    final RadClassDef classDef = classPres.getClassDef();
                    for (Id clasId : classDef.getRelease().getAllClassIdsBasedOnTableByTabId(classDef.getEntityId())) {
                        try {
                            ClassCatalog cat = classDef.getRelease().getClassDef(clasId).getPresentation().findClassCatalogById(getId());
                            if (cat != null && cat.linkMode == ClassCatalog.ELinkMode.VIRTUAL) {
                                allItems.addAll(cat.ownItems);
                            }
                        } catch (DefinitionNotFoundError e) {
                            Arte.get().getTrace().put(EEventSeverity.DEBUG, "Error while scanning class catalogs: " + ExceptionTextFormatter.throwableToString(e), EEventSource.EAS);
                        }
                    }
                }
                presentation = buildPresentation(allItems);
            }
            return presentation;
        }

        private static List<ClassCatalog.ItemPresentation> buildPresentation(final Collection<ClassCatalog.Item> items) {
            final List<ClassCatalog.Item> unprocessed = new LinkedList<>(items);
            final ClassCatalog.ItemPresentation root = new ClassCatalog.ItemPresentation(null, 0);
            final Map<Id, ClassCatalog.ItemPresentation> itemPresById = new HashMap<>();
            int prevUnprocessedSize = unprocessed.size() + 1;
            while (!unprocessed.isEmpty()) {
                if (unprocessed.size() >= prevUnprocessedSize) { //to avoid cycling forever
                    for (ClassCatalog.Item i : unprocessed) {
                        root.addChildItemPres(i);
                    }
                    unprocessed.clear();
                    break;
                } else {
                    prevUnprocessedSize = unprocessed.size();
                }
                final Iterator<ClassCatalog.Item> iter = unprocessed.iterator();

                while (iter.hasNext()) {
                    final ClassCatalog.Item i = iter.next();
                    ClassCatalog.ItemPresentation child = null;
                    if (i.getOwnerTopicId() == null) {
                        child = root.addChildItemPres(i);
                    } else {
                        final ClassCatalog.ItemPresentation ownerTopic = itemPresById.get(i.ownerTopicId);
                        if (ownerTopic != null) { //added already
                            child = ownerTopic.addChildItemPres(i);
                        }
                    }
                    if (child != null) { // processed
                        itemPresById.put(child.getId(), child);
                        iter.remove();
                    }
                }
            }
            final List<ClassCatalog.ItemPresentation> pres = new LinkedList<ClassCatalog.ItemPresentation>();
            root.addChildrenToClassCatalogPresentation(pres);
            return pres;
        }

        protected void link(final RadClassPresentationDef classPres) {
            this.classPres = classPres;
            for (ClassCatalog.Item i : ownItems) {
                i.link(classPres.getClassDef().getRelease());
            }
        }

        public static class Item extends RadDefinition {

            private final Id ownerTopicId;
            private final double order;
            private final Id titleOwnerId;
            private final Id titleId;
            private Release release = null;

            public Item(final Id id, final Id ownerTopicId, final double order, final Id titleOwnerId, final Id titleId) {
                super(id);
                this.ownerTopicId = ownerTopicId;
                this.order = order;
                this.titleId = titleId;
                this.titleOwnerId = titleOwnerId;
            }

            public double getOrder() {
                return order;
            }

            public Id getTitleId() {
                return titleId;
            }

            public Id getTitleOwnerId() {
                return titleOwnerId;
            }

            public Id getOwnerTopicId() {
                return ownerTopicId;
            }

            public String getTitle() {
                return MultilingualString.get(release.getReleaseVirtualEnviroment(), titleOwnerId, titleId);
            }

            protected void link(final Release release) {
                this.release = release;
            }

            protected Release getRelease() {
                return release;
            }

            public Id getClassId() {
                return null;
            }
        }

        public static final class Topic extends ClassCatalog.Item {

            public Topic(final Id topicId, final Id ownerTopicId, final double order, final Id titleOwnerId, final Id titleId) {
                super(topicId, ownerTopicId, order, titleOwnerId, titleId);
            }
        }

        public static final class ClassRef extends ClassCatalog.Item {

            private final boolean bInheritTitle;

            public ClassRef(final Id classId, final Id ownerTopicId, final double order, final boolean bInheritTitle, final Id titleOwnerId, final Id titleId) {
                super(classId, ownerTopicId, order, titleOwnerId, titleId);
                this.bInheritTitle = bInheritTitle;
            }

            @Override
            public final Id getClassId() {
                return getId();
            }

            @Override
            public final String getTitle() {
                if (bInheritTitle) {
                    return getRelease().getClassDef(getClassId()).getTitle();
                }
                return super.getTitle();
            }
        }

        public static final class ItemPresentation {

            private final ClassCatalog.Item item;
            private final int level;
            private List<ClassCatalog.ItemPresentation> children = null;

            protected ItemPresentation(
                    ClassCatalog.Item item,
                    int level) {
                this.item = item;
                this.level = level;
            }

            public int getLevel() {
                return level;
            }

            public Id getId() {
                return item != null ? item.getId() : null;
            }

            public Id getClassId() {
                return item != null ? item.getClassId() : null;
            }

            public String getTitle() {
                return item != null ? item.getTitle() : null;
            }

            public double getOrder() {
                return item != null ? item.getOrder() : 0;
            }

            protected ClassCatalog.ItemPresentation addChildItemPres(final ClassCatalog.Item childItem) {
                if (children != null) {// RADIX-2224 duplicate links
                    for (ClassCatalog.ItemPresentation c : children) {
                        if (c.getId() != null && c.getId().equals(childItem.getId())) {
                            return c;
                        }
                    }
                }
                final ClassCatalog.ItemPresentation child = new ClassCatalog.ItemPresentation(childItem, level + 1);
                if (children == null) {
                    children = new LinkedList<>();
                }

                for (int i = 0, l = children.size(); i < l; i++) {
                    ClassCatalog.ItemPresentation addedChild = children.get(i);
                    if (childItem.getOrder() < addedChild.getOrder()) {
                        children.add(i, child);
                        return child;
                    }
                }
                children.add(child);
                return child;
            }

            protected void addChildrenToClassCatalogPresentation(final List<ClassCatalog.ItemPresentation> list) {
                if (children != null) {
                    for (ClassCatalog.ItemPresentation child : children) {
                        list.add(child);
                        child.addChildrenToClassCatalogPresentation(list);
                    }
                }
            }

            @Override
            public String toString() {
                return "L" + String.valueOf(level) + ": " + getTitle() + ": #" + String.valueOf(getClassId());
            }
        }
    }

    //--------- bit stored boolean properties
    public boolean isMultiFileReport() {
        return classDef.getType() == EClassType.REPORT && (bits & Bits.MULTIFILE_REPORT.value) != 0;
    }

    public boolean isDoublePassReport() {
        return classDef.getType() == EClassType.REPORT && (bits & Bits.DOUBLE_PASS_REPORT.value) != 0;
    }
}
