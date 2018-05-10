package org.radixware.kernel.explorer.inspector;

import com.trolltech.qt.gui.QWidget;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.types.Icon;

/**
 *
 * @author szotov
 */
public class WidgetInfo<T> {

    private final WeakReference<T> widgetRef;
    private final WidgetInspector<T> inspector;

    public WidgetInfo(WidgetInspector<T> inspector, T widget, String className) {
        widgetRef = new WeakReference<>(widget);
        this.inspector = inspector;
    }

    public WidgetInspector<T> getWidgetInspector() {
        return inspector;
    }

    public T getWidget() {
        return widgetRef.get();
    }

    public WidgetInfo<T> getParentWidgetInfo() {
        T parentWidget = this.inspector.getParentWidget(this.getWidget());
        return parentWidget == null ? null : new WidgetInfo<>(inspector, parentWidget, parentWidget.getClass().getSimpleName());
    }

    public List<WidgetInfo<T>> getChildrenInfo() {
        List<WidgetInfo<T>> childrenInfoList = new LinkedList<>();
        for (T widget : this.getWidgetInspector().getChildWidgets(this.getWidget())) {
            if (!(widget instanceof InspectorDialog)) {
                WidgetInfo<T> tempWidgetInfo = new WidgetInfo<>(inspector, widget, widget.getClass().getSimpleName());
                childrenInfoList.add(tempWidgetInfo);
            }
        }
        return Collections.unmodifiableList(childrenInfoList);
    }

    public List<WidgetProperty> getWidgetProperties() {
        return getWidgetInspector().getWidgetProperties(getWidget());
    }

    public boolean isVisible() {
        return getWidgetInspector().isVisible(getWidget());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.getWidget());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WidgetInfo<?> other = (WidgetInfo<?>) obj;
        return Objects.equals(this.getWidget(), other.getWidget());
    }

    public String getDescription() {
        return this.getWidgetInspector().getDescription(getWidget());
    }

    public Icon getIcon(Class cl, IClientEnvironment environment) {
        Class widgetClass = cl;

        while (!widgetClass.equals(QWidget.class)) {
            String widgetName = widgetClass.getSimpleName();
            switch (widgetName) {
                case "QStackedWidget":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/page_set.svg");
                case "QListView":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/list.svg");
                case "QTreeWidget":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/tree.svg");
                case "QTreeView":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/tree.svg");
                case "QLabel":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/label.svg");
                case "QPushButton":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/push_button.svg");
                case "QRadioButton":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/radio_button.svg");
                case "QCheckBox":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/check_box.svg");
                case "QToolButton":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/tool_button.svg");
                case "QComboBox":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/combo_box.svg");
                case "QFrame":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/frame.svg");
                case "QLineEdit":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/line_edit.svg");
                case "QSpinBox":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/spin_box.svg");
                case "QTextEdit":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/text_edit.svg");
                case "QToolBar":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/tool_bar.svg");
                case "QProgressBar":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/progress_bar.svg");
                case "TabSet":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/tab_set.svg");
                case "QTabWidget":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/tab_set.svg");
                case "EditorPage":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/editor_page.svg");
                case "QSplitter":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/splitter.svg");
                case "EmbeddedEditor":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/embedded_editor.svg");
                case "EmbeddedSelector":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/embedded_selector.svg");
                case "QTableView":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/table.svg");
                case "QScrollArea":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/scrollarea.svg");
                case "PropLabel":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/prop_label.svg");
                case "StandardEditor":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/editor.svg");
                case "StandardSelector":
                    return environment.getApplication().getImageManager().loadIcon("classpath:images/selector.svg");
                default:
                    if (widgetName.matches("^Val([a-zA-Z])*Editor$")) {
                        return environment.getApplication().getImageManager().loadIcon("classpath:images/val_edit.svg");
                    } else if (widgetName.matches("^Prop([a-zA-Z])*Editor$")) {
                        return environment.getApplication().getImageManager().loadIcon("classpath:images/prop_editor.svg");
                    }
            }
            widgetClass = widgetClass.getSuperclass();
        }
        return null;
    }

}
