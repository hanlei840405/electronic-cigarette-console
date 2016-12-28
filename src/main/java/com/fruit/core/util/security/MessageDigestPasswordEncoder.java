package com.fruit.core.util.security;

import com.sun.tools.javac.util.Assert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JesseHan on 2016/12/28.
 */
public class MessageDigestPasswordEncoder {
    private final String algorithm;
    private int iterations;

    public MessageDigestPasswordEncoder(String algorithm) {
        this.iterations = 1;
        this.algorithm = algorithm;
        this.getMessageDigest();
    }

    public String encodePassword(String rawPass, Object salt) {
        String saltedPass = this.mergePasswordAndSalt(rawPass, salt, false);
        MessageDigest messageDigest = this.getMessageDigest();
        byte[] digest = messageDigest.digest(Utf8.encode(saltedPass));

        for (int i = 1; i < this.iterations; ++i) {
            digest = messageDigest.digest(digest);
        }

        return new String(Hex.encode(digest));
    }

    protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalArgumentException("No such algorithm [" + this.algorithm + "]");
        }
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public void setIterations(int iterations) {
        Assert.check(iterations > 0, "Iterations value must be greater than zero");
        this.iterations = iterations;
    }

    protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if (password == null) {
            password = "";
        }

        if (strict && salt != null && (salt.toString().lastIndexOf("{") != -1 || salt.toString().lastIndexOf("}") != -1)) {
            throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
        } else {
            return salt != null && !"".equals(salt) ? password + "{" + salt.toString() + "}" : password;
        }
    }
}
