;Se da o lista neliniara. Se cere nr atomilor din lista 
;la de la orice nivel

(defun nr (L)
	(cond
		((atom L) 1)
		(t (apply #'+ (mapcar #'nr L )))
	)
)


;Sa se modifice o lista cu pastrarea structurii
;astfel atomii nenumerici raman nemodificati iar 
;cei numerici isi dubleaza valoarea

(defun modif (L)
	(cond
		((numberp L) (* 2 L))
		((atom L) L)
		(t (mapcar #' modif L))
	
	)
)
;(write (modif '(a 2(5 c (d 6)))))

(defun nrApp (L e)
	(cond
		((equal L e) 1)
		((atom L) 0)
		(t (apply #'+ (mapcar #'(lambda (L) 
							(nrApp L e)
					)
					L)
			)
		)
	)
)

;(write (nrApp '(3(2(b 3)c 3)) '3 ))

;Sa se determine lungimea celei mai lungi subliste ale unei liste neliniare
;Lungimea listei se considera la nivel superficial
;(lgm '(1(2(3(4 5))(6 7) 8)3)) -> 4

;not working
(defun lgm (L)
	(cond 
		((atom L) 0)
		((listp L) (length L))
		(t (max (mapcar #'lgm L )))
	)
)

;(write (lgm '(1(2(3(4 5))(1)(6 7) 8)3)))

;Se da o lista neliniara se cere sa se elimine atomii numerici
;pastrand structura listei nemodificata

(defun elimin (L)
	(cond
		((numberp L) nil)
		((atom L) (list L))
		(t (list (apply #'append (mapcar #'elimin L))))
	)
)

;Se da un arbore n-ar. Sa se determine adancimea arborelui

(defun add (L)
	(cond 
		((null (cdr L)) 0)
		(t (+ 1 (apply #'max (mapcar #' add (cdr L)))))
	)
)

