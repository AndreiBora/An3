%1a. Sort a list with removing the double values. E.g.: [4 2 6 2 3 4]
% --> [2 3 4 6]

suma([],0).
suma([l(H)|T],R):-
    suma(H,R1),
    suma(T,R2),
    R is R1+R2.

suma([i(H)|T],R):-
    suma(T,R1),
    R is R1 + H.
suma([s(_)|T],R):-
    suma(T,R).

submultimi([],[]).
submultimi([_|T],S):-
    submultimi(T,S).
submultimi([H|T],[H|S]):-
    submultimi(T,S).


inserare(E,nil,arb(E,nil,nil)).
inserare(E,arb(R,S,D),arb(R,SNou,D)):-E=<R,!,inserare(E,S,SNou).
inserare(E,arb(R,S,D),arb(R,S,DNou)):-inserare(E,D,DNou).

inordine(nil).
inordine(arb(R,S,D)):-inordine(S),write(R),nl,inordine(D).

creeazaArb([],nil).

creeazaArb([H|T],Arb):-creeazaArb(T,Arb1),inserare(H,Arb1,Arb).

%3.a. Merge two sorted lists with removing the double values
%merge(L1:List,L2:List,R:List)
%(i,i,o)

mergeM([],L2,L2):-!.
mergeM(L1,[],L1):-!.
mergeM([H1|T1],[H2|T2],[H1|R]):-
    H1 < H2,
    !,
    mergeM(T1,[H2|T2],R).

mergeM([H1|T1],[H2|T2],[H2|R]):-
    H2 < H1,
    mergeM([H1|T1],T2,R).

mergeM([H1|T1],[H2|T2],[H1|R]):-
    H1 =:= H2,
    mergeM(T1,T2,R).

testMerge():-
    mergeM([1,5,9],[1,2,3,7,11],X),
    write(X).


% 3.b. For a heterogeneous list, formed from integer numbers and list of
% numbers, merge all sublists with removing the double values. [1, [2,
% 3], 4, 5, [1, 4, 6], 3, [1, 3, 7, 9, 10], 5, [1, 1, 11], 8] => [1, 2,
% 3, 4, 6, 7, 9, 10, 11].

% mergeSublist(L:list,R:list)
% (i,o)

mergeSublist([],Col,Col).
mergeSublist([H|T],Col,R):-
    is_list(H),
    !,
    merge(Col,H,R1),
    mergeSublist(T,R1,R).

mergeSublist([H|T],Col,R):-
    not(is_list(H)),
    !,
    mergeSublist(T,Col,R).


mergeSublistMain(L,R):-
    mergeSublist(L,[],R).

testMergeSublist():-
    mergeSublistMain([1, [2, 3], 4, 5, [1, 4, 6], 3, [1, 3, 7, 9, 10], 5, [1, 1, 11],8],X),
    write(X).

%4.a. Write a predicate to determine the sum of two numbers written in list representation.

%sumNr(L:list,R:list)
%(i,i,o)

%rev(L:list,R:list)
%(i,o)


rev([],Col,Col).
rev([H1|T],Col,R):-
    Col2 = [H1|Col],
    rev(T,Col2,R).

sumNr([],L2,L2).
sumNr(L1,[],L1).
sumNr([H1|T1],[H2|T2],[H3|R]):-
    H3 is H1 + H2,
    sumNr(T1,T2,R).

%normalize(L:list,flag:integer{0|1},R:list)
%(i,i,o)

normalize([],_,[]).
normalize([H|T],C,[NewDigit|R]):-
    NewDigit is (mod(H,10) + C),
    H div 10 > 0,
    C1 = 1,
    normalize(T,C1,R).

normalize([H|T],C,[NewDigit|R]):-
    NewDigit is (mod(H,10) + C),
    H div 10 =:= 0,
    C1 is 0,
    normalize(T,C1,R).

sumMain(L1,L2,R):-
    rev(L1,[],R1),
    rev(L2,[],R2),
    sumNr(R1,R2,X),
    normalize(X,0,X1),
    rev(X1,[],R).

display4():-
    sumMain([1,9,9],[1,2,3,4],X),
    write(X).


% 4.b. For a heterogeneous list, formed from integer numbers and list of
% digits, write a predicate to compute the
%sum of all numbers represented as sublists.
%Eg.: [1, [2, 3], 4, 5, [6, 7, 9], 10, 11, [1, 2, 0], 6] => [8, 2, 2].

%sumMultipleNr(L:list,Col:list,R:list)
%(i,o)

sumMultipleNr([],Col,Col).
sumMultipleNr([H|T],Col,R):-
    is_list(H),
    sumMain(H,Col,Col2),
    sumMultipleNr(T,Col2,R).

sumMultipleNr([H|T],Col,R):-
    number(H),
    sumMultipleNr(T,Col,R).



sumMultipleNrAux(L,R):-
    sumMultipleNr(L,[],R).

display4b():-
    sumMultipleNrAux([1, [2, 3], 4, 5, [6, 7, 9], 10, 11, [1, 2, 0], 6],X),
    write(X).

% 5.a. Substitute all occurrences of an element of a list with all the
% elements of another list.
% Eg. subst([1,2,1,3,1,4],1,[10,11],X) produces
% X=[10,11,2,10,11,3,10,11,4]
%

%subsList(L1:list,E:integer,L2:List,R:list)
%(i,i,i,o)
%

appendList([],L2,L2).
appendList([H1|T1],L2,[H1|R]):-
    appendList(T1,L2,R).

subsList([],_,_,[]).

subsList([E|T],E,L2,R):-
    subsList(T,E,L2,R1),
    appendList(L2,R1,R).

subsList([H|T],E,L2,[H|R]):-
    H =\= E,
    subsList(T,E,L2,R).

display5a():-
    subsList([1,2,1,3,1,4],1,[10,11],X),
    write(X).

%5. b. For a heterogeneous list, formed from integer numbers and list of
% numbers, replace in every sublist all
%occurrences of the first element from sublist it a new given list.
% Eg.: [1, [4, 1, 4], 3, 6, [7, 10, 1, 3, 9], 5, [1, 1, 1], 7] si [11,
% 11] =>
% [1, [11, 11, 1, 11, 11], 3, 6, [11, 11, 10, 1, 3, 9], 5, [11 11 11 11
% 11 11], 7]

%replaceSublist(L:list,S:list,R:list)
%(i,i,o)
%
findFirst([H|_],H).
replaceSublist([],_,[]).
replaceSublist([H|T],S,[R1|R]):-
    is_list(H),
    findFirst(H,F),
    subsList(H,F,S,R1),
    replaceSublist(T,S,R).

replaceSublist([H|T],S,[H|R]):-
    number(H),
    replaceSublist(T,S,R).

display5b():-
    replaceSublist([1, [4, 1, 4], 3, 6, [7, 10, 1, 3, 9], 5, [1, 1, 1], 7],[11,11],X),
    write(X).


%6.a. Determine the product of a number represented as digits in a list to
% a given digit.
%Eg.: [1 9 3 5 9 9] * 2 => [3 8 7 1 9 8]
%b. For a heterogeneous list, formed

reverse6([],Col,Col).
reverse6([H|T],Col,R):-
    Col2 = [H|Col],
    reverse6(T,Col2,R).

% productList(L:list,E:integer,R:list)
% (i,i,o)

productList([],_,0,[]).
productList([],_,Carry,[Carry]).
productList([H|T],E,Carry,[Digit|R]):-
    Digit is ((H * E)+ Carry) mod 10 ,
    Carry2 is ((H*E)+Carry) div 10,
    productList(T,E,Carry2,R).

productMain(L,E,R):-
    reverse6(L,[],Rev),
    productList(Rev,E,0,R1),
    reverse6(R1,[],R).

display6a():-
    productMain([1,9,3,5,9,9],2,X),
    write(X).


%6.b. For a heterogeneous list, formed from integer numbers and list of
% numbers, write a predicate to replace
% every sublist with the position of the maximum element from that
% sublist.
%[1, [2, 3], [4, 1, 4], 3, 6, [7, 10, 1, 3, 9], 5, [1, 1, 1], 7] =>
%[1, [2], [1, 3], 3, 6, [2], 5, [1, 2, 3], 7]


%return max element from a list
maxElem([E],E).
maxElem([H1,H2|T],R):-
    H1 > H2,
    maxElem([H1|T],R).
maxElem([H1,H2|T],R):-
    H1 =< H2,
    maxElem([H2|T],R).

%posElem(L:list,Pos:integer,Max:integer,Col:list,R:list)
%Return a list with the position of max element
%(i,i,i,i,o)
maxPos([],_,_,Col,Col).
maxPos([Max|T],Pos,Max,Col,R):-
    !,
    Col2 =[Pos|Col],
    Pos2 is Pos+1,
    maxPos(T,Pos2,Max,Col2,R).

maxPos([_|T],Pos,Max,Col,R):-
    Pos2 is Pos+1,
    maxPos(T,Pos2,Max,Col,R).


%replaceWithPos(L:list,R:list)
%(i,o)

replaceWithPos([],[]).
replaceWithPos([H|T],[R2|R]):-
    is_list(H),
    !,
    maxElem(H,Max),
    maxPos(H,1,Max,[],R1),
    reverse6(R1,[],R2),
    replaceWithPos(T,R).

replaceWithPos([H|T],[H|R]):-
    number(H),
    replaceWithPos(T,R).

display6b():-
    replaceWithPos([1, [2, 3], [4, 1, 4], 3, 6, [7, 10, 1, 3, 9], 5, [1, 1, 1], 7],X),
    write(X).

%7.a. Determine the position of the maximal element of a linear list.
%Eg.: maxpos([10,14,12,13,14], L) produces L = [2,5].

maxPosList(L,R):-
    maxElem(L,Max),
    maxPos(L,1,Max,[],R).

display7a():-
    maxPosList([10,14,12,13,14],X),
    reverse6(X,[],X1),
    write(X1).

% b. For a heterogeneous list, formed from integer numbers and list of
% numbers, replace every sublist with the
%position of the maximum element from that sublist.
%[1, [2, 3], [4, 1, 4], 3, 6, [7, 10, 1, 3, 9], 5, [1, 1, 1], 7] =>
%[1, [2], [1, 3], 3, 6, [2], 5, [1, 2, 3], 7]

%solved at 6b



%8.
%a. Determine the successor of a number represented as digits in a list.
%Eg.: [1 9 3 5 9 9] --> [1 9 3 6 0 0]

%succ(L:list,R:list)

successor([],0,[]).
successor([],1,[1]).
successor([H|T],Carry,[Digit|R]):-
    Digit is (H + Carry) mod 10,
    Carry2 is (H+Carry)div 10,
    successor(T,Carry2,R).

succMain(L,R):-
    reverse6(L,[],X),
    successor(X,1,X2),
    reverse6(X2,[],R).

display8a():-
    succMain([1,9,3,5,9,9],X),
    write(X).

%8 b. For a heterogeneous list, formed from integer numbers and list of
% numbers, determine the successor of a
%sublist considered as a number.
%[1, [2, 3], 4, 5, [6, 7, 9], 10, 11, [1, 2, 0], 6] =>
%[1, [2, 4], 4, 5, [6, 8, 0], 10, 11, [1, 2, 1], 6]

succList([],[]).
succList([H|T],[R1|R]):-
    is_list(H),
    !,
    succMain(H,R1),
    succList(T,R).

succList([H|T],[H|R]):-
    number(H),
    !,
    succList(T,R).

display8b():-
    succList([1, [2, 3], 4, 5, [6, 7, 9], 10, 11, [1, 2, 0], 6],X),
    write(X).


%9.a. For a list of integer number, write a predicate to add in list after 1-st, 3-rd, 7-th, 15-th element a given value e

%k1 current pos
%k2 used to compute the next pos

addAfter2([],_,_,_,[]).
addAfter2([H|T],E,K1,K2,[H,E|R]):-
    K12 is K1 + 1,
    K1 =:= K2-1,
    K22 is K2 * 2,
    addAfter2(T,E,K12,K22,R).

addAfter2([H|T],E,K1,K2,[H|R]):-
    K12 is K1 + 1,
    K1 =\= K2-1,
    addAfter2(T,E,K12,K2,R).

display9a():-
    addAfter2([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],0,1,2,X),
    write(X).

% 9.b. For a heterogeneous list, formed from integer numbers and list of
% numbers; add in every sublist after 1-st,
% 3-rd, 7-th, 15-th element the value found before the sublist in the
% heterogenous list. The list has the particularity
% that starts with a number and there aren’t two consecutive elements
% lists.
%Eg.: [1, [2, 3], 7, [4, 1, 4], 3, 6, [7, 5, 1, 3, 9, 8, 2, 7], 5] =>
% [1, [2, 1, 3], 7, [4, 7, 1, 4, 7], 3, 6, [7, 6, 5, 1, 6, 3, 9, 8, 2, 6,
% 7], 5].

%TODO later



% 10.a. For a list of integer numbers, define a predicate to write twice
% in list every prime number


isPrime(Nr,Nr):-!.
isPrime(1,_):-!,fail.
isPrime(Nr,K):-
    not(Nr mod K =:= 0),
    K1 is K + 1,
    isPrime(Nr,K1).


displayIsPrime():-
    isPrime(2,2).

repPrime([],[]).
repPrime([H|T],[H,H|R]):-
    isPrime(H,2),
    repPrime(T,R).

repPrime([H|T],[H|R]):-
    not(isPrime(H,2)),
    repPrime(T,R).

display10a():-
    repPrime([3,4,6,7,10],X),
    write(X).

% 10.b. For a heterogeneous list, formed from integer numbers and list
% of numbers, define a predicate to write in every sublist twice every
% prime number. Eg.: [1, [2, 3], 4, 5, [1, 4, 6], 3, [1, 3, 7, 9, 10],
% 5] => [1, [2, 2, 3, 3], 4, 5, [1, 4, 6], 3, [1, 3, 3, 7, 7, 9, 10], 5]

hetRepPrime([],[]).
hetRepPrime([H|T],[R1|R]):-
   is_list(H),
   repPrime(H,R1),
   hetRepPrime(T,R).

hetRepPrime([H|T],[H|R]):-
   number(H),
   hetRepPrime(T,R).

display10b():-
    hetRepPrime( [1, [2, 3], 4, 5, [1, 4, 6], 3, [1, 3, 7, 9, 10],5],X),
    write(X).



% 11.a. Replace all occurrences of an element from a list with another
% element e.

repElem([],_,_,[]).
repElem([H|T],H,NewE,[NewE|R]):-
    repElem(T,H,NewE,R).

repElem([H|T],E,NewE,[H|R]):-
    H =\= E,
    repElem(T,E,NewE,R).

display11a():-
    repElem([1,2,1,4,5],1,10,X),
    write(X).

% 11.b. For a heterogeneous list, formed from integer numbers and list
% of numbers, define a predicate to determine the maximum number of the
% list, and then to replace this value in sublists with the maximum
% value of sublist. Eg.: [1, [2, 5, 7], 4, 5, [1, 4], 3, [1, 3, 5, 8, 5,
% 4], 5, [5, 9, 1], 2] => [1, [2, 7, 7], 4, 5, [1, 4], 3, [1, 3, 8, 8,
% 8, 4], 5, [9, 9, 1], 2]

%determine max in the primary list
%version1
maxSublst([E],E).
maxSublst([H1,H2|T],R):-
    H1 >= H2,
    maxSublst([H1|T],R).

maxSublst([H1,H2|T],R):-
    H1 < H2,
    maxSublst([H2|T],R).

%version2
%(i,i,o)
maxLst([],Max,Max).
maxLst([H|T],Max,R):-
    number(H),
    H > Max,
    Max2 = H,
    maxLst(T,Max2,R).

maxLst([H|T],Max,R):-
    number(H),
    H =< Max,
    maxLst(T,Max,R).


maxLst([H|T],Max,R):-
    is_list(H),
    maxLst(T,Max,R).

testMaxLst():-
    maxLst([1,2,9,5,[1,2,4],23,3],-100,X),
    write(X).


replaceMaxSublist([],_,[]).

replaceMaxSublist([H|T],Max,[H|R]):-
    number(H),
    replaceMaxSublist(T,Max,R).


replaceMaxSublist([H|T],Max,[R1|R]):-
    is_list(H),
    maxSublst(H,MaxSub),
    repElem(H,Max,MaxSub,R1),
    replaceMaxSublist(T,Max,R).


mainMaxLst(L,R):-
    maxLst(L,-100,AbsMax),
    replaceMaxSublist(L,AbsMax,R).

display11b():-
    mainMaxLst([1, [2, 5, 7], 4, 5, [1, 4], 3, [1, 3, 5, 8, 5, 4], 5, [5, 9, 1],2 ],X),
    write(X).

% 12.a. Define a predicate to add after every element from a list, the
% divisors of that number.

%find the divisors of a number

%divisors(Nr:integer,K:integer,Col:list,R:list)
%(i,i,o)

divisors(Nr,Nr,Col,Col):-!.
divisors(1,_,_,[]):-!.
divisors(Nr,K,Col,R):-
    Nr mod K =:= 0,
    K1 is K + 1,
    Col2 = [K|Col],
    divisors(Nr,K1,Col2,R).

divisors(Nr,K,Col,R):-
    Nr mod K =\= 0,
    K1 is K + 1,
    divisors(Nr,K1,Col,R).

divisorsMain(Nr,R):-
    divisors(Nr,2,[],R).

testDivisors():-
    divisors(8,2,[],X),
    write(X).

concat3([],L2,L2).
concat3([H1|T1],L2,[H1|R]):-
    concat3(T1,L2,R).

addDivisors([],[]).

addDivisors([H|T],R):-
    addDivisors(T,R1),
    divisorsMain(H,LD),
    concat3([H|LD],R1,R).

display12a():-
    addDivisors([4,5,2,18],X),
    write(X).

% b. For a heterogeneous list, formed from integer numbers and list of
% numbers, define a predicate to add in
%every sublist the divisors of every element.
% Eg.: [1, [2, 5, 7], 4, 5, [1, 4], 3, 2, [6, 2, 1], 4, [7, 2, 8, 1], 2]
% =>
% [1, [2, 5, 7], 4, 5, [1, 4, 2], 3, 2, [6, 2, 3, 2, 1], 4, [7, 2, 8, 2,
% 4, 1], 2]


divisorLst([],[]).
divisorLst([H|T],[H,LD|R]):-
    is_list(H),
    addDivisors(H,LD),
    divisorLst(T,R).

divisorLst([H|T],[H|R]):-
    number(H),
    divisorLst(T,R).

display12b():-
    divisorLst([1, [2, 5, 7], 4, 5, [1, 4], 3, 2, [6, 2, 1], 4, [7, 2, 8, 1], 2],X),
    write(X).

