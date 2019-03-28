conflevel=input('conf level (in(0,1))');%1-alpha
alpha=1-conflevel;
x1=[22.4 21.7 24.5 23.4 21.6 23.3 22.4 21.6 24.8 20.0];
x2=[17.7 14.8 19.6 19.6 12.1 14.8 15.4 12.6 14.0 12.2];
n1=length(x1);
n2=length(x2);
x1bar=mean(x1);
x2bar=mean(x2);
svar1=var(x1);
svar2=var(x2);
%a)sigma sigma1=sigma2
spsq=((n1-1)*svar1 + (n2-1)*svar2)/(n1+n2-2);
q1=tinv(1-alpha/2,n1+n2-2);
q2=tinv(alpha/2,n1+n2-2);%symetric 1-q1
ci1=x1bar - x2bar - sqrt(spsq)*q1*sqrt(1/n1 + 1/n2);
ci2=x1bar - x2bar - sqrt(spsq)*q2*sqrt(1/n1 + 1/n2);
fprintf('c.i. for the diff of population means mu1-mu2 case sigma1=sigma2 is (%3.5f %3.5f)\n',ci1,ci2);
%b)sigma1 != sigma2

c=(svar1/n1)/(svar1/n1 + svar2/n2);
oneover=c^2/(n1-1) + ((1-c)^2)/(n2-1);
n = 1/oneover;
q3=tinv(1-alpha/2,n);
q4=tinv(alpha/2,n);
ci3=x1bar - x2bar - q3*sqrt(svar1/n1 + svar2/n2);
ci4=x1bar - x2bar - q4*sqrt(svar1/n1 + svar2/n2);
fprintf('c.i. for the diff of population means mu1-mu2 case sigma1 != sigma2,unknown is (%3.5f %3.5f)\n',ci3,ci4);

%c)
q5=finv(1-alpha/2,n1-1,n2-1);
q6=finv(alpha/2,n1-1,n2-1);
ci5=(svar1/svar2)*(1/q5);
ci6=(svar1/svar2)*(1/q6);
fprintf('c.i. for the ratio of population variances is (%3.5f %3.5f)\n',ci5,ci6);




