package com.github.cassandra.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.github.cassandra.CqlFileType;
import org.jetbrains.annotations.NotNull;

public class CqlFile extends PsiFileBase {
    public CqlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CqlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return CqlFileType.INSTANCE;
    }
} 