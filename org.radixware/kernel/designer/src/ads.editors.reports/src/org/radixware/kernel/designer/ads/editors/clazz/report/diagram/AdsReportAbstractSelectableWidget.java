package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectableWidgetKeyListener;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;

public abstract class AdsReportAbstractSelectableWidget extends AbstractAdsReportWidget {
    private final SelectableWidgetKeyListener keyListener;

    public AdsReportAbstractSelectableWidget(AdsReportFormDiagram diagram, RadixObject radixObject) {
        super(diagram, radixObject);
        keyListener = new SelectableWidgetKeyListener(diagram);
    }

    public void setSelected(final boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            onSelected(selected);
            if (selected) {
                requestFocus();
            }
        }
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    protected abstract void onSelected(boolean selected);

    @Override
    public void addNotify() {
        super.addNotify();
        getDiagram().addWidgetSelectionListener(listener);
        addKeyListener(keyListener);
    }
    
    

    @Override
    public void removeNotify() {
        getDiagram().removeWidgetSelectionListener(listener);
        removeKeyListener(keyListener);
        super.removeNotify();
    }
    
    private boolean selected = false;
    private final IRadixEventListener<SelectionEvent> listener = new IRadixEventListener<SelectionEvent>() {

        @Override
        public void onEvent(SelectionEvent e) {
            if (e.getSourse() == AdsReportAbstractSelectableWidget.this){
                setSelected(e.isSelected());
            } else {
                   if (isSelected() && !e.isExpand() && e.isSelected()){
                       setSelected(false);
                   }
            }
        }
    };
    
    protected void fireWidgetSelectionChanged(SelectionEvent event) {
        if (getParent() != null){
            getDiagram().fireSelectionChanged(event);
        }
    }
}
