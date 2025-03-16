package com.github.cassandra.lang;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class CqlElement extends ASTWrapperPsiElement {
    public CqlElement(@NotNull ASTNode node) {
        super(node);
    }
} 