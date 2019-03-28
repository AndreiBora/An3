%probl 1,26 Tests for 1 population

alpha=input('significance level (in 0,1)');
x =  [7 7 4 5 9 9 ...
    4 12 8 1 8 7 ...
    3 13 2 1 17 7 ...
    12 5 6 2 1 13 ...
    14 10 2 4 9 11 ...
    3 5 12 6 10 7];%sample data

%a)
m0=input('test value=');%9
%H0:mu = m0 ( > )
%H1:mu < m0 left-tailed test
sigma=5;%case sigma known
%Z test
[h,p,ci,zval]=ztest(x,m0,sigma,alpha,-1);
if h == 0
    fprintf('Ho is NOT rej i.e standard is met  \n ')
else
    fprintf('Ho is rej i.e.. standard is not met \n ')
end

fprintf('Observed value of the test statistics is %3.5f\n',zval);
fprintf('P value of TS %1.5f \n',p);
q1=norminv(alpha,0,1);
fprintf('Rejection region is (-inf, %3.5f)\n',q1);

%b)
