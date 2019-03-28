class ParseUtil:
    def __init__(self, grammar):
        self._grammar = grammar
        self._first_table = self.construct_first_table()
        self._follow_table = self.construct_follow_table()
        self._parse_table = self.construct_parse_table()

    @property
    def parse_table(self):
        return self._parse_table


    def construct_first_table(self):
        first_table = {}

        for terminal in self._grammar.terminals:
            first_table[terminal.name] = [(terminal.name, 0)]

        for nonterminal in self._grammar.nonterminals:
            first_table[nonterminal.name] = []
            for production in nonterminal.productions:
                if production.rhs[0].type == 'terminal':
                    first_table[nonterminal.name].append((production.rhs[0].name, production.id))

        next_iteration = True
        while (next_iteration):
            next_iteration = False

            for nonterminal in self._grammar.nonterminals:
                for production in nonterminal.productions:
                    try:
                        added = self.union(first_table[nonterminal.name], self.length_1_concatenation(first_table, production))
                    except KeyError as conflict:
                        print (f"nonterminal: {nonterminal.name} ", conflict)
                        raise

                    if (added > 0):
                        next_iteration = True

        return first_table


    def union(self, set1, set2, type='first'):
        added = 0

        for element_to_add in set2:
            already_exists = False
            for element in set1:
                if (element_to_add[0] == element[0]):
                    already_exists = True

                    if (type != 'follow' and element_to_add[1] != element[1]):
                        raise KeyError(f"first: {element[0]}")

            if (not already_exists):
                set1.append(element_to_add)
                added += 1

        return added


    def contains(self, set, name):
        for element in set:
            if (element[0] == name):
                return True

        return False


    def length_1_concatenation(self, first_table, production):
        rhs = production.rhs

        firsts = []
        index = 0
        next_element = True
        while (next_element and index < len(rhs)):
            next_element = False
            element_firsts = first_table[rhs[index].name]
            for element_first in element_firsts:
                if (element_first[0] == '$'):
                    next_element = True
                else:
                    if (element_first[0] not in firsts):
                        firsts.append(element_first[0])

            if (next_element):
                index += 1

        if next_element is True:
            firsts.append('$')


        return list(map(lambda element: (element, production.id), firsts))


    def construct_follow_table(self):
        follow_table = {}

        for nonterminal in self._grammar.nonterminals:
            follow_table[nonterminal.name] = []
        follow_table[self._grammar.starting_symbol.name].append(('$', 0))

        next_iteration = True
        while(next_iteration):
            next_iteration = False

            for nonterminal in self._grammar.nonterminals:
                rhs_productions = nonterminal.get_rhs_productions(self._grammar)
                for production in rhs_productions:
                    next_element = production.next(nonterminal)

                    to_add = self._first_table[next_element.name]
                    added = self.union(follow_table[nonterminal.name], to_add, type='follow')
                    if (self.contains(to_add, '$')):
                        added += self.union(follow_table[nonterminal.name], follow_table[production.lhs.name], type='follow')

                    if (added > 0):
                        next_iteration = True

        return follow_table


    def construct_parse_table(self):
        parse_table = {}

        for terminal in self._grammar.terminals:
            parse_table[terminal.name] = {terminal.name: 'pop'}
        parse_table['$'] = {'$': 'acc'}

        for nonterminal in self._grammar.nonterminals:
            parse_table[nonterminal.name] = {}
            empty_rhs = None
            for first in self._first_table[nonterminal.name]:
                production_rhs = self._grammar.production(first[1]).rhs
                rhs_tokens = list(map(lambda element: element.name, production_rhs))
                if (first[0] in parse_table[nonterminal.name]):
                    print(f"nonterminal: {nonterminal.name}  first: {first[0]} curr_prod: {parse_table[nonterminal.name][first[0]][1]}  new_prod: {first[1]}")
                    raise KeyError('Parse table conflict')
                parse_table[nonterminal.name][first[0]] = [rhs_tokens, first[1]]

                if (len(rhs_tokens) == 1 and rhs_tokens[0] == '$'):
                    empty_rhs = [rhs_tokens, first[1]]

            if (empty_rhs is not None):
                for follow in self._follow_table[nonterminal.name]:
                    if (follow[0] in parse_table[nonterminal.name] and parse_table[nonterminal.name][follow[0]][1] != empty_rhs[1]):  # follow overwriting first???
                        print(f"nonterminal: {nonterminal.name}  first: {follow[0]} curr_prod: {parse_table[nonterminal.name][follow[0]][1]}  new_prod: {empty_rhs[1]}")
                        raise KeyError('Parse table conflict')
                    parse_table[nonterminal.name][follow[0]] = empty_rhs


        return parse_table
