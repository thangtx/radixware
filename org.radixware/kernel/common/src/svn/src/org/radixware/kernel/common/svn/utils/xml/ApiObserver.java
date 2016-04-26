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
package org.radixware.kernel.common.svn.utils.xml;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.CHAR;
import static org.radixware.kernel.common.enums.EValType.INT;
import static org.radixware.kernel.common.enums.EValType.STR;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.adsdef.EnumDefinition;
import org.radixware.schemas.adsdef.EnumItemDefinition;
import org.radixware.schemas.adsdef.MethodDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;
import org.radixware.schemas.product.ApiChanges;
import org.radixware.schemas.product.ApiChangesDocument;
import org.radixware.schemas.product.LayerDocument;
import org.radixware.schemas.product.ModuleDocument;
import org.radixware.schemas.xscml.TypeArguments;
import org.radixware.schemas.xscml.TypeDeclaration;

public class ApiObserver extends AbstractSvnObserver {

    private String newBranch;
    private String oldBranch;

    private class Def {

        public final Id[] path;
        public final String name;

        public Def(Id[] id, String name) {
            this.path = id;
            this.name = name;
        }

        Id[] path() {
            return path;
        }

        Id id() {
            return path[path.length - 1];
        }
    }

    private abstract class ModuleDef extends Def {

        final Module ownerModule;
        final ERuntimeEnvironmentType env;

        public ModuleDef(Module ownerModule, Id id, String name, ERuntimeEnvironmentType env) {
            super(new Id[]{id}, name);
            this.ownerModule = ownerModule;
            this.env = env;
        }

        protected abstract String defintitionType();

        public void compareTo(ModuleDef another) {
        }

        String qname() {
            return ownerModule.parentLayer.name + "::" + ownerModule.name + "::" + name;
        }
    }

    private class Enumeration extends ModuleDef {

        private class Item {

            Id id;
            String name;

            public Item(EnumItemDefinition xItem) {
                id = xItem.getId();
                name = xItem.getName();
            }

            public Id[] path() {
                return new Id[]{Enumeration.this.id(), id};
            }
        }
        private EValType valueType;
        private Map<Id, Item> items;

        public Enumeration(Module ownerModule, EnumDefinition xEnum) {
            super(ownerModule, xEnum.getId(), xEnum.getName(), ERuntimeEnvironmentType.COMMON);
            this.valueType = EValType.getForValue(xEnum.getValType());
            items = new HashMap<>();
            if (xEnum.getItems() != null) {
                for (EnumItemDefinition xItem : xEnum.getItems().getItemList()) {
                    items.put(xItem.getId(), new Item(xItem));
                }
            }
        }

        @Override
        protected String defintitionType() {
            return "Enumeration";
        }

        @Override
        public void compareTo(ModuleDef another) {
            if (!(another instanceof Enumeration)) {
                elementRemoved(ownerModule.parentLayer, env, "Enumeration", qname(), path());
            } else {
                Enumeration newEnum = (Enumeration) another;
                for (Item item : items.values()) {
                    Item newItem = newEnum.items.get(item.id);
                    if (newItem == null) {
                        elementRemoved(ownerModule.parentLayer, env, "Enumeration item", qname() + ":" + item.name, path());
                    }
                }
            }
        }
    }

    private boolean compareTypes(TypeDeclaration oldType, TypeDeclaration newType) {
        if (oldType == null && newType != null) {
            return false;
        }
        if (oldType != null && newType == null) {
            return false;
        }
        if (oldType == null || newType == null) {
            return true;
        }
        if (oldType.getTypeId() != newType.getTypeId()) {
            return false;
        }

        if (oldType.getPath() != null && newType.getPath() == null) {
            return false;
        }
        if (oldType.getPath() == null && newType.getPath() != null) {
            return false;
        }
        if (oldType.getPath() != null) {
            if (oldType.getPath().size() != newType.getPath().size()) {
                return false;
            }
            for (int i = 0, s = oldType.getPath().size(); i < s; i++) {
                if (oldType.getPath().get(i) != newType.getPath().get(i)) {
                    return false;
                }
            }
        }
        if (!Utils.equals(oldType.getExtStr(), newType.getExtStr())) {
            return false;
        }
        if (oldType.getIsArgumentType() != newType.getIsArgumentType()) {
            return false;
        }
        if (oldType.getGenericArguments() != null && newType.getGenericArguments() == null) {
            return false;
        }
        if (oldType.getGenericArguments() == null && newType.getGenericArguments() != null) {
            return false;
        }
        if (oldType.getGenericArguments() != null) {
            if (oldType.getGenericArguments().getArgumentList().size() != newType.getGenericArguments().getArgumentList().size()) {
                return false;
            }
            for (int i = 0, s = oldType.getGenericArguments().getArgumentList().size(); i < s; i++) {
                TypeArguments.Argument oldArg = oldType.getGenericArguments().getArgumentList().get(i);
                TypeArguments.Argument newArg = oldType.getGenericArguments().getArgumentList().get(i);
                if (!compareTypes(oldArg.getType(), newArg.getType())) {
                    return false;
                }
                if (oldArg.getDerivationRule() != newArg.getDerivationRule()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String findDefinitionInLayer(Layer context, List<Id> path) {
        ModuleDef definition = null;
        Module defModule = null;

        for (Module module : context.modules.values()) {
            definition = module.classes.get(path.get(0));
            if (definition != null) {
                defModule = module;
                break;
            }
        }
        if (definition != null) {
            return context.name + "::" + defModule.name + "::" + definition.name;
        } else {
            List<Layer> baseLayers = context.getBaseLayers();
            for (Layer l : baseLayers) {
                String result = findDefinitionInLayer(l, path);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    private String findDefinition(Layer context, List<Id> path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        String definition = findDefinitionInLayer(context, path);
        if (definition != null) {
            return definition;
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (Id id : path) {
            if (first) {
                first = false;
            } else {
                sb.append(":");
            }
            sb.append(id.toString());
        }
        return sb.toString();
    }

    private String typeArgumentsStr(Layer context, TypeDeclaration type) {
        if (type.getGenericArguments() == null) {
            return null;
        }
        if (type.getGenericArguments().getArgumentList().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (TypeArguments.Argument arg : type.getGenericArguments().getArgumentList()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            if (arg.getAlias() != null) {
                sb.append(arg.getAlias());
            }
            if (arg.getDerivationRule() == TypeArguments.Argument.DerivationRule.EXTENDS) {
                sb.append(" extends ");
            } else if (arg.getDerivationRule() == TypeArguments.Argument.DerivationRule.EXTENDS) {
                sb.append(" super ");
            }
            if (arg.getType() != null) {
                sb.append(typeName(context, arg.getType()));
            }
        }
        return sb.toString();
    }

    private String typeName(Layer context, TypeDeclaration type) {
        String definitionName;
        if (type == null || type.getTypeId() == null) {
            return "void";
        }
        switch (type.getTypeId()) {
            case JAVA_CLASS:
            case JAVA_TYPE:
                return type.getExtStr();
            case USER_CLASS:
            case PARENT_REF:
            case INT:
            case STR:
            case CHAR:
            case ARR_STR:
            case ARR_INT:
            case ARR_CHAR:
                definitionName = findDefinition(context, type.getPath());
                break;
            default:
                return type.getTypeId().getName();
        }

        String result;
        if (definitionName != null) {
            if (type.getExtStr() != null) {
                result = definitionName + ":" + type.getExtStr();
            } else {
                result = definitionName;
            }
        } else {
            result = type.getTypeId().getName();
        }
        String args = typeArgumentsStr(context, type);
        if (args != null) {
            result = result + "<" + args + ">";
        }
        return result;

    }

    private ERuntimeEnvironmentType getClassEnvironment(ClassDefinition xClass) {
        if (xClass.getEnvironment() != null) {
            return xClass.getEnvironment();
        }
        EClassType ct = xClass.getType();
        if (ct == null) {
            return null;
        }
        switch (ct) {
            case ALGORITHM:
            case APPLICATION:
            case ENTITY:
            case ENTITY_GROUP:
            case FORM_HANDLER:
            case SQL_CURSOR:
            case SQL_PROCEDURE:
            case SQL_STATEMENT:
                return ERuntimeEnvironmentType.SERVER;
            default:
                return null;
        }
    }

    private class Clazz extends ModuleDef {

        private abstract class ClassMember {

            boolean isStatic;
            boolean isAbstract;
            boolean isFinal;
            Id id;
            String name;
            ERuntimeEnvironmentType env;
            TypeDeclaration resultType;

            public ClassMember(DescribedAdsDefinition xDef) {
                if (xDef.getAccessRules() != null) {
                    isStatic = xDef.getAccessRules().getIsStatic();
                    isAbstract = xDef.getAccessRules().getIsAbstract();
                    isFinal = xDef.getAccessRules().getIsFinal();
                }
                id = xDef.getId();
                name = xDef.getName();
            }

            Id[] path() {
                return new Id[]{Clazz.this.id(), id};
            }

            protected boolean mustHaveType() {
                return true;
            }

            protected boolean compareToImpl(ClassMember another) {
                return true;
            }

            public void compareTo(ClassMember another) {
                boolean profileChanged = false;
                if (!compareTypes(resultType, another.resultType)) {
                    profileChanged = true;
                } else {
                    if (!compareToImpl(another)) {
                        profileChanged = true;
                    }
                    if (!profileChanged) {
                        if (isStatic != another.isStatic || isAbstract != another.isAbstract || (!isFinal && another.isFinal)) {
                            profileChanged = true;
                        }
                    }
                    if (profileChanged) {
                        elementModified(Clazz.this.ownerModule.parentLayer, env, getTypeTitle(), new String[]{profileString(), another.profileString()}, Clazz.this.qname() + "." + this.name, this.path());
                    }
                }
            }

            public abstract String profileString();

            protected abstract String getTypeTitle();

            String prefixString() {
                StringBuilder sb = new StringBuilder();
                if (isStatic) {
                    sb.append("static ");
                }
                if (isFinal) {
                    sb.append("final ");
                }
                if (isAbstract) {
                    sb.append("abstract ");
                }
                if (mustHaveType()) {
                    if (resultType != null) {
                        sb.append(typeName(Clazz.this.ownerModule.parentLayer, resultType)).append(" ");
                    } else {
                        sb.append("void ");
                    }
                }
                return sb.toString();
            }
        }

        private class Property extends ClassMember {

            Property(PropertyDefinition xProperty) {
                super(xProperty);
                env = xProperty.getEnvironment() == null ? Clazz.this.env : xProperty.getEnvironment();
                resultType = xProperty.getType();
            }

            @Override
            public String profileString() {
                return prefixString() + " " + name;
            }

            @Override
            protected String getTypeTitle() {
                return "Property";
            }
        }

        private class Method extends ClassMember {

            TypeDeclaration[] profile;
            TypeDeclaration[] thrownExceptions;
            boolean isConstructor;

            public Method(MethodDefinition xMethod) {
                super(xMethod);
                isConstructor = xMethod.getIsConstructor();
                resultType = xMethod.getReturnType();
                env = xMethod.getEnvironment() == null ? Clazz.this.env : xMethod.getEnvironment();
                int paramCount = xMethod.getParameters() == null || xMethod.getParameters().getParameterList() == null ? 0 : xMethod.getParameters().getParameterList().size();
                profile = new TypeDeclaration[paramCount];
                for (int i = 0; i < paramCount; i++) {
                    profile[i] = xMethod.getParameters().getParameterList().get(i).getType();
                }
                int exceptionCount = xMethod.getThrownExceptions() == null || xMethod.getThrownExceptions().getExceptionList() == null ? 0 : xMethod.getThrownExceptions().getExceptionList().size();
                thrownExceptions = new TypeDeclaration[exceptionCount];
                for (int i = 0; i < exceptionCount; i++) {
                    thrownExceptions[i] = xMethod.getThrownExceptions().getExceptionList().get(i);
                }
                if (xMethod.getAccessRules() != null) {
                    isStatic = xMethod.getAccessRules().getIsStatic();
                    isAbstract = xMethod.getAccessRules().getIsAbstract();
                    isFinal = xMethod.getAccessRules().getIsFinal();
                }
            }

            @Override
            protected boolean mustHaveType() {
                return !isConstructor;
            }

            @Override
            protected String getTypeTitle() {
                return isConstructor ? "Constructor" : "Method";
            }

            @Override
            public boolean compareToImpl(ClassMember anotherClassMember) {
                Method another = (Method) anotherClassMember;
                boolean profileChanged = false;

                if (profile.length == another.profile.length) {
                    for (int i = 0; i < profile.length; i++) {
                        if (!compareTypes(profile[i], another.profile[i])) {
                            profileChanged = true;
                            break;
                        }
                    }
                } else {
                    profileChanged = true;
                }
                if (!profileChanged) {
                    if (thrownExceptions.length == another.thrownExceptions.length) {
                        for (int i = 0; i < thrownExceptions.length; i++) {
                            if (!compareTypes(thrownExceptions[i], another.thrownExceptions[i])) {
                                profileChanged = true;
                                break;
                            }
                        }
                    } else {
                        profileChanged = true;
                    }
                }
                return !profileChanged;
            }

            @Override
            public String profileString() {
                StringBuilder sb = new StringBuilder();
                if (isStatic) {
                    sb.append("static ");
                }
                if (isFinal) {
                    sb.append("final ");
                }
                if (isAbstract) {
                    sb.append("abstract ");
                }
                if (!isConstructor) {
                    if (resultType != null) {
                        sb.append(typeName(Clazz.this.ownerModule.parentLayer, resultType)).append(" ");
                    } else {
                        sb.append("void ");
                    }
                }
                sb.append(name).append(" (");
                for (int i = 0; i < profile.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(typeName(Clazz.this.ownerModule.parentLayer, profile[i]));
                }
                sb.append(") ");
                if (thrownExceptions.length > 0) {
                    sb.append("throws ");
                    for (int i = 0; i < thrownExceptions.length; i++) {
                        if (i > 0) {
                            sb.append(", ");
                        }
                        sb.append(typeName(Clazz.this.ownerModule.parentLayer, thrownExceptions[i]));
                    }
                }
                return sb.toString();
            }

            String paramsString() {
                StringBuilder sb = new StringBuilder();
                sb.append(" (");
                for (int i = 0; i < profile.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(typeName(Clazz.this.ownerModule.parentLayer, profile[i]));
                }
                sb.append(") ");
                return sb.toString();
            }
        }
        private Map<Id, Method> methods = new HashMap<>();
        private Map<Id, Property> properties = new HashMap<>();

        public Clazz(Module ownerModule, ClassDefinition xClass) {
            super(ownerModule, xClass.getId(), xClass.getName(), getClassEnvironment(xClass));
            for (MethodDefinition xMethod : xClass.getMethods().getMethodList()) {
                methods.put(xMethod.getId(), new Method(xMethod));
            }
            for (PropertyDefinition xProperty : xClass.getProperties().getPropertyList()) {
                properties.put(xProperty.getId(), new Property(xProperty));
            }
        }

        @Override
        protected String defintitionType() {
            return "Class";
        }

        @Override
        public void compareTo(ModuleDef another) {
            if (!(another instanceof Clazz)) {
            } else {
                Clazz newClass = (Clazz) another;
                for (Method m : methods.values()) {
                    Method newMethod = newClass.methods.get(m.id);
                    if (newMethod == null) {
                        elementRemoved(ownerModule.parentLayer, m.env, m.getTypeTitle(), m.prefixString() + qname() + "." + m.name + m.paramsString(), m.path());
                    } else {
                        m.compareTo(newMethod);
                    }
                }
                for (Property p : properties.values()) {
                    Property newProperty = newClass.properties.get(p.id);
                    if (newProperty == null) {
                        elementRemoved(ownerModule.parentLayer, p.env, p.getTypeTitle(), p.prefixString() + qname() + "." + p.name, p.path());
                    } else {
                        p.compareTo(newProperty);
                    }
                }
            }
        }
    }

    private class Module extends Def {

        private Map<Id, ModuleDef> classes = new HashMap<>();
        private final Layer parentLayer;

        public Module(Layer parentLayer, Id id, String name) {
            super(new Id[]{id}, name);
            this.parentLayer = parentLayer;
        }

        public ModuleDef findClazz(Id id) {
            return classes.get(id);
        }

        public void acceptAPIXml(APIDocument.API api) {
            for (AdsDefinitionElementType xDef : api.getDefinitionList()) {
                ModuleDef def = null;
                if (xDef.isSetAdsClassDefinition()) {
                    def = new Clazz(this, xDef.getAdsClassDefinition());
                } else if (xDef.isSetAdsEnumDefinition()) {
                    def = new Enumeration(this, xDef.getAdsEnumDefinition());
                }
                if (def != null) {
                    classes.put(def.id(), def);
                }
            }
        }
    }

    private class Layer {

        private final String uri;
        private Map<Id, Module> modules = new HashMap<>();
        private final Map<String, Layer> layersMap;
        private final List<String> baseLayers;
        private List<Layer> prevLayers;
        String name;

        public Layer(Map<String, Layer> layersMap, org.radixware.schemas.product.Layer xLayer) {
            this.uri = xLayer.getUri();
            this.layersMap = layersMap;
            this.baseLayers = xLayer.getBaseLayerURIs();
            this.name = xLayer.getName();
        }

        public List<Layer> getBaseLayers() {
            if (prevLayers == null) {
                prevLayers = new LinkedList<>();
                if (baseLayers != null) {
                    for (String base : baseLayers) {
                        Layer l = layersMap.get(base);
                        if (l != null) {
                            prevLayers.add(l);
                        }
                    }
                }
            }
            return prevLayers;
        }

        public Module addModule(Id id, String name) {
            Module module = new Module(this, id, name);
            modules.put(id, module);
            return module;
        }

        public Module findModule(Id id) {
            return modules.get(id);
        }
    }

    private class ChangeSet {

        String layerName;
        Map<ERuntimeEnvironmentType, ChangeList> lists = new HashMap<>();

        public ChangeSet(String layerName) {
            this.layerName = layerName;
        }

        public ChangeList getForEnv(ERuntimeEnvironmentType env) {
            ChangeList list = lists.get(env);
            if (list == null) {
                list = new ChangeList(layerName);
                lists.put(env, list);
            }
            return list;
        }
    }

    private class Change {

        private String description;
        private Id[] path;
        private String uri;

        public Change(String description, Id[] path) {
            this.description = description;
            this.path = path;
        }

        public Change(String description, String uri) {
            this.description = description;
            this.uri = uri;
        }
    }

    private class ChangeList {

        String layerName;

        public ChangeList(String layerName) {
            this.layerName = layerName;
        }
        List<Change> removals = new LinkedList<>();
        List<Change> changes = new LinkedList<>();
    }
    private Map<String, ChangeSet> changeListByLayers = new HashMap<>();
    private ChangeList removedLayers = new ChangeList(null);

    public ApiObserver(SVNRepositoryAdapter repository, long revision, String oldBranch, String newBranch) {
        super(repository, revision);
        this.newBranch = newBranch;
        this.oldBranch = oldBranch;
    }

    public ApiObserver process() throws RadixSvnException, UnsupportedEncodingException {
        final Map<String, Layer> newLayers = new HashMap<>();
        final Map<String, Layer> oldLayers = new HashMap<>();
        initializeLayers(newBranch, newLayers);
        initializeLayers(oldBranch, oldLayers);

        for (Layer oldLayer : oldLayers.values()) {
            Layer newLayer = newLayers.get(oldLayer.uri);
            if (newLayer == null) {
                elementRemoved(null, null, "Layer", oldLayer.uri, null);
            } else {
                compareLayers(oldLayer, newLayer);
            }
        }
        return this;
    }

    private void initializeLayers(String branch, Map<String, Layer> resultStore) throws RadixSvnException, UnsupportedEncodingException {
        List<String> layers = listLayers(branch);

        for (String layer : layers) {
            try {
                String layerXmlFilePath = SvnPath.append(SvnPath.append(branch, layer), "layer.xml");
                String content = SVN.getFileAsStr(repository, layerXmlFilePath, revision);

                LayerDocument xLayer = LayerDocument.Factory.parse(content);
                final Layer layerObj = new Layer(resultStore, xLayer.getLayer());
                resultStore.put(xLayer.getLayer().getUri(), layerObj);

                List<String> modules = listAdsModules(SvnPath.append(branch, layer));
                for (String module : modules) {
                    String moduleXmlFilePath = SvnPath.append(module, "module.xml");
                    content = SVN.getFileAsStr(repository, moduleXmlFilePath, revision);
                    if (content != null) {
                        try {
                            ModuleDocument xDoc = ModuleDocument.Factory.parse(content);
                            Module moduleObj = layerObj.addModule(Id.Factory.loadFrom(xDoc.getModule().getId()), xDoc.getModule().getName());
                            String apiXmlFilePath = SvnPath.append(module, "api.xml");
                            content = SVN.getFileAsStr(repository, apiXmlFilePath, revision);
                            if (content != null) {
                                APIDocument xApi = APIDocument.Factory.parse(content);
                                if (xApi.getAPI() != null) {
                                    moduleObj.acceptAPIXml(xApi.getAPI());
                                }
                            }

                        } catch (XmlException e) {
                            error(ExceptionTextFormatter.exceptionStackToString(e));
                        }
                    }
                }
            } catch (XmlException ex) {
                error(ExceptionTextFormatter.exceptionStackToString(ex));
            }
        }
    }

    private void compareLayers(Layer oldLayer, Layer newLayer) {
        for (Module oldModule : oldLayer.modules.values()) {
            Module newModule = newLayer.findModule(oldModule.id());
            if (newModule == null) {
                elementRemoved(oldLayer, null, "Module", oldLayer.name + "::" + oldModule.name, oldModule.path);
            } else {
                compareModules(oldModule, newModule);
            }
        }
    }

    private void compareModules(Module oldModule, Module newModule) {
        for (ModuleDef def : oldModule.classes.values()) {
            ModuleDef newDef = newModule.findClazz(def.id());
            if (newDef == null) {
                elementRemoved(oldModule.parentLayer, def.env, def.defintitionType(), oldModule.parentLayer.name + "::" + oldModule.name + "::" + def.name, def.path);
            } else {
                def.compareTo(newDef);
            }
        }
    }

    protected void error(String message) {
        System.err.println(message);
    }
    private static final ERuntimeEnvironmentType[] ORDER = new ERuntimeEnvironmentType[]{ERuntimeEnvironmentType.COMMON, ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.COMMON_CLIENT, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB};

    public void textualReport() {
        if (!removedLayers.removals.isEmpty()) {
            println("", "Branch-level changes:");
            for (Change rl : removedLayers.removals) {
                println("  ", "[-] " + rl.description);
            }
        }
        if (!changeListByLayers.isEmpty()) {
            List<String> urls = new ArrayList<>(changeListByLayers.keySet());
            Collections.sort(urls);
            for (String url : urls) {
                ChangeSet set = changeListByLayers.get(url);
                println("", "Layer '" + set.layerName + "' changes:");

                for (ERuntimeEnvironmentType env : ORDER) {
                    ChangeList list = set.lists.get(env);
                    if (list == null) {
                        continue;
                    }
                    println(" ", env.getName() + " API changes:");
                    for (Change msg : list.removals) {
                        println("   ", "[-] " + msg.description);
                    }
                    for (Change msg : list.changes) {
                        println("   ", "[*] " + msg.description);
                    }
                }
            }
        }
    }

    public ApiChangesDocument xmlReport() {
        ApiChangesDocument xDoc = ApiChangesDocument.Factory.newInstance();
        ApiChanges xDef = xDoc.addNewApiChanges();
        if (!removedLayers.removals.isEmpty()) {
            for (Change rl : removedLayers.removals) {
                ApiChanges.Change xChange = xDef.addNewChange();
                xChange.setType(ApiChanges.Change.Type.DELETED);
                xChange.setURI(rl.uri);
            }
        }
        if (!changeListByLayers.isEmpty()) {
            List<String> urls = new ArrayList<>(changeListByLayers.keySet());
            Collections.sort(urls);
            for (String url : urls) {
                ChangeSet set = changeListByLayers.get(url);

                for (ERuntimeEnvironmentType env : ORDER) {
                    ChangeList list = set.lists.get(env);
                    if (list == null) {
                        continue;
                    }
                    for (Change msg : list.removals) {
                        ApiChanges.Change xChange = xDef.addNewChange();
                        xChange.setPath(Arrays.asList(msg.path));
                        xChange.setType(ApiChanges.Change.Type.DELETED);
                    }
                    for (Change msg : list.changes) {
                        ApiChanges.Change xChange = xDef.addNewChange();
                        xChange.setPath(Arrays.asList(msg.path));
                        xChange.setType(ApiChanges.Change.Type.MODIFIED);
                    }
                }
            }
        }

        return xDoc;
    }

    private void println(String prefix, String message) {
        String[] strings = message.split("\n");
        for (int i = 0; i < strings.length; i++) {
            println(prefix + strings[i]);
        }
    }

    protected void println(String message) {
        System.out.println(message);
    }

    private void elementRemoved(Layer layerContext, ERuntimeEnvironmentType env, String elementTypeDescription, String elementDescripion, Id[] elementPath) {
        if (layerContext == null) {
            removedLayers.removals.add(new Change(elementTypeDescription + " " + elementDescripion, elementDescripion));
            return;
        }

        ChangeSet set = changeListByLayers.get(layerContext.uri);
        if (set == null) {
            set = new ChangeSet(layerContext.name);
            changeListByLayers.put(layerContext.uri, set);
        }
        ChangeList list = set.getForEnv(env);
        list.removals.add(new Change(elementTypeDescription + " " + elementDescripion, elementPath));
    }

    private void elementModified(Layer layerContext, ERuntimeEnvironmentType env, String elementTypeDescription, String[] changeDescription, String elementDescripion, Id[] elementPath) {
        ChangeSet set = changeListByLayers.get(layerContext.uri);
        if (set == null) {
            set = new ChangeSet(layerContext.name);
            changeListByLayers.put(layerContext.uri, set);
        }
        ChangeList list = set.getForEnv(env);
        list.changes.add(new Change(elementTypeDescription + " " + elementDescripion + ":\n        " + changeDescription[0] + "\n     -> " + changeDescription[1], elementPath));
    }
}
