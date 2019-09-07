package ChicagoCrime

import org.apache.spark.sql.types.{StringType, StructField, StructType}

object Schemas {

  val crime = StructType(
    List(
      StructField("ID", StringType, nullable = true),
      StructField("Case Number", StringType, nullable = true),
      StructField("Date", StringType, nullable = true),
      StructField("Block", StringType, nullable = true),
      StructField("IUCR", StringType, nullable = true),
      StructField("Primary Type", StringType, nullable = true),
      StructField("Description", StringType, nullable = true),
      StructField("Location Description", StringType, nullable = true),
      StructField("Arrest", StringType, nullable = true),
      StructField("Domestic", StringType, nullable = true),
      StructField("Beat", StringType, nullable = true),
      StructField("District", StringType, nullable = true),
      StructField("Ward", StringType, nullable = true),
      StructField("Community Area", StringType, nullable = true),
      StructField("FBI Code", StringType, nullable = true),
      StructField("X Coordinate", StringType, nullable = true),
      StructField("Y Coordinate", StringType, nullable = true),
      StructField("Year", StringType, nullable = true),
      StructField("Updated On", StringType, nullable = true),
      StructField("Latitude", StringType, nullable = true),
      StructField("Longitude", StringType, nullable = true),
      StructField("Location", StringType, nullable = true),
      StructField("Historical Wards 2003-2015", StringType, nullable = true),
      StructField("Zip Codes", StringType, nullable = true),
      StructField("Community Areas", StringType, nullable = true),
      StructField("Census Tracts", StringType, nullable = true),
      StructField("Wards", StringType, nullable = true),
      StructField("Boundaries - ZIP Codes", StringType, nullable = true),
      StructField("Police Districts", StringType, nullable = true),
      StructField("Police Beats", StringType, nullable = true)
    )
  )

  val communityArea = StructType(
    List(
      StructField("CHGOCA", StringType, nullable = true),
      StructField("ZCTA5", StringType, nullable = true),
      StructField("TOT2010", StringType, nullable = true)
    )
  )
}
