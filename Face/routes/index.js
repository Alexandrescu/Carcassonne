var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res) {
  res.render('layout');
});

/* Partials */
router.get('/index', function(req, res) {
  res.render('index');
});

router.get('/gamePartial', function(req, res) {
  res.render('game');
});

/* Resources */
var tiles = require('../config/tiles');
router.get('/tile/:id', function(req, res) {
  res.send(tiles[req.params.id]);
});

/* Testing */
router.get('/testTile', function(req, res) {
  res.render('tileTest');
});

module.exports = router;
