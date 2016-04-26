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

package org.radixware.kernel.common.client.env;

import org.radixware.kernel.common.client.IClientApplication;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.RadAlgoDef;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadDomainPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadFormDef;
import org.radixware.kernel.common.client.meta.RadGroupHandlerDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.meta.RadReportPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.environment.IRadixDefManager;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.schemas.msdl.MessageElementDocument;


public abstract class DefManager implements IRadixDefManager {

    private final AdsVersion version;
    private final static HashMap<String, Icon> images = new HashMap<>(256);
    private final IClientApplication env;

    public DefManager(final IClientApplication env) {
        this.env = env;
        version = initializeVersion();
    }

    private AdsVersion initializeVersion() {
        return createVersion(env);
    }

    protected abstract AdsVersion createVersion(IClientApplication env);   

    private Release release() {
        return version.release(env.isInGuiThread());
    }

    @SuppressWarnings("unchecked")
    public Class<IEditorPageView> getCustomEditorPageClass(final Id dialogId) {
        try {
            return (Class<IEditorPageView>) release().getClassLoader().loadExecutableClassById(dialogId);
        } catch (ClassNotFoundException ex) {
            env.getTracer().put(ex);
            throw new DefinitionNotFoundError(dialogId);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<IView> getCustomViewClass(final Id customDialogId) {
        try {
            return (Class<IView>) release().getClassLoader().loadExecutableClassById(customDialogId);
        } catch (ClassNotFoundException ex) {
            env.getTracer().put(ex);
            throw new DefinitionNotFoundError(customDialogId);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<IPropEditorDialog> getPropertyEditorDialogClass(final Id dialogId) {
        try {
            return (Class<IPropEditorDialog>) release().getClassLoader().loadExecutableClassById(dialogId);
        } catch (ClassNotFoundException ex) {
            env.getTracer().put(ex);
            throw new DefinitionNotFoundError(dialogId);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<Model> getDefinitionModelClass(final Id id) {
        try {
            return (Class<Model>) release().getClassLoader().loadExecutableClassById(id);
        } catch (ClassNotFoundException ex) {
            env.getTracer().put(ex);
            throw new DefinitionNotFoundError(id);
        }
    }

    public Class<?> getDynamicClassById(final Id id) {
        try {
            return release().getClassLoader().loadExecutableClassById(id);
        } catch (ClassNotFoundException ex) {
            env.getTracer().put(ex);
            throw new DefinitionNotFoundError(id);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<XmlObject> getXmlBeansClassByNodeName(final String namespace, final String name) throws ClassNotFoundException {
        final String schemeFileName = getRepository().getXmlSchemeFileName(namespace);

        final int last_separator_index = schemeFileName.lastIndexOf("/");
        final String schemeFilePath = schemeFileName.substring(0, last_separator_index + 1);
        final String className = schemeFilePath.replace("/", ".") + name;
        try {
            return (Class<XmlObject>) getClassLoader().loadClass(className + "Document");
        } catch (ClassNotFoundException ex) {
            //try to load one more
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return (Class<XmlObject>) getClassLoader().loadClass(className);
    }

    public RadParagraphDef getParagraphDef(final Id id) {
        return (RadParagraphDef) getDefinition(id);
    }

    public RadClassPresentationDef getClassPresentationDef(final Id id) {
        final Id actualId;
        if (id.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            actualId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        } else {//it may be also id of model class
            actualId = id;
        }
        return (RadClassPresentationDef) getDefinition(actualId);
    }

    public RadEditorPresentationDef getEditorPresentationDef(final Id presentationId) {
        return (RadEditorPresentationDef) getDefinition(presentationId);
    }

    public RadSelectorPresentationDef getSelectorPresentationDef(final Id presentationId) {
        return (RadSelectorPresentationDef) getDefinition(presentationId);
    }

    public RadCommandDef getContextlessCommandDef(final Id id) {
        return (RadCommandDef) getDefinition(id);
    }

    public RadFormDef getFormDef(final Id id) {
        return (RadFormDef) getDefinition(id);
    }

    public RadReportPresentationDef getReportPresentationDef(final Id id) {
        return (RadReportPresentationDef) getDefinition(id);
    }

    public RadEnumPresentationDef getEnumPresentationDef(final Id id) {
        return release().getEnumDef(id);
    }

    public RadGroupHandlerDef getGroupHandlerDef(final Id id) {
        return (RadGroupHandlerDef) getDefinition(id);
    }

    public RadAlgoDef getAlgoDef(final Id id) {
        return (RadAlgoDef) getDefinition(id);
    }

    public Definition getDefinition(final Id definitionId) {
        return release().getDefinition(definitionId);
    }

    public RadMlStringBundleDef getMlStringBundleDef(final Id id) {
        final Id actualId;
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
            actualId = id;
        } else {
            actualId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + id.toString());
        }
        return release().getMlStringBundle(actualId);
    }

    @Override
    public IMlStringBundle getStringBundleById(final Id id) {
        return getMlStringBundleDef(id);
    }

    public String getMlStringValue(final Id definitionId, final Id stringId) {
        return getMlStringBundleDef(definitionId).get(stringId);
    }

    public String getEventTitleByCode(final String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException();
        }
        int delimPos = code.indexOf('-');
        if (delimPos < 0) {
            throw new WrongFormatError("Wrong format of event code: \"" + code + "\"", null);
        }
        final Id definitionId = Id.Factory.loadFrom(code.substring(0, delimPos));
        final Id stringId = Id.Factory.loadFrom(code.substring(delimPos + 1));
        return getMlStringValue(definitionId, stringId);
    }

    public RadixClassLoader getClassLoader() {
        return release() != null ? release().getClassLoader().getRadixClassLoader() : null;
    }

    public Icon getImage(final Id id) {
        try {
            return env.getImageManager().loadIcon(getRepository().getImageFilePath(id));
        } catch (DefinitionError err) {
            env.getTracer().error(err);
            return null;
        }
    }

    public String findCachedImageId(final Icon image) {
        return findCachedImageIdByCacheKey(image.cacheKey());
    }

    public String findCachedImageIdByCacheKey(final long cacheKey) {
        for (Map.Entry<String, Icon> entry : images.entrySet()) {
            if (entry.getValue().cacheKey() == cacheKey) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Object setUserData(final String key, final Object data) {
        return release().userData.put(key, data);
    }

    public Object getUserData(final String key) {
        return release().userData.get(key);
    }

    public final boolean isOldVersionModeEnabled() {
        return version.isNewVersionAvailable() || !version.isActualized();
    }

    public AdsVersion getAdsVersion() {
        return version;
    }

    public ReleaseRepository getRepository() {
        return release().getClassLoader().getRepository();
    }

    public RadDomainPresentationDef getDomainPresentationDef(final Id id) {
        return release().getDomainPresentationDef(id);
    }

    @Override
    public boolean isDefInDomain(final Id defId, final Id domainId) {
        return getRepository().isDefinitionInDomain(defId, domainId);
    }

    @Override
    public RootMsdlScheme getMsdlScheme(final Id id) {
        MessageElementDocument xSchema;
        try {
            xSchema = MessageElementDocument.Factory.parse(getRepository().getInputStreamForMsdlScheme(id));
        } catch (XmlException | IOException ex) {
            throw new DefinitionError("Can`t load msdl scheme #" + id.toString() + ": " + ex.getMessage(), ex);
        }
        if (xSchema == null || xSchema.getMessageElement() == null) {
            throw new DefinitionError("Can`t load msdl scheme #" + id.toString() + ": MessageElement is mandatory");
        }
        RootMsdlScheme schema = new RootMsdlScheme(xSchema.getMessageElement());
        return schema;
    }

    public Collection<RadParagraphDef> getExplorerRoots() {
        return version.release(false).getExplorerRoots();
    }
}
