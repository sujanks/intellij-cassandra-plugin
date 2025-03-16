package com.github.cassandra;

import com.github.cassandra.lang.CqlLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CqlFileType extends LanguageFileType {
    public static final CqlFileType INSTANCE = new CqlFileType();

    private CqlFileType() {
        super(CqlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "CQL File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Cassandra Query Language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "cql";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null; // TODO: Add custom icon
    }
} 