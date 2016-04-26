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

package org.radixware.kernel.server.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class FontChooser implements ActionListener, ChangeListener, ListDataListener {

	private static final class CenterLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {}

		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Dimension dimension = new Dimension();
			if (parent.getComponentCount() == 1) {
				dimension = parent.getComponent(0).getPreferredSize();
			}
			return dimension;
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			Dimension dimension = new Dimension();
			if (parent.getComponentCount() == 1) {
				dimension = parent.getComponent(0).getMinimumSize();
			}
			return dimension;
		}

		@Override
		public void layoutContainer(Container parent) {
			if (parent.getComponentCount() == 1) {
				Component component = parent.getComponent(0);
				int width = component.getPreferredSize().width;
				int height = component.getPreferredSize().height;
				int x = (parent.getWidth() - width) / 2;
				int y = (parent.getHeight() - height) / 2;
				component.setBounds(x, y, width, height);
			}
		}

	}
	
	private final JDialog dialog;
	private JToggleButton boldButton;
	private JToggleButton italicButton;
	private JComboBox comboBox;
	private SpinnerNumberModel spinnerNumberModel;
	private Font font;
	private JLabel exampleLabel;

	private FontChooser(JDialog dialog, Font font) {
		this.dialog = dialog;
		this.font = font;
		initDialog();
	}

	private void initDialog() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.createAdjustmentPanel());
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(this.createExamplePanel());
		final JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		pane.addPropertyChangeListener
		(
			new PropertyChangeListener()
			{
			@Override
				public void propertyChange(PropertyChangeEvent e)
				{
					if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null)
						return;
					int res = ((Integer)(e.getNewValue())).intValue();
					if (res == JOptionPane.OK_OPTION)
						font = exampleLabel.getFont();
					dialog.dispose();
				}
			}
		);
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
			   dialog.dispose();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		dialog.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		dialog.setContentPane(pane);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private JPanel createAdjustmentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		comboBox = new JComboBox(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		comboBox.setSelectedItem(font.getFamily());
		comboBox.getModel().addListDataListener(this);
		JSpinner spinner = new JSpinner();
		spinner.addChangeListener(this);
		spinnerNumberModel = new SpinnerNumberModel(font.getSize(), 1, 100, 1);
		spinner.setModel(spinnerNumberModel);
		boldButton = new JToggleButton(Messages.LBL_BOLD);
		boldButton.setFont(boldButton.getFont().deriveFont(Font.BOLD));
		boldButton.setMargin(new Insets(2, 4, 2, 4));
		boldButton.setFocusable(false);
		boldButton.setActionCommand("boldButton");
		boldButton.addActionListener(this);
		italicButton = new JToggleButton(Messages.LBL_ITALIC);
		italicButton.setFont(italicButton.getFont().deriveFont(Font.ITALIC));
		italicButton.setMargin(new Insets(2, 4, 2, 4));
		italicButton.setFocusable(false);
		italicButton.setActionCommand("italicButton");
		italicButton.addActionListener(this);
		boldButton.setSelected(font.isBold());
		italicButton.setSelected(font.isItalic());
		panel.add(comboBox);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(spinner);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(boldButton);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(italicButton);
		return panel;
	}

	private JPanel createExamplePanel() {
		JPanel panel = new JPanel(new CenterLayout());
		Dimension size = new Dimension(300, 75);
		panel.setPreferredSize(size);
		panel.setMaximumSize(size);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		exampleLabel = new JLabel(Messages.LBL_SAMPLE_TEXT);
		exampleLabel.setFont(new Font(font.getName(), font.getStyle(), font.getSize()));
		panel.add(exampleLabel);
		return panel;
	}

	@Override
	public void intervalAdded(ListDataEvent e) {}
		
	@Override
	public void intervalRemoved(ListDataEvent e) {}
    
	@Override
	public void contentsChanged(ListDataEvent e) {
        exampleLabel.setFont(new Font(comboBox.getSelectedItem().toString(), exampleLabel.getFont().getStyle(), exampleLabel.getFont().getSize()));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		exampleLabel.setFont(exampleLabel.getFont().deriveFont(spinnerNumberModel.getNumber().floatValue()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if ("boldButton".equals(actionCommand) || "italicButton".equals(actionCommand)) {
			if (boldButton.isSelected() && italicButton.isSelected()) {
				exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));
			} else if (!boldButton.isSelected() && !italicButton.isSelected()) {
				exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.PLAIN));
			} else if (boldButton.isSelected() && !italicButton.isSelected()) {
				exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.BOLD));
			} else if (!boldButton.isSelected() && italicButton.isSelected()) {
				exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.ITALIC));
			}
		}
	}

	public static Font showDialog(Dialog owner, Font font) {
		return FontChooser.showDialog(owner, "", font);
	}

	public static Font showDialog(Dialog owner, String title, Font font) {
		return new FontChooser(new JDialog(owner, title, true), font).getFont();
	}

	public static Font showDialog(Frame owner, Font font) {
		return FontChooser.showDialog(owner, "", font);
	}

	public static Font showDialog(Frame owner, String title, Font font) {
		return new FontChooser(new JDialog(owner, title, true), font).getFont();
	}

	private Font getFont() {
		return font;
	}
	
	private static final class Messages
	{
		static {
		    final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

		    LBL_BOLD 	= bundle.getString("LBL_BOLD");
			LBL_ITALIC	= bundle.getString("LBL_ITALIC");
			LBL_SAMPLE_TEXT	= bundle.getString("LBL_SAMPLE_TEXT");
		}
		static final String LBL_BOLD;
		static final String LBL_ITALIC;
		static final String LBL_SAMPLE_TEXT;
	}

}


