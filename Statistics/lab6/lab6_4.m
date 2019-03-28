%Pb 2 Test for comparing 2 pop
x1=[22.4 21.7 24.5 23.4 21.6 23.3 22.4 21.6 24.8 20.0];
x2=[17.7 14.8 19.6 19.6 12.1 14.8 15.4 12.6 14.0 12.2];
alpha=input('significance level (in 0,1)');
%a) H0 mu1 = mu2
%   H1 mu1 > mu2 right-tailed
tail=1;
%vartype='equal' or 'unequal'
vartype='equal';
[h,p,ci,stats]=ttest2(x1,x2,alpha,tail,vartype);

if h == 0
    fprintf('Ho is NOT rej i.e is not higher \n ')
else
    fprintf('Ho is rej i.e.. higher  \n ')
end

fprintf('Observed value of the test statistics is %3.5f\n',stats.tstat);
fprintf('P value of TS %e \n',p);
q2=tinv(1-alpha,stats.df);
fprintf('Rejection region is (%3.5f inf)\n',q2);