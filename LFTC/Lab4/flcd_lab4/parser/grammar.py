import json
from itertools import count

class Grammar:

    def __init__(self, file_name):

        self._json_grammar = self.load_file(file_name)

        self._terminals = self.load_elements('terminal')
        self._nonterminals = self.load_elements('nonterminal')
        self._productions = self.load_productions()
        self._starting_symbol = self.load_starting_symbol()

    @property
    def terminals(self):
        return self._terminals

    @property
    def nonterminals(self):
        return self._nonterminals

    @property
    def productions(self):
        return self._productions

    @property
    def starting_symbol(self):
        return self._starting_symbol

    def find(self, name):
        element = next((element for element in self.terminals if element.name == name), False)

        if (element is False):
            element = next(element for element in self.nonterminals if element.name == name)

        return element

    def production(self, id):
        index = id - 1
        if (index < 0):
            raise IndexError

        return self.productions[id - 1]


    def load_file(self, file_name):
        with open(file_name, "r") as f:
            return json.load(f)


    def load_elements(self, type):
        elements = []

        for element_token in self._json_grammar[type]:
            element = Element(element_token, type)
            elements.append(element)

        if type == 'terminal':
            elements.append(Element('$', type))

        return elements

    def load_productions(self):
        productions = []

        for production in self._json_grammar['P']:
            lhs = production.pop(0)
            nonterminal = self.find(lhs)
            rhs = list(map(lambda token: self.find(token), production))
            production = Production(nonterminal, rhs)
            productions.append(production)
            nonterminal.productions.append(production)

        return productions

    def load_starting_symbol(self):
        nonterminal = self.find(self._json_grammar['S'])
        return nonterminal


class Element:

    def __init__(self, name, type):
        self._name = name
        self._type = type
        self._productions = []

    @property
    def name(self):
        return self._name

    @property
    def type(self):
        return self._type

    @property
    def productions(self):
        return self._productions

    def is_start_symbol_of(self, grammar):
        return self.name == grammar.starting_symbol.name

    def get_rhs_productions(self, grammar):
        rhs_productions = []

        for nonterminal in grammar.nonterminals:
            for production in nonterminal.productions:
                for element in production.rhs:
                    if self.name == element.name:
                        rhs_productions.append(production)

        return rhs_productions


class Production:

    id_generator = count(start=1)

    def __init__(self, lhs, rhs):
        self._id = next(Production.id_generator)
        self._lhs = lhs
        self._rhs = rhs

    @property
    def id(self):
        return self._id

    @property
    def lhs(self):
        return self._lhs

    @property
    def rhs(self):
        return self._rhs

    def next(self, element):
        index = 0

        while (element.name != self.rhs[index].name):
            index += 1

        index += 1
        next_element = None
        if (index < len(self.rhs)):
            next_element = self.rhs[index]
        else:
            next_element = Element('$', 'terminal')

        return next_element
