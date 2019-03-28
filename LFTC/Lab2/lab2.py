from Exception import *
import re
from binary_tree import Node


class Scanner:
    COD_TABLE_NAME = "cod_table.txt"

    def __init__(self, source_code):
        # source code of the program
        self.__source_code = source_code
        # data structure for Identifiers table
        self.__identifiers_table = Node()
        # data structure for constants
        self.__constants_table = Node()
        # data structure for codification table
        self.__cod_table = {}
        # data structure for program internal form
        self.__PIF = []
        # name of output file for constants table
        self.__constants_table_out = file_name.split(".")[0] + "_const.txt"
        # name of output file for PIF
        self.__PIF_out = file_name.split(".")[0] + "_pif.txt"
        # name of output file for Symbol table
        self.__identifiers_table_out = file_name.split(".")[0] + "_identifier.txt"

        self.__populate_cod_table()

    def __populate_cod_table(self):
        '''
        Helper method to load from file the codification table
        and store it into a hash table
        '''
        try:
            f = open(self.COD_TABLE_NAME, "r")
            line = f.readline().strip()
            while line != "":
                (symbol, code) = line.split()
                self.__cod_table[symbol] = code
                line = f.readline().strip()
        except IOError:
            raise Exception("file exception")
        finally:
            f.close()

    def __get_char(self, line):
        '''
        Return a character at a time from a given line
        :param line: a line from source code
        :return: a character from the line
        '''
        for ch in line:
            yield ch

    def __add_token(self, token):
        '''
        Save the indentifier into the PIF and identifiers table or
        if is a reserved word only in PIF
        :param token: atom representing an identifier or reserved word
        :return:
        '''
        if token in self.__cod_table:
            # it is a reserved word
            code = self.__cod_table[token]
            self.__PIF.append((code, -1))
        else:
            # it must be an identifier
            # first we add it in the identifier table
            # we store in the identifier table
            self.__identifiers_table.insert(token)
            # we add to the PIF table a pair formed by
            # code and token
            code_indent = self.__cod_table["identifier"]
            self.__PIF.append((code_indent, token))

    def __add_constant(self, const):
        '''
        Save the atom into the constant table and PIF
        :param const: The atom representing a constant
        :return:
        '''
        self.__constants_table.insert(const)
        code_const = self.__cod_table["constant"]
        self.__PIF.append((code_const, const))

    def write_to_file(self):
        '''
        Write the content of the PIF,identifiers table
        and constant table to files.
        :return:
        '''
        try:
            f_pif = open(self.__PIF_out, "w")
            f_identifiers = open(self.__identifiers_table_out, "w")
            f_constants = open(self.__constants_table_out, "w")
            # write the pif to file
            for code, pos in self.__PIF:
                str_pif = "{} {} \n".format(code, pos)
                f_pif.write(str_pif)
            # write the identifiers table to file
            f_identifiers.write("{:4} {:6} {:6}\n\n".format("Data","Left","Right"))
            f_identifiers.write(self.__identifiers_table.get_tree_repr())
            f_constants.write("{:4} {:6} {:6}\n\n".format("Data", "Left", "Right"))
            f_constants.write(self.__constants_table.get_tree_repr())
        except IOError:
            print("Failed to write the content to file")
        finally:
            f_pif.close()
            f_identifiers.close()
            f_constants.close()

    def parse(self):
        '''
        Read source code from a file and parse the file
        to identify the atoms and save the atoms in
        the appropriate table
        :return: None
        :except: FormatException
            if the atom is not a well formated(e.g 1ac for a variable ) or the
            length of an atom is > 8 the exception is thrown
        '''
        f = open(self.__source_code, "r")
        line = f.readline().strip()
        line_nr = 1
        simple_op_pattern = "^(\+|-|\*|/|%)$"
        comparison_op_pattern = "^(>|<|=|!)$"
        sop_check = re.compile(simple_op_pattern)
        cop_check = re.compile(comparison_op_pattern)

        while line != "":
            gen = self.__get_char(line)
            try:
                ch = next(gen)
                while True:
                    # if the token starts with letter
                    # it can be an identifier or reserved word
                    if ch.isalpha():
                        token = ""
                        while ch.isalnum():
                            token += ch
                            ch = next(gen)
                        if len(token) > 8:
                            err_msg = "Exception Line: " + str(line_nr) + " " + "length of the identifier is grater than 8 characters"
                            raise IdentifierLengthException(err_msg)
                        self.__add_token(token)

                    # if is space we skip the spaces
                    elif ch.isspace():
                        while ch.isspace():
                            ch = next(gen)


                    # if it begin we a digit it must be a constant
                    # or an invalid identifier
                    elif ch.isdigit():
                        const = ""
                        while ch.isalnum() or ch == ".":
                            const += ch
                            ch = next(gen)
                        pattern = "^\d+(\.\d+)?$"
                        prog = re.compile(pattern)
                        if not prog.match(const):
                            err_msg = "Exception Line: " + str(line_nr) + " " + str(const) + " is not an int or float"
                            raise FormatException(err_msg)
                        self.__add_constant(const)
                    # check if is semicolumn
                    elif sop_check.match(ch):
                        self.__add_token(ch)
                        ch = next(gen)
                    elif cop_check.match(ch):
                        _op = ch
                        _look_ahead = next(gen)
                        if _look_ahead == "=":
                            _op += _look_ahead
                            self.__add_token(_op)
                        else:
                            self.__add_token(ch)
                        if not _look_ahead.isspace():
                            self.__add_token(_look_ahead)

                        ch = next(gen)
                    # add any character that do not match the previous cases
                    else:
                        self.__add_token(ch)
                        ch = next(gen)
            except StopIteration:
                pass
            line_nr += 1
            line = f.readline().strip()


if __name__ == '__main__':
    # normal program
    # file_name = "fact.cpp"
    # constant error program
    # file_name = "fact_err_const.cpp"
    # variable length error
    file_name = "fact_err_length.cpp"
    scanner = Scanner(file_name)

    try:
        scanner.parse()
        scanner.write_to_file()
    except FormatException as ex:
        print(ex.msg)
    except IdentifierLengthException as ex:
        print(ex.msg)


