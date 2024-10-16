import sys
import json
import pyuff
import numpy as np


def jsonalzie_list_with_complex(complex_list):
    return [{"real": possible_complex.real, "imag": possible_complex.imag} if isinstance(possible_complex, complex) else possible_complex
            for possible_complex in complex_list]

def jsonalize_set(incoming_set):
    return {k: jsonalzie_list_with_complex(v.tolist()) if isinstance(v, np.ndarray) else v
            for k, v in incoming_set.items()}


def parse_UFF(file_path):
    unv_file = pyuff.UFF(file_path)
    print(str(unv_file.get_set_types())[1:-1])
    for i in range(unv_file.get_n_sets()):
        current_set = unv_file.read_sets(i)
        jsonalizable_dict = jsonalize_set(current_set)
        print(json.dumps(jsonalizable_dict))

def test_parse_UFF():
    parse_UFF('C:\\Temp\\test_uff.uff');
test_parse_UFF()
