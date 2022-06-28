import numpy as np
from sklearn.svm import SVC
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score
import copy
erros = []
for i in range(30):
    err = []
    X, y = make_classification(n_samples=500,n_features=2,n_classes=2,n_redundant=0,n_clusters_per_class=1, random_state=69)
    X[:,1] = X[:,1]*100+1000
    X[0,1] = 3000
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.5)
    for scaler in [None, MinMaxScaler(),StandardScaler()]:
        if scaler != None:
            scaler.fit(X_train)
            X_train1 = scaler.transform(X_train)
            X_test1 = scaler.transform(X_test)
        else:
            X_train1 = copy.deepcopy(X_train) 
            X_test1 = copy.deepcopy(X_test)

        clf = SVC()
        clf.fit(X_train1,y_train)
        err.append(accuracy_score(y_train,clf.predict(X_train1)))
        err.append(accuracy_score(y_test,clf.predict(X_test1)))
    erros.append(err)
means_error = np.mean(erros,axis=0)
print(means_error.shape)
print("Greška bez skalirnja na Train", means_error[0])
print("Greška bez skalirnja na Test", means_error[1])
print("Greška MinMaxScaler na Train", means_error[2])
print("Greška MinMaxScaler na Test", means_error[3])
print("Greška StandardScaler na Train", means_error[4])
print("Greška StandardScaler na Test", means_error[5])