%simulate bernouli Bino(n,p) distr
n=input('nr of trials=');
p=input('prob of success');
%generate 1 var
U=rand(n,1);
X=sum(U<p);%add nr of successes
%generate a sample of size N
N=input('NR of simmulations=');
%10,1e2,10e5
for i = 1:N
    U=rand(n,1);
    X(i)=sum(U<p);
end
%Compare graphically to the Bino(n,p) distr
fprintf('%1d',X);
UX=unique(X)
nX=hist(X,length(UX))
relfreq=nX/N;
%compare graphically
k=0:n;
pk=binopdf(k,n,p);
clf
plot(k,pk,'*',UX,relfreq,'r+')
legend('Bino Distr','Simulation')