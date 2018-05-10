/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.defs.ads.doc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.IFileSystemRadixObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.DocumentationTopicBody;
import org.radixware.schemas.xscml.MmlType;

/**
 * Class for content Dita Topic in language {@link mml}. Use in
 * {@link AdsDocTopicDef}
 *
 * @see Mml
 * @see AdsDocTopicDef
 * @author dkurlyanov
 */
public final class DocTopicBody extends RadixObject implements IFileSystemRadixObject {

    public static final String FILE_SIMPLE_PREFIX = "body";
    public static final String FILE_FULL_PREFIX = FILE_SIMPLE_PREFIX + EDefinitionIdPrefix.TECHNICAL_DOCUMENTATION_TOPIC.getValue();

    private AdsDocTopicDef topic;
    private File file;
    private EIsoLanguage language;
    private Mml content;
    private Mml oldContent;
    private int version;
    private boolean isCompleted = false;
    private boolean isOldCompleted = false;
    private int completedSourceVersion;
    private boolean isAgreed = false;
    private boolean isOldAgreed = false;
    private int agreedVersion;

    public DocTopicBody(final AdsDocTopicDef topic, EIsoLanguage language) {

        this.topic = topic;
        this.language = language;
        setContainer(topic);

        // file
        file = getFile(topic, language);
        if (!file.exists()) {
            save();
        }

        // load
        try {
            DocumentationTopicBody xDocTopicBody = DocumentationTopicBody.Factory.parse(file);
            content = oldContent = Mml.Factory.loadFrom(this, xDocTopicBody.getContent(), "Mml");
            content.setEditState(EEditState.NONE);
            version = xDocTopicBody.getVersion();
            isCompleted = isOldCompleted = xDocTopicBody.getIsCompleted();
            completedSourceVersion = xDocTopicBody.getCompletedSourceVersion();
            isAgreed = isOldAgreed = xDocTopicBody.getIsAgreed();
            agreedVersion = xDocTopicBody.getAgreedVersion();
        } catch (XmlException | IOException ex) {
            Logger.getLogger(DocTopicBody.class.getName()).log(Level.SEVERE, null, ex);
        }

        setEditState(RadixObject.EEditState.NONE);  // TODO: ???
    }

    @Override
    public void save() {

        if (content != null && content.equals(oldContent)) {
            version++;
            oldContent = content;
        }

        boolean needCreateLastAgreedFile = false;
        if (isOldAgreed != isAgreed) {
            agreedVersion = version;
            isOldAgreed = isAgreed;
            needCreateLastAgreedFile = isAgreed;
        }

        if (isCompleted != isOldCompleted) {
            DocTopicBody mainBody = topic.getMainDocLanguageBody();
            completedSourceVersion = mainBody.getVersion();
            isOldCompleted = isCompleted;
        }

        DocumentationTopicBody xDocTopicBody = DocumentationTopicBody.Factory.newInstance();
        MmlType xMml = MmlType.Factory.newInstance();

        if (content != null) {
            content.appendTo(xMml, AdsDefinition.ESaveMode.NORMAL);
            content.setEditState(EEditState.NONE);
        }

        xDocTopicBody.setContent(xMml);
        xDocTopicBody.setVersion(version);
        xDocTopicBody.setIsCompleted(isCompleted);
        xDocTopicBody.setCompletedSourceVersion(completedSourceVersion);
        xDocTopicBody.setIsAgreed(isAgreed);
        xDocTopicBody.setAgreedVersion(agreedVersion);

        try {
            XmlFormatter.save(xDocTopicBody, file);

            // TODO: !? сохраняем файл с последней согласованной версией
            if (needCreateLastAgreedFile) {
                String pathFile = topic.getFile().getParent() + File.separator + language.getValue() + File.separator
                        + "body" + topic.getId() + "_lastAgreedVersion" + ".xml";
                File fileLastAgreedVersion = new File(pathFile);
                XmlFormatter.save(xDocTopicBody, fileLastAgreedVersion);
            }
        } catch (IOException ex) {
            Logger.getLogger(DocTopicBody.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        content.visit(visitor, provider);
    }

    @Override
    public boolean delete() {

        if (!super.delete()) {
            return false;
        }

        final File file = getFile();
        if (file != null) {
            try {
                FileUtils.deleteFileExt(file);
            } catch (IOException cause) {
                throw new RadixObjectError("Unable to delete body file.", this, cause);
            }
        }

        return true;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    // set
    public void setIsCompleted(boolean isCompleted) {
        if (this.isCompleted != isCompleted) {
            this.isCompleted = isCompleted;
            setEditState(RadixObject.EEditState.MODIFIED);
        }
    }

    public void setIsAgreed(boolean isAgreed) {
        if (this.isAgreed != isAgreed) {
            this.isAgreed = isAgreed;
            setEditState(RadixObject.EEditState.MODIFIED);
        }
    }

    // get
    public EIsoLanguage getLanguage() {
        return language;
    }

    public Mml getContent() {
        return content;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public boolean getIsAgreed() {
        return isAgreed;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public IDirectoryRadixObject getOwnerDirectory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // file
    public static boolean existsFile(final AdsDocTopicDef topic, EIsoLanguage language) {
        return getFile(topic, language).exists();
    }

    public static File getFile(final AdsDocTopicDef topic, EIsoLanguage language) {
        return new File(getFilePath(topic, language));
    }

    public static File getParentDir(AdsModule modul, EIsoLanguage lang) {
        return new File(modul.getFile().getParentFile() + File.separator
                + AdsModule.SOURCES_DIR_NAME + File.separator
                + AdsModule.DOCUMENTATION_DIR_NAME + File.separator
                + lang.getValue());
    }

    public static String getFilePath(final AdsDocTopicDef topic, EIsoLanguage language) {
        return topic.getFile().getParent() + File.separator + language.getValue() + File.separator
                + "body" + topic.getId() + ".xml";
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public RadixObject getRadixObjectByFileName(String name) {
        if (name.equals(file.getName())) {
            return this;
        }
        return null;
    }

}
