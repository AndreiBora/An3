from lista.list2 import *


def compute_even_prod(list):
    if is_empty(list):
        return 1
    elif get_first(list) % 2 == 0:
        return get_first(list) * compute_even_prod(sublist(list))
    else:
        return compute_even_prod(sublist(list))


def wrapper_compute_even_prod(list):
    if is_empty(list):
        return -1
    else:
        return compute_even_prod(list)


def insert_elem(list, elem, m):
    if is_empty(list) and m > 1:
        return create_empty()
    elif is_empty(list) and m == 1:
        new_list = create_empty()
        return push_front(new_list, elem)
    elif m == 1:
        return push_front(list, elem)
    else:
        new_list_2 = sublist(list)
        new_list_3 = insert_elem(new_list_2, elem, m - 1)
        return push_front(new_list_3, get_first(list))


'''
After value e we insert e1
'''


def insert_new_value(list, e, e1):
    if is_empty(list):
        return create_empty()
    elif get_first(list) == e:
        r = push_front(sublist(list), e1)
        return push_front(r, e)
    else:
        new_list_2 = sublist(list)
        new_list_3 = insert_new_value(new_list_2, e, e1)
        return push_front(new_list_3, get_first(list))


def test_inclusion_elem(e, list):
    if is_empty(list):
        return False
    if get_first(list) == e:
        return True
    return test_inclusion_elem(e, sublist(list))

def is_set(list):
    if is_empty(list):
        return True
    if test_inclusion_elem(get_first(list),sublist(list)) == True:
        return False
    return is_set(sublist(list))

def get_nr_distinct(list):
    if is_empty(list):
        return 0
    elif test_inclusion_elem(get_first(list), sublist(list)) == False:
        return 1 + get_nr_distinct(sublist(list))
    else:
        return get_nr_distinct(sublist(list))


def get_length(list):
    if is_empty(list):
        return 0
    else:
        return 1 + get_length(sublist(list))


def test_inclusion_list(list1, list2):
    if is_empty(list1):
        return True
    elif not test_inclusion_elem(get_first(list1), list2):
        return False
    return test_inclusion_list(sublist(list1), list2)


def test_intersection_list(list1, list2, result):
    if is_empty(list1):
        return result
    elif test_inclusion_elem(get_first(list1), list2):
        new_result = push_front(result, get_first(list1))
        return test_intersection_list(sublist(list1), list2, new_result)
    else:
        return test_intersection_list(sublist(list1), list2, result)


def test_intersection_list_wrapper(list1, list2):
    return test_intersection_list(list1, list2, create_empty())


def get_gcd(n1, n2):
    if n2 == 0:
        return n1
    else:
        return get_gcd(n2, n1 % n2)


def get_gcd_list(list):
    # test if we have 2 elems
    if is_empty(sublist(sublist(list))):
        return get_gcd(get_first(list), get_first(sublist(list)))
    else:
        first = get_first(list)
        return get_gcd(first, get_gcd_list(sublist(list)))


def push_back(list, e):
    if is_empty(list):
        return push_front(create_empty(), e)
    else:
        new_list = sublist(list)
        new_list_2 = push_back(new_list, e)
        return push_front(new_list_2, get_first(list))


def concatenate_list(list1, list2):
    if is_empty(list2):
        return list1
    else:
        new_list = push_back(list1, get_first(list2))
        return concatenate_list(new_list, sublist(list2))


def test_even_list(list):
    if is_empty(list):
        return True
    elif is_empty(sublist(list)):
        return False
    else:
        return test_even_list(sublist(sublist(list)))


def test_occurences_elem(list, elem):
    if is_empty(list):
        return 0
    elif get_first(list) == elem:
        return 1 + test_occurences_elem(sublist(list), elem)
    else:
        return test_occurences_elem(sublist(list), elem)


def test_list_equality(list1, list2):
    return test_inclusion_list(list1, list2) and test_inclusion_list(list2, list1)


def get_lcm(n1, n2):
    if n1 == 0 or n2 == 0:
        return 0
    else:
        return (n1 * n2) // get_gcd(n1, n2)


def get_lcm_list(list):
    if is_empty(list):
        return 1
    lcm = get_lcm_list(sublist(list))
    return (get_first(list) * lcm) // get_gcd(get_first(list), lcm)


def substitute_elem(list, old_elem, new_elem, result):
    if is_empty(list):
        return result
    elif get_first(list) == old_elem:
        new_result = push_front(result, new_elem)
        return substitute_elem(sublist(list), old_elem, new_elem, new_result)
    else:
        new_result = push_front(result, get_first(list))
        return substitute_elem(sublist(list), old_elem, new_elem, new_result)


def substitute_elem_wrapper(list, old_elem, new_elem):
    return substitute_elem(list, old_elem, new_elem, create_empty())


def invert_list(list, result):
    if is_empty(list):
        return result
    return invert_list(sublist(list), push_front(result, get_first(list)))


def invert_list_wrapper(list):
    return invert_list(list, create_empty())

def get_max_list(list):
    if is_empty(sublist(list)):
        return get_first(list)
    max = get_max_list(sublist(list))
    if max > get_first(list):
        return max
    else:
        return get_first(list)

if __name__ == '__main__':
    '''
    Compute product of even numbers
    '''
    lst = create_list()
    # lst2 = create_list()
    # print(wrapper_compute_even_prod(lst))

    '''
    Insert elem on a given position
    '''
    # l = insert_elem(lst, 9, 2)
    # print_list(l)

    '''
    3. a. Check if a list is a set.
    '''
    # print(is_set(lst))

    '''
    3. Determine the number of distinct elements from a list.
    '''
    print(get_nr_distinct(lst))


    '''
    4. a. Determine if a list has even number of elements, without computing the length of the list.
    '''
    # print(test_even_list(lst))
    '''
    4. b. Delete all occurrences of an element e from a list.
    '''
    # print(test_occurences_elem(lst,9))
    '''
    5.a Determine the greatest common divisors of elements from a list.
    '''
    # print(get_gcd_list(lst))
    '''
    5.b Insert an element on the n-position in a list.
    '''
    # print_list(insert_elem(lst, 9, 2))

    '''
    6.a Add an element at the end of a list.
    '''
    # print_list(push_back(lst, 88))

    '''
    6.b Concatenate two lists
    '''
    # print_list(concatenate_list(lst,lst2))

    '''
    7.a Test the equality of two lists.
    '''
    # print(test_list_equality(lst,lst2))

    '''
    7.b Determine the intersection of two sets represented as lists.
    '''
    # print_list(test_intersection_list_wrapper(lst,lst2))

    '''
    8. a. Determine the lowest common multiple of the elements from a list.
    '''
    # print(get_lcm_list(lst))

    '''
     8. b. Substitute in a list, all occurrence of a value e with a value e1.
    '''
    # print_list(substitute_elem_wrapper(lst,2,9))

    '''
    9. a Invert a list
    '''
    # print_list(invert_list_wrapper(lst))

    '''
    9 b. Determine the maximum element of a numerical list.
    '''
    # print(get_max_list(lst))

    '''    
    11. a. Determine if a certain element is member in a list.
    '''
    # print(test_inclusion_elem(2,lst))
    '''
    11 b. Determine the length of a list.
    '''
    # print(get_length(lst))
    '''
    12. a. Test the inclusion of two lists
    '''
    # print(test_inclusion_list(lst, lst2))
    '''
    12.b Insert in a list, after value e, a new value e1.
    '''
    # print_list(insert_new_value(lst, 2, 3))
