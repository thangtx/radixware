package org.radixware.kernel.common.components;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SizeRequirements;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ParagraphView;

public class HTMLTextField extends JTextPane {

    private final JScrollPane scrollPane;

    public HTMLTextField() {
        super();
        setContentType("text/html"); // NOI18N
        LineEditorKit lek = new LineEditorKit();
        setEditorKit(lek);
        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        setFont(UIManager.getFont("TextField.font"));
        setBorder(UIManager.getBorder("TextField.border"));
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setMaximumSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setMaximumSize(new Dimension(0, 0));
        scrollPane.setBorder(null);
        scrollPane.setViewportView(this);


        
        StyledDocument styledDoc = getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            AbstractDocument doc = (AbstractDocument) styledDoc;
            doc.setDocumentFilter(new LineFilter());
        }
    }

    public JScrollPane getTopComponent() {
        return scrollPane;
    }

    Color backgroundColor;
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            super.setEditable(enabled);
            setBackground(backgroundColor);
        } else {
            backgroundColor = getBackground();
            super.setEditable(enabled);
            setBackground(UIManager.getColor("TextField.inactiveBackground"));
        }
    }

    public class LineEditorKit extends HTMLEditorKit {

        ViewFactory defaultFactory = new LineFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
        
    }

    class LineFactory extends HTMLEditorKit.HTMLFactory {

        @Override
        public View create(Element elem) {
            View view = super.create(elem);
            if (view instanceof ParagraphView) {

                return new ParagraphView(elem) {
                    @Override
                    public float getMinimumSpan(int axis) {
                        return super.getPreferredSpan(axis);
                    }
                };
                      
            }

            return view;
        }
    }

    class LineFilter extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, string.replaceAll("\\n", ""), attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, string.replaceAll("\\n", ""), attr);
        }

    }
}
