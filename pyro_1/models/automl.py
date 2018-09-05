import pandas as pd
from models.algorithm_tools import get_error_metrics
from auto_ml import Predictor


def automl_model(self):
    """auto machine learning model

    Process
    -------
    1. define which cols are categoricals, which column is the target column and which column is a date column
    2. train the algorithm
    3. predict

    Returns
    -------
    A vector with the results that came from the test DataFrame and INBOUND error results
    """
    #self.df_train[self.lag_col] = pd.to_datetime(self.df_train[self.lag_col])
    #self.df_test[self.lag_col] = pd.to_datetime(self.df_test[self.lag_col])

    column_descriptions = {col: 'categorical' for col in self.aggregation}
    column_descriptions.update({self.label: 'output', self.lag_col: 'date'})

    ml_predictor = Predictor(type_of_estimator='regressor',
                             column_descriptions=column_descriptions)

    ml_predictor.train(self.df_train)
    results = ml_predictor.predict(self.df_test)

    results_for_inbound_errors = ml_predictor.predict(self.df_train)

    errors = get_error_metrics(y_actual=self.df_train[self.label],
                               y_predicted=results_for_inbound_errors)

    return results, errors
