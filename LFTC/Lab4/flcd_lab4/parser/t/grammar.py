import unittest

from parser.grammar import *

class TestGrammarMethods(unittest.TestCase):
    def setUp(self):
        self.grammar = Grammar('grammars/grammar_test_cfg.json')

    def tearDown(self):
        Production.id_generator = count(start=1)

    def test_terminals(self):
        expected_terminals = list(map(lambda token: Element(token, 'terminal'), ['a', 'b', 'c', '$']))
        self.assertEqual(len(self.grammar.terminals), 4)
        for index in range(len(self.grammar.terminals)):
            self.assertEqual(self.grammar.terminals[index].name, expected_terminals[index].name)

    def test_nonterminals(self):
        expected_nonterminals = list(map(lambda token: Element(token, 'nonterminal'), ['S', 'T']))
        self.assertEqual(len(self.grammar.nonterminals), 2)
        for index in range(len(self.grammar.nonterminals)):
            self.assertEqual(self.grammar.nonterminals[index].name, expected_nonterminals[index].name)

    def test_productions(self):
        self.assertEqual(len(self.grammar.productions), 4)

        production = self.grammar.productions[0]
        self.assertEqual(production.id, 1)
        self.assertEqual(production.rhs[0].name, 'a')
        self.assertEqual(production.rhs[1].name, 'S')
        self.assertEqual(production.rhs[2].name, 'T')

        production = self.grammar.productions[1]
        self.assertEqual(production.id, 2)
        self.assertEqual(production.rhs[0].name, 'b')
        self.assertEqual(production.rhs[1].name, 'S')

        nonterminal = self.grammar.find('S')
        production = nonterminal.productions[0]
        self.assertEqual(production.id, 1)
        self.assertEqual(production.rhs[0].name, 'a')
        self.assertEqual(production.rhs[1].name, 'S')
        self.assertEqual(production.rhs[2].name, 'T')

        production = nonterminal.productions[1]
        self.assertEqual(production.id, 4)
        self.assertEqual(production.rhs[0].name, 'c')

        nonterminal = self.grammar.find('T')
        production = nonterminal.productions[0]
        self.assertEqual(production.id, 2)
        self.assertEqual(production.rhs[0].name, 'b')
        self.assertEqual(production.rhs[1].name, 'S')

        production = nonterminal.productions[1]
        self.assertEqual(production.id, 3)
        self.assertEqual(production.rhs[0].name, '$')


    def test_find(self):
        terminal = self.grammar.find('a')
        self.assertEqual(terminal.name, 'a')
        self.assertEqual(terminal.type, 'terminal')

        nonterminal = self.grammar.find('S')
        self.assertEqual(nonterminal.name, 'S')
        self.assertEqual(nonterminal.type, 'nonterminal')

        self.assertRaises(StopIteration, self.grammar.find, 'x')

    def test_production(self):
        production = self.grammar.production(1)
        self.assertEqual(len(production.rhs), 3)
        self.assertEqual(production.rhs[0].name, 'a')
        self.assertEqual(production.rhs[1].name, 'S')
        self.assertEqual(production.rhs[2].name, 'T')

        production = self.grammar.production(3)
        self.assertEqual(len(production.rhs), 1)
        self.assertEqual(production.rhs[0].name, '$')

        self.assertRaises(IndexError, self.grammar.production, 0)
        self.assertRaises(IndexError, self.grammar.production, 5)


    def test_starting_symbol(self):
        nonterminal = Element('S', 'nonterminal')
        self.assertTrue(nonterminal.is_start_symbol_of(self.grammar))

    def test_get_rhs_productions(self):
        nonterminal = self.grammar.find('S')
        rhs_productions = nonterminal.get_rhs_productions(self.grammar)

        S = Element('S', 'nonterminal')
        T = Element('T', 'nonterminal')
        a = Element('a', 'terminal')
        b = Element('b', 'terminal')
        expected_rhs_productions = [Production(S, [a, S, T]), Production(T, [b, S])]

        for index in range(len(expected_rhs_productions)):
            self.assertEqual(rhs_productions[index].lhs.name, expected_rhs_productions[index].lhs.name)
            for inner_index in range(len(expected_rhs_productions[index].rhs)):
                self.assertEqual(rhs_productions[index].rhs[inner_index].name, expected_rhs_productions[index].rhs[inner_index].name)

    def test_next(self):
        nonterminal = self.grammar.find('S')
        production = nonterminal.productions[0]
        next_element = production.next(Element('S', 'nonterminal'))
        self.assertEqual(next_element.name, 'T')

        nonterminal = self.grammar.find('T')
        production = nonterminal.productions[0]
        next_element = production.next(Element('S', 'nonterminal'))
        self.assertEqual(next_element.name, '$')


if __name__ == '__main__':
    unittest.main()
