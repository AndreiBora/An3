%5a. Write a predicate to compute the union of two sets
%union(L1:*integer,L2:*integer,R:*integer)
%(i,i,o)

%isMember(E:integer,L:integer*)
%(i,i)
isMember(E,[E|_]):-!.
isMember(E,[_|T]):-
    isMember(E,T).


union2([],L2,L2).
union2([H|T],L2,[H|R]):-
    not(isMember(H,L2)),
    union(T,L2,R).
union2([H|T],L2,R):-
    isMember(H,L2),
    union(T,L2,R).


% 5b. Write a predicate to determine the set of all the pairs of elements
% in a list.
% Eg.: L = [a b c d] => [[a b] [a c] [a d] [b c] [b d] [c d]].

% makePairs(E,L,R)
% (i,i,o)
%
makePairs(_,[],[]).
makePairs(E,[H|T],[[E,H]|R]):-
    makePairs(E,T,R).

%makePairsAux(E,L,Col,Rez)
%(i,i,i,0)


concatList([],L2,L2):-!.
concatList([H|T],L2,[H|R]):-
    concatList(T,L2,R).

%pair(L:integer*,R:integer*)
%(i,o)

pairs([E1,E2],[[E1,E2]]):-!.
pairs([H|T],R):-
    pairs(T,R1),
    makePairs(H,T,R2),
    concatList(R1,R2,R).













