/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package sensordata

import scala.annotation.switch

final case class InvalidMetric(var metric: Metric, var error: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(new Metric, "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        metric
      }.asInstanceOf[AnyRef]
      case 1 => {
        error
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.metric = {
        value
      }.asInstanceOf[Metric]
      case 1 => this.error = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = InvalidMetric.SCHEMA$
}

object InvalidMetric {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"InvalidMetric\",\"namespace\":\"sensordata\",\"fields\":[{\"name\":\"metric\",\"type\":{\"type\":\"record\",\"name\":\"Metric\",\"fields\":[{\"name\":\"deviceId\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"timestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"double\"}]}},{\"name\":\"error\",\"type\":\"string\"}]}")
}