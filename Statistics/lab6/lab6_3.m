%Pb 2 Test for comparing 2 pop
x1=[22.4 21.7 24.5 23.4 21.6 23.3 22.4 21.6 24.8 20.0];
x2=[17.7 14.8 19.6 19.6 12.1 14.8 15.4 12.6 14.0 12.2];
alpha=input('significance level (in 0,1)');
%a) H0 sigma1=sigma2
%   H1 sigma1 != sigma2 two-tailed
tail=0;
[h,p,ci,stats]=vartest2(x1,x2,alpha,tail);

if h == 0
    fprintf('Ho is NOT rej i.e variances are equal \n ')
else
    fprintf('Ho is rej i.e.. variances are not equal \n ')
end

fprintf('Observed value of the test statistics is %3.5f\n',stats.fstat);
fprintf('P value of TS %1.5f \n',p);
q1=finv(alpha/2,stats.df1,stats.df2);
q2=finv(1-alpha/2,stats.df1,stats.df2);
fprintf('Rejection region is  (-inf %3.5f ) U (%3.5f inf)\n',q1,q2);

%b)
