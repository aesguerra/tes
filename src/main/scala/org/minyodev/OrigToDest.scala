package org.minyodev

import com.google.maps.model.DirectionsResult
import com.google.maps.{DirectionsApi, GeoApiContext}

import scala.collection.JavaConversions._

/**
  * A program that will determine information of going to one place to another
  */
object OrigToDest {

  def main(args: Array[String]): Unit = {

    val context: GeoApiContext = new GeoApiContext()
    context.setApiKey("AIzaSyBlw8S1lyoOf8YniWQREPtpg2UeOPFUVeA")
    // context.setEnterpriseCredentials("", "") // For premium customers only ;D

    val origin = "Greenbelt Mall, Legazpi Street, Makati, Kalakhang Maynila, Philippines"
    val destination = "Sun Life Center, 5th Avenue, Rizal Drive, Bonifacio Global City, Taguig, 1634 Metro Manila, Philippines"

    val result: DirectionsResult =
      DirectionsApi.getDirections(context, origin, destination).await()

    result.geocodedWaypoints.toList.foreach(geoWaypoint => {
      println("Geocoder Place ID: [" + geoWaypoint.placeId + "]")
      println("Geocoder Status: [" + geoWaypoint.geocoderStatus + "]")
    })

    println()

    result.routes.toList.foreach(r => {
      val legs = r.legs.toList.map { l =>
        "\tStart Address: " + l.startAddress + " to End Address: " + l.endAddress + "\n" +
        "\tDistance: " + l.distance + "\n" +
        "\tDeparture Time: " + l.departureTime + "\n" +
        "\tArrival Time: " + l.arrivalTime + "\n" +
        "\tDuration: " + l.duration + "\n" +
        "\tDuration (in traffic): " + l.durationInTraffic
      } mkString("\n")

      val polyline = r.overviewPolyline.decodePath().toList.map(ll => "\t" + ll.lat + "," + ll.lng + "").mkString("\n")

      println("Summary: [" + r.summary + "]")
      println("North East: [" + r.bounds.northeast.lat + "," + r.bounds.northeast.lng + "]")
      println("South West: [" + r.bounds.southwest.lat + "," + r.bounds.southwest.lng + "]")
      println("Legs: [\n" +  legs + "]")
      println("Polyline: [\n" + polyline + "]")
    })
  }
}