package org.radixware.kernel.common.enums;

/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

public enum EKeyEvent implements IKernelIntEnum {

    //constant values for compiling
    VK_LBUTTON(0x01l),
    VK_RBUTTON(0x02l),
    VK_CANCEL(0x03l),
    VK_MBUTTON(0x04l),
    VK_XBUTTON1(0x05l),
    VK_XBUTTON2(0x06l),
    VK_BACK(0x08l),
    VK_TAB(0x09l),
    VK_CLEAR(0x0Cl),
    VK_RETURN(0x0Dl),
    VK_SHIFT(0x10l),
    VK_CONTROL(0x11l),
    VK_MENU(0x12l),
    VK_PAUSE(0x13l),
    VK_CAPITAL(0x14l),
    VK_KANA(0x15l),
    VK_HANGUEL(0x15l),
    VK_HANGUL(0x15l),
    VK_JUNJA(0x17l),
    VK_FINAL(0x18l),
    VK_HANJA(0x19l),
    VK_KANJI(0x19l),
    VK_ESCAPE(0x1Bl),
    VK_CONVERT(0x1Cl),
    VK_NONCONVERT(0x1Dl),
    VK_ACCEPT(0x1El),
    VK_MODECHANGE(0x1Fl),
    VK_SPACE(0x20l),
    VK_PRIOR(0x21l),
    VK_NEXT(0x22l),
    VK_END(0x23l),
    VK_HOME(0x24l),
    VK_LEFT(0x25l),
    VK_UP(0x26l),
    VK_RIGHT(0x27l),
    VK_DOWN(0x28l),
    VK_SELECT(0x29l),
    VK_PRINT(0x2Al),
    VK_EXECUTE(0x2Bl),
    VK_SNAPSHOT(0x2Cl),
    VK_INSERT(0x2Dl),
    VK_DELETE(0x2El),
    VK_HELP(0x2Fl),
    VK_LWIN(0x5Bl),
    VK_RWIN(0x5Cl),
    VK_APPS(0x5Dl),
    VK_SLEEP(0x5Fl),
    VK_NUMPAD0(0x60l),
    VK_NUMPAD1(0x61l),
    VK_NUMPAD2(0x62l),
    VK_NUMPAD3(0x63l),
    VK_NUMPAD4(0x64l),
    VK_NUMPAD5(0x65l),
    VK_NUMPAD6(0x66l),
    VK_NUMPAD7(0x67l),
    VK_NUMPAD8(0x68l),
    VK_NUMPAD9(0x69l),
    VK_MULTIPLY(0x6Al),
    VK_ADD(0x6Bl),
    VK_SEPARATOR(0x6Cl),
    VK_SUBTRACT(0x6Dl),
    VK_DECIMAL(0x6El),
    VK_DIVIDE(0x6Fl),
    VK_F1(0x70l),
    VK_F2(0x71l),
    VK_F3(0x72l),
    VK_F4(0x73l),
    VK_F5(0x74l),
    VK_F6(0x75l),
    VK_F7(0x76l),
    VK_F8(0x77l),
    VK_F9(0x78l),
    VK_F10(0x79l),
    VK_F11(0x7Al),
    VK_F12(0x7Bl),
    VK_F13(0x7Cl),
    VK_F14(0x7Dl),
    VK_F15(0x7El),
    VK_F16(0x7Fl),
    VK_F17(0x80l),
    VK_F18(0x81l),
    VK_F19(0x82l),
    VK_F20(0x83l),
    VK_F21(0x84l),
    VK_F22(0x85l),
    VK_F23(0x86l),
    VK_F24(0x87l),
    VK_NUMLOCK(0x90l),
    VK_SCROLL(0x91l),
    VK_LSHIFT(0xA0l),
    VK_RSHIFT(0xA1l),
    VK_LCONTROL(0xA2l),
    VK_RCONTROL(0xA3l),
    VK_LMENU(0xA4l),
    VK_RMENU(0xA5l),
    VK_BROWSER_BACK(0xA6l),
    VK_BROWSER_FORWARD(0xA7l),
    VK_BROWSER_REFRESH(0xA8l),
    VK_BROWSER_STOP(0xA9l),
    VK_BROWSER_SEARCH(0xAAl),
    VK_BROWSER_FAVORITES(0xABl),
    VK_BROWSER_HOME(0xACl),
    VK_VOLUME_MUTE(0xADl),
    VK_VOLUME_DOWN(0xAEl),
    VK_VOLUME_UP(0xAFl),
    VK_MEDIA_NEXT_TRACK(0xB0l),
    VK_MEDIA_PREV_TRACK(0xB1l),
    VK_MEDIA_STOP(0xB2l),
    VK_MEDIA_PLAY_PAUSE(0xB3l),
    VK_LAUNCH_MAIL(0xB4l),
    VK_LAUNCH_MEDIA_SELECT(0xB5l),
    VK_LAUNCH_APP1(0xB6l),
    VK_LAUNCH_APP2(0xB7l),
    VK_OEM_1(0xBAl),
    VK_OEM_PLUS(0xBBl),
    VK_OEM_COMMA(0xBCl),
    VK_OEM_MINUS(0xBDl),
    VK_OEM_PERIOD(0xBEl),
    VK_OEM_2(0xBFl),
    VK_OEM_3(0xC0l),
    VK_OEM_4(0xDBl),
    VK_OEM_5(0xDCl),
    VK_OEM_6(0xDDl),
    VK_OEM_7(0xDEl),
    VK_OEM_8(0xDFl),
    VK_OEM_102(0xE2l),
    VK_PROCESSKEY(0xE5l),
    VK_PACKET(0xE7l),
    VK_ATTN(0xF6l),
    VK_CRSEL(0xF7l),
    VK_EXSEL(0xF8l),
    VK_EREOF(0xF9l),
    VK_PLAY(0xFAl),
    VK_ZOOM(0xFBl),
    VK_NONAME(0xFCl),
    VK_PA1(0xFDl),
    VK_OEM_CLEAR(0xFEl),
    VK_0(0x30l),
    VK_1(0x31l),
    VK_2(0x32l),
    VK_3(0x33l),
    VK_4(0x34l),
    VK_5(0x35l),
    VK_6(0x36l),
    VK_7(0x37l),
    VK_8(0x38l),
    VK_9(0x39l),
    VK_A(0x41l),
    VK_B(0x42l),
    VK_C(0x43l),
    VK_D(0x44l),
    VK_E(0x45l),
    VK_F(0x46l),
    VK_G(0x47l),
    VK_H(0x48l),
    VK_I(0x49l),
    VK_J(0x4Al),
    VK_K(0x4Bl),
    VK_L(0x4Cl),
    VK_M(0x4Dl),
    VK_N(0x4El),
    VK_O(0x4Fl),
    VK_P(0x50l),
    VK_Q(0x51l),
    VK_R(0x52l),
    VK_S(0x53l),
    VK_T(0x54l),
    VK_U(0x55l),
    VK_V(0x56l),
    VK_W(0x57l),
    VK_X(0x58l),
    VK_Y(0x59l),
    VK_Z(0x5Al);
    //constructors
    private final long value;

    private EKeyEvent(long value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public static EKeyEvent getForValue(final Long val) {
        for (EKeyEvent e : EKeyEvent.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EKeyEvent has no item with value: " + String.valueOf(val), val);
    }

}
