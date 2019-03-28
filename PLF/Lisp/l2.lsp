;7. Return the level of a node X in a tree of type (1).
; The level of the root element is 0.

;version 2
(defun findLevel (L n e)
	(cond
		((null L) 0)
		((equal L e) n)
		(t (apply #'+ (mapcar #'(lambda (x) (findLevel x (+ n 1) e)) L)))
	)
)


(defun fmain (L e)
	(findLevel L -1 e)
)


;version 2

(defun inord2 (L e)
	(cond
		((null L) -1000)
		((equal e (car l)) 0)
		(t (max (+ 1 (inord2 (cadr l) e )) (+ 1 (inord2 (caddr l) e )) ))
	)
)

(defun inord2Main (e)
	(inord2 '(A (B) (C (D) (E))) e)
)


(write (fmain '(a(b(d(e))) (c(f)(g))) 'f))

;version 1
(defun getTree (l nv nm)
	(cond 
		((null l) nil)
		((equal nv (+ nm 1)) (setq treeR l) nil)
		(t (cons (car l) (cons (cadr l)(getTree (cddr l) (+ 1 nv) (+ (cadr l) nm)))))
	)
)

(defun getTreeMain (l)
	(setq treeL (getTree (cddr l) 0 0 ))
)


(defun inord (L e)
	(cond
		((null L) -1000)
		((equal (car L) e) 0)
		((equal (cadr L) 0) -1000)
		(t (max (+ 1(inord (cddr L) e)) (+ 1(inord (cddddr L) e))))
	)
)

(defun findLevelMain (L e)
	(getTreeMain L)
	(write treeL)
	(write treeR)
	(write (+ 1 (max (inord treeL e) (inord treeR e))) )
)

(findLevelMain '(a 2 b 2 c 1 i 0 f 2 g 0 x 0 d 2 e 0 h 0) 'x)
