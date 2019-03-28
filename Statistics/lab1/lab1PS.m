A=[1 0 -2; 2 1 3; 0 1 0];
B=[2 1 1; 1 0 -1; 1 1 0];
C=A-B
D=A*B
E=A.*B

fprintf('Matrix C is :\n')
fprintf('%2d %2d %2d\n',C')
fprintf('Matrix D is :\n')
fprintf('%2d %2d %2d\n',D')
fprintf('Matrix E is :\n')
fprintf('%2d %2d %2d\n',E')