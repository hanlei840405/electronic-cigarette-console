package com.fruit.core.util.security;

/**
 * Created by JesseHan on 2016/12/28.
 */
public class Base64 {
    public static final int NO_OPTIONS = 0;
    public static final int ENCODE = 1;
    public static final int DECODE = 0;
    public static final int DO_BREAK_LINES = 8;
    public static final int URL_SAFE = 16;
    public static final int ORDERED = 32;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte EQUALS_SIGN = 61;
    private static final byte NEW_LINE = 10;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte[] _STANDARD_ALPHABET = new byte[]{(byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)43, (byte)47};
    private static final byte[] _STANDARD_DECODABET = new byte[]{(byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-5, (byte)-5, (byte)-9, (byte)-9, (byte)-5, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-5, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)62, (byte)-9, (byte)-9, (byte)-9, (byte)63, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)-9, (byte)-9, (byte)-9, (byte)-1, (byte)-9, (byte)-9, (byte)-9, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9};
    private static final byte[] _URL_SAFE_ALPHABET = new byte[]{(byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)45, (byte)95};
    private static final byte[] _URL_SAFE_DECODABET = new byte[]{(byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-5, (byte)-5, (byte)-9, (byte)-9, (byte)-5, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-5, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)62, (byte)-9, (byte)-9, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)-9, (byte)-9, (byte)-9, (byte)-1, (byte)-9, (byte)-9, (byte)-9, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)63, (byte)-9, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9};
    private static final byte[] _ORDERED_ALPHABET = new byte[]{(byte)45, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)95, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122};
    private static final byte[] _ORDERED_DECODABET = new byte[]{(byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-5, (byte)-5, (byte)-9, (byte)-9, (byte)-5, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-5, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)0, (byte)-9, (byte)-9, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)-9, (byte)-9, (byte)-9, (byte)-1, (byte)-9, (byte)-9, (byte)-9, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)37, (byte)-9, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)62, (byte)63, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9, (byte)-9};

    public Base64() {
    }

    public static byte[] decode(byte[] bytes) {
        return decode(bytes, 0, bytes.length, 0);
    }

    public static byte[] encode(byte[] bytes) {
        return encodeBytesToBytes(bytes, 0, bytes.length, 0);
    }

    public static boolean isBase64(byte[] bytes) {
        try {
            decode(bytes);
            return true;
        } catch (InvalidBase64CharacterException var2) {
            return false;
        }
    }

    private static byte[] getAlphabet(int options) {
        return (options & 16) == 16?_URL_SAFE_ALPHABET:((options & 32) == 32?_ORDERED_ALPHABET:_STANDARD_ALPHABET);
    }

    private static byte[] getDecodabet(int options) {
        return (options & 16) == 16?_URL_SAFE_DECODABET:((options & 32) == 32?_ORDERED_DECODABET:_STANDARD_DECODABET);
    }

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        byte[] ALPHABET = getAlphabet(options);
        int inBuff = (numSigBytes > 0?source[srcOffset] << 24 >>> 8:0) | (numSigBytes > 1?source[srcOffset + 1] << 24 >>> 16:0) | (numSigBytes > 2?source[srcOffset + 2] << 24 >>> 24:0);
        switch(numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
                return destination;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                destination[destOffset + 3] = 61;
                return destination;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                return destination;
            default:
                return destination;
        }
    }

    private static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) {
        if(source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        } else if(off < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + off);
        } else if(len < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + len);
        } else if(off + len > source.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[]{Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length)}));
        } else {
            boolean breakLines = (options & 8) > 0;
            int encLen = len / 3 * 4 + (len % 3 > 0?4:0);
            if(breakLines) {
                encLen += encLen / 76;
            }

            byte[] outBuff = new byte[encLen];
            int d = 0;
            int e = 0;
            int len2 = len - 2;

            for(int lineLength = 0; d < len2; e += 4) {
                encode3to4(source, d + off, 3, outBuff, e, options);
                lineLength += 4;
                if(breakLines && lineLength >= 76) {
                    outBuff[e + 4] = 10;
                    ++e;
                    lineLength = 0;
                }

                d += 3;
            }

            if(d < len) {
                encode3to4(source, d + off, len - d, outBuff, e, options);
                e += 4;
            }

            if(e <= outBuff.length - 1) {
                byte[] finalOut = new byte[e];
                System.arraycopy(outBuff, 0, finalOut, 0, e);
                return finalOut;
            } else {
                return outBuff;
            }
        }
    }

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
        if(source == null) {
            throw new NullPointerException("Source array was null.");
        } else if(destination == null) {
            throw new NullPointerException("Destination array was null.");
        } else if(srcOffset >= 0 && srcOffset + 3 < source.length) {
            if(destOffset >= 0 && destOffset + 2 < destination.length) {
                byte[] DECODABET = getDecodabet(options);
                int outBuff;
                if(source[srcOffset + 2] == 61) {
                    outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12;
                    destination[destOffset] = (byte)(outBuff >>> 16);
                    return 1;
                } else if(source[srcOffset + 3] == 61) {
                    outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12 | (DECODABET[source[srcOffset + 2]] & 255) << 6;
                    destination[destOffset] = (byte)(outBuff >>> 16);
                    destination[destOffset + 1] = (byte)(outBuff >>> 8);
                    return 2;
                } else {
                    outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12 | (DECODABET[source[srcOffset + 2]] & 255) << 6 | DECODABET[source[srcOffset + 3]] & 255;
                    destination[destOffset] = (byte)(outBuff >> 16);
                    destination[destOffset + 1] = (byte)(outBuff >> 8);
                    destination[destOffset + 2] = (byte)outBuff;
                    return 3;
                }
            } else {
                throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[]{Integer.valueOf(destination.length), Integer.valueOf(destOffset)}));
            }
        } else {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[]{Integer.valueOf(source.length), Integer.valueOf(srcOffset)}));
        }
    }

    private static byte[] decode(byte[] source, int off, int len, int options) {
        if(source == null) {
            throw new NullPointerException("Cannot decode null source array.");
        } else if(off >= 0 && off + len <= source.length) {
            if(len == 0) {
                return new byte[0];
            } else if(len < 4) {
                throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
            } else {
                byte[] DECODABET = getDecodabet(options);
                int len34 = len * 3 / 4;
                byte[] outBuff = new byte[len34];
                int outBuffPosn = 0;
                byte[] b4 = new byte[4];
                int b4Posn = 0;
                boolean i = false;
                boolean sbiDecode = false;

                for(int var13 = off; var13 < off + len; ++var13) {
                    byte var14 = DECODABET[source[var13] & 255];
                    if(var14 < -5) {
                        throw new InvalidBase64CharacterException(String.format("Bad Base64 input character decimal %d in array position %d", new Object[]{Integer.valueOf(source[var13] & 255), Integer.valueOf(var13)}));
                    }

                    if(var14 >= -1) {
                        b4[b4Posn++] = source[var13];
                        if(b4Posn > 3) {
                            outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                            b4Posn = 0;
                            if(source[var13] == 61) {
                                break;
                            }
                        }
                    }
                }

                byte[] out = new byte[outBuffPosn];
                System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
                return out;
            }
        } else {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", new Object[]{Integer.valueOf(source.length), Integer.valueOf(off), Integer.valueOf(len)}));
        }
    }
}
