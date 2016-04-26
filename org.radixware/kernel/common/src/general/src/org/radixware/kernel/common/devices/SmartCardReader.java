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

package org.radixware.kernel.common.devices;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import org.radixware.kernel.common.utils.Utils;


public final class SmartCardReader implements ISmartCardReader{
    
    private TerminalFactory factory;
    private CardTerminal cardTerminal;
    private Card card;
    private CardChannel channel;        
    
    public SmartCardReader(){
        factory = TerminalFactory.getDefault();
        if (factory==null){
            throw new IllegalStateException("Unable to get default instance of TerminalFactory");
        }        
    }

    @Override
    public List<String> getNames() throws CardException {
        if (factory==null){
            throw new IllegalStateException("Reader was closed");
        }        
        try{
            final List<CardTerminal> terminals = factory.terminals().list();
            final List<String> terminalNames = new LinkedList<>();
            for (CardTerminal terminal: terminals){
                terminalNames.add(terminal.getName());
            }
            return Collections.<String>unmodifiableList(terminalNames);
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }
        
    @Override
    public boolean open(final String name) throws CardException {
        if (factory==null){
            throw new IllegalStateException("Reader was closed");
        }        
        try{
            final List<CardTerminal> terminals = factory.terminals().list();
            for (CardTerminal terminal: terminals){
                if (Utils.equals(name,terminal.getName())){
                    cardTerminal = terminal;
                    return true;
                }
            }
            return false;
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public void close() throws CardException{
        if (card!=null){
            disconnect(false);
        }
        factory = null;
        cardTerminal = null;
        card = null;
        channel = null;
    }

    @Override
    public boolean isCardPresent() throws CardException {
        try{
            if (cardTerminal==null){
                throw new IllegalStateException("Terminal was not opened");
            }
            return cardTerminal.isCardPresent();
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public boolean waitForCardAbsent(final long timeout) throws CardException {
        try{
            if (cardTerminal==null){
                throw new IllegalStateException("Terminal was not opened");
            }
            return cardTerminal.waitForCardAbsent(timeout);
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public boolean waitForCardPresent(final long timeout) throws CardException {
        try{
            if (cardTerminal==null){
                throw new IllegalStateException("Terminal was not opened");
            }
            return cardTerminal.waitForCardPresent(timeout);
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public boolean connect(final String name, final String protocol) throws CardException {
        if (factory==null){
            throw new IllegalStateException("Reader was closed");
        }        
        try{
            final List<CardTerminal> terminals = factory.terminals().list();
            for (CardTerminal terminal: terminals){
                if (Utils.equals(name,terminal.getName())){
                    cardTerminal = terminal;
                    card = cardTerminal.connect(protocol);
                    channel = null;
                    return true;
                }
            }
            return false;
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public void disconnect(final boolean reset) throws CardException {
        try{
            if (card==null){
                throw new IllegalStateException("Card was not connected");
            }
            card.disconnect(reset);
            card = null;
            channel = null;
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public void beginExclusive() throws CardException {
        try{
            if (card==null){
                throw new IllegalStateException("Card was not connected");
            }        
            card.beginExclusive();
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public void endExclusive() throws CardException {
        try{
            if (card==null){
                throw new IllegalStateException("Card was not connected");
            }        
            card.endExclusive();
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }

    @Override
    public byte[] getAtr() {
        if (card==null){
            throw new IllegalStateException("Card was not connected");
        }
        final ATR atr = card.getATR();
        return atr==null ? null : atr.getBytes();
    }

    @Override
    public byte[] transmit(final byte[] command) throws CardException {
        try{
            if (card==null){
                throw new IllegalStateException("Card was not connected");
            }
            if (channel==null){
                channel = card.getBasicChannel();
            }        
            final ResponseAPDU response = channel.transmit(new CommandAPDU(command));
            return response==null ? null : response.getBytes();
        }catch(javax.smartcardio.CardException exception){
            throw convertException(exception);
        }
    }
    
    private static CardException convertException(final javax.smartcardio.CardException exception){//javax.smartcardio is not present in IBM java
        if (exception instanceof javax.smartcardio.CardNotPresentException){
            return new CardNotPresentException(exception.getMessage());
        }else{
            return new CardException(exception.getMessage());
        }
    }
    
}
