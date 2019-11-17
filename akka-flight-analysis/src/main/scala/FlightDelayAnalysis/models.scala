package FlightDelayAnalysis

case class FlightEvent(year: String,
                       month: String,
                       dayOfMonth: String,
                       dayOfWeek: String,
                       depTime: String,
                       scheduledDepTime: String,
                       arrTime: String,
                       scheduledArrTime: String,
                       uniqueCarrier: String,
                       flightNum: String,
                       tailNum: String,
                       actualElapsedMins: String,
                       crsElapsedMins: String,
                       airMins: String,
                       arrDelayMins: String,
                       depDelayMins: String,
                       originAirportCode: String,
                       destinationAirportCode: String,
                       distanceInMiles: String,
                       taxiInTimeMins: String,
                       taxiOutTimeMins: String,
                       flightCancelled: String,
                       cancellationCode: String, // (A = carrier, B = weather, C = NAS, D = security)
                       diverted: String, // 1 = yes, 0 = no
                       carrierDelayMins: String,
                       weatherDelayMins: String,
                       nasDelayMins: String,
                       securityDelayMins: String,
                       lateAircraftDelayMins: String)

case class FlightDelayRecord(year: String,
                             month: String,
                             dayOfMonth: String,
                             flightNum: String,
                             uniqueCarrier: String,
                             arrDelayMins: String) {
  override def toString = s"$year/$month/$dayOfMonth - $uniqueCarrier $flightNum - $arrDelayMins"
}
