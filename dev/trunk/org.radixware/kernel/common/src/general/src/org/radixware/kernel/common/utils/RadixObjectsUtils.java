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

package org.radixware.kernel.common.utils;

import java.util.*;
import java.util.regex.Pattern;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.HierarchyWalker.Controller;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;


public class RadixObjectsUtils {

    protected RadixObjectsUtils() {
    }
    private static final Pattern STRING_OF_DIGIT_REGEX = Pattern.compile("\\d+");

    /**
     * Comparator for two radix objects by name.
     *
     */
    static interface StringPropOwner {

        String getStringPropByRadixObject(RadixObject obj);
    }

    static abstract class RadixObjectStringComparator implements Comparator<RadixObject>, StringPropOwner {

        @Override
        public int compare(RadixObject radixObject1, RadixObject radixObject2) {
            final String val1 = getStringPropByRadixObject(radixObject1);
            final String val2 = getStringPropByRadixObject(radixObject2);

            int result = val1.compareTo(val2);
            if (result == 0) {
//                RadixObject owner1 = radixObject1.getOwnerForQualifedName();
//                RadixObject owner2 = radixObject2.getOwnerForQualifedName();
//                if (owner1 == null && owner2 != null) {
//                    result = -1;
//                } else if (owner1 != null && owner2 == null) {
//                    result = 1;
//                } else if (owner1 != null && owner2 != null) {
//                    result = owner1.getQualifiedName().compareTo(owner2.getQualifiedName());
//                }
            } else {
                if (STRING_OF_DIGIT_REGEX.matcher(val1).matches() && STRING_OF_DIGIT_REGEX.matcher(val2).matches()) {
                    final int len1 = val1.length();
                    final int len2 = val2.length();
                    if (len1 > len2) {
                        result = 1;
                    } else if (len1 < len2) {
                        result = -1;
                    }
                }
            }
            return result;
        }
    }

    static class RadixObjectNameComparator extends RadixObjectStringComparator {

        @Override
        public String getStringPropByRadixObject(RadixObject obj) {
            return obj.getName();
        }
    }

    static class RadixObjectQualifiedNameComparator extends RadixObjectStringComparator {

        @Override
        public String getStringPropByRadixObject(RadixObject obj) {
            return obj.getQualifiedName();
        }
    }

    public static void sortByQualifiedName(List<? extends RadixObject> radixObjects) {
        Collections.sort(radixObjects, new RadixObjectQualifiedNameComparator());
    }

    public static void sortByName(List<? extends RadixObject> radixObjects) {
        Collections.sort(radixObjects, new RadixObjectNameComparator());
    }

    /**
     * Get title of common type of specified objects. Used in ChooseDefinition
     * dialog, in delete questions, etc.
     */
    public static String getCommonTypeTitle(Collection<? extends RadixObject> radixObjects) {
        if (radixObjects.isEmpty()) {
            return "";
        }

        final RadixObject first = radixObjects.iterator().next();

        if (radixObjects.size() == 1) {
            return first.getTypeTitle();
        }

        boolean isAllDefinitions = true;
        boolean isAllEquals = true;

        final String typesTitle = first.getTypesTitle();
        for (RadixObject radixObject : radixObjects) {
            if (isAllEquals && !Utils.equals(typesTitle, radixObject.getTypesTitle())) {
                isAllEquals = false;
            }
            if (isAllDefinitions && !(radixObject instanceof Definition)) {
                isAllDefinitions = false;
            }
            if (!isAllDefinitions && !isAllEquals) {
                break;
            }
        }

        if (isAllEquals) {
            return typesTitle;
        } else if (isAllDefinitions) {
            return Definition.DEFINITION_TYPES_TITLE;
        } else {
            return RadixObject.RADIX_OBJECTS_TYPES_TITLE;
        }
    }

    public static <T extends RadixObject> void moveItems(RadixObjects<T> from, RadixObjects<T> into) {
        into.clear();
        List<T> objects = from.list();
        from.clear();
        for (T object : objects) {
            into.add(object);
        }
    }
    private static final Pattern NAME_PATTERN = Pattern.compile("[\\w\\. \\(\\)\\-]+");
    private static final Pattern URL_PATTERN = Pattern.compile("[\\w\\. $\\(\\)\\-]+");

    public static boolean isCorrectName(final String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isCorrectURL(final String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    private static class CollectorSet implements IVisitor {

        public Set<RadixObject> result = new HashSet<>();

        @Override
        public void accept(RadixObject object) {
            result.add(object);
        }
    }

    /**
     * @return set of Radix objects inside specified content (include content)
     */
    public static Set<RadixObject> collectAllInside(Collection<? extends RadixObject> contexts, VisitorProvider visitorProvider) {
        final CollectorSet collector = new CollectorSet();
        for (RadixObject context : contexts) {
            context.visit(collector, visitorProvider);
        }
        return collector.result;
    }

    /**
     * @return set of Radix objects inside specified content (include content)
     */
    public static List<Definition> collectAllInside(RadixObject context, VisitorProvider visitorProvider) {
        final DefinitionCollector definitionCollector = new DefinitionCollector();
        context.visit(definitionCollector, visitorProvider);
        return definitionCollector.definitions;
    }

    public static String underlinedName2CamelCasedName(String ulname) {
        if (ulname == null) {
            throw new NullPointerException();
        }
        char[] chars = ulname.toCharArray();
        char[] result = new char[chars.length];
        int index = 0;
        boolean toUpper = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '_':
                    toUpper = index > 0;
                    continue;
                default:
                    if (toUpper) {
                        result[index] = Character.toUpperCase(c);
                    } else {
                        result[index] = Character.toLowerCase(c);
                    }
                    toUpper = false;
                    index++;
            }
        }
        return String.valueOf(result, 0, index);
    }

    protected static class DefinitionCollector implements IVisitor {

        public DefinitionCollector() {
        }
        private final ArrayList<Definition> definitions = new ArrayList<>();

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof Definition) {
                definitions.add((Definition) radixObject);
            }
        }

        public ArrayList<Definition> getResult() {
            return definitions;
        }
    }

    protected static class RadixObjectCollector implements IVisitor {

        public RadixObjectCollector() {
        }
        private final ArrayList<RadixObject> definitions = new ArrayList<>();

        @Override
        public void accept(RadixObject radixObject) {

            definitions.add(radixObject);

        }

        public ArrayList<RadixObject> getResult() {
            return definitions;
        }
    }

    private static List<? extends RadixObject> getList(IVisitor visitor) {
        if (visitor instanceof DefinitionCollector) {
            return ((DefinitionCollector) visitor).getResult();
        } else if (visitor instanceof RadixObjectCollector) {
            return ((RadixObjectCollector) visitor).getResult();
        } else {
            return null;
        }

    }

    private static RadixObject findSingleSideLink(final Definition context) {
        // EditMask logic - ignored, not important
        for (RadixObject object = context; object != null && !(object instanceof Module); object = object.getContainer()) {
            final RadixObject container = object.getContainer();
            if (container instanceof RadixObjects) {
                final RadixObjects radixObjects = (RadixObjects) container;
                if (!radixObjects.contains(object)) {
                    return object;
                }
            }
        }
        return null;
    }

    private static void fixProblemOfModalEditorsWithOkCancel(final Definition context, final List<? extends RadixObject> list, final VisitorProvider visitorProvider) {
        final RadixObject object = findSingleSideLink(context);

        if (object != null) {
            object.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof Definition) {
                        final Definition def = (Definition) radixObject;
                        final Id[] idPath = def.getIdPath();
                        for (int i = list.size() - 1; i >= 0; i--) {
                            final RadixObject listObj = list.get(i);
                            if (listObj instanceof Definition) {
                                Definition def2 = (Definition) listObj;
                                if (Arrays.equals(def2.getIdPath(), idPath)) {
                                    list.remove(i);
                                }
                            }
                        }
                        ((List) list).add(def);
                    }
                }
            }, visitorProvider);
        }
    }

    private static List<? extends RadixObject> collectAroundDefinition(Definition context, VisitorProvider visitorProvider, IVisitor collector) {
        final Module module = context.getModule();
        assert module != null;

        final Set<Module> allowedModules = new HashSet<>();
        allowedModules.add(module);

        final Map<Id, Dependence> moduleId2Dependence = new HashMap<>();
        context.getDependenceProvider().collect(moduleId2Dependence);

        for (Dependence dep : moduleId2Dependence.values()) {
            final List<Module> dependentModule = dep.findDependenceModule(context);
            if (dependentModule != null && !dependentModule.isEmpty()) {
                allowedModules.addAll(dependentModule);
            }
        }

        for (Module allowedModule : allowedModules) {
            for (Module curModule = allowedModule; curModule != null; curModule = curModule.findOverwritten()) {
                curModule.visit(collector, visitorProvider);
            }
        }


        final List<? extends RadixObject> result = getList(collector);
        fixProblemOfModalEditorsWithOkCancel(context, result, visitorProvider);
        return result;
    }

    /**
     * Collect all definitions by specified visitor provider, that visible from
     * specified context (include overwrites).
     */
    public static List<Definition> collectAllAround(RadixObject context, final VisitorProvider visitorProvider) {
        assert context != null;
        assert visitorProvider != null;

        final DefinitionCollector collector = new DefinitionCollector();

        final Definition contextDefinition = context.getDefinition();
        if (contextDefinition != null) {
            collectAroundDefinition(contextDefinition, visitorProvider, collector);
        } else {
            if (context instanceof Layer) {
                Layer.HierarchyWalker.walk((Layer) context, new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(Controller controller, Layer layer) {
                        layer.visit(collector, visitorProvider);
                    }
                });

            } else if (context instanceof Segment) {
                final Segment segment = (Segment) context;
                final Layer root = segment.getLayer();

                Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(Controller controller, Layer layer) {
                        Segment s = layer.getSegmentByType(segment.getType());
                        if (s != null) {
                            s.visit(collector, visitorProvider);
                        }
                    }
                });

            } else {
                context.visit(collector, visitorProvider);
            }
        }

        return collector.getResult();
    }

    public static List<RadixObject> collectAllObjectsAround(RadixObject context, final VisitorProvider visitorProvider) {
        assert context != null;
        assert visitorProvider != null;

        final RadixObjectCollector collector = new RadixObjectCollector();

        final Definition contextDefinition = context.getDefinition();
        if (contextDefinition != null) {
            collectAroundDefinition(contextDefinition, visitorProvider, collector);
        } else {
            if (context instanceof Layer) {
                Layer.HierarchyWalker.walk((Layer) context, new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(Controller controller, Layer layer) {
                        layer.visit(collector, visitorProvider);
                    }
                });

            } else if (context instanceof Segment) {
                final Segment segment = (Segment) context;
                final Layer root = segment.getLayer();

                Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<Object>() {
                    @Override
                    public void accept(Controller controller, Layer layer) {
                        Segment s = layer.getSegmentByType(segment.getType());
                        if (s != null) {
                            s.visit(collector, visitorProvider);
                        }
                    }
                });

            } else {
                context.visit(collector, visitorProvider);
            }
        }

        return collector.getResult();
    }

    public static void removeDuplicates(List<RadixObject> list) {
        if (list.size() > 1) {
            final Set<RadixObject> set = new HashSet<>();
            final Iterator<RadixObject> iterator = list.iterator();
            while (iterator.hasNext()) {
                final RadixObject radixObject = iterator.next();
                if (set.contains(radixObject)) {
                    iterator.remove();
                } else {
                    set.add(radixObject);
                }
            }
        }
    }

    public static <T> T findContainer(RadixObject object, Class<T> cls) {
        return findContainer(object, cls, false);
    }

    public static <T> T findContainer(RadixObject object, Class<T> cls, boolean exact) {
        if (object != null && cls != null) {
            for (RadixObject container = object; container != null; container = container.getContainer()) {
                if (exact) {
                    if (container.getClass() == cls) {
                        return (T) container;
                    }
                } else if (cls.isInstance(container)) {
                    return (T) container;
                }
            }
        }
        return null;
    }
}
