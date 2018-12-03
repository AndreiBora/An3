%1a. Write a predicate to determine the lowest common multiple of a list
% formed from integer numbers.
% gcd(N1:Integer,N2:Integer,R:integer)
gcd(N1,0,N1):-!.
gcd(N1,N2,R):-
    N3 is N1 mod N2,
    gcd(N2,N3,R).

lcm(N1,N2,R):-
    gcd(N1,N2,R1),
    R is (N1*N2)/R1.

%lcmFromList(L:List,R:List)
%L - List
%R - result list
%(i,o)
lcmFromList([],1).
lcmFromList([H|T],R):-
    lcmFromList(T,R1),
    lcm(H,R1,R).

lcmFromList2([E],E).
lcmFromList2([H1,H2|T],R):-
    lcm(H1,H2,R1),
    lcmFromList2([R1|T],R).


%1b. Write a predicate to add a value v after 1-st, 2-nd, 4-th, 8-th, …
% element in a list.
%addToMultiple2Pos(L:list,E:integer,R:list,I1:integer,Pos:integer)
%L - input list
%E - element to be added
%R - result list
%I1 - current position
%Pos - insertion position
%(i,i,o,i)

addToMultiple2Pos([],_,[],_,_).
addToMultiple2Pos([H|T],E,[H|[E|R]],I1,Pos):-
    I1 =:= Pos,
    I2 is I1 + 1,
    Pos2 is Pos * 2,
    addToMultiple2Pos(T,E,R,I2,Pos2).

addToMultiple2Pos([H|T],E,[H|R],I1,Pos):-
    I1 =\= Pos,
    I2 is I1 + 1,
    addToMultiple2Pos(T,E,R,I2,Pos).

add(L,E,R):-
    addToMultiple2Pos(L,E,R,1,1).

% 2a. Write a predicate to remove all occurrences of a certain atom from
% a list

%remove(L:list,E:integer,R:list)
%(i,i,o)

remove([],_,[]):-!.
remove([H|T],E,[H|R]):-
    H =\= E,
    remove(T,E,R).
remove([H|T],E,R):-
    H =:= E,
    remove(T,E,R).

%b. Define a predicate to produce a list of pairs (atom n) from an initial list of atoms. In this initial list atom has
% noccurrences.
% Eg.: numberatom([1, 2, 1, 2, 1, 3, 1], X) => X = [[1, 4], [2, 2], [3, 1]].

noOcc([],_,0).
noOcc([H|T],E,R):-
    H =:= E,
    noOcc(T,E,R1),
    R is R1 + 1.

noOcc([H|T],E,R):-
    H =\= E,
    noOcc(T,E,R).


numberAtom([],[]).
numberAtom([H|T],[R1|R]):-
    noOcc([H|T],H,NrOcc),
    R1 = [H,NrOcc],
    remove(T,H,TR),
    numberAtom(TR,R).

%3.a. Define a predicate to remove from a list all repetitive elements.
%Eg.: l=[1,2,1,4,1,3,4] => l=[2,3])

%removeRep(L:list,R:list)
%(i,o)

removeRep([],[]).
removeRep([H|T],[H|R]):-
    noOcc([H|T],H,NrOcc),
    NrOcc =:= 1,
    removeRep(T,R).

removeRep([H|T],R):-
    noOcc([H|T],H,NrOcc),
    NrOcc =\= 1,
    remove(T,H,TR),
    removeRep(TR,R).


% 3.b. Remove all occurrence of a maximum value from a list on integer
% numbers

maximum([E],E):-!.
maximum([H1,H2|T],R):-
    H1 > H2,
    maximum([H1|T],R).

maximum([H1,H2|T],R):-
    H1 =< H2,
    maximum([H2|T],R).

%removeMax(L:List,R:list)
%(i,o)
removeMax(L,R):-
    maximum(L,Max),
    remove(L,Max,R1),
    R = R1.

%4.a. Write a predicate to determine the difference of two sets

%isMember(E:integer,L:integer*)
%(i,i)
isMember(E,[E|_]):-!.
isMember(E,[_|T]):-
    isMember(E,T).

%difference(L1:integer*,L2:integer*,R:integer*)

difference([],_,[]):-!.
difference([H|T],L2,R):-
    isMember(H,L2),
    difference(T,L2,R).

difference([H|T],L2,[H|R]):-
    not(isMember(H,L2)),
    difference(T,L2,R).

% 4b. Write a predicate to add value 1 after every even element from a
% list.

%addAfterEven(L,R)
%(i,o)

addAfterEvenAux([],_,[]).
addAfterEvenAux([H|T],E,[H|[E|R]]):-
    0 is H mod 2,
    addAfterEvenAux(T,E,R).

addAfterEvenAux([H|T],E,[H|R]):-
    not(0 is H mod 2),
    addAfterEvenAux(T,E,R).

addAfterEven(L,R):- addAfterEvenAux(L,1,R).

%5a. Write a predicate to compute the union of two sets
%union(L1:list,L2:list,R:list)
%(i,i,o)

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
%
/*
makePairsAux(_,[],R,R).
makePairsAux(E,[H|T],Col,R):-
    makePairsAux(E,T,[[E,H]|Col],R).
makePairs2(E,L,R):- makePairsAux(E,L,[],R).
*/

concatList([],L2,L2):-!.
concatList([H|T],L2,[H|R]):-
    concatList(T,L2,R).

pairs([E1,E2],[[E1,E2]]):-!.
pairs([H|T],R):-
    pairs(T,R1),
    makePairs(H,T,R2),
    concatList(R1,R2,R).



%6.a. Write a predicate to test if a list is a set.
%isSet(L:list)
%(i)

isSet([]):-!.
isSet([H|T]):-
    not(isMember(H,T)),
    isSet(T).

% 6.b. Write a predicate to remove the first three occurrences of an
% element in a list. If the element occurs less
% than three times, all occurrences will be removed.

%remove3(L:list,E:integer,R:list)
%(i,i,o)

remove3([],_,_,[]):-!.
remove3([H|T],E,Occ,R):-
    H =:= E,
    Occ < 3,
    Occ2 is Occ + 1,
    remove3(T,E,Occ2,R).

remove3([H|T],E,Occ,[H|R]):-
    H =:= E,
    Occ >= 3,
    Occ2 is Occ + 1,
    remove3(T,E,Occ2,R).


remove3([H|T],E,Occ,[H|R]):-
    H =\= E,
    remove3(T,E,Occ,R).


%7.a Write a predicate to test the intersection of 2 sets
%intersection7(L1:*integer,L2:*integer:R:*integer)
%(i,i,o)

intersection7([],_,[]).
intersection7([H|T],L2,[H|R]):-
    isMember(H,L2),
    intersection(T,L2,R).

intersection7([H|T],L2,R):-
    not(isMember(H,L2)),
    intersection(T,L2,R).

% 7b. Write a predicate to create a list(m..n) of all integer numbers
% from the interval [m,n]

%sublist(N:integer,M:integer,R:integer*)
%(i,i,o)


sublist7(M,M,[M]):-!.
sublist7(N,M,[N|R]):-
    N1 is N+1,
    sublist7(N1,M,R).


% 8.a. Write a predicate to determine if a list has even numbers of
% elements without counting the elements from
%the list.
%



%isListEven(L:list)
% (i)

isListEven([]).
isListEven([_,_|T]):-
    isListEven(T).

test8a():-
    isListEven([1,2,4,4]).


% 8.b. Write a predicate to delete first occurrence of the minimum
% number from a list

%first determine the minimum in a list

%minList(L:list,R:integer)
%(i,o)

minList([E],E):-!.
minList([H1,H2|T],R):-
    H1 > H2,!,
    minList([H2|T],R).

minList([H1,H2|T],R):-
    H1 =< H2,!,
    minList([H1|T],R).

testMinList():-
    minList([4,5,2,9],X),
    write(X).

%removeFirstMin(L:list,R:list)
%(i,o)

removeFirstMin([],_,[],_).
removeFirstMin([H|T],Min,R,Flag):-
    H =:= Min,
    Flag =:= 0,!,
    removeFirstMin(T,Min,R,1).

removeFirstMin([H|T],Min,[H|R],Flag):-
    removeFirstMin(T,Min,R,Flag).

removeFirstMinMain(L,R):-
    minList(L,Min),
    removeFirstMin(L,Min,R,0).

test8b():-
    removeFirstMinMain([9,2,4,2,6],X),
    write(X).

%9.a. Insert an element on the position n in a list

%insertOnPos(L:list,E:integer,K:integer,R:list)
%(i,i,i,o)

insertOnPos(L,E,0,[E|L]).
insertOnPos([H|T],E,K,[H|R]):-
    K2 is K - 1,
    insertOnPos(T,E,K2,R).

display9a():-
    insertOnPos([1,2,4,5,9],6,0,R),
    write(R).

% 9b. Define a predicate to determine the greatest common divisor of all
% numbers from a list

%gcdNr(N1:number,N2:numeber:Gcd:number)
%(i,i,o)

gcdNr(N1,0,N1):-!.
gcdNr(N1,N2,Gcd):-
    N11 is N1 mod N2,
    gcdNr(N2,N11,Gcd).

testGcd():-
    gcdNr(9,5,Nr),
    write(Nr).

gcdList([H1,H2],R):-
    gcdNr(H1,H2,R).

gcdList([H|T],R):-
    gcdList(T,R1),
    gcdNr(R1,H,R).


display9b():-
    gcdList([24,8,12],R),
    write(R).

% 10.a. Define a predicate to test if a list of an integer elements has a
% "valley" aspect (a set has a "valley" aspect if
%elements decreases up to a certain point, and then increases.

%Flag 0 - means descendent
%     1 - means ascending
%hasValleyAspect(L:list,Flag:integer)

hasValleyAspect([_],1):-!.
hasValleyAspect([H1,H2|T],Flag):-
    Flag =:= 0,
    H1 > H2,
    hasValleyAspect([H2|T],0).

hasValleyAspect([H1,H2|T],Flag):-
    Flag =:= 1,
    H1 < H2,
    hasValleyAspect([H2|T],1).

hasValleyAspect([H1,H2|T],Flag):-
    Flag =:= 0,
    H1 < H2,
    hasValleyAspect([H2|T],1).

hasValleyAspMain([H1,H2|T]):-
    H1<H2,
    hasValleyAspect([H2|T],0).

display10a():-
    hasValleyAspect([10,8,6,9,11,13],0).


% 10.b. Calculate the alternate sum of list’s elements (l1 - l2 + l3
% ...).

%sign = 1 means plus
%sign = 0 means minus
%alternateSum(L:list,Sign:integer,Col:integer,R:integer)



alternateSum([],_,Col,Col).

alternateSum([H|T],1,Col,R):-
    Col2 is Col + H,
    alternateSum(T,0,Col2,R).

alternateSum([H|T],0,Col,R):-
    Col2 is Col - H,
    alternateSum(T,1,Col2,R).

display10b():-
    alternateSum([10,8,6,9],1,0,R),
    write(R).

% 11.a. Write a predicate to substitute an element from a list with
% another element in the list.

%subs(L:list,E:integer,NewE:integer,R:list)
%(i,i,i,o)

subs([],_,_,[]).
subs([H|T],H,NewE,[NewE|R]):-
    subs(T,H,NewE,R).

subs([H|T],E,NewE,[H|R]):-
    H =\= E,
    subs(T,E,NewE,R).

display11a():-
    subs([2,4,7,1,9],7,3,X),
    write(X).

% b. Write a predicate to create the sublist (lm, …, ln) from the list
% (l1,…, lk).

%sublist(L:list,Pos:integer,m:Intger,n:integer,R:list)
%(i,i,i,o)

sublist([],_,_,_,[]).
sublist([H|T],Pos,M,N,[H|R]):-
    M =< Pos,
    N >= Pos,!,
    Pos2 is Pos + 1 ,
    sublist(T,Pos2,M,N,R).

sublist([_|T],Pos,M,N,R):-
    Pos2 is Pos + 1,
    sublist(T,Pos2,M,N,R).

display11b():-
    sublist([1,2,4,5,6,7],0,1,3,X),
    write(X).

%12.a. Write a predicate to substitute in a list a value with all the elements of another list.

%subsWithList(L:list,E:integer,S:list,R:list)
%(i,i,i,o)
%

concat([],L2,L2).
concat([H|T],L2,[H|R]):-
    concat(T,L2,R).

subsWithList([],_,_,[]).
subsWithList([H|T],H,S,R):-
    subsWithList(T,H,S,R1),
    concat(S,R1,R).

subsWithList([H|T],E,S,[H|R]):-
    H =\= E,
    subsWithList(T,E,S,R).

display12a():-
    subsWithList([1,22,4,6,123],4,[1,2,3],X),
    write(X).

%12 b. Remove the n-th element of a list.

%remNth(L:list,K:integer,R:list)
%(i,i,o)

remNth([_|T],0,T).
remNth([H|T],K,[H|R]):-
    K1 is K - 1,
    remNth(T,K1,R).

display12b():-
    remNth([1,2,4,5,5],2,X),
    write(X).

% 13.a. Transform a list in a set, in the order of the last occurrences
% of elements. Eg.: [1,2,3,1,2] is transformed in [3,1,2].

isMembr(E,[E|_]):-!.
isMembr(E,[_|T]):-
    isMembr(E,T).

toSet([],[]).

toSet([H|T],R):-
    isMembr(H,T),
    toSet(T,R).

toSet([H|T],[H|R]):-
    not(isMembr(H,T)),
    toSet(T,R).

display13a():-
    toSet([1,2,3,1,2],X),
    write(X).

%13b. Define a predicate to determine the greatest common divisor of
% all numbers in a list.
%same as 9b

% 14.a. Write a predicate to test equality of two sets without using the
% set difference.

%S1 C S2
%S2 C S1

incl([],_).
incl([H|T],S2):-
    isMembr(H,S2),
    incl(T,S2).

eqSet(S1,S2):-
    incl(S1,S2),
    incl(S2,S1).

display14a():-
    eqSet([1,2,4],[1,4,2]).

%14b. Write a predicate to select the n-th element of a given list.

getNth([H|_],0,H).
getNth([_|T],K,R):-
    K2 is K-1,
    getNth(T,K2,R).

display14b():-
    getNth([1,2,4,5],2,X),
    write(X).

%15.a. Write a predicate to transform a list in a set, considering the
% first occurrence.
% Eg: [1,2,3,1,2] is transform in [1,2,3].
%

%revert the list
%and use 13a

revertLst([],Col,Col).
revertLst([H|T],Col,R):-
    Col2 = [H|Col],
    revertLst(T,Col2,R).

displayRev():-
    revertLst([1,2,4,5,6],[],X),
    write(X).

display15a():-
    revertLst([1,2,3,1,2],[],X),
    toSet(X,R1),
    revertLst(R1,[],R),
    write(R).

% 15.b. Write a predicate to decompose a list in a list respecting the
% following: [list of even numbers list of odd
% numbers] and also return the number of even numbers and the numbers of
% odd numbers.


% decomp(L:list,Even:list,LenE:integer,Odd:list,LenO:integer,RE:list,LE:integer,RO:list,RE:integer)
% (i,i,i,i,i,o,o,o,o)

decomp([],Even,LenE,Odd,LenO,Even,LenE,Odd,LenO).

decomp([H|T],[H|Even],LenE,Odd,LenO,EvenR,LenER,OddR,LenOR):-
    H mod 2 =:= 0,
    LenE2 is LenE + 1,
    decomp([H|T],Even,LenE2,Odd,LenO,EvenR,LenER,OddR,LenOR).

decomp([H|T],Even,LenE,[H|Odd],LenO,EvenR,LenER,OddR,LenOR):-
    H mod 2 =\= 0,
    LenO2 is LenO + 1,
    decomp([H|T],Even,LenE,Odd,LenO2,EvenR,LenER,OddR,LenOR).


display15b():-
    decomp([1,2,4,5,6,7],[],0,[],0,E,LE,O,LO),
    write(E).
