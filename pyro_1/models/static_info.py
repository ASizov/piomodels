default_parameters = {"automl": {}}

json_of_time = {"B": {
                  "description": "business day frequency [Mo, Tu, We, Th, Su, Su, Su]",
                  "values": [1, 5, 20, 260]},
                "D": {
                  "description": "calendar day frequency [daily]",
                  "values": [1, 7, 30, 365]},
                "W": {
                  "description": "weekly frequency [Sun-Sat]",
                  "values": [1, 4, 52]},
                "M": {
                  "description": "month end frequency",
                  "values": [1, 6, 12]},
                "Q": {
                  "description": "quarter end frequency",
                  "values": [1, 2, 3, 4]},
                "A": {
                  "description": "year end frequency",
                  "values": [1, 2, 5, 10]},
                "H": {
                  "description": "hourly frequency",
                  "values": [1, 2, 12, 24]},
                "T": {
                  "description": "minutely frequency",
                  "values": [1, 5, 30, 60]},
                "S": {
                  "description": "secondly frequency",
                  "values": [1, 5, 30, 60]},
                "L": {
                  "description": "milliseconds",
                  "values": [1000, 5000, 30000, 60000]},
                "U": {
                  "description": "microseconds",
                  "values": [1000, 5000, 30000, 60000]},
                "N": {
                  "description": "nanoseconds",
                  "values": [1000, 5000, 30000, 60000]}
                }
