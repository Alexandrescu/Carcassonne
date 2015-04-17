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

module.exports = router;
