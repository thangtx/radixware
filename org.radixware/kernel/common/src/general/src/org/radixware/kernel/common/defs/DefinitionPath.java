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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.USER_DEFINED_REPORT;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.USER_FUNC_CLASS;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Utility class that allows to find definition by one or several ids. Default
 * realization searches insize (not searched in overrides).
 *
 */
public abstract class DefinitionPath {

    static final EnumSet<EDefinitionIdPrefix> dialogPrefs = EnumSet.of(
            EDefinitionIdPrefix.CUSTOM_DIALOG,
            EDefinitionIdPrefix.CUSTOM_EDITOR,
            EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE,
            EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG,
            EDefinitionIdPrefix.CUSTOM_FORM_DIALOG,
            EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR,
            EDefinitionIdPrefix.CUSTOM_PROP_EDITOR,
            EDefinitionIdPrefix.CUSTOM_REPORT_DIALOG,
            EDefinitionIdPrefix.CUSTOM_SELECTOR,
            EDefinitionIdPrefix.CUSTOM_WIDGET);

    private static Id[] getWidgetPath(Id[] path) {
        if (isWidgetPath(path)) {
            final Id defId = path[path.length - 1];

            if (path.length > 1 && dialogPrefs.contains(path[path.length - 2].getPrefix())) {
                return path;
            }

            for (int i = path.length - 2; i >= 0; i--) {

                final EDefinitionIdPrefix pref = path[i].getPrefix();
                if (dialogPrefs.contains(pref)) {

                    Id[] widgetPath = new Id[i + 2];
                    System.arraycopy(path, 0, widgetPath, 0, i + 1);
                    widgetPath[i + 1] = defId;

                    return widgetPath;
                }
            }
        }

        return path;
    }

    private static boolean isWidgetPath(Id[] path) {
        if (path == null || path.length == 0) {
            return false;
        }
        
        for (final Id id : path) {
            if (id == null) {
                return false;
            }
        }
        
        return path[path.length - 1].getPrefix() == EDefinitionIdPrefix.WIDGET;
    }
    
    private static final Id[] EMPTY = new Id[]{};
    private Id[] elementIds;

    private void initDefinitionPath(Id[] ids) {
        if (isWidgetPath(ids)) {
            this.elementIds = getWidgetPath(ids);
        } else {
            this.elementIds = ids;
        }
    }

    public DefinitionPath(Definition source) {
        initDefinitionPath(source.getIdPath());
    }

    public DefinitionPath(Id[] ids) {
        final Id[] path = new Id[ids.length];
        System.arraycopy(ids, 0, path, 0, ids.length);

        initDefinitionPath(path);
    }

    public DefinitionPath(List<Id> ids) {
        if (ids == null) {
            this.elementIds = EMPTY;
        } else {
            final Id[] path = new Id[ids.size()];
            ids.toArray(path);
            
            initDefinitionPath(path);
        }
    }

    public DefinitionPath(DefinitionPath source) {
        final Id[] path = new Id[source.elementIds.length];
        System.arraycopy(source.elementIds, 0, path, 0, path.length);
        
        initDefinitionPath(path);
    }

    public boolean isEmpty() {
        return this.elementIds.length == 0;
    }

    private Definition resolveGlobal(Definition context, Id[] path) {
        Definition root = resolveRootGlobal(context, path);

        if (root == null) {
            root = context;
        }

        return findDefinition(root, path, context, null);
    }

    private void resolveAllGlobal(Definition context, Id[] path, List<Definition> resultSet) {
        List<Definition> roots = resolveRootsGlobal(context, path);

        if (roots.isEmpty()) {
            roots = Collections.singletonList(context);
        }

        for (Definition root : roots) {
            findDefinition(root, path, context, resultSet);
        }
    }

    private Definition resolve(Definition context, Id[] path) {
        List<Definition> resultList = context.checkPathIsKnown(path);
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }

        Definition root = resolveRoot(context, path);

        if (root == null) {
            root = context;
        }

        Definition result = findDefinition(root, path, context, null);
        context.rememberPath(path, Collections.singletonList(result));
        return result;
    }

    private void resolveAll(Definition context, Id[] path, List<Definition> resultSet) {

        List<Definition> roots = resolveRoots(context, path, false);

        if (roots.isEmpty()) {
            roots = new LinkedList<>();
            //roots.add(context);
        }

        for (Definition root : roots) {
            findDefinition(root, path, context, resultSet);
        }
    }

    private Definition resolveRoot(Definition context, Id[] path) {
        List<? extends Definition> roots = resolveRoots(context, path, true);
        if (roots.isEmpty()) {
            return null;
        } else {
            return roots.get(0);
        }
    }

    private List<Definition> resolveRoots(Definition context, Id[] path, boolean firstOnly) {
        if (path == null || path.length == 0 || path[0] == null) {
            return Collections.emptyList();
        }
        if (context == null) {
            return Collections.emptyList();
        }

        final Id id = path[0];

        if (id == context.getId()) {
            return Collections.singletonList(context);
        }


        if (id.getPrefix() == EDefinitionIdPrefix.MODULE) {
            if (context.getModule().getId() == id) {
                return Collections.<Definition>singletonList(context.getModule());
            } else {
                return (List<Definition>) (List<? extends Definition>) context.getModule().getDependences().findModuleById(id);
            }
        } else {
            final DefinitionSearcher searcher = getSearcher(id.getPrefix(), context);
            if (searcher != null) {
                if (firstOnly) {
                    SearchResult<Definition> def = searcher.findById(id);
                    if (def.get() == null) {
                        return Collections.emptyList();
                    } else {
                        return Collections.singletonList(def.get());
                    }
                } else {
                    return searcher.findById(id).all();
                }
            } else {
                return Collections.emptyList();
            }
        }
    }

    private List<Definition> resolveRootsGlobal(Definition context, Id[] path) {
        if (path == null || path.length == 0 || path[0] == null) {
            return Collections.emptyList();
        }
        if (context == null) {
            return Collections.emptyList();
        }

        final Id id = path[0];

        if (id == context.getId()) {
            return Collections.emptyList();
        }

        if (id.getPrefix() == EDefinitionIdPrefix.MODULE) {
            if (context.getModule().getId() == id) {
                return Collections.<Definition>singletonList(context.getModule());
            } else {
                final List<Definition> resultSet = new LinkedList<>();
                Layer.HierarchyWalker.walk(context.getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                        Module module = layer.getDds().getModules().findById(id);
                        if (module != null) {
                            resultSet.add(module);
                            controller.pathStop();
                        } else {
                            module = layer.getAds().getModules().findById(id);
                            if (module != null) {
                                resultSet.add(module);
                                controller.pathStop();
                            } else {
                                module = layer.getUds().getModules().findById(id);
                                if (module != null) {
                                    resultSet.add(module);
                                    controller.pathStop();
                                }
                            }
                        }
                    }
                });
                return resultSet;
            }
        } else {
            Layer layer = context.getLayer();
            EDefinitionIdPrefix idPrefix = id.getPrefix();
            final boolean forceDdsSearch = idPrefix.name().startsWith("DDS");
            final List<Definition> resultSet = new LinkedList<>();
            Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {
                @Override
                public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                    if (forceDdsSearch) {
                        Definition dds = findDdsDefinition(layer, id);
                        if (dds != null) {
                            resultSet.add(dds);
                            controller.pathStop();
                        }
                    } else {
                        Definition result = findTopLevelDefinition(layer.getAds(), id);
                        if (result != null) {
                            resultSet.add(result);
                            controller.pathStop();
                        } else {
                            result = findTopLevelDefinition(layer.getUds(), id);
                            if (result != null) {
                                resultSet.add(result);
                                controller.pathStop();
                            } else {
                                result = findDdsDefinition(layer, id);
                                if (result != null) {
                                    resultSet.add(result);
                                    controller.pathStop();
                                }
                            }
                        }
                    }
                }
            });
            return resultSet;
        }
    }

    private Definition resolveRootGlobal(Definition context, Id[] path) {
        if (path == null || path.length == 0 || path[0] == null) {
            return null;
        }
        if (context == null) {
            return null;
        }

        final Id id = path[0];

        if (id == context.getId()) {
            return context;
        }

        if (id.getPrefix() == EDefinitionIdPrefix.MODULE) {
            if (context.getModule().getId() == id) {
                return context.getModule();
            } else {
                return Layer.HierarchyWalker.walk(context.getLayer(), new Layer.HierarchyWalker.Acceptor<Module>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<Module> controller, Layer layer) {
                        Module module = layer.getDds().getModules().findById(id);
                        if (module != null) {
                            controller.setResultAndStop(module);
                        } else {
                            module = layer.getAds().getModules().findById(id);
                            if (module != null) {
                                controller.setResultAndStop(module);
                            } else {
                                module = layer.getUds().getModules().findById(id);
                                if (module != null) {
                                    controller.setResultAndStop(module);
                                }
                            }
                        }
                    }
                });
            }
        } else {
            Layer layer = context.getLayer();
            EDefinitionIdPrefix idPrefix = id.getPrefix();
            final boolean forceDdsSearch = idPrefix.name().startsWith("DDS");
            return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Definition>() {
                @Override
                public void accept(HierarchyWalker.Controller<Definition> controller, Layer layer) {
                    if (forceDdsSearch) {
                        Definition dds = findDdsDefinition(layer, id);
                        if (dds != null) {
                            controller.setResultAndStop(dds);
                        }
//                        else {
//                            controller.setResultAndStop(null);
//                        }

                    } else {
                        Definition result = findTopLevelDefinition(layer.getAds(), id);
                        if (result != null) {
                            controller.setResultAndStop(result);
                        } else {
                            result = findTopLevelDefinition(layer.getUds(), id);
                            if (result != null) {
                                controller.setResultAndStop(result);
                            } else {
                                result = findDdsDefinition(layer, id);
                                if (result != null) {
                                    controller.setResultAndStop(result);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private Definition findDdsDefinition(Layer layer, final Id id) {
        for (Module m : layer.getDds().getModules()) {
            Definition def = (Definition) m.find(new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof Definition && ((Definition) radixObject).getId() == id;
                }
            });
            if (def != null) {
                return def;
            }
        }
        return null;
    }

    private Definition findTopLevelDefinition(Segment<? extends Module> segment, final Id id) {
        for (Module m : segment.getModules()) {
            final Module module = m;
            Definition def = (Definition) module.find(new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof Definition && ((Definition) radixObject).getId() == id;
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return radixObject.getDefinition() == module;
                }
            });
            if (def != null) {
                return def;
            }
        }
        return null;
    }

    protected abstract DefinitionSearcher<? extends Definition> getSearcher(EDefinitionIdPrefix IdPrefix, Definition context);

    private class Link extends DefinitionLink<Definition> {

        private final WeakReference<Definition> contextRef;

        public Link(Definition context) {
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        protected Definition search() {
            final Definition context = contextRef.get();
            return resolve(context, elementIds);
        }
    }

    private class ListLink extends DefinitionListLink<Definition> {

        private final WeakReference<Definition> contextRef;

        public ListLink(Definition context) {
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        protected List<Definition> search() {
            final Definition context = contextRef.get();
            if (context != null) {
                List<Definition> result = context.checkPathIsKnown(elementIds);
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            }
            List<Definition> resultSet = new ArrayList<>(1);
            resolveAll(context, elementIds, resultSet);
            if (context != null) {
                context.rememberPath(elementIds, resultSet);
            }
            return resultSet;
        }
    }

    private class GlobalLink extends DefinitionLink<Definition> {

        private final WeakReference<Definition> contextRef;

        public GlobalLink(Definition context) {
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        protected Definition search() {
            final Definition context = contextRef.get();
            return resolveGlobal(context, elementIds);
        }
    }

    private class GlobalListLink extends DefinitionListLink<Definition> {

        private final WeakReference<Definition> contextRef;

        public GlobalListLink(Definition context) {
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        protected List<Definition> search() {
            final Definition context = contextRef.get();
            if (context != null) {
                List<Definition> result = context.checkPathIsKnown(elementIds);
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            }
            List<Definition> resultSet = new ArrayList<>(1);
            resolveAllGlobal(context, elementIds, resultSet);
            if (context != null) {
                context.rememberPath(elementIds, resultSet);
            }
            return resultSet;
        }
    }
    private ListLink listLink = null;
    private GlobalListLink globalListLink = null;

    public List<Definition> resolveAll(Definition context) {
        ListLink curLink = listLink;
        if (curLink == null || curLink.contextRef.get() != context) {
            curLink = new ListLink(context);
            listLink = curLink;
        }
        return curLink.find();
    }

    /**
     * Find definition within context scope.
     *
     * @return Definition or null.
     */
    public SearchResult<Definition> resolve(Definition context) {

        List<Definition> result = resolveAll(context);
        if (result.isEmpty()) {
            if (this.elementIds.length > 0) {
                Id checkId = this.elementIds[0];
                if (checkId != null) {
                    EDefinitionIdPrefix prefix = checkId.getPrefix();
                    if (prefix != null) {
                        switch (prefix) {
                            case USER_FUNC_CLASS:
                            case USER_DEFINED_REPORT:
                                if (context != null && context.isUserExtension()) {
                                    return resolveGlobal(context);
                                }
                                break;
                        }
                    }
                }
            }
            return SearchResult.empty();
        } else {
            return SearchResult.list(result);
        }
    }

    public SearchResult<Definition> resolveGlobal(Definition context) {
        List<Definition> result = resolveAllGlobal(context);
        if (result.isEmpty()) {
            return SearchResult.empty();
        } else {
            return SearchResult.list(result);
        }
    }

    public List<Definition> resolveAllGlobal(Definition context) {
        GlobalListLink curLink = globalListLink;
        if (curLink == null || curLink.contextRef.get() != context) {
            curLink = new GlobalListLink(context);
            globalListLink = curLink;
        }
        return curLink.find();
    }

    /**
     * Tries to find something from spefified Id sequence. For example, if
     * specified Class id and its property id, and property is missing, this
     * function returns class.
     *
     * @return Last found definition or null.
     */
    public Definition resolveSomething(Definition context) {
        Definition root = resolveRoot(context, elementIds);

        if (root != null) {

            int len = elementIds.length;
            do {
                Id[] ids = new Id[len];
                System.arraycopy(elementIds, 0, ids, 0, len);

                Definition def = findDefinition(root, ids, context, null);
                if (def != null) {
                    return def;
                }
                len--;
            } while (len > 0);

        }

        return root;
    }

    public List<Id> asList() {
        return Arrays.asList(elementIds);
    }

    public Id[] asArray() {
        return Arrays.copyOf(elementIds, elementIds.length);
    }

    public Id getTargetId() {
        return elementIds[elementIds.length - 1];
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        if (elementIds != null) {
            for (Id id : elementIds) {
                hashCode = 31 * hashCode + (id == null ? 0 : id.hashCode());
            }
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefinitionPath other = (DefinitionPath) obj;
        if (this.elementIds != other.elementIds && (this.elementIds == null || !Arrays.equals(elementIds, other.elementIds))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * Utility function, allows to print DefinitionPath even if it is null.
     */
    public static String toString(DefinitionPath path) {
        if (path != null && !path.isEmpty()) {
            StringBuilder b = new StringBuilder();
            boolean isFirst = true;
            b.append("#");
            for (Id id : path.asList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    b.append(".");
                }
                b.append(String.valueOf(id));
            }
            return b.toString();
        } else {
            return "#";
        }
    }

    /**
     * Tries to find definition component by given id
     */
    protected SearchResult<? extends Definition> findComponent(Definition root, final Id id, Definition initialContext) {
        if (root == null) {
            return SearchResult.empty();
        }
        RadixObject result = root.find(new VisitorProvider() {
            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof Definition) {
                    return ((Definition) object).getId().equals(id);
                } else {
                    return false;
                }
            }
        });
        if (result != null) {
            return SearchResult.single((Definition) result);
        } else {
            return SearchResult.empty();
        }
    }

    /**
     * Tries to find definition by given id sequence, if first element of given
     * array equals to definitions id it is ignored;
     */
    private Definition findDefinition(Definition theRoot, final Id[] ids, Definition initialContext, List<Definition> resultSet) {
        if (theRoot == null) {
            return null;
        }
        Definition root = theRoot;
        if (ids != null && ids.length > 0) {
            int start = 0;
            if (Utils.equals(ids[0], root.getId())) {
                start = 1;
            }
            if (start < ids.length) {
                return lookup(root, ids, start, initialContext, resultSet);
            } else {
                if (resultSet != null) {
                    resultSet.add(root);
                }
                return root;
            }
        } else {
            return null;
        }
    }

    private Definition lookup(Definition currentRoot, final Id[] ids, int idIndex, Definition initialContext, List<Definition> resultSet) {

        SearchResult<? extends Definition> localRoots = findComponent(currentRoot, ids[idIndex], initialContext);
        if (idIndex == ids.length - 1) {
            if (resultSet != null) {
                for (Definition d : localRoots.all()) {                    
                    if (!resultSet.contains(d)) {
                        resultSet.add(d);
                    }
                }
            }
            return localRoots.get();
        } else {
            Definition result = null;
            for (Definition d : localRoots.all()) {
                Definition local = lookup(d, ids, idIndex + 1, initialContext, resultSet);
                if (result == null) {
                    result = local;
                }
            }
            return result;
        }
    }
}
