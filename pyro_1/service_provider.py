"""This is an instance that is named and serves as a service for Orax backend"""
import logging
import pandas as pd
import Pyro4
from data_preparations import lagged_data_preparation
from models.automl import automl_model
from models.xgboost_model import xgbregressor

logging.basicConfig(level=logging.INFO)


supported_models = {"automl": {"model": automl_model,
                               "data_preparation": lagged_data_preparation},
                    "xgbregressor": {"model": xgbregressor,
                                     "data_preparation": lagged_data_preparation}}


@Pyro4.expose
@Pyro4.behavior("single")
class ServiceProvider(object):
    def __init__(self):
        self.df = pd.DataFrame()
        self.df_train = pd.DataFrame()
        self.df_test = pd.DataFrame()
        self.label = None
        self.lag_col = None
        self.aggregation = None
        self.freq = None
        self.lag = None
        self.model_params = {}
        self.model_name = "automl"
        self.errors = {}

    def _import_data(self, csv_path):
        """Read from MongoDB"""
        df = pd.read_csv(csv_path)
        #logging.info("{0} Data rows imported".format(len(df)))
        return df

    def _prepare_data(self):
        """Prepares data with generalized or custom preparation"""
        logging.info("Preparing data...")
        data_preparation_process = supported_models[self.model_name]["data_preparation"]
        train, test = data_preparation_process(self)
        return train, test

    def _train_and_predict_model(self):
        """Train in a threaded way the models"""
        #logging.info("Training data...")
        model = supported_models[self.model_name]["model"]
        result_vector, errors = model(self)
        print('hahaha')
        return result_vector, errors

    def _export_results(self):
        """Return both metrics and results from every model"""
        #logging.info("Exporting data...")
        self.df_test.to_csv("resultados.csv")

    @Pyro4.expose
    def first_interaction(self, csv_path, label, lag_col, aggregation, freq, lag, model_params, model_name):
        """Import, prepare, train, evaluate, export"""
        self.df = "this will change"
        self.label = label
        self.lag_col = lag_col
        self.aggregation = aggregation
        self.freq = freq
        self.lag = lag
        self.model_params = model_params
        self.model_name = model_name

        # Import the data to the class
        self.df = self._import_data(csv_path)

        # Prepare the data and split it
        #self.df_test = self._prepare_data()
        #self.df_test = self._prepare_data()
        self.df_train = pd.read_csv('iter10_train.csv')
        self.df_test = pd.read_csv('iter10_test.csv')
        
        # Get the results now that we know all the params
        self.df_test["prediction"], self.errors = self._train_and_predict_model()
        # Export them to csv (for now)
        self._export_results()

        # Your purpose has finished, daemon
        self.kill_yourself()

    @Pyro4.oneway
    def kill_yourself(self):
        """Apoptosis"""
        #logging.info("Inflicting Harakiri... not yet, honorable samurai")

