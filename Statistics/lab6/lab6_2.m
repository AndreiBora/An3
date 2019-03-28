%probl 1,26 Tests for 1 population

alpha=input('significance level (in 0,1)');
x =  [7 7 4 5 9 9 ...
    4 12 8 1 8 7 ...
    3 13 2 1 17 7 ...
    12 5 6 2 1 13 ...
    14 10 2 4 9 11 ...
    3 5 12 6 10 7];%sample data

%a)
m0=input('test value=');%5.5
%H0:mu = m0 
%H1:mu > m0 right-tailed test
%t test
tail=1;
[h,p,ci,stats]=ttest(x,m0,alpha,tail);
if h == 0
    fprintf('Ho is NOT rej i.e does not exceed 5.5 on average  \n ')
else
    fprintf('Ho is rej i.e.. does exceed 5.5 on average \n ')
end

fprintf('Observed value of the test statistics is %3.5f\n',stats.tstat);
fprintf('P value of TS %1.5f \n',p);
q1=tinv(1-alpha,stats.df);
fprintf('Rejection region is  (%3.5f inf)\n',q1);

