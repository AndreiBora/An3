%binomial distribution
n=input('nr of trials(in N)');
p=input('prob o success(in 0, 1)');
xpdf=0:n;
ypdf=binopdf(xpdf,n,p);
plot(xpdf,ypdf,'*')

xcdf=0:0.01:n;
ycdf=binocdf(xcdf,n,p);
clf
plot(xcdf,ycdf,'r');
