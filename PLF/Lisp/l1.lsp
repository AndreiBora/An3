;1.a) Write a function to return the n-th element of a list, 
; or NIL if such an element does not exist.

(defun get_nth (L P)
	(cond
		((null L) nil)
		((= P 1) (car L))
		(t (get_nth (cdr L) (- P 1)))
	)
)

;(write (get_nth '(1 2 3 4 5 6) 3))


;1.b) Write a function to check whether an atom E is a member of a list which is not necessarily linear.

(defun is_member (E L)
	(cond
		((null L) nil)
		((equal E (car L)) T)
		((listp (car L)) (or (is_member E (car L)) (is_member E (cdr L))))
		(t (is_member E (cdr L)))
	)
)

;(write (is_member 2 '(1 3 (7 a b))))

;1.c) Write a function to determine the list of all sublists of a given list, on any level.
;A sublist is either the list itself, or any element that is a list, at any level. Example:
;(1 2 (3 (4 5) (6 7)) 8 (9 10)) => 5 sublists :
;( (1 2 (3 (4 5) (6 7)) 8 (9 10)) (3 (4 5) (6 7)) (4 5) (6 7) (9 10) )

#|
(defun subl (L)
	(cond
		((null L) nil)
		((listp (car L)) (append (cons (subl (car L)) (list L)) (cons (subl (cdr L)) (car L) )))
		(t (subl (cdr L)))
	)
)

(write (subl '(1 2 (3 (4 5) (6 7)) 8 (9 10))))
|#

;1.d) Write a function to transform a linear list into a set.

(defun is_member(E L)
	(cond 
		((null L) nil)
		((= E (car L)) T)
		(t (is_member E (cdr L)))
	)
)

(defun to_set(L)
	(cond
		((null L) nil)
		((not (is_member (car L) (cdr L))) (cons (car L) (to_set (cdr L))))
		(t (to_set (cdr L)))
	)
)

;(write (to_set '(1 1 2 3 2 4)))


;3.a) Write a function that inserts in a linear list a given atom A after the 2nd, 4th, 6th, ... element.

(defun ins_after(L E Cpos Pos)
	(cond
		((null L) nil)
		((= Cpos Pos) (cons E (ins_after L E Cpos (+ 2 Pos))))
		(t (cons (car L) (ins_after (cdr L) E (+ 1 Cpos) Pos)))
	)
)

(defun ins_after_main (L E)
	(ins_after L E 1 3)
)
;(write (ins_after_main '(1 2 3 4 5 6 7 8) 9))



;3.b) Write a function to get from a given list the list of all atoms, on any
;level, but reverse order. Example:
;(((A B) C) (D E)) ==> (E D C B A)

(defun linear_rev (L)
	(cond
		((null L) nil)
		((atom (car L)) (append (linear_rev (cdr L)) (list (car L)) ))
		(t (append (linear_rev (cdr L)) (linear_rev (car L))))
	)
)

;(write (linear_rev '(((A B) C) (D E))))

;3.c) Write a function that returns the greatest common divisor of all numbers in a nonlinear list.

(defun cmmdc (A B)
	(cond
		((= B 0) A)
		(t (cmmdc B (mod A B)))
	)
)

;(write (cmmdc 12 8))

(defun linearize(L)
	(cond 
		((null L) nil)
		((numberp (car L)) (cons (car L) (linearize (cdr L))))
		((atom (car L))  (linearize (cdr L)))
		(t (append (linearize (car L)) (linearize (cdr L))))
	)
)

;(write (linearize '(((1 B) 3) (3 E))))

(defun cmmdc_lst(L)
	(cond 
		((null (cdr L)) (car L))
		(t (cmmdc (car L) (cmmdc_lst (cdr L))))
	)
)

(defun cmmdc_main(L)
	(setq L2 (linearize L))
	(cmmdc_lst L2)
)
;(write (cmmdc_main '(4 16 (8 16))))


;3d) Write a function that determines the number of occurrences of a given atom in a nonlinear list.

(defun nr_occ (L E)
	(cond 
		((null L) 0)
		((and (numberp (car L)) (= E (car L))) (+ 1 (nr_occ (cdr L) E)))
		((atom (car L)) (nr_occ (cdr L) E) )
		(t  (+ (nr_occ (car L) E) (nr_occ (cdr L) E)))
	)
)

;(write (nr_occ '(1 2 (3 2) (2 1) 2) 2 ))

;4.a) Write a function to return the sum of two vectors.

(defun sum_v(L1 L2)
	(cond
		(t (list (+ (car L1) (car L2)) (+ (cadr L1) (cadr L2))))
	)
)
	

;(write (sum_v '(1 2) '(4 5)))

;4b)Write a function to get from a given list the list of all atoms, on any
;level, but on the same order. Example:
;(((A B) C) (D E)) ==> (A B C D E)

(defun get_ord(L)
	(cond
		((null L) nil)
		((atom (car L)) (cons (car L) (get_ord (cdr L))))
		(t (append (get_ord (car L)) (get_ord (cdr L))))
	)
)

;(write (get_ord '(((A B) C) (D E))))

;c) Write a function that, with a list given as parameter, inverts only continuous
;sequences of atoms. Example:
;(a b c (d (e f) g h i)) ==> (c b a (d (f e) i h g))

;not working
#|
(defun inv_cont (L)
	(cond
		((null L) nil)
		((atom (car L)) (append (inv_cont (cdr L)) (list (car L))))
		(t (cons (inv_cont (car L)) (inv_cont (cdr L))))
	)
)
|#

;(write (inv_cont '(a b c (d (e f) g h i))))

;d) Write a list to return the maximum value of the numerical atoms from a list, at superficial level.

(defun superficial(L)
	(cond
		((null L) nil)
		((atom (car L)) (cons (car L) (superficial (cdr L))))
		(t (superficial (cdr L)))
	)
)

(defun maximum(L)
	(cond
		((null (cdr L)) (car L))
		((<= (car L) (cadr L)) (maximum (cdr L)))
		((> (car L) (cadr L)) (maximum (cons (car L) (cddr L) )))	
	)
)

;(write (maximum '(1 2 9 3 4)))

;(write (superficial '(a b c d (d (e f) g h i) v)))

(defun max_main(L)
	(setq L2 (superficial L))
	(maximum L2)
)

;(write (max_main '(1 2 (3 4 (6) 9) 9)))


;5.a) Write twice the n-th element of a linear list. Example: for (10 20 30 40 50) and n=3 will produce (10 20 30 30 40 50).

(defun twice(L N Pos)
	(cond 
		((null L) nil)
		((= N Pos) (append (list (car L) (car L)) (twice (cdr L) N (+ 1 Pos))))
		(t (cons (car L) (twice (cdr L) N (+ 1 Pos))))
	)
)

;(write (twice '(10 20 30 40 50) 3 1))

;5. b) Write a function to return an association list with the two lists given as parameters.
;Example: (A B C) (X Y Z) --> ((A.X) (B.Y) (C.Z)).
(defun asoc(L1 L2)
	(cond 
		((null L1) nil)
		(t (cons (cons (car L1) (car L2)) (asoc (cdr L1) (cdr L2)) ))
	)
)

;(write (asoc '(A B C) '(X Y Z)))

;5.c) Write a function to determine the number of all sublists of a given list, on any level.
;A sublist is either the list itself, or any element that is a list, at any level. Example:
;(1 2 (3 (4 5) (6 7)) 8 (9 10)) => 5 lists:
;(list itself, (3 ...), (4 5), (6 7), (9 10)).

(defun subs_nr (L)
	(cond
		((null L) 0)
		((atom (car L)) (subs_nr (cdr L)))
		((listp (car L)) (+ 1 (subs_nr (car L)) (subs_nr (cdr L ))))
	)
)

;add main where you add one

(write (subs_nr '(1 2 (3 (4 5) (6 7)) 8 (9 10)) ))







