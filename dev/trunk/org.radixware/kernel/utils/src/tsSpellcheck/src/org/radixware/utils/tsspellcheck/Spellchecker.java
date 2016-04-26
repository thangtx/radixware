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

package org.radixware.utils.tsspellcheck;

import com.trolltech.qt.QThread;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QFileInfo;
import com.trolltech.qt.core.QIODevice;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTranslator;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QProgressDialog;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.svg.QSvgRenderer;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;

import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.starter.utils.SystemTools;


public final class Spellchecker {

    public static class QtJambiNativeLibraryLoader {
        
        private QtJambiNativeLibraryLoader(){            
        }

        private final static String LAYER_URI = "org.radixware";

        public static void loadNativeLibrary(final String library) {
            if (!loadNativeLibraryInBranch(library)) {
                throw new AppError("Unable to load Qt library \'" + library + "\' from radix project branch");
            }
        }

        private static boolean loadNativeLibraryInBranch(final String lib) {
            ClassLoader loader = Spellchecker.class.getClassLoader();
            final String classFile = Spellchecker.class.getName().replace(".", "/") + ".class";
            final URL url = loader.getResource(classFile);
            if (url != null) {
                final String urlAsStr;
                try{
                    urlAsStr = URLDecoder.decode(url.getPath(),"UTF-8");
                }catch(UnsupportedEncodingException ex){
                    throw new AppError("Unable to find Qt library \'"+lib+"\' in radix project branch: " + String.valueOf(ex.getMessage()), ex);
                }
                final int endIndex = urlAsStr.lastIndexOf(LAYER_URI);
                if (endIndex > 0) {
                    final String branchPath;
                    if (url.getProtocol().equals("jar")) {
                        branchPath = urlAsStr.substring(5, endIndex + LAYER_URI.length());
                    } else {
                        branchPath = urlAsStr.substring(0, endIndex + LAYER_URI.length());
                    }
                    final String libraryPath = branchPath + "/" + SystemTools.getExplorerNativeLibLayerPath(lib);
                    final File libraryFile = new File(libraryPath);
                    if (libraryFile.exists()) {
                        Runtime.getRuntime().load(libraryFile.getAbsolutePath());
                        return true;
                    }
                }
            }
            return false;
        }
    }
    private final static String APP_TRANSLATIONS_FILE_NAME = "classpath:org/radixware/utils/tsspellcheck/spellchecker_%s.qm";
    private final static String QT_TRANSLATIONS_FILE_NAME = "classpath:org/radixware/utils/tsspellcheck/qt_%s.qm";
    private final static String SPELLCHECKER_RESOURCES = "classpath:org/radixware/utils/tsspellcheck/";
    private final static String OPEN_ICON_FILE_NAME = "open";
    private final static String REFRESH_ICON_FILE_NAME = "refresh";
    private final static String SPELLCHECKER_ICON_FILE_NAME = "validate";
    private final Ui_Spellchecker spellcheckerUi = new Ui_Spellchecker();
    private QDir translationFileDir;
    private final QTextCharFormat defaultCharFormat, selectedCharFormat;
    private String currentFileName;
    private final QMainWindow mainWindow;
    private final QProgressDialog progressDialog;

    public Spellchecker(final String fileName) {
        super();
        mainWindow = new QMainWindow();
        progressDialog = new SpellcheckProgressDialog(mainWindow);
        setupUi();
        defaultCharFormat = spellcheckerUi.teSource.currentCharFormat();
        selectedCharFormat = defaultCharFormat.clone();
        selectedCharFormat.setBackground(new QBrush(QColor.lightGray));
        spellchecking(fileName);
        mainWindow.show();
    }

    private void setupUi() {
        spellcheckerUi.setupUi(mainWindow);
        spellcheckerUi.action_Open.triggered.connect(this, "openFile()");
        spellcheckerUi.actionRefresh.triggered.connect(this, "revalidate()");
        spellcheckerUi.treeWidget.currentItemChanged.connect(this, "onTokenIndexChanged()");
        spellcheckerUi.lstSources.currentItemChanged.connect(this, "onLocationIndexChanged()");
        {
            final QIcon icon = loadSvgIcon(OPEN_ICON_FILE_NAME);
            if (icon != null) {
                spellcheckerUi.action_Open.setIcon(icon);
            }
        }
        {
            final QIcon icon = loadSvgIcon(REFRESH_ICON_FILE_NAME);
            if (icon != null) {
                spellcheckerUi.actionRefresh.setIcon(icon);
            }
        }
    }

    private static QIcon loadSvgIcon(final String fileName) {
        final QFile file = new QFile(SPELLCHECKER_RESOURCES + fileName + ".svg");
        if (file.exists() && file.open(QIODevice.OpenModeFlag.ReadOnly)) {
            final QByteArray content = file.readAll();
            if (!content.isEmpty() && !content.isNull()) {
                final QSvgRenderer renderer = new QSvgRenderer(content);
                try {
                    if (renderer.isValid()) {
                        final QSize iconSize = renderer.defaultSize();
                        final QPixmap pixmap = new QPixmap(iconSize);
                        pixmap.fill(QColor.transparent);
                        final QPainter painter = new QPainter(pixmap);
                        renderer.render(painter);
                        painter.end();
                        return new QIcon(pixmap);
                    }
                } finally {
                    renderer.dispose();
                }
            }
        }
        return null;
    }

    private static EIsoLanguage getSystemLanguage() {
        final String languageName = com.trolltech.qt.core.QLocale.system().language().name();
        try {
            return EIsoLanguage.valueOf(EIsoLanguage.class, languageName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            System.out.println("System language " + languageName + " is unknown - using english language");
            return EIsoLanguage.ENGLISH;
        }
    }

    private static void initTranslation(final EIsoLanguage language) {
        if (language != EIsoLanguage.ENGLISH) {
            final String appTranslationsFileName = String.format(APP_TRANSLATIONS_FILE_NAME, language.getValue());
            if (installTranslationFile(appTranslationsFileName)) {
                final String qtTranslationsFileName = String.format(QT_TRANSLATIONS_FILE_NAME, language.getValue());
                installTranslationFile(qtTranslationsFileName);
            }
        }
    }

    private static boolean installTranslationFile(final String fileName) {
        final QTranslator translator = new QTranslator();
        if (translator.load(fileName) && !translator.isEmpty()) {
            QApplication.installTranslator(translator);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unused")
    private void openFile() {
        final String fname = openTranslationFile(mainWindow, translationFileDir == null ? null : translationFileDir.absolutePath());
        if (fname != null && !fname.isEmpty()) {
            spellchecking(fname);
        }
    }

    @SuppressWarnings("unused")
    private void revalidate() {
        if (currentFileName != null && !currentFileName.isEmpty()) {
            spellchecking(currentFileName);
        }
    }

    private static class SpellcheckTask implements Runnable {

        private TranslationFileSpellchecker.SpellcheckResult result;
        private IOException exception;
        private boolean taskWasInterrupted;
        private final String fileName;
        private final QObject progress;

        public SpellcheckTask(final String fileNameToCheck, final QObject progressHandle) {
            fileName = fileNameToCheck;
            progress = progressHandle;
        }

        @Override
        public void run() {
            try {
                result = TranslationFileSpellchecker.getInstance().check(fileName, progress);
            } catch (IOException ex) {
                exception = ex;
            }
        }

        public TranslationFileSpellchecker.SpellcheckResult getResult() throws IOException {
            if (exception == null) {
                return result;
            }
            throw exception;
        }

        public void interrupt() {
            taskWasInterrupted = true;
            TranslationFileSpellchecker.getInstance().breakSpellcheck();
        }

        public boolean wasInterrupted() {
            return taskWasInterrupted;
        }
    }

    private void spellchecking(final String fileName) {
        final SpellcheckTask task = new SpellcheckTask(fileName, progressDialog);
        final QThread thread = new QThread(task);
        progressDialog.canceled.connect(task, "interrupt()");
        thread.finished.connect(progressDialog, "accept()");
        QApplication.invokeLater(new Runnable() {
            @Override
            public void run() {
                thread.start();
            }
        });
        progressDialog.exec();
        progressDialog.canceled.disconnect();


        if (task.wasInterrupted()) {
            return;
        }
        final TranslationFileSpellchecker.SpellcheckResult result;
        try {
            result = task.getResult();
            if (result == null) {
                return;
            }
        } catch (IOException exception) {
            final String exceptionMessage = QApplication.translate("Spellchecker", "Can't check file %s:\n%s");
            QMessageBox.critical(mainWindow, QApplication.translate("Spellchecker", "Can't check file"), String.format(exceptionMessage, fileName, exception.getMessage()));
            return;
        }
        final QFileInfo fileInfo = new QFileInfo(fileName);
        translationFileDir = fileInfo.absoluteDir();
        currentFileName = fileName;
        spellcheckerUi.actionRefresh.setEnabled(true);
        spellcheckerUi.treeWidget.clear();

        final List<TranslationToken> tokens = result.getTokens();
        QTreeWidgetItem contextItem = null;
        for (TranslationToken token : tokens) {
            if (contextItem == null || !contextItem.text(0).equals(token.getContextName())) {
                contextItem = createContextItem(token.getContextName());
            }
            token.addTreeItems(contextItem);
        }

        int spellcheckTokensCount = 0;
        for (int i = spellcheckerUi.treeWidget.topLevelItemCount() - 1; i >= 0; i--) {
            spellcheckTokensCount += spellcheckerUi.treeWidget.topLevelItem(i).childCount();
        }

        final String statusbarMessage = QApplication.translate("Spellchecker", "Total unknown words: %s");
        spellcheckerUi.statusbar.showMessage(String.format(statusbarMessage, spellcheckTokensCount));
        spellcheckerUi.treeWidget.expandAll();
        onTokenIndexChanged();
    }

    @SuppressWarnings("unused")
    private void onTokenIndexChanged() {
        final TranslationToken token;
        if (spellcheckerUi.treeWidget.currentItem() == null) {
            token = null;
        } else {
            token = (TranslationToken) spellcheckerUi.treeWidget.currentItem().data(0, Qt.ItemDataRole.UserRole);
        }
        spellcheckerUi.leSourceText.setText(token == null ? "" : token.getSourceText());
        spellcheckerUi.leTranslation.setText(token == null ? "" : token.getTranslation());
        spellcheckerUi.lstSources.clear();
        if (token != null) {
            final Collection<TranslationToken.Location> locations = token.getLocations();
            if (locations != null && !locations.isEmpty()) {
                QListWidgetItem item;
                for (TranslationToken.Location location : locations) {
                    item = new QListWidgetItem(location.toString());
                    spellcheckerUi.lstSources.addItem(item);
                    item.setData(Qt.ItemDataRole.UserRole, location);
                }
                spellcheckerUi.lstSources.setCurrentRow(0);
            }
        }
    }

    private QTreeWidgetItem createContextItem(final String context) {
        final QTreeWidgetItem item = new QTreeWidgetItem(spellcheckerUi.treeWidget);
        item.setText(0, context);
        return item;
    }

    @SuppressWarnings("unused")
    private void onLocationIndexChanged() {
        TranslationToken.Location location;
        spellcheckerUi.teSource.clear();
        if (spellcheckerUi.lstSources.currentItem() == null) {
            location = null;
        } else {
            location = (TranslationToken.Location) spellcheckerUi.lstSources.currentItem().data(Qt.ItemDataRole.UserRole);
        }

        if (location != null) {
            final QFileInfo fileInfo = new QFileInfo(translationFileDir, location.filePath);
            if (fileInfo.exists() && fileInfo.isReadable()) {
                final QFile file = new QFile(fileInfo.absoluteFilePath());
                if (file.open(QIODevice.OpenModeFlag.ReadOnly)) {
                    final String content = file.readAll().toString();
                    final String lines[] = content.split("\\n");
                    int position = 0;
                    final StringBuilder sourceText = new StringBuilder();
                    for (int line = 0; line < lines.length; line++) {
                        if (line > 0) {
                            sourceText.append("\n");
                        }
                        if (line == (location.lineNumber - 1)) {
                            spellcheckerUi.teSource.setCurrentCharFormat(defaultCharFormat);
                            spellcheckerUi.teSource.insertPlainText(sourceText.toString());
                            position = spellcheckerUi.teSource.textCursor().position();
                            spellcheckerUi.teSource.setCurrentCharFormat(selectedCharFormat);
                            spellcheckerUi.teSource.insertPlainText(lines[line]);
                            spellcheckerUi.teSource.setCurrentCharFormat(defaultCharFormat);
                            sourceText.setLength(0);
                        } else {
                            sourceText.append(lines[line]);
                        }
                    }
                    spellcheckerUi.teSource.insertPlainText(sourceText.toString());
                    final QTextCursor cursor = spellcheckerUi.teSource.textCursor();
                    cursor.setPosition(position, MoveMode.MoveAnchor);
                    spellcheckerUi.teSource.setTextCursor(cursor);
                    spellcheckerUi.teSource.centerCursor();

                }
            }
        }
    }

    private static String openTranslationFile(final QWidget parent, final String initialPath) {
        final String fileMask = QApplication.translate("Spellchecker", "Qt translation files (%s);;All files (%s)");
        final QFileDialog.Filter fileFilter = new QFileDialog.Filter(String.format(fileMask, "*.ts", "*.*"));
        final QFileDialog.Options options = new QFileDialog.Options(QFileDialog.Option.ReadOnly);
        return QFileDialog.getOpenFileName(parent, QApplication.translate("Spellchecker", "Open Qt translation file"),
                initialPath == null ? QDir.homePath() : initialPath, fileFilter, options);
    }

    public static void main(final String[] args) {
        System.setProperty("com.trolltech.qt.native-library-loader-override", "org.radixware.utils.tsspellcheck.Spellchecker$QtJambiNativeLibraryLoader");
        QApplication.initialize(args);
        initTranslation(getSystemLanguage());
        {
            final QIcon icon = loadSvgIcon(SPELLCHECKER_ICON_FILE_NAME);
            if (icon != null) {
                QApplication.setWindowIcon(icon);
            }
        }
        final String fileName = openTranslationFile(null, null);
        if (fileName != null && !fileName.isEmpty()) {
            final Spellchecker instance = new Spellchecker(fileName);
            QApplication.execStatic();
            System.exit(0);
        }
    }
}
