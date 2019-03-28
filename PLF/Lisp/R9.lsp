;Pb5 Scrie un program care numara cate subliste au ultimul 
;atom de la orice nivel atom nenumeric


(defun transform (L)
	(cond
		((atom L) (list L))
		(t (apply #'append (mapcar #'transform L)))
	)
)

(defun get_last(L)
	(cond
		((null (cdr L)) (car L))
		(t (get_last (cdr L)))
	)
)




(defun verifica (L)
	(cond
		((not (numberp (get_last (transform L)))) t)
		(t nil)
	)
)

(defun numara (L)
	(cond
		((atom L) 0)
		((verifica L) (+ 1 (apply #'+ (mapcar #'numara L))))
		(t (apply #'+ (mapcar #'numara L)))
	)
)

(write (numara '(A (B 2) (1 C 4) (1 (6 F)) ((G 4)6) F)))