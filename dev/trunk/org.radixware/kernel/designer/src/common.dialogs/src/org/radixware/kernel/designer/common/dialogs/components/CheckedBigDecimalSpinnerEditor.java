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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

public class CheckedBigDecimalSpinnerEditor extends JPanel implements ChangeListener, PropertyChangeListener, LayoutManager {

    /**
     * Construct a
     * <code>JSpinner</code> editor that supports displaying and editing the
     * value of a
     * <code>SpinnerNumberModel</code> with a
     * <code>JFormattedTextField</code>.
     * <code>This</code>
     * <code>NumberEditor</code> becomes both a
     * <code>ChangeListener</code> on the spinner and a
     * <code>PropertyChangeListener</code> on the new
     * <code>JFormattedTextField</code>.
     *
     * @param spinner the spinner whose model <code>this</code> editor will
     * monitor
     * @exception IllegalArgumentException if the spinners model is not an
     * instance of <code>SpinnerNumberModel</code>
     *
     * @see #getModel
     * @see #getFormat
     * @see SpinnerNumberModel
     *//*
     public CheckedBigDecimalSpinnerEditor(JSpinner spinner) {
     this(spinner, new DecimalFormat());
     }
     */

    /**
     * Construct a
     * <code>JSpinner</code> editor that supports displaying and editing the
     * value of a
     * <code>SpinnerNumberModel</code> with a
     * <code>JFormattedTextField</code>.
     * <code>This</code>
     * <code>NumberEditor</code> becomes both a
     * <code>ChangeListener</code> on the spinner and a
     * <code>PropertyChangeListener</code> on the new
     * <code>JFormattedTextField</code>.
     *
     * @param spinner the spinner whose model <code>this</code> editor will
     * monitor
     * @param decimalFormatPattern the initial pattern for the
     * <code>DecimalFormat</code> object that's used to display and parse the
     * value of the text field.
     * @exception IllegalArgumentException if the spinners model is not an
     * instance of <code>SpinnerNumberModel</code> or if
     * <code>decimalFormatPattern</code> is not a legal argument      * to <code>DecimalFormat</code>
     *
     * @see #getTextField
     * @see SpinnerNumberModel
     * @see java.text.DecimalFormat
     */
    /* public CheckedBigDecimalSpinnerEditor(JSpinner spinner, String decimalFormatPattern) {
     this(spinner, new DecimalFormat(decimalFormatPattern));
     }*/
    private final char decimalSeparator;

    /**
     * Constructs an editor component for the specified
     * <code>JSpinner</code>. This
     * <code>DefaultEditor</code> is it's own layout manager and it is added to
     * the spinner's
     * <code>ChangeListener</code> list. The constructor creates a single
     * <code>JFormattedTextField</code> child, initializes it's value to be the
     * spinner model's current value and adds it to
     * <code>this</code>
     * <code>DefaultEditor</code>.
     *
     * @param spinner the spinner whose model <code>this</code> editor will
     * monitor
     * @see #getTextField
     * @see JSpinner#addChangeListener
     */
    public CheckedBigDecimalSpinnerEditor(final JSpinner spinner/*, DecimalFormat format*/) {
        super(null);
        if (!(spinner.getModel() instanceof BigDecimalSpinnerModel)) {
            throw new IllegalArgumentException("model not a BigDecimalSpinnerModel");
        }

        DecimalFormatSymbols symbols = new DecimalFormat("0").getDecimalFormatSymbols();
        decimalSeparator = symbols.getDecimalSeparator();

        /*BigDecimalSpinnerModel model = (BigDecimalSpinnerModel) spinner.getModel();
         NumberFormatter formatter = new BigDecimalEditorFormatter(model,
         format);
         DefaultFormatterFactory factory = new DefaultFormatterFactory(
         formatter);
         */
        final Set<Integer> allowedKeys  = new HashSet<>(8);


        allowedKeys.add(new Integer(KeyEvent.VK_UP));
        allowedKeys.add(new Integer(KeyEvent.VK_DOWN));
        allowedKeys.add(new Integer(KeyEvent.VK_LEFT));
        allowedKeys.add(new Integer(KeyEvent.VK_RIGHT));
        allowedKeys.add(new Integer(KeyEvent.VK_BACK_SPACE));
        allowedKeys.add(new Integer(KeyEvent.VK_DELETE));
        allowedKeys.add(new Integer(KeyEvent.VK_HOME));
        allowedKeys.add(new Integer(KeyEvent.VK_END));

        final JFormattedTextField ftf = new JFormattedTextField() {
            

            @Override
            public void paste() {
                //do nothing preventing editor from getting wrong input
            }

            private void commitValue() {
                try {
                    commitEdit();
                } catch (ParseException pe) {
                    // Edited value is invalid, spinner.getValue() will return
                    // the last valid value, you could revert the spinner to show that:

                    final Object value = spinner.getValue();
                    int pos = getTextField().getCaretPosition();
                    int len = getTextField().getText().length();
                    getTextField().setValue(value);
                    int tmp = getTextField().getText().length();
                    getTextField().setCaretPosition(Math.min((pos + tmp - len) > 0 ? (pos + tmp - len) : 0, tmp));

                    // reset the value to some known value:
//                    spinner.setValue(value);
                    // or treat the last valid value as the current, in which
                    // case you don't need to do anything.
                }
            }
            private volatile boolean locked = false;

            @Override
            protected void processKeyEvent(KeyEvent ev) {

                if (locked) {
                    ev.consume();
                    return;
                }
                char keyChar = ev.getKeyChar();
                int st = this.getSelectionStart();
                int en = this.getSelectionEnd();
                if (Character.isDigit(keyChar) && st == 0 && en == this.getText().length()) {
                    ev.consume();
                    locked = true;
                    this.setText(String.valueOf(keyChar));
                    commitValue();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            locked = false;
                        }
                    });
                } else {
                    int keyCode = ev.getKeyCode();
//                    final char keyChar = ev.getKeyChar();
                    if (Character.isDigit(keyChar) || allowedKeys.contains(keyCode)
                            || keyChar == decimalSeparator || (keyChar == '-' && getCaretPosition() == 0)) {
                        super.processKeyEvent(ev);
                        commitValue();
                    } else if ((keyCode == KeyEvent.VK_CONTROL) || (keyCode == KeyEvent.VK_SPACE)
                            || (keyCode == KeyEvent.VK_ENTER) || (keyCode == KeyEvent.VK_ESCAPE)) {
                        super.processKeyEvent(ev);
                    } else if (keyChar == '.' || keyChar == ',') {
                        ev.setKeyChar(decimalSeparator);
                        super.processKeyEvent(ev);
                        commitValue();
                    } else {
                        ev.consume();
                    }
                }
            }
        };

        ftf.setFocusable(true);
        ftf.setEditable(true);
        ftf.setFormatterFactory(new BigDecimalFormatterFactory());

        ftf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ftf.setCaretPosition(ftf.getText().length());
                    }
                });

            }
        });
        ftf.setName("Spinner.formattedTextField");
        ftf.setValue(spinner.getValue());
        ftf.addPropertyChangeListener(this);

        ftf.setInheritsPopupMenu(true);

        String toolTipText = spinner.getToolTipText();
        if (toolTipText != null) {
            ftf.setToolTipText(toolTipText);
        }

        /* TBD - initializing the column width of the text field
         * is imprecise and doing it here is tricky because
         * the developer may configure the formatter later.
         */
        try {
            BigDecimalSpinnerModel model = (BigDecimalSpinnerModel) spinner.getModel();
            JFormattedTextField.AbstractFormatter aft = ftf.getFormatter();
            String maxString = aft.valueToString(model.getMinimum());
            String minString = aft.valueToString(model.getMaximum());
            ftf.setColumns(Math.max(maxString.length(),
                    minString.length()));
        } catch (ParseException e) {
            // TBD should throw a chained error here
        }

        add(ftf);

        setLayout(this);
        spinner.addChangeListener(this);

        // We want the spinner's increment/decrement actions to be
        // active vs those of the JFormattedTextField. As such we
        // put disabled actions in the JFormattedTextField's actionmap.
        // A binding to a disabled action is treated as a nonexistant
        // binding.
        ActionMap ftfMap = ftf.getActionMap();

        if (ftfMap != null) {
            final Action DISABLED_ACTION = new DisabledAction();

            ftfMap.put("increment", DISABLED_ACTION);
            ftfMap.put("decrement", DISABLED_ACTION);
        }
    }

    public DecimalFormat getFormat() {
        return (DecimalFormat) ((NumberFormatter) (getTextField().getFormatter())).getFormat();
    }

    /**
     * Return our spinner ancestor's
     * <code>SpinnerNumberModel</code>.
     *
     * @return <code>getSpinner().getModel()</code>
     * @see #getSpinner
     * @see #getTextField
     */
    public BigDecimalSpinnerModel getModel() {
        return (BigDecimalSpinnerModel) getSpinner().getModel();
    }

    /**
     * Disconnect
     * <code>this</code> editor from the specified
     * <code>JSpinner</code>. By default, this method removes itself from the
     * spinners
     * <code>ChangeListener</code> list.
     *
     * @param spinner the <code>JSpinner</code> to disconnect this editor from;
     * the same spinner as was passed to the constructor.
     */
    public void dismiss(JSpinner spinner) {
        spinner.removeChangeListener(this);
    }

    /**
     * Returns the
     * <code>JSpinner</code> ancestor of this editor or
     * <code>null</code> if none of the ancestors are a
     * <code>JSpinner</code>. Typically the editor's parent is a
     * <code>JSpinner</code> however subclasses of
     * <code>JSpinner</code> may override the the
     * <code>createEditor</code> method and insert one or more containers
     * between the
     * <code>JSpinner</code> and it's editor.
     *
     * @return <code>JSpinner</code> ancestor; <code>null</code> if none of the
     * ancestors are a <code>JSpinner</code>
     *
     * @see JSpinner#createEditor
     */
    public JSpinner getSpinner() {
        for (Component c = this; c != null; c = c.getParent()) {
            if (c instanceof JSpinner) {
                return (JSpinner) c;
            }
        }
        return null;
    }

    /**
     * Returns the
     * <code>JFormattedTextField</code> child of this editor. By default the
     * text field is the first and only child of editor.
     *
     * @return the <code>JFormattedTextField</code> that gives the user access
     * to the <code>SpinnerDateModel's</code> value.
     * @see #getSpinner
     * @see #getModel
     */
    public JFormattedTextField getTextField() {
        return (JFormattedTextField) getComponent(0);
    }

    /**
     * This method is called when the spinner's model's state changes. It sets
     * the
     * <code>value</code> of the text field to the current value of the spinners
     * model.
     *
     * @param e the <code>ChangeEvent</code> whose source is the
     * <code>JSpinner</code> whose model has changed.
     * @see #getTextField
     * @see JSpinner#getValue
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) (e.getSource());
        int pos = getTextField().getCaretPosition();
        int len = getTextField().getText().length();
        BigDecimal val = ((BigDecimal) spinner.getValue());
        String text = getTextField().getText();
        if (!val.equals(BigDecimal.ZERO) || (!"".equals(text.trim()) && !"-".equals(text.trim()))) {
            getTextField().setValue(val);
        }
        int tmp = getTextField().getText().length();
        getTextField().setCaretPosition(Math.min((pos + tmp - len) > 0 ? (pos + tmp - len) : 0, tmp));
    }

    /**
     * Called by the
     * <code>JFormattedTextField</code>
     * <code>PropertyChangeListener</code>. When the
     * <code>"value"</code> property changes, which implies that the user has
     * typed a new number, we set the value of the spinners model.
     * <p>
     * This class ignores
     * <code>PropertyChangeEvents</code> whose source is not the
     * <code>JFormattedTextField</code>, so subclasses may safely make
     * <code>this</code>
     * <code>DefaultEditor</code> a
     * <code>PropertyChangeListener</code> on other objects.
     *
     * @param e the <code>PropertyChangeEvent</code> whose source is * the <code>JFormattedTextField</code> created by this class.
     * @see #getTextField
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        JSpinner spinner = getSpinner();

        if (spinner == null) {
            // Indicates we aren't installed anywhere.
            return;
        }

        Object source = e.getSource();
        String name = e.getPropertyName();
        if ((source instanceof JFormattedTextField) && "value".equals(name)) {
            Object lastValue = spinner.getValue();

            // Try to set the new value
            try {
                BigDecimal obj = (BigDecimal) getTextField().getValue();
                if (obj == null) {
                    throw new IllegalArgumentException();
                }
                String str = getTextField().getText().trim();
                if (!obj.equals(BigDecimal.ZERO) || (!str.equals("") && !str.equals("-"))) {
                    spinner.setValue(obj);
                    BigDecimalSpinnerModel model = (BigDecimalSpinnerModel) spinner.getModel();
                    Comparable maximum = model.getMaximum();
                    Comparable minimum = model.getMinimum();
                    if (!(((minimum == null) || (minimum.compareTo(obj) <= 0))
                            && ((maximum == null) || (maximum.compareTo(obj) >= 0)))) {
                        spinner.setValue(lastValue);
                    }
                }
            } catch (IllegalArgumentException iae) {
                // SpinnerModel didn't like new value, reset
                try {
                    int pos = ((JFormattedTextField) source).getCaretPosition();
                    int len = ((JFormattedTextField) source).getText().length();
                    ((JFormattedTextField) source).setValue(lastValue);
                    int tmp = ((JFormattedTextField) source).getText().length();
                    ((JFormattedTextField) source).setCaretPosition(Math.min((pos + tmp - len) > 0 ? (pos + tmp - len) : 0, tmp));
                } catch (IllegalArgumentException iae2) {
                    // Still bogus, nothing else we can do, the
                    // SpinnerModel and JFormattedTextField are now out
                    // of sync.
                }
            }
        }
    }

    /**
     * This
     * <code>LayoutManager</code> method does nothing. We're only managing a
     * single child and there's no support for layout constraints.
     *
     * @param name ignored
     * @param child ignored
     */
    @Override
    public void addLayoutComponent(String name, Component child) {
    }

    /**
     * This
     * <code>LayoutManager</code> method does nothing. There isn't any per-child
     * state.
     *
     * @param child ignored
     */
    @Override
    public void removeLayoutComponent(Component child) {
    }

    /**
     * Returns the size of the parents insets.
     */
    private Dimension insetSize(Container parent) {
        Insets insets = parent.getInsets();
        int w = insets.left + insets.right;
        int h = insets.top + insets.bottom;
        return new Dimension(w, h);
    }

    /**
     * Returns the preferred size of first (and only) child plus the size of the
     * parents insets.
     *
     * @param parent the Container that's managing the layout
     * @return the preferred dimensions to lay out the subcomponents of the
     * specified container.
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension preferredSize = insetSize(parent);
        if (parent.getComponentCount() > 0) {
            Dimension childSize = getComponent(0).getPreferredSize();
            preferredSize.width += childSize.width;
            preferredSize.height += childSize.height;
        }
        return preferredSize;
    }

    /**
     * Returns the minimum size of first (and only) child plus the size of the
     * parents insets.
     *
     * @param parent the Container that's managing the layout
     * @return the minimum dimensions needed to lay out the subcomponents of the
     * specified container.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension minimumSize = insetSize(parent);
        if (parent.getComponentCount() > 0) {
            Dimension childSize = getComponent(0).getMinimumSize();
            minimumSize.width += childSize.width;
            minimumSize.height += childSize.height;
        }
        return minimumSize;
    }

    /**
     * Resize the one (and only) child to completely fill the area within the
     * parents insets.
     */
    @Override
    public void layoutContainer(Container parent) {
        if (parent.getComponentCount() > 0) {
            Insets insets = parent.getInsets();
            int w = parent.getWidth() - (insets.left + insets.right);
            int h = parent.getHeight() - (insets.top + insets.bottom);
            getComponent(0).setBounds(insets.left, insets.top, w, h);
        }
    }

    /**
     * Pushes the currently edited value to the
     * <code>SpinnerModel</code>.
     * <p>
     * The default implementation invokes
     * <code>commitEdit</code> on the
     * <code>JFormattedTextField</code>.
     *
     * @throws ParseException if the edited value is not legal
     */
    public void commitEdit() throws ParseException {
        // If the value in the JFormattedTextField is legal, this will have
        // the result of pushing the value to the SpinnerModel
        // by way of the <code>propertyChange</code> method.
        JFormattedTextField ftf = getTextField();

        ftf.commitEdit();
    }

    /**
     * Returns the baseline.
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @see javax.swing.JComponent#getBaseline(int,int)
     * @see javax.swing.JComponent#getBaselineResizeBehavior()
     * @since 1.6
     */
    @Override
    public int getBaseline(int width, int height) {
        // check size.
        super.getBaseline(width, height);
        Insets insets = getInsets();
        width = width - insets.left - insets.right;
        height = height - insets.top - insets.bottom;
        int baseline = getComponent(0).getBaseline(width, height);
        if (baseline >= 0) {
            return baseline + insets.top;
        }
        return -1;
    }

    /**
     * Returns an enum indicating how the baseline of the component changes as
     * the size changes.
     *
     * @throws NullPointerException {@inheritDoc}
     * @see javax.swing.JComponent#getBaseline(int, int)
     * @since 1.6
     */
    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior() {
        return getComponent(0).getBaselineResizeBehavior();
    }

    private static class DisabledAction implements Action {

        @Override
        public Object getValue(String key) {
            return null;
        }

        @Override
        public void putValue(String key, Object value) {
        }

        @Override
        public void setEnabled(boolean b) {
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener l) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener l) {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
        }
    }

    private class BigDecimalFormatter extends NumberFormatter {

        public BigDecimalFormatter() {
            super(NumberFormat.getNumberInstance());
            ((NumberFormat) this.getFormat()).setMaximumFractionDigits(16);
        }

        private String prepare(String str) {
//            char ds = getDefaultLocaleDecimalSeparator();
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < str.length(); ++i) {
                if (Character.isDigit(str.charAt(i)) || str.charAt(i) == decimalSeparator || str.charAt(i) == '-') {
                    ret.append(str.charAt(i));
                }
            }
            return ret.toString().replace(decimalSeparator, '.');
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            if ("".equals(text.trim()) || "-".equals(text.trim())) {
                return BigDecimal.ZERO;
            }
//            char ds = getDefaultLocaleDecimalSeparator();

            try {
                String val = prepare(text);
//                if (ds != '.') {
//                    val = text.replace(".", "").replace(" ", "").replace(ds, '.');
//                } else {
//                    val = text.replace(",", "").replace(" ", "");
//                }
                BigDecimal ret = new BigDecimal(val);
                return ret;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            return super.valueToString(value);
        }
//        private char getDefaultLocaleDecimalSeparator() {
//            DecimalFormatSymbols symbols = new DecimalFormat("0").getDecimalFormatSymbols();
//            char ds = symbols.getDecimalSeparator();
//            return ds;
//        }
    }

    private class BigDecimalFormatterFactory extends JFormattedTextField.AbstractFormatterFactory {

        @Override
        public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
            return new BigDecimalFormatter();
        }
    }
    /*
     private static class BigDecimalEditorFormatter extends NumberFormatter {

     private final BigDecimalSpinnerModel model;

     BigDecimalEditorFormatter(BigDecimalSpinnerModel model, NumberFormat format) {
     super(format);
     this.model = model;
     setValueClass(model.getValue().getClass());
     }

     public void setMinimum(Comparable min) {
     model.setMinimum(min);
     }

     public Comparable getMinimum() {
     return model.getMinimum();
     }

     public void setMaximum(Comparable max) {
     model.setMaximum(max);
     }

     public Comparable getMaximum() {
     return model.getMaximum();
     }
     }*/
    ////////////////////////////////////////////////////////////example of usage
    /*
     public static void main(String[] args) {

     javax.swing.JFrame f = new javax.swing.JFrame();
     f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
     f.setLocation(400, 300);
     JPanel jp = new JPanel();

     SpinnerNumberModel model = new SpinnerNumberModel(50, 0.0f, 100.0f, 1.5f);
     JSpinner sp = new JSpinner(model); //or use JSpinner with default constructor
     sp.setEditor(new CheckedNumberSpinnerEditor(sp));
     jp.add(sp);

     f.getContentPane().add(jp, java.awt.BorderLayout.CENTER);

     f.pack();

     f.setVisible(true);
     }*/
}
