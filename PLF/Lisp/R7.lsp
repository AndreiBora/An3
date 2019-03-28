;R7 Scrie un program care determina toate sublistele la care
;maximul de pe toate nivele impare este un numar par


(defun max_odd_level(L N)
	(cond
		((and (numberp L) (= 1 (mod N 2))) L)
		((and (atom L) (= 1 (mod N 2))) -101)
		((and (atom L) (= 0 (mod N 2))) -101)
		(t (apply #'max (mapcar #'(lambda (L)
										(max_odd_level L (+ 1 N))
									)
									L
						)
			)
		)
	)
)

(defun max_main(L)
	(max_odd_level L 0)
)

;(write (max_odd_level '(1 s 4 (99 f (2))) 0))

(defun verifica (L)
	(cond
		((= 0 (mod (max_main L) 2)) t )
		(t nil)
	)
)

(defun numara(L)
	(cond
		((atom L) 0)
		((not (null (verifica L))) (+ 1 (apply #'+ (mapcar #'numara L))))
		(t (apply #'+ (mapcar #'numara L)))
	)
)

(write (numara '(A (B 2) (1 C 4) (1 (6 F)) (((G)4)6)) ))