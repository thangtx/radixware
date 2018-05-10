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
package org.radixware.kernel.designer.environment.actions;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.radixdoc.xmlexport.IExportableXmlSchema;
import org.radixware.kernel.radixdoc.xmlexport.dialogs.XmlSchemasExportDialog;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.radixdoc.xmlexport.AdsXmlSchemeExportableWrapper;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.kernel.radixdoc.html.LocalFileProvider;
import org.radixware.kernel.designer.ads.common.radixdoc.DesignerRadixdocGenerationContext;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.xmlexport.XmlSchemaExporter;
import org.radixware.kernel.radixdoc.xmlexport.XmlSchemasExportTask;

public class ExportXmlSchemasAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        List<IExportableXmlSchema> schemas = new ArrayList<>();
        final Set<EIsoLanguage> languages = new HashSet<>();
        final Map<String, FileProvider.LayerEntry> layerEntries = new HashMap<>();

        final List<Branch> branches = new ArrayList<>(RadixFileUtil.getOpenedBranches());
        if (branches == null || branches.isEmpty()) {
            return;
        }

        ChooseDefinitionCfg tmpCfg = ChooseDefinitionCfg.Factory.newInstance(branches.get(0), new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsXmlSchemeDef;
            }
        });

        for (Definition schema : tmpCfg.collectAllowedDefinitions()) {
            schemas.add(new AdsXmlSchemeExportableWrapper((AdsXmlSchemeDef) schema));
        }

        List<Layer> localizingLayers = new ArrayList<>();
        for (Layer layer : branches.get(0).getLayers()) {
            languages.addAll(layer.getLanguages());
            List<FileProvider.ModuleEntry> modules = new ArrayList<>();
            for (Module module : layer.getAds().getModules()) {
                modules.add(new FileProvider.ModuleEntry(layer.getAds().getName().toLowerCase(), module.getName()));
            }

            if (!layer.isLocalizing()) {
                layerEntries.put(layer.getURI(), new FileProvider.LayerEntry(layer.getURI(), layer.getURI(), 1, modules));
            } else {
                localizingLayers.add(layer);
            }
        }

        for (Layer layer : localizingLayers) {
            if (layer.getBaseLayerURIs() != null && !layer.getBaseLayerURIs().isEmpty()) {
                FileProvider.LayerEntry layerEntry = layerEntries.get(layer.getBaseLayerURIs().get(0));
                if (layer.getLanguages() != null && !layer.getLanguages().isEmpty()) {
                    layerEntry.addLocalizingLayer(layer.getURI(), layer.getLanguages().get(0));
                }
            }
        }

        JMenuItem sourceItem = (JMenuItem) e.getSource();
        JPopupMenu sourceMenu = (JPopupMenu) sourceItem.getParent();
        Component invoker = sourceMenu.getInvoker();
        JComponent invokerAsJComponent = (JComponent) invoker;
        Container topLevel = invokerAsJComponent.getTopLevelAncestor();

        final LocalFileProvider provider;

        String path = branches.get(0).getFile().getParent();
        provider = new LocalFileProvider(path, null) {

            @Override
            public Collection<FileProvider.LayerEntry> getLayers() {
                return layerEntries.values();
            }
        };

        XmlSchemasExportDialog dialog = new XmlSchemasExportDialog((Window) topLevel, schemas, new ArrayList<>(languages), layerEntries.keySet());

        XmlSchemasExportTask task = dialog.show();

        RadixdocGenerationContext context = new DesignerRadixdocGenerationContext(provider, dialog.getTopLayerUri());

        XmlSchemaExporter exporter = new XmlSchemaExporter(task, context);
        exporter.exportSchemas(false);
    }
}
