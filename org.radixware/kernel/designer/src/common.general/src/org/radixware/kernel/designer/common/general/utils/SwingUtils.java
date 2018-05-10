package org.radixware.kernel.designer.common.general.utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.RowSorter;
import org.radixware.kernel.common.builder.check.common.CheckForDuplicationProvider;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class SwingUtils {

    /**
     * <ol>
     * <li> Convert modelRowIndex in tableRowIndex by sorter if need.
     * <li> SetSelectRow in table.
     * <li> Scroll viewPort.
     * </ol>
     *
     * @param table
     * @param modelRowIndex index in model which want to select
     */
    public static void setRowSelection(JTable table, int modelRowIndex) {

        // convert
        RowSorter sorter = table.getRowSorter();
        int index = (sorter == null) ? modelRowIndex : sorter.convertRowIndexToView(modelRowIndex);

        // select
        table.setRowSelectionInterval(index, index);

        // scroll
        JViewport viewPort = (JViewport) table.getParent();
        Rectangle rect = table.getCellRect(index, index, true);
        Point pt = viewPort.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        table.scrollRectToVisible(rect);
    }

    /**
     * {@link #InitButton(JButton, Icon, String, JPanel, int, int)}
     */
    public static void InitButton(final JButton button, Icon icon, String hint, JPanel panelHotKey, int keyCode) {
        InitButton(button, icon, hint, panelHotKey, keyCode, 0);
    }

    /**
     * Default initialization button in RadixWare.
     *
     * @param button
     * @param icon
     * @param toolTip
     * @param panelHotKey the panel where the hotkey will work
     * @param hotKeyCode use KeyEvent static constant
     * @param hotKeyModif use InputEvent static constant
     */
    public static void InitButton(final JButton button, Icon icon, String toolTip, JPanel panelHotKey, int hotKeyCode, int hotKeyModif) {

        button.setIcon(icon);

        String hintHotKeyMod = hotKeyModif == 0 ? "" : InputEvent.getModifiersExText(hotKeyModif) + "+";
        button.setToolTipText(toolTip + " (" + hintHotKeyMod + KeyEvent.getKeyText(hotKeyCode) + ")");

        String nameAction = "Click_" + button.getToolTipText();

        panelHotKey.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(hotKeyCode, hotKeyModif), nameAction);
        panelHotKey.getActionMap().put(nameAction,
                new AbstractAction(nameAction, RadixWareIcons.TECHNICAL_DOCUMENTATION.MAP.getIcon()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        button.doClick();
                    }
                });
    }

    /**
     * Side effect change BG color
     *
     * @see RadixColorUtils
     * @param textField
     */
    public static boolean checkValidName(JTextField textField, RadixObject radixObject) {
        CheckForDuplicationProvider checkForDuplicationProvider = CheckForDuplicationProvider.Factory.newForCheck(radixObject);
        String name = textField.getText();
        RadixObject duplicated = checkForDuplicationProvider.findDuplicated(name);
        if (name.isEmpty() || duplicated != null) {
            textField.setBackground(RadixColorUtils.ERROR_COLOR);
            return false;
        } else {
            textField.setBackground(Color.WHITE);
            return true;
        }
    }
}
