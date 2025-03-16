package com.github.cassandra.highlighting;

import com.github.cassandra.lang.CqlLexer;
import com.github.cassandra.lang.CqlTokenTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class CqlSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD = 
            createTextAttributesKey("CQL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("CQL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("CQL_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("CQL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey OPERATOR =
            createTextAttributesKey("CQL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey PARENTHESES =
            createTextAttributesKey("CQL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BRACES =
            createTextAttributesKey("CQL_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS =
            createTextAttributesKey("CQL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("CQL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new CqlLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (CqlTokenTypes.KEYWORDS.containsValue(tokenType)) {
            return pack(KEYWORD);
        }
        if (tokenType == CqlTokenTypes.NUMBER) {
            return pack(NUMBER);
        }
        if (tokenType == CqlTokenTypes.STRING) {
            return pack(STRING);
        }
        if (tokenType == CqlTokenTypes.COMMENT) {
            return pack(COMMENT);
        }
        if (tokenType == CqlTokenTypes.LPAREN || tokenType == CqlTokenTypes.RPAREN) {
            return pack(PARENTHESES);
        }
        if (tokenType == CqlTokenTypes.LBRACE || tokenType == CqlTokenTypes.RBRACE) {
            return pack(BRACES);
        }
        if (tokenType == CqlTokenTypes.LBRACKET || tokenType == CqlTokenTypes.RBRACKET) {
            return pack(BRACKETS);
        }
        if (tokenType == CqlTokenTypes.IDENTIFIER) {
            return pack(IDENTIFIER);
        }
        if (tokenType == CqlTokenTypes.EQ || tokenType == CqlTokenTypes.COMMA || 
            tokenType == CqlTokenTypes.SEMICOLON || tokenType == CqlTokenTypes.DOT) {
            return pack(OPERATOR);
        }
        return TextAttributesKey.EMPTY_ARRAY;
    }
} 