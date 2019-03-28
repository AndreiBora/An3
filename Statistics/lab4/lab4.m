%simulate bernouli Bern(p) distr
p=input('p(in(0,1))');
%generate 1 var
U=rand;
X=(U<p);%success if U < p
%generate a sample of size N
N=input('NR of simmulations=');
%10,1e2,10e5
for i = 1:N
    U=rand;
    X(i)=(U<p);
end
%Compare to the Bern(p) distr
%fprintf('%1d',X);
UX=unique(X)
nX=hist(X,length(UX))
relfreq=nX/N
