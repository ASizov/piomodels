from multiprocessing import cpu_count

from numpy import timedelta64
import pandas as pd
from xgboost import XGBRegressor

from models.algorithm_tools import get_error_metrics


def xgbregressor(self):
    """XGBoost regressor model

    Model Parameters
    ----------------
    max_depth : int
        Maximum tree depth for base learners.
    learning_rate : float
        Boosting learning rate (xgb's "eta")
    n_estimators : int
        Number of boosted trees to fit.
    booster: string
        Specify which booster to use: gbtree, gblinear or dart.
    gamma : float
        Minimum loss reduction required to make a further partition on a leaf node of the tree.
    min_child_weight : int
        Minimum sum of instance weight(hessian) needed in a child.
    max_delta_step : int
        Maximum delta step we allow each tree's weight estimation to be.
    subsample : float
        Subsample ratio of the training instance.
    colsample_bytree : float
        Subsample ratio of columns when constructing each tree.
    colsample_bylevel : float
        Subsample ratio of columns for each split, in each level.
    base_score:
        The initial prediction score of all instances, global bias.

    Returns
    -------
    A vector with the results that came from the test DataFrame and INBOUND error results
    """
    prm = self.model_params
    xgboost_regressor = XGBRegressor(n_jobs=cpu_count() - 1,
                                     max_depth=prm.get("max_depth"),  # Default: 3
                                     learning_rate=prm.get("learning_rate"),  # Default: 0.1
                                     n_estimators=prm.get("n_estimators"),  # Default: 100
                                     booster=prm.get("booster"),  # Default: "gbtree"
                                     gamma=prm.get("gamma"),  # Default: 0
                                     min_child_weight=prm.get("min_child_weight"),  # Default: 1
                                     max_delta_step=prm.get("max_delta_step"),  # Default: 0
                                     subsample=prm.get("subsample"),  # Default: 1
                                     colsample_bytree=prm.get("colsample_bytree"),  # Default: 1
                                     colsample_bylevel=prm.get("colsample_bylevel"),  # Default: 1
                                     base_score=prm.get("base_score")  # Default: 0.5
                                     )

    #self.df_train[self.lag_col] = (self.df_train[self.lag_col] -
                                   #self.df_train[self.lag_col].min()) / timedelta64(1, self.freq)
    #self.df_test[self.lag_col] = (self.df_test[self.lag_col] -
                                  #self.df_test[self.lag_col].min()) / timedelta64(1, self.freq)

    train_cols = list(set(self.df_train.columns) - {self.label})
    y_train = self.df_train['venta_uni']
    self.df_train1 = self.df_train.drop('venta_uni', 1)
    x_train = self.df_train1

    self.df_test1 = self.df_test.drop('venta_uni', 1)
    x_test = self.df_test1

    xgboost_regressor.fit(x_train, y_train)
    results = xgboost_regressor.predict(x_test)

    results_for_inbound_errors = xgboost_regressor.predict(x_train)

    errors = get_error_metrics(y_actual=self.df_train[self.label],
                               y_predicted=results_for_inbound_errors)

    return results, errors

