
/* Radix::Reports::ReportPubTreeNodeWeb - Web Executable*/

/*Radix::Reports::ReportPubTreeNodeWeb-Web Dynamic Class*/

package org.radixware.ads.Reports.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubTreeNodeWeb")
public class ReportPubTreeNodeWeb  extends org.radixware.wps.rwt.tree.Node.DefaultNode  {



	/*Radix::Reports::ReportPubTreeNodeWeb:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPubTreeNodeWeb:Properties-Properties*/

	/*Radix::Reports::ReportPubTreeNodeWeb:Methods-Methods*/

	/*Radix::Reports::ReportPubTreeNodeWeb:setCellForeground-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubTreeNodeWeb:setCellForeground")
	public  void setCellForeground (int cell, java.awt.Color color) {
		if (cell < getCellsCount()) {
		    getCells().get(cell).setForeground(color);
		}
	}

	/*Radix::Reports::ReportPubTreeNodeWeb:ReportPubTreeNodeWeb-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubTreeNodeWeb:ReportPubTreeNodeWeb")
	public  ReportPubTreeNodeWeb (Str displayName) {
		super(displayName);
	}

	/*Radix::Reports::ReportPubTreeNodeWeb:setCellAlignmet-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPubTreeNodeWeb:setCellAlignmet")
	public  void setCellAlignmet (int cell, org.radixware.wps.rwt.Alignment alignment) {
		if (cell < getCellsCount()) {
		    getCells().get(cell).getHtml().setAttr("align", alignment.name());
		}
	}


}

/* Radix::Reports::ReportPubTreeNodeWeb - Web Meta*/

/*Radix::Reports::ReportPubTreeNodeWeb-Web Dynamic Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPubTreeNodeWeb_mi{
}
