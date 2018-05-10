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
package org.radixware.kernel.utils.traceview.window;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.radixware.kernel.utils.traceview.TraceEvent.IndexedDate;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.DATE_FORMAT_PLAIN_TEXT;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EEventColumn;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_CURR_DIR;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_FILTER_PANEL_SCROLL_BAR_VALUE_IN_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_FIND_TEXT_IN_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_FIRST_TIME_IN_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_INVISIBLE_COLUMNS_IN_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_LAST_SELECTED_TAB;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_LAST_TIME_IN_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SAVE_PROP_TABLE_SCROLL_BAR_VALUE_IN_FILE_X;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.SIMPLE_SEPARATOR;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.X_FILE_SAVE_PROP_BOOKMARK_LIST_Y;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.X_FILE_SAVE_PROP_SELECTED_CONTEXT_Y;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.X_FILE_SAVE_PROP_SELECTED_SEVERITY_Y;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.X_FILE_SAVE_PROP_SELECTED_SOURCE_Y;
import static org.radixware.kernel.utils.traceview.TraceViewSettings.X_FILE_SAVE_PROP_VISIBLE_CONTEXT_Y;
import org.radixware.kernel.utils.traceview.utils.ContextForParsing;
import org.radixware.kernel.utils.traceview.utils.FileParser;
import org.radixware.kernel.utils.traceview.utils.ParseArguments;
import org.radixware.kernel.utils.traceview.utils.TraceViewUtils;
import static org.radixware.kernel.utils.traceview.utils.TraceViewUtils.getLogMessageByIndex;

public class WindowMode {

    private static final Logger logger = Logger.getLogger(WindowMode.class.getName());
    private final JFrame window = new JFrame("Trace Viewer");
    private final Map<File, TablePanel> filePanes = new HashMap<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final ExecutorService threadPool;
    private File selectedFile = null;
    public final Map<Integer, Boolean> columnsState = new HashMap<>();
    private String currDir = System.getProperty("user.home");
    private static final String APPDATA_PATH = (System.getProperty("os.name").toLowerCase().contains("win") ? System.getenv("APPDATA") : System.getProperty("user.home")) + File.separatorChar;

    public WindowMode(ParseArguments arguments) {
        threadPool = Executors.newFixedThreadPool(arguments.getThreadPoolSize() != -1 ? arguments.getThreadPoolSize() : Runtime.getRuntime().availableProcessors());
        for (EEventColumn column : EEventColumn.values()) {
            columnsState.put(column.getIndex(), true);
        }
        createWindow(loadParameters(arguments.getArgs()));
    }

    private boolean openNewFile(File file, TabSettings settings) {
        TablePanel panel = new TablePanel(file, settings == null ? new TabSettings() : settings, this);
        try {
            ContextForParsing context = new ContextForParsing(panel, panel.getFile(), false, true);
            filePanes.put(file, panel);
            tabbedPane.addTab(file.getName(), panel);
            tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new ButtonTabComponent(tabbedPane, filePanes));
            threadPool.submit(new FileParser(panel, context));
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage(), "Parse File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void setColumnState(int index, boolean state) {
        if (columnsState.containsKey(index)) {
            columnsState.put(index, state);
        } else {
            throw new IllegalArgumentException(new StringBuilder(TabSettings.class.getSimpleName()).append(": Not supported value: [").append(String.valueOf(index)).append("]").toString());
        }
    }

    public boolean getColumnState(int index) {
        if (columnsState.containsKey(index)) {
            return columnsState.get(index);
        }
        throw new IllegalArgumentException(new StringBuilder(TabSettings.class.getSimpleName()).append(": Not supported value: [").append(String.valueOf(index)).append("]").toString());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenu label = new JMenu("Navigate");
        JMenuItem menuOpen = new JMenuItem("Open file", KeyEvent.VK_O);
        JMenuItem menuSave = new JMenuItem("Save file", KeyEvent.VK_S);
        JMenuItem labelNext = new JMenuItem("Next Bookmark");
        JMenuItem labelPrevious = new JMenuItem("Previous Bookmark");
        JMenuItem labelAdd = new JMenuItem("Toggle Bookmark");
        menuBar.add(menu);
        menuBar.add(label);
        label.add(labelAdd);
        label.add(labelNext);
        label.add(labelPrevious);
        menu.add(menuOpen);
        menu.add(menuSave);
        menuSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        labelAdd.setAccelerator(KeyStroke.getKeyStroke('M', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        labelNext.setAccelerator(KeyStroke.getKeyStroke(',', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        labelPrevious.setAccelerator(KeyStroke.getKeyStroke('.', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooserWithOverwriting();
                FileNameExtensionFilter logFiles = new FileNameExtensionFilter("Log files (*.log)", "log");
                fc.addChoosableFileFilter(logFiles);
                fc.setFileFilter(logFiles);
                TablePanel tb = (TablePanel) tabbedPane.getSelectedComponent();
                JTable table = tb.getTable();
                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    if (selectedFile.exists()) {
                        selectedFile.delete();
                    }
                    try {
                        selectedFile.createNewFile();
                        saveMessagesInFile(selectedFile, table);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error on creating file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        menuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser(currDir);
                FileNameExtensionFilter traceFiles = new FileNameExtensionFilter("Trace files(*.log,*.xml)", "log", "xml");
                fileopen.addChoosableFileFilter(traceFiles);
                fileopen.setFileFilter(traceFiles);
                if (fileopen.showDialog(null, "Open") == JFileChooser.APPROVE_OPTION) {
                    currDir = fileopen.getCurrentDirectory().toString();
                    File selectedfile = fileopen.getSelectedFile();
                    if (!selectedfile.exists()) {
                        JOptionPane.showMessageDialog(window, new StringBuilder("File [").append(selectedfile.getAbsolutePath()).append("] not exists!"), "Open File Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (!filePanes.containsKey(selectedfile)) {
                            if (openNewFile(selectedfile, null)) {
                                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                            }
                        } else {
                            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                                if (((TablePanel) tabbedPane.getComponentAt(i)).getFile().equals(selectedfile)) {
                                    tabbedPane.setSelectedIndex(i);
                                }
                            }
                        }
                    }
                }
            }
        });
        labelAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((TablePanel) tabbedPane.getSelectedComponent()).addLabel();
            }
        });
        
        labelNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((TablePanel) tabbedPane.getSelectedComponent()).moveToNextLabel();
            }
        });
        
        labelPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((TablePanel) tabbedPane.getSelectedComponent()).moveToPreviousLabel();
            }
        });
        
        return menuBar;
    }

    private void saveMessagesInFile(File file, JTable table) throws IOException {
        try (FileWriter fwr = new FileWriter(file); BufferedWriter bwr = new BufferedWriter(fwr)) {
            for (int i = 0; i < table.getRowCount(); i++) {
                bwr.write(getLogMessageByIndex(i, table) + System.lineSeparator() + System.lineSeparator());
            }
        }
    }

    private void createWindow(Map<File, TabSettings> filesForLoading) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new java.awt.Dimension(1600, 800));
        window.setMinimumSize(new java.awt.Dimension(1000, 170));
        window.setLayout(new BorderLayout());

        for (Map.Entry<File, TabSettings> tab : filesForLoading.entrySet()) {
            File file = tab.getKey();
            if (file.exists() && !filePanes.containsKey(file)) {
                openNewFile(file, tab.getValue());
                if (file.equals(selectedFile)) {
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                }
            }
        }

        if (selectedFile == null) {
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveParametersChanges(tabbedPane, filePanes);
            }
        });

        window.add(tabbedPane, BorderLayout.CENTER);
        window.setJMenuBar(createMenuBar());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    //После 28.04.2018 можно (и нужно) удалить.  
    private Map<File, TabSettings> loadOldSettings(Properties props, String args) {
        Map<File, TabSettings> filesForLoading = new HashMap<>();

        currDir = props.getProperty("CurrDir");

        String filePath;
        for (int i = 0; (filePath = props.getProperty("File" + i)) != null; i++) {
            File file = new File(filePath);
            if (!filesForLoading.containsKey(file) && file.exists()) {
                filesForLoading.put(file, new TabSettings());
            }
        }

        if ((filePath = props.getProperty("LastTab")) != null) {
            selectedFile = new File(filePath);
        }

        if (args != null) {
            for (String pathtoFile : args.split(";")) {
                File file = new File(pathtoFile);
                if (file.exists()) {
                    if (!filesForLoading.containsKey(file)) {
                        filesForLoading.put(file, new TabSettings());
                        selectedFile = file;
                    }
                } else {
                    logger.log(Level.SEVERE, "File not found --> {0}", file.getAbsolutePath());
                }
            }
        }

        if (!filesForLoading.containsKey(selectedFile)) {
            selectedFile = null;
        }

        return filesForLoading;
    }

    private TabSettings loadTabSettings(Properties props, int index) {
        TabSettings tabSettings = new TabSettings();


        try {
            tabSettings.setFilterPanelScrollBarValue(Integer.parseInt(props.getProperty(SAVE_PROP_FILTER_PANEL_SCROLL_BAR_VALUE_IN_FILE_X + index)));
        } catch (NumberFormatException ex) {
            //ignore
        }
        try {
            tabSettings.setTableScrollBarValue(Integer.parseInt(props.getProperty(SAVE_PROP_TABLE_SCROLL_BAR_VALUE_IN_FILE_X + index)));
        } catch (NumberFormatException ex) {
            //ignore
        }
        try {
            tabSettings.setFirstTime(Long.parseLong(props.getProperty(SAVE_PROP_FIRST_TIME_IN_FILE_X + index)));
        } catch (NumberFormatException ex) {
            //ignore
        }
        try {
            tabSettings.setLastTime(Long.parseLong(props.getProperty(SAVE_PROP_LAST_TIME_IN_FILE_X + index)));
        } catch (NumberFormatException ex) {
            //ignore
        }

        String text;
        for (int i = 0; (text = props.getProperty(index + X_FILE_SAVE_PROP_VISIBLE_CONTEXT_Y + i)) != null; i++) {
            tabSettings.addNewContext(text);
        }
        for (int i = 0; (text = props.getProperty(index + X_FILE_SAVE_PROP_SELECTED_CONTEXT_Y + i)) != null; i++) {
            tabSettings.addContextSelectedItem(text);
        }
        for (int i = 0; (text = props.getProperty(index + X_FILE_SAVE_PROP_SELECTED_SOURCE_Y + i)) != null; i++) {
            tabSettings.addSourceSelectedItem(text);
        }
        for (int i = 0; (text = props.getProperty(index + X_FILE_SAVE_PROP_BOOKMARK_LIST_Y + i)) != null; i++) {
            tabSettings.addNewBookmark(text);
            tabSettings.listOfLabels.add(textToData(text));
            
        }
        for (int i = 0; (text = props.getProperty(index + X_FILE_SAVE_PROP_SELECTED_SEVERITY_Y + i)) != null; i++) {
            tabSettings.addSeveritySelectedItem(text);
        }

        if ((text = props.getProperty(SAVE_PROP_FIND_TEXT_IN_FILE_X + index)) != null) {
            tabSettings.setFindText(text);
        }

        return tabSettings;
    }

    private void saveTabSettings(Properties props, int index, TabSettings tabSettings) {
        StringBuilder invisibleColumns = new StringBuilder();
        for (EEventColumn ev : EEventColumn.values()) {
            if (!getColumnState(ev.getIndex())) {
                if (invisibleColumns.length() != 0) {
                    invisibleColumns.append(SIMPLE_SEPARATOR);
                }
                invisibleColumns.append(ev.getIndex());
            }
        }
        if (invisibleColumns.length() != 0) {
            props.setProperty(SAVE_PROP_INVISIBLE_COLUMNS_IN_FILE_X, invisibleColumns.toString());
        }
        props.setProperty(SAVE_PROP_FILTER_PANEL_SCROLL_BAR_VALUE_IN_FILE_X + index, String.valueOf(tabSettings.getFilterPanelScrollBarValue()));
        props.setProperty(SAVE_PROP_TABLE_SCROLL_BAR_VALUE_IN_FILE_X + index, String.valueOf(tabSettings.getTableScrollBarValue()));
        props.setProperty(SAVE_PROP_FIRST_TIME_IN_FILE_X + index, String.valueOf(tabSettings.getFirstTime()));
        props.setProperty(SAVE_PROP_LAST_TIME_IN_FILE_X + index, String.valueOf(tabSettings.getLastTime()));
        props.setProperty(SAVE_PROP_FIND_TEXT_IN_FILE_X + index, tabSettings.getFindText());

        int count = 0;
        for (String ctx : tabSettings.getContextList()) {
            props.setProperty(index + X_FILE_SAVE_PROP_VISIBLE_CONTEXT_Y + count++, ctx);
        }
        count = 0;
        for (String ctx : tabSettings.getContextSelectedList()) {
            props.setProperty(index + X_FILE_SAVE_PROP_SELECTED_CONTEXT_Y + count++, ctx);
        }
        count = 0;
        for (String sour : tabSettings.getSourceSelectedList()) {
            props.setProperty(index + X_FILE_SAVE_PROP_SELECTED_SOURCE_Y + count++, sour);
        }
        count = 0;
        for (String sev : tabSettings.getSeveritySelectedList()) {
            props.setProperty(index + X_FILE_SAVE_PROP_SELECTED_SEVERITY_Y + count++, sev);
        }
        count = 0;
        for (String bm : tabSettings.getBookmarkList()) {
            props.setProperty(index + X_FILE_SAVE_PROP_BOOKMARK_LIST_Y + count++, bm);
        }
    }
    
    private static IndexedDate textToData(String text) {
        IndexedDate indxDate = null;
        String dateS = "";
        Date date;
        int index = 0;
        try {
            for (int i = 0; i < text.length(); i++) {
                if (i < 21) {
                    dateS += text.charAt(i);
                } else if (i > 21) {
                    index = Integer.parseInt(text.substring(i, text.length()));
                    break;
                }
            }
            date = TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().parse(dateS);
            indxDate = new IndexedDate(date, index);
        } catch (ParseException e) {
        }
        return indxDate;
    }

    private Map<File, TabSettings> loadParameters(String args) {
        Map<File, TabSettings> filesForLoading = new HashMap<>();

        Properties props = new Properties();

        try (InputStream is = new FileInputStream(new File(new StringBuilder(APPDATA_PATH).append(".traceview").append(File.separatorChar).append("trace.properties").toString()))) {
            props.load(is);
        } catch (IOException | NullPointerException ex) {
            return filesForLoading;
        }

        //После 28.04.2018 можно (и нужно) удалить это условие.
        if (props.getProperty("File0") != null) {
            return loadOldSettings(props, args);
        }

        currDir = props.getProperty(SAVE_PROP_CURR_DIR);

        String filePath;
        for (int i = 0; (filePath = props.getProperty(SAVE_PROP_FILE_X + i)) != null; i++) {
            File file = new File(filePath);
            if (!filesForLoading.containsKey(file) && file.exists()) {
                filesForLoading.put(file, loadTabSettings(props, i));
            }
        }
        String invisibleColumns = props.getProperty(SAVE_PROP_INVISIBLE_COLUMNS_IN_FILE_X);
        if (invisibleColumns != null) {
            for (String i : invisibleColumns.split(SIMPLE_SEPARATOR)) {
                setColumnState(Integer.parseInt(i), false);
            }
        }

        if ((filePath = props.getProperty(SAVE_PROP_LAST_SELECTED_TAB)) != null) {
            selectedFile = new File(filePath);
        }

        if (args != null) {
            for (String pathtoFile : args.split(";")) {
                File file = new File(pathtoFile);
                if (file.exists()) {
                    if (!filesForLoading.containsKey(file)) {
                        filesForLoading.put(file, new TabSettings());
                        selectedFile = file;
                    }
                } else {
                    logger.log(Level.SEVERE, "File not found --> {0}", file.getAbsolutePath());
                }
            }
        }

        if (!filesForLoading.containsKey(selectedFile)) {
            selectedFile = null;
        }

        return filesForLoading;
    }

    private void saveParametersChanges(JTabbedPane tabbedPane, Map<File, TablePanel> filePanes) {
        Properties props = new Properties();
        props.setProperty(SAVE_PROP_CURR_DIR, currDir);

        if (!filePanes.isEmpty()) {
            props.setProperty(SAVE_PROP_LAST_SELECTED_TAB, ((TablePanel) tabbedPane.getSelectedComponent()).getFile().getAbsolutePath());
        }

        int i = 0;
        for (Entry<File, TablePanel> tab : filePanes.entrySet()) {
            props.setProperty(SAVE_PROP_FILE_X + i, tab.getKey().getAbsolutePath());
            saveTabSettings(props, i, tab.getValue().getTabSettings());
            i++;
        }

        StringBuilder path = new StringBuilder(APPDATA_PATH).append(".traceview");
        new File(path.toString()).mkdir();
        File propertyFile = new File(path.append(File.separatorChar).append("trace.properties").toString());
        try (OutputStream out = new FileOutputStream(propertyFile)) {
            propertyFile.createNewFile();
            props.store(out, "Parameters of traceview");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to store properties of TraceView", ex);
        }
    }
}
