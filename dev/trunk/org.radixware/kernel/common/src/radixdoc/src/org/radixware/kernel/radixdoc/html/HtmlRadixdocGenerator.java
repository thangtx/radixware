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
package org.radixware.kernel.radixdoc.html;

import org.radixware.kernel.common.exceptions.RadixdocStopParseException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.radixdoc.DiagramRefItem;
import org.radixware.schemas.radixdoc.IndexUnit;
import org.radixware.schemas.radixdoc.IndexUnitDocument;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.RadixdocUnitDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Генератор Html документации Radixdoc.
 *
 */
public class HtmlRadixdocGenerator {

    public static final String TRANSFORMER_FACTORY = "javax.xml.transform.TransformerFactory";

    /**
     * Генерация документации с заданными параметрами.
     *
     * @param options параметры документации (список языков, сжатие и т.д.)
     * @param progressHandle обработчик хода выполнения (может быть null)
     * @param cancellable возможность остановить генерауию
     */
    public static void generate(IRadixdocOptions options, IProgressHandle progressHandle, ICancellable cancellable) {
        if (options.isCompressToZip()) {
            final HtmlRadixdocGenerator htmlGenerator = new ZipGenerator(options, progressHandle, cancellable);
            htmlGenerator.generate();
        } else {
            final HtmlRadixdocGenerator htmlGenerator = new HtmlRadixdocGenerator(options, progressHandle, cancellable);
            htmlGenerator.generate();
        }
    }

//    public static boolean generateDitaHtml(List<List<DefinitionDocInfo>> definitions, IRadixdocOptions options, IProgressHandle progressHandle, String dir) {
//        final HtmlRadixdocGenerator htmlGenerator = new HtmlRadixdocGenerator(options, progressHandle, null);
//        return htmlGenerator.generateDita(definitions, dir);
//    }
    /**
     * Проверяет совместимость версий генератора и документации.
     *
     * @param projectVersion версия файла radixdoc.xml
     * @param kerlenVersion версия генератора
     * @return true, если версии совместимы, иначе false
     */
    public static boolean isCompatibleVersion(String projectVersion, String kerlenVersion) {
        return Objects.equals(projectVersion, kerlenVersion);
    }

    private static final class HeadHandler extends DefaultHandler {

        private ReferenceReslover reslover;
        private String root;

        public HeadHandler(ReferenceReslover reslover, String root) {
            this.reslover = reslover;
            this.root = root;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("RadixdocUnit")) {
                final String value = attributes.getValue("Pages");
                final String[] pages = value.split(" ");

                for (final String page : pages) {
                    reslover.add(root + "/" + page);
                }
                throw new RadixdocStopParseException();
            }
        }
    }
    private final ProcessHandle processHandle;
    private final IRadixdocOptions options;
    private Transformer transformer;
    private Set<String> resources;
    private ReferenceReslover reslover;

    HtmlRadixdocGenerator(IRadixdocOptions options, IProgressHandle progressHandle, ICancellable cancellable) {
        this.options = options;
        this.processHandle = new ProcessHandle(progressHandle, cancellable);
    }

    protected final void generate() {
        try {
            processHandle.start();
            processHandle.switchToIndeterminate();
            processHandle.setDisplayName("Generate html...");

            if (prepare()) {

                if (processHandle.wasCancelled()) {
                    return;
                }

                final int size = reslover.getCount() * options.getLanguages().size();
                processHandle.switchToDeterminate(size);

                generateHtml();
            }
        } finally {
            finish();
        }
    }

    protected boolean prepare() {
        reslover = new ReferenceReslover(getOptions());

        if (!preselection()) {
            return false;
        }

        try {
            transformer = (new org.apache.xalan.processor.TransformerFactoryImpl()).newTransformer(new StreamSource(getFileProvider().getXsltSchemeStream()));
            if (transformer == null) {
                return false;
            }
            transformer.setParameter("generator", this);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    protected void finish() {
        getFileProvider().clearCache();
        processHandle.finish();
    }

    /**
     * Обход всех модулей и формирование списка всех описываемых элементов.
     */
    private boolean preselection() {
        final SAXParser parser;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException | SAXException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        for (final FileProvider.LayerEntry layer : getFileProvider().getLayers()) {
            for (final FileProvider.ModuleEntry module : layer.getModules()) {
                try (final InputStream inputStream = getFileProvider().getRadixdocInputStream(module)) {
                    if (inputStream != null) {
                        parser.parse(inputStream, new HeadHandler(reslover, module.getOutputPath()));
                    }
                } catch (RadixdocStopParseException e) {
                    continue;
                } catch (SAXException | IOException ex) {
                    Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return true;
    }

    protected void generateHtml() {

        final IndexUnitDocument indexDocument = IndexUnitDocument.Factory.newInstance();
        final IndexUnit index = indexDocument.addNewIndexUnit();

        for (final FileProvider.LayerEntry layer : getFileProvider().getLayers()) {
            final IndexUnit indexLayer = index.addNewSubUnit();
            indexLayer.setTitle(layer.getIdentifier());
            indexLayer.setSubPath(layer.getSubPath());

            modules_loop:
            for (final FileProvider.ModuleEntry module : layer.getModules()) {
                final IndexUnit indexUnit = indexLayer.addNewSubUnit();

                processUnit();

                try (final InputStream inputStream = getFileProvider().getRadixdocInputStream(module)) {
                    if (inputStream == null) {
                        continue modules_loop;
                    }

                    final RadixdocUnitDocument document = RadixdocUnitDocument.Factory.parse(inputStream);
                    for (final EIsoLanguage lang : options.getLanguages()) {
                        transformer.setParameter("lang", lang);
                        processUnitEntry(document, module.getRootPath(), module.getOutputPath(), lang);
                    }

                    Map<EDefinitionIdPrefix, IndexUnit> typeUnits = new HashMap();
                    for (final Page page : document.getRadixdocUnit().getPageList()) {
                        if (page.isSetType() && page.getType().equals("Module")) {
                            indexUnit.setReference(page.getName());
                            indexUnit.setTitle(page.getTitle());
                            indexUnit.setSubPath(module.getSubPath());
                            indexUnit.setIcon(page.getIcon());
                        } else if (page.isSetTopLevel() && page.getTopLevel()) {
                            final IndexUnit parentIndexElement;
                            if (module.getSubPath().startsWith("dds/")) {
                                EDefinitionIdPrefix prefix = Id.Factory.loadFrom(page.getName()).getPrefix();

                                if (typeUnits.containsKey(prefix)) {
                                    parentIndexElement = typeUnits.get(prefix);
                                } else {
                                    parentIndexElement = indexUnit.addNewSubUnit();
                                    parentIndexElement.setTitle(getPrefixTitle(prefix));
                                    typeUnits.put(prefix, parentIndexElement);
                                }
                            } else {
                                parentIndexElement = indexUnit;
                            }

                            final IndexUnit indexPage = parentIndexElement.addNewSubUnit();
                            indexPage.setReference(page.getName());
                            indexPage.setTitle(page.getTitle());
                            indexPage.setIcon(page.getIcon());
                        }

                        for (final EIsoLanguage lang : options.getLanguages()) {
                            transformer.setParameter("lang", lang);
                            processEntry(page, module.getRootPath(), module.getOutputPath(), lang);

                            processHandle.incProgress(page.getTitle());
                            if (processHandle.wasCancelled()) {
                                return;

                            }
                        }
                    }
                } catch (XmlException | IOException ex) {
                    Logger.getLogger(HtmlRadixdocGenerator.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        for (final EIsoLanguage lang : options.getLanguages()) {
            transformer.setParameter("lang", lang);
            generateUnitsIndex(indexDocument, lang.getValue() + "/index.html");
        }

        processCopyFileEntries();
    }

    private String getPrefixTitle(EDefinitionIdPrefix prefix) {
        switch (prefix) {
            case DDS_TABLE:
                return "Tables";
            case DDS_REFERENCE:
                return "References";
            case DDS_SEQUENCE:
                return "Sequences";
            case DDS_PACKAGE:
                return "Packages";
            case DDS_TYPE:
                return "Types";
            case DDS_COLUMN_TEMPLATE:
                return "Column Templates";
            case DDS_ACCESS_PARTITION_FAMILY:
                return "Access Partition Families";
            default:
                return prefix.toString();
        }
    }

    protected void generateUnitsIndex(IndexUnitDocument indexDocument, String path) {
        final XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveOuter();
        processEntry(indexDocument.newInputStream(xmlOptions), path);
    }

    protected void processUnit() {
        resources = new HashSet<>();
    }

    private void processUnitEntry(RadixdocUnitDocument xUnit, String rootPath, String outPath, EIsoLanguage lang) {
        if (processHandle.wasCancelled()) {
            return;
        }
        if (xUnit.getRadixdocUnit().isSetDdsDiagramHtmlPage()) {
            transformer.setParameter("rootPath", rootPath);
            transformer.setParameter("outPath", outPath);

            if (xUnit.getRadixdocUnit().getDdsDiagramHtmlPage() != null) {
                String diagramHtml = xUnit.getRadixdocUnit().getDdsDiagramHtmlPage().getPageTextAsStr();
                if (diagramHtml != null && !diagramHtml.isEmpty()) {
                    diagramHtml = new String(Base64.decode(diagramHtml));

                    for (DiagramRefItem xRefItem : xUnit.getRadixdocUnit().getDdsDiagramHtmlPage().getDiagramRefItemList()) {
                        String resolvedRef = TransformLib.parsePath(xRefItem.getValue(), lang, this);
                        diagramHtml = diagramHtml.replaceAll(xRefItem.getItemId(), resolvedRef);
                    }

                    final String filePath = lang.getValue() + "/" + outPath + "/diagram.html";

                    prepareFile(filePath);
                    try {
                        try (final StreamWraper<OutputStream> streamWraper = getOutputStreamWraper(filePath)) {
                            streamWraper.getStream().write(diagramHtml.getBytes("utf-8"));

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(HtmlRadixdocGenerator.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void processEntry(Page page, String rootPath, String outPath, EIsoLanguage lang) {
        if (processHandle.wasCancelled()) {
            return;
        }

        transformer.setParameter("rootPath", rootPath);
        transformer.setParameter("outPath", outPath);

        final String fileName = lang.getValue() + "/" + outPath + "/" + page.getName() + ".html";
        final XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveOuter();
        processEntry(page.newInputStream(xmlOptions), fileName);
    }

    final void processEntry(InputStream inputStream, String fileName) {
        try (InputStream buffInputStream = new BufferedInputStream(inputStream)) {
            prepareFile(fileName);

            try (final StreamWraper<OutputStream> streamWraper = getOutputStreamWraper(fileName)) {
                transform(streamWraper.getStream(), buffInputStream);
            }
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected boolean prepareFile(String fileName) {
        final File file = new File(getFileProvider().getOutput().getAbsolutePath() + "/" + fileName);
        return file.getParentFile().mkdirs();
    }

    protected boolean afterFile(String fileName) {
        return true;
    }

    private void transform(OutputStream outputStream, InputStream inputStream) throws TransformerConfigurationException, TransformerException, FileNotFoundException, IOException {
        transformer.transform(new StreamSource(inputStream), new StreamResult(outputStream));
        outputStream.flush();
    }

    final void createFile(InputStream inputStream, String outFileName) throws IOException {
        prepareFile(outFileName);

        try (final StreamWraper<OutputStream> streamWraper = getOutputStreamWraper(outFileName)) {
            FileUtils.copyStream(inputStream, streamWraper.getStream());

        }
    }

    private static class CopyFileEntry {

        final String inFileName;
        final String outFileName;
        final EFileSource source;

        public CopyFileEntry(EFileSource source, String inFileName, String outFileName) {
            this.inFileName = inFileName;
            this.outFileName = outFileName;
            this.source = source;
        }
    }
    private final Map<String, CopyFileEntry> copyEntries = new HashMap<>();

    final void addCopyFileEntry(EFileSource source, String inFileName, String outFileName) {
        copyEntries.put(outFileName, new CopyFileEntry(source, inFileName, outFileName));
    }

    private void processCopyFileEntries() {
        for (final CopyFileEntry entry : copyEntries.values()) {
            try (final InputStream inputStream = getFileProvider().getInputStream(entry.source, entry.inFileName)) {
                if (inputStream != null) {
                    createFile(inputStream, entry.outFileName);

                }
            } catch (IOException ex) {
                Logger.getLogger(ZipGenerator.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected StreamWraper<OutputStream> getOutputStreamWraper(String path) {
        try {
            return new FileOutputStreamWraper(new FileOutputStream(getFileProvider().getOutput().getAbsolutePath() + "/" + path));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class
                    .getName()).log(Level.SEVERE, getFileProvider().getOutput().getAbsolutePath(), ex);

            throw new RuntimeException(ex);
        }
    }

    protected final FileProvider getFileProvider() {
        return options.getFileProvider();
    }

    public IRadixdocOptions getOptions() {
        return options;
    }

    String resolvePath(String path) {
        return reslover.resolve(path);
    }
}
