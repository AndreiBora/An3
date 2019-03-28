%{
	#include <string.h>
	#include <stdio.h>
	#include <stdlib.h>
	extern int yylex();
	extern int yyparse();
	extern FILE *yyin;
	void yyerror(const char *s);
	extern int yylineno;
%}

%token		CONSTANT
%token		IDENTIFIER
%token		MAIN
%token		CIN
%token		COUT
%token		WHILE
%token		IF
%token		ELSE
%token		INT
%token		FLOAT
%token		CHAR
%token		GT
%token		GTE
%token		LTE
%token		LT
%token		EQ
%token		NE
%token		RETURN
%token		STRUCT

%%
program				:	INT MAIN '(' ')' compound_stmt;
compound_stmt			:	'{' stmt_list '}';
stmt_list			:	stmt ';'
				|	stmt ';' stmt_list;
stmt				:	decl
				|	assign
				|	returnstmt
				|	iostmt
				|	loop
				|	if_stmt;
decl				:	type IDENTIFIER
				|	user_type IDENTIFIER;
assign				:	IDENTIFIER '=' expression;
expression			:	expression op1 term
				|	term;
term				:	term op2 fact
				|	fact;
fact				:	IDENTIFIER
				|	CONSTANT
				|	'(' expression ')';
op1				:	'+'
				|	'-';
op2				:	'*'
				|	'/'
				|	'%';
returnstmt			:	RETURN expression;
iostmt				:	input
				|	output;
input				:	CIN	IDENTIFIER;
output				:	COUT IDENTIFIER;
loop				:	WHILE '(' condition ')' stmt;
condition			:	expression relation expression;
if_stmt				:	IF '(' expression ')' compound_stmt
				|	IF '(' expression ')' compound_stmt ELSE compound_stmt;
user_type			:	STRUCT IDENTIFIER '{' decl '}';
relation			:	LT
				|	LTE
				|	EQ
				|	NE
				|	GTE
				|	GT;
type				:	INT
				|	FLOAT
				|	CHAR;


%%
int main(int argc, char *argv[]) {
    ++argv, --argc; /* skip over program name */ 
    
    // sets the input for flex file
    if (argc > 0) 
        yyin = fopen(argv[0], "r"); 
    else 
        yyin = stdin; 
    
    while (!feof(yyin)) {
        yyparse();
    }
    printf("The file is sintactly correct!\n");
    return 0;
}

void yyerror(const char *s) {
    printf("Error: %s at line -> %d ! \n",s,yylineno);
    exit(1);
}
 