x=0:0.01:3;%simulate continuity
y1=x.^4/10;
y2=x.*sin(x);
y3=cos(x);
plot(x,y1,'r*-.',x,y2,x,y3,'m^-')
%after plot
title('This is my grapph')
legend('f1','f2','f3','Location','NorthEastOutside')