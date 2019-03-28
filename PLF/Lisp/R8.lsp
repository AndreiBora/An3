;R8 4 Write a lisp program that return a new list with every n-th element removed

(defun rem_pos(L N Pos)
	(cond
		((null L) nil)
		((= 0 (mod Pos N)) (rem_pos (cdr L) N (+ 1 Pos)))
		(t (cons (car L) (rem_pos (cdr L) N (+ 1 Pos))))
	)
)

(defun rem_main (L N)
	(rem_pos L N 1)
)

;(write (rem_main '(1 2 3 4 5 6 ) 2))


;R7 4.Write a program that replace a nonlinear numbers with thier successor

(defun rep_succ (L)
	(cond
		((null L) nil)
		((and (numberp (car L)) (= 0 (mod (car L) 2))  (cons (+ 1 (car L)) (rep_succ (cdr L)) )))
		((atom (car L)) (cons (car L) (rep_succ (cdr L))))
		(t (cons (rep_succ (car L)) (rep_succ (cdr L)) ))
	)
)

;(write (rep_succ '(1 s 4 (2 f (7)))))






;R9 4.Write a lisp program with all the occurences of a given element removed

(defun rem_e(L E)
	(cond 
		((null L) nil)
		((equal E (car L)) (rem_e (cdr l) E))
		((atom (car L)) (cons (car L) (rem_e (cdr l) E)))
		(t (cons (rem_e (car L) E) (rem_e (cdr L) E)))
	)
)

;(write (rem_e '(1(2 A(3 A)) (A)) 'A ))

;R9 5. Write a program to determine the number of sublist at any level of a given list
;where the last atom (at any level ) is nonnumeric.The list processing will be done using MAP function


(defun nr_sublst(L)
	(cons
		((atom L) 0)
		((verif L) 1)
		(t (apply #'+ (mapcar #'nr_sublst L)))
	)
)

;R10 4 write a program that duplicates from N to N numbers

(defun dup(L N Pos)
	(cond 
		((null L) nil)
		((= 0 (mod Pos N)) (append (list (car L) (car L)) (dup (cdr L) N (+ 1 Pos))) )
		(t (cons (car L) (dup (cdr L) N (+ 1 Pos))))
	)
)

;(write (dup '(1 2 3 4 5) 2 1))


