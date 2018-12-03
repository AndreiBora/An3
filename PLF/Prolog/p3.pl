% 9.a
% a. For a list of integer number, write a predicate to add in list after
% 1-st, 3-rd, %7-th, 15-th element a given value e
%insertOnPos(L:List,E:integer,pos:integer,pos2:integer,R:List)
%(i,i,i,i,o)
%

insertOnPos([],_,_,_,[]).

insertOnPos([H|T],E,Pos,Pos2,[H|R]):-
    Pos2 mod Pos =\= 0,
    !,
    Pos3 is Pos + 1,
    insertOnPos(T,E,Pos3,Pos2,R).

insertOnPos([H|T],E,Pos,Pos2,[H|[E|R]]):-
    Pos2 mod Pos =:= 0,
    Pos =< 15,
    !,
    Pos3 is Pos + 1,
    Pos4 is Pos2*2+1,
    insertOnPos(T,E,Pos3,Pos4,R).


insertOnPosAux(L,E,X):-
    insertOnPos(L,E,1,1,X).
display():-
    %insertOnPos([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],0,1,1,X),
    insertOnPosAux([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],0,X),
    write(X).


/*

b. For a heterogeneous list, formed from integer numbers and list of numbers; add in every sublist after 1-st,
3-rd, 7-th, 15-th element the value found before the sublist in the heterogenous list. The list has the particularity
that starts with a number and there aren’t two consecutive elements lists.
Eg.: [1, [2, 3], 7, [4, 1, 4], 3, 6, [7, 5, 1, 3, 9, 8, 2, 7], 5] =>
[1, [2, 1, 3], 7, [4, 7, 1, 4, 7], 3, 6, [7, 6, 5, 1, 6, 3, 9, 8, 2, 6, 7], 5].
*/

%insertOnPosSublist(L:list,R:list)
%(i,o)
%


insertOnPosSublist([],[]):-!.
insertOnPosSublist([E],[E]):-!.
insertOnPosSublist([H1,H2|T],[H1,R1|R]):-
    is_list(H2),
    !,
    insertOnPosAux(H2,H1,R1),
    insertOnPosSublist(T,R).

insertOnPosSublist([H1,H2|T],[H1|R]):-
    \+ is_list(H2),
    insertOnPosSublist([H2|T],R).

displayB():-
    insertOnPosSublist([1, [2, 3], 7, [4, 1, 4], 3, 6, [7, 5, 1, 3, 9, 8, 2, 7], 5],X),
    write(X).
























