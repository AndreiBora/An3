%simulate Geo(p) distr
p=input('prob of success');
%generate 1 var
X=0;%initial value
while rand >= p %count nr of failures
    X = X + 1; %failure U>=p
end

%generate a sample
N=input('NR of simmulations=');
%10,1e2,10e5
for i = 1:N
    X(i)=0;%initial value
    while rand >= p %count nr of failures
        X(i) = X(i) + 1; %failure U>=p
    end
end
%Compare graphically to the Geo(p) distr
UX=unique(X);
nX=hist(X,length(UX));
relfreq=nX/N;
%compare graphically
k=0:15;
pk=geopdf(k,p);
clf
plot(k,pk,'*',UX,relfreq,'r+')
legend('Geo Distr','Simulation')