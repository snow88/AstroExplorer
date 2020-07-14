import tflearn
import tensorflow as tf

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv('galaxy_star_quasar.csv')
df = df.sample(frac=1).reset_index(drop=True)

# print(df.info())

X = df
# normalizing data
for i in range(len(X)):
    X.iloc[i,2] = X.iloc[i,2]*1000
    X.iloc[i, 14] = X.iloc[i, 14] * 10000
    X.iloc[i, 16] = X.iloc[i, 16] / 100

X = X.drop(['objid','rerun','specobjid','class','fiberid'], axis=1)
y_str = df['class']

y_temp = []
for i in range(len(y_str)):
    if y_str[i] == 'GALAXY':
        y_temp.append([1,0,0])
    elif y_str[i] == 'STAR':
        y_temp.append([0,1,0])
    else:
        y_temp.append([0,0,1])

Y = pd.DataFrame(y_temp, columns = ['isgalaxy', 'isstar', 'isquasar'])

weights = tflearn.initializations.uniform(minval=0, maxval=16)

# input layer
net = tflearn.input_data(
    shape = [None, 13],
    name = 'my_input'
)

# hidden layer
net = tflearn.fully_connected(
    net,
    6,
    activation='softmax',
    weights_init=weights
)

# output layer
net = tflearn.fully_connected(
    net,
    3,
    activation = 'softmax',
    weights_init = weights,
    name = 'my_output'
)

# hyperparameter tuning
net = tflearn.regression(
    net,
    learning_rate=0.1,
    optimizer = 'adam',
    metric='accuracy',
    loss = 'categorical_crossentropy'
)

model = tflearn.DNN(net, tensorboard_verbose=0)

model.fit(X.values.tolist(), Y.values.tolist(), n_epoch=2000, batch_size=1024, show_metric=True)

# remove train ops
with net.graph.as_default():
    del tf.get_collection_ref(tf.GraphKeys.TRAIN_OPS)[:]

# save the model
model.save('galaxy_star_quasar_model.tflearn')
