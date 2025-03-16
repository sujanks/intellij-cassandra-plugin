package com.github.cassandra.completion;

import com.github.cassandra.lang.CqlLanguage;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class CqlCompletionContributor extends CompletionContributor {
    private static final String[] KEYWORDS = {
        "SELECT", "FROM", "WHERE", "AND", "OR", "INSERT", "INTO", "VALUES",
        "UPDATE", "SET", "DELETE", "CREATE", "TABLE", "KEYSPACE", "WITH",
        "PRIMARY KEY", "CLUSTERING ORDER", "TEXT", "INT", "BOOLEAN", "TIMESTAMP",
        "UUID", "TIMEUUID", "BLOB", "COUNTER", "DECIMAL", "DOUBLE", "FLOAT",
        "INET", "LIST", "MAP", "SET", "TUPLE", "VARCHAR", "VARINT"
    };

    public CqlCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(CqlLanguage.INSTANCE),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                @NotNull ProcessingContext context,
                                                @NotNull CompletionResultSet result) {
                        for (String keyword : KEYWORDS) {
                            result.addElement(LookupElementBuilder.create(keyword));
                        }
                    }
                }
        );
    }
} 