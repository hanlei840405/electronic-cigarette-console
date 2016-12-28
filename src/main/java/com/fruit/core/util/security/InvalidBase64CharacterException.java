package com.fruit.core.util.security;

/**
 * Created by JesseHan on 2016/12/28.
 */
class InvalidBase64CharacterException extends IllegalArgumentException {
    InvalidBase64CharacterException(String message) {
        super(message);
    }
}