from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score, explained_variance_score
from numpy import sqrt, fabs


def rmse(y_actual, y_predicted):
    """
    Root Mean Squared Error: 'how many [Units] am I off?'
    """
    return sqrt(mean_squared_error(y_actual, y_predicted))


def mae(y_actual, y_predicted):
    """
    Mean Absolute Error: 'How far am I from what really happened?'
    """
    return mean_absolute_error(y_actual, y_predicted)


def r2(y_actual, y_predicted):
    """
    Coefficient of determination: 'Does my model fit it's training?'
    """
    return r2_score(y_actual, y_predicted)


def variance_score(y_actual, y_predicted):
    """
    Explained variance score: 'How well does my model explain variance?'
    """
    return explained_variance_score(y_actual, y_predicted)


def mape(y_actual, y_predicted):
    """
    Mean Absolute Percentage Error: 'On a percentage, how much did I miss?'
    """
    mask = y_actual != 0  # Only the correct keys are used in the calculation
    return (fabs(y_actual - y_predicted) / y_actual)[mask].mean() * 100


def wmape(y_actual, y_predicted):
    """
    Weighted MAPE: 'mape but considering the size of the actual value'
    """
    mask = y_actual != 0
    total = y_actual.sum()

    mape_vector = (fabs(y_actual - y_predicted)[mask] * 100) / total

    return mape_vector.mean()


def get_error_metrics(y_actual, y_predicted):
    """Provide the pair of vectors and receive a dict with many errors

    Errors
    ------
    1. Root Mean Squared Error: 'how many [Units] am I off?'
    2. Mean Absolute Error: 'How far am I from what really happened?'
    3. Coefficient of determination: 'Does my model fit it's training?'
    4. Explained variance score: 'How well does my model explain variance?'
    5. Mean Absolute Percentage Error: 'On a percentage, how much did I miss?'
    6. Weighted MAPE: 'mape but considering the size of the actual value'


    Parameters
    ----------
    y_actual: Series
        Pandas Series (or any vectorized format) of the real values to predict
    y_predicted: Series
        Pandas Series (or any vectorized format) of the model's prediction

    Returns
    -------
    A dict with currently supported errors
    """

    errors = {"rmse": rmse(y_actual, y_predicted),
              "mae": mae(y_actual, y_predicted),
              "r2": r2(y_actual, y_predicted),
              "variance_score": variance_score(y_actual, y_predicted),
              "mape": mape(y_actual, y_predicted),
              "wmape": wmape(y_actual, y_predicted)}

    return errors
