/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package sensordata

import scala.annotation.switch

final case class Metric(var deviceId: java.util.UUID, var timestamp: java.time.Instant, var name: String, var value: Double) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(java.util.UUID.randomUUID, java.time.Instant.now, "", 0.0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        deviceId.toString
      }.asInstanceOf[AnyRef]
      case 1 => {
        timestamp.toEpochMilli
      }.asInstanceOf[AnyRef]
      case 2 => {
        name
      }.asInstanceOf[AnyRef]
      case 3 => {
        value
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.deviceId = {
        value match {
          case (chars: java.lang.CharSequence) => {
            java.util.UUID.fromString(chars.toString)
          }
        }
      }.asInstanceOf[java.util.UUID]
      case 1 => this.timestamp = {
        value match {
          case (l: Long) => {
            java.time.Instant.ofEpochMilli(l)
          }
        }
      }.asInstanceOf[java.time.Instant]
      case 2 => this.name = {
        value.toString
      }.asInstanceOf[String]
      case 3 => this.value = {
        value
      }.asInstanceOf[Double]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Metric.SCHEMA$
}

object Metric {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Metric\",\"namespace\":\"sensordata\",\"fields\":[{\"name\":\"deviceId\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"timestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"double\"}]}")
}