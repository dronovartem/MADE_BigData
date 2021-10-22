import java.io._

import breeze.linalg._


class ModelConfig {
    val learning_rate: Double = 0.00001
    val n_iterations: Int = 2000
    val train_size: Double = 0.8
    val model_log_path: String = "log.txt"
}

class LinearRegression(n_iterations: Int, learning_rate: Double) {
  var w: DenseVector[Double] = DenseVector[Double]()

  def mse_loss(y_pred: DenseVector[Double], y_true: DenseVector[Double]): Double = {
      val diff = y_pred - y_true
      diff:*= diff // square of difference
      diff := diff / (y_true.length : Double)  // normalize
      sum(diff)
  }

  def fit(x: DenseMatrix[Double], y: DenseVector[Double]): Unit = {
      this.w = DenseVector.zeros[Double](x.cols)
      for(_ <- 1 to this.n_iterations) {
        val preds = this.predict(x)
        // 2 * X.T * (y - X * w)  & normalize
        val grad = 2.0 * x.t * (preds - y) / (y.length : Double)
        this.w = this.w - this.learning_rate * grad
      }
  }

  def predict(X : DenseMatrix[Double]): DenseVector[Double] = {
    X * this.w
  }
}

class TrainTestSplit(test_size: Double ) {
  def split(data: DenseMatrix[Double]):
  (DenseMatrix[Double], DenseVector[Double], DenseMatrix[Double], DenseVector[Double]) = {
    val bound = scala.math.ceil(data.rows * this.test_size).toInt
    val x_train = data(0 until bound, 0 until data.cols - 1)
    val x_valid = data(bound until data.rows, 0 until data.cols - 1)
    val y_train = data(0 until bound, data.cols - 1)
    val y_valid = data(bound until data.rows, data.cols - 1)
    (x_train, y_train,x_valid, y_valid)
  }
}

object main {
  def main(args: Array[String]): Unit = {
    val cfg = new ModelConfig()

    val train_data = csvread(new File(args(0)),',', skipLines=1)
    val test_data = csvread(new File(args(1)),',', skipLines=1)

    val fout = new File(args(2))
    val pw = new PrintWriter(cfg.model_log_path)

    val split = new TrainTestSplit(cfg.train_size) //here its more like train/valid split
    var (x_train, y_train, x_valid, y_valid) = split.split(train_data)

    val model = new LinearRegression(n_iterations = cfg.n_iterations, learning_rate = cfg.learning_rate)
    model.fit(x_train, y_train)

    var preds = model.predict(x_train)
    var model_loss = model.mse_loss(preds, y_train)
    pw.write("Train loss: " + model_loss + "\n")

    preds = model.predict(x_valid)
    model_loss = model.mse_loss(preds, y_valid)
    pw.write("Train loss: " + model_loss + "\n")

    preds = model.predict(test_data)
    csvwrite(fout, preds.toDenseMatrix.t)

    pw.write("Predictions for test data were written at ".concat(args(2)))
    pw.close()
    }
}
