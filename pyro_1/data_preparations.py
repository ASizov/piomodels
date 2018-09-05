from numpy import array
from data_tools import prepare_lag_data
from models.static_info import json_of_time
import pandas as pd


def _get_lag_roll_list(min_lag, freq):
    roll_list = json_of_time.get(freq).get("values")
    if roll_list is None:
        raise Exception("'{0}' frequency is not supported in Orax yet")

    multiplier = 1
    lag_list = []
    while len(lag_list) < 1:
        lag_list = list(array(roll_list) * multiplier)
        lag_list = [x for x in lag_list if x > min_lag]
        lag_list.append(min_lag)

    return lag_list, roll_list


def lagged_data_preparation(self):
    None

    return None
