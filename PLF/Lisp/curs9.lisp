;Se da o list numerica sa se genereze toate submultimile

;1. sa se insereze pe prima pozitie in toate sublistele

(defun ins(e l)
	(cond 
		(
			(null l)
			nil
		)	
		(
			t
			(cons (cons e (car l)) (ins e (cdr l)))
		)
	)
)

(defun subm(l)
	(cond
		(
			(null l)
			(list nil)
		)
		(
			t
			(append (subm (cdr l)) (ins (car l) (subm (cdr l))))
		)
	)
)

;sa se genereze perechile elementul e 
; si elementele listei astfel incat e < l1
(defun pereche(e l)
	(cond
		(
			(null l)
			nil
		)	
		(
			(< e (car l))
			(cons (list e (car l)) (pereche e (cdr l)))
		)
		(
			t
			(pereche e (cdr l))
		)
	)
)

;sa se genereze toate perechile unei liste
;astfel incat e < l1
(defun perechi(l)
	(cond
		(
			(null l)
			nil
		)
		(
			t
			(append (pereche (car l) (cdr l)) (perechi (cdr l)))
		)
	)
)

;sa se se insereze un element element pe o pozitie

(defun insPos(l e poz)
	(cond
		(
			(= poz 1)
			(cons e l)
		)
		(
			t
			(cons (car l) (insPos (cdr l) e (- poz 1)))
		)
	)
)