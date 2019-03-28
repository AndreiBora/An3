;Pb5 Determina toate sublistele care au un numar impar
;de atomi nenumerici la toate nivelurile pare.


(defun nr_atoms(L N)
	(cond
		((and (atom L) (not (numberp L)) (= 0 (mod N 2))) 1)
		((and (atom L) (= 0 (mod N 2))) 0)
		((and (atom L) (= 1 (mod N 2))) 0)
		(t (apply #'+ (mapcar #'(lambda (L)
									(nr_atoms L (+ 1 N))
								)
								L
						)
			)
		)
		
	)
)

(defun nr_atoms_main (L)
	(nr_atoms L 0)
)




(defun verifica (L)
	(cond
		((= 1 (mod (nr_atoms_main L) 2)) t)
		(t nil)
	)
)

(defun numara(L)
	(cond
		((atom L) 0)
		((verifica L) (+ 1 (apply #'+ (mapcar #'numara L))))
		(t (apply #'+ (mapcar #'numara L)))
	)
)

(write (numara '(A (B 2) (1 C 4) (1 (6 F)) (((G)4)6))))