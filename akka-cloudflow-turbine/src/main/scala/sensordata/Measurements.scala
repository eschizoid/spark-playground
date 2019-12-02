/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package sensordata

import scala.annotation.switch

final case class Measurements(var power: Double, var rotorSpeed: Double, var windSpeed: Double) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0.0, 0.0, 0.0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        power
      }.asInstanceOf[AnyRef]
      case 1 => {
        rotorSpeed
      }.asInstanceOf[AnyRef]
      case 2 => {
        windSpeed
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.power = {
        value
      }.asInstanceOf[Double]
      case 1 => this.rotorSpeed = {
        value
      }.asInstanceOf[Double]
      case 2 => this.windSpeed = {
        value
      }.asInstanceOf[Double]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Measurements.SCHEMA$
}

object Measurements {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Measurements\",\"namespace\":\"sensordata\",\"fields\":[{\"name\":\"power\",\"type\":\"double\"},{\"name\":\"rotorSpeed\",\"type\":\"double\"},{\"name\":\"windSpeed\",\"type\":\"double\"}]}")
}