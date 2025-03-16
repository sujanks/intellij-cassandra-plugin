package com.github.cassandra.lang;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public interface CqlTokenTypes {
    // Basic tokens
    IElementType WHITE_SPACE = new CqlElementType("WHITE_SPACE");
    IElementType COMMENT = new CqlElementType("COMMENT");
    IElementType BAD_CHARACTER = new CqlElementType("BAD_CHARACTER");
    IElementType IDENTIFIER = new CqlElementType("IDENTIFIER");
    IElementType NUMBER = new CqlElementType("NUMBER");
    IElementType STRING = new CqlElementType("STRING");

    // Punctuation
    IElementType SEMICOLON = new CqlElementType("SEMICOLON");
    IElementType COMMA = new CqlElementType("COMMA");
    IElementType DOT = new CqlElementType("DOT");
    IElementType LPAREN = new CqlElementType("LPAREN");
    IElementType RPAREN = new CqlElementType("RPAREN");
    IElementType LBRACE = new CqlElementType("LBRACE");
    IElementType RBRACE = new CqlElementType("RBRACE");
    IElementType LBRACKET = new CqlElementType("LBRACKET");
    IElementType RBRACKET = new CqlElementType("RBRACKET");
    IElementType EQ = new CqlElementType("EQ");

    // Keywords
    IElementType SELECT = new CqlElementType("SELECT");
    IElementType FROM = new CqlElementType("FROM");
    IElementType WHERE = new CqlElementType("WHERE");
    IElementType AND = new CqlElementType("AND");
    IElementType OR = new CqlElementType("OR");
    IElementType INSERT = new CqlElementType("INSERT");
    IElementType INTO = new CqlElementType("INTO");
    IElementType VALUES = new CqlElementType("VALUES");
    IElementType UPDATE = new CqlElementType("UPDATE");
    IElementType SET = new CqlElementType("SET");
    IElementType DELETE = new CqlElementType("DELETE");
    IElementType CREATE = new CqlElementType("CREATE");
    IElementType TABLE = new CqlElementType("TABLE");
    IElementType KEYSPACE = new CqlElementType("KEYSPACE");
    IElementType WITH = new CqlElementType("WITH");
    IElementType PRIMARY = new CqlElementType("PRIMARY");
    IElementType KEY = new CqlElementType("KEY");

    // Token sets
    TokenSet WHITESPACES = TokenSet.create(WHITE_SPACE);
    TokenSet COMMENTS = TokenSet.create(COMMENT);
    TokenSet STRINGS = TokenSet.create(STRING);
    TokenSet NUMBERS = TokenSet.create(NUMBER);

    // Keyword mapping
    Map<String, IElementType> KEYWORDS = new HashMap<>() {{
        put("SELECT", SELECT);
        put("FROM", FROM);
        put("WHERE", WHERE);
        put("AND", AND);
        put("OR", OR);
        put("INSERT", INSERT);
        put("INTO", INTO);
        put("VALUES", VALUES);
        put("UPDATE", UPDATE);
        put("SET", SET);
        put("DELETE", DELETE);
        put("CREATE", CREATE);
        put("TABLE", TABLE);
        put("KEYSPACE", KEYSPACE);
        put("WITH", WITH);
        put("PRIMARY", PRIMARY);
        put("KEY", KEY);
    }};

    static IElementType getKeywordToken(@NotNull String keyword) {
        return KEYWORDS.getOrDefault(keyword, IDENTIFIER);
    }
} 