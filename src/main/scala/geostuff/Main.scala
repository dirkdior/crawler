package geostuff

import vectory._

object Main extends App {
//
//  val c = Circle(Vec2(3, 1), 2)
//  val r = AARect(Vec2(1, 1), Vec2(6, 4))
//  println(c intersects r) // true

//  val p  = ConvexPolygon(Vec2Array(Vec2(-3, 1), Vec2(-1, -2), Vec2(2, 2)))
  val p  = ConvexPolygon(
    Vec2Array(
      Vec2(28.835163116455078, 41.24160999808356),
      Vec2(28.831558227539062, 41.232638431545986),
      Vec2(28.84623527526855, 41.22327829087563),
      Vec2(28.855504989624023, 41.23851204236082),
      Vec2(28.835163116455078, 41.24160999808356)
    )
  )
//  val c1 = Circle(Vec2(-1, -4), 3)
  val c1 = Circle(Vec2(28.850914001464844, 41.231353759765625), 0.01)
  val c2 = Circle(Vec2(28.841716766357422, 41.22974395751953), 0.01)
  val c3 = Circle(Vec2(28.841964721679688, 41.227264404296875), 0.01)

  println(p intersectsMtd c1)
  println(p intersectsMtd c2)
  println(p intersectsMtd c3)
  println(Vec2(8.786789894104004, 53.07456970214844).angle)
}

object Main2 extends App {

  import org.geolatte.geom.C2D
  import org.geolatte.geom.Point
  import org.geolatte.geom.Polygon
  import org.geolatte.geom.ProjectedGeometryOperations
  import org.geolatte.geom.builder.DSL._
  import org.geolatte.geom._
  import org.geolatte.geom.crs.CoordinateReferenceSystems.WEB_MERCATOR

//  val pgo         = ProjectedGeometryOperations.Default
//  val point       = new Point(c(4.33, 53.21), WEB_MERCATOR)
//  val polygon     = new Polygon[C2D](ring[C2D](c(4.43, 53.21), c(4.44, 53.22), c(4.43, 53.21)))
//  val isPntInPoly = pgo.contains(polygon, point)

  val pgo         = ProjectedGeometryOperations.Default
  val p           = point(WEB_MERCATOR, c(28.850914001464844, 41.231353759765625))
  val poly        = polygon(
    WEB_MERCATOR,
    ring(
      c(28.835163116455078, 41.24160999808356),
      c(28.831558227539062, 41.232638431545986),
      c(28.84623527526855, 41.22327829087563),
      c(28.855504989624023, 41.23851204236082),
      c(28.835163116455078, 41.24160999808356)
    )
  )
  val isPntInPoly = pgo.contains(poly, p)

  println(isPntInPoly)
}
