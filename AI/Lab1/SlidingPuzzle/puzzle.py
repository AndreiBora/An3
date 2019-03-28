import heapq
from copy import deepcopy
from time import time

'''
For a given puzzle of n x n squares with numbers from 1 to ( n x n-1 ) (one square is
empty) in an initial configuration, find a sequence of movements for the numbers in order to
reach a final given configuration, knowing that a number can move (horizontally or vertically) on
an adjacent empty square. In Figure 7 are presented two examples of puzzles (with the initial
and final configuration).
'''


class State:
    def __init__(self, values):
        self.values = values
        self.size = 0
        self.similarity = 0

    def next_config(self):
        pass

    def get_bank_pos(self):
        '''
        :return: position of the blank
        '''
        for i in range(len(self.values)):
            for j in range(len(self.values[i])):
                if self.values[i][j] == 0:
                    return i, j

    def swap(self, i1, j1, i2, j2):
        new_state = deepcopy(self)
        new_state.values[i1][j1], new_state.values[i2][j2] = new_state.values[i2][j2], new_state.values[i1][j1]
        return new_state

    def get_next_config(self):
        '''
        :return: list of possible configurations
        '''
        next_config = []
        line, col = self.get_bank_pos()
        # down
        if line > 0:
            next_config.append(self.swap(line, col, line - 1, col))
        # up
        if line < self.size - 1:
            next_config.append(self.swap(line, col, line + 1, col))
        # left
        if col < self.size - 1:
            next_config.append(self.swap(line, col, line, col + 1))
        # right
        if col > 0:
            next_config.append(self.swap(line, col, line, col - 1))
        return next_config

    def __str__(self):
        str1 = ""
        for i in self.values:
            for j in i:
                str1 += str(j) + " "
            str1 += "\n"
        return str1

    def __eq__(self, other):
        return self.values == other.values

    def __hash__(self):
        return hash(str(self.values))

    def __lt__(self, other):
        return self.similarity > other.similarity


class Problem:
    def __init__(self):
        self.__init_state = State([])
        self.__final_state = State([])

    def expand(self, state):
        return state.get_next_config()

    def read_from_file(self, file):
        try:
            f = open(file, "r")
            size = int(f.readline().strip())
            self.__init_state.size = size
            self.__final_state.size = size
            line1 = [int(i) for i in f.readline().strip().split()]
            for i in range(0, size):
                lst = line1[i * size:i * size + size]
                self.__init_state.values.append(lst)

            line2 = [int(i) for i in f.readline().strip().split()]
            for i in range(0, size):
                lst = line2[i * size:i * size + size]
                self.__final_state.values.append(lst)

        except IOError as e:
            print(e)
        finally:
            f.close()

    def heuristic(self, state):
        count = 0
        for i in range(len(state.values)):
            for j in range(len(state.values[i])):
                if self.__final_state.values[i][j] == state.values[i][j]:
                    count += 1
        return count

    def get_init_state(self):
        return deepcopy(self.__init_state)

    def get_final_state(self):
        return deepcopy(self.__final_state)


class Controller:
    def __init__(self, problem):
        self.__problem = problem

    def get_path(self, sol):
        sol2 = []
        x = deepcopy(problem.get_final_state())
        while x is not None:
            sol2.append(x)
            x = sol[x]

        sol2.reverse()
        for l in sol2:
            print(l)

    def bfs(self):
        start = problem.get_init_state()
        final = problem.get_final_state()
        queue = [start]
        visited = set()
        visited.add(start)
        prev = {start: None}
        while queue:
            x = queue.pop(0)
            if x == final:
                return prev
            children = problem.expand(x)
            for i in children:
                if i not in visited:
                    visited.add(i)
                    queue.append(i)
                    prev[i] = x

    def gbbfs(self):
        start = problem.get_init_state()
        final = problem.get_final_state()
        final.similarity = final.size * len(final.values) - 1
        start.similarity = problem.heuristic(start)
        queue = [start]
        heapq.heapify(queue)
        visited = set()
        visited.add(start)
        prev = {start: None}
        while queue:
            x = heapq.heappop(queue)
            if x == final:
                return prev
            children = problem.expand(x)
            for i in children:
                if i not in visited:
                    i.similarity = problem.heuristic(i)
                    visited.add(i)
                    heapq.heappush(queue, i)
                    prev[i] = x


class UI:
    def __init__(self, ctrl):
        self.__ctrl = ctrl

    def run(self):
        self.print_menu()

        while True:
            cmd = input("Choose: ")
            if cmd == '1':
                start_time = time()
                sol = ctrl.bfs()
                print('execution time: ', time() - start_time, 'seconds')
                ctrl.get_path(sol)
            elif cmd == '2':
                start_time = time()
                sol = ctrl.gbbfs()
                print('execution time: ', time() - start_time, 'seconds')
                ctrl.get_path(sol)
            elif cmd == '0':
                return
            else:
                print("Wrong option")
                return

    def print_menu(self):
        menu = "Menu:\n 1.BFS\n 2.GBFS \n 0.Exit\n"
        print(menu)


if __name__ == '__main__':
    problem = Problem()
    problem.read_from_file("3.txt")
    ctrl = Controller(problem)
    ui = UI(ctrl)
    ui.run()
