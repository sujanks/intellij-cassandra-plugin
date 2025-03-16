package com.github.cassandra.lang;

import com.intellij.lang.Language;

public class CqlLanguage extends Language {
    public static final CqlLanguage INSTANCE = new CqlLanguage();

    private CqlLanguage() {
        super("CQL");
    }
} 