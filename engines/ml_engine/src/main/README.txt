1 - no lags, only labels(731.2172692221162 - MAE,2671406.1553386445 - RMSE)
2 - combinations of lags(1592.8892323163207 - MAE,7183237.766472619 - RMSE)
3 - 1,2,4,8 month lags(511.73875233756326 - MAE,1968560.013164348 - RMSE)
4 - 0.5,1,2,3,4 month lags(381.02948690976757 - MAE,1617955.2483923258 - RMSE)
6 - Popularity of each kind, prices(~373.3807009514467 - MAE,1520236.0213096293 - RMSE) 

--------------------------
Random Forests(MLLIB) 
MSE = 739356.064922 
RMSE = 859.858165584
MAE = 293.585475911
numTrees=40, featureSubsetStrategy="auto",impurity='variance', maxDepth=30, maxBins=32
--------------------------
Gradient-Boosted
MSE = 713509.272859
RMSE = 844.694780888
MAE = 294.362072014
numIterations=22,maxDepth=7,maxBins=19
------------------------------
