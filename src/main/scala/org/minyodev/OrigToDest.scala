package org.minyodev

import com.google.maps.model.DirectionsResult
import com.google.maps.{DirectionsApi, GeoApiContext}

import scala.collection.JavaConversions._

/**
  * A program that will determine information of going from one place to another
  *
  * @author aesguerra
  * @since  2017-08-16
  */
object OrigToDest {

  /**
    * Get information regarting the trip's routes, distance, time of arrival, etc
    * @param context Google Map API's GeoApiContext
    * @param origin A place where a person originates
    * @param destination A place where a person is going
    * */
  def getDirectionInfo(context: GeoApiContext, origin: String, destination: String): Unit = {

    val result: DirectionsResult =
      DirectionsApi.getDirections(context, origin, destination).await()
    val geoWayPoints = result.geocodedWaypoints.toList.map { geoWaypoint =>
      "\tPlace ID: " + geoWaypoint.placeId + "\n" +
      "\tGeocoder Status: " + geoWaypoint.geocoderStatus + "\n" +
      "\tPartial Match: " + geoWaypoint.partialMatch + "\n" +
      "\tTypes: " + geoWaypoint.types.toList.map(t => t.toString).mkString("/")
    } mkString("\n")

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

      println("Origin: " + origin)
      println("Destination: " + destination)
      println("Summary: " + r.summary)
      println("North East: [" + r.bounds.northeast.lat + "," + r.bounds.northeast.lng + "]")
      println("South West: [" + r.bounds.southwest.lat + "," + r.bounds.southwest.lng + "]")
      println("Legs:\n" +  legs)
      println("Geocoded Way Points:\n" + geoWayPoints)
      println("Polyline:\n" + polyline)
    })

  }

  def main(args: Array[String]): Unit = {

    val context: GeoApiContext = new GeoApiContext()
    context.setApiKey("AIzaSyBlw8S1lyoOf8YniWQREPtpg2UeOPFUVeA")
    // context.setEnterpriseCredentials("", "") // For premium customers only ;D

    val origin = "Greenbelt Mall, Legazpi Street, Makati, Kalakhang Maynila, Philippines"
    val destination = "Sun Life Center, 5th Avenue, Rizal Drive, Bonifacio Global City, Taguig, 1634 Metro Manila, Philippines"

    getDirectionInfo(context, origin, destination)
  }
}