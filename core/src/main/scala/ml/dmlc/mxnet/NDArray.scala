package ml.dmlc.mxnet

import ml.dmlc.mxnet.Base._

object NDArray {
  def _plus(array1: NDArray, array2: NDArray, out: NDArray = null): NDArray = ???
  def _plusScalar(array: NDArray, number: Double, out: NDArray = null): NDArray = ???

  /**
    Return a new empty handle.

    Empty handle can be used to hold result

    Returns
    -------
    a new empty ndarray handle
  */
  def _new_empty_handle(): NDArrayHandle = {
    // TODO
    var hdl: NDArrayHandle = new NDArrayHandle
    checkCall(_LIB.mxNDArrayCreateNone(hdl))
    hdl
  }

  def main(args: Array[String]): Unit = {
    System.loadLibrary("mxnet-scala")
    val ndArrayHandle: NDArrayHandle = _new_empty_handle()
    println(ndArrayHandle.handler)
  }
}

/**
 * NDArray object in mxnet.
 * NDArray is basic ndarray/Tensor like data structure in mxnet.
 */
class NDArray(val handle: NDArrayHandle, val writable: Boolean = true) {
  /*
  override def finalize() = {
    checkCall(_LIB.mxNDArrayFree(handle))
  }
  */

  def +(other: NDArray): NDArray = {
    NDArray._plus(this, other)
  }

  def +(other: Double): NDArray = {
    NDArray._plusScalar(this, other)
  }

  def +=(other: NDArray): NDArray = {
    if (!writable) {
      throw new IllegalArgumentException("trying to add to a readonly NDArray")
    }
    NDArray._plus(this, other, out = this)
  }

  def +=(other: Double): NDArray = {
    if (!writable) {
      throw new IllegalArgumentException("trying to add to a readonly NDArray")
    }
    NDArray._plusScalar(this, other, out = this)
  }

  /**
    Return a copied numpy array of current array.

    Returns
    -------
    array : numpy.ndarray
        A copy of array content.
  */
  def asArray(): Array[Array[Double]] = {
    // TODO
    val (nRows, nCols) = shape()
    val data = Array.ofDim[Double](nRows, nCols)
    /* TODO
    checkCall(_LIB.mxNDArraySyncCopyToCPU(
      self.handle,
      data.ctypes.data_as(mx_float_p),
      ctypes.c_size_t(data.size)))
      */
    data
  }

  /**
    Get shape of current NDArray.

    Returns
    -------
    a tuple representing shape of current ndarray
  */
  def shape(): (Int, Int) = ???
}

object NDArrayConversions {
  implicit def int2Scalar(x: Int): NDArrayConversions[Int] = new NDArrayConversions(x)
  implicit def double2Scalar(x: Double): NDArrayConversions[Double] = new NDArrayConversions(x)
  implicit def float2Scalar(x: Float): NDArrayConversions[Float] = new NDArrayConversions(x)
}

class NDArrayConversions[@specialized(Int, Float, Double) V](val value: V) {
  def +(other: NDArray): NDArray = {
    other + value.asInstanceOf[Double]
  }
}