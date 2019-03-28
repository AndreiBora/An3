class Node:
    '''
    The implementation of a binary tree
    '''
    node_repr =""
    def __init__(self, data=None):

        self.left = None
        self.right = None
        self.data = data


    def insert(self, data):
        '''
        Add a new node in the binary tree in the
        appropriate positon(left or right of the current leaf)
        :param data:
        :return:
        '''
        # If the new node contain the same value
        # it is not stored in the tree

        if self.data == None:
            self.data = data

        if self.data:
            if data < self.data:
                if self.left is None:
                    self.left = Node(data)
                else:
                    self.left.insert(data)
            elif data > self.data:
                if self.right is None:
                    self.right = Node(data)
                else:
                    self.right.insert(data)
        else:
            self.data = data

    # Print the tree
    def print_tree(self):
        '''
            Returns a string representation of the
            Binary tree content
        :return:
        '''
        if self.left:
            self.left.print_tree()
        # print("data = {} left={} right={}".format(self.data, self.left.data if self.left else None,
        #                                          self.right.data if self.right else None))
        Node.node_repr += "{:4} {:6} {:6}\n".format(self.data, self.left.data if self.left else str(None),
                                                               self.right.data if self.right else str(None))
        if self.right:
            self.right.print_tree()

    def get_tree_repr(self):
        Node.node_repr = ""
        self.print_tree()
        return Node.node_repr
