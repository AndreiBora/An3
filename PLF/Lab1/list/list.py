from copy import deepcopy


class Node:
    def __init__(self, elem):
        self.elem = elem
        self.next = None


class List:
    def __init__(self):
        self.head = None

    def traverse(self):
        index = 0
        while self.head != None:
            head = self.head
            yield head, index
            index += 1
            self.head = head.next


def create_empty():
    return List()


def is_empty(list):
    if list.head == None:
        return True
    return False


def get_first(list):
    if is_empty(list):
        return None
    return list.head.elem


def sublist(list):
    new_list = List()
    if not is_empty(list):
        next_node = deepcopy(list.head.next)
        new_list.head = next_node
        return new_list
    return list

def push_front(list,elem):
    node = Node(elem)
    node.next = deepcopy(list.head)
    new_list = create_empty()
    new_list.head = node
    return new_list

def create_rec_list():
    x = int(input("x = "))
    if x == 0:
        return None
    else:
        node = Node(x)
        node.next = create_rec_list()
        return node


def create_list():
    list = List()
    list.head = create_rec_list()
    return list


def print_rec_list(node):
    if node != None:
        print(node.elem)
        print_rec_list(node.next)


def print_list(list):
    print_rec_list(list.head)
