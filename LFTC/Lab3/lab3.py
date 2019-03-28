import json
import copy
import pprint
from graph import Graph


class FiniteAutomata:
    def __init__(self, file_name):
        self.__fName = file_name
        self.__json_automata = {}
        self.__json_grammar = {}

        # load the automata from file
        self.__load_from_file()

    def __load_from_file(self):
        with open(self.__fName, "r") as f:
            self.__json_automata = json.load(f)

    def get_states(self):
        return self.__json_automata["Q"]

    def get_alphabet(self):
        return self.__json_automata["sigma"]

    def get_transitions(self):
        return self.__json_automata["delta"]

    def get_initial_state(self):
        return self.__json_automata["q0"]

    def get_final_states(self):
        return self.__json_automata["F"]

    def get_eq_grammar(self):
        self.__json_grammar["N"] = copy.copy(self.__json_automata["Q"])
        self.__json_grammar["sigma"] = copy.copy(self.__json_automata["sigma"])
        self.__json_grammar["S"] = copy.copy(self.__json_automata["q0"])
        # compute productions
        self.__json_grammar["P"] = self.get_productions()
        return self.__json_grammar

    def get_productions(self):
        graph = Graph(self.__json_grammar["N"], self.__json_automata["F"])
        # let build the graph
        for transition in self.__json_automata["delta"]:
            graph.add_edge(transition["from"], transition["to"])
            key = (transition["from"], transition["to"])
            graph.add_cost(key, transition["input"])
        return graph.bfs(self.__json_grammar["S"])

    def print_grammar(self, reg_grammar):
        for key, value in reg_grammar.items():
            if key != 'P':
                print(f"{key} = {value}")
            else:
                print("P = ")
                for key2, value2 in value.items():
                    if value2:
                        print(f"\t{key2}->{' | '.join(value2)}")


class RegularGrammar:

    def __init__(self, fname):
        self._filename = fname
        self._json_grammar = self.load_file()
        self._json_automata = {}

    def load_file(self):
        with open(self._filename, "r") as f:
            return json.load(f)

    def get_nonterminals(self):
        return self._json_grammar["N"]

    def get_terminals(self):
        return self._json_grammar["sigma"]

    def get_productions(self):
        return self._json_grammar["P"]

    def get_production_of_symbol(self, symbol):
        return self._json_grammar["P"][symbol]

    def print_productions(self):
        for key, value in self.get_productions().items():
            print(f'{key} -> {" | ".join(value)}')

    def print_production_of_symbol(self, symbol):
        print(f'{symbol} -> {" | ".join(self.get_production_of_symbol(symbol))}')

    def get_equivalent_automata(self):
        if not self.is_right_linear():
            raise Exception("Cannot be converted because is not right linear")

        # terminals
        self._json_automata["Q"] = copy.copy(self._json_grammar["N"])
        self._json_automata["Q"].append("k")
        # nonterminals
        self._json_automata["sigma"] = copy.copy(self._json_grammar["sigma"])
        # starting symbol
        self._json_automata["q0"] = copy.copy(self._json_grammar["S"])
        # final states
        if "eps" in self._json_grammar["P"][self._json_grammar["S"]]:
            self._json_automata["F"] = [self._json_grammar["S"], "k"]
        else:
            self._json_automata["F"] = ["k"]
        # transition function delta
        self._json_automata["delta"] = []
        for key, value in self._json_grammar["P"].items():
            for item in value:
                if len(item) == 1:
                    self._json_automata["delta"].append(
                        {
                            "from": key,
                            "to": "k",
                            "input": item
                        }
                    )
                elif len(item) == 2:
                    self._json_automata["delta"].append(
                        {
                            "from": key,
                            "to": item[1],
                            "input": item[0]
                        }
                    )
        return self._json_automata

    def is_right_linear(self):
        # check if S->eps
        s_eps = False
        if "eps" in self.get_production_of_symbol(self._json_grammar["S"]):
            s_eps = True
        for key, value in self.get_productions().items():
            # check if is right linear
            # e.g A->aB or A->a
            for p in value:
                # ex "aA"
                if len(p) > 2 and p != "eps":
                    return False
                if len(p) == 2:
                    if p[0] not in self._json_grammar["sigma"]:
                        return False
                    if p[1] not in self._json_grammar["N"]:
                        return False
                elif len(p) == 1:
                    if p not in self._json_grammar["sigma"]:
                        return False

            # check A -> eps
            if key != self._json_grammar["S"] and 'eps' in value:
                return False
            # check if S->eps and S is not on the RHS
            # if s_eps and self._json_grammar["S"] in value:
            #     return False
            if s_eps:
                start = self._json_grammar["S"]
                for v in value:
                    if len(v) == 2 and v[1] == start:
                        return False
                    if len(v) == 1 and v[0] == start:
                        return False

        return True


def printMenu():
    print("Menu:")
    print("0. Exit")
    print("Finite Automata:")
    print("1. Display states")
    print("2. Display the alphabet")
    print("3. Display the transitions")
    print("4. Display the initial state")
    print("5. Display the set of final states")
    print("6. Construct the equivalent regular grammar")
    print("Regular grammar:")
    print("7. Display terminals")
    print("8. Display non-terminals")
    print("9. Display productions")
    print("10. Display production of a symbol")
    print("11. Verify if is regular")
    print("12. Construct the equivalent finite automata")


def run():
    file_name_automata = "automata.txt"
    file_name_reg_grammar = "grammar.txt"
    automata = FiniteAutomata(file_name_automata)
    grammar = RegularGrammar(file_name_reg_grammar)
    printMenu()
    while True:
        opt = int(input('Choose an option: '))
        if opt == 1:
            print(automata.get_states())
        elif opt == 2:
            print(automata.get_alphabet())
        elif opt == 3:
            print(automata.get_transitions())
        elif opt == 4:
            print(automata.get_initial_state())
        elif opt == 5:
            print(automata.get_final_states())
        elif opt == 6:
            gr = automata.get_eq_grammar()
            automata.print_grammar(gr)
        elif opt == 7:
            print(grammar.get_terminals())
        elif opt == 8:
            print(grammar.get_nonterminals())
        elif opt == 9:
            grammar.print_productions()
        elif opt == 10:
            symbol = input("Enter the symbol")
            grammar.print_production_of_symbol(symbol)
        elif opt == 11:
            print(grammar.is_right_linear())
        elif opt == 12:
            pp = pprint.PrettyPrinter(indent=4)
            pp.pprint(grammar.get_equivalent_automata())
        elif opt == 0:
            break


if __name__ == '__main__':
    run()
