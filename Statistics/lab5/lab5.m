%lab 5 partb probl1
%conf to enter 0.95
conflevel=input('conf level (in(0,1))');%1-alpha
alpha=1-conflevel;
x=  [7 7 4 5 9 9 ...
    4 12 8 1 8 7 ...
    3 13 2 1 17 7 ...
    12 5 6 2 1 13 ...
    14 10 2 4 9 11 ...
    3 5 12 6 10 7];%sample data

n=length(x);
xbar=mean(x);
%a) sigma known
sigma=5;
q1=norminv(1-alpha/2,0,1);
q2=norminv(alpha/2,0,1);%q2-q1 symetry of distribution N(0,1)
ci1=xbar-(sigma/sqrt(n)*q1);
ci2=xbar-(sigma/sqrt(n)*q2);
%fprintf('c.i.for the population mean ,mu,case sigma known is (%3.5f %3.5f)\n',ci1,ci2);

%b) sigma unknown
s=std(x);
q2=tinv(1-alpha/2,n-1);
q3=tinv(alpha/2,n-1);%q2-q1 symetry of distribution N(0,1)

ci3=xbar-(s/sqrt(n)*q2);
ci4=xbar-(s/sqrt(n)*q3);
fprintf('c.i.for the population mean ,mu,case sigma unknown is (%3.5f  %3.5f)\n',ci3,ci4);

%c)
svar=var(x); %s^2 the sample variance
q5=chi2inv(1-alpha/2,n-1);
q6=chi2inv(alpha/2,n-1);%not symetric

ci5=((n-1)*svar)/q5;
ci6=((n-1)*svar)/q6;
fprintf('c.i.for the population variance is (%3.5f)\n',ci5,ci6);
fprintf('c.i.for the population standard deviation(sigma) is (%3.5f %3.5f)\n',sqrt(ci5),sqrt(ci6));
