package org.radixware.kernel.server.reports;

import org.radixware.kernel.common.enums.EPdfPrintSecurityOption;

public class PdfSecurityOptions{
    public static int DEFAULT_ENCRIPTION_LENGHT = 128;
    
    private String ownerPassword;
    private String userPassword;
    private int encryptionLength = DEFAULT_ENCRIPTION_LENGHT;
    private EPdfPrintSecurityOption allowPrint = EPdfPrintSecurityOption.ALLOW_PRINT_HQ;
    private boolean allowCopyContent = true;
    private boolean allowEditContent = true;
    private boolean allowEditAnnotations = true;
    private boolean allowFillInForms = true;
    private boolean allowAccessContent = true;
    private boolean allowAssembleDocument = true;
    private boolean encryptMetadata = true;

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getEncryptionLength() {
        return encryptionLength;
    }

    public void setEncryptionLength(int encryptionLength) {
        this.encryptionLength = encryptionLength;
    }

    public EPdfPrintSecurityOption getAllowPrint() {
        return allowPrint;
    }

    public void setAllowPrint(EPdfPrintSecurityOption allowPrint) {
        this.allowPrint = allowPrint;
    }

    public boolean isAllowCopyContent() {
        return allowCopyContent;
    }

    public void setAllowCopyContent(boolean allowCopyContent) {
        this.allowCopyContent = allowCopyContent;
    }

    public boolean isAllowEditContent() {
        return allowEditContent;
    }

    public void setAllowEditContent(boolean allowEditContent) {
        this.allowEditContent = allowEditContent;
    }

    public boolean isAllowEditAnnotations() {
        return allowEditAnnotations;
    }

    public void setAllowEditAnnotations(boolean allowEditAnnotations) {
        this.allowEditAnnotations = allowEditAnnotations;
    }

    public boolean isAllowFillInForms() {
        return allowFillInForms;
    }

    public void setAllowFillInForms(boolean allowFillInForms) {
        this.allowFillInForms = allowFillInForms;
    }

    public boolean isAllowAccessContent() {
        return allowAccessContent;
    }

    public void setAllowAccessContent(boolean allowAccessContent) {
        this.allowAccessContent = allowAccessContent;
    }

    public boolean isAllowAssembleDocument() {
        return allowAssembleDocument;
    }

    public void setAllowAssembleDocument(boolean allowAssembleDocument) {
        this.allowAssembleDocument = allowAssembleDocument;
    }

    public boolean isEncryptMetadata() {
        return encryptMetadata;
    }

    public void setEncryptMetadata(boolean encryptMetadata) {
        this.encryptMetadata = encryptMetadata;
    }

   
    
}
