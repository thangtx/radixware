package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.util.ArrayList;
import javax.swing.JComponent;

public class TextMetric {
    final JComponent component;
    ArrayList<LineFontsMetric> metrics = new ArrayList<>();
    int width = 0;
    int height = 0;

    public TextMetric(JComponent component) {
        this.component = component;
    }

    public void addFontMetric(LineFontsMetric metric){
        setHeight(metric);
        setWidth(metric.getWidth());
        metrics.add(metric);
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = this.width > width ? this.width : width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(LineFontsMetric metric) {
        this.height += metric.getHeight();
    }
    
    public int getLineCount(){
        return metrics.size();
    }
    
    public LineFontsMetric getLineFontsMetric(int line){
        return metrics.get(line);
    }
}
