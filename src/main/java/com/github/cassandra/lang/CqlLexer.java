package com.github.cassandra.lang;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CqlLexer extends LexerBase {
    private CharSequence buffer;
    private int bufferEnd;
    private int tokenStart;
    private int tokenEnd;
    private int bufferStart;
    private IElementType tokenType;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.bufferStart = startOffset;
        this.bufferEnd = endOffset;
        this.tokenStart = startOffset;
        this.tokenEnd = startOffset;
        advance();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        if (tokenStart >= tokenEnd) return null;
        return tokenType;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return tokenEnd;
    }

    @Override
    public void advance() {
        if (tokenEnd >= bufferEnd) {
            tokenType = null;
            tokenStart = tokenEnd;
            return;
        }

        tokenStart = tokenEnd;
        
        // Basic implementation - you'll want to expand this with proper CQL token recognition
        char c = buffer.charAt(tokenEnd++);
        
        if (Character.isWhitespace(c)) {
            tokenType = CqlTokenTypes.WHITE_SPACE;
        } else if (Character.isLetter(c)) {
            while (tokenEnd < bufferEnd && Character.isLetterOrDigit(buffer.charAt(tokenEnd))) {
                tokenEnd++;
            }
            String keyword = buffer.subSequence(tokenStart, tokenEnd).toString().toUpperCase();
            tokenType = CqlTokenTypes.getKeywordToken(keyword);
        } else if (Character.isDigit(c)) {
            while (tokenEnd < bufferEnd && Character.isDigit(buffer.charAt(tokenEnd))) {
                tokenEnd++;
            }
            tokenType = CqlTokenTypes.NUMBER;
        } else {
            switch (c) {
                case '(' -> tokenType = CqlTokenTypes.LPAREN;
                case ')' -> tokenType = CqlTokenTypes.RPAREN;
                case '{' -> tokenType = CqlTokenTypes.LBRACE;
                case '}' -> tokenType = CqlTokenTypes.RBRACE;
                case '[' -> tokenType = CqlTokenTypes.LBRACKET;
                case ']' -> tokenType = CqlTokenTypes.RBRACKET;
                case ';' -> tokenType = CqlTokenTypes.SEMICOLON;
                case ',' -> tokenType = CqlTokenTypes.COMMA;
                case '=' -> tokenType = CqlTokenTypes.EQ;
                default -> tokenType = CqlTokenTypes.BAD_CHARACTER;
            }
        }
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return bufferEnd;
    }
} 