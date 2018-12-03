%determine the max from a list
%max(L:list,R:integer)
%(i,o)

max([E],E):-!.
max([H1,H2|T],R):-
    H1 > H2,
    !,
    max([H1|T],R).

max([H1,H2|T],R):-
    H1 =< H2,
    !,
    max([H2|T],R).


testMax():-
    max([1,2,10,3],X),
    write(X).

%gcd(N1,N2,R)
%(i,i,0)

gcd(N1,0,N1):-!.

gcd(N1,N2,R):-
    N11 is N1 mod N2,
    gcd(N2,N11,R).

lcm(N1,N2,R):-
    gcd(N1,N2,R1),
    R is (N1*N2)/R1.

testLcm():-
    lcm(4,5,X),
    write(X).

testGcd():-
    gcd(12,8,X),
    write(X).

%remove gcd from list
%rmGcd(L:list,R:list)
%(i,o)

rmGcd([],_,[]).
rmGcd([H|T],E,R):-
    H =:= E,
    rmGcd(T,E,R).


rmGcd([H|T],E,[H|R]):-
    H =:= E,
    rmGcd(T,E,R).


%gcdList(L:list,R:list).
%(i,o)

gcdList([H1,H2],R):-
    gcd(H1,H2,R).
gcdList([H|T],R):-
    gcdList(T,R1),
    gcd(R1,H,R).

testGcdList():-
    gcdList([6,8,12],X),
    write(X).

rmGcdMain(L):-
    gcdList(L,R),
    rmGcd(L,R,X),
    write(X).

testrmGcdList():-
    rmGcdMain([2,8,12]).


%insert the gcd of a list on the even position
%insEvenPos(L:list,Pos:integer,G:integer:R:list)
%(i,i,o)
%
%
insEvenPos([],_,_,[]).
insEvenPos([H|T],Pos,Gcd,[Gcd,H|R]):-
    Pos mod 2 =:= 0,
    Pos2 is Pos + 1,
    insEvenPos(T,Pos2,Gcd,R).

insEvenPos([H|T],Pos,Gcd,[H|R]):-
    Pos mod 2 =\= 0,
    Pos2 is Pos + 1,
    insEvenPos(T,Pos2,Gcd,R).

insEvenPosMain(L,R):-
    gcdList(L,G),
    insEvenPos(L,1,G,R).

testEvenIns():-
    insEvenPosMain([2,4,8,6,8],X),
    write(X).





















