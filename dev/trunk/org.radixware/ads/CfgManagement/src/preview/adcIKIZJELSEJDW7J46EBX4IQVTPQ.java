
/* Radix::CfgManagement::DesktopUtils - Desktop Executable*/

/*Radix::CfgManagement::DesktopUtils-Desktop Dynamic Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils")
public published class DesktopUtils  {



	/*Radix::CfgManagement::DesktopUtils:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::DesktopUtils:Properties-Properties*/

	/*Radix::CfgManagement::DesktopUtils:Methods-Methods*/

	/*Radix::CfgManagement::DesktopUtils:openFileForImport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils:openFileForImport",line=24)
	public static published  java.io.File openFileForImport (org.radixware.kernel.common.client.views.IView parentView, Str title) {
		Str dir = parentView.getEnvironment().getConfigStore().readString(idof[DesktopUtils:openFileForImport].toString());

		Str fileName = Explorer.Qt.Types::QFileDialog.getOpenFileName(
		        (Explorer.Qt.Types::QWidget)parentView,
		        title,
		        dir,
		        new Explorer.Qt.Types::QFileDialog.Filter("XML Files (*.xml)"));

		if (fileName == null || fileName.isEmpty())
		    return null;

		java.io.File f = new java.io.File(fileName);

		parentView.getEnvironment().getConfigStore().writeString(idof[DesktopUtils:openFileForImport].toString(), f.getAbsolutePath());

		return f;

	}

	/*Radix::CfgManagement::DesktopUtils:openFileForExport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils:openFileForExport",line=47)
	public static published  java.io.File openFileForExport (org.radixware.kernel.common.client.views.IView parentView, Str title) {
		Str dir = parentView.getEnvironment().getConfigStore().readString(idof[DesktopUtils:openFileForExport].toString());

		Str fileName = Explorer.Qt.Types::QFileDialog.getSaveFileName(
		        (Explorer.Qt.Types::QWidget)parentView,
		        title,
		        dir,
		        new Explorer.Qt.Types::QFileDialog.Filter("XML Files (*.xml)"));

		if (fileName == null || fileName.isEmpty())
		    return null;

		java.io.File f = new java.io.File(fileName);

		parentView.getEnvironment().getConfigStore().writeString(idof[DesktopUtils:openFileForExport].toString(), f.getAbsolutePath());

		return f;

	}

	/*Radix::CfgManagement::DesktopUtils:saveXmlAndShowCompleteMessage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils:saveXmlAndShowCompleteMessage",line=70)
	public static published  void saveXmlAndShowCompleteMessage (org.radixware.kernel.common.client.views.IView parentView, org.apache.xmlbeans.XmlObject xml) throws java.io.IOException {
		java.io.File file = openFileForExport(parentView, "Save File");
		if (file != null) {
		    //xml.save(file, new org.apache.xmlbeans.XmlOptions().setSavePrettyPrint().setSaveAggressiveNamespaces());
		    /*
		     * setSavePrettyPrint удаляет некоторые символы. Для форматирования 
		     * используется свой класс.
		     * */
		    org.radixware.kernel.common.utils.XmlFormatter.save(xml, file);
		    Explorer.Env::Application.messageInformation("Export complete");
		}
	}

	/*Radix::CfgManagement::DesktopUtils:checkAccessForModifyPacket-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils:checkAccessForModifyPacket",line=86)
	 static  boolean checkAccessForModifyPacket (org.radixware.kernel.common.client.views.IView parentView, org.radixware.ads.CfgManagement.common.CfgPacketState pkgState) {
		if (pkgState == CfgPacketState:Exported || pkgState == CfgPacketState:Closed) {
		    parentView.getEnvironment().messageWarning("Modify Package", Str.format(
		            "Package state is '%s', modification of the package is forbidden.",
		            pkgState.getTitle()));
		    return false;
		}
		return true;
	}

	/*Radix::CfgManagement::DesktopUtils:exportRevision-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils:exportRevision",line=99)
	 static  org.radixware.schemas.commondef.ChangeLogItem exportRevision (org.radixware.ads.CfgManagement.explorer.Revision rev, boolean exportLocals) {
		Types::CommonDefXsd:ChangeLogItem xRev = Types::CommonDefXsd:ChangeLogItem.Factory.newInstance();
		xRev.RevisionNumber = rev.seq.Value;
		xRev.Date = rev.time.Value;
		xRev.Description = rev.description.Value;
		xRev.RefDoc = rev.refDoc.Value;
		xRev.AppVer = rev.appVer.Value;
		xRev.Author = rev.author.Value;
		if (exportLocals) {
		    xRev.LocalNotes = rev.localNotes.Value;
		}
		xRev.Kind = rev.kind.Value;
		return xRev;
	}

	/*Radix::CfgManagement::DesktopUtils:importRevisionFromXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::DesktopUtils:importRevisionFromXml",line=117)
	 static  void importRevisionFromXml (org.radixware.ads.CfgManagement.explorer.Revision rev, org.radixware.schemas.commondef.ChangeLogItem xRev) {
		rev.seq.Value = xRev.RevisionNumber;
		rev.time.Value = xRev.Date;
		rev.description.Value = xRev.Description;
		rev.refDoc.Value = xRev.RefDoc;
		rev.appVer.Value = xRev.AppVer;
		rev.author.Value = xRev.Author;
		rev.localNotes.Value = xRev.LocalNotes;
		rev.kind.Value = xRev.Kind;
	}


}

/* Radix::CfgManagement::DesktopUtils - Desktop Meta*/

/*Radix::CfgManagement::DesktopUtils-Desktop Dynamic Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DesktopUtils_mi{
}

/* Radix::CfgManagement::DesktopUtils - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class DesktopUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export complete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт завершен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBIIDME3U6ZF3LCRHPLVAJN4NKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Save File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохранить файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3NXDCVMPZCUDCGQKWTU4CNDLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Modify Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модифицировать пакет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIBKTLDMOZNAXXE7YJCN3ECQCW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML Files (*.xml)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML файлы (*.xml)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUWRCYIZGFATXKMSLWSVAGG6OI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package state is \'%s\', modification of the package is forbidden.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние пакета \'%s\', модификация пакета запрещена.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXA3KGV45IFFEZGUT6VIPQVZX4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML Files (*.xml)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML файлы (*.xml)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDNMLB6EBJC43FNRDTQDLA7HKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\CfgManagement"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(DesktopUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcIKIZJELSEJDW7J46EBX4IQVTPQ"),"DesktopUtils - Localizing Bundle",$$$items$$$);
}
