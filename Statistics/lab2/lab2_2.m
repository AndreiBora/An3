Pc1=binopdf(0,3,1/2);
Pc2=1-binopdf(1,3,1/2);
fprintf('Prob 1 in point c is %1.4f\n',Pc1);
fprintf('Prob 2 in point c is %1.4f\n',Pc2);
%d
Pd1=binocdf(2,3,1/2);
Pd2=binocdf(1,3,1/2);
fprintf('Prob 3 in point c is %1.4f\n',Pd1);
fprintf('Prob 4 in point c is %1.4f\n',Pd2);
Pe1=1-binocdf(0,3,1/2);
Pe2=1-binocdf(1,3,1/2);
fprintf('Prob 5 in point c is %1.4f\n',Pe1);
fprintf('Prob 6 in point c is %1.4f\n',Pe2);

%P(a<X<=b)=F(b)-F(a)
