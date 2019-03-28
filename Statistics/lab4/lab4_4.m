%simulate Pascal(n,p) distr
p=input('prob of success');
n=input('rank');


N=input('NR of simmulations=');
%10,1e2,10e5
for i = 1:N
    for j = 1:n
        Y(j)=0;%initial value
        while (rand >= p) %count nr of failures
            Y(j) = Y(j) + 1; %failure U>=p
        end;
    end
    X(i)=sum(Y);
end
%Compare graphically to the Geo(p) distr
UX=unique(X);
nX=hist(X,length(UX));
relfreq=nX/N;
%compare graphically
k=0:100;
pk=nbinpdf(k,n,p);
clf
plot(k,pk,'*',UX,relfreq,'r+')
legend('Pascal','Simulation')