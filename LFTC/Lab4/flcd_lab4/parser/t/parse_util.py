import unittest

from parser.grammar import *
from parser.parse_util import *

class TestParseUtilMethods(unittest.TestCase):
    def setUp(self):
        self.grammar = Grammar('grammars/expression_cfg.json')
        self.parse_util = ParseUtil(self.grammar)

    def tearDown(self):
        Production.id_generator = count(start=1)


    def test_union(self):
        set1 = [('a', 1), ('b', 2)]
        set2 = [('cc', 3), ('d', 2)]

        added = self.parse_util.union(set1, set2)
        self.assertEqual(added, 2)

        expected_set = [('a', 1), ('b', 2), ('cc', 3), ('d', 2)]
        for index in range(len(expected_set)):
            self.assertEqual(set1[0], expected_set[0])
            self.assertEqual(set1[1], expected_set[1])


        set1 = [('a', 1), ('b', 2)]
        set2 = [('a', 3), ('b', 4), ('e', 5)]

        added = self.parse_util.union(set1, set2, type='follow')
        self.assertEqual(added, 1)

        expected_set = [('a', 1), ('b', 2), ('e', 5)]
        for index in range(len(expected_set)):
            self.assertEqual(set1[0], expected_set[0])
            self.assertEqual(set1[1], expected_set[1])


    def test_contains(self):
        self.assertTrue(self.parse_util.contains(self.parse_util._first_table['S'], 'a'))
        self.assertFalse(self.parse_util.contains(self.parse_util._first_table['S'], '$'))
        self.assertTrue(self.parse_util.contains(self.parse_util._first_table['D'], '('))
        self.assertTrue(self.parse_util.contains(self.parse_util._first_table['D'], 'a'))
        self.assertFalse(self.parse_util.contains(self.parse_util._first_table['D'], '+'))
        self.assertTrue(self.parse_util.contains(self.parse_util._first_table['C'], '$'))


    def test_length_1_concatenation(self):
        first_table = {
            'A': [('a', 1), ('$', 2)],
            'B': [('b', 3), ('$', 4)],
            'C': [('c', 5), ('$', 6)]
        }

        production = Production('S', [Element('A', 'nonterminal'), Element('B', 'nonterminal'), Element('C', 'nonterminal')])
        firsts = self.parse_util.length_1_concatenation(first_table, production)

        expected_firsts = [('a', production.id), ('b', production.id), ('c', production.id), ('$', production.id)]

        for index in range(len(expected_firsts)):
            self.assertEqual(firsts[index][0], expected_firsts[index][0])
            self.assertEqual(firsts[index][1], expected_firsts[index][1])


        first_table = {
            'A': [('a', 1), ('$', 2)],
            'B': [('b', 3)],
            'C': [('c', 5), ('$', 6)]
        }

        production = Production('S', [Element('A', 'nonterminal'), Element('B', 'nonterminal'), Element('C', 'nonterminal')])
        firsts = self.parse_util.length_1_concatenation(first_table, production)

        expected_firsts = [('a', production.id), ('b', production.id)]

        for index in range(len(expected_firsts)):
            self.assertEqual(firsts[index][0], expected_firsts[index][0])
            self.assertEqual(firsts[index][1], expected_firsts[index][1])


    def test_first_table(self):
        table = self.parse_util._first_table

        expected_table = {
            'S': ['(', 'a'],
            'A': ['+', '$'],
            'B': ['(', 'a'],
            'C': ['*', '$'],
            'D': ['(', 'a'],
            '+': ['+'],
            '$': ['$'],
            '*': ['*'],
            '(': ['('],
            ')': [')'],
            'a': ['a']
        }

        for element in table.keys():
            self.assertEqual(len(table[element]), len(expected_table[element]))
            for first in table[element]:
                self.assertTrue(first[0] in expected_table[element])


    def test_follow_table(self):
        table = self.parse_util._follow_table

        expected_table = {
            'S': ['$', ')'],
            'A': ['$', ')'],
            'B': ['+', ')', '$'],
            'C': ['+', ')', '$'],
            'D': ['+', ')', '*', '$'],
        }

        for element in table.keys():
            self.assertEqual(len(table[element]), len(expected_table[element]))
            for follow in table[element]:
                self.assertTrue(follow[0] in expected_table[element])


    def test_parse_table(self):
        table = self.parse_util._parse_table

        expected_table = {
            'S': {
                '(': (['B', 'A'], 1),
                'a': (['B', 'A'], 1)
            },
            'A': {
                '+': (['+', 'S'], 2),
                ')': (['$'], 3),
                '$': (['$'], 3)
            },
            'B': {
                '(': (['D', 'C'], 4),
                'a': (['D', 'C'], 4)
            },
            'C': {
                '+': (['$'], 6),
                '*': (['*', 'B'], 5),
                ')': (['$'], 6),
                '$': (['$'], 6)
            },
            'D': {
                '(': (['(', 'S', ')'], 7),
                'a': (['a'], 8)
            }
        }
        for terminal in ['+', '*', '(', ')', 'a']:
            expected_table[terminal] = {terminal: 'pop'}
        expected_table['$'] = {'$': 'acc'}

        self.assertEqual(len(expected_table.keys()), len(table.keys()))
        for element in table.keys():
            self.assertEqual(len(expected_table[element].keys()), len(table[element].keys()))
            for first in table[element].keys():
                self.assertEqual(len(table[element][first][0]), len(expected_table[element][first][0]))
                for index in range(len(expected_table[element][first][0])):
                    self.assertEqual(table[element][first][0][index], expected_table[element][first][0][index])
                self.assertEqual(table[element][first][1], expected_table[element][first][1])


if __name__ == '__main__':
    unittest.main()
