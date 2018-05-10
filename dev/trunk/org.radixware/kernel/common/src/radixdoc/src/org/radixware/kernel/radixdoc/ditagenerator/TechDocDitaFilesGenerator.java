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
package org.radixware.kernel.radixdoc.ditagenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixdocGenerationException;
import org.radixware.kernel.common.mml.MarkdownGenerationContext;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.radixdoc.enums.EDitaDocumentType;
import org.radixware.kernel.radixdoc.generator.GeneratorUtils;
import org.radixware.kernel.radixdoc.html.TmpFileInputStreamWrapper;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationTask;
import org.radixware.schemas.radixdoc.AbstractDocItem;
import org.radixware.schemas.radixdoc.AdsDefDocItem;
import org.radixware.schemas.radixdoc.AdsMapDefDocItem;
import org.radixware.schemas.radixdoc.AdsTopicDefDocItem;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class TechDocDitaFilesGenerator {

    private final String NAME_GENERAL_MAP_FILE = "map.ditamap";
    private final List<Id> rootMapId;
    private final RadixdocGenerationContext context;
    private final RadixdocGenerationTask task;
    private EIsoLanguage lang;
    private ZipOutputStream resultZos;
    private final DefinitionDitaGeneratorFactory definitionGeneratorFactory;
    private List<String> fileNameList = new ArrayList<String>();

    public TechDocDitaFilesGenerator(List<Id> rootMapId, RadixdocGenerationContext context, RadixdocGenerationTask task) {
        this.rootMapId = rootMapId;
        this.context = context;
        this.task = task;
        definitionGeneratorFactory = new DefinitionDitaGeneratorFactory(null, null, null);
        definitionGeneratorFactory.setChangeLogContainer(new ChangeLogDitaContainer(context));
    }

    private String getCaption(AdsDefDocItem item) {
        String title = GeneratorUtils.getLocalizedStringValueById(context, item.getId(), item.getTitleId(), lang);
        return StringUtils.isEmpty(title) ? item.getName() : title;
    }

    /**
     * Генерирует архив с dita файлами для генерации документации по rootMapId.
     *
     * @param lang
     * @param generatedFileName в generatedFileName[0] вернется имя файла root
     * map (этот фалй нужно будет при генерации документации).
     * @return архив с dita файлами для генерации документации.
     * @throws RadixdocGenerationException
     */
    public InputStream generate(final EIsoLanguage lang, String[] generatedFileName) throws RadixdocGenerationException {

        this.lang = lang;

        try {
            File tmp = File.createTempFile("ditafiles", ".zip");
            tmp.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(tmp);
            resultZos = new ZipOutputStream(fos);

            expandGeneralMap();

            resultZos.close();
            generatedFileName[0] = NAME_GENERAL_MAP_FILE;

            return new TmpFileInputStreamWrapper(tmp);

        } catch (ParserConfigurationException | IOException | DOMException t) {
            Logger.getLogger(TechDocDitaFilesGenerator.class.getName()).log(Level.SEVERE, null, t);
            throw new RadixdocGenerationException("Technical documentation generation failed!", t);
        }

    }

    private void expandGeneralMap() throws IOException, ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        Document document = docBuilder.newDocument();

        Element map = document.createElement("map");
        document.appendChild(map);

        String mapName = "Technial Documentation";
        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(mapName));
        map.appendChild(title);

        Element topichead;
        topichead = document.createElement("topichead");
        map.appendChild(topichead);
        topichead.setAttribute("navtitle", mapName);

        for (Id def : rootMapId) {
            DefinitionDocInfo docInfo = context.getDefinitionDocInfo(def);
            if (docInfo == null) {
                continue;
            }
            AbstractDocItem item = docInfo.getDocItem();
            AdsDefDocItem defItem = (AdsDefDocItem) item;

            // map
            if (defItem instanceof AdsMapDefDocItem) {
                expandMap(def);

                Element ref = document.createElement("mapref");
                topichead.appendChild(ref);

                ref.setAttribute("href", def.toString() + ".ditamap");
                ref.setAttribute("locktitle", "yes");
            }
        }

        String fileSource = DitaGeneratorUtils.documentToString(document, EDitaDocumentType.MAP);
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        tmpBaos.write(fileSource.getBytes(StandardCharsets.UTF_8));
        resultZos.putNextEntry(new ZipEntry(NAME_GENERAL_MAP_FILE));
        resultZos.write(tmpBaos.toByteArray());
        resultZos.flush();

    }

    /**
     * Создам ditamap фалй и заолняем его. Также вызываем
     * expandTopic()\expandMap() для связаных с map объектов.
     *
     * @param id map id
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private void expandMap(Id id) throws IOException, ParserConfigurationException {

        DefinitionDocInfo docInfoMap = context.getDefinitionDocInfo(id);
        AbstractDocItem itemMap = docInfoMap.getDocItem();
        AdsMapDefDocItem defItemMap = (AdsMapDefDocItem) itemMap;

        String fileName = defItemMap.getId().toString() + ".ditamap";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element map = document.createElement("map");
        document.appendChild(map);

        Element title = document.createElement("title");
        String caption = getCaption(defItemMap);
        title.appendChild(document.createTextNode(caption));
        map.appendChild(title);

        Element topichead;
        topichead = document.createElement("topichead");
        map.appendChild(topichead);
        topichead.setAttribute("navtitle", caption);

        List<Id> defList = defItemMap.getItemsList();
        for (Id def : defList) {

            // defItem
            DefinitionDocInfo docInfo = context.getDefinitionDocInfo(def);
            if (docInfo == null) {
                continue;
            }
            AbstractDocItem item = docInfo.getDocItem();
            AdsDefDocItem defItem = (AdsDefDocItem) item;

            // topic
            if (defItem instanceof AdsTopicDefDocItem) {
                expandTopic(def);

                Element ref = document.createElement("topicref");
                topichead.appendChild(ref);

                ref.setAttribute("format", "markdown");
                ref.setAttribute("href", def.toString() + ".md");
                ref.setAttribute("locktitle", "yes");

                Element topicmeta = document.createElement("topicmeta");
                ref.appendChild(topicmeta);

                Element navtitle = document.createElement("navtitle");
                topicmeta.appendChild(navtitle);
                String navtitleText = getCaption(defItem);
                Text navtitleTextNode = document.createTextNode(navtitleText);
                navtitle.appendChild(navtitleTextNode);
            }

            // map
            if (defItem instanceof AdsMapDefDocItem) {
                expandMap(def);

                Element ref = document.createElement("mapref");
                topichead.appendChild(ref);

                ref.setAttribute("href", def.toString() + ".ditamap");
                ref.setAttribute("locktitle", "yes");
            }

        }

        String fileSource = DitaGeneratorUtils.documentToString(document, EDitaDocumentType.MAP);
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        tmpBaos.write(fileSource.getBytes(StandardCharsets.UTF_8));
        resultZos.putNextEntry(new ZipEntry(fileName));
        resultZos.write(tmpBaos.toByteArray());
        resultZos.flush();

    }

    private void expandTopic(Id id) throws IOException {
        DefinitionDocInfo docInfo = context.getDefinitionDocInfo(id);
        AbstractDocItem item = docInfo.getDocItem();
        AdsTopicDefDocItem defItem = (AdsTopicDefDocItem) item;

        // name
        String fileName = defItem.getId().toString() + ".md";
        if (fileNameList.contains(fileName)) {
            return;
        }
        fileNameList.add(fileName);

        // mml
        Mml mml = context.getMml(defItem.getId(), lang);
        MarkdownGenerationContext markdownContext = new MarkdownGenerationContext(lang, task.getRadixDocDir(), task.getTechDocDir(), task.getResourcesDir());
        String body = (mml == null) ? ("<topic body not found>") : (mml.getMarkdown(markdownContext)); // TODO: !? can better generate error
        String caption = getCaption(defItem);
        String fileSource = "% " + caption + "\n\n" + body;

        // write
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        tmpBaos.write(fileSource.getBytes(StandardCharsets.UTF_8));
        resultZos.putNextEntry(new ZipEntry(fileName));
        resultZos.write(tmpBaos.toByteArray());
        resultZos.flush();
    }
}
