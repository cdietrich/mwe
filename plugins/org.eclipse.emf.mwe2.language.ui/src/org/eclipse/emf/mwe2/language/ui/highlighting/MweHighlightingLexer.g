/*
 * generated by Xtext
 */
lexer grammar MweHighlightingLexer;

@header {
package org.eclipse.emf.mwe2.language.ui.highlighting;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

// Module
KEYWORD_MODULE : 'module';

// DeclaredProperty
KEYWORD_VAR : 'var';
KEYWORD_ASSIGN : '=';

// RootComponent
KEYWORD_AT : '@';
KEYWORD_COLON : ':';
KEYWORD_AUTO_INJECT : 'auto-inject';
KEYWORD_OPENBRACE : '{';
KEYWORD_CLOSINGBRACE : '}';

// Import
KEYWORD_IMPORT : 'import';

// ImportedFQN
KEYWORD_DOT : '.';
KEYWORD_STAR : '.*';

// BooleanLiteral
KEYWORD_FALSE : 'false';
KEYWORD_TRUE : 'true';

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

// RULE_INT : ('0'..'9')+;

// StringLiteral
RULE_STRING : 
  (
    '"'  ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\'|'${'|'//'|'/*') | ~(('\\'|'"'))  )* '"'
  | '\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\'|'${'|'//'|'/*') | ~(('\\'|'\'')) )* '\''
  );

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;
