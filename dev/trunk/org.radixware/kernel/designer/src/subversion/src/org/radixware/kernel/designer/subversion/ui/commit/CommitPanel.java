/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2009 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * May 2013: adapted to work with RadixWare file-based entities by Compass Plus Limited.
 */

package org.radixware.kernel.designer.subversion.ui.commit;

//import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import javax.swing.*;
import static javax.swing.SwingConstants.SOUTH;
import static javax.swing.SwingConstants.WEST;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jdesktop.layout.LayoutStyle;
import static org.jdesktop.layout.LayoutStyle.RELATED;
import org.netbeans.modules.spellchecker.api.Spellchecker;
import org.netbeans.modules.subversion.SvnFileNode;
import org.netbeans.modules.subversion.SvnModuleConfig;
import org.netbeans.modules.subversion.ui.diff.MultiDiffPanel;
import org.netbeans.modules.subversion.ui.diff.Setup;
import org.netbeans.modules.subversion.util.SvnUtils;
import org.netbeans.modules.versioning.hooks.SvnHook;
import org.netbeans.modules.versioning.hooks.SvnHookContext;
import org.netbeans.modules.versioning.util.*;
import org.netbeans.modules.versioning.util.common.SectionButton;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.Mnemonics;
import org.openide.awt.TabbedPaneFactory;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.SaveCookie;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;
//import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;


/**
 *
 */
public class CommitPanel extends AutoResizingPanel implements PreferenceChangeListener, TableModelListener, ChangeListener {

    private final AutoResizingPanel basePanel = new AutoResizingPanel();
    static final Object EVENT_SETTINGS_CHANGED = new Object();
    private static final boolean DEFAULT_DISPLAY_FILES = true;
    private static final boolean DEFAULT_DISPLAY_HOOKS = false;

    final JLabel filesLabel = new JLabel();
    private final JPanel filesPanel = new JPanel(new GridLayout(1, 1));
    private SectionButton filesSectionButton = new SectionButton();
    private final JPanel filesSectionPanel = new JPanel();
    private SectionButton hooksSectionButton = new SectionButton();
    private final PlaceholderPanel hooksSectionPanel = new PlaceholderPanel();
    private final JLabel jLabel1 = new JLabel();
    private final JLabel jLabel2 = new JLabel();
    private final JScrollPane jScrollPane1 = new JScrollPane();
    private final JTextArea messageTextArea = new JTextArea();
    private final JLabel recentLink = new JLabel();
    private final JLabel templateLink = new JLabel();
    final PlaceholderPanel progressPanel = new PlaceholderPanel();

    private CommitTable commitTable;
    private Collection<SvnHook> hooks = Collections.emptyList();
    private SvnHookContext hookContext;
    private JTabbedPane tabbedPane;
    private HashMap<File, MultiDiffPanel> displayedDiffs = new HashMap<File, MultiDiffPanel>();
    private UndoRedoSupport um;
    
    private MicroCommitPanel microCommitPanel = new MicroCommitPanel();

    /** Creates new form CommitPanel */
    public CommitPanel() {
        initComponents();
        initInteraction();
    }

    void setHooks(Collection<SvnHook> hooks, SvnHookContext context) {
        if (hooks == null) {
            hooks = Collections.emptyList();
        }
        this.hooks = hooks;
        this.hookContext = context;
    }

    void setCommitTable(CommitTable commitTable) {
        this.commitTable = commitTable;
    }

    void setErrorLabel(String htmlErrorLabel) {
        jLabel2.setText(htmlErrorLabel);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        SvnModuleConfig.getDefault().getPreferences().addPreferenceChangeListener(this);
        commitTable.getTableModel().addTableModelListener(this);
        listenerSupport.fireVersioningEvent(EVENT_SETTINGS_CHANGED);
        initCollapsibleSections();
        TemplateSelector ts = new TemplateSelector(SvnModuleConfig.getDefault().getPreferences());
        if (ts.isAutofill()) {
            messageTextArea.setText(ts.getTemplate());
        } else {            
//            String lastCommitMessage = SvnModuleConfig.getDefault().getLastCanceledCommitMessage();
//            if (lastCommitMessage.isEmpty() && new StringSelector.RecentMessageSelector(SvnModuleConfig.getDefault().getPreferences()).isAutoFill()) {
//                List<String> messages = Utils.getStringList(SvnModuleConfig.getDefault().getPreferences(), CommitAction.RECENT_COMMIT_MESSAGES);
//                if (messages.size() > 0) {
//                    lastCommitMessage = messages.get(0);
//                }
//            }
            String lastCommitMessage = MicroCommitPanel.getLastCommitMessage();
            messageTextArea.setText(lastCommitMessage);
        }
        messageTextArea.selectAll();
        um = UndoRedoSupport.register(messageTextArea);
    }

    @Override
    public void removeNotify() {
        commitTable.getTableModel().removeTableModelListener(this);
        SvnModuleConfig.getDefault().getPreferences().removePreferenceChangeListener(this);
        if (um != null) {
            um.unregister();
            um = null;
        }
        super.removeNotify();
    }

    private void initCollapsibleSections() {
        initSectionButton(filesSectionButton, filesSectionPanel,
                          "initFilesPanel",                             //NOI18N
                          DEFAULT_DISPLAY_FILES);
        if(!hooks.isEmpty()) {
            Mnemonics.setLocalizedText(hooksSectionButton, (hooks.size() == 1)
                                        ? hooks.iterator().next().getDisplayName()
                                           : getMessage("LBL_Advanced")); // NOI18N                 
            initSectionButton(hooksSectionButton, hooksSectionPanel,
                              "initHooksPanel",                         //NOI18N
                              DEFAULT_DISPLAY_HOOKS);
        } else {
            hooksSectionButton.setVisible(false);
        }
    }

    private void initSectionButton(final SectionButton button,
                                   final JPanel panel,
                                   final String initPanelMethodName,
                                   final boolean defaultSectionDisplayed) {
        if (defaultSectionDisplayed) {
            displaySection(panel, initPanelMethodName);
        } else {
            hideSection(panel);
        }
        button.setSelected(defaultSectionDisplayed);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (panel.isVisible()) {
                    hideSection(panel);
                } else {
                    displaySection(panel, initPanelMethodName);
                }
            }
        });
    }

    private void displaySection(Container sectionPanel,
                                String initPanelMethodName) {
        if (sectionPanel.getComponentCount() == 0) {
            invokeInitPanelMethod(initPanelMethodName);
        }
        sectionPanel.setVisible(true);
        enlargeVerticallyAsNecessary();
    }

    private void hideSection(JPanel sectionPanel) {
        sectionPanel.setVisible(false);
    }

    private void invokeInitPanelMethod(String methodName) {
        try {
            getClass().getDeclaredMethod(methodName).invoke(this);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Do NOT remove this method. It may seem unused but is in fact called from {@link #invokeInitPanelMethod(java.lang.String)}
     * through reflection.
     */
    private void initFilesPanel() {

        /* this method is called using reflection from 'invokeInitPanelMethod()' */

        filesPanel.add(commitTable.getComponent());
        filesPanel.setPreferredSize(new Dimension(0, 2 * messageTextArea.getPreferredSize().height));

        filesSectionPanel.setLayout(new BoxLayout(filesSectionPanel, Y_AXIS));
        filesSectionPanel.add(filesLabel);
        filesSectionPanel.add(makeVerticalStrut(filesLabel, filesPanel, RELATED));
        filesSectionPanel.add(filesPanel);

        filesLabel.setAlignmentX(LEFT_ALIGNMENT);
        filesPanel.setAlignmentX(LEFT_ALIGNMENT);
    }

    /**
     * Do NOT remove this method. It may seem unused but is in fact called from {@link #invokeInitPanelMethod(java.lang.String)}
     * through reflection.
     */
    private void initHooksPanel() {

        /* this method is called using reflection from 'invokeInitPanelMethod()' */

        assert !hooks.isEmpty();

        if (hooks.size() == 1) {
            hooksSectionPanel.add(hooks.iterator().next().createComponent(hookContext));
        } else {
            JTabbedPane hooksTabbedPane = new JTabbedPane();
            for (SvnHook hook : hooks) {
                hooksTabbedPane.add(hook.createComponent(hookContext),
                                    hook.getDisplayName().replaceAll("\\&", ""));
            }
            hooksSectionPanel.add(hooksTabbedPane);
        }
    }

String getCommitMessage() {   
        return  SvnUtils.fixLineEndings(messageTextArea.getText());
    }




    private void onBrowseRecentMessages() {
//        StringSelector.RecentMessageSelector selector = new StringSelector.RecentMessageSelector(SvnModuleConfig.getDefault().getPreferences());
//        String message = selector.getRecentMessage(getMessage("CTL_CommitForm_RecentTitle"),
//                                               getMessage("CTL_CommitForm_RecentPrompt"),
//            Utils.getStringList(SvnModuleConfig.getDefault().getPreferences(), CommitAction.RECENT_COMMIT_MESSAGES));
        String message = MicroCommitPanel.getLastCommitMessage();
        if (message != null) {
            messageTextArea.replaceSelection(message);
        }
    }

    private void onTemplate() {
        TemplateSelector ts = new TemplateSelector(SvnModuleConfig.getDefault().getPreferences());
        if(ts.show()) {
            messageTextArea.setText(ts.getTemplate());
        }
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().startsWith(SvnModuleConfig.PROP_COMMIT_EXCLUSIONS)) {
            Runnable inAWT = new Runnable() {
                @Override
                public void run() {
                    commitTable.dataChanged();
                    listenerSupport.fireVersioningEvent(EVENT_SETTINGS_CHANGED);
                }
            };
            // this can be called from a background thread - e.g. change of exclusion status in Versioning view
            if (EventQueue.isDispatchThread()) {
                inAWT.run();
            } else {
                EventQueue.invokeLater(inAWT);
            }
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        listenerSupport.fireVersioningEvent(EVENT_SETTINGS_CHANGED);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    // <editor-fold defaultstate="collapsed" desc="UI Layout Code">
//    private static final String CFG = "RadixSubversioneConfiguration";
//    private static final String COMPONENT_CFG_NAME2 = "Component_cfg";
    
    
    
    
    private void initComponents() {

        jLabel1.setLabelFor(messageTextArea);
        Mnemonics.setLocalizedText(jLabel1, getMessage("CTL_CommitForm_Message")); // NOI18N

        recentLink.setIcon(new ImageIcon(getClass().getResource("/org/netbeans/modules/subversion/resources/icons/recent_messages.png"))); // NOI18N
        recentLink.setToolTipText(getMessage("CTL_CommitForm_RecentMessages")); // NOI18N

        templateLink.setIcon(new ImageIcon(getClass().getResource("/org/netbeans/modules/subversion/resources/icons/load_template.png"))); // NOI18N
        templateLink.setToolTipText(getMessage("CTL_CommitForm_LoadTemplate")); // NOI18N

        messageTextArea.setColumns(70);    //this determines the preferred width of the whole dialog
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(4);
        messageTextArea.setTabSize(4);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setMinimumSize(new Dimension(100, 18));
        jScrollPane1.setViewportView(messageTextArea);
        messageTextArea.getAccessibleContext().setAccessibleName(getMessage("ACSN_CommitForm_Message")); // NOI18N
        messageTextArea.getAccessibleContext().setAccessibleDescription(getMessage("ACSD_CommitForm_Message")); // NOI18N

        Mnemonics.setLocalizedText(filesSectionButton, getMessage("LBL_CommitDialog_FilesToCommit")); // NOI18N
        Mnemonics.setLocalizedText(filesLabel, getMessage("CTL_CommitForm_FilesToCommit")); // NOI18N

        Mnemonics.setLocalizedText(hooksSectionButton, getMessage("LBL_Advanced")); // NOI18N
        
        JPanel topPanel = new VerticallyNonResizingPanel();
        topPanel.setLayout(new BoxLayout(topPanel, X_AXIS));
        topPanel.add(jLabel1);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(recentLink);
        topPanel.add(makeHorizontalStrut(recentLink, templateLink, RELATED));
        topPanel.add(templateLink);
        jLabel1.setAlignmentY(BOTTOM_ALIGNMENT);
        recentLink.setAlignmentY(BOTTOM_ALIGNMENT);
        templateLink.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel bottomPanel = new VerticallyNonResizingPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, X_AXIS));
        bottomPanel.add(progressPanel);
        progressPanel.setAlignmentY(CENTER_ALIGNMENT);

        basePanel.setLayout(new BoxLayout(basePanel, Y_AXIS));
        
        basePanel.add(microCommitPanel);
        microCommitPanel.setAlignmentX(LEFT_ALIGNMENT); 
        
        basePanel.add(topPanel);
        basePanel.add(makeVerticalStrut(jLabel1, jScrollPane1, RELATED));
        basePanel.add(jScrollPane1);
        basePanel.add(makeVerticalStrut(jScrollPane1, filesSectionButton, RELATED));
        filesSectionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, filesSectionButton.getMaximumSize().height));
        basePanel.add(filesSectionButton);
        basePanel.add(makeVerticalStrut(filesSectionButton, filesSectionPanel, RELATED));
        basePanel.add(filesSectionPanel);
        basePanel.add(makeVerticalStrut(filesSectionPanel, hooksSectionButton, RELATED));
        hooksSectionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, hooksSectionButton.getMaximumSize().height));
        basePanel.add(hooksSectionButton);
        basePanel.add(makeVerticalStrut(hooksSectionButton, hooksSectionPanel, RELATED));
        basePanel.add(hooksSectionPanel);
        basePanel.add(makeVerticalStrut(hooksSectionPanel, jLabel2, RELATED));
        basePanel.add(jLabel2);
        basePanel.add(makeVerticalStrut(hooksSectionPanel, bottomPanel, RELATED));
        basePanel.add(bottomPanel);
        setLayout(new BoxLayout(this, Y_AXIS));
        add(basePanel);
        topPanel.setAlignmentX(LEFT_ALIGNMENT);
        //topP.setAlignmentX(LEFT_ALIGNMENT);       //!!!!!!!!!
        
        jScrollPane1.setAlignmentX(LEFT_ALIGNMENT);
        filesSectionButton.setAlignmentX(LEFT_ALIGNMENT);
        filesSectionPanel.setAlignmentX(LEFT_ALIGNMENT);
        hooksSectionButton.setAlignmentX(LEFT_ALIGNMENT);
        hooksSectionPanel.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.setAlignmentX(LEFT_ALIGNMENT);

        basePanel.setBorder(createEmptyBorder(26,                       //top
                                    getContainerGap(WEST),    //left
                                    0,                        //bottom
                                    15));                     //right

        getAccessibleContext().setAccessibleName(getMessage("ACSN_CommitDialog")); // NOI18N
        getAccessibleContext().setAccessibleDescription(getMessage("ACSD_CommitDialog")); // NOI18N
        

        
    }// </editor-fold>

    private void initInteraction() {
        recentLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        recentLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onBrowseRecentMessages();
            }
        });
        templateLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        templateLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onTemplate();
            }
        });
        Spellchecker.register (messageTextArea);
    }

    private Component makeVerticalStrut(JComponent compA,
                                        JComponent compB,
                                        int relatedUnrelated) {
        int height = LayoutStyle.getSharedInstance().getPreferredGap(
                            compA,
                            compB,
                            relatedUnrelated,
                            SOUTH,
                            this);
        return Box.createVerticalStrut(height);
    }

    private Component makeHorizontalStrut(JComponent compA,
                                          JComponent compB,
                                          int relatedUnrelated) {
        int width = LayoutStyle.getSharedInstance().getPreferredGap(
                            compA,
                            compB,
                            relatedUnrelated,
                            WEST,
                            this);
        return Box.createHorizontalStrut(width);
    }

    private int getContainerGap(int direction) {
        return LayoutStyle.getSharedInstance().getContainerGap(this,
                                                               direction,
                                                               null);
    }

    private static String getMessage(String msgKey) {
        return NbBundle.getMessage(CommitPanel.class, msgKey);
    }

    ListenersSupport listenerSupport = new ListenersSupport(this);
    public void addVersioningListener(VersioningListener listener) {
        listenerSupport.addListener(listener);
    }

    public void removeVersioningListener(VersioningListener listener) {
        listenerSupport.removeListener(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == tabbedPane && tabbedPane.getSelectedComponent() == basePanel) {
            commitTable.setModifiedFiles(new HashSet<File>(getModifiedFiles().keySet()));
        }
    }

    void openDiff (SvnFileNode[] nodes) {
        for (SvnFileNode node : nodes) {
            if (tabbedPane == null) {
                initializeTabs();
            }
            File file = node.getFile();
            MultiDiffPanel panel = displayedDiffs.get(file);
            if (panel == null) {
                panel = new MultiDiffPanel(file, Setup.REVISION_BASE, Setup.REVISION_CURRENT, false); // switch the last parameter to true if editable diff works poorly
                displayedDiffs.put(file, panel);
                tabbedPane.addTab(file.getName(), panel);
            } else {
                if (tabbedPane.indexOfComponent(panel) < 0){
                    tabbedPane.addTab(file.getName(), panel);
                }
            }
            tabbedPane.setSelectedComponent(panel);
        }
        revalidate();
        repaint();
    }

    /**
     * Returns save cookies available for files in the commit table
     * @return
     */
    SaveCookie[] getSaveCookies() {
        return getModifiedFiles().values().toArray(new SaveCookie[0]);
    }

    /**
     * Returns editor cookies available for modified and not open files in the commit table
     * @return
     */
    EditorCookie[] getEditorCookies() {
        LinkedList<EditorCookie> allCookies = new LinkedList<EditorCookie>();
        for (Map.Entry<File, MultiDiffPanel> e : displayedDiffs.entrySet()) {
            EditorCookie[] cookies = e.getValue().getEditorCookies(true);
            if (cookies.length > 0) {
                allCookies.add(cookies[0]);
            }
        }
        return allCookies.toArray(new EditorCookie[allCookies.size()]);
    }

    /**
     * Returns true if trying to commit from the commit tab or the user confirmed his action
     * @return
     */
    boolean canCommit() {
        boolean result = true;
        if (tabbedPane != null && tabbedPane.getSelectedComponent() != basePanel) {
            NotifyDescriptor nd = new NotifyDescriptor(NbBundle.getMessage(CommitPanel.class, "MSG_CommitDialog_CommitFromDiff"), //NOI18N
                    NbBundle.getMessage(CommitPanel.class, "LBL_CommitDialog_CommitFromDiff"), //NOI18N
                    NotifyDescriptor.YES_NO_OPTION, NotifyDescriptor.QUESTION_MESSAGE, null, NotifyDescriptor.YES_OPTION);
            result = NotifyDescriptor.YES_OPTION == DialogDisplayer.getDefault().notify(nd);
        }
        return result;
    }

   String getCommitMessageEx(String message) {
       return microCommitPanel.getCommitMessageEx(message);
   }
   
   void saveConfigurationOptions(){
       microCommitPanel.saveConfigurationOptions();
   }

    private void initializeTabs () {
         tabbedPane = TabbedPaneFactory.createCloseButtonTabbedPane();
         tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
         basePanel.putClientProperty(TabbedPaneFactory.NO_CLOSE_BUTTON, Boolean.TRUE);
        tabbedPane.addPropertyChangeListener(TabbedPaneFactory.PROP_CLOSE, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                JTabbedPane pane = (JTabbedPane) evt.getSource();
                int sel = pane.getSelectedIndex();
                if (sel > 0){
                    Component component = pane.getComponentAt(sel);
                    if (component instanceof MultiDiffPanel){
                        pane.removeTabAt(sel); 
                    }
                }
            }
        });
         tabbedPane.addTab(NbBundle.getMessage(CommitPanel.class, "CTL_CommitDialog_Tab_Commit"), basePanel); //NOI18N
         tabbedPane.setPreferredSize(basePanel.getPreferredSize());
         add(tabbedPane);
         tabbedPane.addChangeListener(this);
    }
    
    private HashMap<File, SaveCookie> getModifiedFiles () {
        HashMap<File, SaveCookie> modifiedFiles = new HashMap<File, SaveCookie>();
        for (Map.Entry<File, MultiDiffPanel> e : displayedDiffs.entrySet()) {
            SaveCookie[] cookies = e.getValue().getSaveCookies(false);
            if (cookies.length > 0) {
                modifiedFiles.put(e.getKey(), cookies[0]);
            }
        }
        return modifiedFiles;
    }
}
