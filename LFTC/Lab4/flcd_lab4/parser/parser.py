import sys
from pathlib import PurePath
import json
from pprint import pprint
from collections import deque
from copy import copy

from grammar import Grammar
from parse_util import ParseUtil


def hardcoded_parse_table():
    parse_table = {}

    # Example 3.3 from "Metode de proiectare a compilatoarelor" pag 61

    parse_table['S'] = {'if': [['if', 'C', 'then', 'I', 'T'], 1]}
    parse_table['T'] = {'else': [['else'], 3]}
    parse_table['T']['$'] = [['eps'], 2]
    parse_table['C'] = {'id': [['E', 'R', 'E'], 4]}
    parse_table['C']['const'] = [['E', 'R', 'E'], 4]
    parse_table['E'] = {}
    parse_table['E']['id'] = [['id'], 5]
    parse_table['E']['const'] = [['const'], 6]
    parse_table['R'] = {}
    parse_table['R']['='] = [['='], 7]
    parse_table['R']['<>'] = [['<>'], 8]
    parse_table['I'] = {}
    parse_table['I']['id'] = [['id', ':=', 'E'], 9]
    parse_table['if'] = {'if': 'pop'}
    parse_table['then'] = {'then': 'pop'}
    parse_table['else'] = {'else': 'pop'}
    parse_table['id'] = {'id': 'pop'}
    parse_table['const'] = {'const': 'pop'}
    parse_table[':='] = {':=': 'pop'}
    parse_table['='] = {'=': 'pop'}
    parse_table['<>'] = {'<>': 'pop'}
    parse_table['$'] = {'$': 'acc'}

    return parse_table


def syntactic_analysis(input_seq, parse_tbl, start_symbol):
    go = True
    w_stack = deque(['$', start_symbol])
    input_seq.append('$')
    input_stack = deque(input_seq[::-1])
    output = []
    output_deriv_str = []
    try:
        while go:
            ws_top = w_stack[-1]
            is_top = input_stack[-1]

            prod = parse_tbl[ws_top][is_top]

            # if working stack top is a non terminal we push
            if isinstance(prod, list):
                non_terminal = w_stack.pop()
                if (len(prod[0]) > 1 or prod[0][0] != '$'):
                    w_stack.extend(prod[0][::-1])
                output.append(prod[1])
                output_deriv_str.append({non_terminal: prod[0]})
            elif parse_tbl[ws_top][is_top] == 'pop':
                w_stack.pop()
                input_stack.pop()
            elif parse_tbl[ws_top][is_top] == 'acc':
                go = False
                print("sequence accepted")

    except KeyError:
        print("Error: Sequence not accepted")
		print(f'Error at symbol {len(input_seq) - len(input_stack) + 1}')
        raise
    except Exception as e:
        print(e)

    return output, output_deriv_str


def construct_deriv_str(input_seq, grammar):
    input_seq_cpy = deque(input_seq)
    start = [next(iter(input_seq[0]))]
    # start = copy(input_seq[0]['S'])
    result = [" ".join(start)]

    for production in input_seq:
        for i in start:
            # if is nonterminal
            if grammar.find(i).type == 'nonterminal':
                index = start.index(i)
                expand_symbol = get_expand(i, input_seq_cpy)
                if (len(expand_symbol) == 1 and expand_symbol[0] == '$'):
                    expand_symbol = []
                new_start = start[:index] + expand_symbol + start[index + 1:]
                input_seq_cpy.popleft()
                result.append(" ".join(new_start))
                start = new_start
                break

    return result


def get_expand(symbol, input_seq):
    for elem in input_seq:
        # get the key
        val = next(iter(elem))
        if val == symbol:
            return elem[symbol]


def parse_table(grammar_path):
    current_path = PurePath()
    grammar_path = current_path / grammar_file
    parse_table_path = current_path / 'parse_tables' / grammar_path.name
    parse_table = None

    try:
        with open(parse_table_path, 'r') as f:
            parse_table_json = f.read()
            parse_table = json.loads(parse_table_json)

            print('read parse table from file')
    except FileNotFoundError:
        grammar = Grammar(str(grammar_path))
        parse_table = ParseUtil(grammar).parse_table

        print('generated parse table')

        parse_table_json = json.dumps(parse_table)
        with open(parse_table_path, 'w') as f:
            f.write(parse_table_json)

    return parse_table

def input_sequence(input_sequence_file):
    with open(input_sequence_file, 'r') as f:
        input_sequence = f.readline().split()

        return input_sequence


if __name__ == '__main__':
    # ht = hardcoded_parse_table()
    # input_seq = ['if', 'id', '=', 'id', 'then', 'id', ':=', 'const']
    # out1, out2 = syntactic_analysis(input_seq, ht, 'S')
    # print(f'Production string:\n {out1}')
    # deriv_str = construct_deriv_str(out2)
    #
    # print(f'Production string:\n {deriv_str}')
    #
    # # pprint derivation string
    # print("Derivation string")
    # print("\n->".join(deriv_str))


    if (len(sys.argv) != 3):
        print("Usage: parser.py grammar_file input_sequence_file");
        sys.exit()
    grammar_file, input_sequence_file = sys.argv[1:3]

    parse_table = parse_table(grammar_file)
    input_sequence = input_sequence(input_sequence_file)
    grammar = Grammar(grammar_file)

    _, productions_string = syntactic_analysis(input_sequence, parse_table, grammar.starting_symbol.name)

    derivations_string = construct_deriv_str(productions_string, grammar)
    print('Derivation string: ')
    print("\n".join(derivations_string))
