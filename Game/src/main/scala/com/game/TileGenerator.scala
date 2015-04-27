package com.game

import com.tile._

object TileGenerator {
  //(Monastery("A", GrassEdge(0), RoadEdge(0, 0, 0),GrassEdge(0), GrassEdge(0)), 2),
  def A : Tile = {
    val a1 = new GrassSection(1)
    val a2 = new MonasterySection(2)
    val a3 = new RoadSection(3)

    val up = GrassEdge(a1)
    val down = RoadEdge(a1, a3, a1)
    val left = GrassEdge(a1)
    val right = GrassEdge(a1)

    Monastery("A", up, down, left, right, a2)
  }

  def B : Tile = {
    val b1 = new GrassSection(1)
    val b2 = new MonasterySection(2)

    val up = GrassEdge(b1)
    val down = GrassEdge(b1)
    val left = GrassEdge(b1)
    val right = GrassEdge(b1)

    Monastery("B", up, down, left, right, b2)
  }

  def C : Tile = {
    val c1 = new CitySection(1)

    val up = CityEdge(c1)
    val down = CityEdge(c1)
    val left = CityEdge(c1)
    val right = CityEdge(c1)

    BannerTile("C", up, down, left, right)
  }

  def D : Tile = {
    val d1 = new GrassSection(1)
    val d2 = new RoadSection(2)
    val d3 = new GrassSection(3)
    val d4 = new CitySection(4)

    val up = RoadEdge(d1, d2, d3)
    val down = RoadEdge(d3, d2, d1)
    val left = GrassEdge(d1)
    val right = CityEdge(d4)

    SimpleTile("D", up, down, left, right)
  }

  def E : Tile = {
    val e1 = new GrassSection(1)
    val e2 = new CitySection(2)

    val up = CityEdge(e2)
    val down = GrassEdge(e1)
    val left = GrassEdge(e1)
    val right = GrassEdge(e1)

    SimpleTile("E", up, down, left, right)
  }

  def F : Tile = {
    val f1 = new GrassSection(1)
    val f2 = new CitySection(2)
    val f3 = new GrassSection(3)

    val up = GrassEdge(f3)
    val down = GrassEdge(f1)
    val left = CityEdge(f2)
    val right = CityEdge(f2)

    BannerTile("F", up, down, left, right)
  }

  def G : Tile = {
    val g1 = new GrassSection(1)
    val g2 = new CitySection(2)
    val g3 = new GrassSection(3)

    val up = CityEdge(g2)
    val down = CityEdge(g2)
    val left = GrassEdge(g1)
    val right = GrassEdge(g3)

    SimpleTile("G", up, down, left, right)
  }

  def H : Tile = {
    val h1 = new CitySection(1)
    val h2 = new GrassSection(2)
    val h3 = new CitySection(3)

    val up = GrassEdge(h2)
    val down = GrassEdge(h2)
    val left = CityEdge(h1)
    val right = CityEdge(h3)

    SimpleTile("H", up, down, left, right)
  }

  def I : Tile = {
    val i1 = new GrassSection(1)
    val i2 = new CitySection(2)
    val i3 = new CitySection(3)

    val up = GrassEdge(i1)
    val down = CityEdge(i2)
    val left = GrassEdge(i1)
    val right = CityEdge(i3)

    SimpleTile("I", up, down, left, right)
  }

  def J : Tile = {
    val j1 = new GrassSection(1)
    val j2 = new CitySection(2)
    val j3 = new RoadSection(3)
    val j4 = new GrassSection(4)

    val up = CityEdge(j2)
    val down = RoadEdge(j4, j3, j1)
    val left = GrassEdge(j1)
    val right = RoadEdge(j1, j3, j4)

    SimpleTile("J", up, down, left, right)
  }

  def K : Tile = {
    val k1 = new GrassSection(1)
    val k2 = new RoadSection(2)
    val k3 = new GrassSection(3)
    val k4 = new CitySection(4)

    val up = RoadEdge(k1, k2, k3)
    val down = GrassEdge(k3)
    val left = RoadEdge(k3, k2, k1)
    val right = CityEdge(k4)

    SimpleTile("K", up, down, left, right)
  }

  def L : Tile = {
    val l1 = new GrassSection(1)
    val l2 = new RoadSection(2)
    val l3 = new GrassSection(3)
    val l4 = new RoadSection(4)
    val l5 = new RoadSection(5)
    val l6 = new GrassSection(6)
    val l7 = new CitySection(7)

    val up = RoadEdge(l1, l4, l6)
    val down = RoadEdge(l6, l5, l3)
    val left = RoadEdge(l3, l2, l1)
    val right = CityEdge(l7)

    SimpleTile("L", up, down, left, right)
  }

  def M : Tile = {
    val m1 = new CitySection(1)
    val m2 = new GrassSection(2)

    val up = CityEdge(m1)
    val down  = GrassEdge(m2)
    val left = CityEdge(m1)
    val right = GrassEdge(m2)

    BannerTile("M", up, down, left, right)
  }

  def N : Tile = {
    val n1 = new CitySection(1)
    val n2 = new GrassSection(2)

    val up = CityEdge(n1)
    val down  = GrassEdge(n2)
    val left = CityEdge(n1)
    val right = GrassEdge(n2)

    SimpleTile("N", up, down, left, right)
  }

  def O : Tile = {
    val o1 = new CitySection(1)
    val o2 = new GrassSection(2)
    val o3 = new RoadSection(3)
    val o4 = new GrassSection(4)

    val up = CityEdge(o1)
    val down  = RoadEdge(o4, o3, o2)
    val left = CityEdge(o1)
    val right = RoadEdge(o2, o3, o4)

    BannerTile("O", up, down, left, right)
  }

  def P : Tile = {
    val p1 = new CitySection(1)
    val p2 = new GrassSection(2)
    val p3 = new RoadSection(3)
    val p4 = new GrassSection(4)

    val up = CityEdge(p1)
    val down  = RoadEdge(p4, p3, p2)
    val left = CityEdge(p1)
    val right = RoadEdge(p2, p3, p4)

    SimpleTile("P", up, down, left, right)
  }

  def Q : Tile = {
    val q1 = new CitySection(1)
    val q2 = new GrassSection(2)

    val up = CityEdge(q1)
    val down = GrassEdge(q2)
    val left = CityEdge(q1)
    val right = CityEdge(q1)

    BannerTile("Q", up, down, left, right)
  }

  def R : Tile = {
    val r1 = new CitySection(1)
    val r2 = new GrassSection(2)

    val up = CityEdge(r1)
    val down = GrassEdge(r2)
    val left = CityEdge(r1)
    val right = CityEdge(r1)

    SimpleTile("R", up, down, left, right)
  }

  def S : Tile = {
    val s1 = new CitySection(1)
    val s2 = new GrassSection(2)
    val s3 = new RoadSection(3)
    val s4 = new GrassSection(4)

    val up = CityEdge(s1)
    val down = RoadEdge(s4, s3, s2)
    val left = CityEdge(s1)
    val right = CityEdge(s1)

    BannerTile("S", up, down, left, right)
  }

  def T : Tile = {
    val t1 = new CitySection(1)
    val t2 = new GrassSection(2)
    val t3 = new RoadSection(3)
    val t4 = new GrassSection(4)

    val up = CityEdge(t1)
    val down = RoadEdge(t4, t3, t2)
    val left = CityEdge(t1)
    val right = CityEdge(t1)

    BannerTile("T", up, down, left, right)
  }

  def U : Tile = {
    val u1 = new GrassSection(1)
    val u2 = new RoadSection(2)
    val u3 = new GrassSection(3)

    val up = RoadEdge(u1, u2, u3)
    val down = RoadEdge(u3, u2, u1)
    val left = GrassEdge(u1)
    val right = GrassEdge(u3)

    SimpleTile("U", up, down, left, right)
  }

  def V : Tile = {
    val v1 = new GrassSection(1)
    val v2 = new RoadSection(2)
    val v3 = new GrassSection(3)

    val up = GrassEdge(v1)
    val down = RoadEdge(v1, v2, v3)
    val left = RoadEdge(v3, v2, v1)
    val right = GrassEdge(v1)

    SimpleTile("V", up, down, left, right)
  }

  def W : Tile = {
    val w1 = new GrassSection(1)
    val w2 = new RoadSection(2)
    val w3 = new GrassSection(3)
    val w4 = new RoadSection(4)
    val w5 = new RoadSection(5)
    val w6 = new GrassSection(6)

    val up = GrassEdge(w1)
    val down = RoadEdge(w6, w4, w3)
    val left = RoadEdge(w3, w2, w1)
    val right = RoadEdge(w1, w5, w6)

    SimpleTile("W", up, down, left, right)
  }

  def X : Tile = {
    val x1 = new GrassSection(1)
    val x2 = new RoadSection(2)
    val x3 = new GrassSection(3)
    val x4 = new RoadSection(4)
    val x5 = new RoadSection(5)
    val x6 = new GrassSection(6)
    val x7 = new RoadSection(7)
    val x8 = new GrassSection(8)

    val up = RoadEdge(x1, x4, x6)
    val down = RoadEdge(x8, x5, x3)
    val left = RoadEdge(x3, x2, x1)
    val right = RoadEdge(x6, x7, x8)

    SimpleTile("X", up, down, left, right)
  }
}
