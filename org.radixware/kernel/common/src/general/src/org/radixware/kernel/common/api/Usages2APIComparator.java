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

package org.radixware.kernel.common.api;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.APIDocument.API;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.AbstractMethodDefinition.ReturnType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.AccessRules;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.adsdef.EnumDefinition;
import org.radixware.schemas.adsdef.EnumItemDefinition;

import org.radixware.schemas.adsdef.UsageDescription;
import org.radixware.schemas.adsdef.UsagesDocument.Usages;
import org.radixware.schemas.commondef.Definition;
import org.radixware.schemas.adsdef.ParameterDeclaration;
import org.radixware.schemas.xscml.TypeArguments;
import org.radixware.schemas.xscml.TypeDeclaration;


public class Usages2APIComparator implements ICancellable {

    public interface Reporter {

        public void message(EEventSeverity severity, String message);
    }

    private class DefaultReporter implements Reporter {

        @Override
        public void message(EEventSeverity severity, String message) {
            System.out.println("[" + severity.getName() + "]: " + message);
        }
    }

    public interface APILookup {

        APIDocument lookup(String layerUri, Id moduleId) throws IOException;
        
        boolean isExpired(String layerUri, String expirationRelease) throws IOException;
    }

    public interface UsagesLookup {

        Usages lookup(String layerUri, Id moduleId) throws IOException;
    }
    protected final APILookup apiLookup;
    private final UsagesLookup usagesLookup;
    private final Reporter reporter;

    public Usages2APIComparator(APILookup apiLookup, UsagesLookup usagesLookup, Reporter reporter) {
        this.apiLookup = apiLookup;
        this.usagesLookup = usagesLookup;
        this.reporter = reporter == null ? new DefaultReporter() : reporter;
    }

    private static boolean isDeclaredInAPI(Id[] ids, APIDocument.API xApi) {
        if (ids == null || ids.length != 1) {
            return false;
        }
        final Id id = ids[0];
        for (Object obj : xApi.getIds()) {
            final Id apiId;
            if (obj instanceof String) {
                apiId = Id.Factory.loadFrom((String) obj);
            } else if (obj instanceof Id) {
                apiId = (Id) obj;
            } else {
                apiId = null;
            }
            if (apiId == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isUsagesCompatible(Usages xUsages, String layerURI, Id moduleId) {
        for (Usages.Layer xLayer : xUsages.getLayerList()) {
            final String usedLayerURI = xLayer.getURI();
            for (Usages.Layer.Module xModule : xLayer.getModuleList()) {
                final Id usedModuleId = xModule.getId();
                for (UsageDescription xUsage : xModule.getUsageList()) {
                    Id[] path = xUsage.getPath().toArray(new Id[xUsage.getPath().size()]);
                    Definition xDef = null;
                    try {
                        xDef = findUsed(usedLayerURI, usedModuleId, path);
                    } catch (IOException ex) {
                        reporter.message(EEventSeverity.ERROR, "Unable to load API of module #" + usedModuleId + " from " + usedLayerURI);
                        return false;
                    }
                    if (xDef == null) {
                        if (xUsage.getDefinitionType() == EDefType.IMAGE) {
                            APIDocument xAPI = null;
                            try {
                                xAPI = apiLookup.lookup(usedLayerURI, usedModuleId);
                            } catch (IOException ex) {
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                            if (xAPI == null || !isDeclaredInAPI(path, xAPI.getAPI())) {
                                reporter.message(EEventSeverity.WARNING, "Warning: used icon " + xUsage.getQName() + " from " + usedLayerURI + "::" + xModule.getName() + " not found");
                            }
                            return true;
                        } else {
                            if (Utils.equals(usedLayerURI, layerURI)) {//same layer. may use non API elements
                                return true;
                            }
                            reporter.message(EEventSeverity.ERROR, "Used definition " + xUsage.getQName() + " from " + usedLayerURI + "::" + xModule.getName() + " referenced from " + layerURI + "::" + moduleId + " not found");
                            return false;
                        }
                    } else {
                        if (xDef.getDefinitionType() == null) {
                            reporter.message(EEventSeverity.WARNING, "Warning: no definition type hint found at " + xUsage.getQName() + " from " + usedLayerURI);
                        } else {
                            if (xDef.getDefinitionType() != xUsage.getDefinitionType()) {
                                reporter.message(EEventSeverity.WARNING, "Used definition is expected to be of type " + xUsage.getDefinitionType().getName() + " but found definition of type " + xDef.getDefinitionType().getName());
                            }
                        }

                        if (xUsage.isSetIsExtension() && xUsage.getIsExtension()) {
                            if (xDef instanceof DescribedAdsDefinition) {
                                if (isFinal(((DescribedAdsDefinition) xDef).getAccessRules())) {
                                    reporter.message(EEventSeverity.ERROR, "Used definition " + xUsage.getQName() + " is expected to be non-final");
                                    return false;
                                }
                            }
                        }
                        switch (xUsage.getDefinitionType()) {
                            case CLASS_METHOD:
                                if (xDef instanceof AbstractMethodDefinition) {
                                    if (!compareClassMethods(xUsage.getMethod(), (AbstractMethodDefinition) xDef, xUsage.getQName(), layerURI, moduleId, usedLayerURI, usedModuleId)) {
                                        return false;
                                    }
                                } else {
                                    reporter.message(EEventSeverity.ERROR, xUsage.getQName() + " is expected to be method definition");
                                    return false;
                                }
                                break;
                            case CLASS_PROPERTY:
                                if (xDef instanceof AbstractPropertyDefinition) {
                                    if (!compareClassProperties(xUsage.getProperty(), (AbstractPropertyDefinition) xDef, xUsage.getQName(), layerURI, moduleId, usedLayerURI, usedModuleId)) {
                                        return false;
                                    }
                                } else {
                                    reporter.message(EEventSeverity.ERROR, xUsage.getQName() + " is expected to be property definition");
                                    return false;
                                }
                                break;
                            case ENUM_ITEM:
                                if (xDef instanceof EnumItemDefinition) {
                                    if (!compareEnumerationItems(xUsage.getEnumItem(), (EnumItemDefinition) xDef, xUsage.getQName(), layerURI, moduleId, usedLayerURI, usedModuleId)) {
                                        return false;
                                    }
                                } else {
                                    reporter.message(EEventSeverity.ERROR, xUsage.getQName() + " is expected to be enumeration item definition");
                                    return false;
                                }
                                break;
                            default:
                                if (xDef instanceof DescribedAdsDefinition) {
                                    if (!compareDefinitions((DescribedAdsDefinition) xDef, xUsage.getQName(), layerURI, moduleId, usedLayerURI, usedModuleId)) {
                                        return false;
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isModuleCompatible(String layerURI, Id moduleId) {
        Usages xUsages;
        boolean mustStop = false;
        try {
            xUsages = usagesLookup.lookup(layerURI, moduleId);
        } catch (IOException ex) {
            mustStop = true;
            return false;
        }
        if (mustStop || xUsages == null) {
            reporter.message(EEventSeverity.ERROR, "Unable to load usages of module #" + moduleId + " from " + layerURI);
            return false;
        }

        return isUsagesCompatible(xUsages, layerURI, moduleId);
    }
    private static final String DIFFERS_SUFFIX = " is differs against expected";

    public Definition findUsed(String layerURI, Id moduleId, Id[] path) throws IOException {
        APIDocument xDoc = apiLookup.lookup(layerURI, moduleId);
        if (xDoc != null) {
            APIDocument.API xApi = xDoc.getAPI();
            return findInApi(xApi, path);
        }
        return null;
    }
    
    private Definition findInApi(APIDocument.API xApi, Id[] path) {
        final XmlCursor defCursor = xApi.newCursor();
        try {
            if (defCursor.toFirstChild()) {
                while (defCursor.toNextSibling()) {
                    XmlCursor cursor = defCursor.getObject().newCursor();
                    try {
                        if (cursor.toFirstChild()) {
                            do {
                                XmlObject obj = cursor.getObject();
                                if (obj instanceof DescribedAdsDefinition) {
                                    DescribedAdsDefinition def = (DescribedAdsDefinition) obj;
                                    if (def.getId() == path[0]) {
                                        if (path.length == 1) {
                                            return def;
                                        } else {
                                            return findInside(def, path, 1);
                                        }
                                    }
                                }
                            } while (cursor.toNextSibling());
                        }
                    } finally {
                        cursor.dispose();
                    }
                }
            }
        } finally {
            defCursor.dispose();
        }
        return null;
    }

    private Definition findInside(XmlObject def, Id[] path, int start) {
        XmlCursor cursor = def.newCursor();
        try {
            if (cursor.toFirstChild()) {
                do {
                    try {
                        XmlObject obj = cursor.getObject();
                        if (obj instanceof DescribedAdsDefinition) {
                            DescribedAdsDefinition xDef = (DescribedAdsDefinition) obj;
                            if (xDef.getId() == path[start]) {
                                if (start == path.length - 1) {
                                    return xDef;
                                } else {
                                    return findInside(xDef, path, start + 1);
                                }
                            }
                        } else {
                            Definition result = findInside(obj, path, start);
                            if (result != null) {
                                return result;
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println(cursor.getDomNode().getNamespaceURI() + "::" + cursor.getDomNode().getLocalName());
                        //  e.printStackTrace();
                    }
                } while (cursor.toNextSibling());
            }
        } finally {
            cursor.dispose();
        }
        return null;
    }

    private void paremeterListDiffers(String contextDescription) {
        reporter.message(EEventSeverity.ERROR, "Parameter list of " + contextDescription + DIFFERS_SUFFIX);
    }

    private void throwsListDiffers(String contextDescription) {
        reporter.message(EEventSeverity.WARNING, "Thrown exception list of " + contextDescription + DIFFERS_SUFFIX);
    }

    private void apiUsageKindDiffers(String contextDescription) {
        reporter.message(EEventSeverity.WARNING, "API usage kind of " + contextDescription + DIFFERS_SUFFIX);
    }
    
    private boolean isStatic(AccessRules accessFlags) {
        if (accessFlags == null) {
            return false;
        }
        if (!accessFlags.isSetIsStatic()) {
            return false;
        }
        return accessFlags.getIsStatic();
    }

    private boolean isFinal(AccessRules accessFlags) {
        if (accessFlags == null) {
            return false;
        }
        if (!accessFlags.isSetIsFinal()) {
            return false;
        }
        return accessFlags.getIsFinal();
    }

    protected boolean compareClassMethods(AbstractMethodDefinition one, AbstractMethodDefinition another, String contextDescription, String contextLayerURI, Id contextModuleId, String usedLayerURI, Id usedModuleId) {
        if (!compareDefinitions(one, another, contextDescription, contextLayerURI, contextModuleId, usedLayerURI, usedModuleId)) {
            return false;
        }
        ReturnType ort = one.getReturnType();
        ReturnType art = another.getReturnType();
        if (!compareTypeDeclarations(ort, art, "return value of " + contextDescription)) {
            return false;
        }


        AbstractMethodDefinition.Parameters opl = one.getParameters();
        AbstractMethodDefinition.Parameters apl = one.getParameters();

        if (opl != null && apl != null) {
            List<ParameterDeclaration> opll = opl.getParameterList();
            List<ParameterDeclaration> apll = apl.getParameterList();
            if (opll.size() != apll.size()) {
                paremeterListDiffers(contextDescription);
                return false;
            }
            for (int i = 0, len = apll.size(); i < len; i++) {
                ParameterDeclaration op = opll.get(i);
                ParameterDeclaration ap = apll.get(i);
                if (!compareTypeDeclarations(op.getType(), ap.getType(), "parameter #" + String.valueOf(i) + " of " + contextDescription)) {
                    return false;
                }
            }
        } else {
            if (opl != null || apl != null) {
                paremeterListDiffers(contextDescription);
                return false;
            }
        }
        AccessRules cacc = one.getAccessRules();
        AccessRules aacc = one.getAccessRules();
        if (isStatic(cacc) != isStatic(aacc)) {
            if (isStatic(cacc)) {
                reporter.message(EEventSeverity.ERROR, contextDescription + " is expected to be static");
            } else {
                reporter.message(EEventSeverity.ERROR, contextDescription + " is not expected to be static");
            }
            return false;
        }
        if (one.isSetIsReflectiveCallable() && one.getIsReflectiveCallable()) {
            if (!another.isSetIsReflectiveCallable() || !another.getIsReflectiveCallable()) {
                reporter.message(EEventSeverity.WARNING, contextDescription + " is expected to be reflective callable");
                return false;
            }
        }

        if (one.getIsConstructor() != another.getIsConstructor()) {
            if (one.getIsConstructor()) {
                reporter.message(EEventSeverity.ERROR, contextDescription + " is expected to be constructor");
            } else {
                reporter.message(EEventSeverity.ERROR, contextDescription + " is not expected to be constructor");
            }
            return false;
        }
        if (one.getNature() != another.getNature()) {
            reporter.message(EEventSeverity.ERROR, "Nature of " + contextDescription + DIFFERS_SUFFIX + " (expected " + one.getNature().getName() + " but found " + another.getNature().getName() + ")");
            return false;
        }
        AbstractMethodDefinition.ThrownExceptions oex = one.getThrownExceptions();
        AbstractMethodDefinition.ThrownExceptions aex = another.getThrownExceptions();

        if (oex != null && aex != null) {
            List<AbstractMethodDefinition.ThrownExceptions.Exception> ox = oex.getExceptionList();
            List<AbstractMethodDefinition.ThrownExceptions.Exception> ax = aex.getExceptionList();
            if (ox.size() != ax.size()) {
                throwsListDiffers(contextDescription);
                return false;
            }
            for (int i = 0, len = ox.size(); i < len; i++) {
                AbstractMethodDefinition.ThrownExceptions.Exception oe = ox.get(i);
                AbstractMethodDefinition.ThrownExceptions.Exception ae = ax.get(i);
                if (!compareTypeDeclarations(oe, ae, " throws list item of " + contextDescription)) {
                    return false;
                }
            }
        } else {
            if (oex != null || aex != null) {
                throwsListDiffers(contextDescription);
                return false;
            }
        }
        return true;
    }

    private boolean compareClassProperties(AbstractPropertyDefinition one, AbstractPropertyDefinition another, String contextDescription, String contextLayerURI, Id contextModuleId, String usedLayerURI, Id usedModuleId) {
        if (!compareDefinitions(one, another, contextDescription, contextLayerURI, contextModuleId, usedLayerURI, usedModuleId)) {
            return false;
        }
        AccessRules cacc = one.getAccessRules();
        AccessRules aacc = one.getAccessRules();
        if (isStatic(cacc) != isStatic(aacc)) {
            if (isStatic(cacc)) {
                reporter.message(EEventSeverity.ERROR, contextDescription + " is expected to be static");
            } else {
                reporter.message(EEventSeverity.ERROR, contextDescription + " is not expected to be static");
            }
            return false;
        }
        if (one.getIsConst() != another.getIsConst()) {
            if (!one.getIsConst()) {
                reporter.message(EEventSeverity.WARNING, contextDescription + " is not expected to be read only");
                return false;
            }
        }
        if (one.getNature() != another.getNature()) {
            reporter.message(EEventSeverity.ERROR, "Nature of " + contextDescription + DIFFERS_SUFFIX + " (expected " + one.getNature().getName() + " but found " + another.getNature().getName() + ")");
            return false;
        }
        //one-usage
        if (one.getIsInvisibleForArte() != another.getIsInvisibleForArte()) {
            if (!one.getIsInvisibleForArte()) {
                reporter.message(EEventSeverity.WARNING, contextDescription + " is expected to be accessible for java code (visible for ARTE)");
            }
            return false;
        }
        if (!one.getIsInvisibleForArte() && !another.getIsInvisibleForArte()) {
            if (!compareTypeDeclarations(one.getType(), another.getType(), contextDescription)) {
                return false;
            }
        }

        if (one.getPresentation() != null && one.getPresentation().getIsPresentable()) {
            if (another.getPresentation() == null) {
                reporter.message(EEventSeverity.WARNING, contextDescription + " is expected to be available for client-server interaction");
                return false;
            } else {
                if (!another.getPresentation().getIsPresentable()) {
                    reporter.message(EEventSeverity.WARNING, contextDescription + " is expected to be available for client-server interaction");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean compareEnumerations(EnumDefinition one, EnumDefinition another, String contextDescription, String contextLayerURI, Id contextModuleId, String usedLayerURI, Id usedModuleId) {
        if (!compareDefinitions(one, another, contextDescription, contextLayerURI, contextModuleId, usedLayerURI, usedModuleId)) {
            return false;
        }
        if (one.getValType() != another.getValType()) {
            reporter.message(EEventSeverity.ERROR, "Item type of " + contextDescription + DIFFERS_SUFFIX + " (expected " + EValType.getForValue(one.getValType()).getName() + " but found " + EValType.getForValue(another.getValType()).getName() + ")");
            return false;
        }
        return true;
    }

    private boolean compareEnumerationItems(EnumItemDefinition one, EnumItemDefinition another, String contextDescription, String contextLayerURI, Id contextModuleId, String usedLayerURI, Id usedModuleId) {
        if (!compareDefinitions(one, another, contextDescription, contextLayerURI, contextModuleId, usedLayerURI, usedModuleId)) {
            return false;
        }
        if (!Utils.equals(one.getValue(), another.getValue())) {
            reporter.message(EEventSeverity.WARNING, "Value of " + contextDescription + DIFFERS_SUFFIX);
            return false;
        }
        return true;
    }

    private boolean compareDefinitions(DescribedAdsDefinition used, String contextDescription, String contextLayerURI, Id contextModuleId, String usedLayerURI, Id usedModuleId) {
        if (used.isSetAccessRules()) {
            if (used.getAccessRules().getIsDeprecated()) {
                try {
                    if (apiLookup.isExpired(usedLayerURI, used.getAccessRules().getExpirationRelease())) {
                        reporter.message(EEventSeverity.ERROR, "API definition " + used.getName() + " expired since " + used.getAccessRules().getExpirationRelease());
                    } else {
                        reporter.message(EEventSeverity.WARNING, "API definition " + used.getName() + " is deprecated");
                    }
                } catch (IOException ex) {
                    reporter.message(EEventSeverity.ERROR, "Error on check definition " + used.getName() + ": " + ex.getMessage());
                }
            }
            if (used.isSetDefinitionType() && used.getDefinitionType() == EDefType.ROLE) {
                return true;
            }
            AccessRules arules = used.getAccessRules();
            if (arules.isSetIsPublished()) {
                boolean isPublished = arules.getIsPublished();
                if (!isPublished) {
                    if (!Utils.equals(contextLayerURI, usedLayerURI)) {
                        reporter.message(EEventSeverity.WARNING, contextDescription + " is not accessible");
                        return false;
                    } else {
                        EAccess acc = EAccess.getForValue(Long.valueOf(arules.getAccess()));
                        if (acc == EAccess.DEFAULT || acc == EAccess.PRIVATE) {
                            reporter.message(EEventSeverity.WARNING, contextDescription + " is not accessible");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean compareDefinitions(DescribedAdsDefinition one, DescribedAdsDefinition another, String contextDescription, String contextLayerURI, Id contextModuleId, String usedLayerURI, Id usedModuleId) {
        if (one.isSetAPIUsageKind() && another.isSetAPIUsageKind()) {
            if (one.getAPIUsageKind() != another.getAPIUsageKind()) {
                apiUsageKindDiffers(contextDescription);
                return false;
            }
        } else {
            if (one.isSetAPIUsageKind() || another.isSetAPIUsageKind()) {
                apiUsageKindDiffers(contextDescription);
                return false;
            }
        }
        return compareDefinitions(another, contextDescription, contextLayerURI, contextModuleId, usedLayerURI, usedModuleId);
    }

    private void typeDeclarationDiffers(String contextDescription) {
        reporter.message(EEventSeverity.ERROR, "Type of " + contextDescription + DIFFERS_SUFFIX);
    }

    private boolean compareTypeDeclarations(TypeDeclaration one, TypeDeclaration anoter, String contextDescription) {
        if (one == null && anoter == null) {
            return true;
        }
        if (one == null) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (anoter == null) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (one.getDimension() != anoter.getDimension()) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (one.getIsArgumentType() != anoter.getIsArgumentType()) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (!Utils.equals(one.getExtStr(), anoter.getExtStr())) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (one.getTypeId() != anoter.getTypeId()) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (one.getPath() != null || anoter.getPath() != null) {
            if (one.getPath() != null && anoter.getPath() != null) {
                List<Id> onePath = new LinkedList<Id>(one.getPath());
                List<Id> anotherPath = new LinkedList<Id>(anoter.getPath());
                if (!Utils.equals(onePath, anotherPath)) {
                    typeDeclarationDiffers(contextDescription);
                    return false;
                }
            } else {
                typeDeclarationDiffers(contextDescription);
                return false;
            }
        }

        TypeArguments oargs = one.getGenericArguments();
        TypeArguments aargs = anoter.getGenericArguments();

        if (oargs == null && aargs == null) {
            return true;
        }
        if (oargs == null) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        if (aargs == null) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        List<TypeArguments.Argument> olist = oargs.getArgumentList();
        List<TypeArguments.Argument> alist = oargs.getArgumentList();

        if (olist.size() != alist.size()) {
            typeDeclarationDiffers(contextDescription);
            return false;
        }
        for (int i = 0, len = olist.size(); i < len; i++) {
            TypeArguments.Argument oa = olist.get(i);
            TypeArguments.Argument aa = alist.get(i);

            if (!Utils.equals(oa.getAlias(), aa.getAlias())) {
                typeDeclarationDiffers(contextDescription);
                return false;
            }
            if (!Utils.equals(oa.getDerivationRule(), aa.getDerivationRule())) {
                typeDeclarationDiffers(contextDescription);
                return false;
            }
            if (!compareTypeDeclarations(oa.getType(), aa.getType(), contextDescription)) {
                typeDeclarationDiffers(contextDescription);
                return false;
            }
        }
        return true;
    }

    protected ClassDefinition findUserFuncClass(API xApi, Id classId) {
        Definition xDef = findInApi(xApi, new Id[]{classId});
        if (xDef instanceof ClassDefinition) {
            return (ClassDefinition) xDef;
        } else {
            return null;
        }
    }
    private boolean cancel = false;

    @Override
    public boolean cancel() {
        cancel = true;
        return true;
    }

    @Override
    public boolean wasCancelled() {
        return cancel;
    }
}
