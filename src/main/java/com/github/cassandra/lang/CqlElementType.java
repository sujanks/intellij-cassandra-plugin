package com.github.cassandra.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CqlElementType extends IElementType {
    public CqlElementType(@NotNull @NonNls String debugName) {
        super(debugName, CqlLanguage.INSTANCE);
    }
} 