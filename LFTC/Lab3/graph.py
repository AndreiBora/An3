# Represent the Finite automata as a graph and
# do a breath for search
class Graph:
    def __init__(self, states, f_states):
        self._final_states = f_states
        self._dict_in = {}
        self._dict_out = {}
        self._dict_cost = {}
        for state in states:
            self._dict_in[state] = []
            self._dict_out[state] = []

    def get_nr_vertices(self):
        return len(self._dict)

    def parse_edge_out(self, state):
        return self._dict_out[state]

    def parse_edge_in(self, state):
        return self._dict_in[state]

    def is_edge(self, state1, state2):
        return state2 in self.parse_edge_out(state1)

    def is_node(self, state):
        return state in self._dict_in.keys()

    def add_edge(self, state1, state2):
        self._dict_in[state2].append(state1)
        self._dict_out[state1].append(state2)

    def add_vertex(self, state):
        self._dict_in[state] = []
        self._dict_out[state] = []

    def add_cost(self, key, cost):
        self._dict_cost[key] = cost

    def get_cost(self, state1, state2):
        return self._dict_cost[(state1, state2)]

    def bfs(self, start):
        visited = set()
        queue = []
        result = {}
        visited.add(start)
        queue.append(start)
        while queue:
            x = queue.pop(0)
            result[x] = []
            for y in self.parse_edge_out(x):
                if y not in visited:
                    # A -> aB
                    result[x].append(f'{self.get_cost(x,y)}{y}')
                    if y in self._final_states:
                        # A -> a
                        result[x].append(f'{self.get_cost(x,y)}')
                    visited.add(y)
                    queue.append(y)
                else:
                    result[x].append(f'{self.get_cost(x,y)}{y}')
                    if y in self._final_states:
                        # A -> a
                        result[x].append(f'{self.get_cost(x,y)}')
                    # build production

        # if start state is final state add epsilon
        if start in self._final_states:
            result[start].append(f'eps')
        return result
