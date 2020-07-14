import tensorflow as tf

graph_def_file = "./frozen_model.pb"
input_arrays = ["my_input/X"]
output_arrays = ["my_output/Softmax"]

converter = tf.lite.TFLiteConverter.from_frozen_graph(
  graph_def_file, input_arrays, output_arrays)

tflite_model = converter.convert()

open("converted_model.tflite", "wb").write(tflite_model)