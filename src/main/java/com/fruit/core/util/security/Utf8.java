package com.fruit.core.util.security;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * Created by JesseHan on 2016/12/28.
 */
public class Utf8 {
    private static final Charset CHARSET = Charset.forName("UTF-8");

    public Utf8() {
    }

    public static byte[] encode(CharSequence string) {
        try {
            ByteBuffer e = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
            byte[] bytesCopy = new byte[e.limit()];
            System.arraycopy(e.array(), 0, bytesCopy, 0, e.limit());
            return bytesCopy;
        } catch (CharacterCodingException var3) {
            throw new IllegalArgumentException("Encoding failed", var3);
        }
    }

    public static String decode(byte[] bytes) {
        try {
            return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException var2) {
            throw new IllegalArgumentException("Decoding failed", var2);
        }
    }
}
