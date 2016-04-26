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

package org.radixware.kernel.common.client.dashboard;

import java.awt.Color;
import org.radixware.kernel.common.client.models.EntityModel;


public class DiagramSettings {

    public static enum EDiagramType {

        STATISTIC, DOT, STEP, CORRELATION
    }
    public static final Color defaultErrorColor = new Color(255, 170, 170);
    public static final Color defaultWarningColor = new Color(255, 204, 170);
    public static final Color defaultNornalColor = Color.blue;
    private String title;
    private EntityModel metricState;
    private Color normColor;
    private Color warningColor;
    private Color errorColor;
    private String metricName;
    private long stateId;
    private String kind;
    public EDiagramType diagramType;
    public boolean isHistorical;
    private boolean isUpdateEnabled = true;
    private boolean isAlertsSuppressed = false;
    CorrelationDiagramSettings corrSettings;
    HistoricalDiagramSettings histSettings;

    public DiagramSettings(boolean isHist) {
        this.isHistorical = isHist;
        if (isHistorical) {
            histSettings = new HistoricalDiagramSettings();
        } else {
            corrSettings = new CorrelationDiagramSettings();
        }
        title = "";
    }

    public DiagramSettings(EntityModel metricState) {
        setMetricState(metricState);
    }

    public DiagramSettings(DiagramSettings ms) {
        this(ms.getMetricState());
        title = ms.getTitle();
        metricName = ms.getMetricName();
        normColor = new Color(ms.getNormColor().getRGB());
        warningColor = new Color(ms.getWarningColor().getRGB());
        errorColor = new Color(ms.getErrorColor().getRGB());
    }

    public EntityModel getMetricState() {
        return metricState;
    }

    public final void setMetricState(EntityModel metricState) {
        if (metricState != null && !metricState.equals(this.metricState)) {
            this.metricState = metricState;
        }
    }

    public CorrelationDiagramSettings getCorrSettings() {
        return corrSettings;
    }

    public void setCorrSettings(CorrelationDiagramSettings corrSettings) {
        this.corrSettings = corrSettings;
    }

    public HistoricalDiagramSettings getHistSettings() {
        return histSettings;
    }

    public void setHistSettings(HistoricalDiagramSettings histSettings) {
        this.histSettings = histSettings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetricName() {
        return metricName == null ? title : metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public EDiagramType getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(EDiagramType diagramType) {
        this.diagramType = diagramType;
    }

    public Color getNormColor() {
        return normColor == null ? defaultNornalColor : normColor;
    }

    public void setNormColor(Color normColor) {
        this.normColor = normColor;
    }

    public Color getWarningColor() {
        return warningColor == null ? defaultWarningColor : warningColor;
    }

    public void setWarningColor(Color warningColor) {
        this.warningColor = warningColor;
    }

    public Color getErrorColor() {
        return errorColor == null ? defaultErrorColor : errorColor;
    }

    public void setErrorColor(Color errorColor) {
        this.errorColor = errorColor;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public void setIsUpdateEnabled(boolean isUpdateEnabled) {
        this.isUpdateEnabled = isUpdateEnabled;
    }

    public boolean getIsUpdateEnabled() {
        return isUpdateEnabled;
    }

    public void setIsAlertsSuppressed(boolean isAlertsSuppressed) {
        this.isAlertsSuppressed = isAlertsSuppressed;
    }

    public boolean getIsAlertsSuppressed() {
        return isAlertsSuppressed;
    }
}
