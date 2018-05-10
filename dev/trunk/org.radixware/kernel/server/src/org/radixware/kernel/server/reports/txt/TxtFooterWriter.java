package org.radixware.kernel.server.reports.txt;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TxtFooterWriter extends PrintStream{
    int height;
    private final OutputStream outputStream;

    private TxtFooterWriter(OutputStream outputStream) {
        super(outputStream);
        this.outputStream = outputStream;
    }
    
    private TxtFooterWriter(OutputStream outputStream, String encoding) throws UnsupportedEncodingException {
        super(outputStream, false, encoding);
        this.outputStream = outputStream;
    }

    @Override
    public String toString() {
       return outputStream.toString();
    }
    
    static class Factory {
        public static TxtFooterWriter newInstance() {
            OutputStream outputStream = new ByteArrayOutputStream();
            return new TxtFooterWriter(outputStream);
        }
        
        public static TxtFooterWriter newInstance(String encoding) throws UnsupportedEncodingException {
            OutputStream outputStream = new ByteArrayOutputStream();
            return new TxtFooterWriter(outputStream, encoding);
        }
    }
}
