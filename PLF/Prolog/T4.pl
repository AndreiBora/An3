% Two integers, n and m are given. Write a predicate to determine all
% possible sequences of numbers
% from 1 to n, such that between any two numbers from consecutive
% positions, the absolute difference
% to be >= m.

%candidate(N:integer,E:integer)
%(i,o)
%nedeterminist

%math model
%1.n
%2.candidate(n-1), n > 1

candidate(N,N).
candidate(N,E):-
    N > 1,
    N1 is N - 1,
    candidate(N1,E).


isMember(E,[E|_]).
isMember(E,[_|T]):-
    isMember(E,T).

%perm(N:integer,M:integer,R:integer)
%(i,i,o)
perm(N,M,R):-
    candidate(N,E),
    perm_aux(N,M,R,[E],1).

perm_aux(N,_,Col,Col,N):-!.

perm_aux(N,M,R,[H|Col],LenCol):-
    candidate(N,E),
    not(isMember(E,Col)),
    abs(H-E) >= M,
    LenCol2 is LenCol +1,
    perm_aux(N,M,R,[E,H|Col],LenCol2).

perm_main(N,M):-
    findall(X,perm(N,M,X),R),
    write(R).

