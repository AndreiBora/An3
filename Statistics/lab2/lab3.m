%Normal distribution
mu=input('mu(in R)');
sigma=input('sigma(>0)');
%a) P(X<=0),P(X>=0)
Pa1=normcdf(0,mu,sigma);
fprintf('Pa1 = %1.4f\n',Pa1);
Pa2=1-Pa1;
fprintf('Pa2 = %1.4f\n',Pa2);
%b)P(-1<=X<=1),P(X<=-1 or X>=1)
Pb3=normcdf(1,mu,sigma)-normcdf(-1,mu,sigma);
Pb4=1-Pb1;
fprintf('Pb3 = %3.4f\n',Pb3);
fprintf('Pb4 = %3.4f\n',Pb4);

alpha=input('alpha');
beta=input('beta');
xalpha=norminv(alpha,mu,sigma);
fprintf('xalpha = %3.4f\n',xalpha);
xbeta=norminv(1-beta,mu,sigma);
fprintf('xbeta = %3.4f\n',xbeta);
%replace norm with t,chi2,f




