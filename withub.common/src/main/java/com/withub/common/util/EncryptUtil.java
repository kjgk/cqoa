package com.withub.common.util;


public final class EncryptUtil {

    public static String RunRC4(final String text, final String key) {

        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++)
            iS[i] = i;

        int j = 1;

        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) key.charAt((i % key.length()));
        }

        j = 0;

        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }

        int i = 0;
        j = 0;
        char[] iInputChar = text.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }

        return new String(iOutputChar);
    }

    public static String encrypt(final String text) {

        String encrypted = "";
        int mod = 5;
        for (int i = 0; i < text.length(); i++) {
            encrypted += (char) (text.getBytes()[i] + ((i + 1) % mod));
        }
        return encrypted;
    }

    public static String decrypt(final String text) {

        String encrypted = "";

        int mod = 5;
        for (int i = 0; i < text.length(); i++) {
            encrypted += (char) (text.getBytes()[i] - ((i + 1) % mod));
        }
        return encrypted;
    }

}