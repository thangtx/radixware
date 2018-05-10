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
package org.radixware.kernel.common.defs.dds;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.radixdoc.DdsDiagramRadixdocSupport;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DefaultReferenceResolver;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.ModuleRadixdoc;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.fs.IRepositoryDdsModule;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Resource;
import org.radixware.schemas.radixdoc.Text;

public class DdsModule extends Module implements IRadixdocProvider {

    private Map<String, String> ddsDiagramReference = new HashMap<>();

    public static class Factory extends Module.Factory<DdsModule> {

        private Factory() {
        }

        @Override
        public DdsModule newInstance(Id moduleId, String moduleName) {
            return new DdsModule(moduleId, moduleName);
        }

        @Override
        public DdsModule newInstance(String moduleName) {
            final DdsModule module = super.newInstance(moduleName);
            final DdsModelDef newModel = DdsModelDef.Factory.newInstance(module);
            module.getModelManager().setModel(newModel);
            return module;
        }

        @Override
        public DdsModule overwrite(DdsModule source) {
            final DdsModule module = super.overwrite(source);
            final DdsModelDef newModel = DdsModelDef.Factory.newInstance(module);
            module.getModelManager().setModel(newModel);
            return module;
        }
        private static final Factory FACTORY_INSTANCE = new Factory();

        public static Factory getDefault() {
            return FACTORY_INSTANCE;
        }
    }

    protected DdsModule(Id id, String name) {
        super(id, name);
    }
    private final DdsModelManager modelManager = new DdsModelManager(this);

    public DdsModelManager getModelManager() {
        return modelManager;
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.DDS;
    }

    private abstract class DdsDefinitionSearcher<T extends DdsDefinition> extends DefinitionSearcher<T> {

        private DdsDefinitionSearcher() {
            super(DdsModule.this);
        }

        public abstract DefinitionSearcher<T> findDdsSearcher(DdsModule module);

        public abstract T findInsideById(DdsModelDef model, Id id);

        @Override
        public T findInsideById(Id id) {
            final DdsModelDef model = getModelManager().findModel();
            if (model != null) {
                return findInsideById(model, id);
            } else {
                return null;
            }
        }

        @Override
        public DefinitionSearcher<T> findSearcher(Module module) {
            if (module instanceof DdsModule) {
                return findDdsSearcher((DdsModule) module);
            } else {
                return null;
            }
        }
    }
    //
    private final DdsDefinitionSearcher<DdsTableDef> tableSearcher = new DdsDefinitionSearcher<DdsTableDef>() {
        @Override
        public DefinitionSearcher<DdsTableDef> findDdsSearcher(DdsModule module) {
            return module.getDdsTableSearcher();
        }

        @Override
        public DdsTableDef findInsideById(DdsModelDef model, Id id) {
            DdsTableDef table = model.getTables().findById(id);
            if (table == null) {
                table = model.getViews().findById(id);
            }
            return table;
        }
    };

    public DefinitionSearcher<DdsTableDef> getDdsTableSearcher() {
        return tableSearcher;
    }
    //
    private final DdsDefinitionSearcher<DdsColumnTemplateDef> columnTemplateSearcher = new DdsDefinitionSearcher<DdsColumnTemplateDef>() {
        @Override
        public DefinitionSearcher<DdsColumnTemplateDef> findDdsSearcher(DdsModule module) {
            return module.getDdsColumnTemplateSearcher();
        }

        @Override
        public DdsColumnTemplateDef findInsideById(DdsModelDef model, Id id) {
            return model.getColumnTemplates().findById(id);
        }
    };

    public DefinitionSearcher<DdsColumnTemplateDef> getDdsColumnTemplateSearcher() {
        return columnTemplateSearcher;
    }
    //
    private final DefinitionSearcher<Definition> definitionSearcher = new DefinitionSearcher(DdsModule.this) {
        @Override
        public Definition findInsideById(Id id) {
            if (id == null) {
                return null;
            }

            final EDefinitionIdPrefix idPrefix = id.getPrefix();
            final DdsModelDef model = getModelManager().findModel();
            if (model == null) {
                return null;
            }

            switch (idPrefix) {
                case DDS_ACCESS_PARTITION_FAMILY:
                    return model.getAccessPartitionFamilies().findById(id);
                case DDS_COLUMN_TEMPLATE:
                    return model.getColumnTemplates().findById(id);
                case DDS_FUNCTION:
                    return model.findFunctionById(id);
                case DDS_REFERENCE:
                    return model.getReferences().findById(id);
                case DDS_SEQUENCE:
                    return model.getSequences().findById(id);
                case DDS_TABLE:
                    return model.getTables().findById(id);
                case DDS_TYPE:
                    return model.getTypes().findById(id);
                case DDS_PACKAGE:
                    return model.getPackages().findById(id);
                default:
                    return null;

                // unused in global searching
                //case DDS_LABEL:
                //case DDS_MODEL:
                //case DDS_EXT_TABLE:
                // subcomponents
                //case DDS_COLUMN:
                //case DDS_INDEX:
                //case DDS_TRIGGER:
                // auto-searched subcomponents
                //case DDS_CHECK_CONSTRAINT:
                //case DDS_UNIQUE_CONSTRAINT:
                //case DDS_CUSTOM_TEXT:
                //case DDS_FUNC_PARAM:
                //case DDS_PROTOTYPE:
                //case DDS_TYPE_FIELD:
            }
        }

        @Override
        public DefinitionSearcher<? extends Definition> findSearcher(Module module) {
            return module.getDefinitionSearcher();
        }
    };

    @Override
    public DefinitionSearcher<? extends Definition> getDefinitionSearcher() {
        return definitionSearcher;
    }

    public IEnumDef findEnumById(final Id enumId) {
        final Definition enumDef = definitionSearcher.findById(enumId).get();
        if (enumDef instanceof IEnumDef) {
            return (IEnumDef) enumDef;
        } else {
            return null;
        }
    }
    //
    private final DdsDefinitionSearcher<DdsReferenceDef> referenceSearcher = new DdsDefinitionSearcher<DdsReferenceDef>() {
        @Override
        public DefinitionSearcher<DdsReferenceDef> findDdsSearcher(DdsModule module) {
            return module.getDdsReferenceSearcher();
        }

        @Override
        public DdsReferenceDef findInsideById(DdsModelDef model, Id id) {
            return model.getReferences().findById(id);
        }
    };

    public DefinitionSearcher<DdsReferenceDef> getDdsReferenceSearcher() {
        return referenceSearcher;
    }
    //
    private final DdsDefinitionSearcher<DdsSequenceDef> sequenceSearcher = new DdsDefinitionSearcher<DdsSequenceDef>() {
        @Override
        public DefinitionSearcher<DdsSequenceDef> findDdsSearcher(DdsModule module) {
            return module.getDdsSequenceSearcher();
        }

        @Override
        public DdsSequenceDef findInsideById(DdsModelDef model, Id id) {
            return model.getSequences().findById(id);
        }
    };

    public DefinitionSearcher<DdsSequenceDef> getDdsSequenceSearcher() {
        return sequenceSearcher;
    }
    //
    private final DdsDefinitionSearcher<DdsAccessPartitionFamilyDef> apfSearcher = new DdsDefinitionSearcher<DdsAccessPartitionFamilyDef>() {
        @Override
        public DefinitionSearcher<DdsAccessPartitionFamilyDef> findDdsSearcher(DdsModule module) {
            return module.getDdsApfSearcher();
        }

        @Override
        public DdsAccessPartitionFamilyDef findInsideById(DdsModelDef model, Id id) {
            return model.getAccessPartitionFamilies().findById(id);
        }
    };

    public DefinitionSearcher<DdsAccessPartitionFamilyDef> getDdsApfSearcher() {
        return apfSearcher;
    }
    //
    private final DdsDefinitionSearcher<DdsTypeDef> typeSearcher = new DdsDefinitionSearcher<DdsTypeDef>() {
        @Override
        public DefinitionSearcher<DdsTypeDef> findDdsSearcher(DdsModule module) {
            return module.getDdsTypeSearcher();
        }

        @Override
        public DdsTypeDef findInsideById(DdsModelDef model, Id id) {
            return model.getTypes().findById(id);
        }
    };

    public DefinitionSearcher<DdsTypeDef> getDdsTypeSearcher() {
        return typeSearcher;
    }
    //
    private final DdsDefinitionSearcher<DdsPackageDef> packageSearcher = new DdsDefinitionSearcher<DdsPackageDef>() {
        @Override
        public DefinitionSearcher<DdsPackageDef> findDdsSearcher(DdsModule module) {
            return module.getDdsPackageSearcher();
        }

        @Override
        public DdsPackageDef findInsideById(DdsModelDef model, Id id) {
            return model.getPackages().findById(id);
        }
    };

    public DefinitionSearcher<DdsPackageDef> getDdsPackageSearcher() {
        return packageSearcher;
    }
    //
    private final DdsDefinitionSearcher<DdsFunctionDef> functionSearcher = new DdsDefinitionSearcher<DdsFunctionDef>() {
        @Override
        public DefinitionSearcher<DdsFunctionDef> findDdsSearcher(DdsModule module) {
            return module.getDdsFunctionSearcher();
        }

        @Override
        public DdsFunctionDef findInsideById(DdsModelDef model, Id id) {
            return model.findFunctionById(id);
        }
    };

    public DefinitionSearcher<DdsFunctionDef> getDdsFunctionSearcher() {
        return functionSearcher;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);

        final DdsModelDef model = getModelManager().findModel();
        if (model != null) {
            model.visit(visitor, provider);
        }

    }

    @Override
    public DdsModule findOverwritten() {
        return (DdsModule) super.findOverwritten();
    }

    @Override
    public List<DdsModule> findAllOverwritten() {
        return (List<DdsModule>) super.findAllOverwritten();
    }

    @Override
    public RadixIcon getIcon() {
        return isTest() ? DdsDefinitionIcon.TEST_MODULE : DdsDefinitionIcon.MODULE;
    }

    private class DdsModuleClipboardSupport extends ClipboardSupport<DdsModule> {

        public DdsModuleClipboardSupport() {
            super(DdsModule.this);
        }

        @Override
        protected XmlObject copyToXml() {
            final org.radixware.schemas.product.Module xModule = org.radixware.schemas.product.Module.Factory.newInstance();
            DdsModule.this.appendTo(xModule);
            return xModule;
        }

        @Override
        protected DdsModule loadFrom(XmlObject xmlObject) {
            final org.radixware.schemas.product.Module xModule = (org.radixware.schemas.product.Module) xmlObject;
            return Factory.getDefault().loadFrom(xModule);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            final DdsModelDef model = getModelManager().findModel();
            return model != null ? model.getClipboardSupport().canPaste(objectsInClipboard, resolver) : CanPasteResult.NO;
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            checkForCanPaste(objectsInClipboard, resolver);

            final DdsModelDef model = getModelManager().findModel();
            model.getClipboardSupport().paste(objectsInClipboard, resolver);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsModule> getClipboardSupport() {
        return new DdsModuleClipboardSupport();
    }

    @Override
    public void save() throws IOException {
        super.save();

        // save model, because model and module is one tree node in designer :-(
        if (getModelManager().isInitialized()) {
            final DdsModelDef model = getModelManager().findModel();
            if (model != null) {
                model.save();
            }
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        final DdsModelDef modifiedModel = getModelManager().getModifiedModelIfLoaded();
        if (modifiedModel != null) {
            sb.append("<br><b>Captured by '");

            final String editor = modifiedModel.getModifierInfo().getEditor();
            sb.append(String.valueOf(editor));
            sb.append("' on '");

            final String station = modifiedModel.getModifierInfo().getStation();
            sb.append(String.valueOf(station));

            sb.append("'</b>");
        }
    }

    @Override
    public DdsModule getModule() {
        return this;
    }

    @Override
    public String getTypeTitle() {
        return "DDS Module";
    }

    @Override
    public String getTypesTitle() {
        return "DDS Modules";
    }

    @Override
    public IRepositoryDdsModule getRepository() {
        return (IRepositoryDdsModule) super.getRepository();
    }

    private static void addFileEntry(final org.radixware.schemas.product.Directory.FileGroups.FileGroup fileGrooup, File dir, final File file) throws IOException {
        if (file.isFile()) {
            final org.radixware.schemas.product.Directory.FileGroups.FileGroup.File fileEntry = fileGrooup.addNewFile();
            fileEntry.setName(FileUtils.getRelativePath(dir, file).replace(File.separatorChar, '/'));
            final byte[] digest;
            try {
                digest = DirectoryFileSigner.calcFileDigest(file);
            } catch (NoSuchAlgorithmException cause) {
                throw new IllegalStateException(cause);
            }
            fileEntry.setDigest(digest);
        }
    }
    
    public void saveSqmlDefsXml() throws IOException {
        new SqmlDefinitionsWriter(this).write();
    }

    public void saveDirectoryXml() throws IOException {
        final org.radixware.schemas.product.DirectoryDocument doc = org.radixware.schemas.product.DirectoryDocument.Factory.newInstance();
        final org.radixware.schemas.product.Directory directoryEntry = doc.addNewDirectory();
        final org.radixware.schemas.product.Directory.FileGroups fileGroups = directoryEntry.addNewFileGroups();
        final org.radixware.schemas.product.Directory.FileGroups.FileGroup fileGrooup = fileGroups.addNewFileGroup();
        fileGrooup.setGroupType(org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType.DDS);

        final File dir = getDirectory();

        if (dir == null) {
            throw new NullPointerException("The module " + getQualifiedName() + " is not file-based");
        }

        final File fixedModelFile = new File(dir, DdsModelManager.FIXED_MODEL_XML_FILE_NAME);
        addFileEntry(fileGrooup, dir, fixedModelFile);

        final File moduleFile = new File(dir, MODULE_XML_FILE_NAME);
        addFileEntry(fileGrooup, dir, moduleFile);
        
        final File sqmlDefsFile = new File(dir, FileUtils.SQML_DEFINITIONS_XML_FILE_NAME);
        addFileEntry(fileGrooup, dir, sqmlDefsFile);
        
        final File localeDir = new File(dir, "locale");
        if (localeDir.exists()) {
            final File[] locales = localeDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith("xml");
                }
            });
            if (locales != null && locales.length>0) {
                final Comparator<File> comparator = new Comparator<File>() {
                    @Override
                    public int compare(File f1, File f2) {
                        return f1.getName().compareTo(f2.getName());
                    }
                };                
                Arrays.sort(locales, comparator);
                for (int i = 0; i < locales.length; i++) {
                    addFileEntry(fileGrooup, dir, locales[i]);
                }
            }
        }

        final File directoryXmlFile = new File(dir, FileUtils.DIRECTORY_XML_FILE_NAME);
        XmlUtils.saveXmlPretty(doc, directoryXmlFile);
    }

    @Override
    public DdsSegment getSegment() {
        return (DdsSegment) super.getSegment();
    }

    @Override
    public void saveDescription() throws IOException {
        super.saveDescription();
        saveDirectoryXml(); // update module.xml digest
        getSegment().saveDirectoryXml(); // update module name
    }

    @Override
    public boolean delete() {
        final DdsSegment segment = getSegment();
        final boolean result = super.delete();

        if (result && segment != null && segment.getDirectory() != null) {
            try {
                segment.saveDirectoryXml();
            } catch (IOException cause) {
                throw new DefinitionError("Unable to update directory.xml", segment, cause);
            }
        }

        return result;
    }

    @Override
    public boolean canDelete() {
        if (!super.canDelete()) {
            return false;
        }

        // unable to delete captured model, because modification script will not created.
        try {
            if (this.getModelManager().getModifiedModel() != null) {
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            return false;
        }

        final DdsModelDef model = this.getModelManager().findModel();
        if (model == null) {
            return false;
        }

        // unable to delete not empty model, because modification script will not created.
        if (model.find(DdsVisitorProviderFactory.newGeneratedInDbProvider()) != null) {
            return false;
        }

        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<Module>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ModuleRadixdoc(DdsModule.this, page, options) {
                    @Override
                    protected Block documentPageHead(Page page) {
                        final Block headChapter = super.documentPageHead(page);

                        final Block diagramBlock = headChapter.addNewBlock();
                        diagramBlock.setStyle(DefaultStyle.TITLE);
                        final Ref diagramRef = diagramBlock.addNewRef();
                        diagramRef.setPath(source.getLayer().getURI() + "/" + source.getSegment().getName().toLowerCase() + "/" + source.getModule().getName() + "/diagram");
                        diagramRef.addNewText().setStringValue("Diagram");

                        return headChapter;
                    }
                };
            }
        };
    }

    public String getDiagramHtmlPage() {
        try {
            return DdsDiagramRadixdocSupport.generateHtml(getMetaData(this.getLayer()));
        } catch (JSONException ex) {
            return null;
        }
    }

    public Map<String, String> getDdsDiagramReference() {
        return ddsDiagramReference;
    }

    public JSONObject getMetaData(RadixObject source) {
        ddsDiagramReference.clear();
        JSONObject meta = new JSONObject();
        JSONArray tableArray = new JSONArray();
        DefaultReferenceResolver ref = new DefaultReferenceResolver();
        try {
            DdsDefinitions<DdsTableDef> tables = getModelManager().findModel().getTables();
            for (DdsTableDef table : tables) {
                JSONObject jsonTable = new JSONObject();
                String tableKey = "ResolveTo" + table.getId();
                ddsDiagramReference.put(tableKey, ref.resolve(null, table));
                jsonTable.put("name", table.getName());
                jsonTable.put("id", table.getId());
                JSONObject jsonTablePos = new JSONObject();
                jsonTablePos.put("x", table.getPlacement().getPosX());
                jsonTablePos.put("y", table.getPlacement().getPosY());
                jsonTable.put("position", jsonTablePos);
                jsonTable.put("link", tableKey);
                JSONArray columnArray = new JSONArray();

                for (DdsColumnDef column : table.getColumns().get(ExtendableDefinitions.EScope.ALL)) {
                    String columnKey = "ResolveTo" + column.getId();
                    ddsDiagramReference.put(columnKey, ref.resolve(null, column));
                    columnArray.put(column.getName() + "@" + columnKey);
                }
                jsonTable.put("columns", columnArray);
                if (!table.getIndices().getAll(ExtendableDefinitions.EScope.ALL).isEmpty()) {
                    JSONArray indexArray = new JSONArray();
                    for (DdsIndexDef index : table.getIndices().getAll(ExtendableDefinitions.EScope.ALL)) {
                        String indexKey = "ResolveTo" + index.getId();
                        ddsDiagramReference.put(indexKey, ref.resolve(null, index));
                        indexArray.put(index.getName() + "@" + indexKey);
                    }
                    jsonTable.put("indicies", indexArray);
                }

                if (!table.getTriggers().getAll(ExtendableDefinitions.EScope.ALL).isEmpty()) {
                    JSONArray tiggersArray = new JSONArray();
                    for (DdsTriggerDef trigger : table.getTriggers().getAll(ExtendableDefinitions.EScope.ALL)) {
                        String triggerKey = "ResolveTo" + trigger.getId();
                        ddsDiagramReference.put(triggerKey, ref.resolve(null, trigger));
                        tiggersArray.put(trigger.getName() + "@" + triggerKey);
                    }
                    jsonTable.put("triggers", tiggersArray);
                }
                tableArray.put(jsonTable);
                meta.put("tables", tableArray);
            }

            DdsDefinitions<DdsViewDef> views = getModelManager().findModel().getViews();
            JSONArray viewArray = new JSONArray();
            for (DdsViewDef view : views) {
                String viewKey = "ResolveTo" + view.getId();
                ddsDiagramReference.put(viewKey, ref.resolve(null, view));
                JSONObject jsonView = new JSONObject();
                jsonView.put("name", view.getName());
                jsonView.put("id", view.getId());
                JSONObject jsonViewPos = new JSONObject();
                jsonViewPos.put("x", view.getPlacement().getPosX());
                jsonViewPos.put("y", view.getPlacement().getPosY());
                jsonView.put("position", jsonViewPos);
                jsonView.put("link", viewKey);
                JSONArray columnArray = new JSONArray();
                for (DdsColumnDef column : view.getColumns().get(ExtendableDefinitions.EScope.ALL)) {
                    String columnKey = "ResolveTo" + column.getId();
                    ddsDiagramReference.put(columnKey, ref.resolve(null, column));
                    columnArray.put(column.getName() + "@" + columnKey);
                }
                jsonView.put("columns", columnArray);

                if (!view.getIndices().getAll(ExtendableDefinitions.EScope.ALL).isEmpty()) {
                    JSONArray indexArray = new JSONArray();
                    for (DdsIndexDef index : view.getIndices().getAll(ExtendableDefinitions.EScope.ALL)) {
                        String key = "ResolveTo" + index.getId();
                        ddsDiagramReference.put(key, ref.resolve(null, index));
                        indexArray.put(index.getName() + "@" + key);
                    }
                    jsonView.put("indices", indexArray);
                }

                if (!view.getTriggers().getAll(ExtendableDefinitions.EScope.ALL).isEmpty()) {
                    JSONArray tiggersArray = new JSONArray();
                    for (DdsTriggerDef trigger : view.getTriggers().getAll(ExtendableDefinitions.EScope.ALL)) {
                        String key = "ResolveTo" + trigger.getId();
                        ddsDiagramReference.put(key, ref.resolve(null, trigger));
                        tiggersArray.put(trigger.getName() + "@" + key);
                    }
                    jsonView.put("triggers", tiggersArray);
                }
                viewArray.put(jsonView);
                meta.put("views", viewArray);
            }
            DdsDefinitions<DdsExtTableDef> extTables = getModelManager().findModel().getExtTables();
            JSONArray shortcutsArray = new JSONArray();
            for (DdsExtTableDef extTable : extTables) {
                JSONObject jsonExtTable = new JSONObject();
                String extTableKey = "ResolveTo" + extTable.getId();
                ddsDiagramReference.put(extTableKey, ref.resolve(null, extTable));
                jsonExtTable.put("name", extTable.getName());
                jsonExtTable.put("id", extTable.getId());
                JSONObject jsonExtTablePos = new JSONObject();
                jsonExtTablePos.put("x", extTable.getPlacement().getPosX());
                jsonExtTablePos.put("y", extTable.getPlacement().getPosY());
                jsonExtTable.put("position", jsonExtTablePos);
                jsonExtTable.put("link", extTableKey);
                JSONArray columnArray = new JSONArray();
                for (DdsColumnDef column : extTable.findTable().getColumns().getAll(ExtendableDefinitions.EScope.ALL)) {
                    if (column.isPrimaryKey()) {
                        columnArray.put(column.getName());
                    }
                }
                jsonExtTable.put("fields", columnArray);
                shortcutsArray.put(jsonExtTable);
                meta.put("shortcuts", shortcutsArray);
            }
            DdsDefinitions<DdsSequenceDef> sequences = getModelManager().findModel().getSequences();
            JSONArray sequencesArray = new JSONArray();
            for (DdsSequenceDef sequence : sequences) {
                JSONObject jsonSequence = new JSONObject();
                String sequenceKey = "ResolveTo" + sequence.getId();
                ddsDiagramReference.put(sequenceKey, ref.resolve(null, sequence));
                jsonSequence.put("name", sequence.getName());
                jsonSequence.put("id", sequence.getId());
                JSONObject jsonSequencePos = new JSONObject();
                jsonSequencePos.put("x", sequence.getPlacement().getPosX());
                jsonSequencePos.put("y", sequence.getPlacement().getPosY());
                jsonSequence.put("position", jsonSequencePos);
                jsonSequence.put("link", sequenceKey);
                sequencesArray.put(jsonSequence);
                meta.put("sequences", sequencesArray);
            }
            DdsDefinitions<DdsLabelDef> labels = getModelManager().findModel().getLabels();
            JSONArray labelsArray = new JSONArray();
            for (DdsLabelDef label : labels) {
                JSONObject jsonLabel = new JSONObject();
                jsonLabel.put("text", label.getText());
                JSONObject jsonLabelPos = new JSONObject();
                jsonLabelPos.put("x", label.getPlacement().getPosX());
                jsonLabelPos.put("y", label.getPlacement().getPosY());
                jsonLabel.put("position", jsonLabelPos);
                labelsArray.put(jsonLabel);
                meta.put("labels", labelsArray);
            }
            DdsDefinitions<DdsReferenceDef> references = getModelManager().findModel().getReferences();
            JSONArray referencesArray = new JSONArray();
            for (DdsReferenceDef reference : references) {
                JSONObject jsonReference = new JSONObject();
                jsonReference.put("type", getStrForRefType(reference.getType().name()));
                jsonReference.put("source", reference.getExtChildTableId() != null ? reference.getExtChildTableId() : reference.getChildTableId());
                jsonReference.put("target", reference.getExtParentTableId() != null ? reference.getExtParentTableId() : reference.getParentTableId());
                for (DdsReferenceDef.ColumnsInfoItem columnInfo : reference.getColumnsInfo()) {
                    jsonReference.put("childColumn", columnInfo.getChildColumn().getName());
                    jsonReference.put("parentColumn", columnInfo.getParentColumn().getName());
                }
                referencesArray.put(jsonReference);
                meta.put("references", referencesArray);
            }
        } catch (JSONException ex) {
            Logger.getLogger(DdsModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meta;
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    public boolean isCapturedStructure() {
        if (!isReadOnly() && modelManager != null && modelManager.isInitialized()) {
            final DdsModelDef modifiedModel = modelManager.getModifiedModelIfLoaded();
            return modifiedModel == null;
        }
        return false;
    }

    public String getStrForRefType(String name) {
        switch (DdsReferenceDef.EType.valueOf(name)) {
            case MASTER_DETAIL:
                return "MasterDetailReference";
            case LINK:
                return "ForeignKeyReference";
            default:
                return "not defined";
        }
    }

    @Override
    public ILocalizingBundleDef findLocalizingBundle() {
        DdsModelDef model = getModelManager().findModel();
        if (model != null) {
            return model.getStringBundle();
        }
        return null;
    }

    @Override
    public ILocalizingBundleDef findExistingLocalizingBundle() {
        return findLocalizingBundle();
    }
    
    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        if (descriptionId != null) {
            final IMultilingualStringDef string = findLocalizedString(descriptionId);
            if (string != null) {
                string.setValue(language, description);
            }
            return true;
        }
        return false;
    }
}
