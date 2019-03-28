;1. Write a function to check if an atom is member of a list (the list is non-liniar)

(defun nr_app(L E)
	(cond
		((equal L E) 1)
		((atom L) 0)
		(t (apply #'+ (mapcar #'(lambda (L) 
									(nr_app L E)
								)
								L
						)
			)
		)
	)
)

(defun check (L E)
	(cond
		((= (nr_app L E) 0) nil)
		(t T)
	)
)

;(write (check '(1 2 (3 5)) 6))

;2. Write a function that returns the sum of numeric atoms in a list, at any level.

(defun suma (L)
	(cond
		((numberp L) L)
		((atom L) 0)
		(t (apply #'+ (mapcar #'suma L) ))
	)
)

;(write (suma '(1 2(a 3) v 4)))

;3. Define a function to tests the membership of a node in a n-tree represented as
; (root list_of_nodes_subtree1 ... list_of_nodes_subtreen)
;Eg. tree is (a (b (c)) (d) (E (f))) and the node is "b" => true

(defun is_member(L E)
	(cond
		((equal L E) 1)
		((atom L) 0)
		(t (apply #'+ (mapcar #'(lambda (L)  
									(is_member L E)
								) 
								L
						)
			)
		)
	)
)

;(write (is_member '(a (b) (c (d) (e)) (f (g))) 'c))


;4. Write a function that returns the product of numeric atoms in a list, at any level.

(defun prod(L)
	(cond
		((numberp L) L)
		((atom L) 1)
		(t (apply #'* (mapcar #'prod L)))
	)
)
;(write (prod '(1 2 (a b 3) 2)))


;5. Write a function that computes the sum of even numbers and the decrease the sum of odd numbers, at any level of a list.

(defun sum_e_o (L)
	(cond
		((and (numberp L) (= 0 (mod L 2))) L )
		((numberp L) (- 0 L) )
		((atom L) 0)
		(t (apply #'+ (mapcar #'sum_e_o L)))
	)
)

;(write (sum_e_o '(1 2 (a b 3) c)))

;6. Write a function that returns the maximum of numeric atoms in a list, at any level.

(defun maximum (L)
	(cond
		((numberp L) L)
		((atom L) -100)
		(t (apply #'max (mapcar #' maximum L) ))
	)
)

;(write (maximum '(1 2 (a b 3) c)))

;7. Write a function that substitutes an element E with all elements of a list L1 at all levels of a given list L.

(defun inlocuire (L E S)
	(cond
		((equal L E) S)
		((atom L) (list L))
		(t (list(apply #' append (mapcar #'(lambda (L)
										(inlocuire L E S)
									)
									L
							)
			)
			)
		)
	)
)

;(write (inlocuire '(1 2 (5 7 3) 2) 2 '(9 7) ))

;8. Write a function to determine the number of nodes on the level k from a n-tree represented as follows: (root list_nodes_subtree1 ... list_nodes_subtreen)
;Eg: tree is (a (b (c)) (d) (e (f))) and k=1 => 3 nodes

(defun nr_nodes(L K)
	(cond
		((= K 0) 1 )
		((atom L) 0)
		(t (apply #'+ (mapcar #'(lambda (L)
									(nr_nodes L (- K 1))
								)
								(cdr L)
					)
			)
		)
	)
)

;(write (nr_nodes '(a (b (c)) (d) (e (f))) 2))


;9. Write a function that removes all occurrences of an atom from any level of a list.
(defun rem_e(L E)
	(cond
		((equal E L) nil)
		((atom L) (list L))
		(t (list(apply #'append (mapcar #'(lambda (L) 
										(rem_e L E)
									)
									L
							)	
			)
			)
		)
	)
)

(defun rem_main (L E)
	(car (rem_e L E))
)

;(write (rem_main '(a (b (c)) (d e) (e (f))) 'e))

;10. Define a function that replaces one node with another one in a n-tree represented as: root list_of_nodes_subtree1... list_of_nodes_subtreen)
;Eg: tree is (a (b (c)) (d) (e (f))) and node 'b' will be replace with node 'g' => tree (a (g (c)) (d) (e (f)))}

(defun rep_node(L E N)
	(cond
		((equal E L) (list N))
		((atom L) (list L))
		(t (list(apply #'append (mapcar #'(lambda (L)
										(rep_node L E N)
									) 
									L
							)
			)
			)
		)
	)
)

;(write (rep_node '(a (b (c)) (d) (e (f)))  'b 'g))

;11. Write a function to determine the depth of a list.

(defun depth_lst(L)
	(cond
		((atom L) 0)
		(t (+ 1 (apply #'max (mapcar #'depth_lst L))))
	)
)

;(write (depth_lst '((a (b (c d))) e (f (g h (i))) )))

;12. Write a function that substitutes an element through another one at all levels of a given list.

(defun subs (l e new)
	(cond
		((and(atom l) (equal l e)) new)
		((atom l) l)
		(t (mapcar #'(lambda (x) (subs x e new)) l))
	)
)
;(write (subs '(a (b (c (d))) (d) (e (f))) 'a 'g) )

;13. Define a function that returns the depth of a tree represented as (root list_of_nodes_subtree1 ... list_of_nodes_subtreen)
;Eg. the depth of the tree (a (b (c)) (d) (e (f))) is 3

(defun depth_tree (L)
	(cond
		((null (cdr L)) 0)
		(t (+ 1 (apply #'max (mapcar #'depth_tree  (cdr L)))))
	)
)

;(write(depth_tree '(a (b (c)) (d) (e (f)))))

;14. Write a function that returns the number of atoms in a list, at any level.

(defun nr_atoms(L)
	(cond
		((atom L) 1)
		(t (apply #'+ (mapcar #'nr_atoms L)))
	)
)

;(write (nr_atoms '(a (b (c)) (d) (e (f))) ))

;16. Write a function that produces the linear list of all atoms of a given list, from all levels, and written in the same order. Eg.: (((A B) C) (D E)) --> (A B C D E)

(defun linear(L)
	(cond
		((atom L) (list L))
		(t (apply #'append (mapcar #'rev_lst  L)))
	)
)

(write (rev_lst '(a (b (c)) (d) (e (f))) ))